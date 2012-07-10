package cn.edu.nju.software.gof.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key ownerID;

	private String content;

	private Date date;

	public Reply() {
		super();
	}

	public Reply(Key ownerID, String content, Date date) {
		super();
		this.content = content;
		this.date = date;
		this.ownerID = ownerID;
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

	public Person getOwner(EntityManager em) {
		return em.find(Person.class, ownerID);
	}

	public void setOwnerID(Key ownerID) {
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
