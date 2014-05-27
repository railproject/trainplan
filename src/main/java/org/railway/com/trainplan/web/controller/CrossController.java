package org.railway.com.trainplan.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.dto.*;
import org.railway.com.trainplan.web.dto.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 

@Controller
@RequestMapping(value = "/cross")
public class CrossController {
	private static Log logger = LogFactory.getLog(CrossController.class.getName());
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "cross/cross_manage";
     }
	 
	 @RequestMapping(value="/unit", method = RequestMethod.GET)
     public String unit() {
		 return "cross/cross_unit_manage";
     }
	 
	 @RequestMapping(value="/runPlan", method = RequestMethod.GET)
     public String runPlan() {
		 return "cross/run_plan";
     }
	 
	 @RequestMapping(value="/crossCanvas", method = RequestMethod.GET)
     public String crossCanvas() {
		 return "cross/canvas_event_getvalue";
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
 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setCode("401");
				result.setMessage("上传失败");
			}  
		return result;
	}
	
	/**
	 * 删除base_cross表和表base_cross_train中数据
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteUnitCorssInfo", method = RequestMethod.POST)
	public Result deleteUnitCorssInfo(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				//先删除cross_train表中数据
				int countTrain = crossService.deleteUnitCrossInfoTrainForCorssIds(crossIdsArray);
				//删除unit_cross表中数据
				int count = crossService.deleteUnitCrossInfoForCorssIds(crossIdsArray);
			 }  
		 }catch(Exception e){
			 logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	}
	
	/**
	 * 删除base_cross表和表base_cross_train中数据
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCorssInfo", method = RequestMethod.POST)
	public Result deleteCorssInfo(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				//先删除base_cross_train表中数据
				int countTrain = crossService.deleteCrossInfoTrainForCorssIds(crossIdsArray);
				//删除base_cross表中数据
				int count = crossService.deleteCrossInfoForCorssIds(crossIdsArray);
			 }  
		 }catch(Exception e){
			 logger.error("deleteCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	}
	
	/**
	 * 审核交路信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCorssInfo", method = RequestMethod.POST)
	public Result checkCorssInfo(@RequestBody Map<String,Object> reqMap) {
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				int count = crossService.updateCorssCheckTime(crossIdsArray);
				System.err.println("update--count==" + count);
			 } 
		 }catch(Exception e){
			 logger.error("checkCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	/**
	 * 提供画交路图形的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideUnitCrossChartData", method = RequestMethod.GET)
	public ModelAndView  provideUnitCrossChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 ModelAndView result = new ModelAndView("cross/unit_cross_canvas"); 
		 ObjectMapper objectMapper = new ObjectMapper();
		 String crossId = StringUtil.objToStr(request.getParameter("crossId"));
		 PlanLineGrid grid = null;
		 
		 System.err.println("crossId---unit=="+ crossId);
		
		 //通过crossId获取unitCross列表信息
		 List<CrossInfo> listUnitCross = crossService.getUnitCrossInfosForCrossId(crossId);
		 if(listUnitCross != null){
			 List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				
			 for(int i = 0;i<listUnitCross.size();i++){
				 Map<String,Object> crossMap = new HashMap<String,Object>();
				 CrossInfo crossInfo = listUnitCross.get(i);
				 String unitCrossId = crossInfo.getUnitCrossId();
				 BaseCrossDto baseCrossDto = crossService.getUnitCrossDtoWithUnitCrossId(unitCrossId);
				 System.err.println("unitCrossId=="+ unitCrossId);
				 Map<String,Object> chartDateMap = null;
				 //避免重复计算坐标
				 if(i == 0){
					
					 chartDateMap = provideOneCrossChartData(baseCrossDto,true,Constants.TYPE_UNIT_CROSS); 
					 grid = (PlanLineGrid)chartDateMap.get("gridData");
				 }else{
					 chartDateMap = provideOneCrossChartData(baseCrossDto,false,Constants.TYPE_UNIT_CROSS); 
				 }
					crossMap.put("jxgx", chartDateMap.get("jxgx"));
					crossMap.put("trains", chartDateMap.get("trains"));
					crossMap.put("crossName", chartDateMap.get("crossName"));
					dataList.add(crossMap);
			 }
			 String myJlData = objectMapper.writeValueAsString(dataList);
			//图形数据
			result.addObject("myJlData",myJlData);
			logger.debug("myJlData==" + myJlData);
			
			//坐标
			//坐标信息
			String gridStr = objectMapper.writeValueAsString(grid);
			logger.debug("gridStr==" + gridStr);
			result.addObject("gridData",gridStr);
		 }
		
		
		 
		 return result;
	}
	
	/**
	 * 提供画交路图形的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideCrossChartData", method = RequestMethod.GET)
	public ModelAndView  provideCrossChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		    ModelAndView result = new ModelAndView("cross/unit_cross_canvas"); 
		
			String crossId = StringUtil.objToStr(request.getParameter("crossId"));
			System.err.println("crossId=="+ crossId);
			//经由信息，由后面调用接口获取，用户提供画图的坐标
			
			ObjectMapper objectMapper = new ObjectMapper();
			if(crossId !=null){
				
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				
					BaseCrossDto baseCrossDto = crossService.getBaseCrossDtoWithCrossId(crossId);
					Map<String,Object> crossMap = new HashMap<String,Object>();
					Map<String,Object> oneCrossMap = provideOneCrossChartData(baseCrossDto,true,Constants.TYPE_CROSS);
					crossMap.put("jxgx", oneCrossMap.get("jxgx"));
					crossMap.put("trains", oneCrossMap.get("trains"));
					crossMap.put("crossName", oneCrossMap.get("crossName"));
					dataList.add(crossMap);
					String myJlData = objectMapper.writeValueAsString(dataList);
					//图形数据
					result.addObject("myJlData",myJlData);
					logger.debug("myJlData==" + myJlData);
					
					//坐标信息
					PlanLineGrid grid = (PlanLineGrid)oneCrossMap.get("gridData");
					String gridStr = objectMapper.writeValueAsString(grid);
					logger.debug("gridStr==" + gridStr);
					result.addObject("gridData",gridStr);
		
			}
		
		System.err.println("result===" + result);
		return result ;
	}
	
	
	
	/**
	 * 提供画交路图形的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMap", method = RequestMethod.POST)
	public Result  createCrossMap(@RequestBody Map<String,Object> reqMap) throws Exception{
		 	Result result = new Result();
			String crossId = StringUtil.objToStr(reqMap.get("crossId"));
			System.err.println("crossId=="+ crossId);
			//经由信息，由后面调用接口获取，用户提供画图的坐标
			
			ObjectMapper objectMapper = new ObjectMapper();
			if(crossId !=null){
				
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				
				BaseCrossDto baseCrossDto = crossService.getBaseCrossDtoWithCrossId(crossId);
				Map<String,Object> crossMap = new HashMap<String,Object>();
				Map<String,Object> oneCrossMap = provideOneCrossChartData(baseCrossDto,true,Constants.TYPE_CROSS);
				crossMap.put("jxgx", oneCrossMap.get("jxgx"));
				crossMap.put("trains", oneCrossMap.get("trains"));
				crossMap.put("crossName", oneCrossMap.get("crossName"));
				dataList.add(crossMap);
				String myJlData = objectMapper.writeValueAsString(dataList);
				//图形数据
				Map<String , Object> map = new HashMap<String , Object>();
				map.put("myJlData", myJlData);
				
				
				//坐标信息
				PlanLineGrid grid = (PlanLineGrid)oneCrossMap.get("gridData");
				String gridStr = objectMapper.writeValueAsString(grid);
				logger.debug("gridStr==" + gridStr);
			 
				
				map.put("gridData", gridStr);
				result.setData(map); 
			}
		
		System.err.println("result===" + result);
		return result ;
	}
	
	/**
	 * 组装列车信息
	 * @param trainDto 从接口返回来的列车信息对象
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
	    List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
	    if(sourceItemDto != null){
	    	PlanLineSTNDto traintempDto = new PlanLineSTNDto();
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
	    		PlanLineSTNDto traintempDto = new PlanLineSTNDto();
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
	    	PlanLineSTNDto traintempDto = new PlanLineSTNDto();
	    	traintempDto.setStnName(targetItemDto.getName());
	    	Integer sourceDay = targetItemDto.getSourceDay();
    		Integer targetDay = targetItemDto.getTargetDay();
    		System.err.println("runDate22==" + runDate);
    		System.err.println("sourceDay==" + sourceDay);
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
	 * 提供一个交路信息的画图数据
	 * @param isProvideGrid 是否组装坐标
	 * @param type cross:交路  unitcross：交路单元
	 * @return
	 */
	private Map<String,Object> provideOneCrossChartData(BaseCrossDto baseCrossDto,boolean isProvideGrid,String type) throws Exception {
		//Map<String,Object> crossChartMap = new HashMap<String,Object>();
		//经由信息，由后面调用接口获取，用户提供画图的坐标
		List<TrainlineTemplateSubDto> stationsInfo = new ArrayList<TrainlineTemplateSubDto>();
	
		Map<String,Object> crossMap = new HashMap<String,Object>();
		List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
		List<CrossRelationDto> jxgx = new ArrayList<CrossRelationDto>();
		//根据crossid查询crossName，trainNbr等信息对象
		//BaseCrossDto baseCrossDto = crossService.getBaseCrossDtoWithCrossId(baseCrossId);
		System.err.println("CrossStartDate==" + baseCrossDto.getCrossStartDate());
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
					System.err.println("baseCrossDto.getCrossStartDate()==" + baseCrossDto.getCrossStartDate());
					tempdto = provideTrainMap(trainLineDto,DateUtil.getFormateDay(baseCrossDto.getCrossStartDate()));
					trains.add(tempdto);
				}else{
					//非第一辆车，始发日期是列车自己的始发日期,先找到第trainCount-1辆列车的终到日期，然后再往后推第trainCount辆车的dayGap天
					
					String endDateI = trains.get(trainCount-1).getEndDate();
					System.err.println("endDateI==" + endDateI);
					if(endDateI != null){
						LocalDate endDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(endDateI);
						int dayGap = trainLineDto.getRundays();
						String startDateII = endDate.plusDays(dayGap).toString("yyyy-MM-dd");
						//解析
						tempdto = provideTrainMap(trainLineDto,startDateII);
						
					 }
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

		if(isProvideGrid && type.equals(Constants.TYPE_CROSS)){
			//组装坐标
			 PlanLineGrid grid = getPlanLineGrid(stationsInfo,baseCrossDto.getCrossStartDate(), baseCrossDto.getCrossEndDate());
			 crossMap.put("gridData", grid);
		}else if(isProvideGrid && type.equals(Constants.TYPE_UNIT_CROSS)){
			String baseCrossId = baseCrossDto.getBaseCrossId();
			List<CrossInfo> crossInfoList = crossService.getUnitCrossInfosForCrossId(baseCrossId);
			String crossStartDate = "";
			String crossEndDate = "";
			if(crossInfoList != null && crossInfoList.size() > 0){
				//取第一个交路单元的开始日期
				crossStartDate = crossInfoList.get(0).getCrossStartDate();
				//取最后一个交路单元的终到日期
				crossEndDate = 	crossInfoList.get(crossInfoList.size() - 1).getCrossEndDate();
				 PlanLineGrid grid = getPlanLineGrid(stationsInfo,crossStartDate,crossEndDate);
				 crossMap.put("gridData", grid);
			}
		}
		
	
		return crossMap;
		
	}
	
	/**
	 * 组装坐标轴数据
	 * @param stationsInfo 经由站信息对象
	 * @param crossStartDate 交路开始日期，格式yyyyMMdd
	 * @param crossEndDate 交路终到日期，格式yyyyMMdd
	 * @return 坐标轴对象
	 */
	private PlanLineGrid getPlanLineGrid(List<TrainlineTemplateSubDto> stationsInfo,String crossStartDate,String crossEndDate){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>(); 
		 /****组装纵坐标****/
		 if(stationsInfo != null){
			  
				for(TrainlineTemplateSubDto subDto : stationsInfo){
				    if(subDto != null){
				    	planLineGridYList.add(new PlanLineGridY(subDto.getName()));
				    }
					
				}
					
			}
		 
		/*****组装横坐标  *****/
		 LocalDate start = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     
		 return new PlanLineGrid(gridXList, planLineGridYList);
	}
	/**
	 * 组装接续关系
	 * @param trains
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
					
					//设置i-1始发日期和时刻
					dto.setToTime(dtoNext.getStartDate() + " "+dtoNext.getStartTime());
					//取i-1的起点信息
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
		String crossId = (String)reqMap.get("crossId");
		logger.debug("crossId==" + crossId);
		 try{
		    	//先获取unitcross基本信息
			 CrossInfo crossinfo = crossService.getCrossInfoForCrossid(crossId);
			 //再获取unitcrosstrainInfo信息
			 List<CrossTrainInfo> list = crossService.getUnitCrossTrainInfoForCrossid(crossId);
		     List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		     Map<String,Object> dataMap = new HashMap<String,Object>();
		     dataMap.put("crossinfo", crossinfo);
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
				//生成交路单元完成后，更改表base_cross中的creat_time字段的值
				crossService.updateCrossUnitCreateTime(crossIds);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("completeUnitCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
		return result;
	}
}
