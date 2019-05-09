package com.bihaoran.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.entity.PersonInfo;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.entity.ShopCategory;


public class ShopDaoTest extends BaseTest{
	@Autowired 
	private ShopDao shopDao;
	@Test
	public void testqueryShopList() {
		Shop shopCondition=new Shop();
		ShopCategory childCategory=new ShopCategory();
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(3L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
//		PersonInfo personInfo=new PersonInfo();
//		personInfo.setUserId(1L);
//		shopCondition.setShopName("咖啡");
//		shopCondition.setPersonInfo(personInfo);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, 0, 18);
		System.out.println(shopList.size());	
	}
	@Test
	@Ignore
	public void testqueryShopCount() {
//		Shop shopCondition=new Shop();
//		ShopCategory shopCategory=new ShopCategory();
//		shopCategory.setShopCategoryId(2L);
//		shopCondition.setShopCategory(shopCategory);
		Shop shopCondition=new Shop();
		ShopCategory childCategory=new ShopCategory();
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(3L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		
		int count=shopDao.queryShopCount(shopCondition);
		System.out.println(count);
	}
	@Test
	@Ignore
	public void testInsertShop()
	{
		Shop shop=new Shop();
		PersonInfo personInfo=new PersonInfo();
		Area area=new Area();
		ShopCategory shopCategory=new ShopCategory();
		personInfo.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setPersonInfo(personInfo);
		shop.setShopCategory(shopCategory);
		shop.setArea(area);
		shop.setCreateTime(new Date());
		shop.setShopName("测试的店铺");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setShopImg("test");
		shop.setPhone("12351");
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effect=shopDao.insertShop(shop);
		assertEquals(1, effect);			
	}
	@Test
	@Ignore
	public void testupdateShop()
	{
		Shop shop=new Shop();
		shop.setShopId(1L);
		shop.setShopName("���µ���");
		shop.setShopDesc("��������");
		shop.setShopAddr("���Ե�ַ");
		shop.setLastEditTime(new Date());
		int effect=shopDao.updateShop(shop);
		assertEquals(1, effect);
				
	}
	@Test
	@Ignore
	public void testqueryByShopId()
	{
		long shopId=1;
		Shop shop=shopDao.queryByShopId(shopId);
		System.out.println(shop.getArea().getAreaId());
		System.out.println(shop.getArea().getAreaName());
	}

}
