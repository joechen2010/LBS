package cn.edu.nju.software.gof.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class CheckIn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key ownerID;

	private Key placeID;

	private Date time;

	public CheckIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckIn(Key ownerID, Key placeID, Date time) {
		super();
		this.ownerID = ownerID;
		this.placeID = placeID;
		this.time = time;
	}

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
		ID = iD;
	}

	public Key getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Key ownerID) {
		this.ownerID = ownerID;
	}

	public Key getPlaceID() {
		return placeID;
	}

	public void setPlaceID(Key placeID) {
		this.placeID = placeID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
