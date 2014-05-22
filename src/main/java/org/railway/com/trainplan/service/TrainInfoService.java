package org.railway.com.trainplan.service;

import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class TrainInfoService {
	@Autowired
	private BaseDao baseDao;
	  
	  public java.util.List<PlanTrain> getTrainTimes(Map<String, Object> params){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO, params);
	  }
}
