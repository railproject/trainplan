package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.TrainInfoServiceDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
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
		String chartId = reqDto.getChartId();
		List<TrainlineTemplateDto> returnList = new ArrayList<TrainlineTemplateDto>();
		//从数据库获取的数据列表
		List<Map<String,Object>> listMap = getTrainsAndTimesForPage(reqDto.getChartId(),reqDto.getOperation(),reqDto.getRownumstart(),reqDto.getRownumend());
		int size = listMap.size();
		//不是同一列车,
		List<TrainlineTemplateSubDto> stationList = new ArrayList<TrainlineTemplateSubDto>();
		TrainlineTemplateDto dto = new TrainlineTemplateDto();
		//设置一个主键，为后面存库做准备
		dto.setPlanTrainId(UUID.randomUUID().toString());
		if(listMap != null && size > 0){
			
			for(int i = 0;i<size;i++){
				
				Map<String,Object> map = listMap.get(i);
				TrainlineTemplateSubDto subDto = new TrainlineTemplateSubDto();
			
				
		
				String baseTrainId = StringUtil.objToStr(map.get("BASE_TRAIN_ID"));
				String baseTrainIdNext = "";
				if(i+1 < size){
					
					Map<String,Object> mapnext = listMap.get(i+1);
					baseTrainIdNext = StringUtil.objToStr(mapnext.get("BASE_TRAIN_ID"));
					if(baseTrainIdNext.equals(baseTrainId)){
						dto = setTrainlineTemplateDto(map,dto,runDate,chartId);
						//设置经由中的planTrainId
						subDto.setPlanTrainId(dto.getPlanTrainId());
					}else{
						dto.setStationList(stationList);
						returnList.add(dto);
						
						//不是同一列车,
						stationList = new ArrayList<TrainlineTemplateSubDto>();
						dto = new TrainlineTemplateDto();
						//设置一个主键，为后面存库做准备
						dto.setPlanTrainId(UUID.randomUUID().toString());
					}
				}else{
					dto = setTrainlineTemplateDto(map,dto,runDate,chartId);
					//设置经由中的planTrainId
					subDto.setPlanTrainId(dto.getPlanTrainId());
					dto.setStationList(stationList);
					returnList.add(dto);
				}
				
				//设置经由
				subDto = setTrainlineTemplateSubDto(map,subDto,runDate);
				//单独设置车次
				subDto.setTrainNbr(dto.getTrainNbr());
				stationList.add(subDto);
			}
		}
		
		return returnList;
	}
	
	/**
	 * 将后台接口返回数据存入本地PLAN_TRAIN和PLAN_TRAIN_STN库中
	 * @param list
	 * @param tempStartDate 格式yyyy-mm-dd
	 */
	public void addTrainPlanLine(List<TrainlineTemplateDto> list) {
		        try{
		        	if(list != null && list.size() > 0){
		        		//List<TrainInfoServiceDto> trainsList = new ArrayList<TrainInfoServiceDto>();
						
						for(TrainlineTemplateDto dto : list){
							//TrainInfoServiceDto destDto = new TrainInfoServiceDto();
							List<TrainlineTemplateSubDto> subList = dto.getStationList();
							//BeanUtils.copyProperties(destDto, dto);
							//trainsList.add(destDto);
							if(subList != null && subList.size() > 0){
								
								Map<String,Object>  map = new HashMap<String,Object>();
								map.put("trainStnList", subList);
								//批量插入数据表train_plan_stn
								int successCountStn = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN_STN, map);
								logger.info("count of inserting into train_plan_stn==" + successCountStn);
									
							}
							
						}
						//插入数据库
						Map<String,Object>  trainmap = new HashMap<String,Object>();
						trainmap.put("trainList",list);
					    int successCount = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN, trainmap);
						logger.info("count of inserting into train_plan==" + successCount);
					}	
		        }catch(Exception e){
					//不做任何处理，只打印日志
					e.printStackTrace();
					//logger.error("存表plan_train失败，plan_train_id["+trainId +"],runDate["+runDate+"]");
				}
				
		
		  /**
		   *   #{item.planTrainId,jdbcType=VARCHAR},
	       #{item.runDate,jdbcType=VARCHAR},
	       #{item.trainNbr,jdbcType=VARCHAR},
	       #{item.startStn,jdbcType=VARCHAR},
	       #{item.endStn,jdbcType=VARCHAR},
	       #{item.baseChartId,jdbcType=VARCHAR},
	       #{item.baseTrainId,jdbcType=VARCHAR},
	       #{item.startBureau,jdbcType=VARCHAR},
	       #{item.endBureau,jdbcType=VARCHAR},
	       #{item.trainType,jdbcType=VARCHAR},
		   */
					
	}
		
	
	
	/**
	 * 设置列车信息
	 * @param map
	 * @param dto
	 * @param runDate
	 * @return
	 */
	private TrainlineTemplateDto setTrainlineTemplateDto(Map<String,Object> map,TrainlineTemplateDto dto ,String runDate,String chartId){
		
		//所经局
		String passBureau = StringUtil.objToStr(map.get("PASS_BUREAU"));
		
		dto.setPassBureau(passBureau);
		//trainScope1:直通；0:管内
		if(passBureau !=null && !"".equals(passBureau)){
			if(passBureau.length()==1){
				dto.setTrainScope(0);
			}else{
				dto.setTrainScope(1);
			}
		}else{
			dto.setTrainScope(3);
		}
		dto.setTrainTypeId(StringUtil.objToStr(map.get("TRAIN_TYPE_ID")));
		
		dto.setBaseTrainId(StringUtil.objToStr(map.get("BASE_TRAIN_ID")));
		
		//车次
		String trainNbr = StringUtil.objToStr(map.get("TRAIN_NBR"));
		//始发站
		String startStn = StringUtil.objToStr(map.get("START_STN"));
		//始发时刻
		String startTime = StringUtil.objToStr(map.get("START_TIME"));
		dto.setTrainNbr(trainNbr);
		dto.setStartStn(startStn);
		//方案id
		dto.setBaseChartId(chartId);
		dto.setRunDate(runDate);
		
		if(runDate != null && !"".equals(runDate)){
			dto.setRunDate_8(runDate.replaceAll("-", ""));
		}
		
		dto.setEndStn(StringUtil.objToStr(map.get("END_STN")));
		dto.setStartBureauFull(StringUtil.objToStr(map.get("START_BUREAU_FULL")));
		dto.setEndBureauFull(StringUtil.objToStr(map.get("END_BUREAU_FULL")));
		dto.setStartBureau(StringUtil.objToStr(map.get("START_BUREAU")));
		dto.setEndBureau(StringUtil.objToStr(map.get("END_BUREAU")));
		//列车运行的天数
		int runDaysAll = (( BigDecimal)map.get("RUN_DAYS_ALL")).intValue();
		dto.setStartTime(runDate + " " + startTime);
		String endRunDate = DateUtil.getDateByDay(runDate, -runDaysAll);
		dto.setEndTime(endRunDate + " " + StringUtil.objToStr(map.get("END_TIME")));
		//始发日期+始发车次+始发站+计划始发时刻
		String planTrainSign  = runDate.replaceAll("-","") + "-" + trainNbr + "-" +startStn + "-" +startTime;
		dto.setPlanTrainSign(planTrainSign);
		return dto;
	}
	
	/**
	 * 设置经由站信息
	 */
	private TrainlineTemplateSubDto setTrainlineTemplateSubDto(Map<String,Object> map,TrainlineTemplateSubDto subDto,String runDate){
		//设置主键，为后面存库做准备
		subDto.setPlanTrainStnId(UUID.randomUUID().toString());
		//同一列车，设置经由站
		//站点名
		subDto.setName(StringUtil.objToStr(map.get("STN_NAME")));
		
		//所属局简称
		subDto.setBureauShortName(StringUtil.objToStr(map.get("STN_BUREAU")));
		//所属局全称
		subDto.setStnBureauFull(StringUtil.objToStr(map.get("STN_BUREAU_FULL")));
		//股道
		subDto.setTrackName(StringUtil.objToStr(map.get("TRACK_NAME")));
		subDto.setIndex((( BigDecimal)map.get("STN_SORT")).intValue());
		subDto.setRunDays((( BigDecimal)map.get("RUN_DAYS")).intValue());
		int  rundays = (( BigDecimal)map.get("RUN_DAYS")).intValue();
		String currentRunDate = DateUtil.getDateByDay(runDate, -rundays);	
		//到站时间
		subDto.setSourceTime(currentRunDate + " " + StringUtil.objToStr(map.get("ARR_TIME")) );
		//离站时间
		subDto.setTargetTime(currentRunDate + " " + StringUtil.objToStr(map.get("DPT_TIME")) );
		
		subDto.setBaseArrTime(subDto.getSourceTime());
		subDto.setBaseDptTime(subDto.getTargetTime());
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
