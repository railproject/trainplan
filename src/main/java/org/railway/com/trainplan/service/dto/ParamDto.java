package org.railway.com.trainplan.service.dto;

public class ParamDto {

	private String sourceEntityId;
	//开行日期yyyy-mm-dd
	private String time;
	private String source = "1";
	private String action = "1";
	public String getSourceEntityId() {
		return sourceEntityId;
	}
	public void setSourceEntityId(String sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}