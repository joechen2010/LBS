package cn.edu.nju.software.gof.entity;

import java.util.Date;

public class PersonalLocation {

	private Long id;

	private Long ownerId;
	
	private Person owner;

	private Double latitude;

	private Double longitude;

	private Date time;

	public PersonalLocation() {
		super();
	}

	public PersonalLocation(Long ownerId, Double latitude, Double longitude,
			Date time) {
		super();
		this.ownerId = ownerId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
}
