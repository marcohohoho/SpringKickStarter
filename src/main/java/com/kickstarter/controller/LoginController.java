package com.kickstarter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kickstarter.service.LoginService;

@Controller
@RequestMapping
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
//	return "view.html"
	@RequestMapping(method = RequestMethod.GET)
	public String defaultPage(){
		return "/login";
	}

//modelmap "key from html", string param
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String handleCalc(@RequestParam String user, @RequestParam String pass, ModelMap modelMap){
		modelMap.put("user", user);
		modelMap.put("pass", pass);
		boolean isValid=loginService.authenticate(user, pass);
		if(isValid){
			System.out.println(true);
			return "/dash";
		}
		else{
			System.out.println(false);
			return "/dash";
		}
			
	}
}
