package org.railway.com.trainplan.service.dto;

public class TrainRunDto {
	private String day;
	private String runFlag; 
	private String createFlag;
	
	public TrainRunDto(String day, String runFlag) {
		super();
		this.day = day;
		this.runFlag = runFlag;
	}
	
	public TrainRunDto(String day, String runFlag, String createFlag) {
		super();
		this.day = day;
		this.runFlag = runFlag;
		this.createFlag = createFlag;
	}
	
	
	public String getCreateFlag() {
		return createFlag;
	}


	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
	}


	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}

}
