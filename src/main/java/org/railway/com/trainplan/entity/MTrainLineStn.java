package org.railway.com.trainplan.entity;

/**
 * 表M_TRAINLINE_SCHEDULE_RITEM，
 * M_TRAINLINE_SCHEDULE_SITEM，
 * M_TRAINLINE_SCHEDULE_TITEM
 * 的实体类
 * @author join
 *
 */
public class MTrainLineStn {

	private String id;
	private String name;
	private String parentId;
	private String parentName;
	private String bureauName;
	private String bureauShortname;
	private String trackName;
	private String sourceTime;
	private String targetTime;
	private String time;
	private Integer childIndex;
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	public String getBureauShortname() {
		return bureauShortname;
	}
	public void setBureauShortname(String bureauShortname) {
		this.bureauShortname = bureauShortname;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(Integer childIndex) {
		this.childIndex = childIndex;
	}

	
}
