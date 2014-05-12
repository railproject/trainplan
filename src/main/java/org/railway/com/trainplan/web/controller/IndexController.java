package org.railway.com.trainplan.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by star on 5/12/14.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return "trainplan/audit";
    }

    @RequestMapping(value = "audit/timetable", method = RequestMethod.GET)
    public String timetable() {
        return "trainplan/timetable";
    }

    @RequestMapping(value = "audit/routing", method = RequestMethod.GET)
    public String routing() {
        return "trainplan/routing";
    }

    @RequestMapping(value = "audit/mycanvas", method = RequestMethod.GET)
    public String canvas() {
        return "trainplan/mycanvas";
    }
}
