package cn.edu.nju.software.lv.beans;

public class BreifPlaceInformationBean extends JSONTarget{

	private String ID;
	private String placeName;

	public BreifPlaceInformationBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BreifPlaceInformationBean(String iD, String placeName) {
		super();
		ID = iD;
		this.placeName = placeName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
}
