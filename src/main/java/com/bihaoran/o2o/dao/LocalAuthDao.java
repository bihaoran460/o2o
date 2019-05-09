package com.bihaoran.o2o.dao;

import org.apache.ibatis.annotations.Param;

import com.bihaoran.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	public LocalAuth getLocalAuthByUserNameAndPassword(@Param("userName")String userName,@Param("password")String password);
}
