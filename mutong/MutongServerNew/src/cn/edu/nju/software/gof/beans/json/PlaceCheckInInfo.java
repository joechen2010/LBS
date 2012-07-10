package cn.edu.nju.software.gof.beans.json;

public class PlaceCheckInInfo extends JSONAble {

	private String topUserID;
	private String topUserName;
	private Integer myCheckInTimes;
	public PlaceCheckInInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PlaceCheckInInfo(String topUserID, String topUserName,
			Integer myCheckInTimes) {
		super();
		this.topUserID = topUserID;
		this.topUserName = topUserName;
		this.myCheckInTimes = myCheckInTimes;
	}
	public String getTopUserID() {
		return topUserID;
	}
	public void setTopUserID(String topUserID) {
		this.topUserID = topUserID;
	}
	public String getTopUserName() {
		return topUserName;
	}
	public void setTopUserName(String topUserName) {
		this.topUserName = topUserName;
	}
	public Integer getMyCheckInTimes() {
		return myCheckInTimes;
	}
	public void setMyCheckInTimes(Integer myCheckInTimes) {
		this.myCheckInTimes = myCheckInTimes;
	}
	
	

}
