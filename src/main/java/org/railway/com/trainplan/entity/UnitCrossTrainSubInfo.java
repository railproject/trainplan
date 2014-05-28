package org.railway.com.trainplan.entity;

public class UnitCrossTrainSubInfo {

	private int trainSort;
	private int dayGap;
	private String unitCrossTrainId;
	private String trainNbr;
	private String baseTrainId;
	private String startStn;
	private String startBureau;
	private String endStn;
	private String endBureau;
	
	
	
	public String getUnitCrossTrainId() {
		return unitCrossTrainId;
	}
	public void setUnitCrossTrainId(String unitCrossTrainId) {
		this.unitCrossTrainId = unitCrossTrainId;
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
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getEndBureau() {
		return endBureau;
	}
	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}
	
}
