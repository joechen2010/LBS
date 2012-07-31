package cn.edu.nju.software.gof.business;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.manager.AccountManager;
import cn.edu.nju.software.manager.PersonManager;
import cn.edu.nju.software.util.SpringContextHolder;

@Component
public class AccountUtilities {
	
	private AccountManager accountManager = SpringContextHolder.getBean("accountManager");
	private PersonManager personManager = SpringContextHolder.getBean("personManager");

	private boolean isUserExisted(String userName) {
		Account account = accountManager.findByName(userName);
		return (account == null) ? false : true;
	}

	public String login(LoginInfo loginInfo) {
		try {
			String userName = loginInfo.getUserName();
			Person person = accountManager.findByName(userName).getOwner();
			if (person == null) {
				return null;
			} else {
				String password = loginInfo.getPassword();
				Account account = person.getAccount();
				if (account.getPassword().equals(password)) {
					String personID = person.getID().toString();
					return account.getSessionID() + "#" + personID;
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
			Profile profile = new Profile(person, realName, birthday,
					school, place);
			//
			person.setAccount(account);
			person.setProfile(profile);
			//
			personManager.save(person);
		}
		return RichManUtilities.initRichMan(person.getID());

	}

	public boolean changePassword(String sessionID, String oldPassword,
			String newPassword) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
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
		} finally {
			em.close();
		}
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
