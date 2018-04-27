/** 
 * Project Name: usercomm_project 
 * File Name: FileService.java 
 * Package Name: com.huifenqi.usercomm.service 
 * Date: 2016年1月12日下午4:11:50 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.service;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.configuration.FileConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.enums.ImgTypeEnums;
import com.huifenqi.hzf_platform.utils.FileUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.MsgUtils;
import com.huifenqi.hzf_platform.utils.OSSClientUtils;

/**
 * ClassName: FileService date: 2016年1月12日 下午4:11:50 Description: 文件服务类
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class FileService {

	private static final Log logger = LogFactory.getLog(FileService.class);

	@Resource
	private FileConfiguration fileConfiguration;
	@Resource
	private OSSClientUtils OSSClient;

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @return 返回本地的URL
	 * @throws Exception
	 */
	/**
	 * public String downFile(String url) throws Exception { String savePath =
	 * downFile(url, createSavePath(), true); return savePath; }
	 */

	/**
	 * 下载文件
	 * 
	 * @param url
	 *            下载的URL
	 * @param savePath
	 *            保存路径
	 * @param isCompress
	 *            是否压缩
	 * @return 文件保存的路径
	 */
	public String downFile(String url, String sellId) throws Exception {
		// 调置http请求连接保持时间，防止大图片下载不成功。
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).setStaleConnectionCheckEnabled(true).build();

		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

		try {
			RequestBuilder get = RequestBuilder.get();
			get.setUri(url);

			CloseableHttpResponse response = httpclient.execute(get.build());
			InputStream instream = null;
			try {
				HttpEntity entity = response.getEntity();
				logger.info(LogUtils.getCommLog(entity.toString()));
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != Constants.Common.HTTP_STATUS_CODE_OK) {
					String result = EntityUtils.toString(entity);
					logger.info(LogUtils.getCommLog(String.format("The result of downloading file is %s", result)));
					throw new Exception(String.format("下载文件出错%s", result));
				}
				instream = entity.getContent();
				String imgName = createFileName(FileUtils.getFileFexName(url));
				String savePath = FileUtils.saveAndUploadImage(instream, fileConfiguration.getImageStandardWidth(),
						fileConfiguration.getImageStandardheight(), OSSClient, sellId, imgName);
				
				return savePath;
			} finally {
				if (instream != null) {
					instream.close();
				}
				response.close();
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("从服务器下载文件失败! url=" + url + " " + e));
			throw new Exception("从服务器下载文件失败!");
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(LogUtils.getCommLog(e.toString()));
			}
		}
	}
	/**
	 * 下载文件
	 * 
	 * @param url
	 *            下载的URL
	 * @param savePath
	 *            保存路径
	 * @param isCompress
	 *            是否压缩
	 * @return 文件保存的路径
	 */
	public String downImage(String url, String sellId) throws Exception {
		// 调置http请求连接保持时间，防止大图片下载不成功。
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).setStaleConnectionCheckEnabled(true).build();

		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

		try {
			RequestBuilder get = RequestBuilder.get();
			get.setUri(url);
			logger.info("图片地址：" + url);
			CloseableHttpResponse response = httpclient.execute(get.build());
			InputStream instream = null;
			try {
				HttpEntity entity = response.getEntity();
				logger.info(LogUtils.getCommLog(entity.toString()));
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != Constants.Common.HTTP_STATUS_CODE_OK) {
					String result = EntityUtils.toString(entity);
					logger.info("http状态码：" + statusCode);
					logger.info(LogUtils.getCommLog(String.format("The result of downloading file is %s", result)));
					throw new Exception(String.format("下载图片http请求出错%s", result));
				}
				instream = entity.getContent();
				String imgName = createFileName(FileUtils.getFileFexName(url));
				String savePath = FileUtils.saveAndUploadImage(instream, fileConfiguration.getImageStandardWidth(),
						fileConfiguration.getImageStandardheight(), OSSClient, sellId, imgName);

				return savePath;
			} finally {
				if (instream != null) {
					instream.close();
				}
				response.close();
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("图片下载失败! url=" + url + " " + e));
			throw new Exception("图片下载失败!");
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(LogUtils.getCommLog(e.toString()));
			}
		}
	}

	/**
	 * 生产文件名 时间+8位的随机串
	 * 
	 * @param suffix
	 *            后缀名
	 * @return
	 */
	public String createFileName(String suffix) {
		StringBuilder fileName = new StringBuilder();
		fileName.append(System.currentTimeMillis());
		fileName.append(MsgUtils.generateNoncestr(8));
		fileName.append(ImgTypeEnums.DEFAULT.getCode());
		fileName.append(".").append(suffix);
		return fileName.toString();
	}

	/**
	 * 根据名字的hash
	 * 
	 * @return
	 */
	/**
	 * public String createSavePath() { StringBuilder pathBuf = new
	 * StringBuilder(); pathBuf.append(fileConfiguration.getBaseSavePath());
	 * Random random = new Random(System.currentTimeMillis());
	 * pathBuf.append(getHexValue(Integer.toHexString(random.nextInt(fileConfiguration.getSaveDirSize()))));
	 * pathBuf.append("/");
	 * pathBuf.append(getHexValue(Integer.toHexString(random.nextInt(fileConfiguration.getSaveDirSize()))));
	 * pathBuf.append("/"); return pathBuf.toString(); }
	 */

	/**
	 * 获取16进制的值
	 * 
	 * @param value
	 * @return
	 */
	private String getHexValue(String value) {
		if (value.length() == 1) {
			StringBuilder hexValue = new StringBuilder();
			hexValue.append("0").append(value);
			return hexValue.toString();
		}
		return value;
	}

}
