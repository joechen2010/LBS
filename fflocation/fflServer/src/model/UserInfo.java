package model;

/**
 * This class represents a user's information with it's own
 * id, nick, name, surname, email, phone, country, address and whether it is or not an administrator.
 * It has also a field to hold the user's last position.
 */
public class UserInfo {
	private int id;
	private String nick;
	private String name;
	private String surname;
	private String email;
	private int phone;
	private String country;
	private String address;
	private boolean isAdministrator;
	private Position lastPosition;
	
	public String toString(){
		return id + "/" + nick + ":" +
			"[" + name + " " + surname + "]" +
			"{" + email + " " + phone + "}" + 
			"(" + country + " " + address + ") " +
			"admin=" + isAdministrator + " ¿" + lastPosition + "?";
	}
	
	public UserInfo()
	{
		this.id = 0;
		this.nick = null;
		this.name = null;
		this.surname = null;
		this.email = null;
		this.phone = 0;
		this.country = null;
		this.address = null;
		this.isAdministrator = false;
		this.lastPosition=null;
	}
	
	public UserInfo(int id, String nick, 
			String name, String surname,
			String email, int phone, String country, String address,
			boolean isAdministrator)
	{
		
		this.id = id;
		this.nick = nick;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.country = country;
		this.address = address;
		this.isAdministrator = isAdministrator;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return nick;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSurname() {
		return surname;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public int getPhone() {
		return phone;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}
	public boolean isAdministrator() {
		return isAdministrator;
	}
	public Position getPosition() {
		return lastPosition;
	}
	public void setPosition(Position p) {
		lastPosition=p;
	}

}
