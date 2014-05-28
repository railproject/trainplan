package org.railway.com.trainplan.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.entity.*;
import org.railway.com.trainplan.exceptions.*;
import org.railway.com.trainplan.repository.mybatis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
public class RunPlanService {

    private static final Log logger = LogFactory.getLog(RunPlanService.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private RunPlanStnDao runPlanStnDao;

    @Autowired
    private UnitCrossDao unitCrossDao;

    @Autowired
    private BaseTrainDao baseTrainDao;

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

    public List<Map<String, Object>> checkLev1(List<Map<String, Object>> list, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = new ArrayList<Map<String, Object>>();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            List<String> planIdList = Lists.newArrayList();
            for(Map<String, Object> item: list) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
                planIdList.add(record.getPlanId());
            }
            if(planIdList.size() > 0) {
                // 增加审核记录
                runPlanDao.addCheckHis(params);
                //修改审核状态和已审核局
                List<Map<String, Object>> planList = runPlanDao.findPlanInfoListByPlanId(planIdList);

                for(Map<String, Object> plan: planList) {

                    try {
                        // 组织返回结果对象
                        Map<String, Object> levResult = new HashMap<String, Object>();
                        levResult.put("id", MapUtils.getString(plan, "PLAN_TRAIN_ID"));

                        int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                        String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU", "");
                        int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
                        String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU", "");
                        String passBureau = MapUtils.getString(plan, "PASS_BUREAU", "");
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
                        // 本局一级审核状态
                        levResult.put("checkLev1", newLev1Type);
                        // 本局二级审核状态
                        levResult.put("checkLev2", newLev2Type);
                        // 本局一级审核是否已审核
                        levResult.put("lev1Checked", 1);
                        // 本局二级审核是否已审核
                        levResult.put("lev2Checked", 0);
                        checkLev1Result.add(levResult);
                    } catch (DailyPlanCheckException e) {
                        logger.error(e);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
        }
        return checkLev1Result;
    }

    public List<Map<String, Object>> checkLev2(List<Map<String, Object>> plans, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = new ArrayList<Map<String, Object>>();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            List<String> planIdList = Lists.newArrayList();
            for(Map<String, Object> item: plans) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
                planIdList.add(record.getPlanId());
            }
            if(planIdList.size() > 0) {
                // 增加审核记录
                runPlanDao.addCheckHis(params);
                //修改审核状态和已审核局
                List<Map<String, Object>> planList = runPlanDao.findPlanInfoListByPlanId(planIdList);

                for(Map<String, Object> plan: planList) {

                    try {
                        // 组织返回结果对象
                        Map<String, Object> levResult = new HashMap<String, Object>();
                        levResult.put("id", MapUtils.getString(plan, "PLAN_TRAIN_ID"));

                        int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                        String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU", "");
                        int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
                        String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU", "");
                        String passBureau = MapUtils.getString(plan, "PASS_BUREAU");
                        // 计算二级已审核局
                        String checkedLev2Bureau = addBureauCode(lev2Bureau, user.getBureauShortName());
                        // 计算新1级审核状态
                        int newLev2Type = newLev1Type(lev2Type, passBureau, lev2Bureau, user.getBureauShortName());
                        // 一级审核完后，二级审核需要重新审核
//                        int newLev2Type = 0;
//                        // 二级已审核局也应该是空
//                        String checkedLev2Bureau = "";
                        Map<String, Object> updateParam = new HashMap<String, Object>();
                        updateParam.put("lev1Type", lev1Type);
                        updateParam.put("lev1Bureau", lev1Bureau);
                        updateParam.put("lev2Type", newLev2Type);
                        updateParam.put("lev2Bureau", checkedLev2Bureau);
                        updateParam.put("planId", MapUtils.getString(plan, "PLAN_TRAIN_ID"));
                        runPlanDao.updateCheckInfo(updateParam);
                        // 本局一级审核状态
//                        levResult.put("checkLev1", newLev1Type);
                        // 本局二级审核状态
                        levResult.put("checkLev2", newLev2Type);
                        // 本局一级审核是否已审核
//                        levResult.put("lev1Checked", 1);
                        // 本局二级审核是否已审核
                        levResult.put("lev2Checked", 1);
                        checkLev1Result.add(levResult);
                    } catch (DailyPlanCheckException e) {
                        logger.error(e);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
        }
        return checkLev1Result;
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
        if(!passBureau.contains(split)) {
            throw new WrongBureauCheckException("计划不属于审核局");
        }
        if(current == 2) {
            throw new WrongCheckTypeException("该计划已经审核过");
        }
        if(current == 1 || current == 0) {
            result = computeLev1Type(current, passBureau, addBureauCode(currentChecked, split));
        } else {
            throw new UnknownCheckTypeException("未知审核类型");
        }
        return result;
    }

    private int computeLev1Type(int current, String passBureau, String checkedBureau) throws WrongDataException {
        if(current != 2 && isAllChecked(passBureau, checkedBureau)) {
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


    public List<PlanCross> findPlanCross() {
        try {
            return unitCrossDao.findPlanCross();
        } catch (Exception e) {
            logger.error("findUnitCross:::::", e);
        }
        return null;
    }


    public List<RunPlan> findRunPlan() {
        try {
            return baseTrainDao.findBaseTrainByPlanCrossid(null);
        } catch (Exception e) {
            logger.error("findUnitCross:::::", e);
        }
        return null;
    }

    public int generateRunPlan(List<String> planCrossIdList, String startDate, int days) {
        List<PlanCross> planCrossList = unitCrossDao.findPlanCross();
        for(PlanCross planCross: planCrossList) {
            executorService.execute(new RunPlanGenerator(planCross, runPlanDao, baseTrainDao, startDate, runPlanStnDao, days));
        }
        return planCrossList.size();
    }


    class RunPlanGenerator implements Runnable {

        // 传入参数
        private PlanCross planCross;

        // 保存客运计划用
        private RunPlanDao runPlanDao;

        private RunPlanStnDao runPlanStnDao;

        // 查询基本图数据用
        private BaseTrainDao baseTrainDao;

        private String startDate;

        private int days;

        RunPlanGenerator(PlanCross planCross, RunPlanDao runPlanDao,
                         BaseTrainDao baseTrainDao, String startDate,
                         RunPlanStnDao runPlanStnDao, int days) {
            this.planCross = planCross;
            this.runPlanDao = runPlanDao;
            this.baseTrainDao = baseTrainDao;
            this.runPlanStnDao = runPlanStnDao;
            this.startDate = startDate;
            this.days = days;
        }

        @Override
        public void run() {
            List<UnitCrossTrain> unitCrossTrainList = this.planCross.getUnitCrossTrainList();
            String planCrossId = planCross.getPlanCrossId();
            List<RunPlan> runPlanList = baseTrainDao.findBaseTrainByPlanCrossid(planCrossId);
            List<RunPlan> resultRunPlanList = Lists.newArrayList();
            // 计算循环次数
            int times = this.days / this.planCross.getGroupTotalNbr() + this.days % this.planCross.getGroupTotalNbr() > 0? 1:0;
            LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(this.startDate);
            // times循环一次就是生成一组unit_cross_train的计划
            for(int i = 0; i < times; i ++) {
                start = start.plusDays(this.planCross.getGroupTotalNbr() * i);

                // 每次循环生成一组车底的计划
                for(int n = 0; n < this.planCross.getGroupTotalNbr(); n ++ ) {
                    // 每组车的第一趟车的开始日期
                    start = start.plusDays(n);
                    // 计算每组有多少车
                    int trainNbr = unitCrossTrainList.size() / this.planCross.getGroupTotalNbr();
                    for(int m = 0; m < trainNbr; m++) {
                        UnitCrossTrain unitCrossTrain = unitCrossTrainList.get(n * trainNbr + m);
                        for(RunPlan runPlan: runPlanList) {
                            if(unitCrossTrain.getBaseTrainId().equals(runPlan.getBaseTrainId())) {
                                LocalDate trainStartDate = null;
                                // 如果不是每组车的第一趟车，就取上一趟车作为前序车
                                if(m > 0) {
                                    RunPlan preRunPlan = resultRunPlanList.get(resultRunPlanList.size() - 1);
                                    // 前后续车都有了，互基
                                    preRunPlan.setNextTrainId(runPlan.getPlanTrainId());
                                    runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                                    trainStartDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime())).plusDays(preRunPlan.getDayGap());
                                    runPlan.setRunDate(trainStartDate.toString("yyyyMMdd"));
                                } else if(m == 0) {
                                    // 每组车的第一趟车，取上一次循环中相同组数的最后一辆车作为前序车
                                    // 处理到后续车的时候再去设置前序车的后续车字段
                                    if(resultRunPlanList.size() > 0) {
                                        // 找出前序车
                                        RunPlan preRunPlan = resultRunPlanList.get(resultRunPlanList.size() - 1);
                                        // 前后续车都有了，互基
                                        preRunPlan.setNextTrainId(runPlan.getPlanTrainId());
                                        runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                                    }
                                    trainStartDate = start.plusDays(m);
                                    runPlan.setRunDate(trainStartDate.toString("yyyyMMdd"));
                                }

                                List<RunPlanStn> runPlanStnList = runPlan.getRunPlanStnList();
                                // 计算计划主表信息
                                runPlan.setPlanTrainId(UUID.randomUUID().toString());
                                runPlan.setPlanCrossId(planCrossId);

                                // unitcross里的信息
                                runPlan.setTrainNbr(unitCrossTrain.getTrainNbr());
                                runPlan.setDayGap(unitCrossTrain.getDayGap());
                                runPlan.setSpareFlag(unitCrossTrain.getSpareFlag());
                                runPlan.setSpareApplyFlag(unitCrossTrain.getSpareApplyFlag());
                                runPlan.setHighLineFlag(unitCrossTrain.getHighLineFlag());
                                runPlan.setHightLineRule(unitCrossTrain.getHighLineFlag());
                                runPlan.setCommonLineRule(unitCrossTrain.getCommonLineRule());
                                runPlan.setAppointWeek(unitCrossTrain.getAppointWeek());
                                runPlan.setAppointDay(unitCrossTrain.getAppointDay());
                                // 计算计划从表信息
                                for(RunPlanStn runPlanStn: runPlanStnList) {
                                    runPlanStn.setPlanTrainId(runPlan.getPlanTrainId());
                                    runPlanStn.setPlanTrainStnId(UUID.randomUUID().toString());
                                    runPlanStn.setArrTime(new java.sql.Date(trainStartDate.plusDays(runPlanStn.getRunDays()).toDate().getTime() + runPlanStn.getArrTime().getTime()));
                                    runPlanStn.setDptTime(new java.sql.Date(trainStartDate.plusDays(runPlanStn.getRunDays()).toDate().getTime() + runPlanStn.getDptTime().getTime()));
                                    runPlanStn.setBaseArrTime(runPlanStn.getArrTime());
                                    runPlanStn.setBaseDptTime(runPlanStn.getDptTime());
                                }
                                // 最后一个站的到站时间就是列车终到时间
                                RunPlanStn lastRunPlanStn = runPlan.getRunPlanStnList().get(runPlan.getRunPlanStnList().size() - 1);
                                runPlan.setEndDateTime(new java.sql.Date(lastRunPlanStn.getArrTime().getTime()));
                                // 处理完了加到结果列表里
                                resultRunPlanList.add(runPlan);
                                break;
                            }
                        }
                    }
                }
            }

            for(RunPlan runPlan: resultRunPlanList) {
                List<RunPlanStn> runPlanStnList = runPlan.getRunPlanStnList();
                runPlanStnDao.addRunPlanStn(runPlanStnList);
                runPlanDao.addRunPlan(runPlan);
            }
        }
    }
}
