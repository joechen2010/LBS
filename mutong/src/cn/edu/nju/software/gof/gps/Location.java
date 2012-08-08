package cn.edu.nju.software.gof.gps;

public class Location {
	
	private LocationDetail location;
	
	private String access_token;

	public LocationDetail getLocation() {
		return location;
	}

	public void setLocation(LocationDetail location) {
		this.location = location;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
}
