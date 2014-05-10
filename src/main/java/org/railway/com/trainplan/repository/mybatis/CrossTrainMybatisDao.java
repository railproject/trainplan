package org.railway.com.trainplan.repository.mybatis;

import java.util.List;

import org.railway.com.trainplan.entity.CrossTrainInfo;

@MyBatisRepository
public interface CrossTrainMybatisDao {
	
	void saveCrossTrainBatch(List<CrossTrainInfo> crosses);

}
