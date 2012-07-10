package org.bmsys.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bmsys.dao.UserDao;
import org.bmsys.form.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController
{
	private Log logger = LogFactory.getLog(getClass());

	private static final String REDIRECT_VIEW_NAME = "login";
	private static final String ERROR_VIEW_NAME = "login";
	private static final String WELCOME_VIEW_NAME = "dashboard";

	@Autowired
	private UserDao loginDao;

	/**
	 * 
	 * @param user
	 * @param result
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/showLogin", method = RequestMethod.GET)
	public ModelAndView redirectToLogin(@ModelAttribute UserCommand user, BindingResult result)
	{
		logger.info("[LoginController] - Invoking redirectToLogin method.");
		ModelAndView model = new ModelAndView(REDIRECT_VIEW_NAME);
		model.addObject("loginCommand", user);
		model.addObject("errors", result);
		return model;
	}

	/**
	 * 
	 * @param user
	 * @param result
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/processLogin", method=RequestMethod.POST)
	public ModelAndView login(@ModelAttribute UserCommand user, BindingResult result,HttpSession session)
	{
		logger.info("[LoginController] - Invoking login method using name=."+user.getUserName());
		
		ModelAndView model = new ModelAndView();

		UserCommand userObj = loginDao.checkCredentials(user.getUserName(), user.getPassword());
		
		if (userObj == null)
			result.addError(new ObjectError("invalidCredentials","Wrong UserName or Password!"));
		
		if (result.hasErrors())
		{
			model.addObject("loginCommand",user);
			model.addObject("errors", result);
			model.setViewName(ERROR_VIEW_NAME);
		}
		else
		{
			session.setAttribute("user", userObj);
			model.setViewName(WELCOME_VIEW_NAME);
		}
		return model;
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session)
	{
		logger.info("[LoginController] - Invoking logout method.");
		if(session.getAttribute("user") != null)
			session.removeAttribute("user");
		return "redirect:showLogin.htm";
	}
}
