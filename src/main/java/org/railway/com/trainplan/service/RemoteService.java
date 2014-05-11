package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.service.dto.PlanBureauStatisticsDto;
import org.railway.com.trainplan.service.dto.PlanBureauTsDto;
import org.railway.com.trainplan.service.dto.SchemeDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.RestClientUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.apache.log4j.Logger;
/**
 * 调用远程接口服务的service
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class RemoteService {
	private static final Logger logger = Logger.getLogger(RemoteService.class);
	/**
	 * 查询方案信息
	 */
	public List<SchemeDto> getSchemeList() throws Exception {

		List<SchemeDto> list = new ArrayList<SchemeDto>();
		Map response = null;
		response = RestClientUtils.post(Constants.SERVICE_URL
				+ Constants.GET_SCHEME_LIST, new HashMap(), Map.class);
		logger.info("getSchemeLis--response= " + response);
		if (response != null && response.size() > 0) {
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) response
					.get("data");
			if (dataList != null && dataList.size() > 0) {

				for (Map<String, Object> tempMap : dataList) {
					SchemeDto dto = new SchemeDto();
					dto.setSchemeId(StringUtil.objToStr(tempMap.get("id")));
					dto.setSchemeName(StringUtil.objToStr(tempMap.get("name")));
					list.add(dto);
				}
			}
		}
		return list;
	}
	
	

	/**
	 * 根据方案id查询基本运行线
	 * 
	 * @param schemeId
	 * @param runDate 格式：yyyy-mm-dd
	 * @return
	 * @throws Exception
	 */

	public List<TrainlineTemplateDto> getTrainLineInfoFromSchemeId(
			String schemeId, String runDate) throws Exception {
		List<TrainlineTemplateDto> returnList = null;
		Map<String,String> request = new HashMap<String,String>();
		request.put("schemeId", schemeId);
        request.put("operation","客运");
		//fortest
		Long starttime = System.currentTimeMillis();
		// 访问后台接口
		Map response = RestClientUtils.post(Constants.SERVICE_URL
				+ Constants.GET_TRAINLINE_TEMPLATES, request, Map.class);
		System.err.println("访问接口所有时间：" + (System.currentTimeMillis() - starttime)/1000);
		//System.err.println("response==" +response );
		logger.info("getTrainLineInfoFromSchemeId--response--code==" + response.get("code"));
		logger.info("getTrainLineInfoFromSchemeId--response--dataSize==" + response.get("dataSize"));
		// 解析返回报文
		if (response != null && response.size() > 0) {
			String code = StringUtil.objToStr(response.get("code"));
			if (!"".equals(code) && code.equals("200")) {
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) response
						.get("data");
				if (dataList != null && dataList.size() > 0) {
					returnList = new ArrayList<TrainlineTemplateDto>();
					for (Map<String, Object> map : dataList) {
						// 返回交易成功
						TrainlineTemplateDto dto = new TrainlineTemplateDto();
						//只解析客运的信息
						dto.setBaseChartId(schemeId);
						dto.setBaseTrainId(StringUtil.objToStr(map.get("id")));
						//格式转换成yyyy-mm-dd
						String myRunDate = DateUtil.format(DateUtil.parse(runDate),"yyyy-MM-dd");
						dto.setRunDate(myRunDate);
						
						// 车次
						dto.setTrainNbr(StringUtil.objToStr(map.get("name")));
						
						Map<String, Object> scheduleMap = (Map<String, Object>) map
								.get("scheduleDto");
						if (scheduleMap != null && scheduleMap.size() > 0) {
							List<TrainlineTemplateSubDto> subDtoList = dto
									.getStationList();
							// 始发站信息
							Map<String, Object> sourceItemMap = (Map<String, Object>) scheduleMap
									.get("sourceItemDto");
							if (sourceItemMap != null
									&& sourceItemMap.size() > 0) {
								TrainlineTemplateSubDto subDto = setSubDtoValue(
										sourceItemMap, Constants.STATION_BEGIN);
						
								//设置始发局全称
								String bureauName = StringUtil.objToStr(sourceItemMap.get("bureauName"));		
								//dto.setStartBureauFull(bureauName ==null? "":bureauName);
								dto.setStartBureauFull(bureauName);
								//设置始发站名
								dto.setStartStn(StringUtil.objToStr(sourceItemMap.get("name")));
								String sourceTimeDto2 = StringUtil.objToStr(sourceItemMap.get("sourceTimeDto2"));
								int daycount = Integer.valueOf(sourceTimeDto2.substring(0,1));
								//System.err.println("daycount==" + daycount);
								String trueRunDate = DateUtil.getDateByDay(myRunDate, -daycount);
								//设置始发时间
								dto.setStartTime(trueRunDate+ " " + StringUtil.handleTime(sourceTimeDto2));
								//设置运行天数
								subDto.setRunDays(daycount);
								subDtoList.add(subDto);
								
							}
							// 终到站信息
							Map<String, Object> targetItemMap = (Map<String, Object>) scheduleMap
									.get("targetItemDto");
							if (targetItemMap != null
									&& targetItemMap.size() > 0) {
								TrainlineTemplateSubDto subDto = setSubDtoValue(
										targetItemMap, Constants.STATION_END);
								
								//设置终到局全称
								String bureauName = StringUtil.objToStr(targetItemMap.get("bureauName"));
								//dto.setEndBureauFull(bureauName ==null? "":bureauName);
								dto.setEndBureauFull(bureauName);
								//设置终到站名
								dto.setEndStn(StringUtil.objToStr(targetItemMap.get("name")));
								String targetTimeDto2 = StringUtil.objToStr(targetItemMap.get("targetTimeDto2"));
								int daycount = Integer.valueOf(targetTimeDto2.substring(0,1));
								//System.err.println("daycountTarget==" + daycount);
								String trueRunDate = DateUtil.getDateByDay(myRunDate, -daycount);
								
								//设置终到时间
								dto.setEndTime(trueRunDate+" "+StringUtil.handleTime(targetTimeDto2));
								//设置运行天数
								subDto.setRunDays(daycount);
								subDtoList.add(subDto);
							}
							// 经由信息
							List<Map<String, Object>> routeItemList = (List<Map<String, Object>>) scheduleMap
									.get("routeItemDtos");
							if (routeItemList != null
									&& routeItemList.size() > 0) {
								for (Map<String, Object> routeItemMap : routeItemList) {
									TrainlineTemplateSubDto subDto = setSubDtoValue(
											routeItemMap,
											Constants.STATION_ROUTE);
									subDtoList.add(subDto);
								}
							}
							// 设置始发站，经由站，终到站等信息
							dto.setStationList(subDtoList);
						}
						returnList.add(dto);
					
				  }
				}
			}
		}
		return returnList;
	}

	
	/**
	 * 6.1.1	查询始发日期为给定日期范围的日计划运行线统计数
	 * @param code:
	 * 	查询统计信息编码
         1.	code="01"，查询所有列车统计项的统计数
         2.	code="02"，只查询运行线统计项的统计数
         3.	code="10"，查询给定时间范围内路局统计数
	 * @param sourceTime  格式：yyyy-MM-dd HH:mm:ss
	 * @param targetTime  格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 */
	public List<PlanBureauStatisticsDto>  getTrainLinesWithDay(String code,String sourceTime,String targetTime) throws Exception{
		List<PlanBureauStatisticsDto> returnList = new ArrayList<PlanBureauStatisticsDto>();
		Map<String,String> request = new HashMap<String,String>();
		request.put("code",code);
		request.put("sourceTime",sourceTime );
		request.put("targetTime", targetTime);
		System.err.println("getTrainLinesWithDay---request==" + request);
		//调用后台的接口
		Map response = RestClientUtils.post(Constants.SERVICE_URL
				+ Constants.GET_TRAINLINS, request, Map.class);
		System.err.println("getTrainLinesWithDay---response==" + response);
		//解析后台报文
		if (response != null && response.size() > 0){
			String codeMessage = StringUtil.objToStr(response.get("code"));
			if (!"".equals(codeMessage) && codeMessage.equals("200")){
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
				if (dataList != null && dataList.size() > 0){
					//取第一个,只有一条数据
					Map<String,Object> dataMap = dataList.get(0);
					//18个路局列表
					List<Map<String,Object>> stationsList = (List<Map<String,Object>>)dataMap.get("planBureauStatisticsDtos");
				    if(stationsList != null && stationsList.size() > 0){
				    	//循环18个路局，并取数据
				    	for(Map<String,Object> stationMap : stationsList){
				    		PlanBureauStatisticsDto dto = new PlanBureauStatisticsDto();
				    		dto.setBureauName(StringUtil.objToStr(stationMap.get("bureauName")));
				    		dto.setBureauShortName(StringUtil.objToStr(stationMap.get("bureauShortName")));
				    		dto.setBureauId(StringUtil.objToStr(stationMap.get("bureauId")));
				    		dto.setBureauCode(StringUtil.objToStr(stationMap.get("bureauCode")));
				    		
				    		List<Map<String,Object>> listDtos =  (List<Map<String,Object>>)stationMap.get("planBureauTsDtos");
				    		if(listDtos != null && listDtos.size() > 0){
				    			for(Map<String,Object> subMap : listDtos){
				    				/**
				    				 * 后台返回的数据
				    				 *  {
                                         "id": "客运图定",
                                         "sourceTargetTrainlineCounts": 0,
                                         "sourceSurrenderTrainlineCounts": 0,
                                         "accessTargetTrainlineCounts": 0,
                                         "accessSurrenderTrainlineCounts": 0
                                        }
				    				 */
				    				String id = StringUtil.objToStr(subMap.get("id"));
				    				if("客运图定".equals(id)){
				    					PlanBureauTsDto subDto = new PlanBureauTsDto();
				    					subDto.setSourceSurrenderTrainlineCounts((Integer)subMap.get("sourceSurrenderTrainlineCounts"));
				    					subDto.setSourceTargetTrainlineCounts((Integer)subMap.get("sourceTargetTrainlineCounts"));
				    					dto.getListBureauDto().add(subDto);
				    					break;
				    				}
				    			}
				    		}
				    		
				    		returnList.add(dto);
				    	}
				    }
				}
			}
		}
		System.err.println("getTrainLinesWithDay---returnList==="+returnList);
		return returnList ;
	}
	
	
	
	/**
	 * 对站点设置基本信息
	 * 
	 * @param map
	 * @param stationType
	 * @return
	 */
	private TrainlineTemplateSubDto setSubDtoValue(Map<String, Object> map,
			String stationType) {
		TrainlineTemplateSubDto subDto = new TrainlineTemplateSubDto();
		/**
		 * String bureauName = StringUtil.objToStr(targetItemMap.get("bureauName"));
								dto.setEndBureauFull(bureauName ==null? "":bureauName);
		 */
		subDto.setName(StringUtil.objToStr(map.get("name")));
		
		//subDto.setBureauName(bureauName ==null? "":bureauName);
		subDto.setSourceTime(StringUtil.objToStr(map.get("sourceTimeDto2")));
		subDto.setTargetTime(StringUtil.objToStr(map.get("targetTimeDto2")));
		subDto.setIndex(StringUtil.strToInteger(StringUtil.objToStr(map
				.get("index"))));
		String bureauName = StringUtil.objToStr(map.get("bureauName"));
		//subDto.setStnBureauFull(bureauName ==null? "":bureauName);
		subDto.setStnBureauFull(bureauName);
		// TODO:局码,现在后台报文还没这个字段
		subDto.setStnBureau(StringUtil.objToStr(map.get("")));
		// 站点类型 【1：始发 2：经由 3：终到】
		subDto.setStationType(stationType);
		//股道
		subDto.setTrackName(StringUtil.objToStr(map.get("trackName")));
		//运行天数
		String sourceTime = StringUtil.objToStr(map.get("sourceTimeDto2"));
		if(sourceTime != null && !"".equals(sourceTime)&& sourceTime.length()>0 ){
			subDto.setRunDays(Integer.valueOf(sourceTime.substring(0,1)));
		}
		
		return subDto;
	}
	
}
