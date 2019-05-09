package com.bihaoran.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.ShopCategory;

public class ShopCategoryServiceTest extends BaseTest{
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	
	@Test
	public void testShopCategoryService() {
		
		ShopCategory shopCategoryCondition=new ShopCategory();
		List<ShopCategory> shopCategoryList=shopCategoryService.getShopCategoryList(shopCategoryCondition);
		assertEquals(2, shopCategoryList.size());
		
		
	}
	

}
