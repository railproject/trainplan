package org.railway.com.trainplan.service;

import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.ExcelUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.SubCrossInfo;
import org.railway.com.trainplan.entity.TrainLineInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfoTime;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.BaseCrossDto;
import org.railway.com.trainplan.service.dto.BaseCrossTrainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
@Service
public class CrossService{
	private static final Logger logger = Logger.getLogger(CommonService.class);
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 

	@Autowired
	private CommonService commonService;
	
	@Value("#{restConfig['SERVICE_URL']}")
    private String restUrl;
	
	@Autowired
	private BaseDao baseDao;
	
	public static void main(String[] args) throws IOException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException {
//		InputStream is = new FileInputStream(
//				"C:\\Users\\Administrator\\Desktop\\work\\交路相关\\对数表模板1.xls");
//		
//		CrossService a = new CrossService();
//		System.out.println(StringUtils.isEmpty(""));
		Date date = dateFormat.parse("20140518");   
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 4 - 1);
	 
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
	/**
	 *@param chartId  方案id
	 * 通过chartId查询unit_cross信息
	 */
	public List<CrossInfo> getUnitCrossInfoForChartId(String chartId){
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSSINFO_FOR_CHARTID, chartId);
	}
	
	/**
	 *@param baseCrossId  base_cross_id
	 * 通过baseCrossId查询unit_cross信息列表
	 */
	public List<CrossInfo> getUnitCrossInfoForBaseCrossId(String baseCrossId){
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_UNITCROSS_INFO_FOR_BASECROSSID, baseCrossId);
	}
	
	/**
	 * 通过base_cross_id更新表base_cross
	 * @param crossInfo base_cross表对应的java对象
	 * @return 更新成功的数据条数
	 */
	public int updateBaseCross(CrossInfo crossInfo){
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_BASE_CROSS_INFO, crossInfo);
	}
	/**
	 * 更新base_cross中的creat_unit_time字段的值
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTime(List<String> crossIds) throws Exception {
		
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CREATETIME, reqMap);
	}
	
	/**
	 * 更新base_cross中的creat_unit_time字段的值为空
	 * @param baseCrossIds  base_cross_id组成的数组
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTimeToNull(List<String> baseCrossIds) throws Exception {
		
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = baseCrossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(baseCrossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CREATETIME_TO_NULL, reqMap);
	}
	
	
	
	
	
	/**
	 * 更新unit_cross中的creat_unit_time字段的值
	 * @param unitCrossIds
	 * @return
	 * @throws Exception
	 */
	public int updateUnitCrossUnitCreateTime(List<String> unitCrossIds) throws Exception {
		
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = unitCrossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());
		
		logger.info("update unit createTime :" + reqMap);
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_Unit_CROSS_CREATETIME, reqMap);
	}
	
	/**
	 * 根据unitCrossIds删除表unit_cross_train表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoTrainForCorssIds(List<String> unitCrossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = unitCrossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());
		
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS, reqMap);
	}
	
	/**
	 * 根据unitCrossIds删除表unit_cross表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoForCorssIds(List<String> unitCrossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = unitCrossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());
		
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}
	
	/**
	 * 根据crossIds删除表base_cross_train表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoTrainForCorssIds(List<String> crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSS_INFO_TRAIN_FOR_CROSSIDS, reqMap);
	}
	
	
	/**
	 * 根据crossIds删除表base_cross表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoForCorssIds(List<String> crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}
	/**
	 * 更新base_cross中的check_time字段的值
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCorssCheckTime(List<String> crossIds) throws Exception{
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CHECKTIME, reqMap);
		
	}
	
	/**
	 * 更新unit_cross中的check_time字段的值
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateUnitCorssCheckTime(List<String> unitCrossIds) throws Exception{
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = unitCrossIds.size();
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());
		
		logger.info("unitCrossIds==" + bf.toString());
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_UNIT_CROSS_CHECKTIME, reqMap);
		
	}
	

	/**
	 * 根据unitCrossid查询trainNbr
	 * @param unitCrossId
	 * @return 以逗号分隔的trainNbr的字符串
	 */
	 public List<Map<String,String>> getTrainNbrFromUnitCrossId(String unitCrossId){
		 List<Map<String,String>> list = baseDao.selectListBySql(Constants.CROSSDAO_GET_TRAINNBR_FROM_UNIT_CROSS, unitCrossId);
		
		 return list;
	 }
	/**
	 * 生成交路单元
	 * @param baseCrossId
	 * @throws Exception
	 */
	public void completeUnitCrossInfo(String baseCrossId) throws Exception{
		//
		CrossInfo crossInfo = getCrossInfoForCrossid(baseCrossId);
		List<CrossTrainInfo> listCrossTrainInfo = getCrossTrainInfoForCrossid(baseCrossId);
		if(listCrossTrainInfo != null && listCrossTrainInfo.size() > 0){
			//取第一辆列车的始发站
			crossInfo.setStartStn(listCrossTrainInfo.get(0).getStartStn());
		}
		logger.info("listCrossTrainInfo.size==" + listCrossTrainInfo.size());
		List<CrossInfo> list = prepareUnitCrossInfo(crossInfo);
		logger.info("list.size==" + list.size());
		
		///前面的规则没变这个地方直接使用前面的结果，如果前面的规则计算错误这里会有影响 
		List<CrossInfo> result = new ArrayList<CrossInfo>();
		CrossInfo resultCorssInfo = new CrossInfo();
		BeanUtils.copyProperties(resultCorssInfo, crossInfo); 
		resultCorssInfo.setCrossStartDate(crossInfo.getCrossStartDate());
		resultCorssInfo.setCrossEndDate(list.get(list.size() - 1).getCrossEndDate()); 
		
		resultCorssInfo.setUnitCrossId(UUID.randomUUID().toString());
		result.add(resultCorssInfo); 
		  
		if(list !=null && list.size() > 0){
			List<CrossTrainInfo> listCrossTrain = prepareUnitCrossTrainInfo(listCrossTrainInfo, list, resultCorssInfo.getUnitCrossId());
			
			 baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_INFO, result);
			
			if(listCrossTrain !=null && listCrossTrain.size() > 0){
			 baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO, listCrossTrain);
					
			}
			
		}
		
	}
	
	/**
	 * 准备表unit_cross_train中的数据
	 * @param crossTrainInfoList 通过crossid对表base_cross_train查询结果
	 * @param crossInfoList 通过crossid对表base_cross查询并对groupTotalNbr进行分组的结果
	 * @return 需要插入到表unit_cross_train表中的数据
	 */
	private List<CrossTrainInfo> prepareUnitCrossTrainInfo(List<CrossTrainInfo> crossTrainInfoList,List<CrossInfo> crossInfoList, String unitCrossId) throws Exception{
		List<CrossTrainInfo> list = new ArrayList<CrossTrainInfo>();
		if(crossTrainInfoList != null && crossTrainInfoList.size() > 0){
			for(int i = 0; i < crossTrainInfoList.size(); i++){ 
				if(crossInfoList != null && crossInfoList.size() > 0){
					for(int j = 0; j < crossInfoList.size(); j++){
						CrossInfo crossInfo = crossInfoList.get(j);
						CrossTrainInfo crossTrainInfo = crossTrainInfoList.get(i); 
						
						CrossTrainInfo temp = new CrossTrainInfo();
						BeanUtils.copyProperties(temp, crossTrainInfo);
						temp.setUnitCrossId(unitCrossId); 
						temp.setMarshallingName(crossInfo.getMarshallingName());
						temp.setGroupSerialNbr(crossInfo.getGroupSerialNbr()); 
						
						Date date = dateFormat.parse(crossTrainInfo.getRunDate());   
					    Calendar calendar = new GregorianCalendar();
					    calendar.setTime(date); 
					    //以后必须要根据开行规律来生成这个时间
					    //
					    calendar.add(Calendar.DATE, crossInfo.getGroupSerialNbr() - 1);
					   
					    temp.setRunDate(dateFormat.format(calendar.getTime()));
					    
					    date = dateFormat.parse(crossTrainInfo.getEndDate());  
					    calendar = new GregorianCalendar();
					    calendar.setTime(date);
					    calendar.add(Calendar.DATE, crossInfo.getGroupSerialNbr() - 1);
					    
					    temp.setEndDate(dateFormat.format(calendar.getTime()));  
			    	    if(j == 0){ 
				    		temp.setGroupGap(0); 
					    }else{  
				    		int groupGap = daysBetween(dateFormat.parse(getPreCrossInfo(crossInfoList, crossInfo).getCrossStartDate()), dateFormat.parse(crossInfo.getCrossStartDate()));
							temp.setGroupGap(groupGap); 
					    } 
						//设置主键
						temp.setUnitCrossTrainId(UUID.randomUUID().toString());
						list.add(temp);
					}
				}
				
			}
		}
		
		return list;
	}
	
	private CrossInfo getPreCrossInfo(List<CrossInfo> crossInfoList , CrossInfo currCross){ 
		for(CrossInfo crossInfo : crossInfoList){
			if(crossInfo.getGroupSerialNbr() == currCross.getGroupSerialNbr() - 1){
				return crossInfo;
			}
		}
		return null; 
	}
	/**
	 * 准备unit_cross表中数据
	 * @param crossInfo
	 * @return
	 */
	private List<CrossInfo>  prepareUnitCrossInfo(CrossInfo crossInfo) throws Exception{
		List<CrossInfo> list = new LinkedList<CrossInfo>();
		//组数（需几组车底担当）
		int groupTotalNbr = crossInfo.getGroupTotalNbr(); 
		
		//通过crossId查询cross_train信息
	    List<CrossTrainInfo> crossTrainList = getCrossTrainInfoForCrossid( crossInfo.getCrossId());
	    if(crossTrainList != null && crossTrainList.size() > 0){
		    CrossTrainInfo crossTrain = crossTrainList.get(0); 
		    String marshallingNamePre = crossTrain.getStartStn() == null ? crossTrain.getTrainNbr() +"-" : crossTrain.getStartStn().substring(0,1) + "开" + "-" + crossTrain.getTrainNbr() +"-"; 
			//高线标记
			int highlineFlag = 0;
			//高线开行规律
			int highlineRule = 0;
			//普线开行规律,普线开行规律（1:每日;2:隔日）
			int commonlineRule = 1;
			
			if(!StringUtil.strIsNull(crossInfo.getHighlineFlag())){
				highlineFlag = Integer.valueOf(crossTrain.getHighlineFlag());
			}
			if(!StringUtil.strIsNull(crossInfo.getHighlineRule())){
				highlineRule = Integer.valueOf(crossTrain.getHighlineRule());
				highlineRule = highlineRule == 0 ? 1 : highlineRule;
			}
			if(!StringUtil.strIsNull(crossInfo.getCommonlineRule())){
				commonlineRule = Integer.valueOf(crossTrain.getCommonLineRule());
				commonlineRule  = commonlineRule == 0 ? 1 : commonlineRule;
			}  
			 
			CrossInfo preUnitCross = null;
			String crossStartDate = crossInfo.getCrossStartDate();
			String crossEndDate = crossInfo.getCrossEndDate();
			if(groupTotalNbr > 0 ){
				for(int i = 0; i < groupTotalNbr; i++){
					CrossInfo tempInfo = new CrossInfo(); 
					if(preUnitCross != null){
						/**计算下一个交路单元的开始日期，在上一个交路单元的终到日期的基础上再加上间隔天数**/
						//上一个交路单元的终到日期,格式为yyyyMMdd
						String preCrossStartDate = preUnitCross.getCrossStartDate(); 
						String preCrossEndDate =  preUnitCross.getCrossEndDate();  
						//高线标记（1:高线；0:普线；2:混合）
						if(highlineFlag == 0 || highlineFlag == 2){
							crossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossStartDate), -commonlineRule); 
							crossEndDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -commonlineRule); 
							 
						}else {
							//高线开行规律（1:平日;2:周末;3:高峰）
							if(highlineRule == 1){
								crossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossStartDate), -highlineRule);	
								crossEndDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -highlineRule);
							}else if(highlineRule == 2 || highlineRule ==3 ){
								//TODO 暂时不处理   默认向后推1天
								crossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossStartDate), -1);	
								crossEndDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -1);
							}else{
								crossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossStartDate), -1);	
								crossEndDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -1);
							} 
						} 
					}
					tempInfo.setCrossStartDate(crossStartDate.replaceAll("-", ""));
					tempInfo.setCrossEndDate(crossEndDate.replaceAll("-", ""));
					tempInfo.setGroupSerialNbr((i + 1));
					tempInfo.setMarshallingName(marshallingNamePre + (i + 1)); 
					list.add(tempInfo);
					preUnitCross = tempInfo;
				}
			} 
	    }
		
		return list;
	}
	/**
	 * 查询cross信息
	 * @param reqMap
	 * @return
	 */
	public List<CrossInfo>  getCrossInfo(Map<String,Object> reqMap){
		List<CrossInfo>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_INFO, reqMap);
		return list;
	}
	
	/**
	 * 查询 unitcross信息总条数
	 */
	public long getCrossInfoCount(Map<String,Object> reqMap){
		List<Map<String,Object>>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_INFO_COUNT, reqMap);
		long count = 0;
		if(list != null){
			//只有一条数据
			Map<String,Object> map = list.get(0);
			count =(( BigDecimal)map.get("COUNT")).longValue();
		}
		return count;
	}
	
	/**
	 * 查询unitcross信息
	 * @param reqMap
	 * @return
	 */
	public List<SubCrossInfo>  getUnitCrossInfo(Map<String,Object> reqMap){
		List<SubCrossInfo>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_INFO, reqMap);
		return list;
	}
	
	/**
	 * 通过crossId在表unitCorss表中查询unitCross基本信息
	 * @param crossId
	 * @return
	 */
	public List<CrossInfo> getUnitCrossInfosForCrossId(String crossId){
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_INFO_FOR_CROSSID, crossId);
	}
	/**
	 * 查询 unitcross信息总条数
	 */
	public long getUnitCrossInfoCount(Map<String,Object> reqMap){
		List<Map<String,Object>>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_INFO_COUNT, reqMap);
		long count = 0;
		if(list != null){
			//只有一条数据
			Map<String,Object> map = list.get(0);
			count =(( BigDecimal)map.get("COUNT")).longValue();
		}
		return count;
	}
	
	/**
	 * 通过crossid查询crossinfo信息
	 * @param crossId
	 * @return
	 */
	public CrossInfo getCrossInfoForCrossid(String crossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("crossId", crossId);
		return (CrossInfo)baseDao.selectOneBySql(Constants.CROSSDAO_GET_CROSS_INFO_FOR_PARAM, paramMap);
	    
	}
	
	/**
	 * 通过crossid查询crossinfo信息
	 * @param crossId
	 * @return
	 */
	public List<CrossInfo> getCrossInfoByParam(Map<String, Object> paramMap){ 
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_INFO_FOR_PARAM, paramMap); 
	}
	
	/**
	 * 通过crossid查询crosstrainInfo信息
	 * @param crossId
	 * @return
	 */
	public List<CrossTrainInfo> getCrossTrainInfoForCrossid(String crossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("crossId", crossId);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_TRAIN_INFO_FOR_CROSSID, paramMap);
	}
	
	
	/**
	 * 通过unitCrossId查询unit_cross_trainInfo信息
	 * @param crossId
	 * @return
	 */
	public List<CrossTrainInfo> getUnitCrossTrainInfoForUnitCrossId(String unitCrossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("unitCrossId", unitCrossId);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSSTRAIN_INFO_FOR_UNIT_CROSSID, paramMap);
	}
	
	
	
	
	/**
	 * 通过UNITcrossid查询UNITcrossinfo信息
	 * @param unitCrossId
	 * @return
	 */
	public CrossInfo getUnitCrossInfoForUnitCrossid(String unitCrossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("unitCrossId", unitCrossId);
		return (CrossInfo)baseDao.selectOneBySql(Constants.CROSSDAO_GET_UNIT_CROSS_INFO_FOR_UNIT_CROSSID, paramMap);
	}
	
	/**
	 * 通过unitcrossid查询crosstrainInfo信息
	 * @param unitCrossId
	 * @return
	 */
	public List<UnitCrossTrainInfo> getUnitCrossTrainInfoForUnitCrossid(String unitCrossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("unitCrossId", unitCrossId);
		List<UnitCrossTrainInfo> list = baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_TRAIN_INFO_FOR_UNIT_CROSSID, paramMap);
		if(list != null && list.size() > 0 ){
			for(UnitCrossTrainInfo crossInfo : list ){
				List<UnitCrossTrainSubInfo> trainList = crossInfo.getTrainInfoList();
				if(trainList != null && trainList.size() > 0 ){
					for(UnitCrossTrainSubInfo train :trainList){
						String runDate = train.getRunDate();
						String endDate = train.getEndDate();
						String startStn = train.getStartStn();
						String endStn = train.getEndStn();
						
						List<UnitCrossTrainSubInfoTime> tempList = new ArrayList<UnitCrossTrainSubInfoTime>();
						List<UnitCrossTrainSubInfoTime> stationTimeList = train.getStationTimeList();	
						
						if(stationTimeList != null && stationTimeList.size() > 0 ){
							for(int i = 0;i<stationTimeList.size();i++){
								UnitCrossTrainSubInfoTime trainTime = stationTimeList.get(i);
								String arrTime = trainTime.getArrTime();
								String dptTime = trainTime.getDptTime();
								String stnName = trainTime.getStnName();
								if(startStn.equals(stnName)&& runDate.equals(dptTime)){
									
									tempList.add(trainTime);
								}else if(endStn.equals(stnName) && endDate.equals(arrTime)){
									tempList.add(trainTime);
								}
								
							}
						}
						train.setStationTimeList(tempList);
						
					}
				}
			}
		}
		return  list;
	}
	
	/**
	 * 通过crossid查询crosstrainInfo信息
	 * @param unitCrossId
	 * @return
	 */
	public List<BaseCrossTrainInfo> getCrossTrainInfoForCrossId(String crossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("baseCrossId", crossId);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_TRAININFO_FOR_CROSSID, paramMap);
	}
	
	/**
	 * 通过crossid查询crosstrainInfo信息
	 * @param unitCrossId
	 * @param bureauShortName 所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossId(String planCrossId,String bureauShortName){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("planCrossId", planCrossId);
		//如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z" : bureauShortName);
		 
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID, paramMap);
	}
	
	/**
	 * 通过planCrossId查询起始站
	 * @param planCrossId
	 * @return
	 */
	public List<Map<String,Object>> getStationListForPlanCrossId(String planCrossId,String stnBureau){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("planCrossId",planCrossId );
		reqMap.put("stnBureau",stnBureau==null ? "Z" : stnBureau);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_STATIONLIST_FOR_PLANCROSSID, reqMap);
	}
	
	
	/**
	 * 根据baseCrossid查询crossName等信息
	 * crossStartDate,crossEndDate
	 * @param baseCrossId
	 * @return
	 */
	public Map<String,String> getCrossNameWithBaseCrossId(String baseCrossId){
		Map<String,String> returnMap = null;
		List<Map<String,String>> list =baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSSNAME_WITH_BASE_CROSSID, baseCrossId);
	   if(list !=null && list.size() > 0){
		   //只有一条数据
		   returnMap = list.get(0);
	   }
	   return returnMap;
	}
	
	/**
	 * 通过baseCorssId查询train_nbr,train_sort等信息
	 * @param baseCrossId
	 * @return
	 */
	public List<Map<String,Object>> getTrainNbrWithBaseCrossId(String baseCrossId){
		return baseDao.selectListBySql(Constants.CROSSDAO_GET_TRAINNBR_WITH_BASE_CROSSID, baseCrossId);
	}

	/**
	 * 通过baseCrossId分别查询数据并组合
	 * @param baseCrossId
	 * @return
	 */
	public BaseCrossDto getBaseCrossDtoWithCrossId(String baseCrossId){
		BaseCrossDto baseCrossDto = new BaseCrossDto();
		Map<String,String> crossInfoMap = getCrossNameWithBaseCrossId(baseCrossId);
		//cross_name,cross_start_date,cross_end_date
		baseCrossDto.setCrossName(crossInfoMap.get("CROSS_NAME"));
		baseCrossDto.setCrossStartDate(crossInfoMap.get("CROSS_START_DATE"));
		baseCrossDto.setCrossEndDate(crossInfoMap.get("CROSS_END_DATE"));
		List<BaseCrossTrainDto> subList = new ArrayList<BaseCrossTrainDto>();
		//查询trainNbr等信息 BASE_TRAIN_ID ,train_nbr,train_sort,day_gap
		List<Map<String,Object>> trainInfoList = getTrainNbrWithBaseCrossId(baseCrossId);
		if(trainInfoList != null && trainInfoList.size() > 0){
			for(Map<String,Object> map :trainInfoList){
				BaseCrossTrainDto dto = new BaseCrossTrainDto();
				String trainId = StringUtil.objToStr(map.get("BASE_TRAIN_ID"));
				if(!"".equals(trainId)&&trainId != null && !"null".equals(trainId)){
					dto.setBaseTrainId(trainId);
					dto.setDayGap(((BigDecimal)map.get("DAY_GAP")).intValue());
					dto.setTrainSort(((BigDecimal)map.get("TRAIN_SORT")).intValue());
					dto.setTrainNbr(StringUtil.objToStr(map.get("TRAIN_NBR")));	
					subList.add(dto);
				}
				
				
			}
			baseCrossDto.setListBaseCrossTrain(subList);
		}
		return baseCrossDto;
	}
	
	
	
	public void actionExcel(InputStream inputStream, String chartId, String startDay, String chartName, String addFlag) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		pm.put("crossIdForExcel", "");
		pm.put("crossName", "");
		pm.put("crossSpareName", "");
		pm.put("alterNateDate", "");
		pm.put("alterNateTranNbr", "");
		pm.put("spareFlag", ""); 
		pm.put("cutOld", ""); 
		pm.put("groupTotalNbr", "");
		pm.put("pairNbr", ""); 
		pm.put("highlineFlag", ""); 
		pm.put("highlineRule", "");
		pm.put("commonlineRule", "");
		pm.put("appointWeek", "");  
		pm.put("appointDay", ""); 
		pm.put("appointPeriod", ""); 
		pm.put("crossSection", ""); 
		pm.put("throughline", "");  
		pm.put("tokenVehBureau", "");  
		pm.put("tokenVehDept", ""); 
		pm.put("tokenVehDepot", ""); 
		pm.put("tokenPsgBureau", "");  
		pm.put("tokenPsgDept", "");  
		pm.put("locoType", "");
		pm.put("crhType", ""); 
		pm.put("elecSupply", "");
		pm.put("dejCollect", "");
		pm.put("airCondition", "");
		pm.put("note", "");
		
		
		Map<String,  Map<String, String>> valuesMap = new HashMap<String, Map<String, String>>();
		
		//可以从数据库中获取
		Map<String, String> tokenPsgDeptValuesMap = commonService.getStationJCMapping();
		 
		//System.err.println("tokenPsgDeptValuesMap==" + tokenPsgDeptValuesMap);
		valuesMap.put("tokenPsgBureau", tokenPsgDeptValuesMap); 
		valuesMap.put("tokenVehBureau", tokenPsgDeptValuesMap);  
		
		ExecutorService service = Executors.newFixedThreadPool(10);
		CompletionService<String> completion = new ExecutorCompletionService<String>(service);
		 
		// TODO Auto-generated method stub
		try{
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			int allNum = 0;
			int num = workbook.getNumberOfSheets();		
			//List<CrossInfo> alllist = new ArrayList<CrossInfo>();
			//全部清空再导入
			if("1".equals(addFlag)){
				clearCrossInfoByChartId(chartId);
			}
			for(int i = 0; i < num; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<CrossInfo> test = new ExcelUtil<CrossInfo>(pm, sheet, CrossInfo.class);
				test.setValueMapping(valuesMap);
				List<CrossInfo> list = test.getEntities(-1); 
				
				for(int j = 0; j < list.size(); j++){ 
					 
					CrossInfo crossInfo = list.get(j);
					crossInfo.setChartId(chartId);
					crossInfo.setChartName(chartName); 
					
					 if(StringUtils.isEmpty(crossInfo.getAlterNateDate())){
						 crossInfo.setAlterNateDate(startDay);
					 }
					completion.submit(new CrossCompletionService(list.get(j), baseDao, addFlag));
				} 
				allNum += list.size();
			} 
//			ArrayList<CrossTrainInfo> crossTrains = new ArrayList<CrossTrainInfo>(); 
			for(int i = 0; i < allNum; i++){
				try {
					completion.take().get();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			service.shutdown();  
		}catch(FileNotFoundException e){
			//e.printStackTrace();
		}catch(IOException e){
			//e.printStackTrace();
		}
		
	} 
	 
	
	private void clearCrossInfoByChartId(String chartId) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("chartId", chartId); 
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
		if(user.getBureau() != null){
			map.put("bureaus", user.getBureau());
		} 
		this.baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNITCROSSTRAIN_BY_CHARID, map);
		this.baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNITCROSS_INFO_BY_CHARID, map);
		this.baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSSTRAIN_BY_CHARID, map); 
		this.baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSSINFO_BY_CHARID, map); 
	} 

	
	
	
	 private static int daysBetween(Date date1,Date date2){  

	        Calendar cal = Calendar.getInstance();  

	        cal.setTime(date1);  

	        long time1 = cal.getTimeInMillis();               

	        cal.setTime(date2);  

	        long time2 = cal.getTimeInMillis();       

	        long between_days=(time2-time1)/(1000*3600*24);   

	       return Integer.parseInt(String.valueOf(between_days));         

	    }  
	




	/**
	 * 用于并行处理交路
	 * @author Administrator
	 *
	 */
	class CrossCompletionService implements Callable<String> { 
		
//		/**
//		 * 并行处理列车基本信息
//		 * @author Administrator
//		 *  800因为之前是调用接口每次大概处理时间为1秒，线程回收没有问题，但是改为数据库调用了以后，处理速度太快，导致线程堆积数量达到最大值就会抛出异常
//		 */
//		class GetTrainInfoCompletionService implements Callable<CrossTrainInfo> {
//			
//			private CrossTrainInfo train;
//			
//			public GetTrainInfoCompletionService(CrossTrainInfo train ){
//			   this.train = train;
//			}
//			
//			public CrossTrainInfo call() throws Exception {
//				trainInfoFromPain(this.train);
//			    return this.train;
//			}
//			
//			private void trainInfoFromPain(CrossTrainInfo train){
////				String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:6:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}}},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
//				//http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=G11 
//				try {
//					//Client client = Client.create();
//					
//					//client.setConnectTimeout(60*1000);
//					String trainNbr = train.getTrainNbr();
//					String stn = null; 
//					
//					String regex = "^(.+?)[\\(（](.+?)[\\)）]";
//				    Pattern pattern = Pattern.compile(regex);
//				    Matcher matcher = pattern.matcher(trainNbr); 
//			       
//			        if(matcher.find()) { 
//			        	trainNbr = matcher.group(1);
//			        	stn =  matcher.group(2); 
//			        } 
//			        Map<String, String> map = new HashMap<String, String>();
//			        map.put("trainNbr", trainNbr);
//			        if(stn != null){
//			        	String[] stns = stn.split("[:：]");
//			        	if(stns.length == 1){
//			        		map.put("startStn", stns[0]); 
//			        	}else if(stns.length == 2){
//			        		map.put("startStn", stns[0]);
//			        		map.put("endStn", stns[1]);
//			        	} 
//			        }
//			        List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map); 
//			        if(baseTrains != null && baseTrains.size() > 0){
//			        	BaseTrainInfo currTrain = baseTrains.get(0);
//			        	train.setBaseTrainId(currTrain.getBaseTrainId());
//			        	train.setSourceTargetTime(currTrain.getStartTime()); 
//			        	train.setStartStn(currTrain.getStartStn()); 
//			        	
//			        	train.setStartBureau(commonService.getStationPy(currTrain.getStartBureauShortName()));
//			        	train.setEndStn(currTrain.getEndStn());
//			        	train.setRunDay(currTrain.getRunDays());
//			        	train.setTargetTime(currTrain.getEndTime());
//			        	train.setRouteBureauShortNames(currTrain.getRouteBureauShortNames());
//			        	train.setEndBureau(commonService.getStationPy(currTrain.getEndBureanShortName())); 
//			        }else{
//			        	logger.warn(map + "没有可匹配的对象");
//			        }
//				}catch (Exception e) {
//					// TODO: handle exception
//					//e.printStackTrace();
//					logger.error("获取列车信息失败:", e);
//				}   
//		 
//			} 
//		}
			
		private CrossInfo cross;
		
		private BaseDao baseDao;
		
		private String addFlag;
		
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		public CrossCompletionService(CrossInfo cross){
		   this.cross = cross;
		}
		
		public CrossCompletionService(CrossInfo cross, BaseDao baseDao, String addFlag){
		   this.cross = cross;
		   this.baseDao = baseDao;
		   this.addFlag = addFlag;
		}
		
		private String[] getCrossInfoByCrossName(String crossName, String charId){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("crossName", crossName);
			map.put("charId", charId);
			List<CrossInfo> list = getCrossInfoByParam(map);
			String[] ids = new String[list.size()];
			for(int i = 0; i < list.size(); i++){ 
				ids[i] = list.get(i).getCrossId();
			} 
			return ids;
		} 
		
		private void clearOldCrossInfo(){ 
			 String[] ids = getCrossInfoByCrossName(this.cross.getCrossName(), this.cross.getChartId());
			 
			 try {
				if(ids.length > 0){
					List<String> idsList = new ArrayList<String>();
					for(int i=0;i<ids.length;i++){
						idsList.add(ids[i]);
					}
					deleteUnitCrossInfoTrainForCorssIds(idsList);
					deleteUnitCrossInfoForCorssIds(idsList);
					deleteCrossInfoTrainForCorssIds(idsList);
					deleteCrossInfoForCorssIds(idsList); 
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				 logger.error("删除已有记录出错 ", e);
			} 
		}
		
		private boolean hasRole(String bureau){
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
			if(user.getBureau() == null){
				return true;
			}
			List<String> permissionList = user.getPermissionList(); 
			String[] bs = bureau.split("[、,]");
			for(String p : permissionList){
				for(String b : bs){
					if(p.endsWith("." + b)){
						return true;
					}
				}
			} 
			return false;
		}
		
		public String call() throws Exception {    
			
			if(!hasRole(this.cross.getTokenVehBureau())){
				return null;
			} 
			
			if("0".equals(addFlag)){
				clearOldCrossInfo();
			} 
			//普线和混合
			if("0".equals(this.cross.getHighlineFlag()) || "2".equals(this.cross.getHighlineFlag())){
				if(this.cross.getCommonlineRule() == null){
					this.cross.setCommonlineRule("1");
				}
			}else if("1".equals(this.cross.getHighlineFlag())){
				if(this.cross.getHighlineRule() == null){
					this.cross.setHighlineRule("1");
				}
			}
			
			LinkedList<CrossTrainInfo>  crossTrains = this.createTrainsForCross(this.cross); 
			//保存交路信息
			ArrayList<CrossInfo> crossList = new ArrayList<CrossInfo>();
			crossList.add(this.cross);
			 
			this.baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_INFO, crossList);  
			 
			logger.debug(this.cross.getCrossName() + " ===crossTrains===" + crossTrains.size());
        	//保存列车
			if(crossTrains != null && crossTrains.size() > 0 ){
				 this.baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
			}  
			
		   return "success";
		}
		
		private void trainInfoFromPain(CrossTrainInfo train){
//			String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:6:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}}},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
			//http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=G11 
			try {
				//Client client = Client.create();
				
				//client.setConnectTimeout(60*1000);
				String trainNbr = train.getTrainNbr();
				String stn = null; 
				
				String regex = "^(.+?)[\\(（](.+?)[\\)）]";
			    Pattern pattern = Pattern.compile(regex);
			    Matcher matcher = pattern.matcher(trainNbr); 
		       
		        if(matcher.find()) { 
		        	trainNbr = matcher.group(1);
		        	stn =  matcher.group(2); 
		        } 
		        Map<String, String> map = new HashMap<String, String>();
		        map.put("trainNbr", trainNbr);
		        if(stn != null){
		        	String[] stns = stn.split("[:：]");
		        	if(stns.length == 1){
		        		map.put("startStn", stns[0]); 
		        	}else if(stns.length == 2){
		        		map.put("startStn", stns[0]);
		        		map.put("endStn", stns[1]);
		        	} 
		        }
		        List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map); 
		        if(baseTrains != null && baseTrains.size() > 0){
		        	BaseTrainInfo currTrain = baseTrains.get(0);
		        	train.setBaseTrainId(currTrain.getBaseTrainId());
		        	train.setSourceTargetTime(currTrain.getStartTime()); 
		        	train.setStartStn(currTrain.getStartStn()); 
		        	
		        	train.setStartBureau(commonService.getStationPy(currTrain.getStartBureauShortName()));
		        	train.setEndStn(currTrain.getEndStn());
		        	train.setRunDay(currTrain.getRunDays());
		        	train.setTargetTime(currTrain.getEndTime());
		        	train.setRouteBureauShortNames(currTrain.getRouteBureauShortNames());
		        	train.setEndBureau(commonService.getStationPy(currTrain.getEndBureanShortName())); 
		        }else{
		        	logger.warn(map + "没有可匹配的对象"); 
		        }
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("获取列车信息失败:", e);
			}   
	 
		} 
		
		
		private void setDayGapForTrains(LinkedList<CrossTrainInfo> crossTrains){
			for(int i = 0; i < crossTrains.size(); i++){ 
				setDayGap(crossTrains.get(i), crossTrains.get(i - 1 < 0 ? crossTrains.size() - 1 : i - 1)); 
			}
		} 
		 
		private void setEndDateForCross(LinkedList<CrossTrainInfo> crossTrains, CrossInfo cross){
			 int dayGapForCross = 0;
			 String crossStartDate = cross.getCrossStartDate();
			 try{
				 //设置交路的终到日期 
				 for(int i = 0; i <  crossTrains.size(); i++){
					 CrossTrainInfo crosstrain = crossTrains.get(i);
					 if(i == 0){
						 crosstrain.setRunDate(crossStartDate);
						 Date date = this.dateFormat.parse(crossStartDate);   
						 Calendar calendar = GregorianCalendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DATE, crosstrain.getRunDay());
						 crosstrain.setEndDate(this.dateFormat.format(calendar.getTime()));
						 dayGapForCross += crosstrain.getRunDay();
					 }else{
						 //第二个车+前面的车的总天数 + daygap
						 Date date = this.dateFormat.parse(crossStartDate);   
						 Calendar calendar = GregorianCalendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DATE, (dayGapForCross + crosstrain.getDayGap())); 
						 
						 crosstrain.setRunDate(this.dateFormat.format(calendar.getTime())); 
						 
						 dayGapForCross += crosstrain.getRunDay() + crosstrain.getDayGap(); 
						 
						 //设置结束时间
						 calendar.add(Calendar.DATE, crosstrain.getRunDay());
						 crosstrain.setEndDate(this.dateFormat.format(calendar.getTime())); 
						 
					 }  
				 }  
				 Date date = this.dateFormat.parse(cross.getCrossStartDate());   
				 Calendar calendar = new GregorianCalendar();
				 calendar.setTime(date);
				 calendar.add(Calendar.DATE, dayGapForCross);
				 
				 cross.setCrossEndDate(this.dateFormat.format(calendar.getTime()));
				 
				 
			 }catch(Exception e){
				 logger.error("设置结束时间出错 ", e);
			 }
		}
		
		private void setStartAndEndTime(LinkedList<CrossTrainInfo> crossTrains, CrossInfo cross){
			//有多线程问题  
			 int dayGapForCross = 0;
			 String crossStartDate = cross.getCrossStartDate();
			 try{
				 //设置交路的终到日期 
				 
				 for(int i = 0; i <  crossTrains.size(); i++){
					 CrossTrainInfo crosstrain = crossTrains.get(i);
					 if(i == 0){
						 crosstrain.setRunDate(cross.getCrossStartDate());
						 Date date = this.dateFormat.parse(crossStartDate);   
						 Calendar calendar = GregorianCalendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DATE, crosstrain.getRunDay());
						 crosstrain.setEndDate(this.dateFormat.format(calendar.getTime()));
						 
						 dayGapForCross += crosstrain.getRunDay();
					 }else{
						 //第二个车+前面的车的总天数 + daygap
						 Date date = this.dateFormat.parse(crossStartDate);   
						 Calendar calendar = GregorianCalendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DATE, dayGapForCross + crosstrain.getDayGap()); 
						 
						 crosstrain.setRunDate(this.dateFormat.format(calendar.getTime())); 
						 
						 dayGapForCross += crosstrain.getRunDay() + crosstrain.getDayGap(); 
						 
						 //设置结束时间
						 calendar.add(Calendar.DATE, crosstrain.getRunDay());
						 crosstrain.setEndDate(this.dateFormat.format(calendar.getTime())); 
						 
					 }  
				 }  
			 }catch(Exception e){
				 logger.error("设置结束时间出错 ", e);
			 }
		} 
		 
		/**
		 * 计算列车间隔时间
		 * @param train1 需要设置间隔时间的列车
		 * @param train2 前置列车
		 */
		private void setDayGap(CrossTrainInfo train1, CrossTrainInfo train2){ 
			try {  
				Date sourceTime = format.parse("1977-01-01 " + train1.getSourceTargetTime());
				Date targetTime = train2 != null ?  format.parse("1977-01-01 " + train2.getTargetTime()) :  format.parse("1977-01-01 " + train1.getTargetTime());
				
				if(sourceTime.compareTo(targetTime) < 0){
					train1.setDayGap(1);
				}else{
					train1.setDayGap(0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(train2.getTrainNbr() + "--getTargetTime-" + train2.getTargetTime());
				logger.error(train1.getTrainNbr() + "--getSourceTargetTime-" + train1.getSourceTargetTime());
				logger.error(e.getMessage(), e);
			};
			 
		}
		
		private LinkedList<CrossTrainInfo> createTrainsForCross(CrossInfo cross){
			String crossName = cross.getCrossName();
			String[] crossSpareNames =StringUtils.isEmpty( cross.getCrossSpareName()) ? null : cross.getCrossSpareName().split("-");
			String[] alertNateTrains = StringUtils.isEmpty(cross.getAlterNateTranNbr()) ? null : cross.getAlterNateTranNbr().split("-");
			String[] alertNateDate = StringUtils.isEmpty(cross.getAlterNateDate()) ? null : cross.getAlterNateDate().split("-");
			String[] spareFlag = StringUtils.isEmpty(cross.getSpareFlag())? null : cross.getSpareFlag().split("-");
			String[] highlineFlag = StringUtils.isEmpty(cross.getHighlineFlag())? null : cross.getHighlineFlag().split("-");
			String[] commonlineRule = StringUtils.isEmpty(cross.getCommonlineRule())? null : cross.getCommonlineRule().split("-");
			String[] highlineRule = StringUtils.isEmpty(cross.getHighlineRule())? null : cross.getHighlineRule().split("-");
			
			String[] appointWeek = StringUtils.isEmpty(cross.getAppointWeek())? null : cross.getAppointWeek().split("-");
			String[] appointDay = StringUtils.isEmpty(cross.getAppointDay())? null : cross.getAppointDay().split("-");
			
			String[] trains = crossName.split("-");
			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			for(int i = 0; i < trains.length; i++){
				train = new CrossTrainInfo();
				train.setTrainSort(i + 1);
				train.setCrossId(cross.getCrossId());
				train.setTrainNbr(trains[i]); 
				try{
					//
					if(alertNateTrains != null){
						 train.setAlertNateTrainNbr(alertNateTrains[i]);
					}
					//
					if(alertNateDate != null ){
						if(alertNateDate.length == 1){
							train.setAlertNateTime(alertNateDate[0] + " 02:00:00"); 
						}else{ 
							train.setAlertNateTime(alertNateDate[i] + " 02:00:00");
						}  
					}
					//
					if(spareFlag != null){
						if(spareFlag.length == 1){
							train.setSpareFlag(Integer.parseInt(spareFlag[0]));
						}else{
							train.setSpareFlag(Integer.parseInt(spareFlag[i]));
						}
					}  
					
					if(highlineFlag != null ){
						if(highlineFlag.length == 1){ 
							train.setHighlineFlag(Integer.parseInt(highlineFlag[0]));
						}else{ 
							train.setHighlineFlag(Integer.parseInt(highlineFlag[i])); 
						}  
					}
					
					if(commonlineRule != null ){
						if(commonlineRule.length == 1){ 
							train.setCommonLineRule(Integer.parseInt(commonlineRule[0]));
						}else{ 
							train.setCommonLineRule(Integer.parseInt(commonlineRule[i])); 
						}  
					}
					
					if(highlineRule != null ){
						if(highlineRule.length == 1){ 
							train.setHighlineRule(Integer.parseInt(highlineRule[0]));
						}else{ 
							train.setHighlineRule(Integer.parseInt(highlineRule[i])); 
						}  
					}
					
					
					if(appointDay != null ){
						if(appointDay.length == 1){ 
							train.setAppointDay(appointDay[0]);
						}else{ 
							train.setAppointDay(appointDay[i]); 
						}  
					} 
					
					if(appointWeek != null ){
						if(appointWeek.length == 1){ 
							train.setAppointWeek(appointWeek[0]);
						}else{ 
							train.setAppointWeek(appointWeek[i]); 
						}  
					}
					
				}catch(Exception e){
					logger.error("创建列车信息出错:" , e);
				}
				
				crossTrains.add(train);
			} 
			//获取列车时刻表和其实和终到站
		   String routeBureauShortNames = "";
		   for(int i=0; i < crossTrains.size(); i++){
			   CrossTrainInfo crossTrain = crossTrains.get(i);
			   trainInfoFromPain(crossTrain); 
				//设置交路的开始日期和结束日期
				if(cross.getCrossName().startsWith(crossTrain.getTrainNbr())){
					cross.setStartBureau(crossTrain.getStartBureau());  
					cross.setCrossStartDate(crossTrain.getAlertNateTime().substring(0, 8)); 
				} 
				routeBureauShortNames += crossTrain.getRouteBureauShortNames() != null ? crossTrain.getRouteBureauShortNames() : ""; 
		   } 
		  
		  if(!"".equals(routeBureauShortNames)){ 
			  String rbsPY = "";
			  String py = "";
			  for(int i = 0; i < routeBureauShortNames.length(); i++){
				 py = commonService.getStationPy(routeBureauShortNames.substring(i, i + 1));
				 if((py != null && rbsPY.indexOf(py) > -1) || py == null){
					 continue;
				 }
				 rbsPY += py;
			  }
			  cross.setRelevantBureau(rbsPY);
		  } 
		  
		 //设置列车的间隔时间
		 setDayGapForTrains(crossTrains);
		 //设置列车的结束日期
		 setEndDateForCross(crossTrains, cross); 
			
		LinkedList<CrossTrainInfo> crossSpareTrains = new LinkedList<CrossTrainInfo>(); 
		if(crossSpareNames != null){
			for(int i = 0; i < crossSpareNames.length; i++){
				try{
					train = new CrossTrainInfo();
					train.setCrossId(cross.getCrossId());
					train.setTrainSort(i + 1);
					train.setTrainNbr(crossSpareNames[i]); 
					train.setSpareApplyFlage(1);  
					train.setSpareFlag(2); 
					//
					if(alertNateDate != null ){
						if(alertNateDate.length == 1){
							train.setAlertNateTime(alertNateDate[0] + " 02:00:00"); 
						}else{ 
							train.setAlertNateTime(alertNateDate[i] + " 02:00:00");
						}  
					} 

					if(highlineFlag != null ){
						if(highlineFlag.length == 1){ 
							train.setHighlineFlag(Integer.parseInt(highlineFlag[0]));
						}else{ 
							train.setHighlineFlag(Integer.parseInt(highlineFlag[i])); 
						}  
					} 
					 
				}catch(Exception e){
					logger.error("创建列车信息出错:" , e); 
				}
				crossSpareTrains.add(train);
			} 
			
		  
		   for(int i=0; i < crossSpareTrains.size(); i++){
			   CrossTrainInfo crossTrain = crossSpareTrains.get(i);
			   trainInfoFromPain(crossTrain);
			   
				//设置交路的开始日期和结束日期
				if(cross.getCrossName().startsWith(crossTrain.getTrainNbr())){
					cross.setStartBureau(crossTrain.getStartBureau());
//					cross.setCrossStartDate(crossTrain.getSourceTargetTime());
					//cross.setCrossStartDate(crossTrain.getAlertNateTime().substring(0, 8)); 
				}   
//				routeBureauShortNames += crossTrain.getRouteBureauShortNames() != null ? crossTrain.getRouteBureauShortNames() : ""; 
		   } 
			 
		   setDayGapForTrains(crossSpareTrains); 
		    
		   //设置车次的开始和结束日期
		   setStartAndEndTime(crossSpareTrains, cross); 
			
		   crossTrains.addAll(crossSpareTrains);
		}
		
		
			 
//			String trains = crossName.split("-");
		logger.debug(this.cross.getCrossName() + "==crossTrains=" + crossTrains.size());
		 
		return crossTrains; 
	}
		
		
	
	} 
 
}
