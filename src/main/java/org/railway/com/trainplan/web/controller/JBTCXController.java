package org.railway.com.trainplan.web.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.JBTCXService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/jbtcx")
public class JBTCXController {
	private static Log logger = LogFactory.getLog(JBTCXController.class.getName());
	
	
	@Autowired
	private JBTCXService jbtcxService;
	
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "plan/plan_runline_check";
     }
	 
	/**
	 * 统计路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrains", method = RequestMethod.POST)
	public Result queryTrains(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			System.err.println("queryConstructionDetail~~reqMap="+reqMap);
			  
//			//路局全称
//			String startBureauFull = StringUtil.objToStr(reqMap.get("startBureauFull"));
			//调用后台接口
			String string = jbtcxService.getTrans(reqMap);
			Map map = new HashedMap();
			map.put("result", string);
			result.setData(map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
}
