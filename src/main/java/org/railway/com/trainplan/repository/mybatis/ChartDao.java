package org.railway.com.trainplan.repository.mybatis;

import java.util.Map;

/**
 * Created by speeder on 2014/5/27.
 */
@MyBatisRepository
public interface ChartDao {

    Map<String, Object> getPlanTypeCount(String date);

    Map<String, Object> getPlanLineCount(String date);
}
