package org.railway.com.trainplan.web.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import mor.railway.cmd.adapter.model.CmdInfoModel;
import mor.railway.cmd.adapter.util.ConstantUtil;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanStnDao;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDto;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 临客相关操作
 * @author Think
 *
 */
@Controller
@RequestMapping(value = "/runPlanLk")
public class RunPlanLkController {
	
	 private static Log logger = LogFactory.getLog(RunPlanLkController.class.getName());
	 private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	 @Autowired
	 private RunPlanLkService runPlanLkService;
    
     @Autowired
     private RunPlanDao runPlanDao;

     @Autowired
     private RunPlanStnDao runPlanStnDao;

	 @RequestMapping(value="/addPage", method = RequestMethod.GET)
     public String addPage() {
		 return "runPlanLk/runPlanLk_add";
     }
	 
	 

	 @RequestMapping(value="/mainPage", method = RequestMethod.GET)
     public String mainPage() {
		 return "runPlanLk/runPlanLk_main";
     }
	 

	 @RequestMapping(value="/jbtTrainInfoPage", method = RequestMethod.GET)
     public ModelAndView jbtTrainInfoPage(HttpServletRequest request) {
		 return new ModelAndView("runPlanLk/jbt_traininfo").addObject("tabType", request.getParameter("tabType"));
     }
	 
	 
	 
	 /**
	  * 在表plan_train中查询临客列车信息
	  * @param reqMap
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getPlanTrainLkInfo", method = RequestMethod.POST)
	 public Result getPlanTrainLkInfo(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 logger.debug("getPlanTrainLkInfo~~~reqMap=="+ reqMap);
		 try{ 
			 String isRelationBureau = StringUtil.objToStr(reqMap.get("isRelationBureau"));
			 if("1".equals(isRelationBureau)){
				 //表示需要查询与本局相关的列车
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 String bureau = user.getBureauShortName();
				 reqMap.put("bureau", bureau);
			 }
			 List<RunPlan> runPlans = runPlanLkService.getPlanTrainLkInfo(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 result.setCode("-1");
			 result.setMessage("查询临客运行线出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
	 /**
	  * 查询临客开行情况
	  * @param reqMap
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "/getTrainLkRunPlans", method = RequestMethod.POST)
	 public Result getTrainLkRunPlans(@RequestBody Map<String,Object> reqMap) throws Exception{
		 Result result = new Result();
		 logger.debug("getTrainLkRunPlans~~~reqMap==" + reqMap);
		 try{ 
			 List<RunPlanTrainDto> runPlans = runPlanLkService.getTrainLkRunPlans(reqMap);
			 result.setData(runPlans);
		 }catch(Exception e){
			 e.printStackTrace();
			 result.setCode("-1");
			 result.setMessage("查询临客运行线开行规律出错:" + e.getMessage());
		 } 
		 return result; 
     }
	 
	 
	    /**
		 * 根据plant_train_id从PLAN_TRAIN_STN中查询列车时刻表
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getTrainLkInfoForPlanTrainId", method = RequestMethod.POST)
		public Result getTrainLkInfoForPlanTrainId(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			logger.info("getTrainLkInfoForPlanTrainId~~reqMap==" + reqMap);
			String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			try{
				
				List<BaseCrossTrainInfoTime> list = runPlanLkService.getTrainLkInfoForPlanTrainId(planTrainId);
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				//列车信息
				 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				 Map<String,Object> crossMap = new HashMap<String,Object>();
				 //用于纵坐标的经由站列表
				 List<Station> listStation = new ArrayList<Station>();
				 //横坐标的开始日期
				 String arrDate = ""; 
				 //横坐标的结束日期
				 String dptDate = "";
				 if(list != null && list.size() > 0 ){
					 //设置列车信息
					 TrainInfoDto dto = new TrainInfoDto();
					 dto.setTrainName(trainNbr);
					 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
					 
					//循环经由站
					 for(int i = 0;i<list.size();i++){
						
						 
						 BaseCrossTrainInfoTime subInfo = list.get(i);
						 PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
						 stnDtoStart.setArrTime(subInfo.getArrTime());
						 stnDtoStart.setDptTime(subInfo.getDptTime());
						 stnDtoStart.setStayTime(subInfo.getStayTime());
						 stnDtoStart.setStnName(subInfo.getStnName());
						 stnDtoStart.setStationType(subInfo.getStationType());
						
						 //获取起始站的到站日期和终到站的出发日期为横坐标的日期段
						 if( i == 0){
							 String arrTime = subInfo.getArrTime();
							 arrDate = DateUtil.format(DateUtil.parseDate(arrTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
						     //设置始发站
							 dto.setStartStn(subInfo.getStnName());
							 stnDtoStart.setStationType("0");
							 
						 }
						 if( i == list.size()-1){
							 String dptTime = subInfo.getDptTime();
							 dptDate = DateUtil.format(DateUtil.parseDate(dptTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
						     //设置终到站
							 dto.setEndStn(subInfo.getStnName());
							 stnDtoStart.setStationType("0");
						 }
						 trainStns.add(stnDtoStart);
						 //纵坐标数据
						 Station station = new Station();
			    		 station.setStnName(stnDtoStart.getStnName());
						 station.setStationType(stnDtoStart.getStationType());
						 listStation.add(station);
					 }
					 dto.setTrainStns(trainStns);
					 //设置planTrainId
					 dto.setPlanTrainId(planTrainId);
					 trains.add(dto);
				 }
				 crossMap.put("trains", trains);
				 dataList.add(crossMap);
				 PlanLineGrid grid = null;
				
				 //生成横纵坐标
		    	 List<PlanLineGridY> listGridY = getPlanLineGridY(listStation); 
				 List<PlanLineGridX> listGridX = getPlanLineGridX(arrDate,dptDate);
			     grid = new PlanLineGrid(listGridX, listGridY);
			     //图形数据
				 Map<String,Object> dataMap = new HashMap<String,Object>();
				 dataMap.put("myJlData",dataList);
				 dataMap.put("gridData", grid);
				 System.err.println("dataMap=="+dataMap);
				result.setData(dataMap);
				
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
			
			return result;
		}
	
		 /**
		 * 查询外局的cmdTrain信息
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getOtherCmdTrainInfo", method = RequestMethod.POST)
		public Result getOtherCmdTrainInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			logger.info("getOtherCmdTrainInfo~~reqMap==" + reqMap);
			String startDate = StringUtil.objToStr(reqMap.get("startDate"));
			String endDate = StringUtil.objToStr(reqMap.get("endDate"));
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			//命令状态
			String cmdType = StringUtil.objToStr(reqMap.get("cmdType"));
			//局令号
			String cmdNbrBureau = StringUtil.objToStr(reqMap.get("cmdNbrBureau"));
			//部令号
			String cmdNbrSuperior = StringUtil.objToStr(reqMap.get("cmdNbrSuperior"));
			//选线状态0：未选择 1：已选择 all:全部
			String selectState = StringUtil.objToStr(reqMap.get("selectState"));
			//生成状态0：未生成 1：已生成 all:全部
			String createState =  StringUtil.objToStr(reqMap.get("createState"));
			try{
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //本局局码
				 String bureuaCode = user.getBureau();
				 logger.debug("bureuaCode==" + bureuaCode);
				 CmdInfoModel model = new CmdInfoModel();
				 model.setStartDate(DateUtil.parse(startDate));
				 model.setEndDate(DateUtil.parse(endDate));
				 model.setCmdBureau(bureuaCode);
				 model.setTrainNbr(trainNbr);
				 model.setCmdNbrBureau(cmdNbrBureau);
				 model.setCmdNbrSuperior(cmdNbrSuperior);
				 if("all".equals(cmdType)){
					 model.setCmdType(ConstantUtil.ALL_ADD_CMD_NAME);
				 }else if("1".equals(cmdType)){
					 //既有线加开
					 model.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
				 }else if("2".equals(cmdType)){
					 //高铁加开
					 model.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
				 }
				 List<CmdInfoModel> listModel = runPlanLkService.getCmdTrainInfoFromRemote(model);
				 List<CmdTrain> returnList = new ArrayList<CmdTrain>();
				 if(listModel !=null && listModel.size() > 0 ){
					 for(CmdInfoModel infoModel : listModel){
						 CmdTrain cmdTrainTempl = new CmdTrain();
						 Integer cmdTxtMlItemId = infoModel.getCmdTxtMlItemId();
				
						 //从本地数据库中查询
						 CmdTrain cmdTrain = runPlanLkService.getCmdTrainInfoForCmdTxtmlItemId(String.valueOf(cmdTxtMlItemId));
						 if(cmdTrain == null){
							 cmdTrainTempl.setCreateState("0");
							 cmdTrainTempl.setSelectState("0");
							 cmdTrainTempl.setIsExsitStn("0");
						 }else{
							 cmdTrainTempl.setCreateState(cmdTrain.getCreateState());
							 cmdTrainTempl.setSelectState(cmdTrain.getSelectState());
							 cmdTrainTempl.setCmdTrainId(cmdTrain.getCmdTrainId());
							 cmdTrainTempl.setPassBureau(cmdTrain.getPassBureau());
							 cmdTrainTempl.setUpdateTime(cmdTrain.getUpdateTime());
							 cmdTrainTempl.setIsExsitStn("1");
						 }
						 cmdTrainTempl.setCmdBureau(infoModel.getCmdBureau());
						 cmdTrainTempl.setCmdItem(infoModel.getCmdItem());
						 cmdTrainTempl.setCmdNbrBureau(infoModel.getCmdNbrBureau());
						 cmdTrainTempl.setCmdNbrSuperior(infoModel.getCmdNbrSuperior());
						 cmdTrainTempl.setCmdTime(DateUtil.format(infoModel.getCmdTime(), "yyyy-MM-dd"));
						 
						 cmdTrainTempl.setCmdType(infoModel.getCmdType());
						 cmdTrainTempl.setEndDate(DateUtil.format(infoModel.getEndDate(), "yyyy-MM-dd"));
						 cmdTrainTempl.setStartDate(DateUtil.format(infoModel.getStartDate(), "yyyy-MM-dd"));
						 cmdTrainTempl.setEndStn(infoModel.getEndStn());
						 cmdTrainTempl.setRule(infoModel.getRule());
						 cmdTrainTempl.setSelectedDate(infoModel.getSelectedDate());
						 cmdTrainTempl.setStartStn(infoModel.getStartStn());
						 cmdTrainTempl.setTrainNbr(infoModel.getTrainNbr());
						 cmdTrainTempl.setCmdTxtMlId(infoModel.getCmdItem());
						 cmdTrainTempl.setCmdTxtMlItemId(cmdTxtMlItemId);
						
						 if("all".equals(selectState) && !"all".equals(createState)){
							if(createState.equals(cmdTrainTempl.getCreateState())){
								returnList.add(cmdTrainTempl); 
							}
						 }else if(!"all".equals(selectState) && "all".equals(createState)){
							 if(selectState.equals(cmdTrainTempl.getSelectState())){
								 returnList.add(cmdTrainTempl); 
							 }
						 }else if(!"all".equals(selectState) && !"all".equals(createState)){
							 if(createState.equals(cmdTrainTempl.getCreateState()) && selectState.equals(cmdTrainTempl.getSelectState())){
								 returnList.add(cmdTrainTempl); 
							 }
						 }else{
							//添加到list中
							 returnList.add(cmdTrainTempl); 
						 }
						 
					 }
				 }
				result.setData(returnList);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
			
			return result;
		}
		
		
		 /**
		 * 查询本局的cmdTrain信息
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getCmdTrainInfo", method = RequestMethod.POST)
		public Result getCmdTrainInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			logger.info("getCmdTrainInfo~~reqMap==" + reqMap);
			String startDate = StringUtil.objToStr(reqMap.get("startDate"));
			String endDate = StringUtil.objToStr(reqMap.get("endDate"));
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			//命令状态
			String cmdType = StringUtil.objToStr(reqMap.get("cmdType"));
			//局令号
			String cmdNbrBureau = StringUtil.objToStr(reqMap.get("cmdNbrBureau"));
			//部令号
			String cmdNbrSuperior = StringUtil.objToStr(reqMap.get("cmdNbrSuperior"));
			//选线状态0：未选择 1：已选择 all:全部
			String selectState = StringUtil.objToStr(reqMap.get("selectState"));
			//生成状态0：未生成 1：已生成 all:全部
			String createState =  StringUtil.objToStr(reqMap.get("createState"));
			try{
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //本局局码
				 String bureuaCode = user.getBureau();
				 logger.debug("bureuaCode==" + bureuaCode);
				 CmdInfoModel model = new CmdInfoModel();
				 model.setStartDate(DateUtil.parse(startDate));
				 model.setEndDate(DateUtil.parse(endDate));
				 model.setCmdBureau(bureuaCode);
				 model.setTrainNbr(trainNbr);
				 model.setCmdNbrBureau(cmdNbrBureau);
				 model.setCmdNbrSuperior(cmdNbrSuperior);
				 if("all".equals(cmdType)){
					 model.setCmdType(ConstantUtil.ALL_ADD_CMD_NAME);
				 }else if("1".equals(cmdType)){
					 //既有线加开
					 model.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
				 }else if("2".equals(cmdType)){
					 //高铁加开
					 model.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
				 }
				 List<CmdInfoModel> listModel = runPlanLkService.getCmdTrainInfoFromRemote(model);
				 List<CmdTrain> returnList = new ArrayList<CmdTrain>();
				 if(listModel !=null && listModel.size() > 0 ){
					 for(CmdInfoModel infoModel : listModel){
						 CmdTrain cmdTrainTempl = new CmdTrain();
						 Integer cmdTxtMlItemId = infoModel.getCmdTxtMlItemId();
				
						 //从本地数据库中查询
						 CmdTrain cmdTrain = runPlanLkService.getCmdTrainInfoForCmdTxtmlItemId(String.valueOf(cmdTxtMlItemId));
						 if(cmdTrain == null){
							 cmdTrainTempl.setCreateState("0");
							 cmdTrainTempl.setSelectState("0");
							 cmdTrainTempl.setIsExsitStn("0");
						 }else{
							 cmdTrainTempl.setCreateState(cmdTrain.getCreateState());
							 cmdTrainTempl.setSelectState(cmdTrain.getSelectState());
							 cmdTrainTempl.setCmdTrainId(cmdTrain.getCmdTrainId());
							 cmdTrainTempl.setPassBureau(cmdTrain.getPassBureau());
							 cmdTrainTempl.setUpdateTime(cmdTrain.getUpdateTime());
							 cmdTrainTempl.setIsExsitStn("1");
						 }
						 cmdTrainTempl.setCmdBureau(infoModel.getCmdBureau());
						 cmdTrainTempl.setCmdItem(infoModel.getCmdItem());
						 cmdTrainTempl.setCmdNbrBureau(infoModel.getCmdNbrBureau());
						 cmdTrainTempl.setCmdNbrSuperior(infoModel.getCmdNbrSuperior());
						 cmdTrainTempl.setCmdTime(DateUtil.format(infoModel.getCmdTime(), "yyyy-MM-dd"));
						 
						 cmdTrainTempl.setCmdType(infoModel.getCmdType());
						 cmdTrainTempl.setEndDate(DateUtil.format(infoModel.getEndDate(), "yyyy-MM-dd"));
						 cmdTrainTempl.setStartDate(DateUtil.format(infoModel.getStartDate(), "yyyy-MM-dd"));
						 cmdTrainTempl.setEndStn(infoModel.getEndStn());
						 cmdTrainTempl.setRule(infoModel.getRule());
						 cmdTrainTempl.setSelectedDate(infoModel.getSelectedDate());
						 cmdTrainTempl.setStartStn(infoModel.getStartStn());
						 cmdTrainTempl.setTrainNbr(infoModel.getTrainNbr());
						 cmdTrainTempl.setCmdTxtMlId(infoModel.getCmdItem());
						 cmdTrainTempl.setCmdTxtMlItemId(cmdTxtMlItemId);
						
						 if("all".equals(selectState) && !"all".equals(createState)){
							if(createState.equals(cmdTrainTempl.getCreateState())){
								returnList.add(cmdTrainTempl); 
							}
						 }else if(!"all".equals(selectState) && "all".equals(createState)){
							 if(selectState.equals(cmdTrainTempl.getSelectState())){
								 returnList.add(cmdTrainTempl); 
							 }
						 }else if(!"all".equals(selectState) && !"all".equals(createState)){
							 if(createState.equals(cmdTrainTempl.getCreateState()) && selectState.equals(cmdTrainTempl.getSelectState())){
								 returnList.add(cmdTrainTempl); 
							 }
						 }else{
							//添加到list中
							 returnList.add(cmdTrainTempl); 
						 }
						 
					 }
				 }
				result.setData(returnList);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
			
			return result;
		}
		
		
		 /**
		 * 查询cmdTrainStn信息
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getCmdTrainStnInfo", method = RequestMethod.POST)
		public Result getCmdTrainStnInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			logger.info("getCmdTrainStnInfo~~reqMap==" + reqMap);
			//cmdTrain表主键
			String cmdTrainId = StringUtil.objToStr(reqMap.get("cmdTrainId"));
			
			try{
				
				List<CmdTrainStn> list = runPlanLkService.getCmdTrainStnInfo(cmdTrainId);
				result.setData(list);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
			
			return result;
		}
		
		/**
		 * 批量生成开行计划
		 * @param reqStr
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/batchCreateRunPlanLine", method = RequestMethod.POST)
		public Result batchCreateRunPlanLine(@RequestBody String reqStr){
			Result result = new Result();
			logger.info("batchCreateRunPlanLine~~reqStr==" + reqStr);
			try{
				JSONObject reqObj = JSONObject.fromObject(reqStr);
				List<String> cmdTraindIdList = (List<String>)reqObj.get("cmdTrainIds");
			    if(cmdTraindIdList != null && cmdTraindIdList.size() > 0 ){
			    	for(String cmdTrainId : cmdTraindIdList){
			    		List<CmdTrain> cmdTrainList = runPlanLkService.getCmdTrandAndStnInfo(cmdTrainId);
			    		//只有一条数据
			    		CmdTrain  cmdTrain = cmdTrainList.get(0);
			    		//时刻表信息
			    		List<CmdTrainStn> listTrainStn = cmdTrain.getCmdTrainStnList();
			    		String startDate = cmdTrain.getStartDate();
			    		String endDate = cmdTrain.getEndDate();
			    		//创建临客命令对象
						CmdInfoModel model = new CmdInfoModel();
						model.setStartDate(DateUtil.parse(startDate));
						model.setEndDate(DateUtil.parse(endDate));
				        // 命令类型
						model.setCmdType(cmdTrain.getCmdType());
				        // 开行规律
						model.setRule(cmdTrain.getRule());
				        // 或者择日
				        model.setSelectedDate(cmdTrain.getSelectedDate());

				        List<Date> listDate = runPlanLkService.getSelectedDateListFromRemote(model);
				        if(listDate !=null && listDate.size() > 0 ){
				        	for(Date date : listDate){
				        		String runDate = DateUtil.format(date, "yyyyMMdd");
				        		//始发站对象
				        		CmdTrainStn startTrainStn = listTrainStn.get(0);
				        		String startTime = startTrainStn.getArrTime();
				        		String startBureau = startTrainStn.getStnBureau();
				        		String startBureauFullName = startTrainStn.get
				        		//终到站对象
				        		CmdTrainStn endTrainStn = listTrainStn.get(listTrainStn.size()-1);
				        		String endTime = endTrainStn.getDptTime();
				        		
				        		//取最后一个站的dptrundays作为整个列车的runDays
				        		int runDays = endTrainStn.getDptRunDays();
				        		//表plan_train对应实体类
					    		RunPlan runPlan = new RunPlan();
					    		//表plan_train对应的主键
					    		String planTrainId = UUID.randomUUID().toString();
					    		runPlan.setPlanTrainId(planTrainId);
					    		runPlan.setRunDate(runDate);
					    		runPlan.setTrainNbr(cmdTrain.getTrainNbr());
					    		runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + startTime).getTime()));
	                            runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).plusDays(runDays).toString("yyyy-MM-dd") + " " + endTime).getTime()));
	                            runPlan.setStartBureauShortName(cmdTrain.getCmdBureau());
	                            //TODO
	                            runPlan.setEndBureauShortName("");
	                            //TODO 需要李龙提供
	                            runPlan.setStartBureauFullName("");
	                            runPlan.setEndBureauShortName("");
	                            
	                            runPlan.setStartStn(cmdTrain.getStartStn());
	                            runPlan.setEndStn(cmdTrain.getEndStn());
	                            runPlan.setPassBureau(cmdTrain.getPassBureau());
	                            //TODO 根据passBureau 来确定 列车范围（1:直通；0:管内）
	                            runPlan.setTrainScope(0);
	                            //TODO 车辆担当局
	                            runPlan.setTokenVehBureau("");
	                            //TODO 高线标记（1:高线；0:普线；2:混合）
	                            runPlan.setHighLineFlag(0);
	                            runPlan.setBaseTrainId(cmdTrain.getBaseTrainId());
	                            //TODO 备用及停运标记（1:开行；2:备用；9:停运）
                                runPlan.setSpareFlag(1);
                                runPlan.setCmdBureau(cmdTrain.getCmdBureau());
                                
				        	}
				        }
			    	}
			    }
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
		
			return result;
		}
		
		

		/**
		 * 保存外局的临客列车运行时刻表
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/saveOtherLkTrainTimes", method = RequestMethod.POST)
		public Result saveOtherLkTrainTimes(@RequestBody String reqStr){
			Result result = new Result();
			logger.info("saveOtherLkTrainTimes~~reqStr==" + reqStr);
			try{
				JSONObject reqObj = JSONObject.fromObject(reqStr);
				Map<String,Object> trainMap = (Map<String,Object>)reqObj.get("cmdTrainMap");
				List<JSONObject>  trainStnList = reqObj.getJSONArray("cmdTrainStnList");
				String planTrainId = StringUtil.objToStr(reqObj.get("planTrainId"));
			    CmdTrain train = new CmdTrain();
			    ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //局简称
				 String bureuaShortName = user.getBureauShortName();
				 logger.debug("bureuaShortName=="+bureuaShortName);
				 Integer cmdTxtMlItemId = (Integer)trainMap.get("cmdTxtMlItemId");
				 //从本地数据库中查询
				 CmdTrain cmdTrain = runPlanLkService.getCmdTrainInfoForCmdTxtmlItemId(String.valueOf(cmdTxtMlItemId));
				 //表cmd_train主键
				 String cmdTrainId = "";
				 if(cmdTrain == null ){
					
				    //表cmd_train主键
					cmdTrainId = UUID.randomUUID().toString();
					train.setBaseTrainId(planTrainId);
					train.setCmdTrainId(cmdTrainId);
				    train.setCmdBureau(bureuaShortName);
				    train.setCmdItem((Integer)trainMap.get("cmdItem"));
				    train.setCmdNbrBureau(StringUtil.objToStr(trainMap.get("cmdNbrBureau")));
				    train.setCmdNbrSuperior(StringUtil.objToStr(trainMap.get("cmdNbrSuperior")));
				    train.setCmdTime(StringUtil.objToStr(trainMap.get("cmdTime")));
				    train.setCmdTxtMlId((Integer)trainMap.get("cmdTxtMlId"));
				    train.setCmdTxtMlItemId((Integer)trainMap.get("cmdTxtMlItemId"));
				    train.setCmdType(StringUtil.objToStr(trainMap.get("cmdType")));
				    train.setCreateState(StringUtil.objToStr(trainMap.get("createState")));
				    train.setEndDate(StringUtil.objToStr(trainMap.get("endDate")));
				    train.setEndStn(StringUtil.objToStr(trainMap.get("endStn")));
				    train.setPassBureau(StringUtil.objToStr(trainMap.get("passBureau")));
				    train.setRule(StringUtil.objToStr(trainMap.get("rule")));
				    train.setSelectedDate(StringUtil.objToStr(trainMap.get("selectedDate")));
				    train.setSelectState(StringUtil.objToStr(trainMap.get("selectState")));
				    train.setStartDate(StringUtil.objToStr(trainMap.get("startDate")));
				    train.setStartStn(StringUtil.objToStr(trainMap.get("startStn")));
				    train.setTrainNbr(StringUtil.objToStr(trainMap.get("trainNbr")));
				    train.setUpdateTime(StringUtil.objToStr(trainMap.get("updateTime")));
					//保存数据到表cmd_train中
				    runPlanLkService.insertCmdTrain(train);
				 }else{
					 cmdTrainId =  cmdTrain.getCmdTrainId();
					 //更新途径局
					 String passBureau = StringUtil.objToStr(trainMap.get("passBureau"));
					 if(passBureau != null && !"".equals(passBureau)){
						 runPlanLkService.updatePassBureauForCmdTraindId(passBureau,cmdTrainId);
					 }
					 
				 }
				 
				 //先删除表cmd_train_stn中对应的数据
				 int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(cmdTrainId);
				 logger.debug("count==" + count);
			    //循环设置子表cmd_train_stn中的数据对象
			    for(JSONObject trainStn : trainStnList){
			    	CmdTrainStn stn = new CmdTrainStn();
			    	
			    	stn.setCmdTrainId(cmdTrainId);
			    	stn.setCmdTrainStnId(UUID.randomUUID().toString());
			    	String arrTrainNbr = StringUtil.objToStr(trainStn.get("arrTrainNbr"));
			    	String dptTrainNbr = StringUtil.objToStr(trainStn.get("arrTrainNbr"));
			    	stn.setArrTrainNbr(arrTrainNbr == null? "":arrTrainNbr);
			    	stn.setDptTrainNbr(dptTrainNbr == null? "":dptTrainNbr);
			    	String arrTime = StringUtil.objToStr(trainStn.get("arrTime"));
			    	String endTime = StringUtil.objToStr(trainStn.get("dptTime"));
			    	String baseArrTime = StringUtil.objToStr(trainStn.get("baseArrTime"));
			    	String baseDptTime = StringUtil.objToStr(trainStn.get("baseDptTime"));
			    	stn.setArrTime(arrTime);
			    	stn.setDptTime(endTime);
			    	if(baseArrTime == null || "".equals(baseArrTime)){
			    		stn.setBaseArrTime(arrTime);
			    	}else{
			    		stn.setBaseArrTime(baseArrTime);
			    	}
			    	
			    	if(baseDptTime == null || "".equals(baseDptTime)){
			    		stn.setBaseDptTime(endTime);
			    	}else{
			    		stn.setBaseDptTime(baseDptTime);
			    	}
			    	
			    	String plantForm = StringUtil.objToStr(trainStn.get("platform"));
			    	stn.setPlatform(plantForm == null?"":plantForm);
			    	stn.setStnBureau(StringUtil.objToStr(trainStn.get("stnBureau")));
			    	stn.setStnName(StringUtil.objToStr(trainStn.get("stnName")));
			    	
			    	stn.setStnType(StringUtil.objToStr(trainStn.get("stnType")));
			    	stn.setTecType(StringUtil.objToStr(trainStn.get("tecType")));
			    	stn.setTrackNbr(StringUtil.objToStr(trainStn.get("trackNbr")));
			    	stn.setStnSort((Integer)trainStn.get("childIndex"));
			    	
			    	int psgFlg = trainStn.get("psgFlg")==null ?0:(Integer)trainStn.get("psgFlg");
			    	int arrRunDays =  trainStn.get("arrRunDays")==null ?0:(Integer)trainStn.get("arrRunDays");
			    	int dptRunDays =  trainStn.get("dptRunDays")==null ?0:(Integer)trainStn.get("dptRunDays");
			    	int locoFlag =  trainStn.get("locoFlag")==null ?0:(Integer)trainStn.get("locoFlag");
			    			
			    	stn.setPsgFlg(psgFlg);
			    	stn.setArrRunDays(arrRunDays);
			    	stn.setDptRunDays(dptRunDays);
			    	stn.setLocoFlag(locoFlag);
			    	//保存数据到表cmd_train_stn中
			    	runPlanLkService.insertCmdTrainStn(stn);
			    }
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
		
			return result;
		} 
		
		
		
		/**
		 * 保存临客列车运行时刻表
		 * @param reqMap
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/saveLkTrainTimes", method = RequestMethod.POST)
		public Result saveLkTrainTimes(@RequestBody String reqStr){
			Result result = new Result();
			logger.info("saveLkTrainTimes~~reqStr==" + reqStr);
			try{
				JSONObject reqObj = JSONObject.fromObject(reqStr);
				Map<String,Object> trainMap = (Map<String,Object>)reqObj.get("cmdTrainMap");
				List<JSONObject>  trainStnList = reqObj.getJSONArray("cmdTrainStnList");
				String planTrainId = StringUtil.objToStr(reqObj.get("planTrainId"));
			    CmdTrain train = new CmdTrain();
			    ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //局简称
				 String bureuaShortName = user.getBureauShortName();
				 logger.debug("bureuaShortName=="+bureuaShortName);
				 Integer cmdTxtMlItemId = (Integer)trainMap.get("cmdTxtMlItemId");
				 //从本地数据库中查询
				 CmdTrain cmdTrain = runPlanLkService.getCmdTrainInfoForCmdTxtmlItemId(String.valueOf(cmdTxtMlItemId));
				 //表cmd_train主键
				 String cmdTrainId = "";
				 if(cmdTrain == null ){
					
				    //表cmd_train主键
					cmdTrainId = UUID.randomUUID().toString();
					train.setBaseTrainId(planTrainId);
					train.setCmdTrainId(cmdTrainId);
				    train.setCmdBureau(bureuaShortName);
				    train.setCmdItem((Integer)trainMap.get("cmdItem"));
				    train.setCmdNbrBureau(StringUtil.objToStr(trainMap.get("cmdNbrBureau")));
				    train.setCmdNbrSuperior(StringUtil.objToStr(trainMap.get("cmdNbrSuperior")));
				    train.setCmdTime(StringUtil.objToStr(trainMap.get("cmdTime")));
				    train.setCmdTxtMlId((Integer)trainMap.get("cmdTxtMlId"));
				    train.setCmdTxtMlItemId((Integer)trainMap.get("cmdTxtMlItemId"));
				    train.setCmdType(StringUtil.objToStr(trainMap.get("cmdType")));
				    train.setCreateState(StringUtil.objToStr(trainMap.get("createState")));
				    train.setEndDate(StringUtil.objToStr(trainMap.get("endDate")));
				    train.setEndStn(StringUtil.objToStr(trainMap.get("endStn")));
				    train.setPassBureau(StringUtil.objToStr(trainMap.get("passBureau")));
				    train.setRule(StringUtil.objToStr(trainMap.get("rule")));
				    train.setSelectedDate(StringUtil.objToStr(trainMap.get("selectedDate")));
				    train.setSelectState(StringUtil.objToStr(trainMap.get("selectState")));
				    train.setStartDate(StringUtil.objToStr(trainMap.get("startDate")));
				    train.setStartStn(StringUtil.objToStr(trainMap.get("startStn")));
				    train.setTrainNbr(StringUtil.objToStr(trainMap.get("trainNbr")));
				    train.setUpdateTime(StringUtil.objToStr(trainMap.get("updateTime")));
					//保存数据到表cmd_train中
				    runPlanLkService.insertCmdTrain(train);
				 }else{
					 cmdTrainId =  cmdTrain.getCmdTrainId();
					 //更新途径局
					 String passBureau = StringUtil.objToStr(trainMap.get("passBureau"));
					 if(passBureau != null && !"".equals(passBureau)){
						 runPlanLkService.updatePassBureauForCmdTraindId(passBureau,cmdTrainId);
					 }
					 
				 }
				 
				 //先删除表cmd_train_stn中对应的数据
				 int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(cmdTrainId);
				 logger.debug("count==" + count);
			    //循环设置子表cmd_train_stn中的数据对象
			    for(JSONObject trainStn : trainStnList){
			    	CmdTrainStn stn = new CmdTrainStn();
			    	
			    	stn.setCmdTrainId(cmdTrainId);
			    	stn.setCmdTrainStnId(UUID.randomUUID().toString());
			    	String arrTrainNbr = StringUtil.objToStr(trainStn.get("arrTrainNbr"));
			    	String dptTrainNbr = StringUtil.objToStr(trainStn.get("arrTrainNbr"));
			    	stn.setArrTrainNbr(arrTrainNbr == null? "":arrTrainNbr);
			    	stn.setDptTrainNbr(dptTrainNbr == null? "":dptTrainNbr);
			    	String arrTime = StringUtil.objToStr(trainStn.get("arrTime"));
			    	String endTime = StringUtil.objToStr(trainStn.get("dptTime"));
			    	String baseArrTime = StringUtil.objToStr(trainStn.get("baseArrTime"));
			    	String baseDptTime = StringUtil.objToStr(trainStn.get("baseDptTime"));
			    	stn.setArrTime(arrTime);
			    	stn.setDptTime(endTime);
			    	if(baseArrTime == null || "".equals(baseArrTime)){
			    		stn.setBaseArrTime(arrTime);
			    	}else{
			    		stn.setBaseArrTime(baseArrTime);
			    	}
			    	
			    	if(baseDptTime == null || "".equals(baseDptTime)){
			    		stn.setBaseDptTime(endTime);
			    	}else{
			    		stn.setBaseDptTime(baseDptTime);
			    	}
			    	
			    	String plantForm = StringUtil.objToStr(trainStn.get("platform"));
			    	stn.setPlatform(plantForm == null?"":plantForm);
			    	stn.setStnBureau(StringUtil.objToStr(trainStn.get("stnBureau")));
			    	stn.setStnName(StringUtil.objToStr(trainStn.get("stnName")));
			    	
			    	stn.setStnType(StringUtil.objToStr(trainStn.get("stnType")));
			    	stn.setTecType(StringUtil.objToStr(trainStn.get("tecType")));
			    	stn.setTrackNbr(StringUtil.objToStr(trainStn.get("trackNbr")));
			    	stn.setStnSort((Integer)trainStn.get("childIndex"));
			    	
			    	int psgFlg = trainStn.get("psgFlg")==null ?0:(Integer)trainStn.get("psgFlg");
			    	int arrRunDays =  trainStn.get("arrRunDays")==null ?0:(Integer)trainStn.get("arrRunDays");
			    	int dptRunDays =  trainStn.get("dptRunDays")==null ?0:(Integer)trainStn.get("dptRunDays");
			    	int locoFlag =  trainStn.get("locoFlag")==null ?0:(Integer)trainStn.get("locoFlag");
			    			
			    	stn.setPsgFlg(psgFlg);
			    	stn.setArrRunDays(arrRunDays);
			    	stn.setDptRunDays(dptRunDays);
			    	stn.setLocoFlag(locoFlag);
			    	//保存数据到表cmd_train_stn中
			    	runPlanLkService.insertCmdTrainStn(stn);
			    }
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
			}
		
			return result;
		} 
		
		
		/**
		 * 组装纵坐标
		 * @param list
		 * @return
		 */
		@SuppressWarnings("unused")
		private  List<PlanLineGridY> getPlanLineGridY(List<Station> list){
			//纵坐标
			 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
			 if(list != null){
				 for(Station station : list){
					 //0:默认的isCurrentBureau
					 planLineGridYList.add(new PlanLineGridY(station.getStnName(),0,station.getStationType()));
				 }
			 }
			 
			 return planLineGridYList ;
		}
		
		/**
		 * 组装横坐标
		 * @param crossStartDate 交路开始日期，格式yyyy-MM-dd
		 * @param crossEndDate 交路终到日期，格式yyyy-MM-dd
		 * @return
		 */
		@SuppressWarnings("unused")
		private List<PlanLineGridX> getPlanLineGridX(String crossStartDate,String crossEndDate){
			
			 //横坐标
			 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();  
			
			 /*****组装横坐标  *****/
			 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
		     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
		     while(!start.isAfter(end)) {
		            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
		            start = start.plusDays(1);
		        }
		     return gridXList ;
		}
	 
}
