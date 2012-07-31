package cn.edu.nju.software.gof.beans.json;

public class NearbyFriendInfo extends JSONAble{
	
	private String friendID;
	private String friendName;
	private double latitude;
	private double longitude;
	private String time;
	public NearbyFriendInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NearbyFriendInfo(String friendID, String friendName, double latitude,
			double longitude, String time) {
		super();
		this.friendID = friendID;
		this.friendName = friendName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}
	public String getFriendID() {
		return friendID;
	}
	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
