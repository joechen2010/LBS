package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;

public class Reply  implements Serializable{

	private Long ID;

	private Long ownerID;

	private String content;

	private Date date;

	public Reply() {
		super();
	}

	public Reply(Long ownerID, String content, Date date) {
		super();
		this.content = content;
		this.date = date;
		this.ownerID = ownerID;
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

	public Person getOwner(EntityManager em) {
		return em.find(Person.class, ownerID);
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
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
}
