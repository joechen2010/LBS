package cn.edu.nju.software.gof.business;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cn.edu.nju.software.gof.beans.FriendSearchCondition;
import cn.edu.nju.software.gof.beans.json.BriefFriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendRequestInfo;
import cn.edu.nju.software.gof.beans.json.NearbyFriendInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.FriendRequest;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.gof.entity.Profile;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class FriendUtilities {

	private static final double RANGE = 1;

	public List<NearbyFriendInfo> getNearbyFriends(String sessionID,
			double latitude, double longitude) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				List<NearbyFriendInfo> results = new LinkedList<NearbyFriendInfo>();
				//
				List<Key> friendIDs = person.getFriendIDs();
				if (friendIDs.size() == 0) {
					return results;
				}
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date lastDay = calendar.getTime();

				String sqlCmd = "SELECT P FROM PersonalLocation AS P WHERE"
						+ " (P.ownerID IN (:friends))"
						+ " AND (P.time BETWEEN :lastDay AND :current)";
				Query query = em.createQuery(sqlCmd);
				query.setParameter("friends", friendIDs);
				query.setParameter("lastDay", lastDay);
				query.setParameter("current", new Date());
				@SuppressWarnings("unchecked")
				List<PersonalLocation> personalLocations = query
						.getResultList();
				for (PersonalLocation location : personalLocations) {
					double locationLatitude = location.getLatitude();
					double locationLongitude = location.getLongitude();
					if (locationLatitude >= latitude - RANGE
							&& locationLatitude <= latitude + RANGE
							&& locationLongitude >= longitude - RANGE
							&& locationLongitude <= longitude + RANGE) {
						Key friendID = location.getOwnerID();
						Person friend = location.getOwner(em);
						Profile friendProfile = friend.getProfile();
						String string_FriendID = KeyFactory
								.keyToString(friendID);
						String friendName = friendProfile.getRealName();
						double friendLatitude = location.getLatitude();
						double friendLongitude = location.getLongitude();
						Date updateTime = location.getTime();
						String time = getTimePast(updateTime);
						NearbyFriendInfo nearbyFriend = new NearbyFriendInfo(
								string_FriendID, friendName, friendLatitude,
								friendLongitude, time);
						results.add(nearbyFriend);
					}
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

	private String getTimePast(Date date) {
		Date currentDate = new Date();
		long delta = currentDate.getTime() - date.getTime();
		delta /= (1000 * 60);
		long hour = delta / 60;
		long minute = delta % 60;
		String result = hour + "h " + minute + "m";
		return result;
	}

	public List<FriendInfo> getFriendsList(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				List<FriendInfo> results = new LinkedList<FriendInfo>();
				// Get friend keys.
				List<Person> friends = person.getFriends(em);
				for (Person friend : friends) {
					Key friendID = friend.getID();
					Profile profile = friend.getProfile();
					String string_FriendID = KeyFactory.keyToString(friendID);
					String friendRealName = profile.getRealName();
					String ipAddress = null;
					String ipPort = null;
					String lastPersonalLocation = null;
					FriendInfo friendInfo = new FriendInfo(string_FriendID,
							friendRealName, null, ipAddress, ipPort,
							lastPersonalLocation);
					results.add(friendInfo);
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

	public ProfileInfo getFriendProfile(String sessionID, String string_FriendID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				try {
					Key friendID = KeyFactory.stringToKey(string_FriendID);
					Person friend = em.find(Person.class, friendID);
					if (friend == null
							|| !CommonUtilities.beFriend(person, friend, em)) {
						return null;
					} else {
						Profile friendProfile = friend.getProfile();
						String realName = friendProfile.getRealName();
						String school = friendProfile.getSchool();
						String place = friendProfile.getCurrentPlace();
						String birthday = friendProfile.getBirthday();
						ProfileInfo profileInfo = new ProfileInfo(realName,
								school, place, birthday);
						return profileInfo;
					}
				} catch (IllegalArgumentException exception) {
					return null;
				}
			}
		} finally {
			em.close();
		}
	}

	public byte[] getFriendAvatar(String sessionID, String string_FriendID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				try {
					Key friendID = KeyFactory.stringToKey(string_FriendID);
					Person friend = em.find(Person.class, friendID);
					if (friend == null) {
						return null;
					} else {
						Profile friendProfile = friend.getProfile();
						Blob avatar = friendProfile.getAvatar();
						if (avatar != null) {
							return avatar.getBytes();
						} else {
							return null;
						}
					}
				} catch (IllegalArgumentException exception) {
					return null;
				}
			}
		} finally {
			em.close();
		}
	}

	public Long getFriendAvatarCounter(String sessionID, String friendID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {

			try {
				Key friendKey = KeyFactory.stringToKey(friendID);
				Person friend = em.find(Person.class, friendKey);
				if (friend == null) {
					return null;
				} else {
					Profile friendProfile = friend.getProfile();
					return friendProfile.getCounter();
				}
			} catch (IllegalArgumentException exception) {
				return null;
			}
		} finally {
			em.close();
		}
	}

	public boolean sendFriendRequest(String sessionID, String string_FriendID) {
		Key friendID = null;
		Key personID = null;
		//
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				try {
					friendID = KeyFactory.stringToKey(string_FriendID);
					Person friend = em.find(Person.class, friendID);
					if (friend != null
							&& !CommonUtilities.beFriend(person, friend, em)) {
						personID = person.getID();
						// Construct the friend request.
						FriendRequest friendRequest = new FriendRequest(
								friendID, personID);
						// Add friend quest to the DB.
						em.persist(friendRequest);
					}
				} catch (IllegalArgumentException exception) {
					return false;
				}
			}
		} finally {
			em.close();
		}
		return true;
	}

	public List<FriendRequestInfo> getFriendRequests(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				List<FriendRequestInfo> result = new LinkedList<FriendRequestInfo>();
				//
				List<FriendRequest> requests = person.getFriendRequests(em);
				for (FriendRequest request : requests) {
					Person sourcePerson = request.getSourcePerson(em);
					Key sourcePersonID = sourcePerson.getID();
					Profile profile = sourcePerson.getProfile();
					Account account = sourcePerson.getAccount();
					String realName = profile.getRealName();
					String userName = account.getUserName();
					String requestID = KeyFactory.keyToString(request.getID());
					String requesterID = KeyFactory.keyToString(sourcePersonID);
					FriendRequestInfo bean = new FriendRequestInfo(userName,
							requestID, realName, requesterID);
					result.add(bean);
				}
				return result;
			}
		} finally {
			em.close();
		}
	}

	public boolean agreeFriendRequest(String sessionID, String string_RequestID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				try {
					Key requestID = KeyFactory.stringToKey(string_RequestID);
					FriendRequest request = em.find(FriendRequest.class,
							requestID);
					if (request != null) {
						Key personID = person.getID();
						Key targetPersonID = request.getTargetPersonID();
						if (targetPersonID.equals(personID)) {
							// Remove the friend request from the database.
							Key sourcePersonID = request.getSourcePersonID();
							Person sourcePerson = request.getSourcePerson(em);
							Person targetPerson = person;
							em.remove(request);
							// setup relationship between the two user.
							targetPerson.getFriendIDs().add(sourcePersonID);
							sourcePerson.getFriendIDs().add(targetPersonID);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} catch (IllegalArgumentException exception) {
					return false;
				}

			}
		} finally {
			em.close();
		}
	}

	public boolean rejectFriendRequest(String sessionID, String string_RequestID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				try {
					Key requestID = KeyFactory.stringToKey(string_RequestID);
					FriendRequest request = em.find(FriendRequest.class,
							requestID);
					if (request != null) {
						Key personID = person.getID();
						Key targetPersonID = request.getTargetPersonID();
						if (targetPersonID.equals(personID)) {
							// Remove the friend request from the database.
							em.remove(request);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} catch (IllegalArgumentException exception) {
					return false;
				}

			}
		} finally {
			em.close();
		}
	}

	public boolean deleteFriend(String sessionID, String string_FriendID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				try {
					Key friendID = KeyFactory.stringToKey(string_FriendID);
					Person friend = em.find(Person.class, friendID);
					if (friend != null
							&& CommonUtilities.beFriend(person, friend, em)) {
						Key personID = person.getID();
						person.getFriendIDs().remove(friendID);
						friend.getFriendIDs().remove(personID);
						return true;
					} else {
						return false;
					}
				} catch (IllegalArgumentException exception) {
					return false;
				}
			}
		} finally {
			em.close();
		}
	}

	public List<BriefFriendInfo> searchFriends(String sessionID,
			FriendSearchCondition condition) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person myself = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (myself == null) {
				return null;
			} else {
				List<BriefFriendInfo> results = new LinkedList<BriefFriendInfo>();
				//
				String userName = condition.getUserName();
				if (userName != null && !userName.equals("")) {
					Person resultPerson = CommonUtilities.getPersonByUserName(
							userName, em);
					if (resultPerson != null) {
						Key personID = resultPerson.getID();
						Profile profile = resultPerson.getProfile();
						BriefFriendInfo friendInfo = new BriefFriendInfo(
								KeyFactory.keyToString(personID), userName,
								profile.getRealName());
						results.add(friendInfo);
					}
					return results;

				} else {
					String realName = condition.getRealName();
					String place = condition.getPlace();
					String birthday = condition.getBirthday();
					String school = condition.getSchool();
					String sqlCmd = "SELECT P FROM Profile AS P WHERE";
					if (realName != null && !realName.trim().equals("")) {
						sqlCmd += " (P.realName = '" + realName + "') AND";
					}
					if (place != null && !place.trim().equals("")) {
						sqlCmd += " (P.currentPlace = '" + place + "') AND";
					}
					if (school != null && !school.trim().equals("")) {
						sqlCmd += " (P.school = '" + school + "') AND";
					}
					if (birthday != null && !birthday.trim().equals("")) {
						sqlCmd += " (P.birthday = '" + birthday + "') AND";
					}
					sqlCmd = sqlCmd.substring(0, sqlCmd.length() - 4);
					Query query = em.createQuery(sqlCmd);
					@SuppressWarnings("unchecked")
					List<Profile> friendProfiles = query.getResultList();
					for (Profile profile : friendProfiles) {
						Person person = profile.getOwner();
						Account account = person.getAccount();
						BriefFriendInfo info = new BriefFriendInfo(
								KeyFactory.keyToString(person.getID()),
								account.getUserName(), profile.getRealName());
						results.add(info);
					}
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

	public List<BriefFriendInfo> getRecommendFriends(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person myself = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (myself == null) {
				return null;
			} else {
				List<BriefFriendInfo> results = new LinkedList<BriefFriendInfo>();
				//
				Profile profile = myself.getProfile();
				// String place = profile.getCurrentPlace();
				String school = profile.getSchool();
				if (school != null && !school.trim().equals("")) {
					String sqlCmd = "SELECT P FROM Profile AS P WHERE (P.school = :school) AND P.ID <> :myID";
					Query query = em.createQuery(sqlCmd);
					query.setParameter("school", school);
					query.setParameter("myID", profile.getID());
					@SuppressWarnings("unchecked")
					List<Profile> profiles = query.getResultList();
					for (Profile friendProfile : profiles) {
						Person person = friendProfile.getOwner();
						Account account = person.getAccount();
						BriefFriendInfo info = new BriefFriendInfo(
								KeyFactory.keyToString(person.getID()),
								account.getUserName(),
								friendProfile.getRealName());
						results.add(info);
					}
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

}
