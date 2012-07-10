package cn.edu.nju.software.gof.beans.json;

public class PlaceGeneral extends JSONAble {

	private String ownerID;
	private String ownerName;
	private String placeName;
	private String placeDescription;
	private Long currentMoney;
	private Long checkInTimes;

	public PlaceGeneral() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaceGeneral(String ownerID, String ownerName, String placeName,
			Long currentMoney, Long checkInTimes, String placeDescription) {
		super();
		this.ownerID = ownerID;
		this.ownerName = ownerName;
		this.placeName = placeName;
		this.currentMoney = currentMoney;
		this.checkInTimes = checkInTimes;
		this.placeDescription = placeDescription;
	}

	public String getPlaceDescription() {
		return placeDescription;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Long getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(Long currentMoney) {
		this.currentMoney = currentMoney;
	}

	public Long getCheckInTimes() {
		return checkInTimes;
	}

	public void setCheckInTimes(Long checkInTimes) {
		this.checkInTimes = checkInTimes;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

}
