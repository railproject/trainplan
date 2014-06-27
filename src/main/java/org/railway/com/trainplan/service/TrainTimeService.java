package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	  
	/**
	 * 通过base_train_id查询列车时刻表，用于对数表和交路单元功能显示详点和简点
	 * @param baseTrainId 列车id
	 * @return
	 */
	  public List<TrainTimeInfo> getTrainTimes(String baseTrainId){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAINTIMEINFO_BY_TRAINID, baseTrainId);
	  }
	  
	  /**
	   * 通过plan_train_id查询列车时刻表(关系到表：plan_train和plan_train_stn)
	   * @param planTrainId  表plan_train中的字段plan_train_id
	   * @return
	   */
	  public List<TrainTimeInfo> getTrainTimeInfoByTrainId(String planTrainId){
		  return baseDao.selectListBySql(Constants.TRAININFO_GETPLANLINE_TRAINTIMEINFO_BY_TRAINID, planTrainId);
		  
	  }

	public List<TrainTimeInfo> getTrainLineTimes(String trainId) {
		 return baseDao.selectListBySql(Constants.GET_TRAINLINES_BY_TRAINLINEID, trainId); 
	}

}
