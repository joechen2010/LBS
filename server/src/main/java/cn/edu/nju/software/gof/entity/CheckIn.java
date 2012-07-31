package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.Date;

public class CheckIn  implements Serializable {

	private Long ID;

	private Long ownerID;

	private Long placeID;

	private Date time;

	public CheckIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckIn(Long ownerID, Long placeID, Date time) {
		super();
		this.ownerID = ownerID;
		this.placeID = placeID;
		this.time = time;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public Long getPlaceID() {
		return placeID;
	}

	public void setPlaceID(Long placeID) {
		this.placeID = placeID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
