package com.bihaoran.o2o.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.LocalAuth;

public class LocalAuthDaoTest extends BaseTest {
	
	@Autowired
	private LocalAuthDao localAuthDao;
	
	@Test
	public void testgetLocalAuthByUserNameAndPassword()
	{
		String userName="bihaoran";
		String password="123456";
		LocalAuth localAuth=localAuthDao.getLocalAuthByUserNameAndPassword(userName, password);
		System.out.println(localAuth.getPersonInfo().getName());
	}

}
