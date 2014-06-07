package org.railway.com.trainplan.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.*;
import org.railway.com.trainplan.exceptions.*;
import org.railway.com.trainplan.repository.mybatis.*;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
public class RunPlanService {

    private static final Log logger = LogFactory.getLog(RunPlanService.class);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private RunPlanStnDao runPlanStnDao;

    @Autowired
    private UnitCrossDao unitCrossDao;

    @Autowired
    private BaseTrainDao baseTrainDao;
    
    @Autowired
    private BaseDao baseDao;

    public List<Map<String, Object>> findRunPlan(String date, String bureau, int type) {
        logger.debug("findRunPlan::::");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("bureau", bureau);
//        map.put("type", type);
        return runPlanDao.findRunPlan(map);
    }

    public List<Map<String, Object>> findPlanTimeTableByPlanId(String planId) {
        logger.debug("findPlanTimeTableByPlanId::::");
        return runPlanDao.findPlanTimeTableByPlanId(planId);
    }

    public Map<String, Object> findPlanInfoByPlanId(String planId) {
        logger.debug("findPlanInfoByPlanId::::");
        return runPlanDao.findPlanInfoByPlanId(planId);
    }

    public List<Map<String, Object>> checkLev1(List<Map<String, Object>> list, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = new ArrayList<Map<String, Object>>();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            List<String> planIdList = Lists.newArrayList();
            for(Map<String, Object> item: list) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
                planIdList.add(record.getPlanId());
            }
            if(planIdList.size() > 0) {
                // 增加审核记录
                runPlanDao.addCheckHis(params);
                //修改审核状态和已审核局
                List<Map<String, Object>> planList = runPlanDao.findPlanInfoListByPlanId(planIdList);

                for(Map<String, Object> plan: planList) {

                    try {
                        // 组织返回结果对象
                        Map<String, Object> levResult = new HashMap<String, Object>();
                        levResult.put("id", MapUtils.getString(plan, "PLAN_TRAIN_ID"));

                        int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                        String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU", "");
                        int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
                        String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU", "");
                        String passBureau = MapUtils.getString(plan, "PASS_BUREAU", "");
                        // 计算一级已审核局
                        String checkedLev1Bureau = addBureauCode(lev1Bureau, user.getBureauShortName());
                        // 计算新1级审核状态
                        int newLev1Type = newLev1Type(lev1Type, passBureau, lev1Bureau, user.getBureauShortName());
                        // 一级审核完后，二级审核需要重新审核
                        int newLev2Type = 0;
                        // 二级已审核局也应该是空
                        String checkedLev2Bureau = "";
                        Map<String, Object> updateParam = new HashMap<String, Object>();
                        updateParam.put("lev1Type", newLev1Type);
                        updateParam.put("lev1Bureau", checkedLev1Bureau);
                        updateParam.put("lev2Type", newLev2Type);
                        updateParam.put("lev2Bureau", checkedLev2Bureau);
                        updateParam.put("planId", MapUtils.getString(plan, "PLAN_TRAIN_ID"));
                        runPlanDao.updateCheckInfo(updateParam);
                        // 本局一级审核状态
                        levResult.put("checkLev1", newLev1Type);
                        // 本局二级审核状态
                        levResult.put("checkLev2", newLev2Type);
                        // 本局一级审核是否已审核
                        levResult.put("lev1Checked", 1);
                        // 本局二级审核是否已审核
                        levResult.put("lev2Checked", 0);
                        checkLev1Result.add(levResult);
                    } catch (DailyPlanCheckException e) {
                        logger.error(e);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
        }
        return checkLev1Result;
    }

    public List<Map<String, Object>> checkLev2(List<Map<String, Object>> plans, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = new ArrayList<Map<String, Object>>();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = new ArrayList<LevelCheck>();
            List<String> planIdList = Lists.newArrayList();
            for(Map<String, Object> item: plans) {
                LevelCheck record = new LevelCheck(UUID.randomUUID().toString(), user.getName(), now, user.getDeptName(), user.getBureau(), checkType, MapUtils.getString(item, "planId"), MapUtils.getString(item, "lineId"));
                params.add(record);
                planIdList.add(record.getPlanId());
            }
            if(planIdList.size() > 0) {
                // 增加审核记录
                runPlanDao.addCheckHis(params);
                //修改审核状态和已审核局
                List<Map<String, Object>> planList = runPlanDao.findPlanInfoListByPlanId(planIdList);

                for(Map<String, Object> plan: planList) {

                    try {
                        // 组织返回结果对象
                        Map<String, Object> levResult = new HashMap<String, Object>();
                        levResult.put("id", MapUtils.getString(plan, "PLAN_TRAIN_ID"));

                        int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                        String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU", "");
                        int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
                        String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU", "");
                        String passBureau = MapUtils.getString(plan, "PASS_BUREAU");
                        // 计算二级已审核局
                        String checkedLev2Bureau = addBureauCode(lev2Bureau, user.getBureauShortName());
                        // 计算新1级审核状态
                        int newLev2Type = newLev1Type(lev2Type, passBureau, lev2Bureau, user.getBureauShortName());
                        // 一级审核完后，二级审核需要重新审核
//                        int newLev2Type = 0;
//                        // 二级已审核局也应该是空
//                        String checkedLev2Bureau = "";
                        Map<String, Object> updateParam = new HashMap<String, Object>();
                        updateParam.put("lev1Type", lev1Type);
                        updateParam.put("lev1Bureau", lev1Bureau);
                        updateParam.put("lev2Type", newLev2Type);
                        updateParam.put("lev2Bureau", checkedLev2Bureau);
                        updateParam.put("planId", MapUtils.getString(plan, "PLAN_TRAIN_ID"));
                        runPlanDao.updateCheckInfo(updateParam);
                        // 本局一级审核状态
//                        levResult.put("checkLev1", newLev1Type);
                        // 本局二级审核状态
                        levResult.put("checkLev2", newLev2Type);
                        // 本局一级审核是否已审核
//                        levResult.put("lev1Checked", 1);
                        // 本局二级审核是否已审核
                        levResult.put("lev2Checked", 1);
                        checkLev1Result.add(levResult);
                    } catch (DailyPlanCheckException e) {
                        logger.error(e);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("checkLev1::::::", e);
        }
        return checkLev1Result;
    }

    /**
     * 路局审核了需要修改已审核路局局码字段
     * @param base 已有局
     * @param split 新增局
     * @return 最终字符
     */
    private String addBureauCode(String base, String split) throws WrongDataException {
        StringBuilder result = new StringBuilder(base);
        if(!base.contains(split)) {
            result.append(split);
        } else {
            throw new WrongDataException(split + " 局已审核过该计划，不能重复审核");
        }
        return result.toString();
    }

    private int newLev1Type(int current, String passBureau, String currentChecked, String split) throws DailyPlanCheckException {
        int result;
        if(!passBureau.contains(split)) {
            throw new WrongBureauCheckException("计划不属于审核局");
        }
        if(current == 2) {
            throw new WrongCheckTypeException("该计划已经审核过");
        }
        if(current == 1 || current == 0) {
            result = computeLev1Type(current, passBureau, addBureauCode(currentChecked, split));
        } else {
            throw new UnknownCheckTypeException("未知审核类型");
        }
        return result;
    }

    private int computeLev1Type(int current, String passBureau, String checkedBureau) throws WrongDataException {
        if(current != 2 && isAllChecked(passBureau, checkedBureau)) {
            return 2;
        }
        if(current == 1 && !isAllChecked(passBureau, checkedBureau)) {
            return 1;
        }
        if(current == 2 && isAllChecked(passBureau, checkedBureau)) {
            throw new WrongDataException("该计划已审核完毕，不能再次审核");
        }
        return 0;
    }

    /**
     * 判断是否所有路局都审核完毕
     * @param standard 判断标准
     * @param token 需要判断的字符
     * @return 是否全部包含
     */
    private boolean isAllChecked(String standard, String token) throws WrongDataException {
        StringBuilder tStandard = new StringBuilder(standard);
        if(standard.length() != token.length()) {
            return false;
        }
        if(standard.length() < token.length()) {
            throw new WrongDataException("已审核路局比途经局多，请联系管理员");
        }
        while (tStandard.length() > 0) {
            String ele = tStandard.substring(0, 1);
            tStandard = new StringBuilder(tStandard.substring(1));
            if(!token.contains(ele)) {
                return false;
            }
        }
        return true;
    }
    
    
	public List<RunPlanTrainDto> getTrainRunPlans(Map<String , Object> map) throws Exception {
			List<RunPlanTrainDto> runPlans = new ArrayList<RunPlanTrainDto>();
			Map<String, RunPlanTrainDto> runPlanTrainMap = new HashMap<String, RunPlanTrainDto>();
			List<CrossRunPlanInfo> crossRunPlans = baseDao.selectListBySql(Constants.GET_TRAIN_RUN_PLAN, map);
			String startDay = map.get("startDay").toString();
			String endDay = map.get("endDay").toString();
			for(CrossRunPlanInfo runPlan: crossRunPlans){
					RunPlanTrainDto currTrain =  runPlanTrainMap.get(runPlan.getTrainNbr());
					if(currTrain == null){
						currTrain = new RunPlanTrainDto(startDay, endDay);
						currTrain.setTrainNbr(runPlan.getTrainNbr()); 
						runPlanTrainMap.put(runPlan.getTrainNbr(), currTrain);
					} 
					currTrain.setRunFlag(runPlan.getRunDay(), runPlan.getRunFlag());
			} 
			runPlans.addAll(runPlanTrainMap.values());   
			return runPlans;
	}

	public List<RunPlanTrainDto> getPlanCross(Map<String, Object> reqMap) {
		
		return baseDao.selectListBySql(Constants.GET_PLAN_CROSS, reqMap);
	}


    public List<PlanCross> findPlanCross() {
        try {
            return unitCrossDao.findPlanCross(null);
        } catch (Exception e) {
            logger.error("findPlanCross:::::", e);
        }
        return null;
    }


    public List<RunPlan> findRunPlan() {
        try {
            return baseTrainDao.findBaseTrainByPlanCrossid(null);
        } catch (Exception e) {
            logger.error("findRunPlan:::::", e);
        }
        return null;
    }

    /**
     *
     * @param planCrossIdList 指定生成plancross计划
     * @param startDate yyyy-MM-dd
     * @param days 比如:30
     * @return 生成了多少个plancross的计划
     */
    public int generateRunPlan(List<String> planCrossIdList, String startDate, int days) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<PlanCross> planCrossList = null;
        try{
            planCrossList = unitCrossDao.findPlanCross(planCrossIdList);
            for(PlanCross planCross: planCrossList) {
                executorService.execute(new RunPlanGenerator(planCross, runPlanDao, baseTrainDao, startDate, runPlanStnDao, days));
            }
        } finally {
            executorService.shutdown();
        }
        return planCrossList.size();
    }

    public List<String> generateRunPlan(List<String> planCrossIdList, String startDate, int days, Map<String, Object> callBackParams) {
        ExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(50));
        List<String> planCrossIds = Lists.newArrayList();
        try{
            List<PlanCross> planCrossList = unitCrossDao.findPlanCross(planCrossIdList);
            for(PlanCross planCross: planCrossList) {
//                executorService.execute(new RunPlanGenerator(planCross, runPlanDao, baseTrainDao, startDate, runPlanStnDao, days));
//                ListenableFuture futureTask = executorService.submit(new RunPlanGenerator(planCross, runPlanDao, baseTrainDao, startDate, runPlanStnDao, days));
//                futureTask.addListener(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, executorService);
                planCrossIds.add(planCross.getPlanCrossId());
            }
        } finally {
            executorService.shutdown();
        }
        return planCrossIds;
    }


    class RunPlanGenerator implements Runnable {

        // 传入参数
        private PlanCross planCross;

        // 保存客运计划用
        private RunPlanDao runPlanDao;

        private RunPlanStnDao runPlanStnDao;

        // 查询基本图数据用
        private BaseTrainDao baseTrainDao;

        private String startDate;

        private int days;

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RunPlanGenerator(PlanCross planCross, RunPlanDao runPlanDao,
                         BaseTrainDao baseTrainDao, String startDate,
                         RunPlanStnDao runPlanStnDao, int days) {

            this.planCross = planCross;
            this.runPlanDao = runPlanDao;
            this.baseTrainDao = baseTrainDao;
            this.runPlanStnDao = runPlanStnDao;
            this.startDate = startDate;
            this.days = days;
        }

        @Override
        @Transactional
        @Monitored
        public void run() {
            List<UnitCrossTrain> unitCrossTrainList = this.planCross.getUnitCrossTrainList();
            String planCrossId = planCross.getPlanCrossId();
            Map<String, Object> params = Maps.newHashMap();
            params.put("planCrossId", planCrossId);
            List<RunPlan> baseRunPlanList = baseTrainDao.findBaseTrainByPlanCrossid(params);
            List<RunPlan> resultRunPlanList = Lists.newArrayList();
            LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(startDate);

            resultRunPlanList.addAll(generateRunPlan(start, unitCrossTrainList, baseRunPlanList, planCrossId, this.days));

            for(RunPlan runPlan: resultRunPlanList) {
                try {
                    List<RunPlanStn> runPlanStnList = runPlan.getRunPlanStnList();
                    runPlanStnDao.addRunPlanStn(runPlanStnList);
                    runPlanDao.addRunPlan(runPlan);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }

        private List<RunPlan> generateRunPlan(LocalDate startDate, List<UnitCrossTrain> unitCrossTrainList,
                                              List<RunPlan> baseRunPlanList, String planCrossId, int days) {
            List<RunPlan> resultList = Lists.newArrayList();
            // 计算结束时间
            LocalDate lastDate = startDate.plusDays(days);
            // 开始生成
            generate: {
                for(int i = 0; true; i ++) {
                    UnitCrossTrain unitCrossTrain = unitCrossTrainList.get(i % unitCrossTrainList.size());
                    LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                    LocalDate endDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getEndDate());
                    int interval = Days.daysBetween(runDate, endDate).getDays();

                    for(RunPlan baseRunPlan: baseRunPlanList) {
                        if (unitCrossTrain.getBaseTrainId().equals(baseRunPlan.getBaseTrainId())) {
                            try {
                                RunPlan runPlan = (RunPlan) BeanUtils.cloneBean(baseRunPlan);
                                // 克隆的对象里，列表没有克隆，重新new一个列表
                                runPlan.setRunPlanStnList(new ArrayList<RunPlanStn>());
                                resultList.add(runPlan);

                                // 基本信息
                                runPlan.setPlanTrainId(UUID.randomUUID().toString());
                                runPlan.setPlanCrossId(planCrossId);
                                runPlan.setBaseChartId(baseRunPlan.getBaseChartId());
                                runPlan.setBaseTrainId(baseRunPlan.getBaseTrainId());
                                runPlan.setDailyPlanFlag(1);

                                // unitcross里的信息
                                runPlan.setGroupSerialNbr(unitCrossTrain.getGroupSerialNbr());
                                runPlan.setTrainSort(unitCrossTrain.getTrainSort());
                                runPlan.setMarshallingName(unitCrossTrain.getMarshallingName());
                                runPlan.setTrainNbr(unitCrossTrain.getTrainNbr());
                                runPlan.setDayGap(unitCrossTrain.getDayGap());
                                runPlan.setSpareFlag(unitCrossTrain.getSpareFlag());
                                runPlan.setSpareApplyFlag(unitCrossTrain.getSpareApplyFlag());
                                runPlan.setHighLineFlag(unitCrossTrain.getHighLineFlag());
                                runPlan.setHightLineRule(unitCrossTrain.getHighLineFlag());
                                runPlan.setCommonLineRule(unitCrossTrain.getCommonLineRule());
                                runPlan.setAppointWeek(unitCrossTrain.getAppointWeek());
                                runPlan.setAppointDay(unitCrossTrain.getAppointDay());

                                // 如果有前车，需要设置接续关系
                                if(resultList.size() > (unitCrossTrainList.size() + 1)) {
                                    RunPlan preRunPlan = resultList.get(resultList.size() - unitCrossTrainList.size());
                                    LocalDate preRunDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(preRunPlan.getRunDate());
                                    runPlan.setRunDate(preRunDate.plusDays(interval).plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));

                                    // 前后车互基
                                    runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                                    preRunPlan.setNextTrainId(runPlan.getPlanTrainId());
                                } else {
                                    LocalDate unitCrossTrainStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                                    int initInterval = Days.daysBetween(unitCrossTrainStartDate, startDate).getDays();
                                    runPlan.setRunDate(unitCrossTrainStartDate.plusDays(initInterval).plusDays(unitCrossTrain.getGroupGap()).toString("yyyyMMdd"));
                                }

                                runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + runPlan.getStartTimeStr()).getTime()));
                                runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + runPlan.getEndTimeStr()).getTime()));
                                runPlan.setPlanTrainSign(runPlan.getRunDate() + "-" + runPlan.getTrainNbr() + "-" + runPlan.getStartStn() + "-" + runPlan.getStartTimeStr());

                                LocalDate runPlanDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate());
                                // 计算计划从表信息
                                List<RunPlanStn> runPlanStnList = baseRunPlan.getRunPlanStnList();
                                for(RunPlanStn baseRunPlanStn: runPlanStnList) {
                                    RunPlanStn runPlanStn = (RunPlanStn) BeanUtils.cloneBean(baseRunPlanStn);
                                    runPlan.getRunPlanStnList().add(runPlanStn);
                                    runPlanStn.setPlanTrainId(runPlan.getPlanTrainId());
                                    runPlanStn.setPlanTrainStnId(UUID.randomUUID().toString());
                                    runPlanStn.setArrTime(new Timestamp(simpleDateFormat.parse(runPlanDate.plusDays(runPlanStn.getRunDays()).toString() + " " + runPlanStn.getArrTimeStr()).getTime()));
                                    runPlanStn.setDptTime(new Timestamp(simpleDateFormat.parse(runPlanDate.plusDays(runPlanStn.getRunDays()).toString() + " " + runPlanStn.getDptTimeStr()).getTime()));
                                    runPlanStn.setBaseArrTime(runPlanStn.getArrTime());
                                    runPlanStn.setBaseDptTime(runPlanStn.getDptTime());
                                }

                                // 如果有一组车的开始日期到了计划最后日期，就停止生成
                                if(lastDate.equals(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()))) {
                                    break generate;
                                }
                                break;
                            } catch (Exception e) {
                                logger.error("生成客运计划出错", e);
                            }
                        }
                    }

                }
            }
            return resultList;
        }
    }
}
