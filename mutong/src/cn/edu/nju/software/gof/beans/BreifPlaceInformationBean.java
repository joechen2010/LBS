package cn.edu.nju.software.gof.beans;

public class BreifPlaceInformationBean extends JSONTarget {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5934768847117785418L;
	private String ID;
	private String placeName;
	private Long currentMoney;

	public BreifPlaceInformationBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BreifPlaceInformationBean(String iD, String placeName, Long currntMoney) {
		super();
		ID = iD;
		this.placeName = placeName;
		this.currentMoney = currntMoney;
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
	
	public String toString(){
		return placeName;
	}
	
	public Long getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(Long currentMoney) {
		this.currentMoney = currentMoney;
	}
}
