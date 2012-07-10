package cn.edu.nju.software.gof.test.business;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.business.CommonUtilities;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.gof.type.UserState;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class AccountTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private AccountUtilities utilities;

	@Before
	public void setUp() {
		helper.setUp();
		utilities = new AccountUtilities();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testIsUserExisted() {
		boolean existed = utilities.isUserExisted("lidejia");
		assertEquals(false, existed);
	}

	@Test
	public void testLogin() {
		String userName = "lidejia";
		String password = "lidejia";
		String realName = "lidejia";
		String birthday = "1989-10-31";
		String ipAddress = "192.168.1.100";
		String ipPort = "4567";
		UserState userState = UserState.ON_LINE;
		boolean success = TestUtilities.registerUser(userName, password,
				realName, birthday);
		assertEquals(true, success);
		LoginInfo info = new LoginInfo(userName, password, userState,
				ipAddress, ipPort);
		String sessionID = utilities.login(info);
		Key personID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			personID = person.getID();
			assertEquals(person.getAccount().getSessionID(), sessionID);
		} finally {
			em.close();
		}
	}

	@Test
	public void testRegister() {
		String userName = "lidejia";
		String password = "lidejia";
		String realName = "lidejia";
		String birthday = "1989-10-31";
		boolean success = TestUtilities.registerUser(userName, password,
				realName, birthday);
		assertEquals(true, success);
		boolean existed = utilities.isUserExisted(userName);
		assertEquals(true, existed);
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonByUserName(userName, em);
			Profile profile = person.getProfile();
			assertEquals(realName, profile.getRealName());
			assertEquals(null, profile.getSchool());
		} finally {
			em.close();
		}
	}

	@Test
	public void testChangePassword() {
		String userName = "lidejia";
		String password = "lidejia";
		String realName = "lidejia";
		String birthday = "1989-10-31";
		boolean success = TestUtilities.registerUser(userName, password,
				realName, birthday);
		assertEquals(true, success);
		String newPassword = "lijiahang";
		String sessionID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonByUserName(userName, em);
			Account account = person.getAccount();
			assertEquals(password, account.getPassword());
			sessionID = account.getSessionID();
		} finally {
			em.close();
		}
		success = utilities.changePassword(sessionID, password, newPassword);
		assertEquals(success, true);
		em = EMF.getInstance().createEntityManager();
		try {
			Query query = em
					.createQuery("SELECT A FROM Account AS A WHERE A.userName = :userName");
			query.setParameter("userName", userName);
			Account account = (Account) query.getSingleResult();
			assertEquals(newPassword, account.getPassword());
		} finally {
			em.close();
		}
	}
}
