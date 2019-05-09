package com.bihaoran.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bihaoran.o2o.dao.ProductCategoryDao;
import com.bihaoran.o2o.dao.ProductDao;
import com.bihaoran.o2o.dto.ProductCategoryExecution;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.enums.ProductCategoryStateEnum;
import com.bihaoran.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	ProductCategoryDao proCategoryDao;
	@Autowired
	ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		// TODO Auto-generated method stub
		return proCategoryDao.queryProductCategory(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) {
		if(productCategoryList!=null&&productCategoryList.size()>0)
		{
			try
			{
				int effctedNum=proCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effctedNum<=0)
				{
					throw new RuntimeException("店铺创建失败");
				}else
				{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e)
			{
				throw new RuntimeException("batchAddProductCategory error:"+e.getMessage());
			}
		}else
		{
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) {
		//解除数据库里商品与该productCategoryId的联系
		try
		{
			int count=productDao.updateProductCategoryToNull(productCategoryId);
			if(count<0)
			{
				throw new RuntimeException("商品类别更新失败");
			}
		}catch (Exception e) {
			throw new RuntimeException("商品类别更新失败"+e.getMessage());
		}
		//删除该productCategoryId
		try {
			int effectedNum=proCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0)
			{
				throw new RuntimeException("商品类别删除失败");
			}
			else
			{
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
	}catch (Exception e) {
		throw new RuntimeException("deleteProductCategory error:" +e.getMessage());
		}
	}
}
