package org.railway.com.trainplan.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class HighLineService{
	private static final Logger logger = Logger.getLogger(HighLineService.class); 
	
	@Autowired
	private BaseDao baseDao;
	
	public static void main(String[] args) throws IOException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException {
//		InputStream is = new FileInputStream(
//				"C:\\Users\\Administrator\\Desktop\\work\\交路相关\\对数表模板1.xls");
//		
//		CrossService a = new CrossService();
//		System.out.println(StringUtils.isEmpty(""));
//		Date date = dateFormat.parse("20140518");   
//		Calendar calendar = new GregorianCalendar();
//		calendar.setTime(date);
//		calendar.add(Calendar.DATE, 4 - 1);
	 
		//System.out.println(dateFormat.format(calendar.getTime()));
//		a.actionExcel(is); 
//		System.out.println("G11(".substring(0,"G11(".indexOf('(')));
	} 

	/**
	 * 对表plan_cross批量插入数据
	 * @param list
	 * @return
	 */
	public int addPlanCrossInfo(List<PlanCrossInfo> list){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("trainCrossList",list );
		int count = baseDao.insertBySql(Constants.CROSSDAO_ADD_PLAN_CROSS_INFO, reqMap);
		//System.err.println("addPlanCrossInfo--count==" + count);
		return count;
	}
	
	public List<BaseTrainInfo> getBaseTrainInfoByParam(Map<String, String> map){
		return this.baseDao.selectListBySql(Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM, map);
	}
	
	/**
	 * 通过plan_cross_id查询plancross信息
	 * @param planCrossId 表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoForPlanCrossId(String planCrossId){
		return (PlanCrossInfo)this.baseDao.selectOneBySql(Constants.CROSSDAO_GET_PLANCROSSINFO_FOR_PLANCROSSID, planCrossId);
	}

	public int updateCorssCheckTime(String startDate) {
		// TODO Auto-generated method stub
		List<PlanCrossInfo> list = this.baseDao.selectListBySql(Constants.GET_PLANCROSSINFO_BY_STARTDATE, startDate);
		
		return 0;
	}
	 
	
	/**
	 * 查询highlineCross信息
	 * @param planCrossId
	 * @return
	 */
	public List<HighlineCrossInfo> getHighlineCrossList(String crossStartDate){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("crossStartDate",crossStartDate );
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_LIST, reqMap);
	}
	
	/**
	 * 查询highlineCrossTrain信息
	 * @param highlineCrossId
	 * @return
	 */
	public List<HighLineCrossTrainInfo> getHighlineCrossTrainList(String highlineCrossId){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("highlineCrossId",highlineCrossId );
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_LIST, reqMap);
	}
	
	/**
	 * 通过highlineCrossId查询
     * 交路下所有列车的始发站，终到站，始发时间和终到时间
	 * @param highlineCrossId
	 * @return
	 */
	public List<HighlineCrossTrainBaseInfo> getHighlineCrossTrainBaseInfoList(String highlineCrossId){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_BASEINFO, highlineCrossId);
	}
	
	/**
	 * 通过highlineCrossId查询该交路下的列车经由站信息
	 * @param highlineCrossId
	 * @return
	 */
	public List<HighlineTrainRunLine> getHighlineTrainTimeForHighlineCrossId(String highlineCrossId){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_TRAINTIME_FOR_HIGHLINE_CROSSID, highlineCrossId);
	}
}
