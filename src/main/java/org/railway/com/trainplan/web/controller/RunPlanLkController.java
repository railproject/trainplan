package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.railway.com.trainplan.entity.RunPlan;
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

/**
 * 临客相关操作
 * @author Think
 *
 */
@Controller
@RequestMapping(value = "/runPlanLk")
public class RunPlanLkController {
	
	 private static Log logger = LogFactory.getLog(RunPlanLkController.class.getName());
		
	 @Autowired
	 private RunPlanLkService runPlanLkService;


	 @RequestMapping(value="/addPage", method = RequestMethod.GET)
     public String addPage() {
		 return "runPlanLk/runPlanLk_add";
     }
	 
	 

	 @RequestMapping(value="/mainPage", method = RequestMethod.GET)
     public String mainPage() {
		 return "runPlanLk/runPlanLk_main";
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
