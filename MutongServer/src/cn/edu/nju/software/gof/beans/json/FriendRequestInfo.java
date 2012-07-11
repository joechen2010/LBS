package cn.edu.nju.software.gof.beans.json;


public class FriendRequestInfo extends JSONAble {

	private String ID;
	private String userName;
	private String realName;
	private String requesterID;

	public FriendRequestInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendRequestInfo(String userName, String ID, String realName,
			String requesterID) {
		super();
		this.userName = userName;
		this.ID = ID;
		this.realName = realName;
		this.requesterID = requesterID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRequesterID() {
		return requesterID;
	}

	public void setRequesterID(String requesterID) {
		this.requesterID = requesterID;
	}
}
