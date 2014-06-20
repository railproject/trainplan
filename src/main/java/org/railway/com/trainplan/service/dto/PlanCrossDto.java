package org.railway.com.trainplan.service.dto;

public class PlanCrossDto {
	
	private String planCrossId;
	
	private String planCrossName;
	
	private String baseCrossId;
	
	private String tokenVehBureau;
	
	private String startBureau;
	//审核状态（0:未审核1:部分局审核2:途经局全部审核）
	private int checkType; 
    private String relevantBureau;
	
    
	public String getRelevantBureau() {
		return relevantBureau;
	}

	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}

	public int getCheckType() {
		return checkType;
	}

	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}

	public String getTokenVehBureau() {
		return tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

	public String getStartBureau() {
		return startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	public String getPlanCrossId() {
		return planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public String getPlanCrossName() {
		return planCrossName;
	}

	public void setPlanCrossName(String planCrossName) {
		this.planCrossName = planCrossName;
	}

	public String getBaseCrossId() {
		return baseCrossId;
	}

	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	} 

}
