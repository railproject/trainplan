package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineThroughlineInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.HighLineService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.OptionDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	 
	 @Autowired
	 private CommonService commonService;
	 
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String highLine() {
		 return "highLine/highLine_cross";
     }
	 
	 @RequestMapping(value="/vehicle",method = RequestMethod.GET)
     public String highLineVehicle() {
		 return "highLine/highLine_cross_vehicle";
     }
	 
	 @RequestMapping(value="/vehicleCheck",method = RequestMethod.GET)
     public String highLineVehicleCheck() {
		 return "highLine/highLine_cross_vehicleCheck";
     }
	 
	 @RequestMapping(value="/vehicleSearch",method = RequestMethod.GET)
     public String highLineVehicleSearch() {
		 return "highLine/highLine_cross_vehicleSearch";
     }
	  
	@ResponseBody
	@RequestMapping(value = "/getVehicles", method = RequestMethod.GET)
	public Result  getVehicles() {
		 Result result = new Result();
		 try{  
			 List<OptionDto> list = highLineService.getVehicles();
			 result.setData(list);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDepots", method = RequestMethod.GET)
	public Result  getDepots() {
		 Result result = new Result();
		 try{  
			 List<OptionDto> list = commonService.getDepots();
			 result.setData(list);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCrhTypes", method = RequestMethod.GET)
	public Result  getCrhTypes() {
		 Result result = new Result();
		 try{  
			 List<OptionDto> list = commonService.getCrhTypes();
			 result.setData(list);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getThroughLines", method = RequestMethod.GET)
	public Result  getThroughLines() {
		 Result result = new Result();
		 try{  
			 List<OptionDto> list = commonService.getThroughLines();
			 result.setData(list);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAccs", method = RequestMethod.GET)
	public Result  getAccs() {
		 Result result = new Result();
		 try{  
			 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
			 System.out.println("============user.getBureau()==============" + user.getBureau()); 
			 List<OptionDto> list = commonService.getAccs(user.getBureau());
			 result.setData(list);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("getHighlineTrainTimeForHighlineCrossId error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}

	 /**
		 * 加载交路信息
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
				 
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 reqMap.put("crossBureau", user.getBureauShortName());
				 List<HighlineCrossInfo> list = highLineService.getHighlineCrossList(reqMap);
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
						 logger.debug("map=="+ map);
						 String highlineCrossId =  StringUtil.objToStr(map.get("highlineCrossId"));
						 String vehicle1 =  StringUtil.objToStr(map.get("vehicle1"));
						 String vehicle2 =  StringUtil.objToStr(map.get("vehicle2"));
						 int count = highLineService.updateHighLineVehicle(highlineCrossId,vehicle1,vehicle2);
						 logger.debug("updateHighLineVehicle~~count==" + count);
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
					 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
					 List<HighlineCrossInfo> hList = new ArrayList<HighlineCrossInfo>();
					 for(Map crossMap : newCrosses){
						 HighlineCrossInfo highlineCrossInfo = new HighlineCrossInfo();
						 String highLineCrossId = UUID.randomUUID().toString();
						 String planCrossId = StringUtil.objToStr(crossMap.get("planCrossId"));
						 String crossName = StringUtil.objToStr(crossMap.get("crossName"));
						 String crossStartDate = StringUtil.objToStr(crossMap.get("crossStartDate"));
						 String crossEndDate = StringUtil.objToStr(crossMap.get("crossEndDate"));
						 String startStn = StringUtil.objToStr(crossMap.get("crossStartStn"));
						 String endStn = StringUtil.objToStr(crossMap.get("crossEndStn"));
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
						 String createReason =  StringUtil.objToStr(crossMap.get("createReason")); 
						 String postName = StringUtil.objToStr(crossMap.get("postName"));
						 String postId = StringUtil.objToStr(crossMap.get("postId"));
						 //set值
						 highlineCrossInfo.setHighLineCrossId(highLineCrossId);
						 highlineCrossInfo.setPlanCrossId(planCrossId == null?"":planCrossId);
						 highlineCrossInfo.setCrossName(crossName == null?"":crossName);
						 String formateType = "yyyy-MM-dd hh:mm:ss";
						 highlineCrossInfo.setCrossStartDate(crossStartDate == null?"":DateUtil.getFormateDayTime(crossStartDate, formateType));
						 highlineCrossInfo.setCrossEndDate(crossEndDate == null?"":DateUtil.getFormateDayTime(crossEndDate, formateType));
						 highlineCrossInfo.setStartStn(startStn == null?"":startStn);
						 highlineCrossInfo.setCrossStartStn(startStn == null?"":startStn);
						 highlineCrossInfo.setCrossEndStn(endStn == null?"":endStn);
						 highlineCrossInfo.setEndStn(endStn == null?"":endStn);
						 highlineCrossInfo.setSpareFlag(spareFlag == null?"":spareFlag);
						 highlineCrossInfo.setPostId(postId == null? "": postId);
						 highlineCrossInfo.setPostName(postName == null? "": postName);
						 highlineCrossInfo.setRelevantBureau(relevantBureau == null?"":relevantBureau);
						 highlineCrossInfo.setTokenVehBureau(tokenVehBureau == null?"":tokenVehBureau);
						 highlineCrossInfo.setTokenVehDept(tokenVehDept == null?"":tokenVehDept);
						 highlineCrossInfo.setTokenVehDepot(tokenVehDepot == null?"":tokenVehDepot);
						 highlineCrossInfo.setTokenPsgBureau(tokenPsgBureau == null?"":tokenPsgBureau);
						 highlineCrossInfo.setTokenPsgDept(tokenPsgDept == null?"":tokenPsgDept);
						 highlineCrossInfo.setCrhType(crhType == null?"":crhType);
						 highlineCrossInfo.setCreateReason(createReason == null?"":createReason);
						 highlineCrossInfo.setNote(note == null?"":note);
						 
						 highlineCrossInfo.setCreatPeople(user.getUsername());
						 highlineCrossInfo.setCrossBureau(user.getBureau());
						 highlineCrossInfo.setCreatPeopleOrg(user.getDeptName());
						 
						 hList.add(highlineCrossInfo);
						 //保存到表highline_cross  
						 List<Map> trainsList = (List<Map>)crossMap.get("trains"); 
						
						 if(trainsList != null && trainsList.size() > 0 ){ 
							 for(Map trainMap : trainsList){//修改列车映射到新建的交路上
								 highLineService.updateHighLineTrain(StringUtil.objToStr(trainMap.get("highLineTrainId")), highlineCrossInfo.getHighLineCrossId());
//								 HighLineCrossTrainInfo crossTrain = new HighLineCrossTrainInfo();
//								 String  highLineTrainId = UUID.randomUUID().toString();
//								 String  planTrainId = StringUtil.objToStr(trainMap.get("planTrainId"));
//								 int   trainSort = Integer.valueOf(StringUtil.objToStr(trainMap.get("trainSort")));
//								 String  trainNbr = StringUtil.objToStr(trainMap.get("trainNbr"));
//								 String  runDate = StringUtil.objToStr(trainMap.get("runDate"));
//								 
//								 crossTrain.setPlanTrainId(planTrainId == null?"":planTrainId);
//								 crossTrain.setRunDate(runDate == null?"":runDate);
//								 crossTrain.setHighLineTrainId(highLineTrainId);
//								 crossTrain.setTrainNbr(trainNbr == null?"":trainNbr);
//								 crossTrain.setTrainSort(trainSort);
//								 crossTrain.setHighLineCrossId(highlineCrossInfo.getHighLineCrossId());
//								 tList.add(crossTrain);
							 }
							 //保存数据到highline_cross_train中 
						 }
						 
					 }
					 highLineService.batchAddHighlineCross(hList); 
				 } 
				 //删除表highline_cross表中数据
				 highLineService.deleteHighlienCrossForHighlineCrossId(highLineCrossIds); 
				 
			 }catch(Exception e){
				 logger.error("saveHighlineCrossAndTrainInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		 /**
		 *更新highlinecross中check的基本信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/updateHiglineCheckInfo", method = RequestMethod.POST)
		public Result  updateHiglineCheckInfo(@RequestBody Map<String,Object> reqMap) {
			 Result result = new Result();
			 try{
				 logger.debug("updateHiglineCheckInfo~~~reqMap==" + reqMap);
				 String highlineCrossIds = StringUtil.objToStr(reqMap.get("highlineCrossIds"));
				 //check_type:0_未审核 1_已审核
				 String checkType = "1";
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 int count = highLineService.updateHiglineCheckInfo(checkType,user.getName(),user.getDeptName(),highlineCrossIds);
				 logger.debug("updateHiglineCheckInfo~~count==" + count);
					
				
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("updateHiglineCheckInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		
		/**
		 * 通过startDate和crossBureau删除数据
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/cleanHighLineForDate", method = RequestMethod.POST)
		public Result cleanHighLineForDate(@RequestBody Map<String,Object> reqMap){
			 Result result = new Result();
			 try{
				 logger.debug("cleanHighLineForDate~~~reqMap==" + reqMap);
				 //交路开始时间，格式yyyyMMdd
				 String startDate = StringUtil.objToStr(reqMap.get("startDate"));
				 //获取登录用户信息
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 HighlineCrossInfo crossInfo = new HighlineCrossInfo();
				 crossInfo.setCrossStartDate(startDate);
				 crossInfo.setCrossBureau(user.getBureauShortName());
				 //获取数据
				 List<HighlineCrossInfo> list = highLineService.getHighlineCrossInfo(crossInfo);
				 StringBuffer highlineCrossIdBf = new StringBuffer();
				 if(list != null && list.size() > 0){
					 int size = list.size();
					 for(int i = 0;i<size;i++){
						 HighlineCrossInfo tempCrossInfo = list.get(i);
						 String highlineCrossId = tempCrossInfo.getHighLineCrossId();
						 if( i != size-1){
							 highlineCrossIdBf.append("'").append(highlineCrossId).append("',");
						 }else{
							 highlineCrossIdBf.append("'").append(highlineCrossId);
						 }
					 }
					 
					 //先删除子表HIGHLINE_CROSS_TRAIN的数据
					 highLineService.deleteHighlienCrossTrainForHighlineCrossId(highlineCrossIdBf.toString());
					 //再删除父表HIGHLINE_CROSS的数据
					 highLineService.deleteHighlienCrossForHighlineCrossId(highlineCrossIdBf.toString());
				 }
				 
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("cleanHighLineForDate error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		
		/**
		 * 通过highlineCrossIds删除数据
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/deleteHighLineForIds", method = RequestMethod.POST)
		public Result deleteHighLineForIds(@RequestBody Map<String,Object> reqMap){
			
			Result result = new Result();
			 try{
				 logger.debug("deleteHighLineForIds~~~reqMap==" + reqMap);
				 String higlineCrossids = StringUtil.objToStr(reqMap.get("higlineCrossids"));
				 //先删除子表HIGHLINE_CROSS_TRAIN的数据
				 highLineService.deleteHighlienCrossTrainForHighlineCrossId(higlineCrossids);
				//再删除父表HIGHLINE_CROSS的数据
				 highLineService.deleteHighlienCrossForHighlineCrossId(higlineCrossids);
				
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("deleteHighLineForIds error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
		
		/**
		 * 预览命令
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/previewCmdInfo", method = RequestMethod.POST)
		public Result previewCmdInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			 try{
				 /**
				  * 2014年08月09日京广高铁日计划命令：
					一、各站列车开行车次：
					1.交路:0D2002-D2001-D2006-D6721;  车底:CRH5049A+CRH5057A; 
					2.交路:0D6721;  车底:CRH5049A+CRH5057A; 
					3.交路:0G2503;  车底:京局担当
					4.交路:0G2504-G2503-G2504;  车底:京局担当
				  */
				 final String f_crossName = "交路:";
				 final String f_vehicleName = "车底:";
				 final String f_point = ".";
				 final String f_semicolon = ";";
				 final String f_cmdinfo = "日计划命令:";
				 final String f_oneCatalog = "一、各站列车开行车次：";
				 final String f_bureauInfo = "局担当";
				 final String f_plus = "+";
				 HighlineCrossInfo crossInfoReq = new HighlineCrossInfo();
				 String crossStartDate =  StringUtil.objToStr(reqMap.get("startDate"));
				 crossInfoReq.setCrossStartDate(crossStartDate);
				 //查询数据
				 List<HighlineThroughlineInfo> list = highLineService.getHighlineThroughCrossInfo(crossInfoReq);
				 if(list != null && list.size() > 0 ){
					 String dateStr = crossStartDate.substring(0,4)+"年" + crossStartDate.substring(4,6)+"月" + crossStartDate.substring(6,8) + "日";
					 StringBuffer  cmdInfoBf = new StringBuffer();
					for(int i = 0;i<list.size();i++){
						HighlineThroughlineInfo throughInfo = list.get(i);
						//铁路线
						String throughLine = throughInfo.getThroughLine();
						cmdInfoBf.append(dateStr).append(throughLine).append(f_cmdinfo).append("\n");
						cmdInfoBf.append(f_oneCatalog).append("\n");
						List<HighlineCrossInfo> crossInfoList = throughInfo.getListCrossInfo();
						if(crossInfoList != null && crossInfoList.size() > 0 ){
							for(int j = 0;j<crossInfoList.size();j++){
								HighlineCrossInfo crossInfo = crossInfoList.get(j);
								String crossName = crossInfo.getCrossName();
								String vehicle1 = crossInfo.getVehicle1();
								String vehicle2 = crossInfo.getVehicle2();
								//车辆担当局
								String tokenVehBureau = crossInfo.getTokenVehBureau();
								//交路计划所属局（局码）
								String crossBureau = crossInfo.getCrossBureau();
								cmdInfoBf.append(j+1).append(f_point).append(f_crossName).append(crossName).append("   ");
								//组装车底
								if(tokenVehBureau.equals(crossBureau)){
									if(vehicle1 != null && !"".equals(vehicle1)){
										if(vehicle2 != null && !"".equals(vehicle2)){
											cmdInfoBf.append(f_vehicleName).append(vehicle1).append(f_plus).append(vehicle2).append(f_semicolon).append("\n");
										}else{
											cmdInfoBf.append(f_vehicleName).append(vehicle1).append(f_semicolon).append("\n");
										}
									}else{
										if(vehicle2 != null && !"".equals(vehicle2)){
											cmdInfoBf.append(f_vehicleName).append(vehicle2).append(f_semicolon).append("\n");
										}else{
											cmdInfoBf.append("\n");
										}
									}
								}else{
									cmdInfoBf.append(f_vehicleName).append(tokenVehBureau).append(f_bureauInfo).append("\n");
								}
								
							}
							 
						}
						
					}
					result.setData(cmdInfoBf.toString());
				 }else{
					 //没有命令
					 result.setData("-1");
				 }
				 
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("previewCmdInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}


		/**
		 * 生成命令
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/createCmdInfo", method = RequestMethod.POST)
		public Result createCmdInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			 try{
				 String cmdInfo = StringUtil.objToStr(reqMap.get("cmdInfo"));
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("createCmdInfo error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}

		/**
		 * 保存
		 * @param reqMap
		 * @param roleType 取值：VEHICLE_SUB，VEHICLE_CHECK
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/saveHighLineWithRole/{roleType}",method = RequestMethod.POST)
		public Result saveHighLineWithRole(@RequestBody Map<String,Object> reqMap,@PathVariable String roleType){
			Result result = new Result();
			 try{
				 List<Map> highLineCrossesList = (List<Map>)reqMap.get("highLineCrosses");
				 if(highLineCrossesList != null && highLineCrossesList.size() > 0 ){
					 for(Map map : highLineCrossesList){
						
						 String highlineCrossId =  StringUtil.objToStr(map.get("highlineCrossId"));
						 String vehicle1 =  StringUtil.objToStr(map.get("vehicle1"));
						 String vehicle2 =  StringUtil.objToStr(map.get("vehicle2"));
						 int count = highLineService.updateHighLineVehicle(highlineCrossId,vehicle1,vehicle2);
						 logger.debug("saveHighLineWithRole~~count==" + count);
					 }
				 }
				
			 }catch(Exception e){
				 logger.error("saveHighLineWithRole error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			 return result;
		}

		
		/**
		 * 提交
		 * @param reqMap
		 * @param roleType 取值：CROSS_CHECK，VEHICLE_SUB，VEHICLE_CHECK
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/submitHighLineWithRole/{roleType}",method = RequestMethod.POST)
		public Result submitHighLineWithRole(@RequestBody Map<String,Object> reqMap,@PathVariable String roleType){
			Result result = new Result();
			 try{
				 HighlineCrossInfo crossInfo = new HighlineCrossInfo();
				 logger.debug("deleteHighLineForIds~~~reqMap==" + reqMap);
				 String crossStartDate = StringUtil.objToStr(reqMap.get("startDate"));
				 crossInfo.setCrossStartDate(crossStartDate);
				 //获取登录用户信息
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 String bureauShortName = user.getBureauShortName();
				 String people = user.getName();
				 String org = user.getDeptName();
				 if("CROSS_CHECK".equals(roleType)){
					 crossInfo.setCrossCheckPeople(people);
					 crossInfo.setCrossCheckPeopleOrg(org);
				 }else if("VEHICLE_SUB".equals(roleType)){
					 crossInfo.setVehicleSubPeople(people);
					 crossInfo.setVehicleSubPeopleOrg(org);
				 }else if("VEHICLE_CHECK".equals(roleType)){
					 crossInfo.setVehicleCheckPeople(people);
					 crossInfo.setVehicleCheckPeopleOrg(org);
				 }
				 crossInfo.setCrossBureau(bureauShortName);
				 
				 highLineService.updateHighlineCrossInfo(crossInfo);
			 }catch(Exception e){
				 e.printStackTrace();
				 logger.error("submitHighLineWithRole error==" + e.getMessage());
				 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			 }
			
			 return result;
		}
}
