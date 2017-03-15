package com.kickstarter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kickstarter.service.LoginService;

@Controller
@RequestMapping("/")
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
//	return "view.html"
	@RequestMapping(method = RequestMethod.GET)
	public String defaultPage(){
		return "/login";
	}

//modelmap "key from html", string param
	@RequestMapping(value = "/login", method = 																																																																																																																														RequestMethod.POST)
	public String handleCalc(@RequestParam String theusername, @RequestParam String thepassword, ModelMap modelMap){
//		modelMap.put("user1", user);
//		modelMap.put("pass1", pass);
//		addAttribute() does null check
		modelMap.addAttribute("user1", theusername);
		modelMap.addAttribute("pass1", thepassword);
		boolean isValid=loginService.authenticate(modelMap);
		System.out.println(isValid);
		
		if(isValid){
			return "/dash";
		}
		else{
			return "/login";
		}
	}
}
