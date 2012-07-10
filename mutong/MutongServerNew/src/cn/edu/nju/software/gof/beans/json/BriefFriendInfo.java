package cn.edu.nju.software.gof.beans.json;


public class BriefFriendInfo extends JSONAble {

	private String ID;
	private String userName;
	private String realName;

	public BriefFriendInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BriefFriendInfo(String iD, String userName, String realName) {
		super();
		ID = iD;
		this.userName = userName;
		this.realName = realName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
