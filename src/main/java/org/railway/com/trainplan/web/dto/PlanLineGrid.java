package org.railway.com.trainplan.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 5/14/14.
 */
public class PlanLineGrid {
    private List<String> days = new ArrayList<String>();

    private List<PlanLineGridY> crossStns = new ArrayList<PlanLineGridY>();

    public PlanLineGrid(List<String> days, List<PlanLineGridY> crossStns) {
        this.days = days;
        this.crossStns = crossStns;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<PlanLineGridY> getCrossStns() {
        return crossStns;
    }

    public void setCrossStns(List<PlanLineGridY> crossStns) {
        this.crossStns = crossStns;
    }
}
