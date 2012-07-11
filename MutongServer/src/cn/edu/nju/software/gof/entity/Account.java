package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
	private Person owner;

	private String userName;

	private String password;

	private String sessionID;

	public Account() {
		super();
	}

	public Account(Person owner, String userName, String password,
			String sessionID) {
		super();
		this.owner = owner;
		this.userName = userName;
		this.password = password;
		this.sessionID = sessionID;
	}

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
		ID = iD;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
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

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
