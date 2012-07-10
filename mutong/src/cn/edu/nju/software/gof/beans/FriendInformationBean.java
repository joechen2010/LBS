package cn.edu.nju.software.gof.beans;

public class FriendInformationBean extends JSONTarget{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2001183045659554146L;
	private String friendID;
	private String friendRealName;
	private String friendState;
	private String ipAddress;
	private String ipPort;
	private String lastPersonalLocation;

	public FriendInformationBean() {
		super();
	}

	public FriendInformationBean(String friendID, String friendRealName,
			String friendState, String ipAddress, String ipPort,
			String lastPeronalLocation) {
		super();
		this.friendID = friendID;
		this.friendRealName = friendRealName;
		this.friendState = friendState;
		this.ipAddress = ipAddress;
		this.ipPort = ipPort;
		this.lastPersonalLocation = lastPeronalLocation;
	}

	public String getFriendID() {
		return friendID;
	}

	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}

	public String getFriendRealName() {
		return friendRealName;
	}

	public void setFriendRealName(String friendRealName) {
		this.friendRealName = friendRealName;
	}

	public String getFriendState() {
		return friendState;
	}

	public void setFriendState(String friendState) {
		this.friendState = friendState;
	}

	public String getLastCheckinLocation() {
		return lastPersonalLocation;
	}

	public void setLastCheckinLocation(String lastCheckinLocation) {
		this.lastPersonalLocation = lastCheckinLocation;
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
