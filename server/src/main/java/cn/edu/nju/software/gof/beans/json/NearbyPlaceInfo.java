package cn.edu.nju.software.gof.beans.json;

public class NearbyPlaceInfo extends JSONAble {

	private Long id;
	private String palceName;
	private double latitude;
	private double longitude;
	private Long checkInTimes;

	public NearbyPlaceInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NearbyPlaceInfo(Long id, String palceName, double latitude,
			double longitude, Long checkInTimes) {
		super();
		this.id = id;
		this.palceName = palceName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.checkInTimes = checkInTimes;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCheckInTimes() {
		return checkInTimes;
	}

	public void setCheckInTimes(Long checkInTimes) {
		this.checkInTimes = checkInTimes;
	}

	public String getPalceName() {
		return palceName;
	}

	public void setPalceName(String palceName) {
		this.palceName = palceName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
