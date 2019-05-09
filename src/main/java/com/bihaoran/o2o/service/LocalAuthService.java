package com.bihaoran.o2o.service;


import com.bihaoran.o2o.entity.LocalAuth;

public interface LocalAuthService {
	public LocalAuth getLocalAuthByUserNameAndPassword(String userName,String password);

}
