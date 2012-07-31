package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

public class CheckInCounter {

	private Long ID;

	private Long ownerID;

	private Long placeID;

	private Integer counter;

	public CheckInCounter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckInCounter(Long ownerID, Long placeID, Integer counter) {
		super();
		this.ownerID = ownerID;
		this.placeID = placeID;
		this.counter = counter;
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

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	
}
