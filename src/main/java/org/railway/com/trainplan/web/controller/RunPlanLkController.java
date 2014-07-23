package org.railway.com.trainplan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 临客
 * @author Think
 *
 */
@Controller
@RequestMapping(value = "/runPlanLk")
public class RunPlanLkController {

	 private static Log logger = LogFactory.getLog(RunPlanLkController.class.getName());
	

	 @RequestMapping(value="/addMainPage", method = RequestMethod.GET)
     public String addMainPage() {
		 return "runPlanLk/runPlanLk_add";
     }
}
