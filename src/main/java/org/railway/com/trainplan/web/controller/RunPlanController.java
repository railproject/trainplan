package org.railway.com.trainplan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanCrossDto;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/runPlan")
public class RunPlanController {
	 private static Log logger = LogFactory.getLog(RunPlanController.class.getName());
	 @Autowired
	 private RunPlanService runPlanService;
	 
	 @Autowired
	 private CommonService commonService ;
	 
	 @Autowired
	 private AmqpTemplate amqpTemplate;
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String runPlan() {
		 return "runPlan/run_plan";
     }
	 
	 @RequestMapping(value="/runPlanGt" ,method = RequestMethod.GET)
     public String runPlanGt() {
		 return "runPlan/run_plan_gt";
     }
	 
	 
	 /**
	  * 跳转到列车运行时刻表编辑页面
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/trainRunTimePage" ,method = RequestMethod.GET)
     public ModelAndView trainRunTimePage(HttpServletRequest request) {
		 return new ModelAndView("runPlan/train_runTime").addObject("trainNbr", request.getParameter("trainNbr"))
		 		.addObject("trainPlanId", request.getParameter("trainPlanId"));
     }
	 
	 
	 @ResponseBody
	 @RequestMapping(value = "/getRunPlans", method = RequestMethod.POST)
	 public Result getRunPlans(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 try{ 
			 List<RunPlanTrainDto> runPlans = runPlanService.getTrainRunPlans(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询运行线出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
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
     *
     * @param baseChartId 基本图id
     * @param startDate 开始日期，格式：yyyyMMdd
     * @param days 天数
     * @param unitcrossId unitcrossid数组
     * @return 正在生成计划的基本交路id
     */
    @RequestMapping(value = "/plantrain/gen", method = RequestMethod.POST)
	public ResponseEntity<List<String>> generatePlanTrainBySchemaId(@RequestParam(value = "baseChartId") String baseChartId,
                                            @RequestParam(value = "startDate") String startDate,
                                            @RequestParam(value = "days") int days,
                                            @RequestParam(value = "unitcrossId", required = false) String[] unitcrossId) {
        List<String> unitCrossIds = runPlanService.generateRunPlan(baseChartId, startDate, days);
        return new ResponseEntity<>(unitCrossIds, HttpStatus.OK);
    }

    @RequestMapping(value = "/plantrain/gen", method = RequestMethod.GET)
    public ResponseEntity<List<String>> testGeneratePlanTrainBySchemaId() {
        List<String> unitCrossIds = runPlanService.generateRunPlan("c1099526-202d-4653-890d-d8b2e2c8c0ae", "20140717", 1);
        return new ResponseEntity<>(unitCrossIds, HttpStatus.OK);
    }
}
