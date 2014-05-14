package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by star on 5/14/14.
 */
public class RunLineDTO {
    // 列车ID
    private String id;

    // 车次
    private String serial = "";

    // 始发站
    private String startSTN = "";

    // 终到站
    private String endSTN = "";

    //  开行日期 MM-dd
    private String runDate = "";

    // 客调
    private String dk = "";

    // 客调审核时间 MM-dd hh:mm
    private String kdauditDate = "";

    // 值班主任
    private String zbzr = "";

    // 值班主任审核时间 MM-dd hh:mm
    private String zbzrauditDate = "";

    public RunLineDTO(Map<String, Object> map) {
        this.id = MapUtils.getString(map, "id", "");
        this.serial = MapUtils.getString(map, "name", "");
        this.startSTN = MapUtils.getString(map, "sourceNodeName", "");
        this.endSTN = MapUtils.getString(map, "targetNodeName", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDk() {
        return dk;
    }

    public void setDk(String dk) {
        this.dk = dk;
    }

    public String getKdauditDate() {
        return kdauditDate;
    }

    public void setKdauditDate(String kdauditDate) {
        this.kdauditDate = kdauditDate;
    }

    public String getZbzr() {
        return zbzr;
    }

    public void setZbzr(String zbzr) {
        this.zbzr = zbzr;
    }

    public String getZbzrauditDate() {
        return zbzrauditDate;
    }

    public void setZbzrauditDate(String zbzrauditDate) {
        this.zbzrauditDate = zbzrauditDate;
    }
}
