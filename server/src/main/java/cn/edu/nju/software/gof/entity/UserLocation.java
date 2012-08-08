package cn.edu.nju.software.gof.entity;

import java.io.Serializable;

public class UserLocation implements Serializable {
	
    private static final long serialVersionUID = -6832688344476748908L;

	private Long id;

	private Person owner;

	private Long ownerId;

	private String address;
	
	private String city;
	
	private String street;

	private Double latitude;
	
	private Double longitude;
	
	private String mobile;
	
	private String timestamp;

	public UserLocation() {
		super();
	}

	public UserLocation(Long ownerId, String address, String city,
			String street, Double latitude, Double longitude, String mobile) {
		super();
		this.ownerId = ownerId;
		this.address = address;
		this.city = city;
		this.street = street;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mobile = mobile;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
