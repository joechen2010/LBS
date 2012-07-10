package controller;

import model.UserInfo;

/**
 * This class makes the UserInfo object type ready to be sent.
 */

public class SUserInfo extends UserInfo{
	public SUserInfo(UserInfo ui){
		setId(ui.getId());
		setNick(ui.getNick());
		setName(ui.getName());
		setSurname(ui.getSurname());
		setEmail(ui.getEmail());
		setPhone(ui.getPhone());
		setCountry(ui.getCountry());
		setAddress(ui.getAddress());
		setAdministrator(ui.isAdministrator());
		setPosition(ui.getPosition());
	}
}
