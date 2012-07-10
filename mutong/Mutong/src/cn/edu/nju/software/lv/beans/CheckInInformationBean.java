package cn.edu.nju.software.lv.beans;

public class CheckInInformationBean extends JSONTarget{

	private String placeID;
	private String placeName;
	private int myCheckInTimes;

	public CheckInInformationBean() {
		super();
	}

	public CheckInInformationBean(String placeID, String placeName,
			int myCheckInTimes) {
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
