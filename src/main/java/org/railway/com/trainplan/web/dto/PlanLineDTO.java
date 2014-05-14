package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/14/14.
 */
public class PlanLineDTO {
    private String trainName;
    private String startStn;
    private String endStn;
    private List<PlanLineSTNDTO> trainStns = new ArrayList<PlanLineSTNDTO>();

    public PlanLineDTO(Map<String, Object> map) {
        this.trainName = MapUtils.getString(map, "TRAIN_NBR", "");
        this.startStn = MapUtils.getString(map, "START_STN", "");
        this.endStn = MapUtils.getString(map, "END_STN", "");
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
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

    public List<PlanLineSTNDTO> getTrainStns() {
        return trainStns;
    }

    public void setTrainStns(List<PlanLineSTNDTO> trainStns) {
        this.trainStns = trainStns;
    }
}
