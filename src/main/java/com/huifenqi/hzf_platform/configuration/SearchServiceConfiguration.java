/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月4日下午2:55:16 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: BaiduConfiguration
 * date: 2016年5月4日 下午2:55:16
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class SearchServiceConfiguration {
	
	@Value("${hzf.search.service.api.ip}")
	private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
	
	
}
