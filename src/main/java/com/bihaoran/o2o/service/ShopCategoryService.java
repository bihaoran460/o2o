package com.bihaoran.o2o.service;

import java.util.List;

import com.bihaoran.o2o.entity.ShopCategory;


public interface ShopCategoryService {
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
