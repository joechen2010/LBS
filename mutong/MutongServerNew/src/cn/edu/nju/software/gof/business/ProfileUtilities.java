package cn.edu.nju.software.gof.business;

import javax.persistence.EntityManager;

import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.gof.type.UserState;

import com.google.appengine.api.datastore.Blob;

public class ProfileUtilities {

	/**
	 * Get the profile information of the given user.
	 * 
	 * @param sessionID
	 *            The session ID of the user.
	 * @return The profile information.
	 */
	public ProfileInfo getUserProfile(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				Profile profile = person.getProfile();
				String realName = profile.getRealName();
				String place = profile.getCurrentPlace();
				String birthday = profile.getBirthday();
				String school = profile.getSchool();
				ProfileInfo profileInfo = new ProfileInfo(realName, school,
						place, birthday);
				return profileInfo;
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Change the user's profile.
	 * 
	 * @param sessionID
	 * @param profileInfo
	 * @return
	 */
	public boolean setUserProfile(String sessionID, ProfileInfo profileInfo) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				String realName = profileInfo.getRealName();
				String birthday = profileInfo.getBirthday();
				String school = profileInfo.getSchool();
				String place = profileInfo.getPlace();

				Profile profile = person.getProfile();
				if (realName != null) {
					profile.setRealName(realName);
				}
				if (birthday != null) {
					profile.setBirthday(birthday);
				}
				if (school != null) {
					profile.setSchool(school);
				}
				if (place != null) {
					profile.setCurrentPlace(place);
				}
				return true;
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Get the avatar of the user.
	 * 
	 * @param sessionID
	 * @return
	 */
	public byte[] getUserAvatar(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person != null) {
				Profile profile = person.getProfile();
				Blob avatar = profile.getAvatar();
				if (avatar != null) {
					return avatar.getBytes();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Change the user's avatar.
	 * 
	 * @param sessionID
	 * @param avatar
	 * @return
	 */
	public boolean setUserAvatar(String sessionID, byte[] avatar) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person != null) {
				Profile profile = person.getProfile();
				if (avatar != null) {
					Blob photo = new Blob(avatar);
					profile.setAvatar(photo);
					profile.increaseCounter();
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Change the user state.
	 * 
	 * @param sessionID
	 * @param userState
	 * @return
	 */
	public boolean changeOnlineState(String sessionID, UserState userState) {
		return true;
	}
}
