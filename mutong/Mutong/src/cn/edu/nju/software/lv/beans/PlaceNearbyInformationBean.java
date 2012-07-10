package cn.edu.nju.software.lv.beans;

public class PlaceNearbyInformationBean extends JSONTarget {

	private String ID;
	private String palceName;
	private double latitude;
	private double longitude;

	public PlaceNearbyInformationBean() {
		super();
	}

	public PlaceNearbyInformationBean(String ID, String palceName,
			double latitude, double longitude) {
		this.setPlaceID(ID);
		this.palceName = palceName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setPlaceID(String ID) {
		this.ID = ID;
	}

	public String getPlaceID() {
		return ID;
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
