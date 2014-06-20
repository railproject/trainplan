package org.railway.com.trainplan.repository.mybatis;

import java.util.Map;

/**
 * 审核页面统计
 * Created by speeder on 2014/5/27.
 */
@MyBatisRepository
public interface ChartDao {

    Map<String, Object> getPlanTypeCount(Map<String, Object> map);

    Map<String, Object> getPlanLineCount(Map<String, Object> map);

    Map<String, Object> getLev1CheckCount(Map<String, Object> map);

    Map<String, Object> getLev2CheckCount(Map<String, Object> map);
}
