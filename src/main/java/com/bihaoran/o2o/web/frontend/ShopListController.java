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

import com.bihaoran.o2o.dto.ShopExecution;
import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.entity.ShopCategory;
import com.bihaoran.o2o.service.AreaService;
import com.bihaoran.o2o.service.ShopCategoryService;
import com.bihaoran.o2o.service.ShopService;
import com.bihaoran.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		long parentId=HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList=null;
		if(parentId!=-1)
		{
			try
			{
				ShopCategory shopCategoryCondition=new ShopCategory();
				ShopCategory parent=new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList=shopCategoryService.getShopCategoryList(shopCategoryCondition);				
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());			
			}
		}else
		{
			try
			{
				shopCategoryList=shopCategoryService.getShopCategoryList(null);
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
		}
		modelMap.put("shopCategoryList",shopCategoryList);
		List<Area> areaList=null;
		try
		{
			areaList=areaService.getAreaList();
			modelMap.put("areaList",areaList);
			modelMap.put("success", true);
			return modelMap;		
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;		
	}
	
	@RequestMapping(value = "/listshops",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		if((pageIndex>-1)&&(pageSize>-1))
		{
			long parentId=HttpServletRequestUtil.getLong(request, "parentId");
			long shopCategoryId=HttpServletRequestUtil.getLong(request, "shopCategoryId");
			int areaId=HttpServletRequestUtil.getInt(request, "areaId");
			String shopName=HttpServletRequestUtil.getString(request, "shopName");
			Shop shopCondition=compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
			ShopExecution se=shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
		
	}

	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition=new Shop();
		if(parentId!=-1L)
		{
			ShopCategory childCategory=new ShopCategory();
			ShopCategory parentCategory=new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);	
		}
		if(shopCategoryId!=-1L) 
		{
			ShopCategory shopCategory=new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);		
		}
		if(areaId!=-1)
		{
			Area area=new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if(shopName!=null)
		{
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
	
}
