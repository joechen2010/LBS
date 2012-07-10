package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

@Entity
public class FriendRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key targetPersonID;

	private Key sourcePersonID;

	public FriendRequest() {
		super();
	}

	public FriendRequest(Key targetPersonID, Key sourcePersonID) {
		super();
		this.targetPersonID = targetPersonID;
		this.sourcePersonID = sourcePersonID;
	}

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
		ID = iD;
	}

	public Key getTargetPersonID() {
		return targetPersonID;
	}

	public void setTargetPersonID(Key targetPersonID) {
		this.targetPersonID = targetPersonID;
	}

	public Key getSourcePersonID() {
		return sourcePersonID;
	}

	public void setSourcePersonID(Key sourcePersonID) {
		this.sourcePersonID = sourcePersonID;
	}

	public Person getSourcePerson(EntityManager em) {
		return getPersonByID(sourcePersonID, em);
	}

	public Person getTargetPerson(EntityManager em) {
		return getPersonByID(targetPersonID, em);
	}

	private Person getPersonByID(Key personID, EntityManager em) {
		return em.find(Person.class, personID);
	}

}
