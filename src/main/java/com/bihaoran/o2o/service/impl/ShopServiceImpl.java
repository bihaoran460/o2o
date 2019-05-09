package com.bihaoran.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bihaoran.o2o.dao.ShopDao;
import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ShopExecution;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.enums.ShopStateEnum;
import com.bihaoran.o2o.service.ShopService;
import com.bihaoran.o2o.util.ImageUtil;
import com.bihaoran.o2o.util.PageCalculator;
import com.bihaoran.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		//空值判断
		if(shop==null)
		{
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effctedNum=shopDao.insertShop(shop);
			if(effctedNum<=0)
			{
				throw new RuntimeException("店铺创建失败");
			}else
			{
				if(thumbnail.getImage()!=null)
				{
					//存储图片
					try
					{
						addShopImg(shop,thumbnail);
					}catch(Exception e)
					{
						throw new RuntimeException("addShopImg error:"+e.getMessage());
					}
					//更新图片地址
					effctedNum=shopDao.updateShop(shop);
					if(effctedNum<=0)
					{
						throw new RuntimeException("更新图片失败");
					}
					
				}
			}
			
		}catch (Exception e) {
			throw new RuntimeException("addShop error:"+e.getMessage());
			
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop,ImageHolder thumbnail) {
		
		// 获取shop图片的相对值路径
		String dest=PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
		
		
		if(shop==null||shop.getShopId()==null&&!"".equals(thumbnail.getImageName()))
		{
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		}else
		{
			//1.判断是否需要处理图片
			try
			{
				if(thumbnail.getImage()!=null)
				{
					Shop tempShop=shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg()!=null)
					{
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);	
				}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				shop.setEnableStatus(1);
				int effectedNum=shopDao.updateShop(shop);
				if(effectedNum<=0)
				{
					 throw new RuntimeException("店铺创建失败");
				}else
				{
					shop=shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			}catch(Exception e)
			{
				throw new RuntimeException("modifyShop error:"+ e.getMessage());
			}
		}

	}
	
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count=shopDao.queryShopCount(shopCondition);
		ShopExecution se=new ShopExecution();
		if(shopList!=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else
		{
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
