package com.bihaoran.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bihaoran.o2o.BaseTest;
import com.bihaoran.o2o.entity.ProductImg;

public class ProductImgDaoTest extends BaseTest {
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	@Ignore
	public void testBatchInsertProductImg() {
		ProductImg productImg=new ProductImg();
		productImg.setImgAddr("图片");
		productImg.setImgDesc("测试图片");
		productImg.setPriority(1);
		productImg.setCreateTime(new Date());
		productImg.setProductId(1L);
		
		ProductImg productImg1=new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(2);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(1L);
		
		List<ProductImg> list=new ArrayList<ProductImg>();
		list.add(productImg);
		list.add(productImg1);
		int effectedNum=productImgDao.batchInsertProductImg(list);
		assertEquals(2, effectedNum);	
	}
	
	@Test
	public void testDeleteProductImgByProductId()
	{
		ProductImg productImg=new ProductImg();
//		productImg.setProductId(1L);
		int count=productImgDao.deleteProductImgByProductId(4L);
		System.out.println(count);

	}
}
