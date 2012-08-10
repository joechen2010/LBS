package cn.edu.nju.software.gof.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.beans.json.BriefPlaceInfo;
import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.beans.json.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.beans.json.PlaceGeneral;
import cn.edu.nju.software.gof.beans.json.PlaceInfo;
import cn.edu.nju.software.gof.beans.json.ReplyInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.gof.entity.Reply;
import cn.edu.nju.software.gof.entity.RichMan;

public class PlaceUtilities  extends BaseUtilities{

	public List<NearbyPlaceInfo> getNearbyPlaces(String sessionID,	double latitude, double longitude) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<Place> places = placeManager.findNearByPlaces(latitude - RANGE, latitude + RANGE);
			List<NearbyPlaceInfo> results = new LinkedList<NearbyPlaceInfo>();
			for (Place place : places) {
				if (place.getLongutide() >= longitude - RANGE
						&& place.getLongutide() <= longitude + RANGE) {
					Long placeID = place.getId();
					NearbyPlaceInfo nearby = new NearbyPlaceInfo(placeID,
							place.getPlaceName(), place.getLatitude(),
							place.getLongutide(), place.getCheckInTimes());
					results.add(nearby);
				}
			}
			return results;
		}
	}

	public PlaceGeneral getPlaceGeneralInfo(String sessionID, Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			// Find the place according to the place ID.
			Place place = placeManager.findById(placeID);
			Person owner = place.getCreator();
			Account account = owner.getAccount();
			String placeName = place.getPlaceName();
			Long currentMoney = place.getCurrentMoney();
			Long checkInTimes = place.getCheckInTimes();
			String ownerID = owner.getId().toString();
			return new PlaceGeneral(ownerID, account.getUserName(),
					placeName, currentMoney, checkInTimes);
		}
	}

	public List<BriefPlaceInfo> getSubPlaces(String sessionID, Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<BriefPlaceInfo> result = new LinkedList<BriefPlaceInfo>();
			// Find the place according to the place ID.
			Place place = placeManager.findById(placeID);
			List<Place> subPlaces = place.getSubPlaces();
			for (Place subPlace : subPlaces) {
				String placeName = subPlace.getPlaceName();
				result.add(new BriefPlaceInfo(subPlace.getId().toString(), placeName, subPlace.getCurrentMoney()));
			}
			return result;
		}
	}

	public List<ReplyInfo> getReplies(String sessionID, Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<ReplyInfo> results = new LinkedList<ReplyInfo>();
			List<Reply> replies = replyManager.findBypalceId(placeID);
			for (Reply reply : replies) {
				Person owner = reply.getOwner();
				String ownerID = owner.getId().toString();
				String ownerName = owner.getAccount().getUserName();
				String content = reply.getContent();
				Date time = reply.getDate();
				results.add(new ReplyInfo(ownerID, ownerName, content, time));
			}
			return results;
		}
	}

	public PlaceCheckInfomationBean getPlaceCheckIn(String sessionID,Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			// Find the place according to the place ID.
			Place place = placeManager.findById(placeID);
			String topUserName = null;
			Long topUserID = null;
			Person topUser = place.getTopUser();
			if (topUser != null) {
				Account topUserAccount = topUser.getAccount();
				topUserName = topUserAccount.getUserName();
				topUserID = topUser.getId();
			}
			Integer myCheckInTimes = person.getCheckInTimes(place.getId());
			if (myCheckInTimes == null) {
				myCheckInTimes = 0;
			}
			return new PlaceCheckInfomationBean(topUserName, topUserID.toString(),myCheckInTimes);
		}
	}

	public PlaceInfo getPlaceInfo(String sessionID, Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			try {
				Place place = placeManager.findById(placeID);
				String placeName = place.getPlaceName();
				String topUserName = null;
				String topUserRealName = null;
				Person topUser = place.getTopUser();
				if (topUser != null) {
					Profile topUserProfile = topUser.getProfile();
					Account topUserAccount = topUser.getAccount();
					topUserName = topUserAccount.getUserName();
					topUserRealName = topUserProfile.getRealName();
				}
				Integer myCheckInTimes = person.getCheckInTimes(placeID);
				if (myCheckInTimes == null) {
					myCheckInTimes = 0;
				}
				PlaceInfo placeInfo = new PlaceInfo(placeName, topUserName,
						topUserRealName, myCheckInTimes);
				// Get replies of the place.
				List<Reply> replies = place.getReplies();
				for (Reply reply : replies) {
					Person owner = reply.getOwner();
					Long ownerID = owner.getId();
					Profile profile = owner.getProfile();
					String ownerName = profile.getRealName();
					String content = reply.getContent();
					Date time = reply.getDate();
					ReplyInfo info = new ReplyInfo(ownerID.toString(), ownerName,content, time);
					placeInfo.getReplies().add(info);
				}
				// Get sub places of the place.
				List<Place> subPlaces = place.getSubPlaces();
				for (Place subPlace : subPlaces) {
					Long subPlaceID = subPlace.getId();
					String subPlaceName = subPlace.getPlaceName();
					BriefPlaceInfo info = new BriefPlaceInfo(subPlaceID.toString(),
							subPlaceName, subPlace.getCurrentMoney());
					placeInfo.getSubPlaces().add(info);
				}
				return placeInfo;
			} catch (IllegalArgumentException exception) {
				return null;
			}
		}
	}

	public byte[] getPlaceImage(String sessionID, Long placeID) {
		return placeManager.findById(placeID).getImage();
	}

	public boolean createPlace(String sessionID, PlaceCreation placeCreation) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			RichMan richMan = richManManager.findByPersonId(person.getId());
			if (richMan.getMoney() < Place.START_MONEY) {
				return false;
			} else {
				richMan.setMoney(richMan.getMoney() - Place.START_MONEY);
			}
		}
		Long personID = person.getId();
		String string_ParentID = placeCreation.getParentID();
		String placeName = placeCreation.getPlaceName();
		byte[] imageBytes = placeCreation.getImages();
		if (string_ParentID != null) {
			Place parentPlace = placeManager.findById(Long.valueOf(string_ParentID));
			if (parentPlace == null) {
				return false;
			} else {
				Place place = new Place(placeName, personID);
				place.setParentId(parentPlace.getId());
				parentPlace.getSubPlaces().add(place);
				if (imageBytes != null) {
					place.setImage(imageBytes);
				}
				placeManager.save(place);
			}
		} else {
			double latitude = placeCreation.getLatitude();
			double longitude = placeCreation.getLongitude();
			Place place = new Place(placeName, latitude, longitude,	personID);
			if (imageBytes != null) {
				place.setImage(imageBytes);
			}
			placeManager.save(place);
		}
		return true;
	}

	public boolean commandPlace(String sessionID, String string_PlaceID, String content) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			Place place = placeManager.findById(Long.valueOf(string_PlaceID));
			Reply reply = new Reply(person.getId(), content, new Date());
			place.getReplies().add(reply);
			replyManager.save(reply);
			return true;
		}
	}
}
