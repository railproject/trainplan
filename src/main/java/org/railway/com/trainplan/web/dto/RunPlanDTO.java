package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Map;

/**
 * Created by star on 5/12/14.
 * 审核界面-客运开行计划dto
 */
public class RunPlanDTO {

    // 列车ID
    private int id;

    // 车次
    private String serial = "";

    // 始发站
    private String startSTN = "";

    // 终到站
    private String endSTN = "";

    //  开行日期 MM-dd
    private String runDate = "";

    // 命令号
    private String command = "";

    // 文电号
    private String tele = "";

    // 上图标记 0: yes, 1: no
    private int flag;

    // 上图时间 MM-dd hh:mm
    private String generateDateTime = "";

    public RunPlanDTO(Map<String, Object> map) {
        this.id = MapUtils.getIntValue(map, "PLAN_TRAIN_ID", 0);
        this.serial = MapUtils.getString(map, "TRAIN_NBR", "");
        this.startSTN = MapUtils.getString(map, "START_STN", "");
        if("null".equalsIgnoreCase(this.startSTN)) {
            this.startSTN = "";
        }
        this.endSTN = MapUtils.getString(map, "END_STN", "");
        if("null".equalsIgnoreCase(this.endSTN)) {
            this.endSTN = "";
        }
        String dbRunDate = MapUtils.getString(map, "RUN_DATE");
        if(dbRunDate != null) {
            LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(dbRunDate);
            this.runDate = runDate.toString("MM-dd");
        } else {
            this.runDate = "";
        }
        this.flag = MapUtils.getIntValue(map, "DAILYPLAN_FLAG", 1);
        java.sql.Date dbDate = (java.sql.Date)map.get("DAILYPLAN_TIME");
        if(dbDate != null) {
            Date date = new Date(dbDate.getTime());
            LocalDate localDate = new LocalDate(date);
            this.generateDateTime = localDate.toString("MM-dd hh:mm");
        } else {
            this.generateDateTime = "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStartSTN() {
        return startSTN;
    }

    public void setStartSTN(String startSTN) {
        this.startSTN = startSTN;
    }

    public String getEndSTN() {
        return endSTN;
    }

    public void setEndSTN(String endSTN) {
        this.endSTN = endSTN;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getGenerateDateTime() {
        return generateDateTime;
    }

    public void setGenerateDateTime(String generateDateTime) {
        this.generateDateTime = generateDateTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }
}
