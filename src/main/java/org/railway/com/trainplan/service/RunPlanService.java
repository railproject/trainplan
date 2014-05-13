package org.railway.com.trainplan.service;

import org.apache.commons.collections.MapUtils;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
public class RunPlanService {

    @Autowired
    private RunPlanDao runPlanDAO;

    public List<Map<String, Object>> findRunPlan(String date, String bureau, int type) {
        Map<String, Object> map = MapUtils.EMPTY_MAP;
        map.put("date", date);
        map.put("bureau", bureau);
        map.put("type", type);
        return runPlanDAO.findRunPlan(map);
    }

    public List<Map<String, Object>> findRunPlanStn(String bureau, String train_id) {
        return runPlanDAO.findRunPlanStnByTrain(bureau, train_id);
    }
}
