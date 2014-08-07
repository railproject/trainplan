package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.MTrainLine;
import org.railway.com.trainplan.entity.MTrainLineStn;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.PlanTrainStnService;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanCrossDto;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.PlanLineInfoDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/runPlan")
public class RunPlanController {
	 private static Log logger = LogFactory.getLog(RunPlanController.class.getName());
	 @Autowired
	 private RunPlanService runPlanService;
	 
	 @Autowired
	 private SendMsgService sendMsgService;
	 @Autowired
	 private PlanTrainStnService planTrainStnService;
	 
	 @Autowired
	 private RunPlanLkService runPlanLkService;
	 
	 @Autowired
	 private CommonService commonService ;
	 
	 @Autowired
	 private AmqpTemplate amqpTemplate;
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String runPlan() {
		 return "runPlan/run_plan";
     }
	 
	 @RequestMapping(value="/runPlanCreate", method = RequestMethod.GET)
     public String runPlanCreate() {
		 return "runPlan/run_plan_create";
     }
	 
	 @RequestMapping(value="/runPlanGt" ,method = RequestMethod.GET)
     public String runPlanGt() {
		 return "runPlan/run_plan_gt";
     }
	 
	 
	 @RequestMapping(value="/runPlanLineCreate" ,method = RequestMethod.GET)
     public String runPlanLineCreate() {
		 return "runPlan/run_plan_line_create";
     }
	 
	 
	 /**
	  * 跳转到列车运行时刻表编辑页面
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/trainRunTimePage" ,method = RequestMethod.GET)
     public ModelAndView trainRunTimePage(HttpServletRequest request) {
		 return new ModelAndView("runPlan/train_runTime").addObject("trainNbr", request.getParameter("trainNbr"))
		 		.addObject("trainPlanId", request.getParameter("trainPlanId"))
		 		.addObject("startStn",request.getParameter("startStn"))
		 		.addObject("endStn",request.getParameter("endStn"));
     }
	 
	 
	 /**
	  * 查看乘务信息跳转页面
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/trainCrewPage" ,method = RequestMethod.GET)
     public ModelAndView trainCrewPage(HttpServletRequest request) {
		 return new ModelAndView("runPlan/train_crew").addObject("trainNbr", request.getParameter("trainNbr"))
		 		.addObject("runDate", request.getParameter("runDate"));
     }
	 
	 
	 @ResponseBody
	 @RequestMapping(value = "/getRunPlans", method = RequestMethod.POST)
	 public Result getRunPlans(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 try{ 
			 List<RunPlanTrainDto> runPlans = runPlanService.getTrainRunPlans(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 e.printStackTrace();
			 result.setCode("-1");
			 result.setMessage("查询运行线出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
	 /**
	  * 图定开行计划查询
	  * @param reqMap
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getPlanCross", method = RequestMethod.POST)
	 public Result getPlanCross(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 try{ 
			 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	if(user.getBureau() != null){
	    		reqMap.put("currentBureau", user.getBureau());
	    	} 
	    	
			 List<PlanCrossDto> runPlans = runPlanService.getPlanCross(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询运行线出错:" + e.getMessage());
		 } 
		 return result; 
     } 
	 
	 
	@ResponseBody
	@RequestMapping(value = "/deletePlanCrosses", method = RequestMethod.POST)
	public Result deletePlanCrosses(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				//先删除cross_train表中数据
				int countTrain = runPlanService.deletePlanCrossByPlanCorssIds(crossIdsArray); 
			 }  
		 }catch(Exception e){
			 logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	} 
	 
	/**
	 * 审核交路
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCrossRunLine", method = RequestMethod.POST)
	public Result checkCrossRunLine(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 logger.info("checkCrossRunLine==" + reqMap);
		 try{
			 String planCrossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			 //计划审核起始时间（格式：yyyymmdd）
			 String startDate = StringUtil.objToStr(reqMap.get("startTime"));
			 //计划审核终止时间（格式：yyyymmdd）
			 String endDate = StringUtil.objToStr(reqMap.get("endTime"));
			 //相关局局码
			// String relevantBureau = StringUtil.objToStr(reqMap.get("relevantBureau"));
			 List<Map<String,String>> planCrossIdList = new ArrayList<Map<String,String>>();
			 
			 if(planCrossIds !=null && planCrossIds.length() > 0 ){
				 String[] crossIdAndBureaus = planCrossIds.split(";");
				 for(String crossIdAndBureau : crossIdAndBureaus){
					 String[] crossIdBureaus = crossIdAndBureau.split("#");
					 Map<String,String> map = new HashMap<String,String>();
					 map.put("planCrossId",crossIdBureaus[0]);
					 map.put("relevantBureau",crossIdBureaus[1]);
					 planCrossIdList.add(map);
				 }
			 }
			 
			 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
			
			 if(planCrossIdList != null && planCrossIdList.size()>0){
				 for(Map<String,String> map : planCrossIdList){
					 PlanCheckInfo planCheckInfo = new PlanCheckInfo();
					 String planCrossId = map.get("planCrossId");
					 planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
					 planCheckInfo.setPlanCrossId(map.get("planCrossId"));
					 planCheckInfo.setStartDate(startDate);
					 planCheckInfo.setEndDate(endDate);
					 planCheckInfo.setCheckBureau(user.getBureau());
					 planCheckInfo.setCheckDept(user.getDeptName());
					 planCheckInfo.setCheckPeople(user.getUsername()); 
					 
					 int count = runPlanService.savePlanCheckInfo(planCheckInfo); 

					 logger.info("checkCrossRunLine~~~~count==" + count);
					 String relevantBureau = map.get("relevantBureau");
					 //根据planCrossid查询planCheckinfo对象
					 List<PlanCheckInfo> list = runPlanService.getPlanCheckInfoForPlanCrossId(planCrossId);
					 if(list != null && list.size() > 0 ){
						 if(relevantBureau.length() == list.size()){
							 //途经局已经全部审核
							 runPlanService.updateCheckTypeForPlanCrossId(planCrossId,2);
						 }else{
							 //部分局已经审核
							 runPlanService.updateCheckTypeForPlanCrossId(planCrossId,1);
						 }
					 }
					 
				 }
			 }
			 
			
			
		 }catch(Exception e){
			 logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	} 
	
	
	/**
	 * 按照交路生成开行计划
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/handleTrainLinesWithCross", method = RequestMethod.POST)
	public Result handleTrainLinesWithCross(@RequestBody Map<String,Object> reqMap){
		Result result  = new Result();
		try{
			
			String startDate =StringUtil.objToStr( reqMap.get("startDate"));
			String endDate = StringUtil.objToStr(reqMap.get("endDate"));
			String planCrossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			logger.debug("startDate==" + startDate);
			logger.debug("endDate==" + endDate);
			logger.debug("planCrossIds==" + planCrossIds);
			if(planCrossIds != null && planCrossIds.length() > 0){
				List<String> planCrossIdList = new ArrayList<String>();
				String[] planCrossIdsArray = planCrossIds.split(",");
				for(String planCrossId : planCrossIdsArray){
					planCrossIdList.add(planCrossId);
				}
				List<ParamDto> listDto = runPlanService.getTotalTrainsForPlanCrossIds(startDate,endDate,planCrossIdList);
				if(listDto != null && listDto.size() > 0){
					String jsonStr = commonService.combinationMessage(listDto);
					//向rabbit发送消息
					amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);
						
				}	
			}
			
			
	    }catch(Exception e){
	    	logger.error("handleTrainLines error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		
		return result;
	}

    /**
     *  生成开行计划
     * @return 正在生成计划的基本交路id
     */
    @RequestMapping(value = "/plantrain/gen", method = RequestMethod.POST)
     public ResponseEntity<List<String>> generatePlanTrainBySchemaId(@RequestBody Map<String, Object> params) {
        logger.debug("generatePlanTrainBySchemaId~~~reqMap = " + params);
    	String baseChartId = MapUtils.getString(params, "baseChartId");
        String startDate = MapUtils.getString(params, "startDate");
        int days = MapUtils.getIntValue(params, "days");
        List<String> unitcrossId = (List<String>) params.get("unitcrossId");
        String msgReceiveUrl = MapUtils.getString(params, "msgReceiveUrl");
        logger.debug("msgReceiveUrl = " + msgReceiveUrl);
        logger.debug("unitcrossId = " + unitcrossId);
        List<String> unitCrossIds = runPlanService.generateRunPlan(baseChartId, startDate, days, unitcrossId, msgReceiveUrl);
        return new ResponseEntity<List<String>>(unitCrossIds, HttpStatus.OK);
    }
    
    /**
    *
    * @return 为生成运行线界面查询数据
    */
   @ResponseBody
   @RequestMapping(value = "/getTrainRunPlansForCreateLine", method = RequestMethod.POST)
    public Result getTrainRunPlansForCreateLine(@RequestBody Map<String, Object> params) {
	   Result result = new Result();
	   String createType = StringUtil.objToStr(params.get("createType"));
	   System.err.println("createType==" + createType);
	   List trainPlans = null;
	   try{
		   //"创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；4:人工添加）"
		   if("0".equals(createType)){
			   
			    trainPlans = runPlanService.getTrainRunPlansForCreateLine(params); 
		   }else if("3".equals(createType)){
			    trainPlans = runPlanService.getTrainRunPlanForLk(params);   
		   }
	       result.setData(trainPlans);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
       
       return result;
   }
   
   
   /**
   * 生成运行线
   * @return 
   */
  @ResponseBody
  @RequestMapping(value = "/createRunPlanForPlanTrain", method = RequestMethod.POST)
   public Result createRunPlanForPlanTrain(@RequestBody Map<String, Object> params) {
	   Result result = new Result(); 
	   List<Map<String, String>> planTrains =  (List<Map<String, String>>)params.get("planTrains");
	   String msgReceiveUrl = (String)params.get("msgReceiveUrl");
	   //通过远程接口生成运行线的list
	   List<ParamDto> listRemoteDto = new ArrayList<ParamDto>();
	   logger.debug("createRunPlanForPlanTrain~~~reqMap==" + params);
	   try{
	       for(Map<String, String> planTrain: planTrains){
	    	   String baseTrainId = planTrain.get("baseTrainId");
	    	   if(baseTrainId != null && !"".equals(baseTrainId)){
	    		   ParamDto pd = new ParamDto();
		    	   pd.setSourceEntityId(baseTrainId);
		    	   pd.setPlanTrainId(planTrain.get("planTrainId"));
		    	   pd.setTime(planTrain.get("day"));
		    	   pd.setMsgReceiveUrl(msgReceiveUrl);
	    		   listRemoteDto.add(pd);   
	    	   }else{
	    		  //调用本地接口直接操作数据库
	    		   //根据planTrainId从表plan_train和表plan_train_stn中查询基本的数据
	    		   String planTrainId = planTrain.get("planTrainId");
	    		     // 查询客运计划主体信息
	    	        Map<String, Object> plan = runPlanService.findPlanInfoByPlanId(planTrainId);
	    	        PlanLineInfoDto planDto = new PlanLineInfoDto(plan);
	    	        MTrainLine mtrainLine = new MTrainLine();
	    	        String dailyPlanId = planDto.getDailyPlanId();
	    	        String id = "";
	    	        if(dailyPlanId != null && !"".equals(dailyPlanId)){
	    	        	 id = planDto.getDailyPlanId();
	    	        	 mtrainLine.setId(id);
	    	        }else{
	    	        	//主键id
	    	        	id = UUID.randomUUID().toString();
	    	        	mtrainLine.setId(id);
	    	        }
	    	       
	    	        mtrainLine.setName(planDto.getTrainName());
	    	        mtrainLine.setOperation("客运");
	    	        mtrainLine.setResourceName("方案外临客");
	    	        //途经局
	    	        mtrainLine.setRouteBureauShortname(planDto.getPassBureau());
	    	        mtrainLine.setSourceBureauName(planDto.getStartBureauFull());
	    	        mtrainLine.setSourceBureauShortname(planDto.getStartBureau());
	    	        mtrainLine.setSourceNodeName(planDto.getStartStn());
	    	        mtrainLine.setSourceTime(planDto.getStartTime());
	    	        mtrainLine.setTargetBureauName(planDto.getEndBureauFull());
	    	        mtrainLine.setTargetBureauShortname(planDto.getEndBureau());
	    	        mtrainLine.setTargetNodeName(planDto.getEndStn());
	    	        mtrainLine.setTargetTime(planDto.getEndTime());
	    	        mtrainLine.setTypeName("");
	    	        //调用后台插入数据或更新数据
	    	        runPlanLkService.insertMTrainLine(mtrainLine);
	    	       
	    	        List<Map<String, Object>> plans = runPlanService.findPlanTimeTableByPlanId(planTrainId);
	    	        for(int i = 0;i<plans.size();i++) {
	    	        	Map<String, Object> map = plans.get(i);
	    	        	MTrainLineStn stn = new MTrainLineStn();
	    	        	stn.setId(UUID.randomUUID().toString());
	    	        	stn.setParentId(id);
	    	        	//局全称
	    	        	stn.setBureauName(MapUtils.getString(map, "BUREAU"));
	    	        	//局简称
	    	        	stn.setBureauShortname(MapUtils.getString(map, "STNBUREAU"));
	    	        	stn.setChildIndex(MapUtils.getInteger(map, "STN_INDEX"));
	    	        	//车站名
	    	        	stn.setName(MapUtils.getString(map, "STN_NAME"));
	    	        	//车次
	    	        	stn.setParentName(planDto.getTrainName());
	    	        	
	    	        	stn.setTrackName(MapUtils.getString(map, "TRACK_NAME",""));
	    	        	
	    	        	if( i == 0 ){
	    	        		//始发站
	    	        		stn.setTime(MapUtils.getString(map, "DPT_TIME"));
	    	        		//保存到数据库
	    	        		runPlanLkService.insertMTrainLineStnSource(stn);
	    	        	}else if( i == plans.size() -1 ){
	    	        		//终到站
	    	        		stn.setTime(MapUtils.getString(map, "ARR_TIME"));
	    	        		runPlanLkService.insertMTrainLineStnTarget(stn);
	    	        	}else {
	    	        		//经由站
	    	        		stn.setSourceTime(MapUtils.getString(map, "ARR_TIME"));
		    	        	stn.setTargetTime(MapUtils.getString(map, "DPT_TIME"));
		    	        	runPlanLkService.insertMTrainLineStnRoute(stn);
	    	        	}
	    	    
	    	        }
	    	        
	    	        Map<String,Object> reqMap = new HashMap<String,Object>();
	    	        reqMap.put("planTrainId", planTrainId);
					 reqMap.put("daylyPlanId",id );
					 //更新表plan_train中字段DAILYPLAN_FLAG值为0

					 planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
					 //向页面推送信息
					 JSONObject jsonMsg = new JSONObject();
						jsonMsg.put("planTrainId", planTrainId);
						jsonMsg.put("createFlag", 1);
						 sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
					 
	    	   }
	       }
	       if(listRemoteDto.size() > 0 ){
	    	   System.err.println("listRemoteDto.size()====" + listRemoteDto.size());
	    	   String jsonStr = commonService.combinationMessage(listRemoteDto); 
	    	   logger.debug("jsonStr====" + jsonStr);
	    	 //向rabbit发送消息
				amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);
	       }
	
			
	   }catch(Exception e){
		   e.printStackTrace();
	   }
      
      return result;
  } 
    
    
}
