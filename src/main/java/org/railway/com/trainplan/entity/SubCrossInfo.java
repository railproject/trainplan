package org.railway.com.trainplan.entity;

public class SubCrossInfo {
    private int rownumstart;
    private int rownumend;
	private int crossId ;
	private String crossName;
	public int getCrossId() {
		return crossId;
	}
	public void setCrossId(int crossId) {
		this.crossId = crossId;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public int getRownumstart() {
		return rownumstart;
	}
	public void setRownumstart(int rownumstart) {
		this.rownumstart = rownumstart;
	}
	public int getRownumend() {
		return rownumend;
	}
	public void setRownumend(int rownumend) {
		this.rownumend = rownumend;
	} 
	
	
}
