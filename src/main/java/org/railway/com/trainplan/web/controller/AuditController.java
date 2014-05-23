package org.railway.com.trainplan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.service.PlanLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.PlanLineCheckResultDto;
import org.railway.com.trainplan.web.dto.RunPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "plan/runplan/{date}/{type}", method = RequestMethod.GET)
    public List<RunPlanDTO> getRunPlan(@PathVariable String date, @PathVariable int type) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        logger.debug(String.format("-X GET plan/runplan/", new Object[]{date, type}));
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        List<Map<String, Object>> list =  runPlanService.findRunPlan(date, user.getBureau(), type);
        for(Map<String, Object> map: list) {
            result.add(new RunPlanDTO(map));
        }
        logger.debug("getRunPlan end::::");
        return result;
    }

    @RequestMapping(value = "plan/{planId}/line/{lineId}/check", method = RequestMethod.GET)
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
    public String checkLev1(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        int result = runPlanService.checkLev1(data, user, checkType);
        if(result == 0) {
            return "ok";
        }
        return "error";
    }
}
