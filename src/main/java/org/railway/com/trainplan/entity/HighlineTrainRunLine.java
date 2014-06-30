package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class HighlineTrainRunLine {

	private List<HighlineTrainRunLineTime> highlineTrainTimeList = new ArrayList<HighlineTrainRunLineTime>();
	private String planTrainId;
	private String trainNbr;
	public List<HighlineTrainRunLineTime> getHighlineTrainTimeList() {
		return highlineTrainTimeList;
	}
	public void setHighlineTrainTimeList(
			List<HighlineTrainRunLineTime> highlineTrainTimeList) {
		this.highlineTrainTimeList = highlineTrainTimeList;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	
	
}
