package com.bihaoran.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bihaoran.o2o.dao.ProductDao;
import com.bihaoran.o2o.dao.ProductImgDao;
import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ProductExecution;
import com.bihaoran.o2o.entity.Product;
import com.bihaoran.o2o.entity.ProductImg;
import com.bihaoran.o2o.enums.ProductCategoryStateEnum;
import com.bihaoran.o2o.enums.ProductStateEnum;
import com.bihaoran.o2o.service.ProductService;
import com.bihaoran.o2o.util.ImageUtil;
import com.bihaoran.o2o.util.PageCalculator;
import com.bihaoran.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	/**
	 * 处理缩略图，获取缩略图的相对路径，并赋值给product
	 * 往tb_product写入商品信息 获取productId
	 *将productId批量处理商品详情图
	 * 将商品详情图列表批量插入tb_product_img中
	 */
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) {
		// TODO Auto-generated method stub
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null)
		{
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			if(thumbnail!=null)
			{
				addThumbnail(product,thumbnail);
			}
			try
			{
				//创建商品信息
				int count=productDao.insertProduct(product);
				if(count<=0)
				{
					throw new RuntimeException("创建商品失败");
				}
			}catch (Exception e) {
				throw new RuntimeException("创建商品失败"+e.getMessage());
			}
			//如果商品详情页图不为空
			if(productImgHolderList!=null&&productImgHolderList.size()>0)
			{
				addProductImgList(product,productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
			
		}else
		{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	/**
	 * 添加缩略图到电脑上的指定文件夹
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product,ImageHolder thumbnail)
	{
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	/**
	 * 添加图片详情图到指定文件夹
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product,List<ImageHolder> productImgHolderList)
	{
		//获取图片储存路径，这里直接存放在相应店铺的文件夹底下
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		//遍历图片一次去处理 添加进ProductImg实体类里
		for(ImageHolder productImgHolder:productImgHolderList)
		{
			String imgAddr=ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg=new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		if(productImgList.size()>0)
		{
			try {
				int count=productImgDao.batchInsertProductImg(productImgList);
				if(count<=0)
				{
					throw new RuntimeException("创建详情图标失败");
				}
			}catch (Exception e) {
				throw new RuntimeException("创建详情图标失败"+e.getMessage());
			}
		}
		
	}
	@Override
	public Product getProductById(long productId) {
		
		return productDao.queryProductById(productId);
	}
	@Override
	@Transactional
	//若缩略图参数有值，则处理缩略图，
	//若原先存在缩略图先删除再添加新图，之后再获取相对路径并赋值给product
	//若详情图列表有值，对详情列表做同样的操作
	//将tb_product_img_下面的商品详情图删除
	//更新tb_product信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) {
		
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null)
		{
			product.setLastEditTime(new Date());
			if(thumbnail!=null)
			{
				Product tempProduct=productDao.queryProductById(product.getProductId());
				if(tempProduct.getImgAddr()!=null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);	
			}
			if(productImgHolderList!=null&&productImgHolderList.size()>0)
			{
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try
			{
				int count=productDao.updateProduct(product);
				if(count<=0)
				{
					throw new RuntimeException("更新商品信息失败");
					
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);	
				
			}catch (Exception e) {
				throw new RuntimeException("更新商品信息失败"+e.getMessage());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
			
	}
	/**
	 * 删除图片详情图和数据库中的路径
	 * @param productId
	 */
	private void deleteProductImgList(long productId)
	{
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		
		for(ProductImg productImg:productImgList)
		{
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
		
	}
	
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count=productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
	@Override
	public ProductExecution updateStatus(Product product) {
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null)
		{
			try
			{
				int count=productDao.updateStatus(product);
				if(count<=0)
				{
					throw new RuntimeException("更新商品信息失败");
					
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);	
				
			}catch (Exception e) {
				throw new RuntimeException("更新商品信息失败"+e.getMessage());
			}
		}else
		{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
			
	}
}
