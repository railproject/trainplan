package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/21/14.
 */
@MyBatisRepository
public interface RunLineDao {

    /**
     * 查询运行线时刻表信息
     * @param lineId
     * @return
     */
    List<Map<String, Object>> findLineTimeTableByLineId(String lineId);

    /**
     * 查询日计划主体信息
     * @param lineId
     * @return
     */
    Map<String, Object> findLineInfoByLineId(String lineId);
}
