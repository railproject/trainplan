package org.railway.com.trainplan.entity;

public class HighlineCrossInfo {

	//高铁日历交路计划ID（本表ID）
	private String highlineCrossId;
	//交路计划ID（对应PLAN_CROSS表中的PLAN_CROSS_ID）
	private String planCrossId;
	//开始日期（该日历交路第一个车次的始发日期）
	private String crossStartDate;
	//结束日期（该日历交路最后一个车次的终到日期）
	private String crossEndDate;
	//交路名称
	private String crossName;
	//备用及停运标记（1:开行;2:备用;0:停运）
	private String spareFlag;
	//相关局（局码）
	private String relevantBureau;
	//车辆担当局（局码）
	private String tokenVehBureau;
	//担当车辆段/动车段
	private String tokenVehDept;
	//担当动车所（用于高铁）
	private String tokenVehDepot;
	//客运担当局（局码）
	private String tokenPsgBureau;
	//担当客运段
	private String tokenPsgDept;
	//动车组车型（用于高铁）
	private String crhType;
	//动车组车组号1（用于高铁）
	private String vehicle1;
	//动车组车组号2（用于高铁）
	private String vehicle2;
	//备注
	private String note;
	//创建人
	private String creatPeople;
	//创建人单位
	private String creatPeopleOrg;
	//创建时间（格式：yyyy-mm-dd hh24:mi:ss）
	private String creatTime;
	//审核状态（0:未审核1:审核）
	private int checkType ;
	//审核人
	private String checkPeople;
	//审核人单位
	private String checkPeopleOrg;
	//审核时间（格式：yyyy-mm-dd hh24:mi:ss）
	private String checkTime;
	
}
