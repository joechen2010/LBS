package cn.edu.nju.software.gof.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.json.CheckInfo;
import cn.edu.nju.software.gof.beans.json.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.entity.CheckIn;
import cn.edu.nju.software.gof.entity.CheckInCounter;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.gof.entity.Place;

public class CheckInUtilities extends BaseUtilities{

	public boolean updateLocation(String sessionID, double latitude,double longitude) {
		Long personID = null;
		String placeName = null;
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			personID = person.getId();
			String lastPersonalLocation = CommonUtilities.getPlaceNameByLL(latitude, longitude);
			if (lastPersonalLocation != null) {
				placeName = lastPersonalLocation;
				person.setLastPersonalLocation(lastPersonalLocation);
			} else {
				placeName = "XXXX";
			}
			// Get current system time.
			Date time = new Date();
			// Construct checkIn entity.
			PersonalLocation personalLocation = new PersonalLocation(person.getId(), latitude, longitude, time);
			personalLocationManager.save(personalLocation);
			person.setLastLocationId(personalLocation.getId());
		}
		SynchronizationUtilities utilities = new SynchronizationUtilities();
		utilities.doSynchronization(personID, placeName);
		return true;

	}

	public boolean checkInPlace(String sessionID, Long placeID) {
		Long personID = null;
		// Add the check in to DB.
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			personID = person.getId();
			CheckIn checkIn = new CheckIn(person.getId(), placeID, new Date());
			checkInManager.save(checkIn);
		}
		//
		// Increase the user's check in counter.
		int currentCounter = 0;
		currentCounter = person.increaseCheckInTimes(placeID);
		// Change the place's top user ID.
		Long topUserID = null;
		boolean topUserChanged = false;
		Place place = placeManager.findById(placeID);
		place.increaseCheckInTimes();
		Person topUser = place.getTopUser();
		if (topUser != null) {
			// The user is not the top user.
			if (!topUser.getId().equals(personID)) {
				topUserID = topUser.getId();
				//
				int topUserCounter = topUser.getCheckInTimes(placeID);
				if (currentCounter > topUserCounter) {
					place.setTopUserId(personID);
					topUserChanged = true;
				}
			}
		} else {
			place.setTopUserId(personID);
			topUserChanged = true;
		}
		//
		// Modify the topPlaceID list of each user.
		if (topUserChanged) {
			topUser = null;
			if (topUserID != null) {
				topUser = personManager.findById(topUserID);
			}
			person.getTopPlaceIds().add(placeID);
			if (topUser != null) {
				topUser.getTopPlaceIds().remove(placeID);
			}
		}
		//
		// RichMan
		RichManUtilities richManUtilities = new RichManUtilities();
		richManUtilities.adjustMoneyByCheckIn(placeID, personID);
		return true;
	}

	public List<CheckInfo> getFriendCheckIns(String sessionID,Long friendID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			Person friend = personManager.findById(friendID);
			if (!CommonUtilities.beFriend(person, friend)) {
				return null;
			} else {
				return getCheckIns(person.getId());
			}
		}
	}

	public List<CheckInfo> getPersonalCheckIns(String sessionID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			return getCheckIns(person.getId());
		}
	}

	private List<CheckInfo> getCheckIns(Long ownerID) {
		
		List<CheckInCounter> counters = checkInCounterManager.findByOwerId(ownerID);
		//
		List<CheckInfo> results = new LinkedList<CheckInfo>();
		//
		for (CheckInCounter counter : counters) {
			Place place = counter.getPlace();
			Long placeID = place.getId();
			String placeName = place.getPlaceName();
			int myCheckInTimes = counter.getCounter();
			CheckInfo info = new CheckInfo(placeID.toString(),placeName, myCheckInTimes);
			results.add(info);
		}
		return results;
	}

	public PlaceCheckInfomationBean getPlaceCheckIn(String sessionID, String placeID){
		return null;
	}
}
