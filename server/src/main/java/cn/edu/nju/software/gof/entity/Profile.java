package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.HashMap;


public class Profile  implements Serializable{
	
    private static final long serialVersionUID = -6822688344476748908L;

	private Long id;

	private Person owner;

	private String realName;

	private String birthday;

	private String school;

	private String currentPlace;

	private byte[] avatar;
	
	private HashMap fieldMap;

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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
}
