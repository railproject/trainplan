package org.railway.com.trainplan.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by star on 5/12/14.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final static Log logger = LogFactory.getLog(IndexController.class);

    @Autowired
    private RunPlanService runPlanService;

    @Autowired
    private RunLineService runLineService;

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return "trainplan/audit";
    }

    @RequestMapping(value = "audit/plan/timetable/{bureau}/{train_id}", method = RequestMethod.GET)
    public ModelAndView timetable(@PathVariable String bureau, @PathVariable String train_id, ModelAndView modelAndView) {
        modelAndView.setViewName("trainplan/timetable");
        List<RunPlanSTNDTO> result = new ArrayList<RunPlanSTNDTO>();
        List<Map<String, Object>> list = runPlanService.findRunPlanStn(train_id);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanSTNDTO(map, bureau));
        }
        modelAndView.addObject("list", result);
        return modelAndView;
    }

    @RequestMapping(value = "audit/line/timetable/{bureau}/{line_id}", method = RequestMethod.GET)
    public ModelAndView yxxTimeTable(@PathVariable String bureau, @PathVariable String line_id, ModelAndView modelAndView) {
        modelAndView.setViewName("trainplan/timetable");
        List<RunPlanSTNDTO> result = new ArrayList<RunPlanSTNDTO>();
        List<Map<String, Object>> lineStn = new ArrayList<Map<String, Object>>();
        Map<String, Object> map =runLineService.getRunLineSTN(line_id);
        Map<String, Map<String, Object>> timeTable = (Map<String, Map<String, Object>>) map.get("scheduleDto");
        lineStn.add(timeTable.get("sourceItemDto"));
        List<Map<String, Object>> routingTable = (List<Map<String, Object>>) timeTable.get("routeItemDtos");
        lineStn.addAll(routingTable);
        lineStn.add(timeTable.get("targetItemDto"));
        for(Map<String, Object> tmp: lineStn) {
            result.add(new RunPlanSTNDTO(tmp, bureau, 0));
        }
        modelAndView.addObject("list", result);
        return modelAndView;
    }

    @RequestMapping(value = "audit/plan/routing", method = RequestMethod.GET)
    public String routing() {
        return "trainplan/routing";
    }

    @RequestMapping(value = "audit/planline/{date}/{bureau}", method = RequestMethod.GET)
    public String canvas(@PathVariable String date, @PathVariable String bureau,
                         @RequestParam(value = "plans") String plans, @RequestParam(value = "lines") String lines) {
        logger.debug("date:" + date);
        logger.debug("bureau:" + bureau);
        logger.debug("plans:" + plans);
        logger.debug("lines:" +lines);
        return "trainplan/planline";
    }

    @RequestMapping(value = "audit/planline", method = RequestMethod.GET)
    public ModelAndView graphic(@RequestParam(value = "date") String date, @RequestParam(value = "bureau") String bureau,
                          @RequestParam(value = "plans", defaultValue = "") String plans,
                          @RequestParam(value = "lines", defaultValue = "") String lines,
                          ModelAndView modelAndView) throws JsonProcessingException {
        modelAndView.setViewName("trainplan/planline");
        if(plans.endsWith(",")) {
            plans = plans.substring(0, plans.length() - 1);
        }
        if(lines.endsWith(",")) {
            lines = lines.substring(0, lines.length() - 1);
        }
        // runplan
        ObjectMapper objectMapper = new ObjectMapper();
        List<PlanLineDTO> runplan = new ArrayList<PlanLineDTO>();
        List<Map<String, Object>> planList = runPlanService.findRunPlans(plans);
        List<Map<String, Object>> planLineY = runPlanService.findPlanLineSTNs(plans);
        List<String> startList = new ArrayList<String>();
        List<java.sql.Timestamp> endList = new ArrayList<java.sql.Timestamp>();
        PlanLineDTO planLineDTO = null;
        for(int i = 0; i < planList.size(); i++) {
            Map<String, Object> row = planList.get(i);
            if(planLineDTO == null || !planLineDTO.getTrainName().equals(MapUtils.getString(row, "TRAIN_NBR"))) {
                planLineDTO = new PlanLineDTO(planList.get(i));
                runplan.add(planLineDTO);
                startList.add(MapUtils.getString(row, "RUN_DATE"));
            }
            PlanLineSTNDTO planLineSTNDTO = new PlanLineSTNDTO(row);
            planLineDTO.getTrainStns().add(planLineSTNDTO);
            endList.add((java.sql.Timestamp) row.get("ARR_TIME"));
            endList.add((java.sql.Timestamp) row.get("DPT_TIME"));
        }
        String runplanStr = objectMapper.writeValueAsString(runplan);
        logger.debug("runplan: " + runplanStr);
        modelAndView.addObject("runplan", runplanStr);

        // runline
        String[] line_ids = lines.split(",");
        List<PlanLineDTO> runline = new ArrayList<PlanLineDTO>();
        for(String line_id: line_ids) {
            Map<String, Object> l1 =runLineService.getRunLineSTN(line_id);
            PlanLineDTO pld = new PlanLineDTO();
            pld.setTrainName(MapUtils.getString(l1, "name", ""));
            pld.setStartStn(MapUtils.getString(l1, "sourceNodeName", ""));
            pld.setEndStn(MapUtils.getString(l1, "targetBureauName", ""));
            List<PlanLineSTNDTO> trainStns = new ArrayList<PlanLineSTNDTO>();
            pld.setTrainStns(trainStns);

            List<Map<String, Object>> lineStn = new ArrayList<Map<String, Object>>();
            Map<String, Map<String, Object>> timeTable = (Map<String, Map<String, Object>>) l1.get("scheduleDto");
            lineStn.add(timeTable.get("sourceItemDto"));
            List<Map<String, Object>> routingTable = (List<Map<String, Object>>) timeTable.get("routeItemDtos");
            lineStn.addAll(routingTable);
            lineStn.add(timeTable.get("targetItemDto"));
            for(Map<String, Object> tmp: lineStn) {
                PlanLineSTNDTO planLineSTNDTO = new PlanLineSTNDTO(tmp, 1);
                trainStns.add(planLineSTNDTO);
            }
        }
        String runlineStr = objectMapper.writeValueAsString(runplan);
        logger.debug("runline: " + runlineStr);
        modelAndView.addObject("runline", runlineStr);


        // Grid
        List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
        for(Map<String, Object> map: planLineY) {
            planLineGridYList.add(new PlanLineGridY(MapUtils.getString(map, "STN_NAME")));
        }

        Collections.sort(startList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                LocalDate d1 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o1);
                LocalDate d2 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o2);
                return d1.compareTo(d2);
            }
        });

        Collections.sort(endList, new Comparator<java.sql.Timestamp>() {
            @Override
            public int compare(java.sql.Timestamp o1, java.sql.Timestamp o2) {
                return o1.compareTo(o2);
            }
        });
        LocalDate ss = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startList.get(0));
        Date eee = new Date(endList.get(endList.size() - 1).getTime());
        LocalDate ee = new LocalDate(eee);
        List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();
        while(!ss.isAfter(ee)) {
            gridXList.add(new PlanLineGridX(ss.toString("yyyy-MM-dd")));
            ss = ss.plusDays(1);
        }
        PlanLineGrid grid = new PlanLineGrid(gridXList, planLineGridYList);
        String gridStr = objectMapper.writeValueAsString(grid);
        logger.debug("grid: " + gridStr);
        modelAndView.addObject("grid", gridStr);


        return modelAndView;
    }
}
