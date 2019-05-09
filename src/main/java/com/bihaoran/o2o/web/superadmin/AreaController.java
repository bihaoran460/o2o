package com.bihaoran.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.entity.ShopCategory;
import com.bihaoran.o2o.service.AreaService;
import com.bihaoran.o2o.service.ShopCategoryService;


@Controller
@RequestMapping("/superadmin")
public class AreaController {
	Logger logger=LoggerFactory.getLogger(AreaController.class);
	@Autowired AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@RequestMapping(value = "/listarea",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea()
	{
		logger.info("====strat====");
		long startTime=System.currentTimeMillis();
		Map<String, Object> modelMap=new HashMap<String, Object>();
		List<Area> list=new ArrayList<Area>();
		List<ShopCategory> list1=new ArrayList<ShopCategory>();
		try
		{
			list=areaService.getAreaList();
			list1=shopCategoryService.getShopCategoryList(new ShopCategory());
			modelMap.put("rows", list1);
			modelMap.put("total",list1.size());
		}catch(Exception e)
		{
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("erMsg",e.toString());
		}
		logger.error("test error");
		long endTime=System.currentTimeMillis();
		logger.debug("costTime:[{}ms]",endTime-startTime);
		logger.info("===end===");
		return modelMap;
		
	}

}
