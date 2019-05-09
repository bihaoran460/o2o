package com.bihaoran.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bihaoran.o2o.dto.ImageHolder;
import com.bihaoran.o2o.dto.ShopExecution;
import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.entity.LocalAuth;
import com.bihaoran.o2o.entity.PersonInfo;
import com.bihaoran.o2o.entity.Shop;
import com.bihaoran.o2o.entity.ShopCategory;
import com.bihaoran.o2o.enums.ShopStateEnum;
import com.bihaoran.o2o.service.AreaService;
import com.bihaoran.o2o.service.LocalAuthService;
import com.bihaoran.o2o.service.ShopCategoryService;
import com.bihaoran.o2o.service.ShopService;
import com.bihaoran.o2o.util.CodeUtil;
import com.bihaoran.o2o.util.HttpServletRequestUtil;
import com.bihaoran.o2o.util.ImageUtil;
import com.bihaoran.o2o.util.PathUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhlabs.image.ShineFilter;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@Autowired LocalAuthService localAuthService;
	
	
	@RequestMapping(value = "/ownerlogincheck",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> loginCheck(HttpServletRequest request) 
	{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String userName=HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		if(userName!=null&&password!=null)
		{
			LocalAuth localAuth=localAuthService.getLocalAuthByUserNameAndPassword(userName, password);
			if(userName.equals(localAuth.getUsername())&&password.equals(localAuth.getPassword()))
			{
				modelMap.put("success", true);
				request.getSession().setAttribute("localAuth",localAuth);
			}else
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		}else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名或密码为空");
		}
		return modelMap;
		
	}
	
	@RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap =new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0)
		{
			Object currentShopObj=request.getSession().getAttribute("currentShop");
			if(currentShopObj==null)
			{
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else
			{
				Shop currentShop=(Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());	
			}
		}else
		{
			Shop currentShop=new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;	
	}
	@RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		LocalAuth localAuth=(LocalAuth) request.getSession().getAttribute("localAuth");
//		PersonInfo user=new PersonInfo();
//		user.setUserId(1L);
//		user.setName("test");
		PersonInfo user=localAuth.getPersonInfo();
//		request.getSession().setAttribute("user", user);
//		user=(PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition=new Shop();
			shopCondition.setPersonInfo(user);
			ShopExecution shopExecution=shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", shopExecution.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	@RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request)
	{
		Map<String, Object> modelMap=new HashMap<String, Object>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1)
		{
			try {
				Shop shop=shopService.getByShopId(shopId);
				List<Area> areaList=areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);		
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());			
			}
		}else
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	
	@RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		List<Area> areaList=new ArrayList<Area>();
		try {
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList=areaService.getAreaList();
			modelMap.put("shopCategoryList",shopCategoryList);
			modelMap.put("areaList",areaList);
			modelMap.put("success",true);
		}catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;		
	}
	@RequestMapping(value = "/registershop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(!CodeUtil.cheakVerifyCode(request))
		{
			modelMap.put("success",false);
			modelMap.put("errMsg","输入了错误的验证码!");
			return modelMap;
		}
		
		//1 接受并转发相应的参数，包括店铺信息和图片信息
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		//Json转化为实体类
		ObjectMapper mapper=new ObjectMapper();
		Shop shop=null;
		try{
			shop=mapper.readValue(shopStr,Shop.class);	
		}catch (Exception e) {
			modelMap.put("succsee",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;	// TODO: handle exception
		}
		CommonsMultipartFile shopImg=null;
		//文件上传解析器
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else
		{
			modelMap.put("succsee",false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}
		//2 店铺注册
		if(shop!=null&&shopImg!=null)
		{
			//PersonInfo personInfo=(PersonInfo)request.getSession().getAttribute("user");
			PersonInfo personInfo=new PersonInfo();
			personInfo.setUserId(1L);
			shop.setPersonInfo(personInfo);
//			File shopImgFile=new File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
//			try {
//				shopImgFile.createNewFile();
//			}catch(IOException e) 
//			{
//				modelMap.put("succsee",false);
//				modelMap.put("errMsg",e.getMessage());
//				return modelMap;
//			}
//			try {
//				inputStreamToFile(shopImg.getInputStream(), shopImgFile);
//			} catch (IOException e) {
//				modelMap.put("succsee",false);
//				modelMap.put("errMsg",e.getMessage());
//				return modelMap;
//			}
//			ShopExecution se=shopService.addShop(shop, shopImgFile);
			ShopExecution se;
			try {
				ImageHolder thumbnail=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop, thumbnail);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success",true);
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0) {
						shopList=new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList",shopList);
				}else {
					modelMap.put("succsee",false);
					modelMap.put("errMsg",se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("succsee",false);
				modelMap.put("errMsg",e.getMessage());
			}	
			return modelMap;
		}else {
			modelMap.put("succsee",false);
			modelMap.put("errMsg","请输入店铺信息");
			return modelMap;
		}
	}
//	private static void inputStreamToFile(InputStream ins,File file) {
//		FileOutputStream os=null;
//		try {
//			os=new FileOutputStream(file);
//			int bytesRead=0;
//			byte[] buffer=new byte[1024];
//			while((bytesRead=ins.read(buffer))!=-1) {
//				os.write(buffer, 0, bytesRead);
//			}
//		}catch(IOException e)
//		{
//			throw new RuntimeException("调用inputStreamToFile产生异常："+ e.getMessage());
//		}finally {
//			try {
//				if(os!=null) {
//					os.close();
//				}
//				if(ins!=null) {
//					ins.close();
//				}
//				
//			}catch (IOException e) {
//				
//				throw new RuntimeException("关闭inputStreamToFile产生异常："+ e.getMessage());
//			}
//		}
//	}
	@RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(!CodeUtil.cheakVerifyCode(request))
		{
			modelMap.put("success",false);
			modelMap.put("errMsg","输入了错误的验证码!");
			return modelMap;
		}
		
		//1 接受并转发相应的参数，包括店铺信息和图片信息
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		//Json转化为实体类
		ObjectMapper mapper=new ObjectMapper();
		Shop shop=null;
		try{
			shop=mapper.readValue(shopStr,Shop.class);	
		}catch (Exception e) {
			modelMap.put("succsee",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;	// TODO: handle exception
		}
		CommonsMultipartFile shopImg=null;
		//文件上传解析器
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}
		//2 店铺修改
		if(shop!=null&&shop.getShopId()!=null)
		{
			
			ShopExecution se;
			try {
			
				if(shopImg==null)
				{
					//这里不能传入NULL的参数
					ImageHolder thumbnail=new ImageHolder();
					se=shopService.modifyShop(shop,thumbnail);
				}
				else
				{
					ImageHolder thumbnail=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop,thumbnail);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}else {
					modelMap.put("succsee",false);
					modelMap.put("errMsg",se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("succsee",false);
				modelMap.put("errMsg",e.getMessage());
			}	
			return modelMap;
		}else {
			modelMap.put("succsee",false);
			modelMap.put("errMsg","请输入店铺Id");
			return modelMap;
		}
	}

}
