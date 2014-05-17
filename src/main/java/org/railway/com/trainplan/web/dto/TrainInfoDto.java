package org.railway.com.trainplan.web.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainInfoDto {
    //车次
	public String trainName;
	//始发站名
	public String startStn;
	//终到站名
	public String endStn;
	//始发时刻
	public String startTime;
	//终到时刻
	public String endTime;
	public String startDate;
	private String endDate;
	public List<PlanLineSTNDTO> trainStns = new ArrayList<PlanLineSTNDTO>();
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
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
	public List<PlanLineSTNDTO> getTrainStns() {
		return trainStns;
	}
	public void setTrainStns(List<PlanLineSTNDTO> trainStns) {
		this.trainStns = trainStns;
	}
	
	
}
