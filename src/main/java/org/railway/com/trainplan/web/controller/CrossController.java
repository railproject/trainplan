package org.railway.com.trainplan.web.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.dto.BaseCrossDto;
import org.railway.com.trainplan.service.dto.BaseCrossTrainDto;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.web.dto.CrossRelationDto;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDTO;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

 

@Controller
@RequestMapping(value = "/cross")
public class CrossController {
	private static Log logger = LogFactory.getLog(CrossController.class.getName());
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "redirect:cross/cross_manage";
     }
	 
	 @RequestMapping(value="/unit", method = RequestMethod.GET)
     public String unit() {
		 return "cross/cross_unit_manage";
     }
	 
	 @Autowired
	private CrossService crossService;
	
	@Autowired
	private RemoteService remoteService;
	
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Result getFullStationTrains(HttpServletRequest request, HttpServletResponse response){
		Result result = new Result(); 
		  try {  
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
				String chartId = request.getParameter("chartId");
				String chartName = request.getParameter("chartName");
				String startDay = request.getParameter("startDay"); 
			    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();   
			    for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {   
			    	// 上传文件 
			    	MultipartFile mf = entity.getValue();  
			    	crossService.actionExcel(mf.getInputStream(), chartId, startDay, chartName);
			  	}  
 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		return result;
	}
	
	/**
	 * 提供画交路图形的数据
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideCrossChartData", method = RequestMethod.GET)
	public ModelAndView  provideCrossChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView result = new ModelAndView("cross/unit_cross_canvas"); 
		
			String crossIds = StringUtil.objToStr(request.getParameter("crossIds"));
			//经由信息，由后面调用接口获取，用户提供画图的坐标
			List<TrainlineTemplateSubDto> stationsInfo = new ArrayList<TrainlineTemplateSubDto>();
			ObjectMapper objectMapper = new ObjectMapper();
			if(crossIds !=null){
				String[] crossidArray = crossIds.split(",");
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				for(String crossid : crossidArray){
					Map<String,Object> crossMap = new HashMap<String,Object>();
					List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					List<CrossRelationDto> jxgx = new ArrayList<CrossRelationDto>();
					//根据crossid查询crossName，trainNbr等信息对象
					BaseCrossDto baseCrossDto = crossService.getBaseCrossDtoWithCrossId(crossid);
					crossMap.put("crossName",baseCrossDto.getCrossName());
					List<BaseCrossTrainDto> listBaseCrossTrain = baseCrossDto.getListBaseCrossTrain();
					if(listBaseCrossTrain != null && listBaseCrossTrain.size() > 0 ){
						
						List<TrainlineTemplateDto> listTrainsInfo = new ArrayList<TrainlineTemplateDto>();
						int trainCount = 0;
						for(BaseCrossTrainDto dto : listBaseCrossTrain){
							String baseTrainId = dto.getBaseTrainId();
							logger.debug("baseTrainId===" + baseTrainId);
							//调用后台接口获取列车时刻表信息对象
							TrainlineTemplateDto trainLineDto = remoteService.getTrainLinesInfoWithId(baseTrainId);
							listTrainsInfo.add(trainLineDto);
							
							//组装列车信息
							TrainInfoDto tempdto = null;
							//第一辆列车的开行日期是cross_start_date
							if(trainCount == 0){
								System.err.println("baseCrossDto.getCrossEndDate()==" + baseCrossDto.getCrossEndDate());
								tempdto = provideTrainMap(trainLineDto,DateUtil.getFormateDay(baseCrossDto.getCrossEndDate()));
								trains.add(tempdto);
							}else{
								//非第一辆车，始发日期是列车自己的始发日期,先找到第trainCount-1辆列车的终到日期，然后再往后推第trainCount辆车的dayGap天
								String endDateI = trains.get(trainCount-1).getEndDate();
								System.err.println("endDateI==" + endDateI);
								LocalDate endDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(endDateI);
								int dayGap = trainLineDto.getRundays();
								String startDateII = endDate.plusDays(dayGap).toString("yyyy-MM-dd");
								//解析
								tempdto = provideTrainMap(trainLineDto,startDateII);
								trains.add(tempdto);
							}
							trainCount++;
						}
						
						//经由信息列表,取cross_name中第一辆车的经由站，为后面算纵坐标提供数据
						if(listTrainsInfo != null && listTrainsInfo.size() > 0){
							TrainlineTemplateDto dto = listTrainsInfo.get(0);
							Map<String,Object> scheduleMap = dto.getScheduleMap();
							TrainlineTemplateSubDto sourceItemDto = (TrainlineTemplateSubDto)scheduleMap.get("sourceItemDto");
							TrainlineTemplateSubDto targetItemDto = (TrainlineTemplateSubDto)scheduleMap.get("targetItemDto");
							List<TrainlineTemplateSubDto> routeItemDtos = (List<TrainlineTemplateSubDto>)scheduleMap.get("routeItemDtos");
							stationsInfo.add(sourceItemDto);
							for(TrainlineTemplateSubDto  routeDto : routeItemDtos){
								stationsInfo.add(routeDto);
							}
							stationsInfo.add(targetItemDto);
						}
						
					}
					
					//组装接续关系信息
					jxgx =  getJxgx(trains);
					
					//将接续关系和列车时刻信息放入crossMap中
					crossMap.put("jxgx", jxgx);
					crossMap.put("trains", trains);
	   
				    dataList.add(crossMap);
				    String myJlData = objectMapper.writeValueAsString(dataList);
				  //图形数据
					result.addObject("myJlData",myJlData);
					logger.debug("myJlData==" + myJlData);
					//组装坐标
					 // Grid
			        List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
					if(stationsInfo != null){
					  
						for(TrainlineTemplateSubDto subDto : stationsInfo){
						
							planLineGridYList.add(new PlanLineGridY(subDto.getName()));
						}
							
					}
					
					//组装时间轴
					
					//格式为yyyyMMdd,需要转成yyyy-dd-mm格式
					String crossStartDate = baseCrossDto.getCrossStartDate();
					String crossEndDate = baseCrossDto.getCrossEndDate();
					
					 LocalDate start = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossStartDate);
				     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossEndDate));
				        List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();
				        while(!start.isAfter(end)) {
				            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
				            start = start.plusDays(1);
				        }
				        
					 PlanLineGrid grid = new PlanLineGrid(gridXList, planLineGridYList);
					 String gridStr = objectMapper.writeValueAsString(grid);
					 logger.debug("gridStr==" + gridStr);
					result.addObject("gridData",gridStr);
				    
				}
				
			}
		
		System.err.println("result===" + result);
		return result ;
	}
	
	/**
	 * 组装列车信息
	 * @param TrainInfoDto 从接口返回来的列车信息对象
	 * @param runDate  列车始发时间，格式yyyy-mm-dd
	 * @return 组装后的列车信息，主要是将列车始发时间添加到经由站始发日期中
	 */
	private TrainInfoDto provideTrainMap(TrainlineTemplateDto trainDto,String runDate){
		TrainInfoDto trainInfoDto = new TrainInfoDto();
		//列车车次
		String trainName = trainDto.getTrainNbr();
		//始发站
		String startStn = trainDto.getStartStn();
		//终到站
		String endStn = trainDto.getEndStn();
		trainInfoDto.setTrainName(trainName);
		trainInfoDto.setStartStn(startStn);
		trainInfoDto.setEndStn(endStn);
		trainInfoDto.setStartDate(runDate);
	   
	   
	    Map<String,Object> scheduleMap = trainDto.getScheduleMap();
	    //始发站信息
	    TrainlineTemplateSubDto sourceItemDto = (TrainlineTemplateSubDto)scheduleMap.get("sourceItemDto");
	    //终到站信息
	    TrainlineTemplateSubDto targetItemDto = (TrainlineTemplateSubDto)scheduleMap.get("targetItemDto");
	    //经由站信息
	    List<TrainlineTemplateSubDto> routeItemDtos = (List<TrainlineTemplateSubDto>) scheduleMap.get("routeItemDtos");
	    List<PlanLineSTNDTO> trainStns = new ArrayList<PlanLineSTNDTO>();
	    if(sourceItemDto != null){
	    	PlanLineSTNDTO traintempDto = new PlanLineSTNDTO();
	    	traintempDto.setStnName(sourceItemDto.getName());
	    	//起点站的日期是传入的runDay
	    	traintempDto.setArrTime(runDate + " " + sourceItemDto.getSourceTime());
	    	traintempDto.setDptTime(runDate + " " + sourceItemDto.getTargetTime());
	    	trainStns.add(traintempDto);
	    	//设置始发时刻
	    	trainInfoDto.setStartTime(sourceItemDto.getSourceTime());
	    	
	    	
	    }
	    //经由站
	    if(routeItemDtos != null && routeItemDtos.size() > 0){
	    	for(TrainlineTemplateSubDto routeDto : routeItemDtos){
	    		PlanLineSTNDTO traintempDto = new PlanLineSTNDTO();
	    		traintempDto.setStnName(routeDto.getName());
	    		Integer sourceDay = routeDto.getSourceDay();
	    		Integer targetDay = routeDto.getTargetDay();
	    		//System.err.println("runDate==" + runDate);
	    		LocalDate sourceDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(runDate);
	    		traintempDto.setArrTime(sourceDate.plusDays(sourceDay).toString("yyyy-MM-dd") + " "+routeDto.getSourceTime());
	    		traintempDto.setDptTime(sourceDate.plusDays(targetDay).toString("yyyy-MM-dd") + " " +routeDto.getTargetTime());
	    		trainStns.add(traintempDto);
	    	}
	    }
	    //终到站
	    if(targetItemDto != null){
	    	PlanLineSTNDTO traintempDto = new PlanLineSTNDTO();
	    	traintempDto.setStnName(targetItemDto.getName());
	    	Integer sourceDay = targetItemDto.getSourceDay();
    		Integer targetDay = targetItemDto.getTargetDay();
    		//System.err.println("runDate22==" + runDate);
    		LocalDate targetDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(runDate);
    		traintempDto.setArrTime(targetDate.plusDays(sourceDay).toString("yyyy-MM-dd") + " "+targetItemDto.getSourceTime());
    		//终到站离站日期
    		String targetDateTemp = targetDate.plusDays(targetDay).toString("yyyy-MM-dd");
    		traintempDto.setDptTime(targetDateTemp + " "+ targetItemDto.getTargetTime());
    		//设置终到站日期到对象
    		trainInfoDto.setEndDate(targetDateTemp);
    		//设置终到时刻
    		trainInfoDto.setEndTime(targetItemDto.getTargetTime());
    		trainStns.add(traintempDto);
	    }
	    trainInfoDto.setTrainStns(trainStns);
	   
		return trainInfoDto;
	}
	
	
	
	/**
	 * 组装接续关系
	 * @param trains
	 * @param listCrossTrain 列车对应的信息列表，主要是通过车次找到对应的dayGap
	 * @return
	 */
	private List<CrossRelationDto> getJxgx(List<TrainInfoDto> trains){
		List<CrossRelationDto> returnList = new ArrayList<CrossRelationDto>();
		if(trains != null && trains.size() > 0 ){
			int size = trains.size();
			for(int i = 0;i<size;i++){
				//接续关系的对象
				CrossRelationDto dto = new CrossRelationDto();
				int temp = i+1;
				if(temp<size){
					TrainInfoDto dtoCurrent = trains.get(i);
					TrainInfoDto dtoNext = trains.get(temp);
					
					//取i的终点站信息
					dto.setFromStnName(dtoCurrent.getEndStn());
					//取i的终点站日期和时刻进行组合
					dto.setFromTime(dtoCurrent.getEndDate()+ " " + dtoCurrent.getEndTime());
					
					//设置i+1始发日期和时刻
					dto.setToTime(dtoNext.getStartDate() + " "+dtoNext.getStartTime());
					//取i+1的起点信息
					dto.setToStnName(dtoNext.getStartStn());
					returnList.add(dto);
				}
				
			}
		}
		return returnList;
	}
	
	/**
	 * 通过车次找对应的dayGap
	 * @return
	 */
	private int getDayGap(List<BaseCrossTrainDto> listCrossTrain,String trainName){
		    int dayGap = 0;
		    if(listCrossTrain != null && listCrossTrain.size() >0 ){
		    	for(BaseCrossTrainDto dto :listCrossTrain ){
		    		if(trainName.equals(dto.getTrainNbr())){
		    			dayGap = dto.getDayGap();
		    			break;
		    		}
		    	}
		    }
		    return dayGap;
	}
	/**
	 * 5.2.4	更新给定列车的基本图运行线车底交路id
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUnitCrossId", method = RequestMethod.POST)
	public Result updateUnitCrossId(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		String unitCrossIds = StringUtil.objToStr(reqMap.get("unitCrossIds"));
		logger.info("updateUnitCrossId----unitCrossIds=="+unitCrossIds);
		try{
			if(unitCrossIds != null){
				String[] unitcrossArray = unitCrossIds.split(",");
				for(String unitCrossId :unitcrossArray){
					List<Map<String,String>> listMap = crossService.getTrainNbrFromUnitCrossId(unitCrossId);
					if(listMap !=null && listMap.size() > 0){
						
						//方案id
						String baseChartId="";
						int size = listMap.size();
						List<String> trainNbrs = new ArrayList<String>();
						for(int i = 0;i<size;i++){
							String trainNbr = listMap.get(i).get("TRAIN_NBR");
							baseChartId = listMap.get(i).get("BASE_CHART_ID");
							trainNbrs.add(trainNbr);
							
						}
						//调用后台接口
						remoteService.updateUnitCrossId(baseChartId, unitCrossId, trainNbrs);
					}
					
				}
			}
		}catch(Exception e){
			logger.error("updateUnitCrossId error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossInfo", method = RequestMethod.POST)
	public Result getUnitCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		List<CrossInfo> list = null;
	    try{
	    	list = crossService.getUnitCrossInfo(reqMap);
	    	PagingResult page = new PagingResult(crossService.getUnitCrossInfoCount(reqMap),list);
	    	result.setData(page);
	    }catch(Exception e){
			logger.error("getUnitCrossInfo error==" + e.getMessage());
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public Result getCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 

		List<CrossInfo> list = null;
	    try{
	    	list = crossService.getCrossInfo(reqMap);
	    	PagingResult page = new PagingResult(crossService.getCrossInfoCount(reqMap),list);
	    	result.setData(page);
	    }catch(Exception e){
			logger.error("getCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossTrainInfo", method = RequestMethod.POST)
	public Result getUnitCrossTrainInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String unitCrossId = (String)reqMap.get("unitCrossId");
		logger.debug("unitCrossId==" + unitCrossId);
		 try{
		    	//先获取unitcross基本信息
			 CrossInfo crossinfo = crossService.getUnitCrossInfoForUnitCrossid(unitCrossId);
			 //再获取unitcrosstrainInfo信息
			 List<CrossTrainInfo> list = crossService.getUnitCrossTrainInfoForUnitCrossid(unitCrossId);
		     List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		     Map<String,Object> dataMap = new HashMap<String,Object>();
		     dataMap.put("unitCrossInfo", crossinfo);
		     dataMap.put("unitCrossTrainInfo", list);
		     dataList.add(dataMap);
			 result.setData(dataList);
		    }catch(Exception e){
				logger.error("getUnitCrossTrainInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			}
		
		return result;
	}
	
	
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossTrainInfo", method = RequestMethod.POST)
	public Result getCrossTrainInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String crossId = (String)reqMap.get("crossId");
		logger.debug("crossId==" + crossId);
		 try{
		    	//先获取cross基本信息
			 CrossInfo crossinfo = crossService.getCrossInfoForCrossid(crossId);
			 //再获取crosstrainInfo信息
			 List<CrossTrainInfo> list = crossService.getCrossTrainInfoForCrossid(crossId);
		     List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		     Map<String,Object> dataMap = new HashMap<String,Object>();
		     dataMap.put("crossInfo", crossinfo);
		     dataMap.put("crossTrainInfo", list);
		     dataList.add(dataMap);
			 result.setData(dataList);
		    }catch(Exception e){
				logger.error("getCrossTrainInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			}
		
		return result;
	}
	
	/**
	 * 生成基本交路单元
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/completeUnitCrossInfo", method = RequestMethod.POST)
	public Result completeUnitCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//crossid以逗号分隔
			String crossId = StringUtil.objToStr(reqMap.get("crossIds"));
			logger.debug("crossId==" + crossId);
			if(crossId != null){
				String[] crossIds = crossId.split(",");
				for(String crossid :crossIds){
					//根据crossid生成交路单元
					crossService.completeUnitCrossInfo(crossid);
				}
			}
		}catch(Exception e){
			logger.error("completeUnitCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
		return result;
	}
}
