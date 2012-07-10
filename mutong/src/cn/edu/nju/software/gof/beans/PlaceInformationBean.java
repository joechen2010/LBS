package cn.edu.nju.software.gof.beans;

import java.util.ArrayList;
import java.util.List;

public class PlaceInformationBean extends JSONTarget{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6356467453061675224L;
	private String placeName;
	private String topUserName;
	private String topUserRealName;
	private int myCheckInTimes;

	private List<CommentBean> replies = new ArrayList<CommentBean>();

	private List<BreifPlaceInformationBean> subPlaces = 
		new ArrayList<BreifPlaceInformationBean>();

	public PlaceInformationBean() {
		this(null,null,null,0);
	}

	public PlaceInformationBean(String placeName, String topUserName,
			String topUserRealName, int myCheckInTimes) {
		this.placeName = placeName;
		this.topUserName = topUserName;
		this.topUserRealName = topUserRealName;
		this.myCheckInTimes = myCheckInTimes;
		replies.add(new CommentBean());
		subPlaces.add(new BreifPlaceInformationBean());
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public List<CommentBean> getReplies() {
		return replies;
	}

	public void setReplies(List<CommentBean> replies) {
		this.replies = replies;
	}

	public List<BreifPlaceInformationBean> getSubPlaces() {
		return subPlaces;
	}

	public void setSubPlaces(List<BreifPlaceInformationBean> subPlaces) {
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
