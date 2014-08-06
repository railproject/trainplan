package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by star on 5/22/14.
 */
public class PlanLineInfoDto {

    private String trainName;

    private String startBureau;

    private String endBureau;

    private String startStn;

    private String endStn;

    private String startTime;

    private String endTime;

    private String  dailyPlanId;
    private String planTrainId;
    private String passBureau;
    private String endBureauFull;
    private String startBureauFull;
   
    
    public PlanLineInfoDto(Map<String, Object> map) {
        this.trainName = MapUtils.getString(map, "TRAIN_NAME");
        this.startBureau = MapUtils.getString(map, "START_BUREAU");
        this.endBureau = MapUtils.getString(map, "END_BUREAU");
        this.startStn = MapUtils.getString(map, "START_STN");
        this.endStn = MapUtils.getString(map, "END_STN");
        this.startTime = MapUtils.getString(map, "START_TIME");
        this.endTime = MapUtils.getString(map, "END_TIME");
        this.dailyPlanId = MapUtils.getString(map, "DAILYPLAN_ID");
        this.planTrainId = MapUtils.getString(map, "PLAN_TRAIN_ID");
        this.passBureau = MapUtils.getString(map, "PASS_BUREAU");
        this.endBureauFull = MapUtils.getString(map, "END_BUREAU_FULL");
        this.startBureauFull = MapUtils.getString(map, "START_BUREAU_FULL");
    }

    
    public String getPassBureau() {
		return passBureau;
	}


	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}


	public String getEndBureauFull() {
		return endBureauFull;
	}


	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}


	public String getStartBureauFull() {
		return startBureauFull;
	}


	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}


	public String getDailyPlanId() {
		return dailyPlanId;
	}

	public void setDailyPlanId(String dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}

	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStartBureau() {
        return startBureau;
    }

    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
    }

    public String getEndBureau() {
        return endBureau;
    }

    public void setEndBureau(String endBureau) {
        this.endBureau = endBureau;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
