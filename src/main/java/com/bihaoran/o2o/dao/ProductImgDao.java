package com.bihaoran.o2o.dao;

import java.util.List;

import com.bihaoran.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 批量添加商品详情图
	 * @param productImgList
	 * @return
	 */
	public int batchInsertProductImg(List<ProductImg> productImgList);
	/**
	 * 删除指定商品下的所有详情图
	 * @param productId
	 * @return
	 */
	public int deleteProductImgByProductId(long productId);
	
	public List<ProductImg> queryProductImgList(long productId);

}
