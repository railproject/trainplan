package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.HashMap;
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
	 * 通过方案id，operation查询列车和列车时刻表的总数量
	 * @param chartId 方案id
	 * @param operation 货运 or 客运
	 * @return
	 */
	public int getTrainsAndTimesCount(String chartId,String operation){
		 int count = 0;
		 Map<String,Object> reqMap = new HashMap<String,Object>();
		 reqMap.put("chartId",chartId );
		 reqMap.put("operation",operation );
		 List<Map<String,Object>> list = baseDao.selectListBySql(Constants.TRAININFO_GET_TRAINS_AND_TIMES_COUNT, reqMap);
	     if(list != null && list.size() > 0){
	    	//只有一条数据
	    	 Map<String,Object> map = list.get(0);
	    	 count = (( BigDecimal)map.get("COUNT")).intValue();
	     }
	     return count;
	}
	/**
	 * 通过方案id，operation查询列车和列车时刻表(分页)
	 * @param chartId 方案id
	 * @param operation 货运 or 客运
	 * @param rownumstart
	 * @param rownumend
	 * @return
	 */
	 public List<Map<String,Object>> getTrainsAndTimesForPage(String chartId,String operation,int rownumstart,int rownumend){
		 Map<String,Object> reqMap = new HashMap<String,Object>();
		 reqMap.put("chartId",chartId );
		 reqMap.put("operation",operation );
		 reqMap.put("rownumstart", rownumstart);
		 reqMap.put("rownumend", rownumend);
		 return baseDao.selectListBySql(Constants.TRAININFO_GET_TRAINS_AND_TIMES_FORPAGE, reqMap);
	 }
	  /**
	   * 根据方案id等信息查询列车信息列表(分页)
	   */
	  public List<PlanTrain> getTrainsForPage(Map<String, Object> params){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO_PAGE, params);
	  }
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
