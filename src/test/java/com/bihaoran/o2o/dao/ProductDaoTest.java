package com.bihaoran.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.Product;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.ProductImg;
import com.bihaoran.o2o.entity.Shop;

public class ProductDaoTest extends BaseTest {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testInsertProduct() {
		Product product=new Product();
		product.setCreateTime(new Date());
		product.setEnableStatus(1);
		product.setImgAddr("测试1");
		product.setLastEditTime(new Date());
		product.setNormalPrice("100");
		product.setPriority(1);
		ProductCategory productCategory=new ProductCategory();
		productCategory.setProductCategoryId(1L);
		product.setProductCategory(productCategory);
		product.setProductDesc("测试描述");
		product.setProductName("地球仪");
		product.setPromotionPrice("80");
		Shop shop=new Shop();
		shop.setShopId(19L);
		product.setShop(shop);
		
		int count=productDao.insertProduct(product);
		assertEquals(1, count);	
	}
	
	@Test 
	@Ignore
	public void testQueryProductById()
	{
		long productId=1;
		ProductImg productImg1=new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		
		ProductImg productImg2=new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("测试图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> list=new ArrayList<ProductImg>();
		list.add(productImg1);
		list.add(productImg2);
		int count=productImgDao.batchInsertProductImg(list);
		assertEquals(2, count);
		
		Product product=productDao.queryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
		
		count=productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2,  count);
	}
	
	@Test
	@Ignore
	public void testUpdateProduct()
	{
		Product product=new Product();
		ProductCategory pc=new ProductCategory();
		Shop shop=new Shop();
		shop.setShopId(19L);
		pc.setProductCategoryId(2L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("还是地球仪");
		int count=productDao.updateProduct(product);
		System.out.println(count);
	}
	
	@Test
	@Ignore
	public void testUpdateStatus()
	{
		Product product=new Product();
		ProductCategory pc=new ProductCategory();
		Shop shop=new Shop();
		shop.setShopId(19L);
		pc.setProductCategoryId(2L);
		product.setProductId(1L);
		product.setEnableStatus(1);
		product.setShop(shop);
		int count=productDao.updateStatus(product);
		System.out.println(count);	
	}
	@Test
	@Ignore
	public void testQueryProductList()
	{
		Product productCondition=new Product();
//		Shop shop=new Shop();
////		shop.setShopId(19L);
////		productCondition.setShop(shop);
		productCondition.setProductName("测试");
		
		List<Product> productList=productDao.queryProductList(productCondition, 0, 8);
		
		assertEquals(7, productList.size());
		
		int count=productDao.queryProductCount(productCondition);
		
		assertEquals(7, count);
				
	}
	@Test
	public void testUpdateProductCategoryToNull()
	{
		int count=productDao.updateProductCategoryToNull(2L);
		assertEquals(1, count);
	}

}
