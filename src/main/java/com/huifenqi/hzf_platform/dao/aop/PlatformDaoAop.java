/** 
* Project Name: hzf_platform_project 
* File Name: PlatformDaoAop.java 
* Package Name: com.huifenqi.hzf_platform.dao.aop 
* Date: 2016年6月2日下午3:57:44 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.aop;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;

/**
 * ClassName: PlatformDaoAop date: 2016年6月2日 下午3:57:44 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Aspect
@Component
public class PlatformDaoAop {

	@Autowired
	private RedisCacheManager redisCacheManager;

	private static Log logger = LogFactory.getLog(PlatformDaoAop.class);

	private static final String PLATFORM_CUSTOMER_KEY = "customer.list.info";

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository.findByAppId(..)) && args(appId)")
	public PlatformCustomer findByAppId(ProceedingJoinPoint pjp, String appId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(PLATFORM_CUSTOMER_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(appId);
		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, PlatformCustomer.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取平台用户%s失败", hashKey.toString())));
		}

		PlatformCustomer platformCustomer = (PlatformCustomer) pjp.proceed();

		if (platformCustomer != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(platformCustomer));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存平台用户(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(platformCustomer))));
			}
		}

		return platformCustomer;
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository.findBySource(..)) && args(source)")
	public PlatformCustomer findBySource(ProceedingJoinPoint pjp, String source) throws Throwable {
		String key = RedisUtils.getInstance().getKey(PLATFORM_CUSTOMER_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(source);
		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, PlatformCustomer.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取平台用户%s失败", hashKey.toString())));
		}

		PlatformCustomer platformCustomer = (PlatformCustomer) pjp.proceed();

		if (platformCustomer != null) {
			try {
			
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(platformCustomer));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存平台用户(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(platformCustomer))));
			}
		}

		return platformCustomer;
	}
	
	//查询需要过滤的渠道列表
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository.findAllWithoutPermissions(..))")
	public List<PlatformCustomer> findAllWithoutPermissions(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(PLATFORM_CUSTOMER_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-permission");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<PlatformCustomer>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从平台用户列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<PlatformCustomer> platformCustomers = (List<PlatformCustomer>) pjp.proceed();

		if (platformCustomers != null && !platformCustomers.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(platformCustomers));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存平台用户列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(platformCustomers)))));
			}
		}
		return platformCustomers;
	}
	
	//查询需要图片美化的渠道列表
		@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository.findByisImgs(..))")
		public List<PlatformCustomer> findByisImgs(ProceedingJoinPoint pjp) throws Throwable {
			String key = RedisUtils.getInstance().getKey(PLATFORM_CUSTOMER_KEY);
			StringBuilder hashKey = new StringBuilder();
			hashKey.append("all-isimg");

			try {
				String result = redisCacheManager.getHash(key, hashKey.toString());
				if (result != null) {
					return GsonUtils.getInstace().fromJson(result, new TypeToken<List<PlatformCustomer>>() {
					}.getType());
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("从平台用户列表信息%s失败", hashKey.toString())));
			}

			@SuppressWarnings("unchecked")
			List<PlatformCustomer> platformCustomers = (List<PlatformCustomer>) pjp.proceed();

			if (platformCustomers != null && !platformCustomers.isEmpty()) {
				try {
					redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(platformCustomers));
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog(String.format("保存平台用户列表信息(%s=%s)到缓存失败", hashKey.toString(),
							GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(platformCustomers)))));
				}
			}
			return platformCustomers;
		}
	
}
