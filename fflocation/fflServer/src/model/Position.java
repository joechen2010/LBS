package model;

/**
 * This class represents a position with it's own latitude, longitude and date.
 */
public class Position{


	private String latitude;
	private String longitude;
	
	private String date;
	
	public String toString(){
		return date + "(" + latitude   + "," + longitude+ ")";
	}
	
	public Position(){
		latitude=null;
		longitude=null;
		date = null;
	}
	public Position(float latitude, float longitude, String dt)
	{
		this.latitude =  String.valueOf(latitude);
		this.longitude = String.valueOf(longitude);
		this.date = dt;
	}

	public void setLatitude(float latitude) {
		this.latitude = String.valueOf(latitude);
	}
	public void setLatitude(String latitude) {
		this.latitude = (latitude);
	}

	public String getLatitude() {
		return latitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = String.valueOf(longitude);
	}
	public void setLongitude(String longitude) {
		this.longitude = (longitude);
	}
	public String getLongitude() {
		return longitude;
	}
	public void setDate(String dt) {
		this.date = dt;
	}
	public String getDate() {
		return date;
	}
	
}
