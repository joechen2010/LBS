package cn.edu.nju.software.gof.entity;

import java.io.Serializable;


public class CheckInCounter implements Serializable {
	
    private static final long serialVersionUID = -6832688364476748908L;

	private Long id;

	private Long ownerId;

	private Long placeId;
	
	private Person owner;
	
	private Place place;

	private Integer counter;

	public CheckInCounter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckInCounter(Long ownerId, Long placeId, Integer counter) {
		super();
		this.ownerId = ownerId;
		this.placeId = placeId;
		this.counter = counter;
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

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
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
	
	
}
