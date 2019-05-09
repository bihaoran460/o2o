package com.bihaoran.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ProductExecution;
import com.bihaoran.o2o.entity.Product;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.enums.ProductCategoryStateEnum;
import com.bihaoran.o2o.enums.ProductStateEnum;


public class ProductServiceTest extends BaseTest{
	
	@Autowired
	private ProductService productService;
	
	@Test
	@Ignore
	public void testAddProduct() throws FileNotFoundException
	{
		Product product=new Product();
		Shop shop=new Shop();
		shop.setShopId(1L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("测试商品3");
		product.setProductDesc("测试商品3");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductCategoryStateEnum.SUCCESS.getState());
		
		File thumbnailFile=new File("D:\\Game\\ceshi.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail=new ImageHolder(thumbnailFile.getName(), is);
		
		File pruductImg1=new File("D:\\Game\\ceshi.jpg");
		InputStream is1=new FileInputStream(pruductImg1);
		File pruductImg2=new File("D:\\Game\\ceshi.jpg");
		InputStream is2=new FileInputStream(pruductImg2);
		
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(pruductImg1.getName(),is1));
		productImgList.add(new ImageHolder(pruductImg2.getName(),is2));
		
		
		
		ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());	
	}
	
	@Test
	public void testModifyProduct() throws FileNotFoundException {

		Product product=new Product();
		Shop shop=new Shop();
		shop.setShopId(1L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setProductId(2L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("正式的商品");
		product.setProductDesc("正式的商品");
		
		File thumbnailFile=new File("D:\\Game\\kafei.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail=new ImageHolder(thumbnailFile.getName(), is);
		
		File pruductImg1=new File("D:\\Game\\kafei.jpg");
		InputStream is1=new FileInputStream(pruductImg1);
		File pruductImg2=new File("D:\\Game\\kafei.jpg");
		InputStream is2=new FileInputStream(pruductImg2);
		
		List<ImageHolder> productImgHolderList=new ArrayList<ImageHolder>();
		productImgHolderList.add(new ImageHolder(pruductImg1.getName(),is1));
		productImgHolderList.add(new ImageHolder(pruductImg2.getName(),is2));
		
		ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgHolderList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
		
	}
	
	

}
