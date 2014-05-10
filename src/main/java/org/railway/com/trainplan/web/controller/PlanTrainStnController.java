package org.railway.com.trainplan.web.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PlanTrainStnController {
	private static Log logger = LogFactory.getLog(PlanTrainStnController.class.getName());

	@Autowired
	private CommonService commonService ;
	
	@ResponseBody
	@RequestMapping(value = "/mytest", method = RequestMethod.POST)
	public Result getTest(@RequestBody Map<String,Object> reqMap) throws Exception{
		Result result = new Result();
		//String ljqc = StringUtil.objToStr(reqMap.get("ljqc"));
		//System.err.println("ljqc == " + ljqc);
		//Ljzd ljzd = commonService.getLjInfo(ljqc);
		return result;
	}
}
