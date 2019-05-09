package com.bihaoran.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bihaoran.o2o.dto.ProductExecution;
import com.bihaoran.o2o.entity.Product;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.service.ProductCategoryService;
import com.bihaoran.o2o.service.ProductService;
import com.bihaoran.o2o.service.ShopService;
import com.bihaoran.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopSerivice;
	@Autowired
	private ProductService productSerivice;
	@Autowired
	private ProductCategoryService productCategorySerivice;
	
	
	@RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop=null;
		List<ProductCategory> productCategoryList=null;
		if(shopId!=-1)
		{
			shop=shopSerivice.getByShopId(shopId);
			productCategoryList=productCategorySerivice.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success",true);
		}else
		{
			modelMap.put("success",false);
			modelMap.put("errMsg","empty shopId");
		}
		
		return modelMap;
		
	}
	
	@RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByShop(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if((pageIndex>-1)&&(pageSize>-1)&&(shopId>-1))
		{
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategorytId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition4Search(shopId,productCategoryId,productName);
			ProductExecution pe=productSerivice.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
			
		}else
		{
			modelMap.put("success",false);
			modelMap.put("errMsg","enpty pageSize or pageIndex or shopId");
		}
		return modelMap;
		
	}

	private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId!=-1)
		{
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName!=null)
		{
			productCondition.setProductName(productName);
			
		}
		productCondition.setEnableStatus(1);
		return productCondition;
	}
	
}
