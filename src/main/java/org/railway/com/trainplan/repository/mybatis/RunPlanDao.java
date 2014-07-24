package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.LevelCheck;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.UnitCross;

import java.util.List;
import java.util.Map;

/**
 * 计划dao
 * Created by star on 5/12/14.
 */
@MyBatisRepository
public interface RunPlanDao {

    /**
     * 校验列车信息
     * @param map
     * @return
     */
    List<Map<String, Object>> checkTrainInfo(Map<String, Object> map);

    /**
     * 查询客运计划时刻表数据
     * @param planId 计划id
     * @return 计划时刻表
     */
    List<Map<String, Object>> findPlanTimeTableByPlanId(String planId);

    /**
     * 查询客运计划主体信息
     * @param planId 计划id
     * @return 计划主体信息
     */
    Map<String, Object> findPlanInfoByPlanId(String planId);

    /**
     * 一级审核
     * @param list 审核列表
     * @return 审核结果
     */
    int addCheckHis(List<LevelCheck> list);

    /**
     * 更新审核状态和已审核局
     * @param map 审核信息
     * @return 更新结果
     */
    int updateCheckInfo(Map<String, Object> map);

    /**
     * 根据计划id列表查询列车信息列表
     * @param params 计划id列表
     * @return 查询结果
     */
    List<Map<String, Object>> findPlanInfoListByPlanId(List<String> params);


    int addRunPlanList(List<RunPlan> list);

    int addRunPlan(RunPlan runPlan);

    /**
     * 根据日期和担当局查询始发终到
     * @param map date: 日期，格式 yyyymmdd, bureau: 路局简称
     * @return 计划列表
     */
    List<Map<String, Object>> findRunPlan_sfzd(Map<String, Object> map);

    /**
     * 根据日期和担当局查询始发交出
     * @param map date: 日期，格式 yyyymmdd, bureau: 路局简称
     * @return 计划列表
     */
    List<Map<String, Object>> findRunPlan_sfjc(Map<String, Object> map);

    /**
     * 根据日期和担当局查询接入终到
     * @param map date: 日期，格式 yyyymmdd, bureau: 路局简称
     * @return 计划列表
     */
    List<Map<String, Object>> findRunPlan_jrzd(Map<String, Object> map);

    /**
     * 根据日期和担当局查询接入交出
     * @param map date: 日期，格式 yyyymmdd, bureau: 路局简称
     * @return 计划列表
     */
    List<Map<String, Object>> findRunPlan_jrjc(Map<String, Object> map);

    /**
     * 根据日期和担当局查询所有类型
     * @param map date: 日期，格式 yyyymmdd, bureau: 路局简称
     * @return 计划列表
     */
    List<Map<String, Object>> findRunPlan_all(Map<String, Object> map);

    /**
     * 根据plancrossid查询已生成的、group_serial_nbr值最大的所有列车的信息，用来继续生成后面的开行计划
     * @param planCrossId 外键
     * @return
     */
    List<RunPlan> findPreRunPlanByPlanCrossId(String planCrossId);

    /**
     * 删除某个时间点之后的列车
     * @param map 参数
     */
    void deleteRunPlanByStartTime(Map<String, Object> map);

    /**
     * 保存后续车id
     * @param runPlan
     */
    void updateNextTrainId(RunPlan runPlan);
}
