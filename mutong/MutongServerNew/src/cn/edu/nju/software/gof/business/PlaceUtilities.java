package cn.edu.nju.software.gof.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.beans.PlaceModification;
import cn.edu.nju.software.gof.beans.json.BriefPlaceInfo;
import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.beans.json.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.beans.json.PlaceGeneral;
import cn.edu.nju.software.gof.beans.json.PlaceInfo;
import cn.edu.nju.software.gof.beans.json.ReplyInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.gof.entity.Reply;
import cn.edu.nju.software.gof.entity.RichMan;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PlaceUtilities {

	private static final double RANGE = 0.05;

	public List<NearbyPlaceInfo> getNearbyPlaces(String sessionID,
			double latitude, double longitude) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				String sqlCmd = "SELECT P FROM Place AS P WHERE (P.latitude BETWEEN :lowLatitude AND :highLatitude)";
				Query query = em.createQuery(sqlCmd);
				query.setParameter("lowLatitude", latitude - RANGE);
				query.setParameter("highLatitude", latitude + RANGE);
				@SuppressWarnings("unchecked")
				List<Place> places = query.getResultList();
				List<NearbyPlaceInfo> results = new LinkedList<NearbyPlaceInfo>();
				for (Place place : places) {
					if (place.getLongutide() >= longitude - RANGE
							&& place.getLongutide() <= longitude + RANGE) {
						Key placeID = place.getID();
						NearbyPlaceInfo nearby = new NearbyPlaceInfo(
								KeyFactory.keyToString(placeID),
								place.getPlaceName(), place.getLatitude(),
								place.getLongutide(), place.getCheckInTimes());
						results.add(nearby);
					}
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

	public PlaceGeneral getPlaceGeneralInfo(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				// Find the place according to the place ID.
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				//
				Key ownerKey = place.getCreatorID();
				Person owner = em.find(Person.class, ownerKey);
				Account account = owner.getAccount();
				//
				String placeName = place.getPlaceName();
				Long currentMoney = place.getCurrentMoney();
				Long checkInTimes = place.getCheckInTimes();
				String ownerID = KeyFactory.keyToString(ownerKey);
				String placeDescription = place.getPlaceDescription();

				return new PlaceGeneral(ownerID, account.getUserName(),
						placeName, currentMoney, checkInTimes, placeDescription);
			}
		} finally {
			em.close();
		}
	}

	public List<BriefPlaceInfo> getSubPlaces(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				List<BriefPlaceInfo> result = new LinkedList<BriefPlaceInfo>();
				// Find the place according to the place ID.
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				List<Place> subPlaces = place.getSubPlaces();
				for (Place subPlace : subPlaces) {
					String id = KeyFactory.keyToString(subPlace.getID());
					String placeName = subPlace.getPlaceName();
					result.add(new BriefPlaceInfo(id, placeName, subPlace
							.getCurrentMoney()));
				}
				return result;
			}
		} finally {
			em.close();
		}
	}

	public List<ReplyInfo> getReplies(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				List<ReplyInfo> results = new LinkedList<ReplyInfo>();
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				List<Reply> replies = place.getReplies();
				for (Reply reply : replies) {
					Person owner = reply.getOwner(em);
					String ownerID = KeyFactory.keyToString(owner.getID());
					String ownerName = owner.getAccount().getUserName();
					String content = reply.getContent();
					Date time = reply.getDate();
					results.add(new ReplyInfo(ownerID, ownerName, content, time));
				}
				return results;
			}
		} finally {
			em.close();
		}
	}

	public PlaceCheckInfomationBean getPlaceCheckIn(String sessionID,
			String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				// Find the place according to the place ID.
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				//
				String topUserName = null;
				String topUserID = null;
				Person topUser = place.getTopUser(em);
				if (topUser != null) {
					Account topUserAccount = topUser.getAccount();
					topUserName = topUserAccount.getUserName();
					topUserID = KeyFactory.keyToString(topUser.getID());
				}
				Integer myCheckInTimes = person.getCheckInTimes(placeKey, em);
				if (myCheckInTimes == null) {
					myCheckInTimes = 0;
				}
				return new PlaceCheckInfomationBean(topUserName, topUserID,
						myCheckInTimes);
			}
		} finally {
			em.close();
		}
	}

	public PlaceInfo getPlaceInfo(String sessionID, String string_PlaceID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				try {
					// Find the place according to the place ID.
					Key placeID = KeyFactory.stringToKey(string_PlaceID);
					Place place = em.find(Place.class, placeID);
					//
					String placeName = place.getPlaceName();
					String topUserName = null;
					String topUserRealName = null;
					Person topUser = place.getTopUser(em);
					if (topUser != null) {
						Profile topUserProfile = topUser.getProfile();
						Account topUserAccount = topUser.getAccount();
						topUserName = topUserAccount.getUserName();
						topUserRealName = topUserProfile.getRealName();
					}
					Integer myCheckInTimes = person
							.getCheckInTimes(placeID, em);
					if (myCheckInTimes == null) {
						myCheckInTimes = 0;
					}
					PlaceInfo placeInfo = new PlaceInfo(placeName, topUserName,
							topUserRealName, myCheckInTimes);
					// Get replies of the place.
					List<Reply> replies = place.getReplies();
					for (Reply reply : replies) {
						Person owner = reply.getOwner(em);
						Key ownerID = owner.getID();
						Profile profile = owner.getProfile();
						String ownerName = profile.getRealName();
						String content = reply.getContent();
						Date time = reply.getDate();
						ReplyInfo info = new ReplyInfo(
								KeyFactory.keyToString(ownerID), ownerName,
								content, time);
						placeInfo.getReplies().add(info);
					}
					// Get sub places of the place.
					List<Place> subPlaces = place.getSubPlaces();
					for (Place subPlace : subPlaces) {
						Key subPlaceID = subPlace.getID();
						String subPlaceName = subPlace.getPlaceName();
						BriefPlaceInfo info = new BriefPlaceInfo(
								KeyFactory.keyToString(subPlaceID),
								subPlaceName, subPlace.getCurrentMoney());
						placeInfo.getSubPlaces().add(info);
					}
					return placeInfo;
				} catch (IllegalArgumentException exception) {
					return null;
				}
			}
		} finally {
			em.close();
		}
	}

	public byte[] getPlaceImage(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Key placeKey = KeyFactory.stringToKey(placeID);
			Place place = em.find(Place.class, placeKey);
			Blob image = place.getImage();
			return image == null ? null : image.getBytes();
		} finally {
			em.close();
		}
	}

	public Long getPlaceImageCounter(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {

			try {
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				if (place == null) {
					return null;
				} else {
					return place.getCounter();
				}
			} catch (IllegalArgumentException exception) {
				return null;
			}
		} finally {
			em.close();
		}
	}

	public boolean modifyPlace(String sessionID, String placeID,
			PlaceModification modification) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			}
			Key placeKey = KeyFactory.stringToKey(placeID);
			Place place = em.find(Place.class, placeKey);
			if (!place.getCreatorID().equals(person.getID())) {
				return false;
			}
			String placeName = modification.getPlaceName();
			String placeDescription = modification.getPlaceDescription();
			byte[] image = modification.getImage();
			if (placeName != null && !placeName.equals("")) {
				place.setPlaceName(placeName);
			}
			if (placeDescription != null && !placeDescription.equals("")) {
				place.setPlaceDescription(placeDescription);
			}
			if (image != null) {
				place.setImage(new Blob(image));
				place.increaseCounter();
			}
			return true;
		} finally {
			em.close();
		}

	}

	public boolean createPlace(String sessionID, PlaceCreation placeCreation) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				RichMan richMan = RichManUtilities.getRichManByPersonID(
						person.getID(), em);
				if (richMan.getMoney() < Place.START_MONEY) {
					return false;
				} else {
					richMan.setMoney(richMan.getMoney() - Place.START_MONEY);
				}
			}
		} finally {
			em.close();
		}

		em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				Key personID = person.getID();
				String string_ParentID = placeCreation.getParentID();
				String placeName = placeCreation.getPlaceName();
				byte[] imageBytes = placeCreation.getImages();
				String placeDescription = placeCreation.getPlaceDescription();
				if (string_ParentID != null) {
					try {
						Key parentID = KeyFactory.stringToKey(string_ParentID);
						Place parentPlace = em.find(Place.class, parentID);
						if (parentPlace == null) {
							return false;
						} else {
							Place place = new Place(placeName, personID);
							parentPlace.getSubPlaces().add(place);
							if (imageBytes != null) {
								Blob image = new Blob(imageBytes);
								place.setImage(image);
							}
							place.setPlaceDescription(placeDescription);
							em.persist(place);
						}
					} catch (IllegalArgumentException exception) {
						return false;
					}
				} else {
					double latitude = placeCreation.getLatitude();
					double longitude = placeCreation.getLongitude();
					Place place = new Place(placeName, latitude, longitude,
							personID);
					if (imageBytes != null) {
						Blob image = new Blob(imageBytes);
						place.setImage(image);
					}
					place.setPlaceDescription(placeDescription);
					em.persist(place);
				}
				return true;
			}
		} finally {
			em.close();
		}
	}

	public boolean commandPlace(String sessionID, String string_PlaceID,
			String content) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				try {
					Key placeID = KeyFactory.stringToKey(string_PlaceID);
					Place place = em.find(Place.class, placeID);
					Reply reply = new Reply(person.getID(), content, new Date());
					place.getReplies().add(reply);
					em.persist(reply);
					return true;
				} catch (IllegalArgumentException exception) {
					return false;
				}
			}
		} finally {
			em.close();
		}
	}
}
