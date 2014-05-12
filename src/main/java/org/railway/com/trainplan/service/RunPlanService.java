package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.RunPlanDAO;
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
    private RunPlanDAO runPlanDAO;

    public List<Map<String, Object>> findRunPlan(String date) {
        return runPlanDAO.findRunPlanByDate(date);
    }

    public List<Map<String, Object>> findRunPlanStn(String train_id) {
        return runPlanDAO.findRunPlanStnByTrain(train_id);
    }
}
