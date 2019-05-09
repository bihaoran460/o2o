package com.bihaoran.o2o.service;

import java.util.List;


import com.bihaoran.o2o.dto.ProductCategoryExecution;
import com.bihaoran.o2o.entity.ProductCategory;
public interface ProductCategoryService {
	List<ProductCategory> getProductCategoryList(long shopId);
	
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList);
	/**
	 * 将此类别下商品类别ID置为空，再删除商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId);
}
