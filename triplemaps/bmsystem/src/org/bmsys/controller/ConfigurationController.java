package org.bmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConfigurationController
{
	private static final String SHOW_CONFIG_VIEW = "configuration";
	
	@RequestMapping(value = "/showConfig", method = RequestMethod.GET)
	public String showConfig()
	{
		return SHOW_CONFIG_VIEW;
	}
}
