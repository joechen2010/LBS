package cn.edu.nju.software.gof.entity;

import java.io.Serializable;

public class Account implements Serializable {
	
    private static final long serialVersionUID = -6832688344476748908L;

	private Long id;

	private Person owner;

	private String userName;

	private String password;

	private String sessionId;

	public Account() {
		super();
	}

	public Account(Person owner, String userName, String password,
			String sessionId) {
		super();
		this.owner = owner;
		this.userName = userName;
		this.password = password;
		this.sessionId = sessionId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

}
