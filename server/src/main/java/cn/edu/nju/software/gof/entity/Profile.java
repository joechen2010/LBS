package cn.edu.nju.software.gof.entity;

import com.mysql.jdbc.Blob;


public class Profile {

	private Long ID;

	private Person owner;

	private String realName;

	private String birthday;

	private String school;

	private String currentPlace;

	private Blob avatar;

	public Profile() {
		super();
	}

	public Profile(Person owner, String realName) {
		super();
		this.owner = owner;
		this.realName = realName;
	}

	public Profile(Person owner, String realName, String birthday,
			String school, String currentPlace) {
		super();
		this.owner = owner;
		this.realName = realName;
		this.birthday = birthday;
		this.school = school;
		this.currentPlace = currentPlace;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(String currentPlace) {
		this.currentPlace = currentPlace;
	}

	public Blob getAvatar() {
		return avatar;
	}

	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
}
