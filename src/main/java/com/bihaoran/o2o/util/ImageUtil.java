package com.bihaoran.o2o.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bihaoran.o2o.dto.ImageHolder;

import ch.qos.logback.classic.Logger;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	public static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	public static final Random r=new Random();
	
	/*public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("D:\\Game\\ceshi.jpg")).size(200, 200)
		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f)
		.toFile("D:\\Game\\ceshinew.jpg");
		
	}*/
	

	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		String realFileName=getRandomFileName();
		String extension=getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr="/"+targetAddr + realFileName +extension;
		File dest=new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200,200)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
			.outputQuality(0.8f).toFile(dest);
			}catch(IOException e) {
				e.printStackTrace();
			}
		return relativeAddr;
	}
	/**
	 * 创建目标路径所涉及到的目录
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		// TODO Auto-generated method stub
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath=new File(realFileParentPath);
		if(!dirPath.exists())
		{
			dirPath.mkdirs();
		}
		
	}
	/**
	 * 获取输入文件流扩展名
	 * @param thumbnail
	 * @return
	 */
	public static String getFileExtension(String fileName) {
//		String originalFileName=cFile.getName();
		return fileName.substring(fileName.lastIndexOf("."));
	}
	public static String getRandomFileName() {
		// 获取随机五位数
		int rannum=r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
		return nowTimeStr+rannum;
	}
	/**
	 * 判断storePath是文件路径还是目录的路径
	 * 如果是文件路径则删除文件，
	 * 如果是目录路径，则删除目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath)
	{
		File fileOrPath=new File(PathUtil.getImgBasePath() + storePath);
		if(fileOrPath.exists())
		{
			if(fileOrPath.isDirectory())
			{
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr) {
		//获取不重复随机名
		String realFileName=getRandomFileName();
		//获取文件扩展名
		String extension=getFileExtension(thumbnail.getImageName());
		//如果目标路径不存在则自动创建
		makeDirPath(targetAddr);
		//获取文件储存的相对路径
		String relativeAddr="/"+targetAddr + realFileName +extension;
		//获取文件要保存的目标路径
		File dest=new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			//生成带水印的图片
			Thumbnails.of(thumbnail.getImage()).size(337,640)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
			.outputQuality(0.9f).toFile(dest);
			}catch(IOException e) {
				throw new RuntimeException("缩略图创建失败："+e.getMessage());
			}
		return relativeAddr;
	}

}
