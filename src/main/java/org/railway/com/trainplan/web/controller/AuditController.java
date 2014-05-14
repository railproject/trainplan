package org.railway.com.trainplan.web.controller;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.web.dto.RunLineDTO;
import org.railway.com.trainplan.web.dto.RunPlanDTO;
import org.railway.com.trainplan.web.dto.RunPlanSTNDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    @Autowired
    private RunPlanService runPlanService;

    @Autowired
    private RunLineService runLineService;

    @RequestMapping(value = "plan/runplan/{date}/{bureau}/{type}", method = RequestMethod.GET)
    public List<RunPlanDTO> getRunPlan(@PathVariable String date, @PathVariable String bureau, @PathVariable int type) {
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        List<Map<String, Object>> list =  runPlanService.findRunPlan(date, bureau, type);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanDTO(map));
        }
        return result;
    }

    @RequestMapping(value = "plan/runplan/stn/{bureau}/{train_id}", method = RequestMethod.GET)
    public List<RunPlanSTNDTO> getRunPlanSTN(@PathVariable String bureau, @PathVariable String train_id) {
        List<RunPlanSTNDTO> result = new ArrayList<RunPlanSTNDTO>();
        List<Map<String, Object>> list = runPlanService.findRunPlanStn(train_id);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanSTNDTO(map, bureau));
        }
        return result;
    }

    @RequestMapping(value = "plan/runline/{date}/{bureau}/{type}", method = RequestMethod.GET)
    public List getRunLinePlan(@PathVariable String date, @PathVariable String bureau, @PathVariable int type) {
        LocalDate ld = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(date);
        List<RunLineDTO> result = new ArrayList<RunLineDTO>();
        List<Map<String, Object>> list = runLineService.getRunLine(ld.toString("yyyy-MM-dd"), bureau);
        for(Map<String, Object> map: list) {
            RunLineDTO runLineDTO = new RunLineDTO(map);
            runLineDTO.setRunDate(ld.toString("MM-dd"));
            result.add(runLineDTO);
        }
        return result;
    }
}
