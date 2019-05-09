package com.bihaoran.o2o.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ShopExecution;
import com.bihaoran.o2o.entity.Shop;

public interface ShopService {
	//ShopExecution addShop(Shop shop,File shopImg);
	ShopExecution addShop(Shop shop,ImageHolder thumbnail);
	Shop getByShopId(long shopId);
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail);
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	

}
