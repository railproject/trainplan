package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.PlanCrossInfo;

/**
 * PlanCross操作类
 * Created by speeder on 2014/8/1.
 */
@MyBatisRepository
public interface PlanCrossDao {
    /**
     * 新增一条plancross记录
     * @param planCrossInfo 实体对象
     */
    void save(PlanCrossInfo planCrossInfo);

    /**
     * 根据plancrossid更新一条记录
     * @param planCrossInfo 实体对象
     */
    void update(PlanCrossInfo planCrossInfo);

    /**
     * 根据plancrossid删除一条记录
     * @param planCrossId 表主键
     */
    void deleteById(String planCrossId);

    /**
     * 根据unitcrossid查询一条记录，unitcross和plancross是一一对应的
     * @param unitCrossId 外键参数
     */
    void findByUnitCrossId(String unitCrossId);

    /**
     * 根据主键查询一条记录
     * @param planCrossId 表主键
     */
    void findById(String planCrossId);
}