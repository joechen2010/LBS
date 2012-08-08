package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.Date;

public class Reply  implements Serializable{

	private Long Id;

	private Long ownerId;

	private String content;

	private Date date;
	
	private Place place;
	
	private Person owner;

	public Reply() {
		super();
	}

	public Reply(Long ownerId, String content, Date date) {
		super();
		this.content = content;
		this.date = date;
		this.ownerId = ownerId;
	}


	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
	
	
}
