package com.bihaoran.o2o.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bihaoran.o2o.entity.Product;

public interface ProductDao {
	
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	public int insertProduct(Product product);
	
	public Product queryProductById(long productId);
	
	public int updateProduct(Product product);
	
	public List<Product> queryProductList(@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	public int queryProductCount(@Param("productCondition") Product productCondition);
	
	public int updateStatus(Product product);
	
	public int updateProductCategoryToNull(long productCategoryId);

}
