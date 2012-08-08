package cn.edu.nju.software.gof.gps;

public class MLocation {
	public String Access_token;

	public double Latitude;

	public double Longitude;

	public String Accuracy;

	public String Region;

	public String Street_number;

	public String Country_code;

	public String Street;

	public String City;

	public String Country;

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Access_token:" + Access_token + "\n");
		buffer.append("Region:" + Region + "\n");
		buffer.append("Accuracy:" + Accuracy + "\n");
		buffer.append("Latitude:" + Latitude + "\n");
		buffer.append("Longitude:" + Longitude + "\n");
		buffer.append("Country_code:" + Country_code + "\n");
		buffer.append("Country:" + Country + "\n");
		buffer.append("City:" + City + "\n");
		buffer.append("Street:" + Street + "\n");
		buffer.append("Street_number:" + Street_number + "\n");
		return buffer.toString();
	}
	
	public String getLocationString(){
		return this.getLatitude() + ":" + this.getLongitude() + "@" + this.getCountry()+this.getCity()+this.getStreet()+this.getStreet_number();
	}

	public String getAccess_token() {
		return Access_token == null ? "" : Access_token;
	}

	public void setAccess_token(String access_token) {
		Access_token = access_token;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public String getAccuracy() {
		return Accuracy == null ? "" : Accuracy;
	}

	public void setAccuracy(String accuracy) {
		Accuracy = accuracy;
	}

	public String getRegion() {
		return Region == null ? "" : Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getStreet_number() {
		return Street_number == null ? "" : Street_number;
	}

	public void setStreet_number(String street_number) {
		Street_number = street_number;
	}

	public String getCountry_code() {
		return Country_code == null ? "" : Country_code;
	}

	public void setCountry_code(String country_code) {
		Country_code = country_code;
	}

	public String getStreet() {
		return Street == null ? "" : Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCity() {
		return City == null ? "" : City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country  == null ? "" : Country;
	}

	public void setCountry(String country) {
		Country = country;
	}
	
	
}
