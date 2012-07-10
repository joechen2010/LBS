package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class CheckInCounter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key ownerID;

	private Key placeID;

	private Integer counter;

	public CheckInCounter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckInCounter(Key ownerID, Key placeID, Integer counter) {
		super();
		this.ownerID = ownerID;
		this.placeID = placeID;
		this.counter = counter;
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
	
	public Place getPlace(EntityManager em) {
		return em.find(Place.class, placeID);
	}

	public void setPlaceID(Key placeID) {
		this.placeID = placeID;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	
}
