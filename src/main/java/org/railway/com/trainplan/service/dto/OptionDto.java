package org.railway.com.trainplan.service.dto;

public class OptionDto {
	
	private String code;
	private String name;
	private String bureauCode;
	
	 
	public String getBureauCode() {
		return bureauCode;
	}
	public void setBureauCode(String bureauCode) {
		this.bureauCode = bureauCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
