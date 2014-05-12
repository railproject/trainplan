package org.railway.com.trainplan.web.controller;

import java.io.IOException;
import java.util.Map;

import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

 

@Controller
@RequestMapping(value = "/cross")
public class CrossController {
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "cross/cross_manage";
     }
	 
	 @Autowired
	private CrossService crossService;
	
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Result getFullStationTrains( @RequestParam("fileName") MultipartFile file, @RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
			System.out.println("-----------------------haha -------------------------------------");
//				FileOutputStream fos = new FileOutputStream(new File("E:/text.txt"));
			try {
				System.out.println(reqMap.get("plainId"));
				System.out.println(reqMap.get("appointDay"));
				crossService.actionExcel(file.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		return result;
	}
}
