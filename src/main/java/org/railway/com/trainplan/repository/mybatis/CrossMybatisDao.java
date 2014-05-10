package org.railway.com.trainplan.repository.mybatis;

import java.util.List;

import org.railway.com.trainplan.entity.CrossInfo;


@MyBatisRepository
public interface CrossMybatisDao {
	void saveCrossBatch(List<CrossInfo> crosses);
}
