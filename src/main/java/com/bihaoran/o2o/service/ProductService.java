package com.bihaoran.o2o.service;

import java.util.List;

import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ProductExecution;
import com.bihaoran.o2o.entity.Product;

public interface ProductService {
	/**
	 * 添加商品信息以及图片处理
	 * @return
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList);
	
	Product getProductById(long productId);
	
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList);
	
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
	
	ProductExecution updateStatus(Product product);
	

}
