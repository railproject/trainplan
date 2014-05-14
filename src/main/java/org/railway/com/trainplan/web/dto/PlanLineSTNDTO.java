package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by star on 5/14/14.
 */
public class PlanLineSTNDTO {
    private int runDays;
    private String stnName;
    private String arrTime;
    private String dptTime;
    private int stayTime;

    public PlanLineSTNDTO(Map<String, Object> map) {
        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        this.runDays = MapUtils.getIntValue(map, "RUN_DAYS", 0);
        this.stnName = MapUtils.getString(map, "STN_NAME", "");
        java.sql.Timestamp dbArrDateTime = (java.sql.Timestamp)map.get("ARR_TIME");
        if(dbArrDateTime != null) {
            Date date = new Date(dbArrDateTime.getTime());
            this.arrTime = ff.format(date);
        }
        java.sql.Timestamp dbDptDateTime = (java.sql.Timestamp)map.get("DPT_TIME");
        if(dbDptDateTime != null) {
            Date date = new Date(dbDptDateTime.getTime());
            this.dptTime = ff.format(date);
        }
        this.stayTime = 0;
    }

    public PlanLineSTNDTO(Map<String, Object> map, int line) {
        this.stnName = MapUtils.getString(map, "name", "");
        this.arrTime = MapUtils.getString(map, "sourceTimeDto2", "");
        this.arrTime = this.arrTime.substring(0, 15);
        this.dptTime = MapUtils.getString(map, "targetTimeDto2", "");
        this.dptTime = this.dptTime.substring(0, 15);
    }

    public int getRunDays() {
        return runDays;
    }

    public void setRunDays(int runDays) {
        this.runDays = runDays;
    }

    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDptTime() {
        return dptTime;
    }

    public void setDptTime(String dptTime) {
        this.dptTime = dptTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }
}
