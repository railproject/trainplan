package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.RunLineDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/21/14.
 */
@Component
@Monitored
public class PlanLineService {

    private static final Log logger = LogFactory.getLog(PlanLineService.class);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private RunLineDao runLineDao;

    public int checkTrainInfo(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTrainInfo:::::::::::::::::::");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("planId", planId);
        param.put("lineId", lineId);
        List<Map<String, Object>> result = runPlanDao.checkTrainInfo(param);
        if(result.size() == 0) {
            return -1;
        }
        Map<String, Object> cells = result.get(0);
        if(cells.containsValue(0)) {
            return -1;
        }
        return 1;
    }

    public int checkTimeTable(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTimeTable::::::::::::::::");
        List<Map<String, Object>> plans = runPlanDao.findPlanTimeTableByPlanId(planId);
        List<Map<String, Object>> lines = runLineDao.findLineTimeTableByLineId(lineId);
        if(plans.size() != lines.size()) {
            return -1;
        }
        if(plans.size() == 0) {
            return -1;
        }
        // 客运计划和日计划存储的始发站-到站时间 和 终到站-出发时间 不一样，且时刻表不关心这2个时间，所以不校验。
        Map<String, Object> plan_start = plans.get(0);
        plans.remove(plan_start);
        plan_start.remove("ARR_TIME");
        plans.add(plan_start);
        Map<String, Object> line_start = lines.get(0);
        lines.remove(line_start);
        line_start.remove("ARR_TIME");
        lines.add(line_start);
        Map<String, Object> plan_end = plans.get(plans.size() - 1);
        plans.remove(plan_end);
        plan_end.remove("DPT_TIME");
        plans.add(plan_end);
        Map<String, Object> line_end = lines.get(lines.size() - 1);
        lines.remove(line_end);
        line_end.remove("DPT_TIME");
        lines.add(line_end);

        if(plans.containsAll(lines) && lines.containsAll(plans)) {
            return 1;
        }
        return -1;
    }

    public int checkRouting(String planId, String lineId) {
        return 0;
    }
}
