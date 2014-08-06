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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
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
		 return new ModelAndView("runPlanLk/jbt_traininfo").addObject("tabType", request.getParameter("tabType"))
				 .addObject("trainNbr", request.getParameter("trainNbr"));
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
			String startDate =  StringUtil.objToStr(reqMap.get("startDate"));
			String endDate =  StringUtil.objToStr(reqMap.get("endDate"));
			//用于获取纵坐标经由站的planTrainId
			String planTraindForStation = "";
			try{
				StringBuffer planTrainIds = new StringBuffer();
				String[] ids = planTrainId.split(",");
				planTraindForStation = ids[0];
				for(int i =0;i<ids.length;i++){
					if(i == ids.length - 1 ){
						planTrainIds.append("'").append(ids[i]).append("'");
					}else{
						planTrainIds.append("'").append(ids[i]).append("'").append(",");
					}
					
				}
				List<TrainLineSubInfo> list = runPlanLkService.getTrainLkInfoForPlanTrainId(planTrainIds.toString());
				List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
				//列车信息
				 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				 Map<String,Object> crossMap = new HashMap<String,Object>();
				 //用于纵坐标的经由站列表
				 List<Station> listStation = new ArrayList<Station>();
				 //获取纵坐标
				 List<TrainLineSubInfoTime> listSubInfoTime = runPlanLkService.getTrainLineSubinfoTime(planTraindForStation);
				 int endStnSort = listSubInfoTime.get(listSubInfoTime.size() - 1 ).getStnSort();
				 for(int i = 0;i<listSubInfoTime.size();i++){
					 TrainLineSubInfoTime subInfo = listSubInfoTime.get(i);
					 Station station = new Station();
					 station.setStnName(subInfo.getStnName());
					 String stationType = subInfo.getStationType();
					 //车站类型（1:发到站，2:分界口，3:停站,4:不停站）
					 if("4".equals(stationType)){
						 stationType = "BT";
					 }else if("3".equals(stationType)){
						 stationType = "TZ";
					 }else if("2".equals(stationType)){
						 stationType = "FJK";
					 }
					 
					 if(i == 0 || i == listSubInfoTime.size()-1){
						 stationType = "0"; 
					 }
					 station.setStationType(stationType);
					 listStation.add(station);
				 }
				 
			
				 if(list != null && list.size() > 0 ){
					 for(int j = 0;j<list.size();j++){
						 TrainLineSubInfo train  = list.get(j);
						 
						//设置列车信息
						 TrainInfoDto dto = new TrainInfoDto();
						 dto.setTrainName(trainNbr);
						 dto.setPlanTrainId(train.getPlanTrainId());
						 List<TrainLineSubInfoTime> subInfoTimeList = train.getTrainStaionList();
						 //循环经由站
						 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>(); 
						 for(int i = 0;i<subInfoTimeList.size();i++){
							 TrainLineSubInfoTime subInfo = subInfoTimeList.get(i);
							 PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
							 stnDtoStart.setArrTime(subInfo.getArrTime());
							 stnDtoStart.setDptTime(subInfo.getDptTime());
							 stnDtoStart.setStnName(subInfo.getStnName());
							 stnDtoStart.setStationType(subInfo.getStationType());
							
							 if( subInfo.getStnSort() == 0){
								 
							     //设置始发站
								 dto.setStartStn(subInfo.getStnName());
								 stnDtoStart.setStationType("0");
								 
								 
							 }
							 if( endStnSort == subInfoTimeList.size()-1){
								 //String dptTime = subInfo.getDptTime();
								 //dptDate = DateUtil.format(DateUtil.parseDate(dptTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
							     //设置终到站
								 dto.setEndStn(subInfo.getStnName());
								 stnDtoStart.setStationType("0");
							 }
							 trainStns.add(stnDtoStart);
						 }
						 dto.setTrainStns(trainStns);
						 trains.add(dto);
					 }
				
					 
				 }
				 crossMap.put("trains", trains);
				 dataList.add(crossMap);
				 PlanLineGrid grid = null;
		
				 //生成横纵坐标
		    	 List<PlanLineGridY> listGridY = getPlanLineGridY(listStation); 
				 List<PlanLineGridX> listGridX = getPlanLineGridX(startDate,endDate);
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
			//命令状态 1:既有线临客加开 2：高铁临客加开
			String cmdType = StringUtil.objToStr(reqMap.get("cmdType"));
			//部令号
			String cmdNbrSuperior = StringUtil.objToStr(reqMap.get("cmdNbrSuperior"));
			//选线状态0：未选择 1：已选择 all:全部
			String selectState = "0";
			//生成状态0：未生成 1：已生成 all:全部
			String createState =  StringUtil.objToStr(reqMap.get("createState"));
			//担当局
			String cmdBureau = StringUtil.objToStr(reqMap.get("cmdBureau"));
			logger.debug("cmdBureau==" + cmdBureau);
			try{
				
				 CmdTrain cmdTrain = new CmdTrain();
				 cmdTrain.setStartDate(startDate+" 00:00:00");
				 cmdTrain.setEndDate(endDate+" 23:59:59");
				 cmdTrain.setCmdBureau(cmdBureau);
				 cmdTrain.setTrainNbr(trainNbr);
				 cmdTrain.setCmdNbrSuperior(cmdNbrSuperior);
				 cmdTrain.setSelectState(selectState);
				 cmdTrain.setCreateState(createState);
				 System.err.println("cmdType==" + cmdType);
				 if("1".equals(cmdType)){
					cmdTrain.setCmdType(ConstantUtil.JY_ADD_CMD_NAME) ;
				 }else if("2".equals(cmdType)){
					cmdTrain.setCmdType(ConstantUtil.GT_ADD_CMD_NAME) ;
				 }else{
					 cmdTrain.setCmdType(cmdType) ;
				 }
				 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //局简称
				 String bureuaShortName = user.getBureauShortName();
				 cmdTrain.setUserBureau(bureuaShortName);
				 //从本地数据库中查询
				List<CmdTrain> cmdTrainList = runPlanLkService.getCmdTraindForMultipleParame(cmdTrain);
				List<CmdTrain> dataList = new ArrayList<CmdTrain>();
				
				for(CmdTrain train : cmdTrainList ){
					String passBureau = train.getPassBureau();
					if(!"".equals(passBureau) && passBureau != null){
						if(passBureau.contains(bureuaShortName)){
				
							String start = train.getStartDate();
							String end = train.getEndDate();
							train.setStartDate(DateUtil.format(DateUtil.parseDate(start,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd"));
							train.setEndDate(DateUtil.format(DateUtil.parseDate(end,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd"));
							dataList.add(train);
						}
					}
				}
				result.setData(dataList);
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
					 model.setCmdType(ConstantUtil.ALL_CMD_NAME);
				 }else if("1".equals(cmdType)){
					 //既有线加开
					 model.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
				 }else if("2".equals(cmdType)){
					 //高铁加开
					 model.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
				 }else if("3".equals(cmdType)){
					 //既有线停运
					 model.setCmdType(ConstantUtil.JY_STOP_CMD_NAME);
				 }else if("4".equals(cmdType)){
					 //高铁停运
					 model.setCmdType(ConstantUtil.GT_STOP_CMD_NAME);
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
						 String rule = infoModel.getRule();
						 if(rule == null || "null".equals(rule)){
							 rule = "";
						 }
						 cmdTrainTempl.setRule(rule);
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
			    		System.err.println("listTrainStn.size==" + listTrainStn.size());
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
				        	//始发站对象
			        		CmdTrainStn startTrainStn = listTrainStn.get(0);
			        		//终到站对象
			        		CmdTrainStn endTrainStn = listTrainStn.get(listTrainStn.size()-1);	
			        		//取最后一个站的dptrundays作为整个列车的runDays
			        		int runDays = endTrainStn.getDptRunDays();
			        		String cmdType = cmdTrain.getCmdType();
			        		
			        		//先根据cmdTrainId查询表plan_train，确认是否已经生成计划
			        		List<Map<String, Object>> mapList = runPlanLkService.getPlanTrainIdForCmdTrainId(cmdTrainId);
			        		if(mapList != null){
			        			//删除表plan_train和表plan_train_stn中对应的数据
			        			runPlanLkService.deleteTrainForCmdTrainId(cmdTrainId);
			        			for(Map<String, Object> map : mapList){
			        				String planTrainId = StringUtil.objToStr(map.get("PLANTRAINID"));
				        			//删除表plan_train_stn中对应数据
				        			runPlanLkService.deleteTrainStnForPlanTrainId(planTrainId);
			        			}
			        		}
				        	for(Date date : listDate){
				        		String runDate = DateUtil.format(date, "yyyyMMdd");
				        		
				        		//表plan_train对应实体类
					    		RunPlan runPlan = new RunPlan();
					    		//表plan_train对应的主键
					    		String planTrainId = UUID.randomUUID().toString();
					    		runPlan.setPlanTrainId(planTrainId);
					    		//20140802-G1208/5-青岛北-06:25:00
					    		//列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
					    		String planTrainSign = runDate + "-" + cmdTrain.getTrainNbr() + "-" + startTrainStn.getStnName() + "-" + startTrainStn.getArrTime() ;
					    		runPlan.setPlanTrainSign(planTrainSign);
					    		runPlan.setRunDate(runDate);
					    		runPlan.setTrainNbr(cmdTrain.getTrainNbr());
					    		System.err.println("arrTime==" + startTrainStn.getArrTime());
					    		runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runDate).toString("yyyy-MM-dd") + " " + startTrainStn.getArrTime()).getTime()));
	                            runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runDate).plusDays(runDays).toString("yyyy-MM-dd") + " " + endTrainStn.getDptTime()).getTime()));
	                            runPlan.setStartBureauShortName(startTrainStn.getStnBureau());
	                            runPlan.setEndBureauShortName(endTrainStn.getStnBureau());
	                            runPlan.setStartBureauFullName(startTrainStn.getStnBureauFull());
	                            runPlan.setEndBureauFullName(endTrainStn.getStnBureauFull());
	                            runPlan.setStartStn(cmdTrain.getStartStn());
	                            runPlan.setEndStn(cmdTrain.getEndStn());
	                            runPlan.setPassBureau(cmdTrain.getPassBureau());
	                            //根据passBureau 来确定 列车范围（1:直通；0:管内）
	                            runPlan.setTrainScope(cmdTrain.getPassBureau().length() ==1 ? 0 :1);
	                            //车辆担当局
	                            runPlan.setTokenVehBureau(cmdTrain.getCmdBureau());
	                            //高线标记（1:高线；0:普线；2:混合）
	                            if(cmdType.equals(ConstantUtil.GT_ADD_CMD_TYPE)){
	                            	runPlan.setHighLineFlag(1);
	                            }else if(cmdType.equals(ConstantUtil.JY_ADD_CMD_TYPE)){
	                            	runPlan.setHighLineFlag(0);
	                            }
	                           
	                            runPlan.setBaseTrainId(cmdTrain.getBaseTrainId());
	                            //备用及停运标记（1:开行；2:备用；9:停运）
                                runPlan.setSpareFlag(1);
                                runPlan.setCmdBureau(cmdTrain.getCmdBureau());
                                runPlan.setCmdTxtmlId(cmdTrain.getCmdTxtMlId());
                                runPlan.setCmdTxtmlitemId(cmdTrain.getCmdTxtMlItemId());
                                String cmdTime = cmdTrain.getCmdTime();
                                cmdTime = cmdTime.substring(0,10).replace("-", "");
                                String cmdNbrSuperior = cmdTrain.getCmdNbrSuperior();
                                if(cmdNbrSuperior != null &&!"".equals(cmdNbrSuperior)){
                                	//命令简要信息(发令日期+局命令号+命令子项号+部命令号)
                                	runPlan.setCmdShortInfo(cmdTime + "-"+cmdTrain.getCmdNbrBureau()+"（铁总" + cmdTrain.getCmdNbrSuperior()+"）");
                                }else{
                                	
                                	//命令简要信息(发令日期+局命令号+命令子项号+部命令号)
                                	runPlan.setCmdShortInfo(cmdTime + "-"+cmdTrain.getCmdNbrBureau());
                                }
				        	    //创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；4:人工添加）
                                runPlan.setCreateType(3);
                                //日计划一级审核状态（0:未审核1:部分局审核2:途经局全部审核）
                                runPlan.setCheckLev1Type(0);
                                //生成运行线标记（0: 是；1 :否）
                                runPlan.setDailyPlanFlag(1);
                                runPlan.setCmdTrainId(cmdTrainId);
                                ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
                                runPlan.setCreatePeople(user.getName());
                                runPlan.setCreatePeopleOrg(user.getBureauFullName());
                                List<RunPlanStn> stnList = new ArrayList<RunPlanStn>();
                                for(CmdTrainStn trainStn : listTrainStn){
                                	RunPlanStn   stn = new RunPlanStn();
                                	Integer arrRunDays = trainStn.getArrRunDays();
                                	Integer dptRunDays = trainStn.getDptRunDays();
                                	stn.setPlanTrainId(planTrainId);
                                
                                	stn.setPlanTrainStnId(UUID.randomUUID().toString());
                                	stn.setStnSort(trainStn.getStnSort());
                                	stn.setStnName(trainStn.getStnName());
                                	stn.setStnBureauShortName(trainStn.getStnBureau());
                                	stn.setStnBureauFullName(trainStn.getStnBureauFull());
                                	stn.setArrTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runDate).plusDays(arrRunDays).toString("yyyy-MM-dd") + " " + trainStn.getArrTime()).getTime()));
                                	stn.setDptTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runDate).plusDays(dptRunDays).toString("yyyy-MM-dd") + " " + trainStn.getDptTime()).getTime()));
                                	stn.setArrTrainNbr(trainStn.getArrTrainNbr());
                                	stn.setDptTrainNbr(trainStn.getDptTrainNbr());
                                	stn.setArrTimeStr(trainStn.getArrTime());
                                	stn.setDptTimeStr(trainStn.getDptTime());
                                	stn.setBaseArrTimeStr(trainStn.getBaseArrTime());
                                	stn.setBaseDptTimeStr(trainStn.getBaseDptTime());
                                	stn.setTrackNbr(trainStn.getTrackNbr());
                                	stn.setPlatform(trainStn.getPlatform());
                                	stn.setPsgFlag(trainStn.getPsgFlg());
                                	stn.setLocoFlag(trainStn.getLocoFlag());
                                	stn.setTecType(trainStn.getTecType());
                                	stn.setStnType(trainStn.getStnType());
                                	stn.settRunDays(dptRunDays);
                                	stn.setsRunDays(arrRunDays);
                                    //add to list
                                	stnList.add(stn);
                                	
                                }
                                //保存数据到plan_train中
                                runPlanLkService.addRunPlanLk(runPlan);
                                //保存数据到plan_train_stn中
                                runPlanLkService.addRunPlanLkTrainStn(stnList);
				        	}
				        }
				        
				        //更新表cmd_train中的字段createState  0:未生成，1：已生成
				        runPlanLkService.updateCreateStateForCmdTrainId(cmdTrainId, "1");
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
				//表cmd_train主键
				String cmdTrainId = StringUtil.objToStr(trainMap.get("cmdTrainId"));
				//局码简称
				String stnBureau = StringUtil.objToStr(trainMap.get("stnBureau"));
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //局简称
				 String bureuaShortName = user.getBureauShortName();
				 //先删除表cmd_train_stn中对应的数据
				 int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(cmdTrainId,bureuaShortName);
				 logger.debug("count==" + count);
			    //循环设置子表cmd_train_stn中的数据对象
			    for(JSONObject trainStn : trainStnList){
			    	
			    	//只保存本局的数据
			    	if(stnBureau.equals(bureuaShortName)){
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
			    	if((arrTime == null || "".equals(arrTime)) && (endTime != null && !"".equals(endTime))){
			    		stn.setArrTime(endTime);
				    	stn.setDptTime(endTime);
				    	
				    	if(baseArrTime == null || "".equals(baseArrTime)){
				    		stn.setBaseArrTime(endTime);
				    	}else{
				    		stn.setBaseArrTime(baseArrTime);
				    	}
				    	if(baseDptTime == null || "".equals(baseDptTime)){
				    		stn.setBaseDptTime(endTime);
				    	}else{
				    		stn.setBaseDptTime(baseDptTime);
				    	}
			    	}else if((endTime == null || "".equals(endTime)) && (arrTime != null && !"".equals(arrTime))){
			    		stn.setArrTime(arrTime);
				    	stn.setDptTime(arrTime);
				    	if(baseArrTime == null || "".equals(baseArrTime)){
				    		stn.setBaseArrTime(arrTime);
				    	}else{
				    		stn.setBaseArrTime(baseArrTime);
				    	}
				    	if(baseDptTime == null || "".equals(baseDptTime)){
				    		stn.setBaseDptTime(arrTime);
				    	}else{
				    		stn.setBaseDptTime(baseDptTime);
				    	}
			    	}
			    	
			    	Integer plantForm = Integer.valueOf(StringUtil.objToStr(trainStn.get("platform")));
			    	stn.setPlatform(plantForm == null?0:plantForm);
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
				String trainStr = (String)reqObj.get("cmdTrainMap");
				JSONObject trainMap = JSONObject.fromObject(trainStr);
				List<JSONObject>  trainStnList = reqObj.getJSONArray("cmdTrainStnList");
				String baseTrainId = StringUtil.objToStr(reqObj.get("baseTrainId"));
			    CmdTrain train = new CmdTrain();
			    ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				 //局简称
				 String bureuaShortName = user.getBureauShortName();
				 logger.debug("bureuaShortName=="+bureuaShortName);
				 Integer cmdTxtMlItemId = Integer.valueOf(StringUtil.objToStr(trainMap.get("cmdTxtMlItemId")));
				 //从本地数据库中查询
				 CmdTrain cmdTrain = runPlanLkService.getCmdTrainInfoForCmdTxtmlItemId(String.valueOf(cmdTxtMlItemId));
				 //表cmd_train主键
				 String cmdTrainId = "";
				 if(cmdTrain == null ){
					
				    //表cmd_train主键
					cmdTrainId = UUID.randomUUID().toString();
					train.setBaseTrainId(baseTrainId);
					train.setCmdTrainId(cmdTrainId);
				    train.setCmdBureau(bureuaShortName);
				    train.setCmdItem(Integer.valueOf(StringUtil.objToStr(trainMap.get("cmdItem"))));
				    train.setCmdNbrBureau(StringUtil.objToStr(trainMap.get("cmdNbrBureau")));
				    train.setCmdNbrSuperior(StringUtil.objToStr(trainMap.get("cmdNbrSuperior")));
				    train.setCmdTime(StringUtil.objToStr(trainMap.get("cmdTime")));
				    train.setCmdTxtMlId(Integer.valueOf(StringUtil.objToStr(trainMap.get("cmdTxtMlId"))));
				    train.setCmdTxtMlItemId(Integer.valueOf(StringUtil.objToStr(trainMap.get("cmdTxtMlItemId"))));
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
				    
				    if(!"".equals(baseTrainId) && baseTrainId != null){
				    	train.setSelectState("1");	
				    }else{
				    	train.setSelectState("0");	
				    }
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
				 int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(cmdTrainId,null);
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
			    	if((arrTime == null || "".equals(arrTime)) && (endTime != null && !"".equals(endTime))){
			    		stn.setArrTime(endTime);
				    	stn.setDptTime(endTime);
				    	
				    	if(baseArrTime == null || "".equals(baseArrTime)){
				    		stn.setBaseArrTime(endTime);
				    	}else{
				    		stn.setBaseArrTime(baseArrTime);
				    	}
				    	if(baseDptTime == null || "".equals(baseDptTime)){
				    		stn.setBaseDptTime(endTime);
				    	}else{
				    		stn.setBaseDptTime(baseDptTime);
				    	}
			    	}else if((endTime == null || "".equals(endTime)) && (arrTime != null && !"".equals(arrTime))){
			    		stn.setArrTime(arrTime);
				    	stn.setDptTime(arrTime);
				    	if(baseArrTime == null || "".equals(baseArrTime)){
				    		stn.setBaseArrTime(arrTime);
				    	}else{
				    		stn.setBaseArrTime(baseArrTime);
				    	}
				    	if(baseDptTime == null || "".equals(baseDptTime)){
				    		stn.setBaseDptTime(arrTime);
				    	}else{
				    		stn.setBaseDptTime(baseDptTime);
				    	}
			    	}
	
			    	String plantFormStr = StringUtil.objToStr(trainStn.get("platform"));
			    	Integer plantForm = Integer.valueOf("".equals(plantFormStr)?"0":plantFormStr);
			    	stn.setPlatform(plantForm == null?0:plantForm);
			    	stn.setStnBureau(StringUtil.objToStr(trainStn.get("stnBureau")));
			    	stn.setStnBureauFull(StringUtil.objToStr(trainStn.get("stnBureauFull")));
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
		 * 获取站的基本信息
		 * @param 
		 * @return data中为list，list中的map的key有：STNNAME(站名)，STNBUREAU(局的拼音码)，STNBUREAUFULLNAME（局全称），STNBUREAUSHORTNAME（局简称）
		 */
		@ResponseBody
		@RequestMapping(value = "/getBaseStationInfo", method = RequestMethod.POST)
		public Result getBaseStationInfo(@RequestBody Map<String,Object> reqMap){
			Result result = new Result();
			String stnName = StringUtil.objToStr(reqMap.get("stnName"));
			try{
				List<Map<String,Object>> list = runPlanLkService.getBaseStationInfo(stnName);
				result.setData(list);
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
