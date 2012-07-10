package cn.edu.nju.software.gof.beans;

public class PlaceModification {

	private String placeName;
	private String placeDescription;
	private byte[] image;

	public PlaceModification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceModification(String placeName, String placeDescription,
			byte[] image) {
		super();
		this.placeName = placeName;
		this.placeDescription = placeDescription;
		this.image = image;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
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
}
