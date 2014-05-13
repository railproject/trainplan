package org.railway.com.trainplan.web.controller;

import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.web.dto.RunPlanSTNDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private RunPlanService runPlanService;

    @Autowired
    private RunLineService runLineService;

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return "trainplan/audit";
    }

    @RequestMapping(value = "audit/plan/timetable/{bureau}/{train_id}", method = RequestMethod.GET)
    public ModelAndView timetable(@PathVariable String date, @PathVariable String bureau,
                                  @PathVariable String train_id, ModelAndView modelAndView) {
        modelAndView.setViewName("trainplan/timetable");
        List<RunPlanSTNDTO> result = new ArrayList<RunPlanSTNDTO>();
        List<Map<String, Object>> list = runPlanService.findRunPlanStn(train_id);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanSTNDTO(map, bureau));
        }
        return modelAndView;
    }

    @RequestMapping(value = "audit/plan/routing", method = RequestMethod.GET)
    public String routing() {
        return "trainplan/routing";
    }

    @RequestMapping(value = "audit/planline/{date}/{bureau}/{train_id}", method = RequestMethod.GET)
    public String canvas(@PathVariable String date, @PathVariable String bureau, @PathVariable String train_id) {

        return "trainplan/planline";
    }
}
