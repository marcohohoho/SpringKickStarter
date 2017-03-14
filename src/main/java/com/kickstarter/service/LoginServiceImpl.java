package com.kickstarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kickstarter.dao.LoginDbDao;
import com.kickstarter.model.LoginModel;

@Service
@Transactional
public class LoginServiceImpl implements LoginService{

	@Autowired
	LoginDbDao loginDbDao; 
	
	@Override
	public boolean authenticate(String user, String pass) {
		LoginModel lmodel = loginDbDao.getCredentials(user, pass);
		if(lmodel!=null)
			return true;
		else
			return false;
//		return true;
	}
}
