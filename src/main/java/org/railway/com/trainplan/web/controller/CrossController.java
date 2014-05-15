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
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

 

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
