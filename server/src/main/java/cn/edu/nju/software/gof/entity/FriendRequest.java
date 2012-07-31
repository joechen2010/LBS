package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

public class FriendRequest {

	private Long ID;

	private Long targetPersonID;

	private Long sourcePersonID;

	public FriendRequest() {
		super();
	}

	public FriendRequest(Long targetPersonID, Long sourcePersonID) {
		super();
		this.targetPersonID = targetPersonID;
		this.sourcePersonID = sourcePersonID;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getTargetPersonID() {
		return targetPersonID;
	}

	public void setTargetPersonID(Long targetPersonID) {
		this.targetPersonID = targetPersonID;
	}

	public Long getSourcePersonID() {
		return sourcePersonID;
	}

	public void setSourcePersonID(Long sourcePersonID) {
		this.sourcePersonID = sourcePersonID;
	}

}
