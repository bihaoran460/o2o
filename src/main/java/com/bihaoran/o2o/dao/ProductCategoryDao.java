package com.bihaoran.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bihaoran.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	public List<ProductCategory> queryProductCategory(long shopId);
	
	/**
	 * 批量新增商品类别
	 * @param productCategoryList
	 * @return
	 */
	public int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	public int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
	
	

}
