package org.railway.com.trainplan.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mor.railway.cmd.adapter.model.HighlineCross;
import mor.railway.cmd.adapter.service.impl.HighLinePlanGeneratorService;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineThroughlineInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.OptionDto;
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
		return count;
	}
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	public List<HighlineCross>  createHighLineCross(String bureauName,String bureauCode,String startDate) throws ParseException{
		Date runDate = DateUtil.parseDate(startDate,"yyyyMMdd");
		// 加载高铁计划服务
		List<HighlineCross> highlineData = HighLinePlanGeneratorService.getInstance().loadHighlineData(bureauName, bureauCode, runDate);

		return highlineData;
		
	}
	 
	/**
	 * 批量向表highline_cross表中插入数据
	 * @param hList
	 * @return
	 */
	public int  batchAddHighlineCross(List<HighlineCrossInfo> hList){
		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("hList", hList);
		return this.baseDao.insertBySql(Constants.CROSSDAO_ADD_HIGHLINE_CROSS, hMap);
	}
	
	/**
	 * 批量向表highline_cross_train表中插入数据
	 * @param tList
	 * @return
	 */
	public int batchAddHighlineCrossTrain(List<HighLineCrossTrainInfo> tList){
		return this.baseDao.insertBySql(Constants.CROSSDAO_ADD_HIGHLINE_CROSS_TRAIN, tList);
	}
	/**
	 * 查询highlineCross信息
	 * @param planCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighlineCrossInfo> getHighlineCrossList(Map<String, Object> reqMap2){
		 
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_LIST, reqMap2);
	}
	
	/**
	 * 查询highlineCrossTrain信息
	 * @param highlineCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	public List<HighlineCrossTrainBaseInfo> getHighlineCrossTrainBaseInfoList(String highlineCrossId){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_BASEINFO, highlineCrossId);
	}
	
	/**
	 * 通过highlineCrossId查询该交路下的列车经由站信息
	 * @param highlineCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighlineTrainRunLine> getHighlineTrainTimeForHighlineCrossId(String highlineCrossId){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_TRAINTIME_FOR_HIGHLINE_CROSSID, highlineCrossId);
	}
	
	/**
	 * 根据highlineCrossId删除表highline_cross_train中数据
	 * @param highlineCrossIds,多个id，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossTrainForHighlineCrossId(String highlineCrossIds){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("highlineCrossIds", highlineCrossIds);
		return baseDao.deleteBySql(Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSSTRAIN_FOR_ID, reqMap);
	}
	
	
	/**
	 * 根据highlineCrossId删除表highline_cross中数据
	 * @param highlineCrossIds, 多个id，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossForHighlineCrossId(String highlineCrossIds){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("highlineCrossIds", highlineCrossIds);
		return baseDao.deleteBySql(Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_ID, reqMap);
	}
	
	/**
	 * 根据highlineCrossId更新车底信息
	 * @param highlineCrossId
	 * @param vehicle1
	 * @param vehicle2
	 * @return
	 */
	public int updateHighLineVehicle(String highlineCrossId,String vehicle1,String vehicle2){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("highlineCrossId", highlineCrossId);
		reqMap.put("vehicle1",vehicle1 );
		reqMap.put("vehicle2",vehicle2 );
		return baseDao.deleteBySql(Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_VEHICLE, reqMap);
	}
	
	
	/**
	 * 根据时间和局码简称删除highline_cross表中数据
	 * @param startDate   对应数据表highline_cross中CROSS_START_DATE，格式yyyyMMdd
	 * @param crossBureau 局码简称，比如：京
	 * @return
	 */
	public int cleanHighLineForDate(String startDate,String crossBureau){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossBureau",crossBureau );
		reqMap.put("startDate", startDate);
		return baseDao.deleteBySql(Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_DATE, reqMap);
	}
	/**
	 * 更新highlinecross中check的基本信息
	 * @param checkType 审核状态
	 * @param checkPeople  审核人
	 * @param checkPeopleOrg 审核人所属单位
	 * @param highlineCrossIds
	 * @return
	 */
	public int updateHiglineCheckInfo(String checkType,String checkPeople,String checkPeopleOrg,String highlineCrossIds){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("checkType",checkType );
		reqMap.put("checkPeople", checkPeople);
		reqMap.put("checkPeopleOrg",checkPeopleOrg );
		reqMap.put("highlineCrossIds",highlineCrossIds);
		return baseDao.deleteBySql(Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_CHECKINFO, reqMap);
	}

	public void updateHighLineTrain(String highLineTrainId, String highLineCrossId) {
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("highLineTrainId", highLineTrainId );
		reqMap.put("highLineCrossId", highLineCrossId); 
	 
		baseDao.updateBySql(Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINECROSSID, reqMap);
	
	}

	public List<OptionDto> getVehicles() {
		
		Map<String,Object> reqMap = new HashMap<String,Object>(); 
		 
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_VEHICLES, reqMap);
	}
	
	/**
	 * 根据条件查询highline_cross表中数据
	 * @param crossInfo
	 * @return
	 */
	public List<HighlineCrossInfo> getHighlineCrossInfo(HighlineCrossInfo crossInfo){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_INFO, crossInfo);
	}
	
	/**
	 * 更新表highline_cross中数据
	 * @param crossInfo
	 * @return
	 */
	public int updateHighlineCrossInfo(HighlineCrossInfo crossInfo){
		return baseDao.updateBySql(Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_CROSSINFO, crossInfo);
	}
	
	/**
	 * 根据throughline分组查询highlineCross信息
	 * @param crossInfo
	 * @return
	 */
	public List<HighlineThroughlineInfo> getHighlineThroughCrossInfo(HighlineCrossInfo crossInfo){
		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_HIGHLINE_THROUGH_CROSSINFO, crossInfo);
	}
}
