package cn.edu.nju.software.gof.entity;


public class RenRen {

	private Long id;

	private Long ownerId;
	
	private Person owner;

	private String userName;

	private String password;

	public RenRen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RenRen(Long ownerId, String userName, String password) {
		super();
		this.ownerId = ownerId;
		this.userName = userName;
		this.password = password;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	
	
}
