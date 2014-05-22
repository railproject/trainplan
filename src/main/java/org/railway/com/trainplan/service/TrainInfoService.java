package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.List;
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
	  
	 
	  /**
	   * 根据方案id等信息查询列车信息列表
	   */
	  public List<PlanTrain> getTrains(Map<String, Object> params){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO, params);
	  }
	  
	  /**
	   * 根据方案id等信息查询列车总数
	   */
	  public int getTrainInfoCount(Map<String,Object> params){
		  List<Map<String,Object>> countList = baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO_COUNT, params);
		  int count= 0;
		  if(countList != null && countList.size() > 0){
			  //只有一条数据
			  Map<String,Object> map = countList.get(0);
			  count = (( BigDecimal)map.get("COUNT")).intValue();
		  }
		  return count;
	  }
}
