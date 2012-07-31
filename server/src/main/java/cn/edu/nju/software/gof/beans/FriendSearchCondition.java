package cn.edu.nju.software.gof.beans;

public class FriendSearchCondition {

	private String userName;
	private String realName;
	private String place;
	private String school;
	private String birthday;
	public FriendSearchCondition() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FriendSearchCondition(String userName, String realName,
			String place, String school, String birthday) {
		super();
		this.userName = userName;
		this.realName = realName;
		this.place = place;
		this.school = school;
		this.birthday = birthday;
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
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
