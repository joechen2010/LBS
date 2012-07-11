package cn.edu.nju.software.gof.test.business;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.beans.json.PlaceInfo;
import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.business.CheckInUtilities;
import cn.edu.nju.software.gof.business.CommonUtilities;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.business.RichManUtilities;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.RichMan;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class CheckInTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	CheckInUtilities utilities;

	@Before
	public void setUp() {
		helper.setUp();
		utilities = new CheckInUtilities();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testUpdateLocation() {
		String userName = "lidejia";
		String password = "lidejia";
		boolean success = TestUtilities.registerUser("lidejia", "lidejia",
				"lidejia", "lidejia");
		assertEquals(true, success);
		String sessionID = TestUtilities.login(userName, password);
		double latitude = 38.45;
		double longitude = 118.54;
		success = utilities.updateLocation(sessionID, latitude, longitude);
		assertEquals(true, success);
		success = utilities.updateLocation(sessionID, latitude + 1,
				longitude + 1);
		assertEquals(true, success);
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			List<PersonalLocation> checkIns = person.getPersonalLocations(em);
			assertEquals(checkIns.size(), 2);
			PersonalLocation personalLocation = checkIns.get(0);
			double storeLatitude = personalLocation.getLatitude();
			success = (storeLatitude == latitude)
					|| (storeLatitude == latitude + 1);
			assertEquals(true, success);
		} finally {
			em.close();
		}
	}

	@Test
	public void testCheckIn() {
		String aUserName = "lidejia";
		String aPassword = "lidejia";
		String aRealName = "lidejia";
		String aBirthday = "1989-10-31";
		//
		String bUserName = "lijiahang";
		String bPassword = "lijiahang";
		String bRealName = "lijiahang";
		String bBirthday = "1989-09-13";
		//
		boolean success = TestUtilities.registerUser(aUserName, aPassword,
				aRealName, aBirthday);
		assertEquals(true, success);
		success = TestUtilities.registerUser(bUserName, bPassword, bRealName,
				bBirthday);
		assertEquals(true, success);
		//
		String aSessionID = TestUtilities.login(aUserName, aPassword);
		String bSessionID = TestUtilities.login(bUserName, bPassword);
		assertNotNull(aSessionID);
		assertNotNull(bSessionID);
		PlaceUtilities placeUtilities = new PlaceUtilities();
		PlaceCreation placeCreation = new PlaceCreation("NJU", 38.45, 118.54);
		success = placeUtilities.createPlace(aSessionID, placeCreation);
		assertEquals(true, success);
		placeCreation = new PlaceCreation("HLJU", 38.46, 118.53);
		success = placeUtilities.createPlace(aSessionID, placeCreation);
		assertEquals(true, success);
		List<NearbyPlaceInfo> nearbyPlaces = placeUtilities.getNearbyPlaces(
				aSessionID, 38.45, 118.50);
		assertEquals(2, nearbyPlaces.size());
		NearbyPlaceInfo nearbyPlace = nearbyPlaces.get(0);
		String placeID = nearbyPlace.getID();
		PlaceInfo place = placeUtilities.getPlaceInfo(aSessionID, placeID);
		assertEquals(null, place.getTopUserName());
		success = utilities.checkInPlace(aSessionID, placeID);
		assertEquals(true, success);
		RichManInfo richMan = RichManUtilities.getRichManInfo(aSessionID);
		assertEquals(new Long(1024 - 256 * 2 + 64), richMan.getMoney());
		place = placeUtilities.getPlaceInfo(aSessionID, placeID);
		assertEquals("lidejia", place.getTopUserRealName());
		success = utilities.checkInPlace(bSessionID, placeID);
		assertEquals(true, success);
		success = utilities.checkInPlace(bSessionID, placeID);
		assertEquals(true, success);
		success = utilities.checkInPlace(bSessionID, placeID);
		assertEquals(true, success);
		richMan = RichManUtilities.getRichManInfo(aSessionID);
		assertEquals(new Long(1024 - 256 * 2 + 64 + 16 * 3), richMan.getMoney());
		richMan = RichManUtilities.getRichManInfo(bSessionID);
		assertEquals(new Long(1024 + 64 * 3), richMan.getMoney());
		place = placeUtilities.getPlaceInfo(bSessionID, placeID);
		assertEquals("lijiahang", place.getTopUserRealName());
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person a = CommonUtilities.getPersonBySessionID(aSessionID, em);
			assertEquals(1,
					a.getCheckInTimes(KeyFactory.stringToKey(placeID), em));
			assertEquals(0, a.getTopPlaceIDs().size());
			Person b = CommonUtilities.getPersonBySessionID(bSessionID, em);
			assertEquals(3,
					b.getCheckInTimes(KeyFactory.stringToKey(placeID), em));
			assertEquals(1, b.getTopPlaceIDs().size());
			Key key = b.getTopPlaceIDs().get(0);
			assertEquals(placeID, KeyFactory.keyToString(key));
		} finally {
			em.close();
		}
	}

}
