package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 与临客相关的操作
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class RunPlanLkService {

	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 查询临客的基本信息
	 * 
	 * @param reqMap，包括的字段：startDate，endDate，trainNbr，
	 * startBureau，isRelationBureau，bureau，tokenVehBureau
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RunPlan> getPlanTrainLkInfo(Map<String,Object> reqMap){
		return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_PLANTRAIN_LK_INFO, reqMap);
	}
	
	
	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RunPlanTrainDto> getTrainLkRunPlans(Map<String , Object> map) throws Exception {
		List<RunPlanTrainDto> runPlans = Lists.newArrayList();
		Map<String, RunPlanTrainDto> runPlanTrainMap = Maps.newHashMap();
		List<CrossRunPlanInfo> crossRunPlans = baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_TRAINLK_RUNPLAN, map);
		String startDay = map.get("startDate").toString();
		String endDay = map.get("endDate").toString();
		for(CrossRunPlanInfo runPlan: crossRunPlans){
				RunPlanTrainDto currTrain =  runPlanTrainMap.get(runPlan.getTrainNbr());
				if(currTrain == null){
					currTrain = new RunPlanTrainDto(startDay, endDay);
					currTrain.setTrainNbr(runPlan.getTrainNbr()); 
					runPlanTrainMap.put(runPlan.getTrainNbr(), currTrain);
				} 
				currTrain.setRunFlag(runPlan.getRunDay(), runPlan.getRunFlag());
		} 
		runPlans.addAll(runPlanTrainMap.values());   
		return runPlans;
    }
	
	
	
	 /**
	   * 通过plan_train_id从PLAN_TRAIN_STN中获取列车时刻表
	   * @param planTrainId
	   * @return
	   */
	  public List<BaseCrossTrainInfoTime> getTrainLkInfoForPlanTrainId(String planTrainId){
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_TRAINLK_FOR_PLAN_TRAIN_ID, planTrainId);
	  }
	  
	  
}
