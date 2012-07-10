package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class RichMan {

	private static final Long START_MONEY = 1024L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key ID;

	private Key personID;

	private Long money = START_MONEY;

	public RichMan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RichMan(Key personID) {
		super();
		this.personID = personID;
	}

	public Key getID() {
		return ID;
	}

	public void setID(Key iD) {
		ID = iD;
	}

	public Key getPersonID() {
		return personID;
	}

	public void setPersonID(Key personID) {
		this.personID = personID;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

}
