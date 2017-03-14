package com.kickstarter.dao;

import org.apache.ibatis.annotations.Param;

import com.kickstarter.model.LoginModel;

public interface LoginDbDao {

	//key - value
	public LoginModel getCredentials(
			@Param("userValue") String username,
			@Param("passValue") String password
			);
	
}
