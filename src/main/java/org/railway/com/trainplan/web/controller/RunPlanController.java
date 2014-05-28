package org.railway.com.trainplan.web.controller;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/runPlan")
public class RunPlanController {
	
	 @Autowired
	 private RunPlanService runPlanService;
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String runPlan() {
		 return "runPlan/run_plan";
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
			 List<RunPlanTrainDto> runPlans = runPlanService.getPlanCross(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询运行线出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 

}
