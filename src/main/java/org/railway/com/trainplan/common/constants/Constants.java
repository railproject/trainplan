
package org.railway.com.trainplan.common.constants;



/**
 * 定义基本常用的参数
 * @author join
 *
 */
public class Constants {
	

	//rest请求超时设置30秒
	public static final int CONNECT_TIME_OUT = 30 * 1000;
	public static final String DATE_FORMAT_1 = "yyyyMMddHHmm";	
	public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
	//TODO:后台服务地址，以后要改为配置文件
	//后台接口地址
	
	public static  String SERVICE_URL = "" ;//PropertiesConfiguration.getStringValue("SERVICE_URL");
	//获取方案列表
	public static final String GET_SCHEME_LIST = "/rail/template/TemplateSchemes";
	//基本图方案包含的基本图运行线
	public static final String GET_TRAINLINE_TEMPLATES = "/rail/template/TrainlineTemplates";
	//查询始发日期为给定日期范围的日计划运行线统计数
	public static final String GET_TRAINLINS = "/rail/plan";
	/****站点类型【1：始发 2：经由 3：终到】 ***/
	public static final String STATION_BEGIN = "1";
	public static final String STATION_ROUTE = "2";
	public static final String STATION_END = "3";
	
	//列车类型：客运
	public static final String ZUOYE_DENGJI_KEYUN = "客运";
	
	
	/******mapper.xml文件中的id常量*****/
	//获取路局信息
	public static final String LJZDDAO_GET_LJ_INFO = "ljzdDao.getLjInfo";
	//获取trainType 货运或客运getTrainType
	public static final String LJZDDAO_GET_TRAIN_TYPE = "ljzdDao.getTrainType";
	//获取18个路局信息getFullStationInfo
	public static final String LJZDDAO_GET_FULL_STATION_INFO = "ljzdDao.getFullStationInfo";
	//更新表train_plan字段check_state  trainPlanDao   updateCheckState
	public static final String TRAINPLANDAO_UPDATE_CHECKSTATE = "trainPlanDao.updateCheckState";
	//更新表train_plan字段 plan_flag updatePlanTrainDaylyPlanFlag
	public static final String TRAINPLANDAO_UPDATE_PLANFLAG = "trainPlanDao.updatePlanTrainDaylyPlanFlag";
	//根据路局名称和运行时间查询列车
	public static final String TRAINPLANDAO_GET_TOTALTRAINS = "trainPlanDao.getTotalTrains";
	//  getTrainShortInfo
	public static final String TRAINPLANDAO_GET_TRAIN_SHORTINFO = "trainPlanDao.getTrainShortInfo";
	//对表train_plan插入计划数据 addTrainPlan
	public static final String TRAINPLANDAO_ADD_TRAIN_PLAN = "trainPlanDao.addTrainPlan";
	//对表train_plan_stn插入经由数据addTrainPlanStn
	public static final String TRAINPLANDAO_ADD_TRAIN_PLAN_STN = "trainPlanDao.addTrainPlanStn";
	//获取最大的train_plan_id   getMaxPlanTrainId
	public static final String TRAINPLANDAO_GET_MAX_PLANTRAIN_ID = "trainPlanDao.getMaxPlanTrainId";
}
