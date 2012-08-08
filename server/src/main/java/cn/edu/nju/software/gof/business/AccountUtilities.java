package cn.edu.nju.software.gof.business;

import java.util.UUID;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.manager.AccountManager;

@Component
public class AccountUtilities extends BaseUtilities {
	
	public boolean isUserExisted(String userName) {
		Account account = accountManager.findByName(userName);
		return (account == null) ? false : true;
	}

	public String login(LoginInfo loginInfo) {
		try {
			String userName = loginInfo.getUserName();
			Account account = accountManager.findByName(userName);
			if (account == null) {
				return null;
			} else {
				String password = loginInfo.getPassword();
				if (account.getPassword().equals(password)) {
					Long personID = account.getOwner().getId();
					return account.getSessionId() + "#" + personID;
				} else {
					return null;
				}
			}
		} finally {
		}

	}

	public boolean register(String userName, String password,
			ProfileInfo profileInfo) {
		Person person = null;
		if (isUserExisted(userName)) {
			return false;
		} else {
			person = new Person();
			// Generate session ID.
			String sessionID = UUID.randomUUID().toString();
			Account account = new Account(person, userName, password,sessionID);
			//
			String realName = profileInfo.getRealName();
			String birthday = profileInfo.getBirthday();
			String place = profileInfo.getPlace();
			String school = profileInfo.getSchool();
			String mobile = profileInfo.getMobile();
			Profile profile = new Profile(person,realName, birthday,
					school, place);
			//
			person.setAccount(account);
			person.setProfile(profile);
			person.setMobile(mobile);
			//
			personManager.save(person);
		}
		return richManUtilities.initRichMan(person.getId());

	}

	public boolean changePassword(String sessionID, String oldPassword,
			String newPassword) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			Account account = person.getAccount();
			if (account.getPassword().equals(oldPassword)) {
				account.setPassword(newPassword);
				return true;
			} else {
				return false;
			}
		}
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
