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
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.service.CrossService;
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
	 
	 @Autowired
	private CrossService crossService;
	
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Result getFullStationTrains(HttpServletRequest request, HttpServletResponse response){
		Result result = new Result(); 
		  try {  
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
				String chartId = request.getParameter("chartId");
				String startDay = request.getParameter("startDay"); 
			    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();   
			    for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {   
			    	// 上传文件 
			    	MultipartFile mf = entity.getValue();  
			    	crossService.actionExcel(mf.getInputStream(), chartId, startDay);
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
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public Result getCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		//车辆担当局
//		Object tokenVehBureau = reqMap.get("tokenVehBureau");
//		//始发局
//		Object startBureau = reqMap.get("startBureau");
//		//铁路线类型
//		Object highlineFlag = reqMap.get("highlineFlag");
		List<CrossInfo> list = null;
	    try{
	    	list = crossService.getCrossInfo(reqMap);
	    	result.setData(list);
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
	
}
