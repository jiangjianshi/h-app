/** 
 * Project Name: mq_project 
 * File Name: RedisUtils.java 
 * Package Name: com.huifenqi.mq.utils 
 * Date: 2015年11月30日下午7:52:30 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.utils;

/** 
 * ClassName: RedisUtils
 * date: 2015年11月30日 下午7:52:30
 * Description: Redis操作的工具类
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class RedisUtils {
	
	private static final RedisUtils instance = new RedisUtils();
	
	private RedisUtils() {
		
	}
	
	public static RedisUtils getInstance() {
		return instance;
	}
	
	/**
	 * 判断redis返回的值是否为空(例如：NULL,"null","")
	 * @param value
	 * @return
	 */
	public boolean isBlank(String value) {
		if (value == null || "null".equals(value.trim().toLowerCase()) || "".equals(value.trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 找房系统的前缀
	 * @return
	 */
	public String getSystemKeyPrefix() {
		return "hzf_platform-";
	}
	
	/**
	 * 获取当前系统的key
	 * @param key
	 * @return
	 */
	public String getKey(String key) {
		return getSystemKeyPrefix() + key;
	}
}
