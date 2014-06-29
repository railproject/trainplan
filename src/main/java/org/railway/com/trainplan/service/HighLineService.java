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
public class HighLineService{
	private static final Logger logger = Logger.getLogger(CommonService.class); 
	
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
	 
}
