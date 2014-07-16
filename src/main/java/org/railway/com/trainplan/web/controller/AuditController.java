package org.railway.com.trainplan.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.railway.com.trainplan.entity.PlanCross;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.service.*;
import org.railway.com.trainplan.web.dto.ChartDto;
import org.railway.com.trainplan.web.dto.PlanLineCheckResultDto;
import org.railway.com.trainplan.web.dto.RunPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 审核相关不涉及页面跳转rest接口
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
    private RunLineService runLineService;

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "plan/runplan/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<RunPlanDTO>> getRunPlan(@PathVariable String date, @PathVariable int type,
                                                       @RequestParam(defaultValue = "") String name,
                                                       @RequestParam(defaultValue = "") int trainType) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        logger.debug("-X GET plan/runplan/");
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        List<Map<String, Object>> list =  runPlanService.findRunPlan(date, user.getBureauShortName(), name, type, trainType);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanDTO(map));
        }
        logger.debug("getRunPlan end::::");
        return new ResponseEntity<List<RunPlanDTO>>(result, HttpStatus.OK);
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
    @RequiresPermissions("JHPT.RJH.KDSP")//局客运调度权限
    public ResponseEntity<List<Map<String, Object>>> checkLev1(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev1(data, user, checkType);
        return new ResponseEntity<List<Map<String, Object>>>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "plan/checklev2/{checkType}", method = RequestMethod.POST)
    @RequiresPermissions("JHPT.RJH.KDSP")//局值班主任
    public ResponseEntity<List<Map<String, Object>>> checkLev2(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev2(data, user, checkType);
        return new ResponseEntity<List<Map<String, Object>>>(resp, HttpStatus.OK);
    }


    @RequestMapping(value = "plan/chart/traintype/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getTrainType(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        params.put("type", type);
        params.put("name", name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> result = chartService.getPlanTypeChart(params);
        List<ChartDto> charts = Lists.newArrayList();

        ChartDto chart1 = new ChartDto();
        chart1.setName("开行");
        chart1.setCount(MapUtils.getIntValue(result, "RUNNING", 0));
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("备用");
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
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 上图状态统计图接口
     * @param date 日期，格式：yyyymmdd
     * @param type 是否高线，0：既有线，1：高线
     * @param name 车次
     * @return 统计结果
     */
    @RequestMapping(value = "plan/chart/planline/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getPlanLine(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        params.put("type", type);
        params.put("name", name);
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
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 测试接口，查询unitcross列表
     * @return 查询结果
     */
    @RequestMapping(value = "plancross/test", method = RequestMethod.GET)
    public ResponseEntity<List<PlanCross>> getUnitCross() {
        List<PlanCross> result = runPlanService.findPlanCross();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<List<PlanCross>>(result, httpHeaders, HttpStatus.OK);
    }

    /**
     * 测试接口，查询计划列表
     * @return 查询结果
     */
    @RequestMapping(value = "runplan/test", method = RequestMethod.GET)
    public ResponseEntity<List<RunPlan>> getRunPlan() {
        List<RunPlan> result = runPlanService.findRunPlan();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<List<RunPlan>>(result, httpHeaders, HttpStatus.OK);
    }

    /**
     * 测试接口，生成开行计划
     * @param date 开始日期
     * @param days 偏移量
     * @param planCrossId 可选参数，可指定生成某个交路
     * @return 生成结果
     */
    @RequestMapping(value = "runplan/generate/{date}/{days}/{planCrossId}")
    public ResponseEntity<Integer> generateRunPlan(@PathVariable String date, @PathVariable int days, @PathVariable String planCrossId) {
        List<String> list = Lists.newArrayList();
        list.add(planCrossId);
        int i = runPlanService.generateRunPlan(list, date, days);
        return new ResponseEntity<Integer>(i, HttpStatus.OK);
    }

    /**
     * 一级审核统计图
     * @param date 日期
     * @param type 是否高线，0：否，1：是
     * @param name 车次
     * @return 统计结果
     */
    @RequestMapping(value = "plan/chart/lev1check/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getLev1CheckPie(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        params.put("type", type);
        params.put("name", name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> result = chartService.getLev1CheckCount(params);
        List<ChartDto> charts = Lists.newArrayList();

        ChartDto chart1 = new ChartDto();
        chart1.setName("已审核");
        chart1.setCount(MapUtils.getIntValue(result, "CHECKED", 0));
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未审核");
        chart2.setCount(MapUtils.getIntValue(result, "UNCHECKED", 0));
        charts.add(chart2);
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 二级审核统计图
     * @param date 日期
     * @param type 是否高线，0：否， 1：是
     * @param name 车次
     * @return 统计结果
     */
    @RequestMapping(value = "plan/chart/lev2check/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getLev2CheckPie(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("bureau", user.getBureauShortName());
        params.put("type", type);
        params.put("name", name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> result = chartService.getLev2CheckCount(params);
        List<ChartDto> charts = Lists.newArrayList();

        ChartDto chart1 = new ChartDto();
        chart1.setName("已审核");
        chart1.setCount(MapUtils.getIntValue(result, "CHECKED", 0));
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未审核");
        chart2.setCount(MapUtils.getIntValue(result, "UNCHECKED", 0));
        charts.add(chart2);
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 查询找不到对应客运计划的运行线数量（冗余运行线数量）
     * @param date 日期
     * @return 数量
     */
    @RequestMapping(value = "check/line/{date}/unknown", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findUnknownRunLine(@PathVariable String date) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> result;
        try {
            result = runLineService.findUnknownRunLineCount(user.getBureauShortName(), date);
        } catch (ParseException e) {
            logger.error("findUnknownRunLine", e);
            Map<String, Object> error = Maps.newHashMap();
            error.put("code", "500");
            error.put("message", "日期格式错误");
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("findUnknownRunLine", e);
            Map<String, Object> error = Maps.newHashMap();
            error.put("code", "500");
            error.put("message", "查询冗余运行线出错");
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
}
