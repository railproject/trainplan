package org.railway.com.trainplan.entity;

import java.util.Date;
import java.util.UUID;


public class CrossInfo {
	
	//BASE_CROSS_ID	BASE_CHART_ID	BASE_CHART_NAME	CROSS_START_DATE	CROSS_END_DATE	
	//CROSS_NAME	CROSS_SPARE_NAME	ALTERNATE_DATE	ALTERNATE_TRAIN_NBR	SPARE_FLAG	
	//CUT_OLD	GROUP_TOTAL_NBR	PAIR_NBR	HIGHLINE_FLAG	HIGHLINE_RULE	COMMONLINE_RULE	
	//APPOINT_WEEK	APPOINT_DAY	CROSS_SECTION	THROUGH_LINE	START_BUREAU	
	//TOKEN_VEH_BUREAU	TOKEN_VEH_DEPT	TOKEN_VEH_DEPOT	TOKEN_PSG_BUREAU	TOKEN_PSG_DEPT	
	//LOCO_TYPE	CRH_TYPE	ELEC_SUPPLY	DEJ_COLLECT	AIR_CONDITION	NOTE	CREAT_PEOPLE	
	//CREAT_PEOPLE_ORG	CREAT_TIME 
	//LOCO_TYPE	CRH_TYPE	ELEC_SUPPLY	DEJ_COLLECT	AIR_CONDITION	NOTE	CREAT_PEOPLE	
		//CREAT_PEOPLE_ORG	CREAT_TIME 
	private String crossId = UUID.randomUUID().toString() ;
	
	private String crossIdForExcel;
	

	private int highlineRule;
	private int commonlineRule;
	private int cutOld;
	private int groupTotalNbr;
	private int elecSupply;
	private int dejCollect;
	private int airCondition;
	private String crossName; 
	private String chartId;
	private String chartName;
	private String crossStartDate;
	private String crossEndDate;
	private String crossSpareName;
	private String alterNateDate;
	private String alterNateTranNbr;
	private String spareFlag;
	private String pairNbr;
	private String highlineFlag;
	private String appointWeek;
	private String appointDay;
	private String crossSection;
	private String throughline;
	private String startBureau;
	private String tokenVehBureau;
	private String tokenVehDept;
	private String tokenVehDepot;
	private String tokenPsgBureau;
	private String tokenPsgDept;
	private String tokenPsgDepot;
	private String locoType;
	private String crhType;

	private String note; 
	private String createPeople; 
	private String createPeopleOrg;  
	private String createTime ;
	
	
	public String getCrossIdForExcel() {
		return crossIdForExcel;
	}
	public void setCrossIdForExcel(String crossIdForExcel) {
		
	}
	 
	public String getCrossId() {
		return crossId;
	}
	public void setCrossId(String crossId) {
    	this.crossId = crossId;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getChartId() {
		return chartId;
	}
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getCrossStartDate() {
		return crossStartDate;
	}
	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}
	public String getCrossEndDate() {
		return crossEndDate;
	}
	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}
	public String getCrossSpareName() {
		return crossSpareName;
	}
	public void setCrossSpareName(String crossSpareName) {
		this.crossSpareName = crossSpareName;
	}
	public String getAlterNateDate() {
		return alterNateDate;
	}
	public void setAlterNateDate(String alterNateDate) {
		this.alterNateDate = alterNateDate;
	}
	public String getAlterNateTranNbr() {
		return alterNateTranNbr;
	}
	public void setAlterNateTranNbr(String alterNateTranNbr) {
		this.alterNateTranNbr = alterNateTranNbr;
	}
	public String getSpareFlag() {
		return spareFlag;
	}
	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}
	public int getCutOld() {
		return cutOld;
	}
	public void setCutOld(int cutOld) {
		this.cutOld = cutOld;
	}
	public int getGroupTotalNbr() {
		return groupTotalNbr;
	}
	public void setGroupTotalNbr(int groupTotalNbr) {
		this.groupTotalNbr = groupTotalNbr;
	}
	public String getPairNbr() {
		return pairNbr;
	}
	public void setPairNbr(String pairNbr) {
		this.pairNbr = pairNbr;
	}
	public String getHighlineFlag() {
		return highlineFlag;
	}
	public void setHighlineFlag(String highlineFlag) {
		this.highlineFlag = highlineFlag;
	}
	public int getHighlineRule() {
		return highlineRule;
	}
	public void setHighlineRule(int highlineRule) {
		this.highlineRule = highlineRule;
	}
	public int getCommonlineRule() {
		return commonlineRule;
	}
	public void setCommonlineRule(int commonlineRule) {
		this.commonlineRule = commonlineRule;
	}
	public String getAppointWeek() {
		return appointWeek;
	}
	public void setAppointWeek(String appointWeek) {
		this.appointWeek = appointWeek;
	}
	public String getAppointDay() {
		return appointDay;
	}
	public void setAppointDay(String appointDay) {
		this.appointDay = appointDay;
	}
	public String getCrossSection() {
		return crossSection;
	}
	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}
	public String getThroughline() {
		return throughline;
	}
	public void setThroughline(String throughline) {
		this.throughline = throughline;
	}
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	public String getTokenVehBureau() {
		return tokenVehBureau;
	}
	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}
	public String getTokenVehDept() {
		return tokenVehDept;
	}
	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}
	public String getTokenVehDepot() {
		return tokenVehDepot;
	}
	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}
	public String getTokenPsgBureau() {
		return tokenPsgBureau;
	}
	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}
	public String getTokenPsgDept() {
		return tokenPsgDept;
	}
	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}
	public String getTokenPsgDepot() {
		return tokenPsgDepot;
	}
	public void setTokenPsgDepot(String tokenPsgDepot) {
		this.tokenPsgDepot = tokenPsgDepot;
	}
	public String getLocoType() {
		return locoType;
	}
	public void setLocoType(String locoType) {
		this.locoType = locoType;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}
	public int getElecSupply() {
		return elecSupply;
	}
	public void setElecSupply(int elecSupply) {
		this.elecSupply = elecSupply;
	}
	public int getDejCollect() {
		return dejCollect;
	}
	public void setDejCollect(int dejCollect) {
		this.dejCollect = dejCollect;
	}
	public int getAirCondition() {
		return airCondition;
	}
	public void setAirCondition(int airCondition) {
		this.airCondition = airCondition;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreatePeople() {
		return createPeople;
	}
	public void setCreatePeople(String createPeople) {
		this.createPeople = createPeople;
	}
	public String getCreatePeopleOrg() {
		return createPeopleOrg;
	}
	public void setCreatePeopleOrg(String createPeopleOrg) {
		this.createPeopleOrg = createPeopleOrg;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	 
	public static void main(String args[]){
		System.err.println(new Date());
		
	}

}
