package org.railway.com.trainplan.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.railway.com.trainplan.entity.PlanCross;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.UnitCross;
import org.railway.com.trainplan.service.ChartService;
import org.railway.com.trainplan.service.PlanLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.ChartDto;
import org.railway.com.trainplan.web.dto.PlanLineCheckResultDto;
import org.railway.com.trainplan.web.dto.RunPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    private final static Log logger = LogFactory.getLog(AuditController.class);

    @Autowired
    private RunPlanService runPlanService;

    @Autowired
    private PlanLineService planLineService;

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "plan/runplan/{date}/{type}", method = RequestMethod.GET)
    public List<RunPlanDTO> getRunPlan(@PathVariable String date, @PathVariable int type) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        logger.debug(String.format("-X GET plan/runplan/", new Object[]{date, type}));
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        List<Map<String, Object>> list =  runPlanService.findRunPlan(date, user.getBureauShortName(), type);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanDTO(map));
        }
        logger.debug("getRunPlan end::::");
        return result;
    }

    @RequestMapping(value = "plan/{planId}/line/{lineId}/check", method = RequestMethod.GET)
    @RequiresRoles({"局客运调度", "局值班主任"})
    public PlanLineCheckResultDto checkPlanLine(@PathVariable String planId, @PathVariable String lineId) {
        logger.debug("checkPlanLine::: - planId: " + planId + " - lineId: " + lineId);
        PlanLineCheckResultDto result = new PlanLineCheckResultDto();
        // 检查列车信息
        result.setIsTrainInfoMatch(planLineService.checkTrainInfo(planId, lineId));
        // 检查时刻表
        result.setIsTimeTableMatch(planLineService.checkTimeTable(planId, lineId));
        // 检查经由
        return result;
    }


    @RequestMapping(value = "plan/checklev1/{checkType}", method = RequestMethod.POST)
    @RequiresRoles("局客运调度")
    public Response checkLev1(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev1(data, user, checkType);
        return Response.ok(resp).build();
    }

    @RequestMapping(value = "plan/checklev2/{checkType}", method = RequestMethod.POST)
    @RequiresRoles("局值班主任")
    public Response checkLev2(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev2(data, user, checkType);
        return Response.ok(resp).build();
    }


    @RequestMapping(value = "plan/chart/traintype/{date}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getTrainType(@PathVariable String date) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> result = chartService.getPlanTypeChart(params);
        List<ChartDto> charts = Lists.newArrayList();

        ChartDto chart1 = new ChartDto();
        chart1.setName("开行");
        chart1.setCount(MapUtils.getIntValue(result, "RUNNING", 0));
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("热备");
        chart2.setCount(MapUtils.getIntValue(result, "BACKUP", 0));
        charts.add(chart2);

        ChartDto chart3 = new ChartDto();
        chart3.setName("停运");
        chart3.setCount(MapUtils.getIntValue(result, "STOPPED", 0));
        charts.add(chart3);

        ChartDto chart4 = new ChartDto();
        chart4.setName("未知");
        chart4.setCount(MapUtils.getIntValue(result, "UNKNOWN", 0));
        charts.add(chart4);
        return new ResponseEntity(charts, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "plan/chart/planline/{date}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getPlanLine(@PathVariable String date) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> result = chartService.getPlanLineCount(params);
        List<ChartDto> charts = Lists.newArrayList();

        ChartDto chart1 = new ChartDto();
        chart1.setName("已上图");
        chart1.setCount(MapUtils.getIntValue(result, "LINE", 0));
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未上图");
        chart2.setCount(MapUtils.getIntValue(result, "PLAN", 0));
        charts.add(chart2);

        ChartDto chart3 = new ChartDto();
        chart3.setName("未知");
        chart3.setCount(MapUtils.getIntValue(result, "UNKNOWN", 0));
        charts.add(chart3);
        return new ResponseEntity(charts, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "plancross/test", method = RequestMethod.GET)
    public ResponseEntity<List<PlanCross>> getUnitCross() {
        List<PlanCross> result = runPlanService.findPlanCross();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<List<PlanCross>>(result, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "runplan/test", method = RequestMethod.GET)
    public ResponseEntity<List<RunPlan>> getRunPlan() {
        List<RunPlan> result = runPlanService.findRunPlan();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<List<RunPlan>>(result, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "runplan/generate/{date}/{days}")
    public ResponseEntity generateRunplan(@PathVariable String date, @PathVariable int days) {
        int i = runPlanService.generateRunPlan(null, date, days);
        return new ResponseEntity(i, HttpStatus.OK);
    }
}
