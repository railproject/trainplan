package org.railway.com.trainplan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.record.formula.functions.Int;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.*;
import org.railway.com.trainplan.exceptions.*;
import org.railway.com.trainplan.repository.mybatis.*;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanCrossDto;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客运计划服务类
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

    @Autowired
    private CrossService crossService;

    @Autowired
    private SendMsgService msgService;

    @Autowired
    private PlanCrossDao planCrossDao;

    @Value("#{restConfig['plan.generatr.thread']}")
    private int threadNbr;

    /**
     * 查询客运计划列表（开行计划列表）
     * @param date 日期
     * @param bureau 所属局
     * @param name 车次
     * @param type 查询类型，详看下面switch
     * @param trainType 是否高线
     * @return 计划列表
     */
    public List<Map<String, Object>> findRunPlan(String date, String bureau, String name, int type, int trainType) {
        logger.debug("findRunPlan::::");
        Map<String, Object> map = Maps.newHashMap();
        map.put("date", date);
        map.put("bureau", bureau);
        map.put("name", name);
        if(trainType == 1) {
            map.put("trainType", trainType);
        }
        List<Map<String, Object>> list = Lists.newArrayList();
        switch(type) {
            case 0:
                list = runPlanDao.findRunPlan_all(map);
                break;
            case 1:
                list = runPlanDao.findRunPlan_sfzd(map);
                break;
            case 2:
                list = runPlanDao.findRunPlan_sfjc(map);
                break;
            case 3:
                list = runPlanDao.findRunPlan_jrzd(map);
                break;
            case 4:
                list = runPlanDao.findRunPlan_jrjc(map);
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * 根据列车id查询列车时刻表
     * @param planId 列车id
     * @return 时刻表
     */
    public List<Map<String, Object>> findPlanTimeTableByPlanId(String planId) {
        logger.debug("findPlanTimeTableByPlanId::::");
        return runPlanDao.findPlanTimeTableByPlanId(planId);
    }

    /**
     * 根据列车id查询列车信息
     * @param planId 列车id
     * @return 列车信息
     */
    public Map<String, Object> findPlanInfoByPlanId(String planId) {
        logger.debug("findPlanInfoByPlanId::::");
        return runPlanDao.findPlanInfoByPlanId(planId);
    }

    /**
     * 一级审核
     * @param list 列车列表
     * @param user 当前审核
     * @param checkType 审核类型
     * @return 审核结果
     */
    public List<Map<String, Object>> checkLev1(List<Map<String, Object>> list, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = Lists.newArrayList();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = Lists.newArrayList();
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
                        Map<String, Object> levResult = Maps.newHashMap();
                        levResult.put("id", MapUtils.getString(plan, "PLAN_TRAIN_ID"));

                        int lev1Type = MapUtils.getIntValue(plan, "CHECK_LEV1_TYPE");
                        String lev1Bureau = MapUtils.getString(plan, "CHECK_LEV1_BUREAU", "");
//                        int lev2Type = MapUtils.getIntValue(plan, "CHECK_LEV2_TYPE");
//                        String lev2Bureau = MapUtils.getString(plan, "CHECK_LEV2_BUREAU", "");
                        String passBureau = MapUtils.getString(plan, "PASS_BUREAU", "");
                        // 计算一级已审核局
                        String checkedLev1Bureau = addBureauCode(lev1Bureau, user.getBureauShortName());
                        // 计算新1级审核状态
                        int newLev1Type = newLev1Type(lev1Type, passBureau, lev1Bureau, user.getBureauShortName());
                        // 一级审核完后，二级审核需要重新审核
                        int newLev2Type = 0;
                        // 二级已审核局也应该是空
                        String checkedLev2Bureau = "";
                        Map<String, Object> updateParam = Maps.newHashMap();
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

    /**
     * 二级审核
     * @param plans 列车列表
     * @param user 当前审核
     * @param checkType 审核类型
     * @return 审核结果
     */
    public List<Map<String, Object>> checkLev2(List<Map<String, Object>> plans, ShiroRealm.ShiroUser user, int checkType) {
        List<Map<String, Object>> checkLev1Result = Lists.newArrayList();
        try {
            // 准备参数
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            List<LevelCheck> params = Lists.newArrayList();
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
                        Map<String, Object> levResult = Maps.newHashMap();
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
                        Map<String, Object> updateParam = Maps.newHashMap();
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

    /**
     * 计算新一级审核状态
     * @param current 当前状态
     * @param passBureau 经由局
     * @param currentChecked 当前已审核局
     * @param split 当前局
     * @return 新状态
     * @throws DailyPlanCheckException
     */
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

    /**
     * 是否已审核完
     * @param current 当前审核状态
     * @param passBureau 经由局
     * @param checkedBureau 已审核局
     * @return 审核状态
     * @throws WrongDataException
     */
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
        if(current != 2 && passBureau.contains(checkedBureau)) {
            return 1;
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
			List<RunPlanTrainDto> runPlans = Lists.newArrayList();
			Map<String, RunPlanTrainDto> runPlanTrainMap = Maps.newHashMap();
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
	        
	@SuppressWarnings("unchecked")
    public List<PlanCrossDto> getPlanCross(Map<String, Object> reqMap) {
		
		return baseDao.selectListBySql(Constants.GET_PLAN_CROSS, reqMap);
	}

    /**
     *
     * @param baseChartId 基本图id
     * @param startDate yyyy-MM-dd
     * @param days 比如:30
     * @return 生成了多少个plancross的计划
     */
    public List<String> generateRunPlan(String baseChartId, String startDate, int days, List<String> unitCrossIds, String msgReceiveUrl) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNbr);
        Map<String, Object> params = Maps.newHashMap();
        params.put("baseChartId", baseChartId);
        params.put("unitCrossIds", unitCrossIds);
        List<UnitCross> unitCrossList = unitCrossDao.findUnitCrossBySchemaId(params);
        List<String> unitCrossIdList = Lists.newArrayList();
        try{
            for(UnitCross unitCross: unitCrossList) {
                executorService.execute(new RunPlanGenerator(unitCross, runPlanDao, baseTrainDao, startDate, runPlanStnDao, days - 1, msgService, msgReceiveUrl));
                unitCrossIdList.add(unitCross.getUnitCrossId());
            }
        } finally {
            executorService.shutdown();
        }
        return unitCrossIdList;
    }

    class RunPlanGenerator implements Runnable {

        // 传入参数
        private UnitCross unitCross;

        // 保存客运计划用
        private RunPlanDao runPlanDao;

        private RunPlanStnDao runPlanStnDao;

        // 查询基本图数据用
        private BaseTrainDao baseTrainDao;

        private String startDate;

        private int days;

        private SendMsgService msgService;

        private String msgReceiveUrl;

        private List<RunPlan> baseRunPlanList;

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RunPlanGenerator(UnitCross unitCross, RunPlanDao runPlanDao,
                         BaseTrainDao baseTrainDao, String startDate,
                         RunPlanStnDao runPlanStnDao, int days, SendMsgService msgService, String msgReceiveUrl) {

            this.unitCross = unitCross;
            this.runPlanDao = runPlanDao;
            this.baseTrainDao = baseTrainDao;
            this.runPlanStnDao = runPlanStnDao;
            this.startDate = startDate;
            this.days = days;
            this.msgService = msgService;
            this.msgReceiveUrl = msgReceiveUrl;
        }

        @Override
        @Transactional
        @Monitored
        public void run() {
            logger.debug("thread start:" + LocalTime.now().toString("hh:mm:ss"));
            Map<String, Object> params = Maps.newHashMap();
            params.put("unitCrossId", this.unitCross.getUnitCrossId());
            this.baseRunPlanList = baseTrainDao.findBaseTrainByUnitCrossid(params);
            LocalDate start = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(this.startDate);

            try {
                generateRunPlan(start, this.baseRunPlanList);
            } catch (WrongDataException e) {
                logger.error("数据错误：unitCross_id = " + this.unitCross.getPlanCrossId(), e);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成计划失败：unitCross_id = " + this.unitCross.getPlanCrossId(), e);
            }
            logger.debug("thread end:" + LocalTime.now().toString("hh:mm:ss"));
        }

        /**
         * 生成开行计划，可以重复生成。
         * 原则：保证生成的计划是在一个连续的时间区间内。
         * TODO：切换方案重复生成
         * @param startDate 起始日期
         * @param baseRunPlanList 基本图数据
         * @throws WrongDataException
         * @throws Exception
         */
        private void generateRunPlan(LocalDate startDate, List<RunPlan> baseRunPlanList) throws WrongDataException, Exception {
            //生成plan_cross逻辑
            String planCrossId = this.unitCross.getPlanCrossId();
            boolean isNewPlanCrossInfo = false;
            PlanCrossInfo planCrossInfo;
            // 按组别保存最后一个计划
            Map<Integer, RunPlan> lastRunPlans = Maps.newHashMap();
            int initTrainSort = 0;
            if(planCrossId == null) { // 之前未生成过开行计划
                planCrossInfo = new PlanCrossInfo();
                BeanUtils.copyProperties(planCrossInfo, this.unitCross);
                planCrossInfo.setPlanCrossId(UUID.randomUUID().toString());
                planCrossInfo.setCrossStartDate(this.startDate);
                planCrossId = planCrossInfo.getPlanCrossId();
                isNewPlanCrossInfo = true;
            } else { // 之前已经生成过开行计划
                // 按时间删除已存在的开行计划，后面重新生成
                // 查询planCross对象，生成完开行计划后需要更新crossEndDate
                planCrossInfo = crossService.getPlanCrossInfoForPlanCrossId(planCrossId);
                // 查询每组车最新的计划，作为新计划的前序车
                initTrainSort = this.getLastRunPlans(planCrossId, lastRunPlans);
            }
            // 用来保存最后一个交路起点
            RunPlan lastStartPoint = null;
            final List<UnitCrossTrain> unitCrossTrainList = this.unitCross.getUnitCrossTrainList();
            int totalGroupNbr = this.unitCross.getGroupTotalNbr();
            // 计算每组有几个车次
            if(unitCrossTrainList.size() % totalGroupNbr != 0) {
                throw new WrongDataException("交路数据错误，每组车数量不一样");
            }
            // 每组有几个车
            int trainCount = unitCrossTrainList.size() / totalGroupNbr;
            // 计算结束时间
            LocalDate lastDate = startDate.plusDays(this.days);
            // 记录daygap
            int totalDayGap = 0;
            // 一刀切后可能存在不完整交路，先补全不完整的计划
//            lastRunPlans = completeUnitCross(lastRunPlans);

            // 找到最先生成第几组车，然后继续生成下去
            int firstGroup = getFirstEndedGroup(lastRunPlans);
            initTrainSort = (firstGroup - 1) * trainCount;
            // 继续生成
            generate: {
                for(int i = initTrainSort; i < 10000; i ++) {
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

                                RunPlan preRunPlan = lastRunPlans.get(runPlan.getGroupSerialNbr());
                                if(preRunPlan != null) { // 有前序车
                                    // 当前列车的开始日期，是前车的结束日期+daygap
                                    LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                    runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));

                                    // 前后车互基
                                    runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                                    preRunPlan.setNextTrainId(runPlan.getPlanTrainId());
                                } else if(i == 0) { // 第一组第一个车
                                    LocalDate unitCrossTrainStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                                    int initInterval = Days.daysBetween(unitCrossTrainStartDate, startDate).getDays();
                                    runPlan.setRunDate(unitCrossTrainStartDate.plusDays(initInterval).toString("yyyyMMdd"));
                                } else { // 每组第一个车（除了第一组）
                                    LocalDate unitCrossTrainStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                                    int initInterval = Days.daysBetween(unitCrossTrainStartDate, startDate).getDays();
                                    totalDayGap += unitCrossTrain.getGroupGap();
                                    runPlan.setRunDate(unitCrossTrainStartDate.plusDays(initInterval).plusDays(totalDayGap).toString("yyyyMMdd"));
                                }

                                runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + runPlan.getStartTimeStr()).getTime()));
                                runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).plusDays(interval).toString("yyyy-MM-dd") + " " + runPlan.getEndTimeStr()).getTime()));
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

                                // 保存当前处理组的第一个车
                                if(i % trainCount == 0) {
                                    lastStartPoint = runPlan;
                                }
                                // 保存每组车的最后一个车
                                lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);

                                runPlanDao.addRunPlan(runPlan);
                                runPlanStnDao.addRunPlanStn(runPlan.getRunPlanStnList());
                                runPlanDao.updateNextTrainId(preRunPlan);

                                sendRunPlanMsg(this.unitCross.getUnitCrossId(), runPlan);
                                // 如果有一组车的第一辆车的开始日期到了计划最后日期，就停止生成
                                LocalDate lastStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(lastStartPoint.getRunDate());

                                if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) >= 0)) {
                                    planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
                                    break generate;
                                }
                                break;
                            } catch (Exception e) {
                                logger.error("生成客运计划出错", e);
                                sendUnitCrossMsg(this.unitCross.getUnitCrossId(), -1);
                                throw e;
                            }
                        }
                    }

                }
            }
            if(isNewPlanCrossInfo) {
                planCrossDao.save(planCrossInfo);
            } else {
                planCrossDao.update(planCrossInfo);
            }
            sendUnitCrossMsg(this.unitCross.getUnitCrossId(), 2);
        }

        /**
         * 根据plancrossid查询前序列车，日期有重叠的开行计划，按新日期一刀切
         * 1、不管三七二十一，切一刀再说
         * 2、查询每组车的最后一次生成的每个车（可以和unitcrosstrain匹配），通过参数lastRunPlans传出去
         * @param planCrossId 外键plan_cross表id
         * @param lastRunPlans 每组车的最后一个车
         * @return 按groupserianbr保存最新计划
         */
        private int getLastRunPlans(String planCrossId, Map<Integer, RunPlan> lastRunPlans) throws ParseException {
            // 按时间切一刀
            Map<String, Object> params = Maps.newHashMap();
            params.put("startTime", new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(this.startDate).toString("yyyy-MM-dd") + " 00:00:00").getTime()));
            runPlanDao.deleteRunPlanByStartTime(params);
            // 每组车的最新一组开行计划
            List<RunPlan> preGroup = runPlanDao.findPreRunPlanByPlanCrossId(planCrossId);
            for(RunPlan runPlan: preGroup) {
                lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);
            }
            RunPlan preRunPlan = null;
            List<RunPlan> sortList = Lists.newArrayList();
            for(RunPlan runPlan: preGroup) {
                if(preRunPlan != null && preRunPlan.getGroupSerialNbr() == runPlan.getGroupSerialNbr() && preRunPlan.getTrainSort() > runPlan.getTrainSort()) {
                    sortList.remove(preRunPlan);
                    preRunPlan = runPlan;
                } else {
                    preRunPlan = runPlan;
                }
                sortList.add(runPlan);
            }
            return sortList.size();
        }

        /**
         * 根据groupNbr和trainsort获取同一组车的下一辆车
         * @param groupNbr 组号
         * @param trainSort 车号
         * @return
         */
        private UnitCrossTrain getNextUnitCrossTrain(int groupNbr, int trainSort) {
            final List<UnitCrossTrain> unitCrossTrainList = this.unitCross.getUnitCrossTrainList();
            for(UnitCrossTrain unitCrossTrain: unitCrossTrainList) {
                if(groupNbr == unitCrossTrain.getGroupSerialNbr() && trainSort == unitCrossTrain.getTrainSort() - 1) {
                    return unitCrossTrain;
                }
            }
            return null;
        }

        /**
         * 找到最先结束的交路组数，后面就从这个交路开始继续生成计划
         * @param lastRunPlans
         * @return
         */
        private int getFirstEndedGroup(Map<Integer, RunPlan> lastRunPlans) {
            if(lastRunPlans.values().size() == 0) {
                return 1;
            }
            List<RunPlan> runPlanList = Lists.newArrayList(lastRunPlans.values());
            Collections.sort(runPlanList, new Comparator<RunPlan>() {
                @Override
                public int compare(RunPlan o1, RunPlan o2) {
                    return o1.getEndDateTime().compareTo(o2.getEndDateTime());
                }
            });
            return runPlanList.get(0).getGroupSerialNbr();
        }

        private void sendRunPlanMsg(String unitCrossId, RunPlan runPlan) {
            if(this.msgReceiveUrl == null) {
                return;
            }
            Map<String, Object> msg = Maps.newHashMap();
            msg.put("unitCrossId", unitCrossId);
            msg.put("trainNbr", runPlan.getTrainNbr());
            msg.put("day", runPlan.getRunDate());
            msg.put("runFlag", runPlan.getSpareFlag());
            ObjectMapper jsonUtil = new ObjectMapper();

            try {
                this.msgService.sendMessage(jsonUtil.writeValueAsString(msg), this.msgReceiveUrl, "updateTrainRunPlanDayFlag");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("发送消息失败", e);
            }
        }

        private void sendUnitCrossMsg(String unitCrossId, int status) {
            if(this.msgReceiveUrl == null) {
                return;
            }
            Map<String, Object> msg = Maps.newHashMap();
            msg.put("unitCrossId", unitCrossId);
            msg.put("status", status);
            ObjectMapper jsonUtil = new ObjectMapper();

            try {
                this.msgService.sendMessage(jsonUtil.writeValueAsString(msg), this.msgReceiveUrl, "updateTrainRunPlanDayFlag");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("发送消息失败", e);
            }
        }
    }

	public int deletePlanCrossByPlanCorssIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = Maps.newHashMap();
		int size = crossIdsArray.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIdsArray[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString()); 
		//删除经由
		deletePlanTrainStnsByPlanCrossIds(crossIdsArray);
		//删除车
		deletePlanTrainsByPlanCorssIds(crossIdsArray);
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_PLANCROSS_INFO_TRAIN_FOR_CROSSIDS, reqMap); 
	}
	
	/**
	 * 保存PlanCheckInfo到数据库plan_check中
	 * @param planCheckInfo
	 * @return
	 */
	public int savePlanCheckInfo(PlanCheckInfo planCheckInfo){
		return baseDao.insertBySql(Constants.CROSSDAO_INSERT_PLAN_CHECK_INFO, planCheckInfo);
	}
	
	
	/**
	 * 通过planCrossId查询planCheckInfo对象
	 * @param planCrossId planCrossId
	 * @return List<PlanCheckInfo>
	 */
	public List<PlanCheckInfo> getPlanCheckInfoForPlanCrossId(String planCrossId){
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSID, planCrossId);
	}
	
	/**
	 * 更新表plan_cross中checkType的值
	 * @param planCrossId planCrossId
	 * @param checkType 审核状态（0:未审核1:部分局审核2:途经局全部审核）
	 * @return 删除数量
	 */
	public int updateCheckTypeForPlanCrossId(String planCrossId,int checkType){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("planCrossId", planCrossId);
		reqMap.put("checkType", checkType);
		return baseDao.updateBySql(Constants.CROSSDAO_UPDATE_CHECKTYPE_FOR_PLANCROSSID, reqMap);
	}
	
	
	 /**
     * 根据planCrossId列表和rundate的开始时间和结束时间查询上图的列车信息
     * @param startDate 格式yyyyMMdd
     * @param endDate 格式yyyyMMdd
     * @param planCrossIdList
     * @return
     */
    public  List<ParamDto>  getTotalTrainsForPlanCrossIds(String startDate,String endDate,List<String> planCrossIdList){
	    List<ParamDto>  list = new ArrayList<ParamDto>();
	    Map<String,String> paramMap = new HashMap<String,String>();
	    paramMap.put("startDate", startDate);
	    paramMap.put("endDate", endDate);
	    StringBuffer bf = new StringBuffer();
	    int size = planCrossIdList.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(planCrossIdList.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		paramMap.put("planCrossIds", bf.toString());
	    List<Map<String,Object>> mapList = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TOTALTRAINS_FOR_PLAN_CROSS_ID, paramMap);
	    if(mapList != null && mapList.size() > 0){
	    	 //System.err.println("mapList.size===" + mapList.size());
	    	for(Map<String,Object> map :mapList ){
	    		ParamDto dto = new ParamDto();
	    		dto.setSourceEntityId(StringUtil.objToStr(map.get("BASE_TRAIN_ID")));
	    		dto.setPlanTrainId(StringUtil.objToStr(map.get("PLAN_TRAIN_ID")));
	    		String time = StringUtil.objToStr(map.get("RUN_DATE"));
	    		dto.setTime(DateUtil.formateDate(time));
	    		list.add(dto);
	    	}
	    }
	    return list;
}

    
    
	private int deletePlanTrainStnsByPlanCrossIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIdsArray.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIdsArray[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString()); 
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_PLANTRAINSTN_INFO_TRAIN_FOR_CROSSIDS, reqMap); 
	} 
	private int deletePlanTrainsByPlanCorssIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIdsArray.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIdsArray[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString()); 
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_PLANTRAIN_INFO_TRAIN_FOR_CROSSIDS, reqMap); 
	}
}
