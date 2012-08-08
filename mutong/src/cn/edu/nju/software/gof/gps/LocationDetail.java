package cn.edu.nju.software.gof.gps;

public class LocationDetail {
	
	private String latitude;
	private String longitude;
	private String altitude;
	private String accuracy;
	private String altitude_accuracy;
	private Address address;
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getAltitude_accuracy() {
		return altitude_accuracy;
	}
	public void setAltitude_accuracy(String altitude_accuracy) {
		this.altitude_accuracy = altitude_accuracy;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
