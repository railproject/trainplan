package org.railway.com.trainplan.entity;

/**
 * 交路字典
 * @author denglj
 *
 */
public class CrossDictInfo {

	private String drawGrapId;// 本表ID（绘制交路图ID）
	private String baseChartId;// 基本方案ID
	private String baseChartName;// 基本方案名
	private String baseCrossId;// 对数表交路ID
	private String crossName;// 车底交路名
	private String ljjm;// 路局局码
	private String ljjc;// 路局简称

	public String getDrawGrapId() {
		return drawGrapId;
	}

	public void setDrawGrapId(String drawGrapId) {
		this.drawGrapId = drawGrapId;
	}

	public String getBaseChartId() {
		return baseChartId;
	}

	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}

	public String getBaseChartName() {
		return baseChartName;
	}

	public void setBaseChartName(String baseChartName) {
		this.baseChartName = baseChartName;
	}

	public String getBaseCrossId() {
		return baseCrossId;
	}

	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}

	public String getCrossName() {
		return crossName;
	}

	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}

	public String getLjjm() {
		return ljjm;
	}

	public void setLjjm(String ljjm) {
		this.ljjm = ljjm;
	}

	public String getLjjc() {
		return ljjc;
	}

	public void setLjjc(String ljjc) {
		this.ljjc = ljjc;
	}

	
}
