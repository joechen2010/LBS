package cn.edu.nju.software.gof.beans.json;

import java.util.ArrayList;
import java.util.List;


public class PlaceInfo extends JSONAble {

	private String placeName;
	//
	private String topUserName;
	private String topUserRealName;
	//
	private int myCheckInTimes;

	private List<ReplyInfo> replies = new ArrayList<ReplyInfo>();

	private List<BriefPlaceInfo> subPlaces = new ArrayList<BriefPlaceInfo>();

	public PlaceInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceInfo(String placeName, String topUserName,
			String topUserRealName, int myCheckInTimes) {
		super();
		this.placeName = placeName;
		this.topUserName = topUserName;
		this.topUserRealName = topUserRealName;
		this.myCheckInTimes = myCheckInTimes;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public List<ReplyInfo> getReplies() {
		return replies;
	}

	public void setReplies(List<ReplyInfo> replies) {
		this.replies = replies;
	}

	public List<BriefPlaceInfo> getSubPlaces() {
		return subPlaces;
	}

	public void setSubPlaces(List<BriefPlaceInfo> subPlaces) {
		this.subPlaces = subPlaces;
	}

	public String getTopUserName() {
		return topUserName;
	}

	public void setTopUserName(String topUserName) {
		this.topUserName = topUserName;
	}

	public String getTopUserRealName() {
		return topUserRealName;
	}

	public void setTopUserRealName(String topUserRealName) {
		this.topUserRealName = topUserRealName;
	}

	public int getMyCheckInTimes() {
		return myCheckInTimes;
	}

	public void setMyCheckInTimes(int myCheckInTimes) {
		this.myCheckInTimes = myCheckInTimes;
	}

}
