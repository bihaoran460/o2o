package com.bihaoran.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.Area;

public class AreaTestDao extends BaseTest {
	@Autowired 
	private AreaDao areaDao;
	public static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	@Test
	@Ignore
	public void testQueryArea()
	{
		List<Area> areaList=areaDao.queryArea();
		assertEquals(2,areaList.size());
	}
	
	@Test
	public void test1()
	{
		String s=sDateFormat.format(new Date());
		System.out.println(s);
			
	}

}
