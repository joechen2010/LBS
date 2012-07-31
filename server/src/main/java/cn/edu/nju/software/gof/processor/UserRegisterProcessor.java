package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserRegisterProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		//http://localhost:9000/api?processor_name=register&user_name=joechen&password=123456&real_name=joe&school=安工大&place=科韵路&birthday=19831020
		String userName = request.getParameter(ServletParam.RequestParam.UserName);
		String password = request.getParameter(ServletParam.RequestParam.Password);
		String realName = request.getParameter(ServletParam.RequestParam.RealName);
		String school = request.getParameter(ServletParam.RequestParam.School);
		String place = request.getParameter(ServletParam.RequestParam.Place);
		String birthday = request.getParameter(ServletParam.RequestParam.Birthday);

		ProfileInfo info = new ProfileInfo(realName, school, place, birthday);
		AccountUtilities utilites = new AccountUtilities();
		boolean succ = utilites.register(userName, password, info);

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);

	}
}
