package com.bihaoran.o2o.web.shopadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bihaoran.o2o.dto.ProductCategoryExecution;
import com.bihaoran.o2o.dto.Result;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.enums.ProductCategoryStateEnum;
import com.bihaoran.o2o.service.ProductCategoryService;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeProductCategory(Long productCategoryId,HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(productCategoryId!=null&&productCategoryId>0)
		{
			try
			{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState())
				{
					modelMap.put("success",true);
				}else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());		
				}
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
			
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
	@ResponseBody
	public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
//		Shop shop=new Shop();
//		shop.setShopId(19L);
//		request.getSession().setAttribute("currentShop", shop);
		
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> productCategoryList=null;
		if(currentShop!=null&&currentShop.getShopId()>0)
		{
			productCategoryList=productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, productCategoryList);
		}else
		{
			ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
		}
	}
	
	@RequestMapping(value = "/addproductcategorylist",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addProductCategoryList(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		for(ProductCategory pc:productCategoryList)
		{
			pc.setShopId(currentShop.getShopId());
			pc.setCreateTime(new Date());
		}
		if(productCategoryList!=null&&productCategoryList.size()>0)
		{
			try {
				ProductCategoryExecution pe=productCategoryService.batchAddProductCategory(productCategoryList);
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState())
				{
					modelMap.put("success", true);
				}else
				{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;	
			}
		}else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg","请至少输入一个商品类别");
		}
		return modelMap;	
	}
	
	
	
	

}
