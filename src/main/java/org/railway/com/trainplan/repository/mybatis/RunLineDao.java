package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/21/14.
 */
@MyBatisRepository
public interface RunLineDao {

    List<Map<String, Object>> findRunLineById(String id);

    List<Map<String, Object>> findLineTimeTableByLineId(String lineId);
}
