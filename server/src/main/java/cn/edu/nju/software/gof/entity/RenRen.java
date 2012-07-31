package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

public class RenRen {

	private Long ID;

	private Long ownerID;

	private String userName;

	private String password;

	public RenRen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RenRen(Long ownerID, String userName, String password) {
		super();
		this.ownerID = ownerID;
		this.userName = userName;
		this.password = password;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
