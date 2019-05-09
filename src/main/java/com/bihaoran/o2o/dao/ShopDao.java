package com.bihaoran.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bihaoran.o2o.entity.Shop;

public interface ShopDao {
	public int insertShop(Shop shop);
	public int updateShop(Shop shop);
	public Shop queryByShopId(long shopId);
	
	/**
	 * 分页查询店铺：店铺名（模糊），店铺状态，店铺类别，区域Id
	 * @param shopCondition
	 * @param rowIndex 从第几行开始写数据
	 * @param pageSize 返回的条数
	 * @return
	 */
	public List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
	
	public int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
