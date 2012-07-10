package cn.edu.nju.software.gof.test.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.nju.software.gof.beans.FriendSearchCondition;
import cn.edu.nju.software.gof.beans.json.BriefFriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendRequestInfo;
import cn.edu.nju.software.gof.beans.json.NearbyFriendInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.CheckInUtilities;
import cn.edu.nju.software.gof.business.CommonUtilities;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.FriendRequest;
import cn.edu.nju.software.gof.entity.Person;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FriendTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	FriendUtilities utilities;

	@Before
	public void setUp() {
		helper.setUp();
		utilities = new FriendUtilities();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testGetNearbyFriends() {
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
		//
		Key friendID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		List<FriendRequestInfo> requests = utilities
				.getFriendRequests(bSessionID);
		FriendRequestInfo request = requests.get(0);
		success = utilities.agreeFriendRequest(bSessionID, request.getID());
		CheckInUtilities checkInUtilities = new CheckInUtilities();
		success = checkInUtilities.updateLocation(bSessionID, 38.34, 118.45);
		success = checkInUtilities.updateLocation(bSessionID, 36.34, 116.45);
		success = checkInUtilities.updateLocation(bSessionID, 38.54, 118.35);
		List<NearbyFriendInfo> nearbyFriends = utilities.getNearbyFriends(
				aSessionID, 38.45, 118.34);
		assertEquals(2, nearbyFriends.size());
	}

	@Test
	public void testGetFriendsList() {
		String aUserName = "lidejia";
		String aPassword = "lidejia";
		String aRealName = "lidejia";
		String aBirthday = "1989-10-31";
		//
		String bUserName = "lijiahang";
		String bPassword = "lijiahang";
		String bRealName = "lijiahang";
		String bBirthday = "1989-09-13";
		boolean success = TestUtilities.registerUser(aUserName, aPassword,
				aRealName, aBirthday);
		assertEquals(true, success);
		success = TestUtilities.registerUser(bUserName, bPassword, bRealName,
				bBirthday);
		assertEquals(true, success);
		String aSessionID = TestUtilities.login(aUserName, aPassword);
		String bSessionID = TestUtilities.login(bUserName, bPassword);
		assertNotNull(aSessionID);
		assertNotNull(bSessionID);
		Key friendID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		List<FriendRequestInfo> requests = utilities
				.getFriendRequests(bSessionID);
		FriendRequestInfo request = requests.get(0);
		success = utilities.agreeFriendRequest(bSessionID, request.getID());
		List<FriendInfo> friends = utilities.getFriendsList(aSessionID);
		assertEquals(1, friends.size());
		FriendInfo friend = friends.get(0);
		assertEquals(KeyFactory.keyToString(friendID), friend.getFriendID());
		assertEquals(bRealName, friend.getFriendRealName());
	}

	@Test
	public void testGetFriendProfile() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetFriendAvatar() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSendFriendRequest() {
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
		//
		Key friendID = null;
		Key myselfID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person myself = CommonUtilities
					.getPersonBySessionID(aSessionID, em);
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
			myselfID = myself.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		assertEquals(true, success);
		em = EMF.getInstance().createEntityManager();
		try {
			Person person = em.find(Person.class, friendID);
			List<FriendRequest> requests = person.getFriendRequests(em);
			assertEquals(1, requests.size());
			FriendRequest request = requests.get(0);
			assertEquals(friendID, request.getTargetPersonID());
			assertEquals(myselfID, request.getSourcePersonID());
		} finally {
			em.close();
		}
	}

	@Test
	public void testGetFriendRequests() {
		String aUserName = "lidejia";
		String aPassword = "lidejia";
		String aRealName = "lidejia";
		String aBirthday = "1989-10-31";
		String bUserName = "lijiahang";
		String bPassword = "lijiahang";
		String bRealName = "lijiahang";
		String bBirthday = "1989-09-13";
		boolean success = TestUtilities.registerUser(aUserName, aPassword,
				aRealName, aBirthday);
		assertEquals(true, success);
		success = TestUtilities.registerUser(bUserName, bPassword, bRealName,
				bBirthday);
		assertEquals(true, success);
		String aSessionID = TestUtilities.login(aUserName, aPassword);
		String bSessionID = TestUtilities.login(bUserName, bPassword);
		assertNotNull(aSessionID);
		assertNotNull(bSessionID);
		Key friendID = null;
		Key myselfID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person myself = CommonUtilities
					.getPersonBySessionID(aSessionID, em);
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
			myselfID = myself.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		List<FriendRequestInfo> requests = utilities
				.getFriendRequests(bSessionID);
		assertEquals(1, requests.size());
		FriendRequestInfo request = requests.get(0);
		assertEquals(aRealName, request.getRealName());
		String myIDString = KeyFactory.keyToString(myselfID);
		assertEquals(myIDString, request.getRequesterID());
	}

	@Test
	public void testAgreeFriendRequest() {
		String aUserName = "lidejia";
		String aPassword = "lidejia";
		String aRealName = "lidejia";
		String aBirthday = "1989-10-31";
		String bUserName = "lijiahang";
		String bPassword = "lijiahang";
		String bRealName = "lijiahang";
		String bBirthday = "1989-09-13";
		boolean success = TestUtilities.registerUser(aUserName, aPassword,
				aRealName, aBirthday);
		assertEquals(true, success);
		success = TestUtilities.registerUser(bUserName, bPassword, bRealName,
				bBirthday);
		assertEquals(true, success);
		String aSessionID = TestUtilities.login(aUserName, aPassword);
		String bSessionID = TestUtilities.login(bUserName, bPassword);
		assertNotNull(aSessionID);
		assertNotNull(bSessionID);
		Key friendID = null;
		Key myselfID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person myself = CommonUtilities
					.getPersonBySessionID(aSessionID, em);
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
			myselfID = myself.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		List<FriendRequestInfo> requests = utilities
				.getFriendRequests(bSessionID);
		FriendRequestInfo request = requests.get(0);
		success = utilities.agreeFriendRequest(bSessionID, request.getID());
		assertEquals(true, success);
		em = EMF.getInstance().createEntityManager();
		try {
			Person myself = em.find(Person.class, myselfID);
			Person hang = em.find(Person.class, friendID);
			List<Key> myFriends = myself.getFriendIDs();
			List<Key> hangFriends = hang.getFriendIDs();
			assertEquals(true, myFriends.contains(friendID));
			assertEquals(true, hangFriends.contains(myselfID));
			assertEquals(0, myself.getFriendRequests(em).size());
			assertEquals(0, hang.getFriendRequests(em).size());
		} finally {
			em.close();
		}
	}

	@Test
	public void testDeleteFriend() {
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
		//
		Key friendID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person friend = CommonUtilities
					.getPersonBySessionID(bSessionID, em);
			friendID = friend.getID();
		} finally {
			em.close();
		}
		success = utilities.sendFriendRequest(aSessionID,
				KeyFactory.keyToString(friendID));
		List<FriendRequestInfo> requests = utilities
				.getFriendRequests(bSessionID);
		FriendRequestInfo request = requests.get(0);
		success = utilities.agreeFriendRequest(bSessionID, request.getID());
		success = utilities.deleteFriend(aSessionID,
				KeyFactory.keyToString(friendID));
		assertEquals(true, success);
		List<FriendInfo> friends = utilities.getFriendsList(aSessionID);
		assertEquals(0, friends.size());
		friends = utilities.getFriendsList(bSessionID);
		assertEquals(0, friends.size());
	}

	@Test
	public void testSearchFriends() {
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
		FriendSearchCondition condition = new FriendSearchCondition(
				"lijiahang", null, null, null, null);
		List<BriefFriendInfo> friends = utilities.searchFriends(aSessionID,
				condition);
		assertEquals(1, friends.size());
		BriefFriendInfo friend = friends.get(0);
		assertEquals("lijiahang", friend.getRealName());
		//
		condition = new FriendSearchCondition(null, "lijiahang", null, null,
				null);
		friends = utilities.searchFriends(aSessionID, condition);
		assertEquals(1, friends.size());
		friend = friends.get(0);
		assertEquals("lijiahang", friend.getRealName());
		//
		condition = new FriendSearchCondition(null, null, null, null,
				"1989-09-13");
		friends = utilities.searchFriends(aSessionID, condition);
		assertEquals(1, friends.size());
		friend = friends.get(0);
		assertEquals("lijiahang", friend.getRealName());
		//
		condition = new FriendSearchCondition(null, null, null, "Nanjing",
				"1989-09-13");
		friends = utilities.searchFriends(aSessionID, condition);
		assertEquals(0, friends.size());
	}

	@Test
	public void testGetRecommendFriends() {
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
		ProfileUtilities profileUtilities = new ProfileUtilities();
		ProfileInfo info = new ProfileInfo(null, "NJU", null, null);
		success = profileUtilities.setUserProfile(aSessionID, info);
		assertEquals(true, success);
		success = profileUtilities.setUserProfile(bSessionID, info);
		assertEquals(true, success);
		List<BriefFriendInfo> friendInfos = utilities.getRecommendFriends(aSessionID);
		assertEquals(1, friendInfos.size());
		BriefFriendInfo friendInfo = friendInfos.get(0);
		assertEquals(friendInfo.getRealName(), "lijiahang");
	}

}
