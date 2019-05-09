package com.bihaoran.o2o.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;

public class ProductCategoryServiceTest extends BaseTest{
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Test
	public void tsetGetProductCategoryList() {
		
		List<ProductCategory> list=productCategoryService.getProductCategoryList(17L);
		System.out.println(list);
	}

}
