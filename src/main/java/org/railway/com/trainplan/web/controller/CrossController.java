package org.railway.com.trainplan.web.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
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
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

 

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
			List<TrainlineTemplateSubDto> stationsInfo = null;
			ObjectMapper objectMapper = new ObjectMapper();
			if(crossIds !=null){
				String[] crossidArray = crossIds.split(",");
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				for(String crossid : crossidArray){
					Map<String,Object> crossMap = new HashMap<String,Object>();
					//根据crossid查询crossName
					String crossName = crossService.getCrossNameWithBaseCrossId(crossid);
					crossMap.put("crossName",crossName);
					//根据crossId查询base_train_id,train_sort等信息
					List<Map<String, Object>> listBaseTrainId = crossService.getTrainNbrWithBaseCrossId(crossid);
				    if(listBaseTrainId != null){
				    	List<Map<String,Object>> trains = new ArrayList<Map<String,Object>>();
				    	//循环base_cross_id，调用接口得到列车运行时刻表信息
				    	for(Map<String,Object> map : listBaseTrainId){
				    		Object baseTrainId = map.get("BASE_TRAIN_ID");
				    		if(baseTrainId != null && !"".equals(baseTrainId)){
				    			
				    			Map<String,Object> trainMap = new HashMap<String,Object>();
				    			
				    			//调用后台接口
				    			TrainlineTemplateDto trainLineDto = remoteService.getTrainLinesInfoWithId(baseTrainId.toString());
				    		    if(trainLineDto != null){
				    		    	//车次
				    		    	trainMap.put("trainName", trainLineDto.getTrainNbr());
				    		    	//始发站名
				    		    	trainMap.put("startStn", trainLineDto.getStartStn());
				    		    	trainMap.put("endStn", trainLineDto.getEndStn());
				    		    	//始发时间
				    		    	trainMap.put("startTime",trainLineDto.getStartTime() );
				    		    	//终到时间
				    		    	trainMap.put("endTime",trainLineDto.getEndTime());
				    		    	List<Map<String,Object>> trainStnsList = new ArrayList<Map<String,Object>>();
				    		    	//经由站信息
				    		    	List<TrainlineTemplateSubDto> subDtoList = trainLineDto.getStationList();
				    		    	stationsInfo = subDtoList;
				    		    	if(subDtoList != null && subDtoList.size() > 0){
				    		        	
				    		        	for(TrainlineTemplateSubDto subDto :subDtoList ){
				    		        		Map<String,Object> trainStnMap = new HashMap<String,Object>();
				    		        		//站名
				    		        		trainStnMap.put("stnName", subDto.getName());
				    		        		//到站时间
				    		        		trainStnMap.put("arrTime",subDto.getSourceTime() );
				    		        		trainStnMap.put("dptTime",subDto.getTargetTime() );
				    		        		trainStnsList.add(trainStnMap);
				    		        	}
				    		        }
				    		        trainMap.put("trainStns", trainStnsList);
				    		    
				    		    }
				    		    trains.add(trainMap);
				    		}
				    	}
				    	
				    	crossMap.put("trains", trains);
				    	
				    	//获取接续关系数组
				    	List<Map<String,Object>>  jxgx = getJxgx(trains);
				    	crossMap.put("jxgx", jxgx);
				    }
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
					Map<String,String> dateMap = crossService.getCrossDateWithBaseCrossId(crossid);
					//格式为yyyyMMdd,需要转成yyyy-dd-mm格式
					String crossStartDate = dateMap.get("CROSS_START_DATE");
					String crossEndDate = dateMap.get("CROSS_END_DATE");
					
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
	 * 组装接续关系
	 * @param trains
	 * @return
	 */
	private List<Map<String,Object>> getJxgx(List<Map<String,Object>> trains){
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		if(trains != null && trains.size() > 0 ){
			int size = trains.size();
			for(int i = 0;i<size;i++){
				//接续关系的map
				Map<String,Object> jxgxMap = new HashMap<String,Object>();
				int temp = i+1;
				if(temp<size){
					Map<String,Object> mapCurrent = trains.get(i);
					Map<String,Object> mapNext = trains.get(temp);
					//取i的终点站信息
					jxgxMap.put("fromStnName",mapCurrent.get("endStn"));
					jxgxMap.put("fromTime", mapCurrent.get("endTime"));
					//取i+1的起点信息
					jxgxMap.put("toStnName",mapNext.get("startStn"));
					jxgxMap.put("toTime", mapNext.get("startTime"));
					returnList.add(jxgxMap);
				}
				
			}
		}
		return returnList;
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
