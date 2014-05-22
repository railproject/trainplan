package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
 
@Component
@Transactional
@Monitored
public class TrainTimeService {
	
	@Autowired
	private BaseDao baseDao;
	  
	  public java.util.List<TrainTimeInfo> getTrainTimes(String trainId){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAINTIMEINFO_BY_TRAINID, trainId);
	  }

}
