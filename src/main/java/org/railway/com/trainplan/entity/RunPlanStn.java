package org.railway.com.trainplan.entity;

import java.sql.Date;

/**
 * Created by speeder on 2014/5/28.
 */
public class RunPlanStn {

    private String planTrainStnId;
    private String planTrainId;
    private int stnSort;
    private String stnName;
    private String stnBureauShortName;
    private String stnBureauFullName;
    private String arrTrainNbr;
    private String dptTrainNbr;
    private Date arrTime;
    private Date dptTime;
    private Date baseArrTime;
    private Date baseDptTime;
    private String upDown;
    private String trackNbr;
    private String trackName;
    private int platform;
    private int psgFlag;
    private int locoFlag;
    private String tecType;
    private String stnType;
    private int boundaryInOut;
    private int runDays;

    public String getPlanTrainStnId() {
        return planTrainStnId;
    }

    public void setPlanTrainStnId(String planTrainStnId) {
        this.planTrainStnId = planTrainStnId;
    }

    public String getPlanTrainId() {
        return planTrainId;
    }

    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    public int getStnSort() {
        return stnSort;
    }

    public void setStnSort(int stnSort) {
        this.stnSort = stnSort;
    }

    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }

    public String getStnBureauShortName() {
        return stnBureauShortName;
    }

    public void setStnBureauShortName(String stnBureauShortName) {
        this.stnBureauShortName = stnBureauShortName;
    }

    public String getStnBureauFullName() {
        return stnBureauFullName;
    }

    public void setStnBureauFullName(String stnBureauFullName) {
        this.stnBureauFullName = stnBureauFullName;
    }

    public String getArrTrainNbr() {
        return arrTrainNbr;
    }

    public void setArrTrainNbr(String arrTrainNbr) {
        this.arrTrainNbr = arrTrainNbr;
    }

    public String getDptTrainNbr() {
        return dptTrainNbr;
    }

    public void setDptTrainNbr(String dptTrainNbr) {
        this.dptTrainNbr = dptTrainNbr;
    }

    public Date getArrTime() {
        return arrTime;
    }

    public void setArrTime(Date arrTime) {
        this.arrTime = arrTime;
    }

    public Date getDptTime() {
        return dptTime;
    }

    public void setDptTime(Date dptTime) {
        this.dptTime = dptTime;
    }

    public Date getBaseArrTime() {
        return baseArrTime;
    }

    public void setBaseArrTime(Date baseArrTime) {
        this.baseArrTime = baseArrTime;
    }

    public Date getBaseDptTime() {
        return baseDptTime;
    }

    public void setBaseDptTime(Date baseDptTime) {
        this.baseDptTime = baseDptTime;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getTrackNbr() {
        return trackNbr;
    }

    public void setTrackNbr(String trackNbr) {
        this.trackNbr = trackNbr;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getPsgFlag() {
        return psgFlag;
    }

    public void setPsgFlag(int psgFlag) {
        this.psgFlag = psgFlag;
    }

    public int getLocoFlag() {
        return locoFlag;
    }

    public void setLocoFlag(int locoFlag) {
        this.locoFlag = locoFlag;
    }

    public String getTecType() {
        return tecType;
    }

    public void setTecType(String tecType) {
        this.tecType = tecType;
    }

    public String getStnType() {
        return stnType;
    }

    public void setStnType(String stnType) {
        this.stnType = stnType;
    }

    public int getBoundaryInOut() {
        return boundaryInOut;
    }

    public void setBoundaryInOut(int boundaryInOut) {
        this.boundaryInOut = boundaryInOut;
    }

    public int getRunDays() {
        return runDays;
    }

    public void setRunDays(int runDays) {
        this.runDays = runDays;
    }
}
