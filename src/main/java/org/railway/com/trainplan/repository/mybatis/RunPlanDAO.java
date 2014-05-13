package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@MyBatisRepository
public interface RunPlanDAO {
    List<Map<String, Object>> findRunPlanByDate(String date);

    List<Map<String, Object>> findRunPlanStnByTrain(String train_id);
}
