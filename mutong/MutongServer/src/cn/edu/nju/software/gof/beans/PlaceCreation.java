package cn.edu.nju.software.gof.beans;

public class PlaceCreation {

	private String placeName;
	private double latitude;
	private double longitude;
	private String parentID;
	private byte[] images;

	public PlaceCreation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceCreation(String placeName, String parentID) {
		super();
		this.placeName = placeName;
		this.parentID = parentID;
	}

	public PlaceCreation(String placeName, double latitude, double longitude) {
		super();
		this.placeName = placeName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public byte[] getImages() {
		return images;
	}

	public void setImages(byte[] images) {
		this.images = images;
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
