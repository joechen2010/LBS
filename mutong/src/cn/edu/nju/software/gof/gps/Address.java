package cn.edu.nju.software.gof.gps;

public class Address {
	
	private String street_number;
    private String street;
    private String postal_code;
    private String city;
    private String county;
    private String region;
    private String country;
    private String country_code;
    
    
    
	public String getStreet_number() {
		return street_number == null ? "" : street_number;
	}
	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	public String getStreet() {
		return street == null ? "" : street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city == null ? "" : city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county == null ? "" : county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getRegion() {
		return region == null ? "" : region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCountry() {
		return country == null ? "" : country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getAddreStr() {
		return this.getCountry() + this.getRegion() + this.getCity() + this.getStreet() + this.getStreet_number() ;
	}
    
    
}
