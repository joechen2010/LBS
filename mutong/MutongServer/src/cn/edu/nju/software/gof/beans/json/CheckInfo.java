package cn.edu.nju.software.gof.beans.json;


public class CheckInfo extends JSONAble{

	private String placeID;
	private String placeName;
	private int myCheckInTimes;
	public CheckInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CheckInfo(String placeID, String placeName, int myCheckInTimes) {
		super();
		this.placeID = placeID;
		this.placeName = placeName;
		this.myCheckInTimes = myCheckInTimes;
	}
	public String getPlaceID() {
		return placeID;
	}
	public void setPlaceID(String placeID) {
		this.placeID = placeID;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public int getMyCheckInTimes() {
		return myCheckInTimes;
	}
	public void setMyCheckInTimes(int myCheckInTimes) {
		this.myCheckInTimes = myCheckInTimes;
	}
}
