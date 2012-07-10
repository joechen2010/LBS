package cn.edu.nju.software.gof.beans;


public class FriendRequestBean extends JSONTarget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882316631786330359L;
	
	private String ID;
	private String userName;
	private String realName;
	private String requesterID;

	public FriendRequestBean() {
		super();
	}

	public FriendRequestBean(String userName, String ID, String realName,
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

	public void setID(String ID) {
		this.ID = ID;
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
