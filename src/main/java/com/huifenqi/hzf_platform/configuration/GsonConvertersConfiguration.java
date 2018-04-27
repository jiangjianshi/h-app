/** 
 * Project Name: hzf_platform_project 
 * File Name: GsonConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年4月26日下午3:25:26 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import com.huifenqi.hzf_platform.utils.GsonUtils;

/** 
 * ClassName: GsonConfiguration
 * date: 2016年4月26日 下午3:25:26
 * Description: Gson的配置
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Configuration
public class GsonConvertersConfiguration {
	
	@Bean
    public HttpMessageConverters GsonMessageConverters(GsonHttpMessageConverter gsonHttpMessageConverter) {
		gsonHttpMessageConverter.setGson(GsonUtils.getInstace());
        return new HttpMessageConverters(gsonHttpMessageConverter);
    }
}
