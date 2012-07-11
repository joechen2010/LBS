package cn.edu.nju.software.gof.beans.json;


public class BriefPlaceInfo extends JSONAble {

	private String ID;
	private String placeName;
	private Long currentMoney;

	public BriefPlaceInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BriefPlaceInfo(String iD, String placeName, Long currentMoney) {
		super();
		ID = iD;
		this.placeName = placeName;
		this.currentMoney = currentMoney;
	}
	
	

	public Long getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(Long currentMoney) {
		this.currentMoney = currentMoney;
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
