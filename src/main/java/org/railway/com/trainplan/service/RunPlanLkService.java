package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	 * <if test="startDate != null and startDate !='' ">AND RUN_DATE>=#{startDate}</if>
		<if test="endDate !=null and endDate !='' ">AND RUN_DATE &lt;=#{endDate}</if>
		<if test="trainNbr !=null and trainNbr !='' ">AND TRAIN_NBR =#{trainNbr}</if>
		<if test="startBureau != null and startBureau !='' ">AND START_BUREAU = #{startBureau}</if>
		<if test="isRelationBureau == '1' ">AND #{bureau} in (PASS_BUREAU)</if>
	 <if test="tokenVehBureau !=null and tokenVehBureau !='' ">AND TOKEN_VEH_BUREAU =#{tokenVehBureau}</if>
	 * @return
	 */
	public List<RunPlan> getPlanTrainLkInfo(Map<String,Object> reqMap){
		return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_PLANTRAIN_LK_INFO, reqMap);
	}
}
