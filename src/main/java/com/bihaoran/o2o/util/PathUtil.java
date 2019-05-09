package com.bihaoran.o2o.util;

public class PathUtil {
	private static String seperator=System.getProperty("file.separator");
	public static String getImgBasePath() {
		
		String os=System.getProperty("os.name");//获取操作系统
		String basePath="";
		if(os.toLowerCase().startsWith("win")) {
			basePath="D:/projectdev/image/";
		}else {
			basePath="/home/bihaoran/image/";
		}
		basePath=basePath.replace("/", seperator);
		return basePath;
		
	}
	public static String getShopImagePath(long shopId) {
		String imagePath="upload/item/shop/" + shopId +"/";
		return imagePath.replace("/", seperator);
	}
}
