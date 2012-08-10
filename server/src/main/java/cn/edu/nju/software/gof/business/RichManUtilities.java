package cn.edu.nju.software.gof.business;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.json.BriefPlaceInfo;
import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.RichMan;

public class RichManUtilities extends BaseUtilities{

	private static final int CHECK_IN_CHECKER_MONEY = 64;
	private static final int CHECK_IN_OWNER_MONEY = 16;

	public RichMan getRichManByPersonID(Long personID) {
		return richManManager.findByPersonId(personID);
	}

	public RichManInfo getRichManInfo(String sessionID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			RichMan richMan = getRichManByPersonID(person.getId());
			if (richMan != null) {
				Long money = richMan.getMoney();
				Place p = new Place();
				p.setCreatorId(person.getId());
				List<Place> places = placeManager.find(p);
				RichManInfo result = new RichManInfo(money);
				for (Place place : places) {
					String placeName = place.getPlaceName();
					Long placeID = place.getId();
					money = place.getCurrentMoney();
					result.addPlace(new BriefPlaceInfo(placeID.toString(), placeName,money));
				}
				return result;
			} else {
				return null;
			}
		}
	}

	public void adjustMoneyByCheckIn(Long placeID, Long personID) {
		RichMan richMan = getRichManByPersonID(personID);
		richMan.setMoney(richMan.getMoney() + CHECK_IN_CHECKER_MONEY);
		Long ownerID = this.placeManager.findById(placeID).getCreatorId();
		if (!ownerID.equals(personID)) {
			richMan = getRichManByPersonID(ownerID);
			richMan.setMoney(richMan.getMoney() + CHECK_IN_OWNER_MONEY);
		}
	}

	public boolean initRichMan(Long personID) {
		RichMan richMan = new RichMan(personID);
		richManManager.save(richMan);
		return true;
	}

	public boolean buyExistedPlace(String sessionID, Long placeID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			Place place = placeManager.findById(placeID);
			// Is his owner place.
			if (person.getId().equals(place.getCreatorId())) {
				return false;
			} else {
				RichMan buyer = getRichManByPersonID(person.getId());
				if (buyer.getMoney() >= place.getCurrentMoney()) {
					Long money = place.getCurrentMoney() * 2;
					place.setCurrentMoney(money);
					buyer.setMoney(buyer.getMoney() - money);
					//
					RichMan seler = getRichManByPersonID(place.getCreatorId());
					seler.setMoney(seler.getMoney() + money);
					place.setCreatorId(buyer.getPersonId());
					return true;
				} else {
					return false;
				}
			}
		}
	}

}
