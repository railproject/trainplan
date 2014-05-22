package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@MyBatisRepository
public interface RunPlanDao {
    /**
     * 查询审核表格数据
     * @param map
     * @return
     */
    List<Map<String, Object>> findRunPlan(Map<String, Object> map);

    /**
     * 校验列车信息
     * @param map
     * @return
     */
    List<Map<String, Object>> checkTrainInfo(Map<String, Object> map);

    /**
     * 查询客运计划时刻表数据
     * @param planId
     * @return
     */
    List<Map<String, Object>> findPlanTimeTableByPlanId(String planId);

    /**
     * 查询客运计划主体信息
     * @param planId
     * @return
     */
    Map<String, Object> findPlanInfoByPlanId(String planId);
}
