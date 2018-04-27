/** 
* Project Name: trunk 
* File Name: MimeUtils.java 
* Package Name: com.huifenqi.usercomm.utils 
* Date: 2016年3月21日下午3:30:48 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import com.huifenqi.hzf_platform.context.enums.ImgTypeEnums;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * ClassName: FileUtils date: 2016年3月21日 下午3:30:48 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class FileUtils {

	private static final Log logger = LogFactory.getLog(FileUtils.class);

	private static BufferedImage wartermarkImage = null;

	private static final Object syncObject = new Object();

	private static final String SAVE_PATH = "/tmp/house_image/";
	private static final String ALI_PATH_PREFIX = "hr_image/";

	public static File createFile(File file) throws IOException {
		if (!file.exists()) {
			makeDir(file.getParentFile());
		}
		file.createNewFile();
		return file;
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	/**
	 * 根据ContentType获取扩展名
	 * 
	 * @param contentType
	 * @return
	 */
	public static String getExtension(String contentType) {

		if (StringUtil.isEmpty(contentType)) {
			return null;
		}

		// "image/jpg"类型的特殊处理，tika库不识别，强制转换下
		String contentTypeToProcess = contentType;
		if (contentTypeToProcess.equals("image/jpg")) {
			contentTypeToProcess = "image/jpeg";
		}

		String extension = null;

		MimeTypes types = MimeTypes.getDefaultMimeTypes();
		MimeType type = null;
		try {
			type = types.forName(contentTypeToProcess);
		} catch (MimeTypeException e) {
			logger.error(LogUtils.getCommLog("failed to get mime type from contentType, contentType:"
					+ contentTypeToProcess + ", " + e.getLocalizedMessage()));
		}

		if (type != null) {
			extension = type.getExtension();
		}

		return extension;

	}

	/**
	 * 压缩文件
	 *
	 * @param cWidth
	 *            压缩宽度
	 * @param cHeight
	 *            压缩高度
	 * @return
	 */
	public static String saveAndUploadImage(InputStream inputStream, int cWidth, int cHeight, OSSClientUtils OSSClient,
			String sellId, String imgName) throws Exception {
		try {
			//拼接oss服务路径hr_image/HF047600604/
			String aliPath = ALI_PATH_PREFIX + sellId + File.separator;
			
			//将输入流转换图片字符流
			BufferedImage oldImage = ImageIO.read(inputStream);
			int width = oldImage.getWidth();//图片宽
			int height = oldImage.getHeight();//图片高
			
			//创建文件/tmp/house_image/
			File fullImg = FileUtils.createFile(new File(SAVE_PATH + imgName));
			
			//图片存储本地
			Thumbnails.of(oldImage).size(width, height).toFile(fullImg);
			if(height > width){
				logger.info("图片为竖向图片，需要进行裁剪");
				String erect = "_("+height+","+width+")";
				imgName = imgName.replace(ImgTypeEnums.DEFAULT.getCode(), erect);		
			}
			//上传阿里oss
			OSSClient.upload(fullImg, aliPath + imgName);
			//删除本地图片
			fullImg.delete();
			return aliPath + imgName;
		} catch (IOException e) {
			logger.error("保存图片出错");
			logger.error(LogUtils.getCommLog(e.getMessage()));
			throw e;
		}
//		try {
//			String aliPath = ALI_PATH_PREFIX + sellId + File.separator;
//			String returnPath = "";
//
//			BufferedImage oldImage = ImageIO.read(inputStream);
//			int width = oldImage.getWidth();
//			int height = oldImage.getHeight();

//			if (height > width) {//竖向图片
//				logger.info("图片为竖向图片，需要进行裁剪");				
//				//图片后缀替换
//				String fullImgName = imgName.replace(ImgTypeEnums.ORIGINAL.getCode(), ImgTypeEnums.FULL.getCode());		
//				//创建文件
//				//File fullImg = FileUtils.createFile(new File(SAVE_PATH + fullImgName));				
//				//图片打水印  图片压缩
////				Thumbnails.of(oldImage).size(cHeight, cWidth).watermark(Positions.BOTTOM_RIGHT, getWarterMark(), 0.5f)
////						.toFile(fullImg);
//				
//				//图片后缀替换
//				String partImgName = imgName.replace(ImgTypeEnums.ORIGINAL.getCode(), ImgTypeEnums.PART.getCode());
//				File partImg = FileUtils.createFile(new File(SAVE_PATH + partImgName));
//
//				int cutHeight = (int) (width * 0.75);
//				Thumbnails.of(oldImage).sourceRegion(Positions.CENTER, width, cutHeight).size(width, cutHeight)
//						.keepAspectRatio(false).watermark(Positions.BOTTOM_RIGHT, getWarterMark(), 0.5f)
//						.toFile(partImg);
//
//				//OSSClient.upload(fullImg, aliPath + fullImgName);
//				OSSClient.upload(partImg, aliPath + partImgName);
//				//fullImg.delete();// 删除临时文件
//				partImg.delete();
//				returnPath = aliPath + fullImgName;
//			} else {// 横向图片
//				logger.info("横向图片>压缩");
//				String normalImgName = imgName.replace(ImgTypeEnums.ORIGINAL.getCode(), "");
//				File fullImg = FileUtils.createFile(new File(SAVE_PATH + normalImgName));
//				Thumbnails.of(oldImage).size(cWidth, cHeight).watermark(Positions.BOTTOM_RIGHT, getWarterMark(), 0.5f)
//						.toFile(fullImg);
//				OSSClient.upload(fullImg, aliPath + normalImgName);
//				fullImg.delete();
//				returnPath = aliPath + normalImgName;
//			}
//
//			//下载原图，并上传到阿里云
//			File originalImg = new File(SAVE_PATH + imgName);
//			Thumbnails.of(oldImage).size(width, height).toFile(originalImg);
//			OSSClient.upload(originalImg, aliPath + imgName); // 上传原图
//			originalImg.delete();
//			logger.info("原图上传成功");
//			return returnPath;

	}

	/**
	 * 从响应中获取文件名
	 * 
	 * @param response
	 * @return
	 */
	public static String getFileName(CloseableHttpResponse response) {
		String fileName = getFileNameFromContentDisposition(response);
		if (StringUtil.isEmpty(fileName)) {
			fileName = getFileExtensionFromContentType(response);
		}
		return fileName;
	}

	/**
	 * 获取response header中Content-Disposition中的filename值
	 * 
	 * @param response
	 * @return
	 */
	public static String getFileNameFromContentDisposition(CloseableHttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values != null && values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}

	/**
	 * 获取response header中Content-Type中的文件扩展名
	 * 
	 * @param response
	 * @return
	 */
	public static String getFileExtensionFromContentType(CloseableHttpResponse response) {
		String extension = null;

		Header contentTypeHeader = response.getFirstHeader("Content-Type");
		if (contentTypeHeader == null) {
			return null;
		}

		HeaderElement[] values = contentTypeHeader.getElements();
		if (values == null || values.length != 1) {
			return null;
		}

		String name = values[0].getName();
		if (StringUtil.isNotEmpty(name)) {
			extension = FileUtils.getExtension(name);
		}

		return extension;
	}

	/**
	 * 获取文件的后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFexName(String fileName) {
		if (fileName.lastIndexOf(".") == -1) {
			return null;
		}
		String filefex = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (filefex.length() > 4) {
			if (filefex.indexOf("@") != -1) {
				filefex = filefex.substring(0, filefex.indexOf("@"));
			}
		}
		return filefex;
	}

	/**
	 * 获取水印
	 * 
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage getWarterMark() throws IOException {
		if (wartermarkImage != null) {
			return wartermarkImage;
		}
		synchronized (syncObject) {
			if (wartermarkImage != null) {
				return wartermarkImage;
			}
			wartermarkImage = ImageIO.read(new File("config/watermark.png"));
			return wartermarkImage;
		}
	}
}
