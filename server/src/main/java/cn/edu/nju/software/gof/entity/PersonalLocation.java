package cn.edu.nju.software.gof.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

public class PersonalLocation {

	private Long ID;

	private Long ownerID;

	private Double latitude;

	private Double longitude;

	private Date time;

	public PersonalLocation() {
		super();
	}

	public PersonalLocation(Long ownerID, Double latitude, Double longitude,
			Date time) {
		super();
		this.ownerID = ownerID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long ID) {
		this.ID = ID;
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public Person getOwner(EntityManager em) {
		return em.find(Person.class, ownerID);
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date checkInTime) {
		this.time = checkInTime;
	}
}
