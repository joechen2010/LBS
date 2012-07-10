package cn.edu.nju.software.gof.test.business;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.beans.json.BriefPlaceInfo;
import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.beans.json.PlaceInfo;
import cn.edu.nju.software.gof.beans.json.ReplyInfo;
import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.business.RichManUtilities;
import cn.edu.nju.software.gof.entity.RichMan;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PlaceTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	PlaceUtilities utilities;

	@Before
	public void setUp() {
		helper.setUp();
		utilities = new PlaceUtilities();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testGetNearbyPlaces() {

	}

	@Test
	public void testGetPlaceInfo() {

	}

	@Test
	public void testCreatePlace() {
		String userName = "lidejia";
		String password = "lidejia";
		String realName = "lidejia";
		boolean success = TestUtilities.registerUser(userName, password,
				realName, null);
		assertEquals(true, success);
		String sessionID = TestUtilities.login(userName, password);
		assertNotNull(sessionID);
		PlaceCreation placeCreation = new PlaceCreation("NJU", 38.45, 118.54);
		success = utilities.createPlace(sessionID, placeCreation);
		assertEquals(true, success);
		RichManInfo richMan = RichManUtilities.getRichManInfo(sessionID);
		assertEquals(new Long(1024 - 256), richMan.getMoney());
		placeCreation = new PlaceCreation("HLJU", 15.45, 119.54);
		success = utilities.createPlace(sessionID, placeCreation);
		assertEquals(true, success);
		richMan = RichManUtilities.getRichManInfo(sessionID);
		assertEquals(new Long(1024 - 256 * 2), richMan.getMoney());

		List<NearbyPlaceInfo> nearbyPlaces = utilities.getNearbyPlaces(
				sessionID, 38.45, 118.50);
		assertEquals(1, nearbyPlaces.size());
		NearbyPlaceInfo place = nearbyPlaces.get(0);
		assertEquals(place.getPalceName(), "NJU");
		assertEquals(place.getLatitude(), 38.45, 0.001);
		String placeID = place.getID();
		success = utilities.commandPlace(sessionID, placeID, "I love here");
		assertEquals(true, success);
		placeCreation = new PlaceCreation("Software Lab", placeID);
		success = utilities.createPlace(sessionID, placeCreation);
		assertEquals(true, success);
		nearbyPlaces = utilities.getNearbyPlaces(sessionID, 38.45, 118.50);
		assertEquals(nearbyPlaces.size(), 1);
		PlaceInfo placeInfo = utilities.getPlaceInfo(sessionID, placeID);
		assertEquals(0, placeInfo.getMyCheckInTimes());
		List<ReplyInfo> replies = placeInfo.getReplies();
		assertEquals(1, replies.size());
		ReplyInfo reply = replies.get(0);
		assertEquals("I love here", reply.getContent());
		assertEquals("lidejia", reply.getOwnerName());
		List<BriefPlaceInfo> subPlaces = placeInfo.getSubPlaces();
		assertEquals(1, subPlaces.size());
		BriefPlaceInfo subPlace = subPlaces.get(0);
		assertEquals(subPlace.getPlaceName(), "Software Lab");
	}

	@Test
	public void testCommandPlace() {
	}

}
