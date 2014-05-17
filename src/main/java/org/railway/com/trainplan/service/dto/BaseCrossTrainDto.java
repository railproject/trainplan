package org.railway.com.trainplan.service.dto;

public class BaseCrossTrainDto {

	private String trainNbr;
	private int trainSort;
	private int dayGap;
	private String baseTrainId;
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	
	public int getTrainSort() {
		return trainSort;
	}
	public void setTrainSort(int trainSort) {
		this.trainSort = trainSort;
	}
	public int getDayGap() {
		return dayGap;
	}
	public void setDayGap(int dayGap) {
		this.dayGap = dayGap;
	}
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	
	
}
