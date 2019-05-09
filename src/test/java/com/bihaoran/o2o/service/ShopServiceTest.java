package com.bihaoran.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ShopExecution;
import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.entity.PersonInfo;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.entity.ShopCategory;
import com.bihaoran.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;
	
	@Test
	@Ignore
	public void testgetShopList()
	{
		Shop shopCondition=new Shop();
		ShopCategory sc=new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setShopCategory(sc);
		ShopExecution shopExecution=shopService.getShopList(shopCondition, 2,2);
		System.out.println(shopExecution.getShopList().size());
		System.out.println(shopExecution.getCount());
	}
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException
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
		shop.setShopName("测试的店铺4");
		shop.setShopDesc("test4");
		shop.setShopAddr("test4");
		shop.setPhone("12351");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg=new File("D:\\Game\\ceshi.jpg");
		InputStream is=new FileInputStream(shopImg);
		ImageHolder imageHolder=new ImageHolder(shopImg.getName(), is);
		ShopExecution se=shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
	@Test
	public void testModifyShop() throws FileNotFoundException
	{
		Shop shop=new Shop();
		shop.setShopId(15L);
		shop.setShopName("修改后的店铺名称4.301");
		File shopImg=new File("D:\\Game\\kafei.jpg");
		InputStream is=new FileInputStream(shopImg);
		ImageHolder imageHolder=new ImageHolder();
		ShopExecution shopExecution=shopService.modifyShop(shop,imageHolder);
		System.out.println(shopExecution.getShop().getShopImg());
		
		
		
	}
}
