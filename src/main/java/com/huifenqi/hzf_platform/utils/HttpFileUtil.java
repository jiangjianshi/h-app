package com.huifenqi.hzf_platform.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http传送文件
 * 
 * @author changmingwei
 *
 */
public class HttpFileUtil {

	private final static Logger logger = LoggerFactory.getLogger(HttpFileUtil.class);
	
	//链接超时 5s
	private static int connectTimeout = 5000;
	//读取超时 5s
	private static int requestTimeout = 5000;
	//请求超时 20s
	private static int socketTimeout = 20000;
	
	
	public static String post(String url, Map<String, Object> param) throws IOException {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(requestTimeout)
				.build();
		
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		ContentType content_type = ContentType.create("text/plain", Consts.UTF_8);
		ContentType file_type = ContentType.create("application/json", Consts.UTF_8);

		try {
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setCharset(Consts.UTF_8);
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //以浏览器兼容模式运行，防止文件名乱码。
			for (String key : param.keySet()) {
				if (param.get(key) instanceof File) {
					FileBody bin = new FileBody((File) param.get(key),file_type);
					entityBuilder.addPart(key, bin);
				} else {
					StringBody content = new StringBody((String) param.get(key), content_type);
					entityBuilder.addPart(key, content);
				}
			}
			HttpEntity reqEntity = entityBuilder.build();
			httpPost.setEntity(reqEntity);
			logger.info("发起请求的地址：{}", httpPost.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				logger.info("请求状态：{}", response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					String jsonData = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
					logger.info("响应内容: ：{}", jsonData);
					return jsonData;
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return null;
	}
	
	/**
	 * 获取后缀名
	 * @param url
	 * @return
	 */
	public static String getUrlExtendtion(String url){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String extendtion = url.substring(url.lastIndexOf("."), url.indexOf("?"));
		return extendtion;
	}
	
	/**
	 * 通过URL获取输入流
	 * @param strUrl
	 * @return
	 */
	public static InputStream getInputStreamByUrl(String strUrl){  
        try { 
            
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//获取输入流 
            return inStream;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	
}
