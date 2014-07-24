package org.railway.com.trainplan.entity;

import java.util.Date;

/**
 * 表cmd_train对应的实体类
 * @author join
 *
 */
public class CmdTrain {
	//临客命令列车ID
	private String cmdTrainId;
	//发令局 (发令局、担当局局码)
	private String cmdBureau;
	//命令类型 (加开；停运)
	private String cmdType;
	//命令主表ID
	private Integer cmdTxtMlId;
	//命令子表ID
	private Integer cmdTxtMlItemId;
    //路局命令号
	private String cmdNbrBureau;
	//项号
	private Integer cmdItem;
	//总公司命令号
	private String cmdNbrSuperior;
	//车次
	private String trainNbr;
	//始发站
	private String startStn;
	//终到站
	private String endStn;
	private String rule;
	//择日 (加开令填择日日期;停运令填停运日期)
	private Date selectedDate;
	//途径局 (按顺序列出途经局简称)
	private String passBureau;
	//选线状态
	private String selectState;
	//生成开行计划状态
	private String createState;
	//更新时间
	private String updateTime;
	//发布时间
	private String cmdTime;
	public String getCmdTrainId() {
		return cmdTrainId;
	}
	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}
	public String getCmdBureau() {
		return cmdBureau;
	}
	public void setCmdBureau(String cmdBureau) {
		this.cmdBureau = cmdBureau;
	}
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public Integer getCmdTxtMlId() {
		return cmdTxtMlId;
	}
	public void setCmdTxtMlId(Integer cmdTxtMlId) {
		this.cmdTxtMlId = cmdTxtMlId;
	}
	public Integer getCmdTxtMlItemId() {
		return cmdTxtMlItemId;
	}
	public void setCmdTxtMlItemId(Integer cmdTxtMlItemId) {
		this.cmdTxtMlItemId = cmdTxtMlItemId;
	}
	public String getCmdNbrBureau() {
		return cmdNbrBureau;
	}
	public void setCmdNbrBureau(String cmdNbrBureau) {
		this.cmdNbrBureau = cmdNbrBureau;
	}
	public Integer getCmdItem() {
		return cmdItem;
	}
	public void setCmdItem(Integer cmdItem) {
		this.cmdItem = cmdItem;
	}
	public String getCmdNbrSuperior() {
		return cmdNbrSuperior;
	}
	public void setCmdNbrSuperior(String cmdNbrSuperior) {
		this.cmdNbrSuperior = cmdNbrSuperior;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public Date getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getPassBureau() {
		return passBureau;
	}
	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}
	public String getSelectState() {
		return selectState;
	}
	public void setSelectState(String selectState) {
		this.selectState = selectState;
	}
	public String getCreateState() {
		return createState;
	}
	public void setCreateState(String createState) {
		this.createState = createState;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCmdTime() {
		return cmdTime;
	}
	public void setCmdTime(String cmdTime) {
		this.cmdTime = cmdTime;
	}

	
	
}
