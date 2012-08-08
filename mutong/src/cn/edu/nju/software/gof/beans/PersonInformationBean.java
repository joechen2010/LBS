package cn.edu.nju.software.gof.beans;

public class PersonInformationBean extends JSONTarget{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4913090166721488838L;
	private String realName;
	private String birthday;
	private String school;
	private String place;
	private String mobile;
	
	public PersonInformationBean() {
		super();
	}
	
	public PersonInformationBean(String realName,
			String birthday, String school, String place,String mobile) {
		super();
		this.setRealName(realName);
		this.birthday = birthday;
		this.setSchool(school);
		this.setPlace(place);
		this.setMobile(mobile);
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealName() {
		return realName;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSchool() {
		return school;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return place;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
