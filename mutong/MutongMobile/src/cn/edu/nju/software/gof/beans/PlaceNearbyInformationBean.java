package cn.edu.nju.software.gof.beans;

import com.google.android.maps.GeoPoint;

public class PlaceNearbyInformationBean extends JSONTarget {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6351562271703485866L;
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

	public GeoPoint getGeoPoint() {
		int latitudeE6 = new Double(latitude * 1E6).intValue();
		int longitudeE6 = new Double(longitude * 1E6).intValue();
		return new GeoPoint(latitudeE6, longitudeE6);
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

	@Override
	public String toString() {
		return palceName;
	}
}
