package org.railway.com.trainplan.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.service.dto.TrainRunDto;

public class Result {

	private String code;

	private String message;

	private Object data;

	public Result(){
		this.code = StaticCodeType.SYSTEM_SUCCESS.getCode();
		this.message = StaticCodeType.SYSTEM_SUCCESS.getDescription();
	}
	public Result(String code,String message){
		this.code = code;
		this.message = message;
	}
	public Result(String code,String message,Object data){
		this(code,message);
		this.data = data;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	} 
	
	
}
