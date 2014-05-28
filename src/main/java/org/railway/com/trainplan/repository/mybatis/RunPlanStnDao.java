package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.RunPlanStn;

import java.util.List;

/**
 * Created by speeder on 2014/5/28.
 */
@MyBatisRepository
public interface RunPlanStnDao {

    int addRunPlanStn(List<RunPlanStn> list);
}
