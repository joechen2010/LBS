package org.bmsys.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bmsys.dao.UserDao;
import org.bmsys.form.UserCommand;
import org.bmsys.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController
{
	private Log logger = LogFactory.getLog(getClass());

	private static final String ADDUSER_VIEW_NAME = "addUser";
	private static final String EDITUSER_VIEW_NAME = "editUser";
	private static final String ADDUSER_ERROR_VIEW_NAME = "addUser";
	private static final String LISTUSER_VIEW_NAME = "listUser";

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserValidator validator;

	/**
	 * 
	 * @param user
	 * @param result
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/showAddUser", method = RequestMethod.GET)
	public String showAddUser(Map<String, Object> model)
	{
		logger.info("[UserController] - Invoking showAddUser method.");
		model.put("userCommand", new UserCommand());
		return ADDUSER_VIEW_NAME;
	}

	/**
	 * 
	 * @param user
	 * @param result
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(Map<String, Object> model, @ModelAttribute
	UserCommand command, BindingResult result)
	{
		logger.info("[UserController] - Invoking addUser method.");
		validator.validateUser(command, result);

		if (result.hasErrors())
		{
			model.put("errors", result);
			return ADDUSER_ERROR_VIEW_NAME;
		}

		userDao.addUser(command);
		return "redirect:" + LISTUSER_VIEW_NAME + ".htm";
	}

	/**
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/listUser", method = RequestMethod.GET)
	public String listUsers(Map<String, Object> model)
	{
		logger.info("[UserController] - Invoking listUsers method.");
		model.put("usersList", userDao.listUsers());
		return LISTUSER_VIEW_NAME;
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public String deleteUser(@RequestParam(value = "id")
	String userID)
	{
		logger.info("[UserController] - Invoking deleteUser method.");
		int status = userDao.deleteUserByID(userID);
		return "redirect:listUser.htm?s=" + status;
	}

	@RequestMapping(value = "/showEditUser", method = RequestMethod.GET)
	public String editUser(Map<String, Object> model,
			@RequestParam(value = "id")
			String userID)
	{
		logger.info("[UserController] - Invoking editUser method.");
		model.put("command", userDao.findUserByID(userID));
		return EDITUSER_VIEW_NAME;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(Map<String, Object> model, @ModelAttribute
			UserCommand command, BindingResult result)
	{
		logger.info("[UserController] - Invoking addUser method.");
		validator.validateUser(command, result);

		if (result.hasErrors())
		{
			model.put("errors", result);
			return ADDUSER_ERROR_VIEW_NAME;
		}

		userDao.updateUser(command);
		return "redirect:" + LISTUSER_VIEW_NAME + ".htm";
	}
}