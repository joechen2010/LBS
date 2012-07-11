package cn.edu.nju.software.gof.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@Entity
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	@OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
	private Person owner;

	private String realName;

	private String birthday;

	private String school;

	private String currentPlace;

	@Basic(fetch = FetchType.LAZY)
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

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
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
