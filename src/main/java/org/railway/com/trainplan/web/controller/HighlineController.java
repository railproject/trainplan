package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
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
					 List<HighlineCrossInfo> list = highLineService.createHighLineCross(startDate); 
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
		
		 /**
		 *更新车底编号
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/updateHighLineVehicle", method = RequestMethod.POST)
		public Result  updateHighLineVehicle(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 List<Map> highLineCrossesList = (List<Map>)reqMap.get("highLineCrosses");
				 if(highLineCrossesList != null && highLineCrossesList.size() > 0 ){
					 for(Map map : highLineCrossesList){
						 System.err.print("map=="+ map);
					 }
				 }
				
			 }catch(Exception e){
				 logger.error("updateHighLineVehicle error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		 /**
		 *保存重组交路信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/saveHighlineCrossAndTrainInfo", method = RequestMethod.POST)
		public Result  saveHighlineCrossAndTrainInfo(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 /**
			  * {highLineCrossIds:",", newCrosses:[{"", "", "", trains:[{}]}]}
			  */
			 try{
				 String highLineCrossIds = StringUtil.objToStr(reqMap.get("highLineCrossIds"));
				 List<Map> newCrosses = (List<Map>)reqMap.get("newCrosses");
				 if(newCrosses != null && newCrosses.size() > 0){
					 List<HighlineCrossInfo> hList = new ArrayList<HighlineCrossInfo>();
					 for(Map crossMap : newCrosses){
						 String highLineCrossId = StringUtil.objToStr(crossMap.get("highLineCrossId"));
						 String planCrossId = StringUtil.objToStr(crossMap.get("planCrossId"));
						 String crossName = StringUtil.objToStr(crossMap.get("crossName"));
						 String crossStartDate = StringUtil.objToStr(crossMap.get("crossStartDate"));
						 String crossEndDate = StringUtil.objToStr(crossMap.get("crossEndDate"));
						 String startStn = StringUtil.objToStr(crossMap.get("startStn"));
						 String endStn = StringUtil.objToStr(crossMap.get("endStn"));
						 String spareFlag = StringUtil.objToStr(crossMap.get("spareFlag"));
						 String relevantBureau = StringUtil.objToStr(crossMap.get("relevantBureau"));
						 String tokenVehBureau = StringUtil.objToStr(crossMap.get("tokenVehBureau"));
						 String tokenVehDept = StringUtil.objToStr(crossMap.get("tokenVehDept"));
						 String tokenVehDepot = StringUtil.objToStr(crossMap.get("tokenVehDepot"));
						 String tokenPsgBureau= StringUtil.objToStr(crossMap.get("tokenPsgBureau"));
						 String tokenPsgDept = StringUtil.objToStr(crossMap.get("tokenPsgDept"));
						 String crhType = StringUtil.objToStr(crossMap.get("crhType"));
						 String note = StringUtil.objToStr(crossMap.get("note"));
						 String createPeople = StringUtil.objToStr(crossMap.get("createPeople"));
						
						 /**
						  *    
	    
	        sysdate 
						  */
					 }
				 }
				
			 }catch(Exception e){
				 logger.error("saveHighlineCrossAndTrainInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		
}
