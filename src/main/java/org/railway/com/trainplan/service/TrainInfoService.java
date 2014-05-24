package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.PlanTrainStn;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.service.task.DayTrainPlanTask;
import org.railway.com.trainplan.service.task.DaytaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class TrainInfoService {
	private static final Logger logger = Logger.getLogger(TrainInfoService.class);
	@Autowired
	private BaseDao baseDao;
	
	
	
	/**
	 * 解析从数据库中获取的数据为列车的List对象
	 * @param reqDto
	 * @return
	 */
	public List<TrainlineTemplateDto>  getTrainsAndTimesForList(DaytaskDto reqDto){
		//运行日期格式：yyyy-mm-dd
		String runDate = reqDto.getRunDate();
		List<TrainlineTemplateDto> returnList = new ArrayList<TrainlineTemplateDto>();
		//从数据库获取的数据列表
		List<Map<String,Object>> listMap = getTrainsAndTimesForPage(reqDto.getChartId(),reqDto.getOperation(),reqDto.getRownumstart(),reqDto.getRownumend());
		int size = listMap.size();
		//不是同一列车,
		List<TrainlineTemplateSubDto> stationList = new ArrayList<TrainlineTemplateSubDto>();
		TrainlineTemplateDto dto = new TrainlineTemplateDto();
		if(listMap != null && size > 0){
			
			for(int i = 0;i<size;i++){
				
				Map<String,Object> map = listMap.get(i);
				TrainlineTemplateSubDto subDto = new TrainlineTemplateSubDto();
			
				//设置经由
				subDto = setTrainlineTemplateSubDto(map,subDto,runDate);
				stationList.add(subDto);
		
				String baseTrainId = StringUtil.objToStr(map.get("BASE_TRAIN_ID"));
				String baseTrainIdNext = "";
				if(i+1 < size){
					
					Map<String,Object> mapnext = listMap.get(i+1);
					baseTrainIdNext = StringUtil.objToStr(mapnext.get("BASE_TRAIN_ID"));
					if(baseTrainIdNext.equals(baseTrainId)){
						dto = setTrainlineTemplateDto(map,dto,runDate);
					}else{
						dto.setStationList(stationList);
						returnList.add(dto);
						
						//不是同一列车,
						stationList = new ArrayList<TrainlineTemplateSubDto>();
						dto = new TrainlineTemplateDto();
					}
				}else{
					dto = setTrainlineTemplateDto(map,dto,runDate);
					dto.setStationList(stationList);
					returnList.add(dto);
				}
				
				
			}
		}
		
		return returnList;
	}
	
	/**
	 * 将后台接口返回数据存入本地PLAN_TRAIN和PLAN_TRAIN_STN库中
	 * @param list
	 * @param tempStartDate 格式yyyy-mm-dd
	 */
	public void addTrainPlanLine(List<TrainlineTemplateDto> list,String tempStartDate) {
		
				if (list != null && list.size() > 0) {
					
					for (TrainlineTemplateDto dto : list) {
						String runDate = tempStartDate;
						String trainId= UUID.randomUUID().toString();
						try{
						
						runDate = DateUtil.format(DateUtil.parse(runDate),"yyyy-MM-dd");
						String starttime = dto.getStartTime();
						String endtime = dto.getEndTime();
	
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("runDate",runDate.replaceAll("-", ""));
						paramMap.put("trainNbr",dto.getTrainNbr() );
						paramMap.put("startTime",starttime );
						paramMap.put("endTime",endtime);
						paramMap.put("startStn",dto.getStartStn() );
						paramMap.put("endStn",dto.getEndStn() );
						paramMap.put("baseChartId",dto.getBaseChartId() );
						paramMap.put("baseTrainId",dto.getBaseTrainId() );
						paramMap.put("startBureauFull", dto.getStartBureauFull());
						paramMap.put("endBureauFull",dto.getEndBureauFull() );
						paramMap.put("trainType",dto.getTrainType() );
						//插入数据库
					    int successCount = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN, paramMap);
						logger.info("count of inserting into train_plan==" + successCount);
						//获取当前的plan_train_id  plan_train_id
						//Map map = (Map)baseDao.selectOneBySql(Constants.TRAINPLANDAO_GET_MAX_PLANTRAIN_ID, null);
						//trainId = (BigDecimal)map.get("plan_train_id");
						// 获取经由信息
						List<TrainlineTemplateSubDto> stationList = dto
								.getStationList();
						//System.err.println("stationList==" + stationList);
						if (stationList != null && stationList.size() > 0) {
                            
							List<PlanTrainStn> tempList = new ArrayList<PlanTrainStn>();
							
							for (TrainlineTemplateSubDto dtoStn : stationList) {
								
								PlanTrainStn tempSubDto = new PlanTrainStn();
								//数据库主键
								String planTrainStnId = UUID.randomUUID().toString();
								tempSubDto.setPlanTrainStnId(planTrainStnId);
								
								String sourceTime = dtoStn.getSourceTime();
								int daycountSouce = Integer.valueOf(sourceTime.substring(0,1));
								String trueRunDate = DateUtil.getDateByDay(runDate, -daycountSouce);
								String sourcetime = trueRunDate+ " "+ StringUtil.handleTime(sourceTime);
								
								String targetTime = dtoStn.getTargetTime();
								int daycountTarget = Integer.valueOf(targetTime.substring(0,1));
								trueRunDate = DateUtil.getDateByDay(runDate, -daycountTarget);
								String targettime = trueRunDate+ " "+ StringUtil.handleTime(targetTime);
                               
								
								tempSubDto.setArrTime(sourcetime);
								tempSubDto.setDptTime(targettime);
								tempSubDto.setBaseArrTime(sourcetime);
								tempSubDto.setBaseDptTime(targettime );
								tempSubDto.setStnName(dtoStn.getName());
								tempSubDto.setStnBureauFull(dtoStn.getStnBureauFull());
								tempSubDto.setStnSort(dtoStn.getIndex());
								tempSubDto.setTrackName(dtoStn.getTrackName());
								tempSubDto.setRunDays(dtoStn.getRunDays());
								
								tempList.add(tempSubDto);
								
								
							}
							//批量插入数据表train_plan_stn
							int successCountStn = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN_STN, tempList);
							logger.info("count of inserting into train_plan_stn==" + successCountStn);
							
						}

					}catch(Exception e){
						//不做任何处理，只打印日志
						e.printStackTrace();
						logger.error("存表plan_train失败，plan_train_id["+trainId +"],runDate["+runDate+"]");
					}
				 }
					
				}
		
	}
	
	/**
	 * 设置列车信息
	 * @param map
	 * @param dto
	 * @param runDate
	 * @return
	 */
	private TrainlineTemplateDto setTrainlineTemplateDto(Map<String,Object> map,TrainlineTemplateDto dto ,String runDate){
		
		//车次
		dto.setTrainNbr(StringUtil.objToStr(map.get("TRAIN_NBR")));
		dto.setRunDate(runDate);
		dto.setStartStn(StringUtil.objToStr(map.get("START_STN")));
		dto.setEndStn(StringUtil.objToStr(map.get("END_STN")));
		dto.setStartBureauFull(StringUtil.objToStr(map.get("START_BUREAU_FULL")));
		dto.setEndBureauFull(StringUtil.objToStr(map.get("END_BUREAU_FULL")));
		//列车运行的天数
		int runDaysAll = (( BigDecimal)map.get("RUN_DAYS_ALL")).intValue();
		dto.setStartTime(runDate + " " + StringUtil.objToStr(map.get("START_TIME")));
		String endRunDate = DateUtil.getDateByDay(runDate, -runDaysAll);
		dto.setEndTime(endRunDate + " " + StringUtil.objToStr(map.get("END_TIME")));
		return dto;
	}
	
	/**
	 * 设置经由站信息
	 */
	private TrainlineTemplateSubDto setTrainlineTemplateSubDto(Map<String,Object> map,TrainlineTemplateSubDto subDto,String runDate){
		
		//同一列车，设置经由站
		//站点名
		subDto.setName(StringUtil.objToStr(map.get("STN_NAME")));
		//所属局简称
		subDto.setBureauShortName(StringUtil.objToStr(map.get("STN_BUREAU")));
		//股道
		subDto.setTrackName(StringUtil.objToStr(map.get("TRACK_NAME")));
		int  rundays = (( BigDecimal)map.get("RUN_DAYS")).intValue();
		String currentRunDate = DateUtil.getDateByDay(runDate, -rundays);	
		//到站时间
		subDto.setSourceTime(currentRunDate + " " + StringUtil.objToStr(map.get("ARR_TIME")) );
		//离站时间
		subDto.setTargetTime(currentRunDate + " " + StringUtil.objToStr(map.get("DPT_TIME")) );
		return subDto;
	}
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
