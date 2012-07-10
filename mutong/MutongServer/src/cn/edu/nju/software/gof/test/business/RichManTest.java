package cn.edu.nju.software.gof.test.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.beans.json.PlaceGeneral;
import cn.edu.nju.software.gof.beans.json.RichManInfo;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.business.RichManUtilities;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class RichManTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testGetRichManInfo() {
		String userName = "lidejia";
		String password = "lidejia";
		boolean success = TestUtilities.registerUser("lidejia", "lidejia",
				"lidejia", "lidejia");
		assertEquals(true, success);
		String sessionID = TestUtilities.login(userName, password);
		assertNotNull(sessionID);
		RichManInfo info = RichManUtilities.getRichManInfo(sessionID);
		assertEquals(new Long(1024), info.getMoney());
	}

	@Test
	public void testBuyExistedPlace() {
		PlaceUtilities utilities = new PlaceUtilities();
		//
		String userName = "lidejia";
		String password = "lidejia";
		boolean success = TestUtilities.registerUser("lidejia", "lidejia",
				"lidejia", "lidejia");
		assertEquals(true, success);
		String asessionID = TestUtilities.login(userName, password);
		assertNotNull(asessionID);
		//
		userName = "lijiahang";
		password = "lijiahang";
		success = TestUtilities.registerUser("lijiahang", "lijiahang", "lidejia",
				"lidejia");
		assertEquals(true, success);
		String bsessionID = TestUtilities.login(userName, password);
		assertNotNull(bsessionID);
		PlaceCreation placeCreation = new PlaceCreation("NJU", 38.45, 118.54);
		success = utilities.createPlace(asessionID, placeCreation);
		assertEquals(true, success);
		RichManInfo richMan = RichManUtilities.getRichManInfo(asessionID);
		assertEquals(new Long(1024 - 256), richMan.getMoney());
		//
		List<NearbyPlaceInfo> nearbyPlaces = utilities.getNearbyPlaces(
				asessionID, 38.45, 118.50);
		assertEquals(1, nearbyPlaces.size());
		NearbyPlaceInfo place = nearbyPlaces.get(0);
		success = RichManUtilities.buyExistedPlace(bsessionID, place.getID());
		assertEquals(true, success);
		richMan = RichManUtilities.getRichManInfo(asessionID);
		assertEquals(new Long(1024 - 256 + 512), richMan.getMoney());
		richMan = RichManUtilities.getRichManInfo(bsessionID);
		assertEquals(new Long(1024 - 512), richMan.getMoney());
		nearbyPlaces = utilities.getNearbyPlaces(asessionID, 38.45, 118.50);
		assertEquals(1, nearbyPlaces.size());
		place = nearbyPlaces.get(0);
		PlaceGeneral general = utilities.getPlaceGeneralInfo(asessionID,
				place.getID());
		assertEquals(new Long(512), general.getCurrentMoney());

	}

}
