package com.kickstarter.service;

import org.springframework.ui.ModelMap;

public interface LoginService {
	
	public boolean authenticate(String user, String pass);

	public boolean authenticate(ModelMap mapper);
	
}
