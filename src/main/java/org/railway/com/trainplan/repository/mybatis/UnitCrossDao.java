package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.UnitCross;

import java.util.List;
import java.util.Map;

/**
 * UnitCross 操作类
 * Created by speeder on 2014/5/28.
 */
@MyBatisRepository
public interface UnitCrossDao {

    List<UnitCross> findUnitCrossBySchemaId(Map<String, Object> params);

    List<UnitCross> findUnitCrossByName(String unitCrossName);
}
