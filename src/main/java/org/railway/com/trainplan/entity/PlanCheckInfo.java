package org.railway.com.trainplan.entity;

/**
 * 对应表plan_check
 * @author join
 *
 */
public class PlanCheckInfo {

	private String  planCheckId;
	private String  planCrossId;
	private String  startDate;
	private String  endDate;
	private String  checkPeople;
	private String  checkTime;
	private String  checkDept;
	private String  checkBureau;
	public String getPlanCheckId() {
		return planCheckId;
	}
	public void setPlanCheckId(String planCheckId) {
		this.planCheckId = planCheckId;
	}
	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
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
	public String getCheckPeople() {
		return checkPeople;
	}
	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckDept() {
		return checkDept;
	}
	public void setCheckDept(String checkDept) {
		this.checkDept = checkDept;
	}
	public String getCheckBureau() {
		return checkBureau;
	}
	public void setCheckBureau(String checkBureau) {
		this.checkBureau = checkBureau;
	}
	
	
}
