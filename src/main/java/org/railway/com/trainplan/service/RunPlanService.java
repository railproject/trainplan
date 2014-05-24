package org.railway.com.trainplan.service;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.LevelCheck;
import org.railway.com.trainplan.exceptions.DailyPlanCheckException;
import org.railway.com.trainplan.exceptions.UnknownCheckTypeException;
import org.railway.com.trainplan.exceptions.WrongBureauCheckException;
import org.railway.com.trainplan.exceptions.WrongDataException;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
public class RunPlanService {

    private static final Log logger = LogFactory.getLog(RunPlanService.class);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private BaseDao baseDao;

    public List<Map<String, Object>> findRunPlan(String date, String bureau, int type) {
        logger.debug("findRunPlan::::");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("bureau", bureau);
//        map.put("type", type);
        return runPlanDao.findRunPlan(map);
    }

    public List<Map<String, Object>> findPlanTimeTableByPlanId(String planId) {
        logger.debug("findPlanTimeTableByPlanId::::");
        return runPlanDao.findPlanTimeTableByPlanId(planId);
    }

    public Map<String, Object> findPlanInfoByPlanId(String planId) {
        logger.debug("findPlanInfoByPlanId::::");
        return runPlanDao.findPlanInfoByPlanId(planId);
    }

    public int checkLev1(List<Map<String, Object>> list, ShiroRealm.ShiroUser user, int checkType) {
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            StringBuilder stringBuilder = new StringBuilder();
            for(Map<String, Object> item: list) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
                stringBuilder.append(record.getPlanId()).append(",");
            }
            if(stringBuilder.length() > 0) {
                // 增加审核记录
                runPlanDao.addCheckHis(params);
                //修改审核状态和已审核局
                List<Map<String, Object>> planList = runPlanDao.findPlanInfoListByPlanId(stringBuilder.substring(0, stringBuilder.length() - 1));

                for(Map<String, Object> plan: planList) {
                    int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                    String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU");
                    int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
                    String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU");
                    String passBureau = MapUtils.getString(plan, "PASS_BUREAU");
                    // 计算一级已审核局
                    String checkedLev1Bureau = addBureauCode(lev1Bureau, user.getBureauShortName());
                    // 计算新1级审核状态
                    int newLev1Type = newLev1Type(lev1Type, passBureau, lev1Bureau, user.getBureauShortName());
                    // 一级审核完后，二级审核需要重新审核
                    int newLev2Type = 0;
                    // 二级已审核局也应该是空
                    String checkedLev2Bureau = "";
                    Map<String, Object> updateParam = new HashMap<String, Object>();
                    updateParam.put("lev1Type", newLev1Type);
                    updateParam.put("lev1Bureau", checkedLev1Bureau);
                    updateParam.put("lev2Type", newLev2Type);
                    updateParam.put("lev2Bureau", checkedLev2Bureau);
                    updateParam.put("planId", MapUtils.getString(plan, "PLAN_TRAIN_ID"));
                    runPlanDao.updateCheckInfo(updateParam);
                }
            }

        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
            return -1;
        } catch (WrongDataException e) {
            logger.error("checkLev1::::::", e);
            return -1;
        } catch (DailyPlanCheckException e) {
            logger.error("checkLev1::::::", e);
            return -1;
        }
        return 0;
    }

    /**
     * 路局审核了需要修改已审核路局局码字段
     * @param base
     * @param split
     * @return
     */
    private String addBureauCode(String base, String split) throws WrongDataException {
        StringBuilder result = new StringBuilder(base);
        if(!base.contains(split)) {
            result.append(split);
        } else {
            throw new WrongDataException(split + " 局已审核过该计划，不能重复审核");
        }
        return result.toString();
    }

    private int newLev1Type(int current, String passBureau, String currentChecked, String split) throws DailyPlanCheckException {
        int result = 0;
        if(current == 0) {
            if(passBureau.contains(split)) {
                result = 1;
            } else {
                throw new WrongBureauCheckException("计划不属于审核局");
            }
        } else if(current == 1 || current == 2) {
            result = computeLev1Type(current, passBureau, addBureauCode(currentChecked, split));
        } else {
            throw new UnknownCheckTypeException("未知审核类型");
        }
        return result;
    }

    private int computeLev1Type(int current, String passBureau, String checkedBureau) throws WrongDataException {
        if(current == 1 && isAllChecked(passBureau, checkedBureau)) {
            return 2;
        }
        if(current == 1 && !isAllChecked(passBureau, checkedBureau)) {
            return 1;
        }
        if(current == 2 && isAllChecked(passBureau, checkedBureau)) {
            throw new WrongDataException("该计划已审核完毕，不能再次审核");
        }
        return 0;
    }

    /**
     * 判断是否所有路局都审核完毕
     * @param standard
     * @param token
     * @return
     */
    private boolean isAllChecked(String standard, String token) throws WrongDataException {
        StringBuilder tStandard = new StringBuilder(standard);
        if(standard.length() != token.length()) {
            return false;
        }
        if(standard.length() < token.length()) {
            throw new WrongDataException("已审核路局比途经局多，请联系管理员");
        }
        while (tStandard.length() > 0) {
            String ele = tStandard.substring(0, 1);
            tStandard = new StringBuilder(tStandard.substring(1));
            if(!token.contains(ele)) {
                return false;
            }
        }
        return true;
    }

}
