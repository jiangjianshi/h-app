package com.house.asz;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {

	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	private static final String charset = "utf-8";

	/**
	 * 请求
	 * 
	 * @param reqURL 请求URL
	 * @param jsonStr json参数
	 * @return
	 */
	public static String postJson(String reqURL, String jsonStr) {
		CloseableHttpClient httpclient = HttpClients.custom().build();
		try {
			RequestBuilder builder = RequestBuilder.post(reqURL);
			builder.setCharset(Charset.forName(charset));
			StringEntity stringEntity = new StringEntity(jsonStr, charset);
			stringEntity.setContentEncoding(charset);
			stringEntity.setContentType("application/json"); 
			builder.setEntity(stringEntity);
			HttpUriRequest post = builder.build();
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, charset);
				EntityUtils.consume(entity);
				return result;
			} finally {
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}

}
