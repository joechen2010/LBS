package cn.edu.nju.software.gof.beans.json;


public class BriefFriendInfo extends JSONAble {

	private Long id;
	private String userName;
	private String realName;

	public BriefFriendInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BriefFriendInfo(Long id, String userName, String realName) {
		super();
		this.id = id;
		this.userName = userName;
		this.realName = realName;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
