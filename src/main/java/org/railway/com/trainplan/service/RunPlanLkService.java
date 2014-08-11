package org.railway.com.trainplan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mor.railway.cmd.adapter.model.CmdInfoModel;
import mor.railway.cmd.adapter.service.ICmdAdapterService;
import mor.railway.cmd.adapter.service.impl.CmdAdapterServiceImpl;
import mor.railway.cmd.adapter.util.ConstantUtil;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.MTrainLine;
import org.railway.com.trainplan.entity.MTrainLineStn;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
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
	   * @param planTrainIds, 格式如： '','',''
	   * @return
	   */
	  public List<TrainLineSubInfo> getTrainLkInfoForPlanTrainId(String planTrainIds){
		  Map<String,Object> reqMap = new HashMap<String,Object>();
		  reqMap.put("planTrainIds", planTrainIds);
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_TRAINLK_FOR_PLAN_TRAIN_ID, reqMap);
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
	   * 根据cmdTxtmlItemId查询cmdTrain信息
	   * @param cmdTxtmlId
	   * @return
	   */
	  public CmdTrain  getCmdTrainInfoForCmdTxtmlItemId(String cmdTxtmlItemId){
		  Map<String,String> reqMap = new HashMap<String,String>();
		  reqMap.put("cmdTxtmlItemId", cmdTxtmlItemId);
		  return (CmdTrain) baseDao.selectOneBySql(Constants.RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDMLID, reqMap);
	  }
	  
	  /**
	   * 
	   * @param startDate 格式:yyyy-MM-dd
	   * @param endDate 格式:yyyy-MM-dd
	   * @param bureuaCode 局码
	   * @return
	   */
	  public List<CmdInfoModel>  getCmdTrainInfoFromRemote(CmdInfoModel model){
		 
		  //构造接口服务实例
		  ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
		  //服务初始化
		  service.initilize(model.getCmdBureau());
		  //根据开始结束时间，查询符合条件的临客命令对象集合
		  List<CmdInfoModel> list = service.findCmdInfoModelListByDateAndBureau(model);
		
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
	  public List<Date> getSelectedDateListFromRemote(CmdInfoModel model){
		    
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
	  
	  /**
	   * 通过cmdTrainId删除表cmd_train_stn中的数据
	   * @param cmdTrainId
	   * @return
	   */
	  public int deleteCmdTrainStnForCmdTrainId(String cmdTrainId,String stnBureau){
		  Map<String,Object> reqMap = new HashMap<String,Object>();
		  reqMap.put("cmdTrainId", cmdTrainId);
		  reqMap.put("stnBureau", stnBureau);
		  return baseDao.deleteBySql(Constants.RUNPLANLKDAO_DELETE_CMD_TRAINSTN_FOR_CMDTRAINID, reqMap);
	  }
	  
	  /**
	   * 根据cmdTrainId更新途径局passBureau
	   * @param passBureau 途径局
	   * @param cmdTrainId
	   * @return
	   */
	  public int updatePassBureauForCmdTraindId(String passBureau,String cmdTrainId){
		  Map<String,String> reqMap = new HashMap<String,String>();
		  reqMap.put("passBureau",passBureau);
		  reqMap.put("cmdTrainId",cmdTrainId);
		  return baseDao.updateBySql(Constants.RUNPLANLKDAO_UPDATE_PASS_BUREAU_FOR_CMD_TRAINID, reqMap);
	  }
	  
	  
	  /**
	   * 根据cmdTrainId查询表cmd_train和表cmd_train_stn的关联数据
	   * @param cmdTrainId
	   * @return
	   */
	  public List<CmdTrain> getCmdTrandAndStnInfo(String cmdTrainId){
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_CMDTRAIN_AND_STNINFO, cmdTrainId);
	  }
	  
	  /**
	   * 根据多条件查询表cmd_train数据对象
	   * @param cmdTrain
	   * @return
	   */
	  public List<CmdTrain> getCmdTraindForMultipleParame(CmdTrain cmdTrain){
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_CMDTRAIN_FOR_MULTIPLE_PARAME, cmdTrain);
	  }
	  /**
	   * 生成临客客运计划，对表plan_train插入数据 
	   * @param runPlan
	   * @return
	   */
	  public int addRunPlanLk(RunPlan runPlan){
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_ADD_RUN_PLAN_LK, runPlan);
	  }
	  
	  /**
	   * 生成临客客运计划，对表plan_train_stn插入数据 
	   * @param trainStnList
	   * @return
	   */
	  public int  addRunPlanLkTrainStn(List<RunPlanStn> trainStnList){
		  Map<String,Object> reqMap = new HashMap<String,Object>();
		  reqMap.put("trainStnList", trainStnList);
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_ADD_RUN_PLAN_STN_LK, reqMap);
	  }
	  
	  /**
	   * 根据站名获取站的基本信息，包括站名，站所属局局码，局简称，局全称
	   * @return
	   */
	  public List<Map<String,Object>> getBaseStationInfo(String stnName){
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_BASE_STATION_INFO, stnName);
	  }
	  
	  
	  public   List<Map<String,Object>> getPlanTrainIdForCmdTrainId(String cmdTrainId){
		  
		  return  baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_PLANTRAINID_FOR_CMDTRAINID, cmdTrainId);
	  }
	  
	  
	  public int deleteTrainForCmdTrainId(String cmdTrainId){
		  return baseDao.deleteBySql(Constants.RUNPLANLKDAO_DELETE_TRAIN_FOR_CMDTRAINID, cmdTrainId);
	  }
	 
	  public int deleteTrainStnForPlanTrainId(String planTrainId){
		  return baseDao.deleteBySql(Constants.RUNPLANLKDAO_DELETE_TRAINSTN_FOR_PLANTRAINID, planTrainId);
	  }
	  
	  /**
	   * 更新表cmd_train中字段createState
	   * @param cmdTrainId
	   * @param createState 0:未生成 1：已生成
	   * @return
	   */
	  public int updateCreateStateForCmdTrainId(String cmdTrainId,String createState){
		  Map<String,Object> reqMap = new HashMap<String,Object>();
		  reqMap.put("cmdTrainId",cmdTrainId );
		  reqMap.put("createState",createState);
		  return baseDao.updateBySql(Constants.RUNPLANLKDAO_UPDATE_CREATESTATE, reqMap);
	  }
	  
	  /**
	   * 生成运行线，对表M_TRAINLINE添加数据
	   * @param trainLine
	   * @return
	   */
	  public int insertMTrainLine(MTrainLine trainLine){
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_MTRAINLINE, trainLine);
	  }
	  
	  /**
	   * 生成运行线，对表M_TRAINLINE_SCHEDULE_SITEM添加数据
	   * @param trainLineStn
	   * @return
	   */
	  public int insertMTrainLineStnSource(MTrainLineStn trainLineStn){
		
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_MTRAINLINE_SOURCE, trainLineStn);
	  }
	  
	  /**
	   * 生成运行线，对表M_TRAINLINE_SCHEDULE_RITEM添加数据
	   * @param trainStnList
	   * @return
	   */
	  public int insertMTrainLineStnRoute(MTrainLineStn trainLineStn){
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_MTRAINLINE_ROUTE, trainLineStn);
	  }
	  
	  /**
	   * 生成运行线，对表M_TRAINLINE_SCHEDULE_TITEM添加数据
	   * @param trainLineStn
	   * @return
	   */
	  public int insertMTrainLineStnTarget(MTrainLineStn trainLineStn){
		  
		  return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_MTRAINLINE_TARGET, trainLineStn);
	  }
	  
	  public List<TrainLineSubInfoTime> getTrainLineSubinfoTime(String planTrainId){
		  return baseDao.selectListBySql(Constants.RUNPLANLKDAO_GET_TRAINLINE_SUBINFO_TIME, planTrainId);
	  }
	  
	 

}
