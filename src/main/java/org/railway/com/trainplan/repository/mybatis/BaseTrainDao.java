package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.RunPlan;

import java.util.List;
import java.util.Map;

/**
 * Created by speeder on 2014/5/28.
 */
@MyBatisRepository
public interface BaseTrainDao {

    List<RunPlan> findBaseTrainByUnitCrossid(Map<String, Object> map);
}
