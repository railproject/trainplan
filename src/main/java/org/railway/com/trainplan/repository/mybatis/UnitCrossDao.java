package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.PlanCross;
import org.railway.com.trainplan.entity.UnitCross;

import java.util.List;

/**
 * Created by speeder on 2014/5/28.
 */
@MyBatisRepository
public interface UnitCrossDao {

    List<PlanCross> findPlanCross(List<String> planCrossIds);
}
