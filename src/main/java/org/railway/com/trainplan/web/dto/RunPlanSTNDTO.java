package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
public class RunPlanSTNDTO {
    private int id;

    private String name;

    private String arrDateTime;

    private String dptDateTime;

    private String trackName;

    private int psg;

    public RunPlanSTNDTO(Map<String, Object> map) {
        this.id = MapUtils.getIntValue(map, "PLAN_TRAIN_STN_ID", 0);
        this.name = MapUtils.getString(map, "STN_NAME", "");
        java.sql.Timestamp dbArrDateTime = (java.sql.Timestamp)map.get("ARR_TIME");
        if(dbArrDateTime != null) {
            Date date = new Date(dbArrDateTime.getTime());
            LocalDate localDate = new LocalDate(date);
            this.arrDateTime = localDate.toString("MM-dd hh:mm");
        }
        java.sql.Timestamp dbDptDateTime = (java.sql.Timestamp)map.get("DPT_TIME");
        if(dbDptDateTime != null) {
            Date date = new Date(dbDptDateTime.getTime());
            LocalDate localDate = new LocalDate(date);
            this.dptDateTime = localDate.toString("MM-dd hh:mm");
        }
        this.trackName = MapUtils.getString(map, "TRACK_NAME", "");
        this.psg = MapUtils.getIntValue(map, "PSG_FLG", 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrDateTime() {
        return arrDateTime;
    }

    public void setArrDateTime(String arrDateTime) {
        this.arrDateTime = arrDateTime;
    }

    public String getDptDateTime() {
        return dptDateTime;
    }

    public void setDptDateTime(String dptDateTime) {
        this.dptDateTime = dptDateTime;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getPsg() {
        return psg;
    }

    public void setPsg(int psg) {
        this.psg = psg;
    }
}