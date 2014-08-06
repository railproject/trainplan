package org.railway.com.trainplan.entity;

/**
 * 表m_trainline的对象类
 * @author join
 *
 */
public class MTrainLine {

	private String id ;
	private String name ;
	private String resourceName ;
	private String typeName ;
	private String operation ;
	private String sourceBureauName ;
	private String sourceBureauShortname ;
	private String sourceNodeName ;
	private String targetNodeName ;
	private String targetBureauName;
	private String targetBureauShortname ;
	//途经局简称
	private String routeBureauShortname;
	private String sourceTime ;
	private String targetTime ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSourceBureauName() {
		return sourceBureauName;
	}
	public void setSourceBureauName(String sourceBureauName) {
		this.sourceBureauName = sourceBureauName;
	}
	public String getSourceBureauShortname() {
		return sourceBureauShortname;
	}
	public void setSourceBureauShortname(String sourceBureauShortname) {
		this.sourceBureauShortname = sourceBureauShortname;
	}
	public String getSourceNodeName() {
		return sourceNodeName;
	}
	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}
	public String getTargetNodeName() {
		return targetNodeName;
	}
	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}
	public String getTargetBureauName() {
		return targetBureauName;
	}
	public void setTargetBureauName(String targetBureauName) {
		this.targetBureauName = targetBureauName;
	}
	public String getTargetBureauShortname() {
		return targetBureauShortname;
	}
	public void setTargetBureauShortname(String targetBureauShortname) {
		this.targetBureauShortname = targetBureauShortname;
	}
	public String getRouteBureauShortname() {
		return routeBureauShortname;
	}
	public void setRouteBureauShortname(String routeBureauShortname) {
		this.routeBureauShortname = routeBureauShortname;
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
	
	
	

}
