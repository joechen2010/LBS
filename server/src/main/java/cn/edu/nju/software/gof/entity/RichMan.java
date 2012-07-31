package cn.edu.nju.software.gof.entity;

import java.io.Serializable;


public class RichMan  implements Serializable{

	private static final Long START_MONEY = 1024L;

	private Long ID;

	private Long personID;

	private Long money = START_MONEY;

	public RichMan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RichMan(Long personID) {
		super();
		this.personID = personID;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getPersonID() {
		return personID;
	}

	public void setPersonID(Long personID) {
		this.personID = personID;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

}
