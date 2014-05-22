package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "PLAN_TRAIN")
public class PlanTrain implements Serializable{
    //  select   PLAN_TRAIN_ID as planTrainId,TRAIN_NBR as trainNbr,START_STN as startStn,
	//  END_STN as endStn,RUN_DATE as  runDate,START_TIME as startTime,
	//  END_TIME as endTime,BASE_CHART_ID as baseChartId,BASE_TRAIN_ID as baseTrainId,
	//  START_BUREAU as startBureau,END_BUREAU as endBureau,END_BUREAU_FULL as endBureauFull,
	//  START_BUREAU_FULL as startBureauFull from plan_train where run_date=#{runDate} and  START_BUREAU_FULL=#{startBureauFull}
	//   order by TRAIN_NBR desc
	    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    //列车id
	private String planTrainId;//PLAN_TRAIN_ID;
	//车次
	private String trainNbr;//TRAIN_NBR;
	//始发站
	private String startStn;//START_STN;
	//终点站
	private String endStn;//END_STN;
	//开行日期
	private String runDate;
	private Date startTime;
	private Date endTime;
	//基本方案id
	private String baseChartId;
	//基本图列车id
	private String baseTrainId;
	
	private String startBureau;//始发局局码
	private String endBureau;//终到局局码
	private String endBureauFull;//终到局全称
	
	

	private String startTimeStr;
	private String endTimeStr;

	private String startBureauFull;//始发局全称
	private String jylj;//经由路局  如：“京,济,上”
	//列车种类：货运，客运
	private String operation;
	
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	//@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	//@Column(name = "PLAN_TRAIN_ID", unique = true, nullable = false)
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	
	//@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	//@Column(name = "START_BUREAU")
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	
	
	//@Column(name = "START_BUREAU_FULL")
	public String getStartBureauFull() {
		return startBureauFull;
	}
	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}
	
	
	//@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	//@Column(name = "BASE_CHART_ID")
	public String getBaseChartId() {
		return baseChartId;
	}
	
	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}
	//@Column(name = "BASE_TRAIN_ID")
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	//@Column(name = "TRAIN_NBR")
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	//@Column(name = "START_STN")
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	//@Column(name = "END_STN")
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	//@Column(name = "RUN_DATE")
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	

	//@Column(name = "END_BUREAU")
	public String getEndBureau() {
		return endBureau;
	}
	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}
	
	//@Column(name = "END_BUREAU_FULL")
	public String getEndBureauFull() {
		return endBureauFull;
	}
	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}
	
	
	//@Transient
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	//@Transient
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	
	
	//@Transient
	public String getJylj() {
		return jylj;
	}
	public void setJylj(String jylj) {
		this.jylj = jylj;
	}
	
	
	
	
	
	
	
}
