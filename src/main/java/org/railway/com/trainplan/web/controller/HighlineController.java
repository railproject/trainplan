package org.railway.com.trainplan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/highLine")
public class HighlineController {
	 private static Log logger = LogFactory.getLog(HighlineController.class.getName());
	 
	 
	 @RequestMapping(method = RequestMethod.GET)
     public String highLine() {
		 return "highLine/highLine_cross";
     } 
	  
	
}
