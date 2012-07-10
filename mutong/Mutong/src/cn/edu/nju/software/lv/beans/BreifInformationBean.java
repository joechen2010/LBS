package cn.edu.nju.software.lv.beans;

public class BreifInformationBean extends JSONTarget{

	private String ID;
	private String userName;
	private String realName;

	public BreifInformationBean() {
		super();
	}

	public BreifInformationBean(String iD, String userName, String realName) {
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
