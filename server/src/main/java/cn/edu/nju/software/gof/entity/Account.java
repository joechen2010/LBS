package cn.edu.nju.software.gof.entity;

import java.io.Serializable;

public class Account implements Serializable {
	
    private static final long serialVersionUID = -6832688344476748908L;

	private Long ID;

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

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
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
