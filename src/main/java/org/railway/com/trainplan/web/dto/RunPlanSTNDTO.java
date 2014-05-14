package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.text.SimpleDateFormat;
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

    private boolean owner;

    public RunPlanSTNDTO(Map<String, Object> map, String bureau) {
        SimpleDateFormat ff = new SimpleDateFormat("MM-dd hh:mm");
        this.id = MapUtils.getIntValue(map, "PLAN_TRAIN_STN_ID", 0);
        this.name = MapUtils.getString(map, "STN_NAME", "");
        java.sql.Timestamp dbArrDateTime = (java.sql.Timestamp)map.get("ARR_TIME");
        if(dbArrDateTime != null) {
            Date date = new Date(dbArrDateTime.getTime());
            this.arrDateTime = ff.format(date);
        }
        java.sql.Timestamp dbDptDateTime = (java.sql.Timestamp)map.get("DPT_TIME");
        if(dbDptDateTime != null) {
            Date date = new Date(dbDptDateTime.getTime());
            this.dptDateTime = ff.format(date);
        }
        this.trackName = MapUtils.getString(map, "TRACK_NAME", "");
        this.psg = MapUtils.getIntValue(map, "PSG_FLG", 1);
        this.owner = MapUtils.getString(map, "STN_BUREAU", "").equals(bureau);
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

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
