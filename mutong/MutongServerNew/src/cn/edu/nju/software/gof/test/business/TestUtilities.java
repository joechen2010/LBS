package cn.edu.nju.software.gof.test.business;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.type.UserState;

public class TestUtilities {

	public static boolean registerUser(String userName, String password,
			String realName, String birthday) {
		AccountUtilities utilities = new AccountUtilities();
		ProfileInfo profileInfo = new ProfileInfo(realName, null, null,
				birthday);
		boolean success = utilities.register(userName, password, profileInfo);
		return success;
	}

	public static String login(String userName, String password) {
		AccountUtilities accountUtilities = new AccountUtilities();
		LoginInfo loginInfo = new LoginInfo(userName, password,
				UserState.ON_LINE, "192.168.1.2", "9999");
		String sessionID = accountUtilities.login(loginInfo);
		return sessionID;
	}
}
