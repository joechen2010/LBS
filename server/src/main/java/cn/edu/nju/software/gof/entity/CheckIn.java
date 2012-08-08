package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.Date;

public class CheckIn  implements Serializable {
	
    private static final long serialVersionUID = -6832688344476738908L;

	private Long id;

	private Long ownerId;

	private Long placeId;
	
	private Person owner;
	
	private Place place;

	private Date time;

	public CheckIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckIn(Long ownerId, Long placeId, Date time) {
		super();
		this.ownerId = ownerId;
		this.placeId = placeId;
		this.time = time;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
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

	public Long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
