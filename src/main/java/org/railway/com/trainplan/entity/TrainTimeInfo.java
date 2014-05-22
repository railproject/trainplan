package org.railway.com.trainplan.entity;

public class TrainTimeInfo {
	 private int index;
	 private String stnName;
	 private String bureauShortName;
	 private String trackName;
	 private String arrTime;
	 private String dptTime;
	 private int runDay; 
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getArrTime() {
		return arrTime;
	}
	public void setArrTime(String arrTime) {
		this.arrTime = arrTime;
	}
	public String getDptTime() {
		return dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	public int getRunDay() {
		return runDay;
	}
	public void setRunDay(int runDay) {
		this.runDay = runDay;
	}


}
