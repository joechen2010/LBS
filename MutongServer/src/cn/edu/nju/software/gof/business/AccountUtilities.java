package cn.edu.nju.software.gof.business;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.KeyFactory;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;

public class AccountUtilities {

	public boolean isUserExisted(String userName) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			return isUserExisted(userName, em);
		} finally {
			em.close();
		}
	}

	private boolean isUserExisted(String userName, EntityManager em) {
		Person person = CommonUtilities.getPersonByUserName(userName, em);
		return (person == null) ? false : true;
	}

	public String login(LoginInfo loginInfo) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			String userName = loginInfo.getUserName();
			Person person = CommonUtilities.getPersonByUserName(userName, em);
			if (person == null) {
				return null;
			} else {
				String password = loginInfo.getPassword();
				Account account = person.getAccount();
				if (account.getPassword().equals(password)) {
					String personID = KeyFactory.keyToString(person.getID());
					return account.getSessionID() + "#" + personID;
				} else {
					return null;
				}
			}
		} finally {
			em.close();
		}

	}

	public boolean register(String userName, String password,
			ProfileInfo profileInfo) {
		EntityManager em = EMF.getInstance().createEntityManager();
		Person person = null;
		try {
			if (isUserExisted(userName, em)) {
				return false;
			} else {
				person = new Person();
				// Generate session ID.
				String sessionID = UUID.randomUUID().toString();
				Account account = new Account(person, userName, password,
						sessionID);
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
				em.persist(person);
			}
		} finally {
			em.close();
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
}
