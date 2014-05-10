package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Component
@Transactional
@Monitored
public class PlanTrainCheckService {
	private static final Logger logger = Logger.getLogger(PlanTrainCheckService.class);

/*
	@Autowired
	private CommonService commonService;
	
	

	@Value("#{sqlprop['select.traintime']}")
	private String sqlTrainTimeDetail;
	
	@Value("#{sqlprop['select.gather.total.station.train']}")
	private String select_gather_total_station_train;
	
	@Value("#{sqlprop['select.gather.one.station.train']}")
	private String select_gather_one_station_train;
	
	@Value("#{sqlprop['select.gather.total.station.jieru']}")
	private String select_gather_total_station_jieru;
	
	@Value("#{sqlprop['select.gather.period.rundate.trains']}")
	private String select_gather_period_rundate_trains;
	
	@Value("#{sqlprop['select.period.rundate.trains.total']}")
	private String select_period_rundate_trains_total;
	
	@Value("#{sqlprop['select.rundate.train.count']}")
	private String select_rundate_train_count;
	
	//根据rundate和train_nbr删除表plan_train中的数据
	@Value("#{sqlprop['delete.plan.train.rundate.train.nbr']}")
	private String delete_plan_train_rundate_train_nbr;
	
	//根据plan_train_id删除表plan_train_stn中的数据
	@Value("#{sqlprop['delete.plan.train.rundate.train.stn']}")
	private String delete_plan_train_rundate_train_stn;
	
	//导入计划前删除子表历史数据
	@Value("#{sqlprop['delete.plan.train.rundate.train.stn.init']}")
	private String delete_plan_train_rundate_train_stn_init;
	
	//导入计划前删除父表历史数据
	@Value("#{sqlprop['delete.plan.train.rundate.train.nbr.init']}")
	private String delete_plan_train_rundate_train_nbr_init;
	
	*//**
	 * 根据run_date的开始时间和结束时间删除
	 * 表plan_train_stn中的历史数据
	 * @param startRunDate 格式：yyyyMMdd
	 * @param endRunDate 格式：yyyyMMdd
	 * @throws Exception
	 *//*
	public void deletePlanTrainStnHistoryDate(String startRunDate,String endRunDate) throws Exception {
		String sql = String.format(delete_plan_train_rundate_train_stn_init, startRunDate,endRunDate);
		System.err.println("deletePlanTrainStnHistoryDate---sql==" + sql);
		jdbcDao.execute(sql);
	}
	*//**
	 * 根据run_date的开始时间和结束时间删除
	 * 表plan_train中的历史数据
	 * @param startRunDate 格式：yyyyMMdd
	 * @param endRunDate 格式：yyyyMMdd
	 * @throws Exception
	 *//*
	public void deletePlanTrainHistoryDate(String startRunDate,String endRunDate) throws Exception {
		String sql = String.format(delete_plan_train_rundate_train_nbr_init, startRunDate,endRunDate);
		System.err.println("deletePlanTrainHistoryDate---sql==" + sql);
		jdbcDao.execute(sql);
	}
	*//**
	 * 根据条件删除表train_plan中数据
	 * @param runDate 开行日期，格式：yyyyMMdd
	 * @param trainNbr 列车号
	 * @throws Exception
	 *//*
	public void deletePlanTrainWithRundate(String runDate,String trainNbr) throws Exception{
		String sql = String.format(delete_plan_train_rundate_train_nbr, runDate,trainNbr);
		System.err.println("deletePlanTrainSql==" + sql);
		//执行删除动作
		jdbcDao.execute(sql);
	}
	
	*//**
	 * 根据条件删除表train_plan_stn中数据
	 * @param runDate 开行日期，格式：yyyyMMdd
	 * @param trainNbr 列车号
	 * @throws Exception
	 *//*
	public void deletePlanTrainStnWithRundate(String runDate,String trainNbr) throws Exception {
		String sql = String.format(delete_plan_train_rundate_train_stn, runDate,trainNbr);
		System.err.println("deletePlanTrainStnSql==" + sql);
		//执行删除动作
	    jdbcDao.execute(sql);
	}
	*//**
	 * 通过开行时间段查询某局的开行客运信息
	 * @param paging  分页对象
	 * @param startBureauFullName  客车始发局全称
	 * @param beginRunDate   开始运行时间,格式yyyy-MM-dd
	 * @param endRunDate   结束运行时间,格式yyyy-MM-dd
	 * @param trainNbr 车次号
	 * @return  分页对象
	 *//*
	@Override
	public PagingResult findPlanTrainWithPeriodRunDate(PagingInfo paging,String startBureauFullName,String startDate,int dayCount,String trainNbrFrom){
		int currentPage = paging.getCurrentPage();
		int pageSize = paging.getPageSize();
		
		int startNum = pageSize * currentPage +1;
		int endNum = pageSize * ( currentPage + 1);
		int totalCount = 0;
		List<Map<String,Object>> returnDaysList = new ArrayList<Map<String,Object>>();
		List<String> daysList = DateUtil.getDateListWithDayCount(startDate, dayCount);
		
		if(daysList != null && daysList.size() > 0){
			String endDate = DateUtil.getDateByDay(startDate, -(dayCount-1));
			//相当于格式化时间
			startDate =  DateUtil.getDateByDay(startDate, 0);
			String tempPara = "";
			if(!"".equals(trainNbrFrom) && trainNbrFrom != null){
				tempPara= " and t.TRAIN_NBR='" + trainNbrFrom + "'" ;
			}
			//查询数据库 
			String sql = String.format(select_gather_period_rundate_trains, 
					DateUtil.format(DateUtil.parse(startDate),"yyyyMMdd"),
					DateUtil.format(DateUtil.parse(endDate),"yyyyMMdd"),
					startBureauFullName,tempPara,
					startNum,endNum);
			System.err.println("select_gather_period_rundate_trains==" + sql);
			//某天某局的车次
			List<Map<String,Object>> list = jdbcDao.queryList(sql);
			
			if( list != null && list.size() > 0){
				//查询数据总的条数
				String sqlTotalNum = String.format(select_period_rundate_trains_total, DateUtil.format(DateUtil.parse(startDate),"yyyyMMdd"),DateUtil.format(DateUtil.parse(endDate),"yyyyMMdd"),startBureauFullName,tempPara);
				System.err.println("select_period_rundate_trains_total==" + sqlTotalNum);
				List<Map<String,Object>> listTotalCount = jdbcDao.queryList(sqlTotalNum);
				//只有一条数据
				if(listTotalCount != null){
					Map<String,Object> countMap = listTotalCount.get(0);
					totalCount = StringUtil.strToInteger(StringUtil.objToStr(countMap.get("count")));
					
				}
				for(Map<String,Object> map : list){
					Map<String,Object> pageDataMap = new HashMap<String,Object>();
					List<Map<String,Object>> days = new ArrayList<Map<String,Object>>();
					String trainNbr = StringUtil.objToStr(map.get("TRAIN_NBR"));
					
					pageDataMap.put("trainNbr", trainNbr);
					for(int i = 0;i<daysList.size();i++){
						Map<String,Object> dayMap = new HashMap<String,Object>();
						String myRunDate = DateUtil.getDateByDay(startDate, -i);
						
						dayMap.put("key",trainNbr+"@@"+myRunDate.replaceAll("-", "") );
						dayMap.put("index",i+1);
						String sqlCount = String.format(select_rundate_train_count, myRunDate.replaceAll("-", ""),trainNbr);
						int count = StringUtil.strToInteger(StringUtil.objToStr(jdbcDao.queryList(sqlCount).get(0).get("COUNT")));
						
						if(count > 0){
							//status:0:不开行，1：开行
							dayMap.put("status",1);
						}else{
							dayMap.put("status",0);
						}
						days.add(dayMap);
					}
					pageDataMap.put("days", days);
					returnDaysList.add(pageDataMap);
				}
			   	
			}
			
		}
		//long totalRecord, long pageSize, Object data
		System.err.println("returnDaysList==" + returnDaysList);
		System.err.println("totalCount==" + totalCount);
		return new PagingResult(Long.valueOf(totalCount),Long.valueOf(pageSize),returnDaysList);
	}
	

	*//**
	 * 6.1.1	查询始发日期为给定日期范围的日计划运行线统计数
	 * @param code:
	 * 	查询统计信息编码
         1.	code="01"，查询所有列车统计项的统计数
         2.	code="02"，只查询运行线统计项的统计数
         3.	code="10"，查询给定时间范围内路局统计数
	 * @param sourceTime  格式：yyyy-MM-dd HH:mm:ss
	 * @param targetTime  格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 *//*
	@Override
	public List<PlanBureauStatisticsDto>  getTrainLinesWithDay(String code,String sourceTime,String targetTime) throws Exception{
		List<PlanBureauStatisticsDto> returnList = new ArrayList<PlanBureauStatisticsDto>();
		Map<String,String> request = new HashMap<String,String>();
		request.put("code",code);
		request.put("sourceTime",sourceTime );
		request.put("targetTime", targetTime);
		System.err.println("getTrainLinesWithDay---request==" + request);
		//调用后台的接口
		Map response = RestClientUtils.post(Constants.SERVICE_URL
				+ Constants.GET_TRAINLINS, request, Map.class);
		System.err.println("getTrainLinesWithDay---response==" + response);
		//解析后台报文
		if (response != null && response.size() > 0){
			String codeMessage = StringUtil.objToStr(response.get("code"));
			if (!"".equals(codeMessage) && codeMessage.equals("200")){
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
				if (dataList != null && dataList.size() > 0){
					//取第一个,只有一条数据
					Map<String,Object> dataMap = dataList.get(0);
					//18个路局列表
					List<Map<String,Object>> stationsList = (List<Map<String,Object>>)dataMap.get("planBureauStatisticsDtos");
				    if(stationsList != null && stationsList.size() > 0){
				    	//循环18个路局，并取数据
				    	for(Map<String,Object> stationMap : stationsList){
				    		PlanBureauStatisticsDto dto = new PlanBureauStatisticsDto();
				    		dto.setBureauName(StringUtil.objToStr(stationMap.get("bureauName")));
				    		dto.setBureauShortName(StringUtil.objToStr(stationMap.get("bureauShortName")));
				    		dto.setBureauId(StringUtil.objToStr(stationMap.get("bureauId")));
				    		dto.setBureauCode(StringUtil.objToStr(stationMap.get("bureauCode")));
				    		
				    		List<Map<String,Object>> listDtos =  (List<Map<String,Object>>)stationMap.get("planBureauTsDtos");
				    		if(listDtos != null && listDtos.size() > 0){
				    			for(Map<String,Object> subMap : listDtos){
				    				*//**
				    				 * 后台返回的数据
				    				 *  {
                                         "id": "客运图定",
                                         "sourceTargetTrainlineCounts": 0,
                                         "sourceSurrenderTrainlineCounts": 0,
                                         "accessTargetTrainlineCounts": 0,
                                         "accessSurrenderTrainlineCounts": 0
                                        }
				    				 *//*
				    				String id = StringUtil.objToStr(subMap.get("id"));
				    				if("客运图定".equals(id)){
				    					PlanBureauTsDto subDto = new PlanBureauTsDto();
				    					subDto.setSourceSurrenderTrainlineCounts((Integer)subMap.get("sourceSurrenderTrainlineCounts"));
				    					subDto.setSourceTargetTrainlineCounts((Integer)subMap.get("sourceTargetTrainlineCounts"));
				    					dto.getListBureauDto().add(subDto);
				    					break;
				    				}
				    			}
				    		}
				    		
				    		returnList.add(dto);
				    	}
				    }
				}
			}
		}
		System.err.println("getTrainLinesWithDay---returnList==="+returnList);
		return returnList ;
	}
	
	*//**
	 * 根据始发局局码、开行日期查询列车开行计划信息
	 * @param startBureau 始发局局码
	 * @param runDate 开行日期 yyyyMMdd
	 * @author denglj
	 * @return
	 *//*
	public PagingResult findPlanTrainByStartBureauAndRundate(PagingInfo paging, String startBureau, String runDate, String trainNbr){
		Search search = new Search();
		search.addFilterEqual("runDate", runDate);
//		search.addFilterEqual("startBureau", startBureau);
		search.addFilterEqual("startBureauFull", startBureau);
		if(trainNbr!=null) {
			search.addFilterEqual("trainNbr", trainNbr);
		}
		
		
		List<Sort> sortList = new ArrayList<Sort>();
		Sort sort = new Sort();
		sort.setDesc(false);
		sort.setProperty("trainNbr");
		sortList.add(sort);
		
		search.setSorts(sortList);
		search.setPage(paging.getCurrentPage()).setMaxResults(paging.getPageSize());
		SearchResult<PlanTrain> result = planTrainDao.searchAndCount(search);
		for(PlanTrain obj:result.getResult()) {
			//查询生成经由局
			StringBuffer tempJylj = new StringBuffer("");//经由路局
			String tempJyljStr = "";
			List<Map<String, Object>> listMap = this.getTrainTimeDetail(runDate, obj.getTrainNbr());
			String previousLjjc = "";//上一个路局简称
			if (listMap!=null && listMap.size()>0) {
				for(int i=0;i<listMap.size();i++) {
					String currentLjjc = String.valueOf(listMap.get(i).get("LJJC"));
					if (!previousLjjc.equals(currentLjjc)) {
						tempJylj.append(currentLjjc).append(",");
						previousLjjc = currentLjjc;
					}
				}
				if (!tempJylj.equals("")) {
					tempJyljStr = tempJylj.substring(0, tempJylj.lastIndexOf(","));
				}
				
			}
			obj.setJylj(tempJyljStr);
			
			
			//格式化时间
			obj.setStartTimeStr(DateUtil.format(obj.getStartTime(), "MMdd HH:mm"));
			obj.setEndTimeStr(DateUtil.format(obj.getEndTime(), "MMdd HH:mm"));
			//获取局
			try {
				obj.setStartBureau(commonService.getLjInfo(obj.getStartBureauFull()).getLjjc());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//获取局
			try {
				obj.setEndBureau(commonService.getLjInfo(obj.getEndBureauFull()).getLjjc());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
		
		PagingResult pageing = new PagingResult(result.getTotalCount(), paging.getPageSize(), result.getResult());
		return pageing;
	}
	
	*//**
	 * 统计某天某局接入交出，接入终到的列车数
	 * runDate:开行日期 yyyyMMdd
	 * bureauName :路局全称
	 *//*
	
	public Map<String,Object> getOneStationTrains(String runDate,String bureauName){
		List<Map<String,Object>> list = null;
		Map<String,Object> returnMap = null;
		String sql = String.format(select_gather_total_station_jieru, bureauName,bureauName,runDate,bureauName,bureauName,bureauName);
		list = jdbcDao.queryList(sql);
		if(list != null){
			//只有一条数据
			returnMap = list.get(0);
		}
		return returnMap;
	}
    *//**
     * 统计一个路局某天的图定，临客，施工等信息
     * @param runDate 开行日期，格式：yyyyMMdd
     * @param startBureauFull 路局全称
     *//*
	public List<Map<String,Object>> getFullStationTrains(String runDate,String startBureauFull){
		List<Map<String,Object>> resultList = null;
		String sql = "";
		//统计始发交出，始发终到
		if(startBureauFull !=null && !"".equals(startBureauFull)){
			//统计一个路局的信息
			sql = String.format(select_gather_one_station_train, runDate,runDate,startBureauFull);
		}else{
			//统计全路局的信息
			sql = String.format(select_gather_total_station_train, runDate,runDate);
		}
		logger.info("getFullStationTrains--sql===" + sql);
		resultList = jdbcDao.queryList(sql);
		//统计接入交出，接入终到
		return resultList;
	}
	
	
	
	
	*//**
	 * 根据车次及开行日期查询 时刻信息
	 * @param trainNbr 车次号
	 * @param runDate 开行日期 yyyy-MM-dd
	 * @author denglj
	 * @return
	 *//*
	public List<Map<String, Object>> getTrainTimeDetail(String runDate, String trainNbr) {
		String sql = String.format(sqlTrainTimeDetail, runDate, trainNbr);
		logger.info("sql==" + sql.toString());
		// 调用dao获取数据
		List<Map<String, Object>> listMap = jdbcDao.queryList(sql.toString());
		if (listMap != null && listMap.size()>0) {
			for (Map mp :listMap) {
				if("1".equals(String.valueOf(mp.get("IS_START_STN")))) {//始发站
					mp.put("ARR_TIME", "----");//到站时间
					mp.put("STOP_TIME", "----");//停靠时间
				} else if("1".equals(String.valueOf(mp.get("IS_END_STN")))) {//终到站
					mp.put("DPT_TIME", "----");//出发时间
					mp.put("STOP_TIME", "----");//停靠时间
				} else {
					mp.put("STOP_TIME", DateUtil.calcBetweenTwoTimes(String.valueOf(mp.get("ARR_TIME_ALL")), String.valueOf(mp.get("DPT_TIME_ALL"))));//停靠时间
				}
			}
		}
		
		return listMap;
	}
	
	*/
}
