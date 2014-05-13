package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 基本图方案包含的基本图运行线对象
 * @author join
 *
 */
public class TrainlineTemplateDto {
	private List<TrainlineTemplateSubDto> stationList = new ArrayList<TrainlineTemplateSubDto>();
	private String planTrainId ;
   //开行日期
	private String runDate;
	//车次
	private String trainNbr;
	//始发时间
	private String startTime ;
	//终到时间
	private String endTime;
    //始发站名
	private String startStn;
	//终到站名
	private String endStn;
	//基本图案id
	private String baseChartId;
	//基本图列车id
	private String baseTrainId;
	//始发局全称
	private String startBureauFull;
	//终到局全称
	private String endBureauFull;
	//列车类型id
	private String trainType;
	
	
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getTrainType() {
		return trainType;
	}
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	public List<TrainlineTemplateSubDto> getStationList() {
		return stationList;
	}
	public void setStationList(List<TrainlineTemplateSubDto> stationList) {
		this.stationList = stationList;
	}
	public String getRunDate() {
		return runDate;
	}
	
	
	public String getStartBureauFull() {
		return startBureauFull;
	}
	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}
	public String getEndBureauFull() {
		return endBureauFull;
	}
	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getBaseChartId() {
		return baseChartId;
	}
	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	
	
	
}
