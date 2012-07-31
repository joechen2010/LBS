package cn.edu.nju.software.gof.beans.json;

public class PlaceCheckInfomationBean extends JSONAble {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5250982739124667060L;

	private String topUserName, topUserID;
	private int myCheckinTimes;

	public PlaceCheckInfomationBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceCheckInfomationBean(String topUserName, String topUserID,
			int myCheckinTimes) {
		super();
		this.topUserName = topUserName;
		this.topUserID = topUserID;
		this.myCheckinTimes = myCheckinTimes;
	}

	public String getTopUserName() {
		return topUserName;
	}

	public void setTopUserName(String topUserName) {
		this.topUserName = topUserName;
	}

	public String getTopUserID() {
		return topUserID;
	}

	public void setTopUserID(String topUserID) {
		this.topUserID = topUserID;
	}

	public int getMyCheckinTimes() {
		return myCheckinTimes;
	}

	public void setMyCheckinTimes(int myCheckinTimes) {
		this.myCheckinTimes = myCheckinTimes;
	}

}
