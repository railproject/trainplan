package org.railway.com.trainplan.web.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.service.HighLineService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/highLine")
public class HighlineController {
	 private static Log logger = LogFactory.getLog(HighlineController.class.getName());
	 
	 @Autowired
	 private HighLineService highLineService;
	 
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String highLine() {
		 return "highLine/highLine_cross";
     }

	 /**
		 * 审核交路信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/createHighLineCross", method = RequestMethod.POST)
		public Result  createHighLineCross(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 String startDate = StringUtil.objToStr(reqMap.get("startDate"));
				 if(startDate != null){  
					 List<HighlineCrossInfo> list = highLineService.updateCorssCheckTime(startDate); 
					 result.setData(list);
				 } 
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("checkCorssInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
	
	  
		 /**
		 * 查询highlineCross信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getHighlineCrossList", method = RequestMethod.POST)
		public Result  getHighlineCrossList(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 String crossStartDate = StringUtil.objToStr(reqMap.get("crossStartDate"));
				 List<HighlineCrossInfo> list = highLineService.getHighlineCrossList(crossStartDate);
				 result.setData(list);
			 }catch(Exception e){
				 logger.error("getHighlineCrossList error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
	
		 /**
		 * 查询highlineCrossTrain信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getHighlineCrossTrainList", method = RequestMethod.POST)
		public Result  getHighlineCrossTrainList(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 String highlineCrossId = StringUtil.objToStr(reqMap.get("highlineCrossId"));
				 List<HighLineCrossTrainInfo> list = highLineService.getHighlineCrossTrainList(highlineCrossId);
				 result.setData(list);
			 }catch(Exception e){
				 logger.error("getHighlineCrossTrainList error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}	
		
		 /**
		 * * 通过highlineCrossId查询
         * 交路下所有列车的始发站，终到站，始发时间和终到时间
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getHighlineCrossTrainBaseInfoList", method = RequestMethod.POST)
		public Result  getHighlineCrossTrainBaseInfoList(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 String highlineCrossId = StringUtil.objToStr(reqMap.get("highlineCrossId"));
				 logger.debug("highlineCrossId==" + highlineCrossId);
				 List<HighlineCrossTrainBaseInfo> list = highLineService.getHighlineCrossTrainBaseInfoList(highlineCrossId);
				 result.setData(list);
			 }catch(Exception e){
				 logger.error("getHighlineCrossTrainBaseInfoList error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		 /**
		 *通过highlineCrossId查询该交路下的列车经由站信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getHighlineTrainTimeForHighlineCrossId", method = RequestMethod.POST)
		public Result  getHighlineTrainTimeForHighlineCrossId(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 String highlineCrossId = StringUtil.objToStr(reqMap.get("highlineCrossId"));
				 logger.debug("highlineCrossId==" + highlineCrossId);
				 List<HighlineTrainRunLine> list = highLineService.getHighlineTrainTimeForHighlineCrossId(highlineCrossId);
				 result.setData(list);
			 }catch(Exception e){
				 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		
}
