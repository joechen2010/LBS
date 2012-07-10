package cn.edu.nju.software.gof.beans;

public class PlaceGeneral extends JSONTarget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1692437297546217295L;
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

	public PlaceGeneral(String ownerID, String placeName, Long currentMoney,
			Long checkInTimes, String ownerName) {
		super();
		this.ownerID = ownerID;
		this.placeName = placeName;
		this.currentMoney = currentMoney;
		this.checkInTimes = checkInTimes;
		this.ownerName = ownerName;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

}
