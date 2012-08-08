package cn.edu.nju.software.gof.entity;

import java.io.Serializable;


public class RichMan  implements Serializable{

	private static final Long START_MONEY = 1024L;

	private Long id;

	private Long personId;

	private Long money = START_MONEY;

	public RichMan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RichMan(Long personId) {
		super();
		this.personId = personId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

}
