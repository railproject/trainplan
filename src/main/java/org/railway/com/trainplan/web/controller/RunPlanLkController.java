package org.railway.com.trainplan.web.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 临客相关操作
 * @author Think
 *
 */
@Controller
@RequestMapping(value = "/runPlanLk")
public class RunPlanLkController {
	
	 private static Log logger = LogFactory.getLog(RunPlanLkController.class.getName());
		
	 @Autowired
	 private RunPlanLkService runPlanLkService;


	 @RequestMapping(value="/addPage", method = RequestMethod.GET)
     public String addPage() {
		 return "runPlanLk/runPlanLk_add";
     }
	 
	 

	 @RequestMapping(value="/mainPage", method = RequestMethod.GET)
     public String mainPage() {
		 return "runPlanLk/runPlanLk_main";
     }
	 
	 
	 /**
	  * 在表plan_train中查询临客列车信息
	  * @param reqMap
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getPlanTrainLkInfo", method = RequestMethod.POST)
	 public Result getPlanTrainLkInfo(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 logger.debug("getPlanTrainLkInfo~~~reqMap=="+ reqMap);
		 try{ 
			 String isRelationBureau = StringUtil.objToStr(reqMap.get("isRelationBureau"));
			 if("1".equals(isRelationBureau)){
				 //表示需要查询与本局相关的列车
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 String bureau = user.getBureauShortName();
				 reqMap.put("bureau", bureau);
			 }
			 List<RunPlan> runPlans = runPlanLkService.getPlanTrainLkInfo(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询临客运行线出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
	 /**
	  * 查询临客开行情况
	  * @param reqMap
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getTrainLkRunPlans", method = RequestMethod.POST)
	 public Result getTrainLkRunPlans(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 try{ 
			 List<RunPlanTrainDto> runPlans = runPlanLkService.getTrainLkRunPlans(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询临客运行线开行规律出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
	 
	 
}
