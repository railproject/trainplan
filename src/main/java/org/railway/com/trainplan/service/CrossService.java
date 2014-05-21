package org.railway.com.trainplan.service;

<<<<<<< HEAD
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

=======
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
>>>>>>> 7dcade55941a1f96527c15e8c3aa781075700ffd
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.ExcelUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.BaseCrossDto;
import org.railway.com.trainplan.service.dto.BaseCrossTrainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
 
@Service
public class CrossService{
	private static final Logger logger = Logger.getLogger(CommonService.class);
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 

	@Autowired
	private CommonService commonService;
	
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
	 
		System.out.println(dateFormat.format(calendar.getTime()));
//		a.actionExcel(is); 
//		System.out.println("G11(".substring(0,"G11(".indexOf('(')));
	}

	/**
	 * 更新base_cross中的creat_unit_time字段的值
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTime(String[] crossIds) throws Exception {
		
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CREATETIME, reqMap);
	}
	
	/**
	 * 根据crossIds删除表unit_cross_train表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoTrainForCorssIds(String[] crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS, reqMap);
	}
	
	/**
	 * 根据crossIds删除表unit_cross表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoForCorssIds(String[] crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}
	
	/**
	 * 根据crossIds删除表base_cross_train表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoTrainForCorssIds(String[] crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSS_INFO_TRAIN_FOR_CROSSIDS, reqMap);
	}
	
	
	/**
	 * 根据crossIds删除表base_cross表中数据
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoForCorssIds(String[] crossIds) throws Exception {
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}
	/**
	 * 更新base_cross中的check_time字段的值
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCorssCheckTime(String[] crossIds) throws Exception{
		//组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String,Object> reqMap = new HashMap<String,Object>();
		int size = crossIds.length;
		for(int i = 0 ;i<size;i++){
			bf.append("'").append(crossIds[i]).append("'");
			if(i != size - 1){
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		
		System.err.println("bf.toString()==" +  bf.toString());
		
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CHECKTIME, reqMap);
		
	}
	/**
	 * 根据unitCrossid查询trainNbr,以逗号分隔组成一个字符串
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
		System.err.println("listCrossTrainInfo.size==" + listCrossTrainInfo.size());
		List<CrossInfo> list = prepareUnitCrossInfo(crossInfo);
		System.err.println("list.size==" + list.size());
		if(list !=null && list.size() > 0){
			List<CrossTrainInfo> listCrossTrain = prepareUnitCrossTrainInfo(listCrossTrainInfo,list);
			System.err.println("listCrossTrain.size===" + listCrossTrain.size());
			int count = baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_INFO, list);
			System.err.println("count=====" + count);
			if(listCrossTrain !=null && listCrossTrain.size() > 0){
				int trainCount = baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO, listCrossTrain);
				System.err.println("trainCount=====" + trainCount);		
			}
			
		}
		
	}
	
	/**
	 * 准备表unit_cross_train中的数据
	 * @param crossTrainInfoList 通过crossid对表base_cross_train查询结果
	 * @param crossInfoList 通过crossid对表base_cross查询并对groupTotalNbr进行分组的结果
	 * @return 需要插入到表unit_cross_train表中的数据
	 */
	private List<CrossTrainInfo> prepareUnitCrossTrainInfo(List<CrossTrainInfo> crossTrainInfoList,List<CrossInfo> crossInfoList) throws Exception{
		List<CrossTrainInfo> list = new ArrayList<CrossTrainInfo>();
		if(crossTrainInfoList != null && crossTrainInfoList.size() > 0){
			for(CrossTrainInfo crossTrainInfo : crossTrainInfoList){
				
				if(crossInfoList != null && crossInfoList.size() > 0){
					for(CrossInfo crossInfo : crossInfoList){
						CrossTrainInfo temp = new CrossTrainInfo();
						BeanUtils.copyProperties(temp, crossTrainInfo);
						temp.setUnitCrossId(crossInfo.getUnitCrossId());
						//设置主键
						temp.setUnitCrossTrainId(UUID.randomUUID().toString());
						list.add(temp);
					}
				}
				
			}
		}
		System.err.println("CrossTrainInfo_list=====" + list);
		return list;
	}
	/**
	 * 准备unit_cross表中数据
	 * @param crossInfo
	 * @return
	 */
	private List<CrossInfo>  prepareUnitCrossInfo(CrossInfo crossInfo) throws Exception{
		List<CrossInfo> list = new ArrayList<CrossInfo>();
		//组数（需几组车底担当）
		int groupTotalNbr = crossInfo.getGroupTotalNbr();
		String crossId = crossInfo.getCrossId();
		//通过crossId查询cross_train信息
		 List<CrossTrainInfo> crossTrainList = getCrossTrainInfoForCrossid(crossId);
		//交路开始日期,格式yyyyMMdd
		String crossStartDate = crossInfo.getCrossStartDate();
		String crossEndDate = crossInfo.getCrossEndDate();
		System.err.println("crossStartDate11==" + crossStartDate);
		System.err.println("crossEndDate11==" + crossEndDate);
		
		//第一辆车的始发站
		String startStn = crossInfo.getStartStn();
		//获取第一辆车的车次
		String trainNbr = "";
		//一个交路跨越的天数
		int crossDay = DateUtil.getDaysBetween(DateUtil.getFormateDay(crossEndDate), DateUtil.getFormateDay(crossStartDate));
		//高线标记
		int highlineFlag = 0;
		//高线开行规律
		int highlineRule = 0;
		//普线开行规律,普线开行规律（1:每日;2:隔日）
		int commonlineRule = 1;
		
		/*if(!StringUtil.strIsNull(crossInfo.getHighlineFlag())){
			highlineFlag = Integer.valueOf(crossInfo.getHighlineFlag());
		}
		if(!StringUtil.strIsNull(crossInfo.getHighlineRule())){
			highlineRule = Integer.valueOf(crossInfo.getHighlineRule());
		}
		if(!StringUtil.strIsNull(crossInfo.getCommonlineRule())){
			commonlineRule = Integer.valueOf(crossInfo.getCommonlineRule());
		}*/
		
		
		if(groupTotalNbr >0 ){
			for(int i = 0;i<groupTotalNbr;i++){
				CrossInfo tempInfo = new CrossInfo();
				BeanUtils.copyProperties(tempInfo, crossInfo);
				if(crossTrainList != null && crossTrainList.size()-1>=i ){
					CrossTrainInfo trainInfo = crossTrainList.get(i);
					highlineFlag = trainInfo.getHighlineFlag();
					highlineRule = trainInfo.getHighlineRule();
					commonlineRule = trainInfo.getCommonLineRule();
					trainNbr = trainInfo.getTrainNbr();
				}
				//如果是第一组交路单元，那么交路单元的开始日期为交路的开始日期
				if(i == 0){
					tempInfo.setCrossStartDate(crossStartDate);
					tempInfo.setCrossEndDate(crossEndDate);
				}else{
					/**计算下一个交路单元的开始日期，在上一个交路单元的终到日期的基础上再加上间隔天数**/
					//上一个交路单元的终到日期,格式为yyyyMMdd
					System.err.println("list.get(i-1)" +list.get(i-1));
					String preCrossEndDate = list.get(i-1).getCrossEndDate();
					String nextCrossStartDate = "";
					String nextCrossEndDate = "";
					//高线标记（1:高线；0:普线；2:混合）
					if(highlineFlag == 0 || highlineFlag == 2){
						nextCrossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -commonlineRule);
						
					}else {
						//高线开行规律（1:平日;2:周末;3:高峰）
						if(highlineRule == 1){
							nextCrossStartDate = DateUtil.getDateByDay(DateUtil.getFormateDay(preCrossEndDate), -highlineRule);	
						
						}else if(highlineRule == 2 || highlineRule ==3 ){
							//TODO 暂时不处理
						}
						
					}
					if(!"".equals(nextCrossStartDate)){
						LocalDate sourceDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(nextCrossStartDate);
						nextCrossStartDate = sourceDate.toString("yyyyMMdd");
						
						tempInfo.setCrossStartDate(nextCrossStartDate);
						//计算终到日期
						nextCrossEndDate = DateUtil.getDateByDay(DateUtil.getFormateDay(nextCrossStartDate), -crossDay);
						sourceDate =  DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(nextCrossEndDate);
						tempInfo.setCrossEndDate( sourceDate.toString("yyyyMMdd"));	
					}
					
					
				}	
				tempInfo.setUnitCrossId(UUID.randomUUID().toString());
				//编组名，规则：第一辆车的始发站的第一个字 + "开" + "-" +第一辆车的车次+组数
				int teamNum = i+1;
				if( startStn != null && !"".equals(startStn) ){
					tempInfo.setMarshallingName(startStn.substring(0,1)+"开"+"-"+trainNbr+"-" +teamNum);
				}else{
					//TODO
					tempInfo.setMarshallingName(trainNbr+"-" +teamNum);
				}
				
				//组数序号
				tempInfo.setGroupSerialNbr(teamNum);
				list.add(tempInfo);
			}
		}
		System.err.println("CrossInfo_list=====" + list);
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
	public List<CrossInfo>  getUnitCrossInfo(Map<String,Object> reqMap){
		List<CrossInfo>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_INFO, reqMap);
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
		return (CrossInfo)baseDao.selectOneBySql(Constants.CROSSDAO_GET_CROSS_INFO_FOR_CROSSID, paramMap);
	    
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
	 * 通过crossid查询crosstrainInfo信息
	 * @param unitCrossId
	 * @return
	 */
	public List<CrossTrainInfo> getUnitCrossTrainInfoForUnitCrossid(String unitCrossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("unitCrossId", unitCrossId);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_UNIT_CROSS_TRAIN_INFO_FOR_UNIT_CROSSID, paramMap);
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
	 *  通过unitCorssId分别查询数据并组合
	 * @param unitCorssId
	 * @return
	 */
	public BaseCrossDto  getUnitCrossDtoWithUnitCrossId(String unitCorssId){
		BaseCrossDto baseCrossDto = new BaseCrossDto();
		
		CrossInfo crossInfo = getUnitCrossInfoForUnitCrossid(unitCorssId);
		System.err.println("getUnitCrossDtoWithUnitCrossId---crossid==" + crossInfo.getCrossId());
		baseCrossDto.setBaseCrossId(crossInfo.getCrossId());
		baseCrossDto.setCrossName(crossInfo.getMarshallingName());
		baseCrossDto.setCrossStartDate(crossInfo.getCrossStartDate());
		baseCrossDto.setCrossEndDate(crossInfo.getCrossEndDate());
		List<BaseCrossTrainDto> subList = new ArrayList<BaseCrossTrainDto>();
		/**
		 * unitCrossTrain信息以train_sort升序排列
		 */
		List<CrossTrainInfo> trainInfoList = getUnitCrossTrainInfoForUnitCrossid(unitCorssId);
		if(trainInfoList != null && trainInfoList.size() > 0){
			System.err.println("trainInfoList.size()==" + trainInfoList.size());
			for(CrossTrainInfo trainInfo :trainInfoList){
				BaseCrossTrainDto dto = new BaseCrossTrainDto();
				String trainId = trainInfo.getBaseTrainId();
				if(!"".equals(trainId)&&trainId != null && !"null".equals(trainId)){
					dto.setBaseTrainId(trainId);
					dto.setDayGap(trainInfo.getDayGap());
					dto.setTrainSort(trainInfo.getTrainSort());
					dto.setTrainNbr(trainInfo.getTrainNbr());
					subList.add(dto);
				}
			}
			baseCrossDto.setListBaseCrossTrain(subList);
		}
		return baseCrossDto;
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
	
	
	public void actionExcel(InputStream inputStream, String chartId, String startDay, String chartName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
		Map<String, String> tokenPsgDeptValuesMap = new HashMap<String, String>();
		//路局字典信息
		List<Ljzd> lizdList = commonService.getFullStationInfo();
		if(lizdList !=null && lizdList.size() > 0){
			for(Ljzd dto : lizdList){
				tokenPsgDeptValuesMap.put(dto.getLjpym(), dto.getLjjc());
			}
		}
		System.err.println("tokenPsgDeptValuesMap==" + tokenPsgDeptValuesMap);
		valuesMap.put("tokenPsgBureau", tokenPsgDeptValuesMap); 
		valuesMap.put("tokenVehBureau", tokenPsgDeptValuesMap);  
		
		 
		
		// TODO Auto-generated method stub
		try{
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			int num = workbook.getNumberOfSheets();		
			List<CrossInfo> alllist = new ArrayList<CrossInfo>();
			for(int i = 0; i < num; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<CrossInfo> test = new ExcelUtil<CrossInfo>(pm, sheet, CrossInfo.class);
				test.setValueMapping(valuesMap);
				List<CrossInfo> list = test.getEntities(-1);
				alllist.addAll(list);  
			}
			
			ExecutorService service=Executors.newFixedThreadPool(10);
			CompletionService<List<CrossTrainInfo>> completion=new ExecutorCompletionService<List<CrossTrainInfo>>(service);
			 
			ArrayList<CrossTrainInfo> crossTrains = new ArrayList<CrossTrainInfo>();
			
			for(int i = 0; i < alllist.size(); i++){
				CrossInfo crossInfo = alllist.get(i);
				crossInfo.setChartId(chartId);
				crossInfo.setChartName(chartName); 
				 if(StringUtils.isEmpty(crossInfo.getAlterNateDate())){
					 crossInfo.setAlterNateDate(startDay);
				 }
				completion.submit(new CrossCompletionService(alllist.get(i)));
			}
			
			for(int i = 0; i < alllist.size(); i++){
				try {
					crossTrains.addAll(completion.take().get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			} 
			service.shutdown();
		 
			///fortest
			if(alllist != null && alllist.size() > 0){
				for(int i = 0;i<alllist.size();i++){
					CrossInfo crossinfo = alllist.get(i);
					System.err.println("chartId==" + crossinfo.getChartId());
					System.err.println("chartName==" + crossinfo.getChartName());
					System.err.println("***********************");
					
				}
			}
			
			///////////
			
			if(alllist != null && alllist.size() > 0){
				//保存交路信息
				baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_INFO,alllist);
				
			}
			System.err.println("crossTrains===" + crossTrains);
        	//保存列车
			if(crossTrains != null && crossTrains.size() > 0 ){
				baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
			}
	
			
		}catch(FileNotFoundException e){
			//e.printStackTrace();
		}catch(IOException e){
			//e.printStackTrace();
		}
		
	} 

	
	private void setDayGapForTrains(LinkedList<CrossTrainInfo> crossTrains){
		for(int i = 0; i < crossTrains.size(); i++){
			 
			setDayGap(crossTrains.get(i), crossTrains.get(i - 1 < 0 ? crossTrains.size() - 1 : i - 1));
			 
		}
	} 
	
	private void setEndDateForCross(LinkedList<CrossTrainInfo> crossTrains, CrossInfo cross){
		 try{
			 //设置交路的终到日期
			 int dayGapForCross = 0;
			 for(CrossTrainInfo crosstrain : crossTrains){
				 dayGapForCross += crosstrain.getRunDay() + crosstrain.getDayGap();
			 } 
			 Date date = dateFormat.parse(cross.getCrossStartDate());   
			 Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE, dayGapForCross);
			 
			 cross.setCrossEndDate(dateFormat.format(calendar.getTime()));
		 }catch(Exception e){
			 
		 }
	} 
	 
	/**
	 * 计算列车间隔时间
	 * @param train1 需要设置间隔时间的列车
	 * @param train2 前置列车
	 */
	private void setDayGap(CrossTrainInfo train1, CrossTrainInfo train2){ 
		try { 
			logger.debug(train1.getSourceTargetTime());
			logger.debug(train1.getSourceTargetTime() + "-" + train1.getSourceTargetTime().substring(train1.getSourceTargetTime().indexOf(":") + 1));
			Date sourceTime = format.parse("1977-01-01 " + train1.getSourceTargetTime().substring(train1.getSourceTargetTime().indexOf(":") + 1));
			Date targetTime = train2 != null ?  format.parse("1977-01-01 " + train2.getTargetTime().substring(train2.getTargetTime().indexOf(":") + 1)) :  format.parse("1977-01-01 " + train1.getTargetTime().substring(train1.getTargetTime().indexOf(":") + 1));
			
			if(sourceTime.compareTo(targetTime) < 0){
				train1.setDayGap(1);
			}else{
				train1.setDayGap(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		};
		 
	}
	




	/**
	 * 用于并行处理交路
	 * @author Administrator
	 *
	 */
	class CrossCompletionService implements Callable<List<CrossTrainInfo>> {
		
		/**
		 * 并行处理列车基本信息
		 * @author Administrator
		 *
		 */
		class GetTrainInfoCompletionService implements Callable<CrossTrainInfo> {
			
			private CrossTrainInfo train;
			
			public GetTrainInfoCompletionService(CrossTrainInfo train ){
			   this.train = train;
			}
			
			public CrossTrainInfo call() throws Exception {
				trainInfoFromPain(this.train);
			    return this.train;
			}
			
			private void trainInfoFromPain(CrossTrainInfo train){
				String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:6:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}}},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
				//http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=G11 
				try {
					Client client = Client.create();
					
					client.setConnectTimeout(60*1000);
					String trainNbr = train.getTrainNbr();
					String stn = null;
					if(trainNbr.indexOf("(") >= 0){
						trainNbr = trainNbr.substring(0, trainNbr.indexOf("(")).trim();
						
						
						
						stn = trainNbr.substring(trainNbr.indexOf("(") + 1, trainNbr.indexOf(")")).trim();
					}
					WebResource webResource = client.resource("http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=" + trainNbr); 
					
//					webResource.method(this.method, GenericType)
					
					ClientResponse response = webResource.type("application/json")
							.accept("application/json").method("GET", ClientResponse.class); 
				    
					//将返回结果转换为指定对象 
					result = response.getEntity(String.class);  
					 logger.debug(result);
					JSONArray obj = JSONObject.fromObject(result).getJSONArray("data");
					if(obj.size() > 0){
						
						JSONObject curTrain = null;
						JSONObject scheduleDto = null;
						JSONObject sourceItemDto = null;
						JSONObject targetItemDto = null;   
						
						//findFlag
						boolean findFlag = false;
						for(int i = 0; i < obj.size(); i++){ 
							curTrain = obj.getJSONObject(i);
							scheduleDto = curTrain.getJSONObject("scheduleDto");
							sourceItemDto = scheduleDto.getJSONObject("sourceItemDto");
							targetItemDto = scheduleDto.getJSONObject("targetItemDto"); 
							//如果车次中有站名，如果不匹配就返回
							if(stn != null){ 
								String startStn = sourceItemDto.getString("name");
								String endStn = targetItemDto.getString("name");
								String fixStnYw = startStn + ":" + endStn; 
								String fixStnZw = startStn + "：" + endStn; 
								if(stn.equals(startStn) 
										|| stn.equals(endStn) 
										||  stn.equals(fixStnYw)
										||  stn.equals(fixStnZw)){
									findFlag = true;
									break;
								} 
								 
//								sourceItemDto.getString("name")
//								if(stn.equals(sourceItemDto.getString("name")) 
//												|| stn.equals(targetItemDto.getString("nodeName"))){
//									findFlag = true;
//									break;
//								}
							}else{//如果没有站名做标示就取第一个列车，应该只有一辆吧 
								if(obj.size() > 1){
									logger.error(trainNbr + ",有两辆列车从计划平台陪查询出来，默认取了第一辆");
								}
								findFlag = true;
								break;
							}
						}
						
						
						
						if(findFlag){
							//设置列车在计划平台ID
							train.setBaseTrainId(curTrain.getString("id"));
							if(sourceItemDto != null){
								//设置始发时间
								try {
									train.setSourceTargetTime(sourceItemDto.getString("sourceTimeDto2"));
									train.setStartBureau(commonService.getLjInfo(sourceItemDto.getString("bureauName")).getLjpym());
									//设置始发站
									train.setStartStn(sourceItemDto.getString("nodeName"));
								} catch (Exception e) {
									// TODO: handle exception
								} 
							}
//							"{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\"," +
//							"\"pinyinCode\":null,\"description\":null," +
//							"\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"}," +
//							"\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":" +
//							"\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\"," +
//							"\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\"," +
//							"\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\"," +
//							"\"targetTimeDto2\":\"0:6:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\"," +
//							"\"timeDto\":null," +
//							"\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}}},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}
//							targetItemDto.getJSONObject("timeDto1").getString("day")
							
							if(targetItemDto != null){
								train.setRunDay(targetItemDto.getJSONObject("timeDto1").getInt("day"));
								//设置终到时间
								train.setTargetTime(targetItemDto.getString("targetTimeDto2"));
								//设置终到局
								train.setEndBureau(commonService.getLjInfo(targetItemDto.getString("bureauName")).getLjpym());
								//设置中档站
								train.setEndStn(targetItemDto.getString("nodeName"));
							}
						}
					}
					//间隔天数
					 
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}   
			} 
		}
			
		private CrossInfo cross;
		
		public CrossCompletionService(CrossInfo cross ){
		   this.cross = cross;
		}
		
		public List<CrossTrainInfo> call() throws Exception {
			 
		    return this.createTrainsForCross(this.cross);
		}
		
		private LinkedList<CrossTrainInfo> createTrainsForCross(CrossInfo cross){
			logger.debug("");
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
							train.setAlertNateTime(alertNateDate[0]); 
						}else{ 
							train.setAlertNateTime(alertNateDate[i]);
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
							train.setAppointWeek(highlineRule[i]); 
						}  
					}
					
				}catch(Exception e){
					logger.error("创建列车信息出错:" , e);
				}
				crossTrains.add(train);
			} 
			//获取列车时刻表和其实和终到站
		   ExecutorService service=Executors.newCachedThreadPool();
		   CompletionService<CrossTrainInfo> completion=new ExecutorCompletionService<CrossTrainInfo>(service);
		   
		   for(int i=0; i < crossTrains.size(); i++){
			   completion.submit(new GetTrainInfoCompletionService(crossTrains.get(i)));
		   } 
		  
		   for(int i=0;i < crossTrains.size();i++){
			   try {
					CrossTrainInfo crossTrain = completion.take().get();
					//设置交路的开始日期和结束日期
					if(cross.getCrossName().startsWith(crossTrain.getTrainNbr())){
						cross.setStartBureau(crossTrain.getStartBureau());
	//					cross.setCrossStartDate(crossTrain.getSourceTargetTime());
						cross.setCrossStartDate(crossTrain.getAlertNateTime());
					}   
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
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
					//
					if(alertNateTrains != null){
						 train.setAlertNateTrainNbr(alertNateTrains[i]);
					}
					//
					if(alertNateDate != null){
						if(alertNateDate.length == 1){
							Date date = dateFormat.parse(alertNateDate[0]);   
							Calendar calendar = new GregorianCalendar();
							calendar.setTime(date);
							calendar.add(Calendar.DATE, i);
							train.setAlertNateTime(dateFormat.format(calendar.getTime()));
						}else{
							train.setAlertNateTime(alertNateDate[i]);
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
				}catch(Exception e){
					logger.error("创建列车信息出错:" , e);
				}
				crossSpareTrains.add(train);
			} 
			
		   completion=new ExecutorCompletionService<CrossTrainInfo>(service); 
		   for(int i=0; i < crossSpareTrains.size(); i++){
			   completion.submit(new GetTrainInfoCompletionService(crossSpareTrains.get(i)));
		   }
		   for(int i=0;i < crossSpareTrains.size();i++){
			   try {
				completion.take().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		   }
		   service.shutdown();
			 
		   setDayGapForTrains(crossSpareTrains);
			
		   crossTrains.addAll(crossSpareTrains);
		}
			 
//			String trains = crossName.split("-");
		logger.debug(this.cross.getCrossName() + "==crossTrains=" + crossTrains.size());
		
		return crossTrains; 
	}
		
		
	
	}
}
