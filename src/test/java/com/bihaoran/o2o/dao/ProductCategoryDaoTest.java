package com.bihaoran.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest{
	
	@Autowired 
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testqueryProductCategory()
	{
		List<ProductCategory> list=productCategoryDao.queryProductCategory(19L);
		System.out.println(list.size());
	}
	
	@Test
	@Ignore
	public void testBatchInsertProductCategory()
	{
		ProductCategory productCategory1=new ProductCategory();
		productCategory1.setProductCategoryName("商品类别3");
		productCategory1.setPriority(1);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(19L);
		ProductCategory productCategory2=new ProductCategory();
		productCategory2.setProductCategoryName("商品类别4");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(19L);
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		list.add(productCategory1);
		list.add(productCategory2);
		int effectedNum=productCategoryDao.batchInsertProductCategory(list);
		assertEquals(2, effectedNum);	
	}
	@Test
	public void testDeleteProductCategory()
	{
		long shopId=19L;
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategory(shopId);
		for(ProductCategory pc:productCategoryList)
		{
			if("商品类别3".equals(pc.getProductCategoryName())||"商品类别4".equals(pc.getProductCategoryName()))
			{
				int effectedNum=productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);	
				assertEquals(1, effectedNum);	
			}
					
		}
	}
	
	

}
