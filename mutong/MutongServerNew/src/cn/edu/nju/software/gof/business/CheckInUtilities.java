package cn.edu.nju.software.gof.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cn.edu.nju.software.gof.beans.json.CheckInfo;
import cn.edu.nju.software.gof.beans.json.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.entity.CheckIn;
import cn.edu.nju.software.gof.entity.CheckInCounter;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Place;

public class CheckInUtilities {

	public boolean updateLocation(String sessionID, double latitude,
			double longitude) {
		Key personID = null;
		String placeName = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				personID = person.getID();
				String lastPersonalLocation = CommonUtilities.getPlaceNameByLL(
						latitude, longitude);
				if (lastPersonalLocation != null) {
					placeName = lastPersonalLocation;
					person.setLastPersonalLocation(lastPersonalLocation);
				} else {
					placeName = "XXXX";
				}

				// Get current system time.
				Date time = new Date();
				// Construct checkIn entity.
				PersonalLocation personalLocation = new PersonalLocation(
						person.getID(), latitude, longitude, time);
				em.persist(personalLocation);
			}
		} finally {
			em.close();
		}
		SynchronizationUtilities utilities = new SynchronizationUtilities();
		utilities.doSynchronization(personID, placeName);
		return true;

	}

	public boolean checkInPlace(String sessionID, String string_PlaceID) {
		Key placeID = null;
		Key personID = null;
		//
		// Add the check in to DB.
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				personID = person.getID();
				try {
					placeID = KeyFactory.stringToKey(string_PlaceID);
					CheckIn checkIn = new CheckIn(person.getID(), placeID,
							new Date());
					em.persist(checkIn);
					//
				} catch (IllegalArgumentException exception) {
					return false;
				}
			}
		} finally {
			em.close();
		}
		//
		// Increase the user's check in counter.
		int currentCounter = 0;
		em = EMF.getInstance().createEntityManager();
		try {
			Person person = em.find(Person.class, personID);
			currentCounter = person.increaseCheckInTimes(placeID, em);
		} finally {
			em.close();
		}
		// Change the place's top user ID.
		Key topUserID = null;
		boolean topUserChanged = false;
		em = EMF.getInstance().createEntityManager();
		try {
			Place place = em.find(Place.class, placeID);
			place.increaseCheckInTimes();
			//
			Person topUser = place.getTopUser(em);
			if (topUser != null) {
				// The user is not the top user.
				if (!topUser.getID().equals(personID)) {
					topUserID = topUser.getID();
					//
					int topUserCounter = topUser.getCheckInTimes(placeID, em);
					if (currentCounter > topUserCounter) {
						place.setTopUserID(personID);
						topUserChanged = true;
					}
				}
			} else {
				place.setTopUserID(personID);
				topUserChanged = true;
			}
		} finally {
			em.close();
		}
		//
		// Modify the topPlaceID list of each user.
		if (topUserChanged) {
			em = EMF.getInstance().createEntityManager();
			try {
				Person person = em.find(Person.class, personID);
				Person topUser = null;
				if (topUserID != null) {
					topUser = em.find(Person.class, topUserID);
				}
				//
				person.getTopPlaceIDs().add(placeID);
				if (topUser != null) {
					topUser.getTopPlaceIDs().remove(placeID);
				}
			} finally {
				em.close();
			}
		}
		//
		// RichMan
		RichManUtilities.adjustMoneyByCheckIn(placeID, personID);
		return true;
	}

	public List<CheckInfo> getFriendCheckIns(String sessionID,
			String string_FriendID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				try {
					Key friendID = KeyFactory.stringToKey(string_FriendID);
					Person friend = em.find(Person.class, friendID);
					if (!CommonUtilities.beFriend(person, friend, em)) {
						return null;
					} else {
						return getCheckIns(friend.getID(), em);
					}
				} catch (IllegalArgumentException exception) {
					return null;
				}
			}
		} finally {
			em.close();
		}
	}

	public List<CheckInfo> getPersonalCheckIns(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				return getCheckIns(person.getID(), em);
			}
		} finally {
			em.close();
		}
	}

	public List<CheckInfo> getMyTopCheckIns(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				String sqlCmd = "SELECT P FROM Place AS P WHERE P.topUserID = :topUserID";
				Query query = em.createQuery(sqlCmd);
				query.setParameter("topUserID", person.getID());
				@SuppressWarnings("unchecked")
				List<Place> places = query.getResultList();
				//
				List<CheckInfo> results = new LinkedList<CheckInfo>();
				for (Place place : places) {
					sqlCmd = "SELECT C FROM CheckInCounter AS C WHERE C.ownerID = :ownerID AND C.placeID = :placeID";
					query = em.createQuery(sqlCmd);
					query.setParameter("ownerID", person.getID());
					query.setParameter("placeID", place.getID());
					CheckInCounter counter = (CheckInCounter) query
							.getSingleResult();
					CheckInfo info = new CheckInfo(KeyFactory.keyToString(place
							.getID()), place.getPlaceName(),
							counter.getCounter());
					results.add(info);
				}
				return results;
			}
		} finally {
			em.close();
		}

	}

	private List<CheckInfo> getCheckIns(Key ownerID, EntityManager em) {
		String sqlCmd = "SELECT C FROM CheckInCounter AS C WHERE C.ownerID = :ownerID";
		Query query = em.createQuery(sqlCmd);
		query.setParameter("ownerID", ownerID);
		@SuppressWarnings("unchecked")
		List<CheckInCounter> counters = query.getResultList();
		//
		List<CheckInfo> results = new LinkedList<CheckInfo>();
		//
		for (CheckInCounter counter : counters) {
			Place place = counter.getPlace(em);
			Key placeID = place.getID();
			String placeName = place.getPlaceName();
			int myCheckInTimes = counter.getCounter();
			CheckInfo info = new CheckInfo(KeyFactory.keyToString(placeID),
					placeName, myCheckInTimes);
			results.add(info);
		}
		return results;
	}
}
