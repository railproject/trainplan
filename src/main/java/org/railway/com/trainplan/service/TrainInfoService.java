package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanTrain;
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
