package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.SpringContextUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanTrainStn;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Transactional
@Monitored
public class PlanTrainStnService {
	private static final Logger logger = Logger.getLogger(PlanTrainStnService.class);
	
    @Autowired
    private CommonService commonService;
    
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private BaseDao baseDao;
    
    /**
	 * 更新数据表plan_train中字段daylyplan_flag,的值
	 * @param base_train_id
	 * @return
	 * @throws Exception
	 */
   
    public Result updatePlanTrainDaylyPlanFlag(Map<String,Object> reqMap) throws Exception {
    	Result result = new Result();
    	 baseDao.updateBySql(Constants.TRAINPLANDAO_UPDATE_PLANFLAG, reqMap);
    	return result;
    }
    /**
     * 更新表plan_train中字段check_state的值
     */
 
    public Result updatePlanTrainCheckStats(String base_train_id) throws Exception{
    	   Result result = new Result();
    	   //更新数据库
    	   baseDao.updateBySql(Constants.TRAINPLANDAO_UPDATE_CHECKSTATE, base_train_id);
    	   return result;
    }
  
    /**
     * runDate :格式：yyyyMMdd
     */
  
    public  List<ParamDto>  getTotalTrains(String runDate,String startBureauFull){
    	    List<ParamDto>  list = new ArrayList<ParamDto>();
    	    Map<String,String> paramMap = new HashMap<String,String>();
    	    paramMap.put("runDate", runDate);
    	    paramMap.put("startBureauFull",startBureauFull );
    	    List<Map<String,Object>> mapList = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TOTALTRAINS, paramMap);
    	    if(mapList != null && mapList.size() > 0){
    	    	 System.err.println("mapList.size===" + mapList.size());
    	    	for(Map<String,Object> map :mapList ){
    	    		ParamDto dto = new ParamDto();
    	    		dto.setSourceEntityId(StringUtil.objToStr(map.get("base_train_id")));
    	    		String time = StringUtil.objToStr(map.get("run_date"));
    	    		dto.setTime(DateUtil.formateDate(time));
    	    		list.add(dto);
    	    	}
    	    }
    	    return list;
    }
    


	/**
	 * 查询车次的始发站和终点站等信息
	 * 
	 * @param runDate
	 *            开行日期
	 * @param trainNbr
	 *            车次
	 * @param count
	 *            返回的条数
	 */
	
	public List<PlanTrainDto> getTrainShortInfo(String runDate,
			String trainNbr, int count) {
		List<PlanTrainDto> returnList = new ArrayList<PlanTrainDto>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("runDate",runDate);
		paramMap.put("rownum",count);
		if (trainNbr != null && !"".equals(trainNbr)) {
			paramMap.put("trainNbr",trainNbr);
		}
		// 调用dao获取数据  TRAINPLANDAO_GET_TRAIN_SHORTINFO
		List<Map<String, Object>> listMap = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TRAIN_SHORTINFO, paramMap);
		if (listMap != null && listMap.size() > 0) {
			for (Map<String, Object> map : listMap) {
				PlanTrainDto dto = new PlanTrainDto();
				dto.setPlanTrainId(StringUtil.objToStr(map.get("PLAN_TRAIN_ID")));
				dto.setEndStn(StringUtil.objToStr(map.get("END_STN")));
				dto.setRunDate(StringUtil.objToStr(map.get("RUN_DATE")));
				dto.setStartStn(StringUtil.objToStr(map.get("START_STN")));
				dto.setTrainNbr(StringUtil.objToStr(map.get("TRAIN_NBR")));
				returnList.add(dto);
			}
		}
		return returnList;
	}

	/**
	 * 访问接口，将返回数据存到本地数据库
	 * startDate 格式:yyyy-mm-dd
	 * @throws Exception
	 */

	public Map<String, Object> importTainPlan( String schemeId,
			 String startDate,  String dayCount) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//调用后台接口之前推送一条消息到页面
		quoteService.sendQuotes("", 0, 0, "plan.getInfo.begin");
		
		
		// 调用后台接口，获取数据
		 List<TrainlineTemplateDto> list = remoteService.getTrainLineInfoFromSchemeId(
				schemeId, startDate);
		
		System.err.println("importTrainPlan--list.size==" + list.size());
		
		// 循环dayCount次
		TrainPlanThread  thread = new TrainPlanThread(list,startDate,dayCount,this,quoteService);
		//启动thread
		thread.start();
		
		//returnMap.put("", schemeId);
		return returnMap;
	}

	
	/**
	 * 根据方案id查询列车并根据列车id查询列车时刻表
	 * @param schemeId 方案id
	 * @param runDate格式 yyyy-mm-dd
	 * @return
	 */
	public List<TrainlineTemplateDto>  getTrainsWithSchemeId(String schemeId,String runDate){
		   List<TrainlineTemplateDto> trainsList = new ArrayList<TrainlineTemplateDto>();
		   
		   return trainsList;
	}
	/**
	 * 将后台接口返回数据存入本地PLAN_TRAIN和PLAN_TRAIN_STN库中
	 * @param list
	 * @param tempStartDate 格式yyyy-mm-dd
	 */
	public void addTrainPlanLine(List<TrainlineTemplateDto> list,String tempStartDate) {
		
				if (list != null && list.size() > 0) {
					
					for (TrainlineTemplateDto dto : list) {
						String runDate = tempStartDate;
						String trainId= UUID.randomUUID().toString();
						try{
						
						runDate = DateUtil.format(DateUtil.parse(runDate),"yyyy-MM-dd");
						String starttime = dto.getStartTime();
						String endtime = dto.getEndTime();
	
						Map<String,Object> paramMap = new HashMap<String,Object>();
						paramMap.put("runDate",runDate.replaceAll("-", ""));
						paramMap.put("trainNbr",dto.getTrainNbr() );
						paramMap.put("startTime",starttime );
						paramMap.put("endTime",endtime);
						paramMap.put("startStn",dto.getStartStn() );
						paramMap.put("endStn",dto.getEndStn() );
						paramMap.put("baseChartId",dto.getBaseChartId() );
						paramMap.put("baseTrainId",dto.getBaseTrainId() );
						paramMap.put("startBureauFull", dto.getStartBureauFull());
						paramMap.put("endBureauFull",dto.getEndBureauFull() );
						paramMap.put("trainType",dto.getTrainType() );
						//插入数据库
					    int successCount = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN, paramMap);
						logger.info("count of inserting into train_plan==" + successCount);
						//获取当前的plan_train_id  plan_train_id
						//Map map = (Map)baseDao.selectOneBySql(Constants.TRAINPLANDAO_GET_MAX_PLANTRAIN_ID, null);
						//trainId = (BigDecimal)map.get("plan_train_id");
						// 获取经由信息
						List<TrainlineTemplateSubDto> stationList = dto
								.getStationList();
						//System.err.println("stationList==" + stationList);
						if (stationList != null && stationList.size() > 0) {
                            
							List<PlanTrainStn> tempList = new ArrayList<PlanTrainStn>();
							
							for (TrainlineTemplateSubDto dtoStn : stationList) {
								
								PlanTrainStn tempSubDto = new PlanTrainStn();
								//数据库主键
								String planTrainStnId = UUID.randomUUID().toString();
								tempSubDto.setPlanTrainStnId(planTrainStnId);
								
								String sourceTime = dtoStn.getSourceTime();
								int daycountSouce = Integer.valueOf(sourceTime.substring(0,1));
								String trueRunDate = DateUtil.getDateByDay(runDate, -daycountSouce);
								String sourcetime = trueRunDate+ " "+ StringUtil.handleTime(sourceTime);
								
								String targetTime = dtoStn.getTargetTime();
								int daycountTarget = Integer.valueOf(targetTime.substring(0,1));
								trueRunDate = DateUtil.getDateByDay(runDate, -daycountTarget);
								String targettime = trueRunDate+ " "+ StringUtil.handleTime(targetTime);
                               
								
								tempSubDto.setArrTime(sourcetime);
								tempSubDto.setDptTime(targettime);
								tempSubDto.setBaseArrTime(sourcetime);
								tempSubDto.setBaseDptTime(targettime );
								tempSubDto.setStnName(dtoStn.getName());
								tempSubDto.setStnBureauFull(dtoStn.getStnBureauFull());
								tempSubDto.setStnSort(dtoStn.getIndex());
								tempSubDto.setTrackName(dtoStn.getTrackName());
								tempSubDto.setRunDays(dtoStn.getRunDays());
								
								tempList.add(tempSubDto);
								
								
							}
							//批量插入数据表train_plan_stn
							int successCountStn = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN_STN, tempList);
							logger.info("count of inserting into train_plan_stn==" + successCountStn);
							
						}

					}catch(Exception e){
						//不做任何处理，只打印日志
						e.printStackTrace();
						logger.error("存表plan_train失败，plan_train_id["+trainId +"],runDate["+runDate+"]");
					}
				 }
					
				}
		
	}

	/**
	 * 对站点设置基本信息
	 * 
	 * @param map
	 * @param stationType
	 * @return
	 */
	private TrainlineTemplateSubDto setSubDtoValue(Map<String, Object> map,
			String stationType) {
		TrainlineTemplateSubDto subDto = new TrainlineTemplateSubDto();
		/**
		 * String bureauName = StringUtil.objToStr(targetItemMap.get("bureauName"));
								dto.setEndBureauFull(bureauName ==null? "":bureauName);
		 */
		subDto.setName(StringUtil.objToStr(map.get("name")));
		
		//subDto.setBureauName(bureauName ==null? "":bureauName);
		subDto.setSourceTime(StringUtil.objToStr(map.get("sourceTimeDto2")));
		subDto.setTargetTime(StringUtil.objToStr(map.get("targetTimeDto2")));
		subDto.setIndex(StringUtil.strToInteger(StringUtil.objToStr(map
				.get("index"))));
		String bureauName = StringUtil.objToStr(map.get("bureauName"));
		//subDto.setStnBureauFull(bureauName ==null? "":bureauName);
		subDto.setStnBureauFull(bureauName);
		// TODO:局码,现在后台报文还没这个字段
		subDto.setStnBureau(StringUtil.objToStr(map.get("")));
		// 站点类型 【1：始发 2：经由 3：终到】
		subDto.setStationType(stationType);
		//股道
		subDto.setTrackName(StringUtil.objToStr(map.get("trackName")));
		//运行天数
		String sourceTime = StringUtil.objToStr(map.get("sourceTimeDto2"));
		if(sourceTime != null && !"".equals(sourceTime)&& sourceTime.length()>0 ){
			subDto.setRunDays(Integer.valueOf(sourceTime.substring(0,1)));
		}
		
		return subDto;
	}
	
}
