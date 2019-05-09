package com.bihaoran.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ProductExecution;
import com.bihaoran.o2o.entity.Product;
import com.bihaoran.o2o.entity.ProductCategory;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.enums.ProductStateEnum;
import com.bihaoran.o2o.service.ProductCategoryService;
import com.bihaoran.o2o.service.ProductService;
import com.bihaoran.o2o.util.CodeUtil;
import com.bihaoran.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	
	private static final int IMAGEMAXCOUNT=6;
	
	@RequestMapping(value = "/updatestatus",method =RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> updateStatus(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
//		//验证码校验
//		if(!CodeUtil.cheakVerifyCode(request))
//		{
//			modelMap.put("success", false);
//			modelMap.put("errMsg", "验证码错误");
//			return modelMap;
//		}
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		try
		{
			product=mapper.readValue(productStr, Product.class);
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		if(product!=null)
		{
			try
			{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=productService.updateStatus(product);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
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
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;	
	}
	
	@RequestMapping(value = "/getproductlistbyshop",method =RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null))
		{
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
			ProductExecution pe=productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success",true);
			
		}else
		{
			modelMap.put("success",false);
			modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
		}
		return modelMap;
		
	}
	
	private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId!=-1L)
		{
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName!=null)
		{
			productCondition.setProductName(productName);
		}
		
		return productCondition;
	}

	@RequestMapping(value = "/getproductbyid",method =RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(productId>-1)
		{
			Product product=productService.getProductById(productId);
			
			List<ProductCategory> productCategoryList=productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);		
		}else
		{
			modelMap.put("success", false);		
			modelMap.put("errMsg","enmty productId");		
		}
		return modelMap;
	}
	@RequestMapping(value = "/modifyproduct",method =RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		boolean statusChange=HttpServletRequestUtil.getBoolean(request, "statusChange");
		
		if(!statusChange&&!CodeUtil.cheakVerifyCode(request))
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		
		MultipartHttpServletRequest multipartRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgHolderList=new ArrayList<ImageHolder>();
		//获取到文件流
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//判断请求中是否存在文件流
			if(multipartResolver.isMultipart(request))
			{
				thumbnail = handleImage(request, productImgHolderList);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try
		{
			String productStr=HttpServletRequestUtil.getString(request, "productStr");
			product=mapper.readValue(productStr, Product.class);
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		if(product!=null)
		{
			try
			{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgHolderList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
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
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;	
	}
	
	@RequestMapping(value = "/addproduct",method =RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//验证码校验
		if(!CodeUtil.cheakVerifyCode(request))
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//商品 缩略图 详情图列表
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		MultipartHttpServletRequest multipartRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		//获取到文件流
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//判断请求中是否存在文件流
			if(multipartResolver.isMultipart(request))
			{
				thumbnail = handleImage(request, productImgList);
			}else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片为空");
				return modelMap;
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try
		{
			product=mapper.readValue(productStr, Product.class);
		}catch(Exception e)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		if(product!=null&&thumbnail!=null&&productImgList.size()>0)
		{
			try
			{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				Shop shop=new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState())
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
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
		
	}
	private ImageHolder handleImage(HttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
		MultipartHttpServletRequest multipartRequest;
		ImageHolder thumbnail;
		multipartRequest=(MultipartHttpServletRequest) request;
		
		CommonsMultipartFile thumbnailFile=(CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());//得到缩略图文件流和文件名
		//得到图片详情图文件流和文件名
		for(int i=0;i<IMAGEMAXCOUNT;i++)
		{
			CommonsMultipartFile productImgFile=(CommonsMultipartFile) 
					multipartRequest.getFile("productImg"+i);
			if(productImgFile!=null)
			{
				ImageHolder productImg=new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);
			}else
			{
				break;
			}
		}
		return thumbnail;
	}

}
