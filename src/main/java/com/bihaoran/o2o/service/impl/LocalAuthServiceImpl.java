package com.bihaoran.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bihaoran.o2o.dao.LocalAuthDao;
import com.bihaoran.o2o.entity.LocalAuth;
import com.bihaoran.o2o.service.LocalAuthService;

@Service
public class LocalAuthServiceImpl implements LocalAuthService{
	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUserNameAndPassword(String userName, String password) {
		// TODO Auto-generated method stub
		return localAuthDao.getLocalAuthByUserNameAndPassword(userName, password);
	}

}
