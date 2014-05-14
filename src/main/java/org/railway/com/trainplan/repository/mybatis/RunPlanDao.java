package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@MyBatisRepository
public interface RunPlanDao {
    List<Map<String, Object>> findRunPlan(Map<String ,Object> map);

    List<Map<String, Object>> findRunPlanStnByTrain(String train_id);

    List<Map<String, Object>> findRunPlanByTrainIds(String train_ids);

    List<Map<String, Object>> findPlanLineSTNs(String train_ids);
}
