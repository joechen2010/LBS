package cn.edu.nju.software.gof.beans;

import cn.edu.nju.software.gof.type.UserState;

public class LoginInfo {

	private String userName;
	private String password;
	private UserState userState;
	private String ipAddress;
	private String ipPort;

	public LoginInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginInfo(String userName, String password, UserState userState,
			String ipAddress, String ipPort) {
		super();
		this.userName = userName;
		this.password = password;
		this.userState = userState;
		this.ipAddress = ipAddress;
		this.ipPort = ipPort;
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

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

}
