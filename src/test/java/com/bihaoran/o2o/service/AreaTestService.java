package com.bihaoran.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.Area;

public class AreaTestService extends BaseTest {
	@Autowired
	private AreaService areaService;
	
	@Test
	public void testGetAreaList()
	{
		List<Area> areaList=areaService.getAreaList();
		assertEquals("西苑",areaList.get(0).getAreaName());
		
	}

}
