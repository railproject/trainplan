package org.railway.com.trainplan.service;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.LevelCheck;
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
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            for(Map<String, Object> item: list) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
            }
            LevelCheck no1 = params.get(0);
            no1.setId(UUID.randomUUID().toString());
            params.add(no1);
            runPlanDao.checkLev1(params);
        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
            return -1;
        }
        return 0;
    }
}
