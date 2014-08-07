package org.railway.com.trainplan.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StationMerge;
import org.railway.com.trainplan.common.utils.StattionUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.BaseCrossTrainSubInfo;
import org.railway.com.trainplan.entity.CrossDictInfo;
import org.railway.com.trainplan.entity.CrossDictStnInfo;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.SubCrossInfo;
import org.railway.com.trainplan.entity.TrainLineInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
import org.railway.com.trainplan.entity.UnitCrossTrainInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfoTime;
import org.railway.com.trainplan.service.CrossDictService;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.CrossRelationDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

 

@Controller
@RequestMapping(value = "/cross")
public class CrossController {
	private static Log logger = LogFactory.getLog(CrossController.class.getName());
	 @RequestMapping(method = RequestMethod.GET)
     public String content() {
		 return "cross/cross_manage";
     }
	 
	 @RequestMapping(value="/unit", method = RequestMethod.GET)
     public String unit() {
		 return "cross/cross_unit_manage";
     } 
	 
	 @RequestMapping(value="/crossCanvas", method = RequestMethod.GET)
     public String crossCanvas() {
		 return "cross/canvas_event_getvalue";
     }
	
	 @Autowired
	private CrossService crossService;
	
	@Autowired
	private RemoteService remoteService;
	
	@Autowired
	private TrainInfoService trainInfoService;
	
	@Autowired
	private CrossDictService crossDictService;
	
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Result getFullStationTrains(HttpServletRequest request, HttpServletResponse response){
		Result result = new Result(); 
		  try {  
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
				String chartId = request.getParameter("chartId");
				String chartName = request.getParameter("chartName");
				String startDay = request.getParameter("startDay"); 
				String addFlag = request.getParameter("addFlag"); 
			    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();   
			    for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {   
			    	// 上传文件 
			    	MultipartFile mf = entity.getValue();  
			    	crossService.actionExcel(mf.getInputStream(), chartId, startDay, chartName, addFlag);
			  	}  
 
			} catch (Exception e) {
				
				e.printStackTrace();
				result.setCode("401");
				result.setMessage("上传失败");
			}  
		return result;
	}
	
	/**
	 * 修改对数表信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editBaseCorssInfo", method = RequestMethod.POST)
	public Result editBaseCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.info("editBaseCorssInfo~~~~reqMap==" + reqMap);
			String baseCrossId =  StringUtil.objToStr(reqMap.get("crossId"));
			//通过baseCrossId查询crossInfo对象
			CrossInfo crossInfo = crossService.getCrossInfoForCrossid(baseCrossId);
			crossInfo.setHighlineRule(StringUtil.objToStr(reqMap.get("highlineRule")));
			crossInfo.setCommonlineRule(StringUtil.objToStr(reqMap.get("commonlineRule")));	
			crossInfo.setPairNbr(StringUtil.objToStr(reqMap.get("pairNbr")));
			crossInfo.setCutOld(Integer.valueOf(StringUtil.objToStr(reqMap.get("cutOld"))));
			crossInfo.setCrossStartDate(StringUtil.objToStr(reqMap.get("crossStartDate")));
			crossInfo.setCrossEndDate(StringUtil.objToStr(reqMap.get("crossEndDate")));
			crossInfo.setAppointWeek(StringUtil.objToStr(reqMap.get("appointWeek")));
			crossInfo.setAppointDay(StringUtil.objToStr(reqMap.get("appointDay")));
			crossInfo.setTokenVehDept(StringUtil.objToStr(reqMap.get("tokenVehDept")));
			crossInfo.setTokenVehDepot(StringUtil.objToStr(reqMap.get("tokenVehDepot")));
			crossInfo.setTokenPsgDept(StringUtil.objToStr(reqMap.get("tokenPsgDept")));
			crossInfo.setLocoType(StringUtil.objToStr(reqMap.get("locoType")));
			crossInfo.setCrhType(StringUtil.objToStr(reqMap.get("crhType")));
			crossInfo.setElecSupply(Integer.valueOf(StringUtil.objToStr(reqMap.get("elecSupply"))));
			crossInfo.setDejCollect(Integer.valueOf(StringUtil.objToStr(reqMap.get("dejCollect"))));
			crossInfo.setAirCondition(Integer.valueOf(StringUtil.objToStr(reqMap.get("airCondition"))));
			crossInfo.setNote( StringUtil.objToStr(reqMap.get("note")));
			crossInfo.setThroughline(StringUtil.objToStr(reqMap.get("throughline")));
			crossInfo.setCreateUnitTime("");
			crossInfo.setBaseCrossId(baseCrossId);
			
		
			if(crossInfo != null){
				//通过baseCrossId查询unitCross信息
				List<CrossInfo> unitCrossInfoList = crossService.getUnitCrossInfosForCrossId(baseCrossId);
			    //删除unit_cross_train中的数据
				if( unitCrossInfoList != null && unitCrossInfoList.size() > 0){
					
					List<String> unitCrossIdsList = new ArrayList<String>();
					for(CrossInfo  cross : unitCrossInfoList){
						unitCrossIdsList.add(cross.getBaseCrossId());
					}
					//删除unit_cross_train中的数据
					crossService.deleteUnitCrossInfoTrainForCorssIds(unitCrossIdsList);
					//删除unit_cross中的数据
					crossService.deleteUnitCrossInfoForCorssIds(unitCrossIdsList);
					
					
				}
				//更改crossinfo,crossInfo要根据页面传入重新设置，并将createUnitTime设为空
				crossService.updateBaseCross(crossInfo);
			}
			
		}catch(Exception e){
			 logger.error("editBaseCrossInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		return result;
	}
	
	/**
	 * 删除unit_cross表和表unit_cross_train中数据
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteUnitCorssInfo", method = RequestMethod.POST)
	public Result deleteUnitCorssInfo(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 try{
			 String unitCrossIds = StringUtil.objToStr(reqMap.get("unitCrossIds"));
			 if(unitCrossIds != null){
				String[] crossIdsArray = unitCrossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for(int i = 0;i<crossIdsArray.length;i++){
					crossIdsList.add(crossIdsArray[i]);
				}
				//先删除unit_cross_train表中数据
				int countTrain = crossService.deleteUnitCrossInfoTrainForCorssIds(crossIdsList);
				//删除unit_cross表中数据
				int count = crossService.deleteUnitCrossInfoForCorssIds(crossIdsList);
			 }  
		 }catch(Exception e){
			 logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	}
	
	/**
	 * 删除base_cross表和表base_cross_train中数据
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCorssInfo", method = RequestMethod.POST)
	public Result deleteCorssInfo(@RequestBody Map<String,Object> reqMap){
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for(int i = 0;i<crossIdsArray.length;i++){
					crossIdsList.add(crossIdsArray[i]);
				}
				//先删除base_cross_train表中数据
				int countTrain = crossService.deleteCrossInfoTrainForCorssIds(crossIdsList);
				//删除base_cross表中数据
				int count = crossService.deleteCrossInfoForCorssIds(crossIdsList);
			 }  
		 }catch(Exception e){
			 logger.error("deleteCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	 
		 }
		 return result;
	}
	
	/**
	 * 审核交路信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCorssInfo", method = RequestMethod.POST)
	public Result checkCorssInfo(@RequestBody Map<String,Object> reqMap) {
		 Result result = new Result();
		 try{
			 String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			 if(crossIds != null){
				String[] crossIdsArray = crossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for(int i = 0;i<crossIdsArray.length;i++){
					crossIdsList.add(crossIdsArray[i]);
				}
				int count = crossService.updateCorssCheckTime(crossIdsList);
				logger.debug("update--count==" + count);
			 } 
		 }catch(Exception e){
			 logger.error("checkCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	/**
	 * 审核交路信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUnitCorssInfo", method = RequestMethod.POST)
	public Result checkUnitCorssInfo(@RequestBody Map<String,Object> reqMap) {
		 Result result = new Result();
		 try{
			 String unitCrossIds = StringUtil.objToStr(reqMap.get("unitCrossIds"));
			 if(unitCrossIds != null){
				String[] unitCrossIdsArray = unitCrossIds.split(",");
				List<String> unitCrossIdsList = new ArrayList<String>();
				for(int i = 0;i<unitCrossIdsArray.length;i++){
					unitCrossIdsList.add(unitCrossIdsArray[i]);
				}
				int count = crossService.updateUnitCorssCheckTime(unitCrossIdsList);
				logger.debug("update--count==" + count);
			 } 
		 }catch(Exception e){
			 logger.error("checkCorssInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	/**
	 * 提供画交路单元图形的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideUnitCrossChartData", method = RequestMethod.GET)
	public ModelAndView  provideUnitCrossChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 ModelAndView result = new ModelAndView("cross/unit_cross_canvas"); 
		 ObjectMapper objectMapper = new ObjectMapper();
		 String unitCrossId = StringUtil.objToStr(request.getParameter("unitCrossId"));
		 PlanLineGrid grid = null;
		 String crossStartDate = "";
		 String crossEndDate = "";
		 
		 logger.info("unitCrossId---unit=="+ unitCrossId);
		 //用于纵坐标的列车list
		 List<Station> stationDictList = new LinkedList<Station>(); 
		 //标识纵坐标数据是否来源与经由字典
		 boolean isDictDate = false;
	     //查看经由字典是否有数据
		 List<CrossDictStnInfo> listDitStnInfo = crossDictService.getCrossDictStnForUnitCorssId(unitCrossId);
		 
		 if(listDitStnInfo != null && listDitStnInfo.size() > 0){
			 isDictDate = true;
			 for(CrossDictStnInfo dictStnInfo : listDitStnInfo){
				 Station station = new Station();
				 station.setStnName(dictStnInfo.getStnName());
				 String stationType = dictStnInfo.getStnType();
				 //车站类型（1:发到站，2:分界口，3:停站,4:不停站）
				 if("1".equals(stationType)){
					 stationType = "0";
				 }else if("4".equals(stationType)){
					 stationType = "BT";
				 }else if("3".equals(stationType)){
					 stationType = "TZ";
				 }else if("2".equals(stationType)){
					 stationType = "FJK";
				 }
				 station.setStationType(stationType);
				 stationDictList.add(station);
			 }
		 }
		 //众坐标的站列表
		 List<String> stationList = new ArrayList<String>();
		//通过unitCrossId获取unitCross列表信息
		 List<UnitCrossTrainInfo> list = crossService.getUnitCrossTrainInfoForUnitCrossid(unitCrossId);
		 List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		 if(list != null && list.size() > 0){
			 int size = list.size();
			 //i是用来循环交路单元的组
			 for(int i = 0;i<size;i++){
				 Map<String,Object> crossMap = new HashMap<String,Object>();
				 UnitCrossTrainInfo unitCrossInfo = list.get(i);
				 String groupSerialNbr = unitCrossInfo.getGroupSerialNbr();
				 //列车信息列表
				 List<UnitCrossTrainSubInfo> unitStationsList = unitCrossInfo.getTrainInfoList();
				//列车信息列表
				 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				 
				 if(unitStationsList != null && unitStationsList.size() > 0){
					 int sizeStation = unitStationsList.size();
					 for(int j = 0;j<unitStationsList.size();j++){
						
						 UnitCrossTrainSubInfo subInfo = unitStationsList.get(j);
						 TrainInfoDto dto = new TrainInfoDto();
						 dto.setTrainName(subInfo.getTrainNbr());
						 dto.setStartStn(subInfo.getStartStn());
						 dto.setEndStn(subInfo.getEndStn());
						 dto.setStartDate(subInfo.getRunDate());
						 dto.setEndDate(subInfo.getEndDate());
						 dto.setGroupSerialNbr(groupSerialNbr);
						 //列车经由时刻等信息
						 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
						 List<UnitCrossTrainSubInfoTime> stationTimeList = subInfo.getStationTimeList();
						 if(stationTimeList != null && stationTimeList.size() > 0){
							 for(int k = 0;k<stationTimeList.size();k++){
								 UnitCrossTrainSubInfoTime subInfoTime = stationTimeList.get(k);	 
								 PlanLineSTNDto stnDto = new PlanLineSTNDto();
								 BeanUtils.copyProperties(stnDto, subInfoTime);
								 trainStns.add(stnDto);
							 }
							 dto.setTrainStns(trainStns);
						 }
						 
						 trains.add(dto);
						
						 
						 if(i == 0 && j == 0){
							 //取第一组交路单元的第一个列车的始发日期作为横坐标的开始
							 crossStartDate = subInfo.getRunDate();
							 if(crossStartDate != null){
								 crossStartDate = crossStartDate.substring(0,10);
							 }
						 }else if(i == size-1 && j == sizeStation - 1){
							 //取最后一组交路单元的最后一个列车的终到日期作为横坐标的结束
							 crossEndDate = subInfo.getEndDate();
							 if(crossEndDate != null ){
								 crossEndDate = crossEndDate.substring(0,10);
							 }
						 }	
						 if(!isDictDate){
							//合并始发站和终到站
							 if(!stationList.contains(subInfo.getStartStn())){
								 stationList.add(subInfo.getStartStn());
							 }
							 if(!stationList.contains(subInfo.getEndStn())){
								 stationList.add(subInfo.getEndStn());
							 } 
						 }
						
						
					 } 
				 }
				
				 //组装接续关系
				 List<CrossRelationDto> jxgx = getJxgx(trains);
				 crossMap.put("jxgx", jxgx);
				 crossMap.put("trains", trains);
				 crossMap.put("groupSerialNbr",groupSerialNbr);
				 crossMap.put("crossName","");
				 dataList.add(crossMap);
			 }
		 }
		 logger.info("isDictDate==" + isDictDate );
		 if(isDictDate){
			 List<PlanLineGridY> listGridY = getPlanLineGridY(stationDictList); 
			 List<PlanLineGridX> listGridX = getPlanLineGridX(crossStartDate,crossEndDate);
		     grid = new PlanLineGrid(listGridX, listGridY);
		 }else{
			 //组装坐标
			 grid = getPlanLineGrid(stationList,crossStartDate,crossEndDate);	 
		 }
		
		 String myJlData = objectMapper.writeValueAsString(dataList);
			//图形数据
			result.addObject("myJlData",myJlData);
			logger.info("myJlData==" + myJlData);
			
			String gridStr = objectMapper.writeValueAsString(grid);
			logger.info("gridStr==" + gridStr);
			result.addObject("gridData",gridStr);
			
		
		 return result;
	}
	
	/**
	 * 提供画交路图形的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideCrossChartData", method = RequestMethod.GET)
	public ModelAndView  provideCrossChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		    ModelAndView result = new ModelAndView("cross/cross_canvas"); 
		
			String crossId = StringUtil.objToStr(request.getParameter("crossId"));
			
			//经由信息，由后面调用接口获取，用户提供画图的坐标
			PlanLineGrid grid = null;
			 Map<String,Object> crossMap = new HashMap<String,Object>();
			 ObjectMapper objectMapper = new ObjectMapper();
			 List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			//先查看表draw_graph中是否有该交路的信息
			 List<CrossDictStnInfo> listDicStn = crossDictService.getCrossDictStnForBaseCrossId(crossId);
			 //用于纵坐标的列车list
			 List<Station> stationList = new LinkedList<Station>();
			 //标识纵坐标的站点信息是否来源于字典
			 boolean isStationFromDic = false;
			 if(listDicStn != null && listDicStn.size() >0 ){
				 isStationFromDic = true;
				 for(CrossDictStnInfo dictStnInfo : listDicStn){
					 Station station = new Station();
					 station.setStnName(dictStnInfo.getStnName());
					 String stationType = dictStnInfo.getStnType();
					 //车站类型（1:发到站，2:分界口，3:停站,4:不停站）
					 if("1".equals(stationType)){
						 stationType = "0";
					 }else if("4".equals(stationType)){
						 stationType = "BT";
					 }else if("3".equals(stationType)){
						 stationType = "TZ";
					 }else if("2".equals(stationType)){
						 stationType = "FJK";
					 }
					 station.setStationType(stationType);
					 stationList.add(station);
				 }
			 }
			if(crossId !=null){
				 //根据crossId获取列车信息和始发站，终到站等信息
				 List<BaseCrossTrainInfo> list = crossService.getCrossTrainInfoForCrossId(crossId);
				 //只有一条记录
				 if(list != null && list.size() > 0){
					 BaseCrossTrainInfo trainInfo = list.get(0);
					 //列车信息
					 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					 //众坐标的站列表
					 List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
					 List<BaseCrossTrainSubInfo> trainList = trainInfo.getTrainList();
					 if(trainList != null && trainList.size() > 0){
						
						 for(int i = 0;i<trainList.size();i++){
							 
							 //列车信息
							 BaseCrossTrainSubInfo subInfo = trainList.get(i);
							 TrainInfoDto dto = new TrainInfoDto();
							 dto.setTrainName(subInfo.getTrainNbr());
							 dto.setStartStn(subInfo.getStartStn());
							 dto.setEndStn(subInfo.getEndStn());
							 dto.setStartDate(subInfo.getStartTime());
							 dto.setEndDate(subInfo.getEndTime());
							 //列车经由时刻等信息
							 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
							 //用于纵坐标的列车list
							 LinkedList<Station> trainsList = new LinkedList<Station>();
							 //列车经由时刻信息
							 List<BaseCrossTrainInfoTime> stationTimeList = subInfo.getStationList();
							 if(stationTimeList != null && stationTimeList.size() > 0 ){
								 for(int j = 0;j<stationTimeList.size();j++){
									 //纵坐标的一个站
									 Station station = new Station();
									 BaseCrossTrainInfoTime trainTime = stationTimeList.get(j);
									
									 PlanLineSTNDto stnDto = new PlanLineSTNDto();
									 BeanUtils.copyProperties(stnDto, trainTime);
									 station.setStnName(stnDto.getStnName());
									 station.setDptTime(stnDto.getDptTime());
									 station.setStationType(stnDto.getStationType());
									 trainStns.add(stnDto);
									 trainsList.add(station);
									 
									
								 }
								 dto.setTrainStns(trainStns) ;
								 list1.add(trainsList);
							 }
							 trains.add(dto);
						 }
					 }
					 
					 //组装接续关系
					 List<CrossRelationDto> jxgx = getJxgx(trains);
					 crossMap.put("jxgx", jxgx);
					 crossMap.put("trains", trains);
					 crossMap.put("crossName", trainInfo.getCrossName());
					 dataList.add(crossMap);
					
					 /********fortest*********/
				/*	 if(list1 != null&&list1.size() > 0){
						 for(LinkedList<Station> stationList : list1){
							 System.err.println("*****************************");
							 for(Station station :stationList){
								 System.err.println(station.getStnName() + "|" + station.getDptTime() + "|" + station.getStationType());
								 
							 }
						 }
					 }*/
					 /*******************/
					 if(isStationFromDic){
						 List<PlanLineGridY> listGridY = getPlanLineGridY(stationList); 
						 List<PlanLineGridX> listGridX = getPlanLineGridX(trainInfo.getCrossStartDate(),trainInfo.getCrossEndDate());
					     grid = new PlanLineGrid(listGridX, listGridY);
					 }else{
					    //liuhang
						grid = getPlanLineGridForAll(list1,trainInfo.getCrossStartDate(),trainInfo.getCrossEndDate());
						
						
						//查询有关baseCross的信息
						 CrossInfo crossInfo = crossService.getCrossInfoForCrossid(crossId);
						 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
						 //保存到数据库
						 //先保存表draw_graph的数据
						 CrossDictInfo dicInfo = new CrossDictInfo();
						 String drawGrapId = UUID.randomUUID().toString();
						 dicInfo.setDrawGrapId(drawGrapId);
						 dicInfo.setBaseChartId(crossInfo.getChartId());
						 dicInfo.setBaseChartName(crossInfo.getChartName());
						 dicInfo.setCrossName(crossInfo.getCrossName());
						 dicInfo.setBaseCrossId(crossId);
						 dicInfo.setCheckPeople(user.getName());
						 dicInfo.setCheckPeopleOrg(user.getDeptName());
						 dicInfo.setCreatePeople(user.getName());
						 dicInfo.setCreatePeopleOrg(user.getDeptName());
						 //保存数据
						 int countDic = crossDictService.addCrossDictInfo(dicInfo);
						 logger.debug("countDic===" + countDic);
						 List<CrossDictStnInfo> listDictStn = new ArrayList<CrossDictStnInfo>();
						 //获取纵坐标信息
						 List<PlanLineGridY> listGridY =  grid.getCrossStns();
						 int height = 0;
						 int heightSimple = 0;
						 int stnsort = 0;
						 for(int i = 0;i<listGridY.size();i++){
							 PlanLineGridY gridY = listGridY.get(i);
							 String stationType = gridY.getStationType();
							 if(!"BT".equals(stationType)){
								 //不存不停的站
								 CrossDictStnInfo crossStnInfo = new CrossDictStnInfo();
								 crossStnInfo.setDrawGrapId(drawGrapId);
								 crossStnInfo.setDrawGrapStnId(UUID.randomUUID().toString());
								 //TODO 所属局简称
								 crossStnInfo.setBureau("");
								 //默认每个站的间隔为100
								 height = height+100;
								 crossStnInfo.setHeightDetail(String.valueOf(height));
								 crossStnInfo.setStnName(gridY.getStnName());
								 crossStnInfo.setStnSort(stnsort++);
								 
								 //车站类型（1:发到站，2:分界口，3:停站）
								 if("0".equals(stationType)){
									 stationType = "1";		
								 }else if("TZ".equals(stationType)){
									 stationType = "3";
								 }else if("FJK".equals(stationType)){
									 stationType = "2";
								 }
								 crossStnInfo.setStnType(stationType);
								 
								 if(i == 0 && "1".equals(stationType)){
									 crossStnInfo.setHeightSimple(String.valueOf(0)) ;
								 }else if( i !=0 && "1".equals(stationType)){
									 heightSimple = heightSimple + 100;
									 crossStnInfo.setHeightSimple(String.valueOf(heightSimple));
								 }else{
									 crossStnInfo.setHeightSimple(String.valueOf(0));
								 }
								 listDictStn.add(crossStnInfo);	 
							 }
							
						 }
						
						 //保存数据
						 int countDicStn = crossDictService.batchAddCrossDictStnInfo(listDictStn);
						 logger.debug("countDicStn=="+ countDicStn);
					 }
					
					 //fortest
					// List<PlanLineGridY> listy = grid.getCrossStns();
					 //for(PlanLineGridY y :listy){
					///	 System.err.println(y.getStnName() + "#" + y.getDptTime()+"#"+y.getStationType());
					 //}
					 //////
					 String myJlData = objectMapper.writeValueAsString(dataList);
					  //图形数据
					  result.addObject("myJlData",myJlData);
					  logger.info("myJlData==" + myJlData);
					  //System.err.println("myJlData==" + myJlData);
					String gridStr = objectMapper.writeValueAsString(grid);
					logger.info("gridStr==" + gridStr);
					//System.err.println("gridStr==" + gridStr);
					result.addObject("gridData",gridStr);

				 }

			}
				
		return result ;
	}
	
	
	
	
	/**
	 * 列车运行线图
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMap", method = RequestMethod.POST)
	public Result  provideTrainLineChartDate(@RequestBody Map<String,Object> reqMap) throws Exception{
		 	Result result = new Result();
			String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
			String startTime = StringUtil.objToStr(reqMap.get("startTime"));
			String endTime = StringUtil.objToStr(reqMap.get("endTime"));
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
			String bureauShortName = user.getBureauShortName();
		
			//查询列车运行线信息
			List<TrainLineInfo>  list = crossService.getTrainPlanLineInfoForPlanCrossId(planCrossId,bureauShortName);
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			
			if(list != null && list.size() > 0 ){
				 //循环组
				 for(TrainLineInfo lineInfo :list ){
					 Map<String,Object> crossMap = new HashMap<String,Object>();
					 String groupSerialNbr = lineInfo.getGroupSerialNbr();
					 //列车信息列表
					 List<TrainLineSubInfo> subInfoList = lineInfo.getTrainSubInfoList();
					//列车信息
					 List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					 if(subInfoList != null && subInfoList.size() > 0 ){
						
						//循环列车
						 for(TrainLineSubInfo subInfo : subInfoList){
							 //设置列车信息
							 TrainInfoDto dto = new TrainInfoDto();
							 dto.setTrainName(subInfo.getTrainNbr());
							 dto.setStartStn(subInfo.getStartStn());
							 dto.setEndStn(subInfo.getEndStn());
							 dto.setGroupSerialNbr(groupSerialNbr);
							 dto.setStartDate(subInfo.getStartTime());
							 dto.setEndDate(subInfo.getEndTime());
							 dto.setPlanTrainId(subInfo.getPlanTrainId());
							 //起始站，终到站，分解口列表              
							 List<TrainLineSubInfoTime> subInfoTimeList = subInfo.getTrainStaionList();
							 
							 if(subInfoTimeList != null && subInfoTimeList.size() > 0){
								 List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
								 //循环经由站
								 for(TrainLineSubInfoTime subInfoTime : subInfoTimeList){
									 PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
									 stnDtoStart.setArrTime(subInfoTime.getArrTime());
									 stnDtoStart.setDptTime(subInfoTime.getDptTime());
									 stnDtoStart.setStayTime(subInfoTime.getStayTime());
									 stnDtoStart.setStnName(subInfoTime.getStnName());
									 stnDtoStart.setStationType(subInfoTime.getStationType());
									 trainStns.add(stnDtoStart);
								 }
								 dto.setTrainStns(trainStns);
							 }
							 trains.add(dto);
						 }
					 }
					 
					 //组装接续关系
					 List<CrossRelationDto> jxgx = getJxgx(trains);
					 crossMap.put("jxgx", jxgx);
					 crossMap.put("trains", trains);
					 crossMap.put("groupSerialNbr", groupSerialNbr);
					 dataList.add(crossMap);
				 }
				 
				 PlanLineGrid grid = null;
				 ObjectMapper objectMapper = new ObjectMapper();
				 
				 //纵坐标
				 List<Station> listStation = new ArrayList<Station>();
				 //组装坐标
				 //首先查看经由字典中是否有数据
				 boolean isHaveDate = false;
				 PlanCrossInfo planCrossInfo = crossService.getPlanCrossInfoForPlanCrossId(planCrossId);
				 if(planCrossInfo != null){
					 String baseCrossId = planCrossInfo.getBaseCrossId(); 
					 //通过baseCrossid查询经由站信息
					 List<CrossDictStnInfo> listDictStn = crossDictService.getCrossDictStnForBaseCrossId(baseCrossId);
				     if(listDictStn != null && listDictStn.size() > 0){
				    	 isHaveDate = true;
				    	
				    	 for(CrossDictStnInfo dictStn : listDictStn){
				    		 Station station = new Station();
				    		 station.setStnName(dictStn.getStnName());
				    		 String stationType = dictStn.getStnType();
				    		 //车站类型（1:发到站，2:分界口，3:停站,4:不停站）
							 if("1".equals(stationType)){
								 stationType = "0";
							 }else if("4".equals(stationType)){
								 stationType = "BT";
							 }else if("3".equals(stationType)){
								 stationType = "TZ";
							 }else if("2".equals(stationType)){
								 stationType = "FJK";
							 }
							 station.setStationType(stationType);
							 listStation.add(station);
				    	 }
				    	 //生成横纵坐标
				    	 List<PlanLineGridY> listGridY = getPlanLineGridY(listStation); 
						 List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,endTime);
					     grid = new PlanLineGrid(listGridX, listGridY);
				     }
				 
				 }
				 logger.info("isHaveDate===" + isHaveDate);
				 //经由字典中没有数据，则组装
				 if(!isHaveDate){
					 
					 if(dataList != null && dataList.size() > 0){
						 Map<String,Object> crossmap = dataList.get(0);
						 List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap.get("trains");
						 //取交路中的第一辆车的经由站信息
						 TrainInfoDto dto = trains.get(0);
						 List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						 for(PlanLineSTNDto trainStn : listTrainStn){
				    		 Station station = new Station();
				    		 station.setStnName(trainStn.getStnName());
							 station.setStationType(trainStn.getStationType());
							 listStation.add(station);
				    	 }
						 //生成横纵坐标
				    	 List<PlanLineGridY> listGridY = getPlanLineGridY(listStation); 
						 List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,endTime);
					     grid = new PlanLineGrid(listGridX, listGridY);
					 }	 
				 }
				
				 String myJlData = objectMapper.writeValueAsString(dataList);
			     //图形数据
				 Map<String,Object> dataMap = new HashMap<String,Object>();
				 String gridStr = objectMapper.writeValueAsString(grid);
				 dataMap.put("myJlData",myJlData);
				 dataMap.put("gridData", gridStr);
				 //System.err.println("myJlData==" + myJlData);
				 //System.err.println("gridStr==" + gridStr);
				result.setData(dataMap);
				
			}
		return result ;
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
	
	
	
	/**
	 * 组装坐标轴数据
	 * @param list1  列车列表
	 * @param crossStartDate 交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate 交路终到日期，格式yyyy-MM-dd
	 * @return 坐标轴对象
	 */
	private PlanLineGrid getPlanLineGridForAll(List<LinkedList<Station>> list1,String crossStartDate,String crossEndDate){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>(); 
		 
		 /****组装纵坐标****/
		 int trainSize = list1.size();
		 List<Station> mergeList = new LinkedList<Station>();
		 for(int i = 0;i<trainSize;i++){
			
			if(i==0){
				LinkedList<Station> listStation1 = list1.get(0);
				LinkedList<Station> listStation2 = list1.get(1);
				boolean isSameDirection = StattionUtils.isSameDirection(listStation1, listStation2);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(listStation1, listStation2);
				}else{
					
					if(listStation1.size() >=listStation2.size()){
						mergeList=StattionUtils.mergeStation(listStation1, listStation2);
					}else{
						mergeList=StattionUtils.mergeStation(listStation2, listStation1);
					}
					
				}
			}else if(i>1){
				LinkedList<Station> trainStationCurrentList = list1.get(i);
				boolean isSameDirection = StattionUtils.isSameDirection(trainStationCurrentList, mergeList);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(mergeList, trainStationCurrentList);
				}else{
					if(mergeList.size() >= trainStationCurrentList.size()){
						mergeList=StattionUtils.mergeStation(mergeList, trainStationCurrentList);
					}else{
						mergeList=StattionUtils.mergeStation(trainStationCurrentList, mergeList);	
					}
				}
				
			}
			
		 }
		  
			for(Station station : mergeList){
			    if(station != null){
			    	//planLineGridYList.add(new PlanLineGridY(stationName));
			    	planLineGridYList.add(new PlanLineGridY(
			    			station.getStnName(),
			    			0,
			    			station.getStationType()));
			    }
				
			}
					
			
		 
		/*****组装横坐标  *****/
		
		 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     
		 return new PlanLineGrid(gridXList, planLineGridYList);
	}
	
	/**
	 * 组装坐标轴数据
	 * @param stationsInfo 经由站信息对象
	 * @param crossStartDate 交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate 交路终到日期，格式yyyy-MM-dd
	 * @return 坐标轴对象
	 */
	private PlanLineGrid getPlanLineGrid(List<String> stationsInfo,String crossStartDate,String crossEndDate){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>(); 
		 
		 /****组装纵坐标****/
		 if(stationsInfo != null){
			  
				for(String stationName : stationsInfo){
				    if(stationName != null){
				    	//planLineGridYList.add(new PlanLineGridY(stationName));
				    	planLineGridYList.add(new PlanLineGridY(
				    			stationName,
				    			0,
				    			"0"));
				    }
					
				}
					
			}
		 
		/*****组装横坐标  *****/
		
		 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     
		 return new PlanLineGrid(gridXList, planLineGridYList);
	}
	/**
	 * 组装接续关系
	 * @param trains
	 * @return
	 */
	private List<CrossRelationDto> getJxgx(List<TrainInfoDto> trains){
		List<CrossRelationDto> returnList = new ArrayList<CrossRelationDto>();
		if(trains != null && trains.size() > 0 ){
			int size = trains.size();
			for(int i = 0;i<size;i++){
				//接续关系的对象
				CrossRelationDto dto = new CrossRelationDto();
				int temp = i+1;
				if(temp<size){
					TrainInfoDto dtoCurrent = trains.get(i);
					TrainInfoDto dtoNext = trains.get(temp);
					
					//取i的终点站信息
					dto.setFromStnName(dtoCurrent.getEndStn());
					//取i的终点站日期和时刻进行组合
					dto.setFromTime(dtoCurrent.getEndDate());
					dto.setFromStartStnName(dtoCurrent.getStartStn());
					
					//设置i-1始发日期和时刻
					dto.setToTime(dtoNext.getStartDate());
					//取i-1的起点信息
					dto.setToStnName(dtoNext.getStartStn());
					dto.setToEndStnName(dtoNext.getEndStn());
					returnList.add(dto);
				}
				
			}
		}
		return returnList;
	}
	
	
	/**
	 * 5.2.4	更新给定列车的基本图运行线车底交路id
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUnitCrossId", method = RequestMethod.POST)
	public Result updateUnitCrossId(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		String unitCrossIds = StringUtil.objToStr(reqMap.get("unitCrossIds"));
		logger.info("updateUnitCrossId----unitCrossIds=="+unitCrossIds);
		
		try{
			if(unitCrossIds != null){
				JSONArray resultArr = new JSONArray();
				String[] crossArray = unitCrossIds.split(",");
				//取一条unit_cross_id查询出方案id
				String unitCrossid = crossArray[0];
				CrossInfo unitCrossInfo = crossService.getUnitCrossInfoForUnitCrossid(unitCrossid);
				//方案id
				String baseChartId= unitCrossInfo.getChartId() ;
				
				for(String unitCrossId :crossArray){
					//List<CrossTrainInfo> listTrainInfo = crossService.getCrossTrainInfoForCrossid(unitCrossId);
					
					try{
						List<CrossTrainInfo> listTrainInfo = crossService.getUnitCrossTrainInfoForUnitCrossId(unitCrossId);
						if(listTrainInfo !=null && listTrainInfo.size() > 0){ 
							List<String> trainNbrs = new ArrayList<String>();
							for(CrossTrainInfo trainInfo :listTrainInfo ){ 
								String trainNbr = trainInfo.getTrainNbr();
								trainNbrs.add(trainNbr); 
							}
							//调用后台接口
							String response = remoteService.updateUnitCrossId(baseChartId, unitCrossId, trainNbrs);
							
							if(response.equals(Constants.REMOTE_SERVICE_SUCCESS)){
								//调用后台接口成功，更新本地数据表unit_cross中字段CREAT_CROSS_TIME
								List<String> unitCrossIdList = new ArrayList<String>();
								unitCrossIdList.add(unitCrossId);
								crossService.updateUnitCrossUnitCreateTime(unitCrossIdList);
								JSONObject subResult = new JSONObject();
								subResult.put("unitCrossId", unitCrossId); 
								subResult.put("flag", 1); 
								resultArr.add(subResult); 
							} 
						}
					}catch (Exception e) {
						logger.error("updateUnitCrossId:[" + unitCrossId + "]error==" + e.getMessage());
					}  
				}
				result.setData(resultArr);
			}
		}catch(Exception e){
			logger.error("updateUnitCrossId error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossInfo", method = RequestMethod.POST)
	public Result getUnitCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		List<SubCrossInfo> list = null;
	    try{
	    	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	if(user.getBureau() != null){
	    		reqMap.put("currentBureau", user.getBureau());
	    	}
	    	
	    	list = crossService.getUnitCrossInfo(reqMap);
	    	PagingResult page = new PagingResult(crossService.getUnitCrossInfoCount(reqMap),list);
	    	result.setData(page);
	    }catch(Exception e){
			logger.error("getUnitCrossInfo error==" + e.getMessage());
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public Result getCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
		List<CrossInfo> list = null;
	    try{
	    	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	if(user.getBureau() != null){
	    		reqMap.put("currentBureau", user.getBureau());
	    	}
	    	
	    	list = crossService.getCrossInfo(reqMap); 
	    	PagingResult page = new PagingResult(crossService.getCrossInfoCount(reqMap),list);
	    	result.setData(page);
	    }catch(Exception e){
			logger.error("getCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	/**
	 * 获取基本交路单元信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossTrainInfo", method = RequestMethod.POST)
	public Result getUnitCrossTrainInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String unitCrossId = (String)reqMap.get("unitCrossId");
		logger.debug("unitCrossId==" + unitCrossId);
		 try{
			    	//先获取unitcross基本信息
				 CrossInfo crossinfo = crossService.getUnitCrossInfoForUnitCrossid(unitCrossId);
				 //再获取unitcrosstrainInfo信息
				 List<CrossTrainInfo> list = crossService.getUnitCrossTrainInfoForUnitCrossId(unitCrossId);
			     List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			     Map<String,Object> dataMap = new HashMap<String,Object>();
			     dataMap.put("crossinfo", crossinfo);
			     dataMap.put("unitCrossTrainInfo", list);
			     dataList.add(dataMap);
				 result.setData(dataList);
		    }catch(Exception e){
				logger.error("getUnitCrossTrainInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			}
		
		return result;
	}
	
	
	/**
	 * 获取车底交路信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossTrainInfo", method = RequestMethod.POST)
	public Result getCrossTrainInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String crossId = (String)reqMap.get("crossId");
		logger.debug("crossId==" + crossId);
		 try{
			    	//先获取cross基本信息
				 CrossInfo crossinfo = crossService.getCrossInfoForCrossid(crossId);
				 //再获取crosstrainInfo信息
				 List<CrossTrainInfo> list = crossService.getCrossTrainInfoForCrossid(crossId);
			     List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			     Map<String,Object> dataMap = new HashMap<String,Object>();
			     dataMap.put("crossInfo", crossinfo);
			     dataMap.put("crossTrainInfo", list);
			     dataList.add(dataMap);
				 result.setData(dataList);
		    }catch(Exception e){
				logger.error("getCrossTrainInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
			}
		
		return result;
	}
	
	/**
	 * 获取planCross信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanCrossInfo", method = RequestMethod.POST)
	public Result getPlanCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
			PlanCrossInfo  planCrossInfo = crossService.getPlanCrossInfoForPlanCrossId(planCrossId);
			result.setData(planCrossInfo);
		}catch(Exception e){
			logger.error("getPlanCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
		return result;
	}
	/**
	 * 生成基本交路单元
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/completeUnitCrossInfo", method = RequestMethod.POST)
	public Result completeUnitCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String message = "";
		try{
			//crossid以逗号分隔
			String crossId = StringUtil.objToStr(reqMap.get("crossIds"));
			logger.debug("crossId==" + crossId);
			if(crossId != null){
				String[] crossIds = crossId.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				JSONArray resultArr = new JSONArray();
				for(String crossid :crossIds){ 
					//根据crossid生成交路单元
					try{
						JSONObject obj = new JSONObject();
						crossService.completeUnitCrossInfo(crossid);
						crossIdsList.add(crossid);
						obj.put("crossId", crossid);
						obj.put("flag", 1); 
						resultArr.add(obj);
					}catch(Exception e){
						  
					} 
					
				} 
				//生成交路单元完成后，更改表base_cross中的creat_unit_time字段的值
				if(crossIdsList.size() > 0){
					crossService.updateCrossUnitCreateTime(crossIdsList);
				}
				result.setData(resultArr);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("completeUnitCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
		return result;
	}
	
	
	
	
	
}
