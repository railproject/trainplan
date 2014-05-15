package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
public class RunPlanSTNDTO {
    private String id;

    private String name;

    private String arrDateTime;

    private String dptDateTime;

    private String trackName;

    private int psg;

    private boolean owner;

    public RunPlanSTNDTO(Map<String, Object> map, String bureau) {
        SimpleDateFormat ff = new SimpleDateFormat("MM-dd hh:mm");
        this.id = MapUtils.getString(map, "PLAN_TRAIN_STN_ID", "");
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

    public RunPlanSTNDTO(Map<String, Object> map, String bureau, int flag) {
        this.id = MapUtils.getString(map, "id", "");
        this.name = MapUtils.getString(map , "name", "");
        this.arrDateTime = MapUtils.getString(map, "sourceTimeDto2", "");
        if(this.arrDateTime.length() > 0) {
            this.arrDateTime = this.arrDateTime.substring(5, 16);
        }
        this.dptDateTime = MapUtils.getString(map , "targetTimeDto2", "");
        if(this.dptDateTime.length() > 0) {
            this.dptDateTime = this.dptDateTime.substring(5, 16);
        }
        this.trackName = MapUtils.getString(map, "trackName", "");
        this.owner = MapUtils.getString(map, "bureauShortName", "").equals(bureau);
    }

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
