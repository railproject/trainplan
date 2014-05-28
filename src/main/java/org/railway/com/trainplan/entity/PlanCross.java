

package org.railway.com.trainplan.entity;

import java.util.List;

/**
 * Created by speeder on 2014/5/28.
 */
public class PlanCross {

    private String planCrossId;

    private String unitCrossId;

    private int groupTotalNbr;

    private List<UnitCrossTrain> unitCrossTrainList;

    public String getPlanCrossId() {
        return planCrossId;
    }

    public int getGroupTotalNbr() {
        return groupTotalNbr;
    }

    public void setGroupTotalNbr(int groupTotalNbr) {
        this.groupTotalNbr = groupTotalNbr;
    }

    public void setPlanCrossId(String planCrossId) {
        this.planCrossId = planCrossId;
    }

    public String getUnitCrossId() {
        return unitCrossId;
    }

    public void setUnitCrossId(String unitCrossId) {
        this.unitCrossId = unitCrossId;
    }

    public List<UnitCrossTrain> getUnitCrossTrainList() {
        return unitCrossTrainList;
    }

    public void setUnitCrossTrainList(List<UnitCrossTrain> unitCrossTrainList) {
        this.unitCrossTrainList = unitCrossTrainList;
    }
}






















































































































































































































































