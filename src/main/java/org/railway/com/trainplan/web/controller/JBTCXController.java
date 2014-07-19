package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.service.JBTCXService;
import org.railway.com.trainplan.service.SchemeService;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.TrainTimeService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



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
	
	
}
