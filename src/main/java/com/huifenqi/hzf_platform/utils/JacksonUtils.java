/** 
 * Project Name: hzf_platform_project 
 * File Name: JasonUtils.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年4月26日下午7:59:14 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.utils;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/** 
 * ClassName: JasonUtils
 * date: 2016年4月26日 下午7:59:14
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class JacksonUtils {
	
	//设置时间格式
	private  static final  XmlMapper xmlMapper = new XmlMapper();
	static {
		xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
	
	public static XmlMapper getXmlMapper() {
		return xmlMapper;
	}
}
