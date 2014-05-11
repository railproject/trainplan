package org.railway.com.trainplan.service.dto;
/**
 * 基本运行线中始发站，终到站以及经由站基本信息对象
 * @author join
 *
 */
public class TrainlineTemplateSubDto {
	 //站点名称
	private String name;
	//到站时间
	private String sourceTime;
	//出发时间
	private String targetTime;
	//站点类型1：始发站 2：经由站  3：终到站
	private String stationType;
	//局码
	private String stnBureau;
	//局码中文描述
	private String stnBureauFull;
	//局码中文描述
	//private String bureauName;
	
	private Integer index;
	//股道
	private String trackName;
	//图定到达时间
	private String baseArrTime;
	//图定出发时间
	private String baseDptTime;
	//运行天数
	private Integer runDays;
	
	
	public Integer getRunDays() {
		return runDays;
	}
	public void setRunDays(Integer runDays) {
		this.runDays = runDays;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getStnBureauFull() {
		return stnBureauFull;
	}
	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}
	public String getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}
	
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public String getStnBureau() {
		return stnBureau;
	}
	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
	}
	/*public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}*/
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getBaseArrTime() {
		return baseArrTime;
	}
	public void setBaseArrTime(String baseArrTime) {
		this.baseArrTime = baseArrTime;
	}
	public String getBaseDptTime() {
		return baseDptTime;
	}
	public void setBaseDptTime(String baseDptTime) {
		this.baseDptTime = baseDptTime;
	}
	
		
}