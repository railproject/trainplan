package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mor.railway.cmd.adapter.model.CmdInfoModel;
import mor.railway.cmd.adapter.service.ICmdAdapterService;
import mor.railway.cmd.adapter.service.impl.CmdAdapterServiceImpl;
import mor.railway.cmd.adapter.util.ConstantUtil;
import mor.railway.cmd.adapter.util.StringAndTimeUtil;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
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
	  
	  
	  /**
	   * 查询cmdTrainStn表信息
	   * @param cmdTrainId cmdTrain表主键
	   * @return
	   */
	  public List<CmdTrainStn> getCmdTrainStnInfo(String cmdTrainId){
		  return  baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_CMD_TRAINSTN_INFO, cmdTrainId);
	  }
	  
	  
	  /**
	   * 根据cmdTxtmlId查询cmdTrain信息
	   * @param cmdTxtmlId
	   * @return
	   */
	  public CmdTrain  getCmdTrainInfoForCmdTxtmlId(int cmdTxtmlId){
		  return (CmdTrain) baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDMLID, cmdTxtmlId);
	  }
	  
	  /**
	   * 
	   * @param startDate 格式:yyyy-MM-dd
	   * @param endDate 格式:yyyy-MM-dd
	   * @param bureuaCode 局码
	   * @return
	   */
	  public List<CmdInfoModel>  getCmdTrainInfoFromRemote(String startDate,String endDate,String bureuaCode){
		 
		  Date startDay = DateUtil.parse(startDate);
		  Date endDay = DateUtil.parse(endDate);
		  //构造接口服务实例
		  ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
		  //服务初始化
		  service.initilize(bureuaCode);
		  //根据开始结束时间，查询符合条件的临客命令对象集合
		  List<CmdInfoModel> list = service.findCmdInfoModelListByDateAndBureau(startDay, endDay);
		  //关闭服务资源
		  service.closeResource();
		  return list;
	  }
	  
	  
	  /**
	   * 根据CMD_TRAIN(命令生成开行计划列车表)中字段计算可以生成开行计划(停运开行计划)的日期集合
	   * @param startDate 格式yyyy-MM-dd
	   * @param endDate 格式yyyy-MM-dd
	   * @param cmdType 命令类型 (既有加开；既有停运；高铁加开；高铁停运)
	   * @param rule
	   * @param selectedDate
	   * @return
	   */
	  public List<Date> getSelectedDateListFromRemote(String startDate,String endDate,String cmdType,String rule,String selectedDate){
		    //创建临客命令对象
			CmdInfoModel model = new CmdInfoModel();
			model.setStartDate(DateUtil.parse(startDate));
			model.setEndDate(DateUtil.parse(endDate));
	        // 命令类型
			model.setCmdType(cmdType);
	        // 开行规律
			model.setRule(rule);
	        // 或者择日
	        model.setSelectedDate(selectedDate);
			
			return ConstantUtil.getSelectedDateList(model);
	  }
	  
	  /**
	   * 保存表cmd_train_stn中数据
	   * @param cmdTrainStn
	   * @return
	   */
	  public int insertCmdTrainStn(CmdTrainStn cmdTrainStn){
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_CMD_TRAIN_STN, cmdTrainStn);
	  }
	  
	  /**
	   * 保存表cmd_train中数据
	   * @param cmdTrain
	   * @return
	   */
	  public int insertCmdTrain(CmdTrain cmdTrain){
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_CMD_TRAIN, cmdTrain);
	  }
}
