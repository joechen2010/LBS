package model;

/**
 * This class extends the UserInfo class adding a password field.
 */
public class User extends UserInfo{

	private String password;
	
	public User(){
		super();
	}
	
	public User(int id, String nick, String password,
			String name, String surname,
			String email, int phone, String country, String address,
			boolean isAdministrator){
		super(id, nick, name, surname, email, phone,
				country, address, isAdministrator);
		this.password = password;
		
	}
	
	public User(UserInfo ui) {
    	setId(ui.getId());
    	setNick(ui.getNick());
    	setAddress(ui.getAddress());
    	setCountry(ui.getCountry());
    	setEmail(ui.getEmail());
    	setName(ui.getName());
    	setAdministrator(ui.isAdministrator());
    	setPhone(ui.getPhone());
    	setSurname(ui.getSurname());
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}


}
