package cn.edu.nju.software.gof.business;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cn.edu.nju.software.gof.beans.json.BriefPlaceInfo;
import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.RichMan;

public class RichManUtilities {

	private static final int CHECK_IN_CHECKER_MONEY = 64;
	private static final int CHECK_IN_OWNER_MONEY = 16;

	public static RichMan getRichManByPersonID(Key personID, EntityManager em) {
		String sqlCmd = "SELECT R FROM RichMan AS R WHERE R.personID = :personID";
		Query query = em.createQuery(sqlCmd);
		query.setParameter("personID", personID);
		RichMan richMan = (RichMan) query.getSingleResult();
		return richMan;
	}

	public static RichManInfo getRichManInfo(String sessionID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return null;
			} else {
				RichMan richMan = getRichManByPersonID(person.getID(), em);
				if (richMan != null) {
					Long money = richMan.getMoney();
					String sqlCmd = "SELECT P FROM Place AS P WHERE P.creatorID = :creatorID";
					Query query = em.createQuery(sqlCmd);
					query.setParameter("creatorID", person.getID());
					@SuppressWarnings("unchecked")
					List<Place> places = query.getResultList();
					RichManInfo result = new RichManInfo(money);
					for (Place place : places) {
						String placeName = place.getPlaceName();
						String placeID = KeyFactory.keyToString(place.getID());
						money = place.getCurrentMoney();
						result.addPlace(new BriefPlaceInfo(placeID, placeName,
								money));
					}
					return result;
				} else {
					return null;
				}
			}
		} finally {
			em.close();
		}
	}

	public static void adjustMoneyByCheckIn(Key placeID, Key personID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			RichMan richMan = getRichManByPersonID(personID, em);
			richMan.setMoney(richMan.getMoney() + CHECK_IN_CHECKER_MONEY);
			Key ownerID = em.find(Place.class, placeID).getCreatorID();
			if (!ownerID.equals(personID)) {
				richMan = getRichManByPersonID(ownerID, em);
				richMan.setMoney(richMan.getMoney() + CHECK_IN_OWNER_MONEY);
			}
		} finally {
			em.close();
		}
	}

	public static boolean initRichMan(Key personID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			RichMan richMan = new RichMan(personID);
			em.persist(richMan);
			return true;
		} finally {
			em.close();
		}
	}

	public static boolean buyExistedPlace(String sessionID, String placeID) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				Key placeKey = KeyFactory.stringToKey(placeID);
				Place place = em.find(Place.class, placeKey);
				// Is his owner place.
				if (person.getID().equals(place.getCreatorID())) {
					return false;
				} else {
					RichMan buyer = getRichManByPersonID(person.getID(), em);
					if (buyer.getMoney() >= place.getCurrentMoney()) {
						Long money = place.getCurrentMoney() * 2;
						place.setCurrentMoney(money);
						buyer.setMoney(buyer.getMoney() - money);
						//
						RichMan seler = getRichManByPersonID(
								place.getCreatorID(), em);
						seler.setMoney(seler.getMoney() + money);
						//
						place.setCreatorID(buyer.getPersonID());
						return true;
					} else {
						return false;
					}
				}
			}
		} finally {
			em.close();
		}
	}

}
