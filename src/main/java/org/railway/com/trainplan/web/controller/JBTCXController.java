package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.service.JBTCXService;
import org.railway.com.trainplan.service.SchemeService;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.TrainTimeService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDto;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
@RequestMapping("/jbtcx")
public class JBTCXController {
	private static Log logger = LogFactory.getLog(JBTCXController.class.getName());
	
	
	@Autowired
	private JBTCXService jbtcxService;
	
	@Autowired
	private SchemeService schemeService;
	
	@Autowired
	private TrainTimeService trainTimeService;
	
	@Autowired
	private TrainInfoService trainInfoService;
	
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "plan/plan_runline_check";
     }
	 

	/**
	 * 跳转到列车运行时刻表图形页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTrainTimeCanvasPage" ,method = RequestMethod.GET)
	public ModelAndView getTrainTimeCanvasPage(HttpServletRequest request) {
		return new ModelAndView("plan/train_runline_canvas").addObject("planTrainId", request.getParameter("planTrainId"));
	}
 
	@ResponseBody
	@RequestMapping(value = "/querySchemes", method = RequestMethod.POST)
	public Result querySchemes(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.info("querySchemes~~reqMap="+reqMap);
			//调用后台接口
			List<SchemeInfo> schemeInfos = schemeService.getSchemes();
			result.setData(schemeInfos);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	 
	/**
	 * 统计路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrains", method = RequestMethod.POST)
	public Result queryTrains(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.info("queryTrains~~reqMap="+reqMap);
			reqMap.put("operation", "客运");
			//调用后台接口
			PagingResult page = new PagingResult(trainInfoService.getTrainInfoCount(reqMap), trainInfoService.getTrainsForPage(reqMap));
			result.setData(page);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
	
	/**
	 * 统计路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainLines", method = RequestMethod.POST)
	public Result queryTrainLines(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.info("queryTrains~~reqMap="+reqMap);
			reqMap.put("operation", "客运");
			//调用后台接口
			PagingResult page = new PagingResult(trainInfoService.getTrainLinesCount(reqMap), trainInfoService.getTrainLinesForPage(reqMap));
			result.setData(page);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimes", method = RequestMethod.POST)
	public Result queryTrainTimes(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//调用后台接口
			String trainId =  StringUtil.objToStr(reqMap.get("trainId"));
			List<TrainTimeInfo> times = trainTimeService.getTrainTimes(trainId);
			result.setData(times);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
	
	@ResponseBody
	@RequestMapping(value = "/queryTrainLineTimes", method = RequestMethod.POST)
	public Result queryTrainLineTimes(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//调用后台接口
			String trainId =  StringUtil.objToStr(reqMap.get("trainId"));
			List<TrainTimeInfo> times = trainTimeService.getTrainLineTimes(trainId);
			result.setData(times);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
	
	/**
	 * 查询运行线的列车运行时刻表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryPlanLineTrainTimes", method = RequestMethod.POST)
	public Result queryPlanLineTrainTimes(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			
			String trainId =  StringUtil.objToStr(reqMap.get("trainId"));
			logger.info("queryPlanLineTrainTimes~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService.getTrainTimeInfoByTrainId(trainId);
			result.setData(times);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
	
	/**
	 * 根据planTrainId查询运行线的列车运行时刻表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanTrainStnInfoForPlanTrainId", method = RequestMethod.POST)
	public Result getPlanTrainStnInfoForPlanTrainId(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			
			String trainId =  StringUtil.objToStr(reqMap.get("trainId"));
			logger.info("getPlanTrainStnInfoForPlanTrainId~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService.getPlanTrainStnInfoForPlanTrainId(trainId);
			result.setData(times);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	
	/**
	 * 修改运行线的列车运行时刻表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editPlanLineTrainTimes", method = RequestMethod.POST)
	public Result editPlanLineTrainTimes(@RequestBody String reqStr){
		Result result = new Result();
		logger.info("editPlanLineTrainTimes~~reqStr==" + reqStr);
		try{
			JSONArray reqObj = JSONArray.fromObject(reqStr);
			List<TrainTimeInfo> list = new ArrayList<TrainTimeInfo>();
			if(reqObj != null && reqObj.size() > 0){
				for(int i = 0;i<reqObj.size();i++){
					TrainTimeInfo temp = new TrainTimeInfo();
					JSONObject obj = reqObj.getJSONObject(i);
					temp.setPlanTrainStnId(StringUtil.objToStr(obj.get("planTrainStnId")));
					temp.setArrTime(StringUtil.objToStr(obj.get("arrTime")));
					temp.setDptTime(StringUtil.objToStr(obj.get("dptTime")));
					temp.setTrackName(StringUtil.objToStr(obj.get("trackName")));
					list.add(temp);
				}
			}
			//保存数据
			int count = trainTimeService.editPlanLineTrainTimes(list);
			logger.info("editPlanLineTrainTimes~~count==" + count);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
	
		return result;
	} 
	
	/**
	 * 修改运行线的列车运行时刻表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSpareFlag", method = RequestMethod.POST)
	public Result updateSpareFlag(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		logger.info("updateSpareFlag~~reqStr==" + reqMap);
		String spareFlag = StringUtil.objToStr(reqMap.get("spareFlag"));
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		try{
			
			int count = trainTimeService.updateSpareFlag(spareFlag,planTrainId);
			
			logger.info("updateSpareFlag~~count==" + count);	
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		
		return result;
	} 
	/**
	 * 根据plant_train_id从基本图库中查询列车时刻表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainTimeInfoByPlanTrainId", method = RequestMethod.POST)
	public Result getTrainTimeInfoByPlanTrainId(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		logger.info("getTrainTimeInfoByPlanTrainId~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		try{
			
			List<BaseCrossTrainInfoTime> list = trainTimeService.getTrainTimeInfoByPlanTrainId(planTrainId);
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			//列车信息
			 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
			 Map<String,Object> crossMap = new HashMap<String,Object>();
			 //用于纵坐标的经由站列表
			 List<Station> listStation = new ArrayList<Station>();
			 //横坐标的开始日期
			 String arrDate = ""; 
			 //横坐标的结束日期
			 String dptDate = "";
			 if(list != null && list.size() > 0 ){
				 //设置列车信息
				 TrainInfoDto dto = new TrainInfoDto();
				 dto.setTrainName(trainNbr);
				 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
				 
				//循环经由站
				 for(int i = 0;i<list.size();i++){
					
					 
					 BaseCrossTrainInfoTime subInfo = list.get(i);
					 PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
					 stnDtoStart.setArrTime(subInfo.getArrTime());
					 stnDtoStart.setDptTime(subInfo.getDptTime());
					 stnDtoStart.setStayTime(subInfo.getStayTime());
					 stnDtoStart.setStnName(subInfo.getStnName());
					 stnDtoStart.setStationType(subInfo.getStationType());
					 trainStns.add(stnDtoStart);
					 //获取起始站的到站日期和终到站的出发日期为横坐标的日期段
					 if( i == 0){
						 String arrTime = subInfo.getArrTime();
						 arrDate = DateUtil.format(DateUtil.parseDate(arrTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
					 }
					 if( i == list.size()-1){
						 String dptTime = subInfo.getDptTime();
						 dptDate = DateUtil.format(DateUtil.parseDate(dptTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
					 }
					 //纵坐标数据
					 Station station = new Station();
		    		 station.setStnName(subInfo.getStnName());
					 station.setStationType(subInfo.getStationType());
					 listStation.add(station);
				 }
				 dto.setTrainStns(trainStns);
				 trains.add(dto);
			 }
			 crossMap.put("trains", trains);
			 dataList.add(crossMap);
			 PlanLineGrid grid = null;
			 ObjectMapper objectMapper = new ObjectMapper();
			 //生成横纵坐标
	    	 List<PlanLineGridY> listGridY = getPlanLineGridY(listStation); 
			 List<PlanLineGridX> listGridX = getPlanLineGridX(arrDate,dptDate);
		     grid = new PlanLineGrid(listGridX, listGridY);
		     String myJlData = objectMapper.writeValueAsString(dataList);
		     //图形数据
			 Map<String,Object> dataMap = new HashMap<String,Object>();
			 String gridStr = objectMapper.writeValueAsString(grid);
			 dataMap.put("myJlData",myJlData);
			 dataMap.put("gridData", gridStr);
			 System.err.println("myJlData==" + myJlData);
			 System.err.println("gridStr==" + gridStr);
			result.setData(dataMap);
			//result.setData(list);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		
		return result;
	}
	
	
	/**
	 * 组装纵坐标
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unused")
	private  List<PlanLineGridY> getPlanLineGridY(List<Station> list){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 if(list != null){
			 for(Station station : list){
				 //0:默认的isCurrentBureau
				 planLineGridYList.add(new PlanLineGridY(station.getStnName(),0,station.getStationType()));
			 }
		 }
		 
		 return planLineGridYList ;
	}
	
	/**
	 * 组装横坐标
	 * @param crossStartDate 交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate 交路终到日期，格式yyyy-MM-dd
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<PlanLineGridX> getPlanLineGridX(String crossStartDate,String crossEndDate){
		
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();  
		
		 /*****组装横坐标  *****/
		 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     return gridXList ;
	}
}
