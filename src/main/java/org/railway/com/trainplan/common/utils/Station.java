package org.railway.com.trainplan.common.utils;

public class Station {
    //站名
	private String stnName;
	//离站时间
	private String dptTime;
	
	private Integer minites;
	private String stationType;
	public Station(){}
	public Station(String stnName,String dptTime,Integer minites){
		this(stnName,dptTime);
		this.minites = minites;
	}
	
	public Station(String stnName,String dptTime){
		this.stnName = stnName ;
		this.dptTime = dptTime ;
	}
	
	
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	public String getDptTime() {
		return dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	
	
	public Integer getMinites() {
		return minites;
	}

	public void setMinites(Integer minites) {
		this.minites = minites;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stnName == null) ? 0 : stnName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (stnName == null) {
			if (other.stnName != null)
				return false;
		} else if (!stnName.equals(other.stnName))
			return false;
		return true;
	} 
	
}
