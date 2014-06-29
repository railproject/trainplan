package org.railway.com.trainplan.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/crew")
public class CrewController {

	/**
	 * 车长乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/cz", method = RequestMethod.GET)
	public String pageCrewCz() {
		return "crew/crew_cz";
	}
	
	/**
	 * 司机乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/sj", method = RequestMethod.GET)
	public String pageCrewSj() {
		return "crew/crew_sj";
	}
	
	/**
	 * 机械师乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/jxs", method = RequestMethod.GET)
	public String pageCrewJxs() {
		return "crew/crew_jxs";
	}
}
