package cn.edu.nju.software.gof.beans;

public class PlaceCreationBean {

	private String placeName;
	private String placeDescription;
	private double latitude;
	private double longitude;
	private String parentID;
	private byte[] image;

	public PlaceCreationBean() {

	}

	public PlaceCreationBean(String placeName, String parentID) {
		this.placeName = placeName;
		this.parentID = parentID;
	}

	public PlaceCreationBean(String placeName, double latitude, double longitude) {
		this.placeName = placeName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getPlaceDescription() {
		return placeDescription;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
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

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
}
