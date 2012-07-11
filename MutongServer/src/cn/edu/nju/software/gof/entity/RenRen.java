package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class RenRen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key ownerID;

	private String userName;

	private String password;

	public RenRen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RenRen(Key ownerID, String userName, String password) {
		super();
		this.ownerID = ownerID;
		this.userName = userName;
		this.password = password;
	}

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
		ID = iD;
	}

	public Key getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Key ownerID) {
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
