/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseDaoAop.java 
 * Package Name: com.huifenqi.hzf_platform.dao.aop 
 * Date: 2016年5月21日下午12:20:37 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.dao.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.entity.house.Complaint;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;


/** 
 * ClassName: ComplaintDaoAop
 * date: 2016年5月21日 下午12:20:37
 * Description: 投诉DAO的处理
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Aspect
@Component
public class ComplaintDaoAop {
	
	@Autowired
	private RedisCacheManager redisCacheManager;
	
	private static Log logger = LogFactory.getLog(ComplaintDaoAop.class);
	
	private static final String  COMPLAINT_KEY = "house.complaint.sellId";

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ComplaintRepository.save(..))", returning = "retObj")
	public void afterSave(JoinPoint joinPoint, Object retObj) {
		Complaint complaint = (Complaint) retObj;
		String key = RedisUtils.getInstance().getKey(COMPLAINT_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(complaint.getSellId()).append("-").append(complaint.getUid()).append("-")
				.append(complaint.getRoomId());
		try {
			redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(complaint));
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(
					String.format("保存投诉信息(%s=%s)到缓存失败", hashKey.toString(), GsonUtils.getInstace().toJson(complaint))));
		}
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ComplaintRepository.getNumByUidAndSellIdAndRoomId(..)) && args(uid,sellId,roomId)")
	public int BeforeGetNumByUidAndSellIdAndRoomId(ProceedingJoinPoint pjp, String uid, String sellId, long roomId)
			throws Throwable {
		String key = RedisUtils.getInstance().getKey(COMPLAINT_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(sellId).append("-").append(uid).append("-").append(roomId);
		try {
			String reuslt = redisCacheManager.getHash(key, hashKey.toString());
			if (reuslt != null) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取投诉信息%s失败", hashKey.toString())));

		}
		return (int) pjp.proceed();
	}
}
