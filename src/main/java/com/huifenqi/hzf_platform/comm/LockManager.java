/**
 * Project Name: usercomm_smart
 * File Name: LockManager.java
 * Package Name: com.huifenqi.usercomm.lock
 * Date: 2017年3月11日下午2:19:45
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved.
 *
 */
package com.huifenqi.hzf_platform.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: LockManager
 * date: 2017年3月11日 下午2:19:45
 * Description: 
 *
 * @author xiaozhan
 * @version
 * @since JDK 1.8 
 */
@Component
public class LockManager {

	@Autowired
	private RedisClient redisClient;

	//锁的过期时间,单位为ms
	private static final int LOCK_TIME_OUT = 15000;

    /**
     * 获取锁
     * @param lockResource
     * @return
     */
    public boolean lock(String lockResource) {
        return lock(lockResource,LOCK_TIME_OUT);
    }

	/**
	 * 获取锁
	 * @param lockResource
	 * @return
	 */
	public boolean lock(String lockResource,long timeout) {

		long now = System.currentTimeMillis();

		long timeStamp = now + timeout;

		// 获取锁
		boolean lock = redisClient.setNx(lockResource, String.valueOf(timeStamp));

		// 判断是否获取锁成功，其满足的条件为：
		// 1:lock为true时
		// a:lock为true，设置成功
		// 2:lock为false时
		// a:判断是否设置过期时间，-1代表永久不过期，如果有过期时间则等待过期后再重新获取锁
		// b:锁的时间戳小于当期时间，代表锁已经过期
		// c:重新更新锁的时间戳,看返回值是否小于当前值，否则代表已经被别人设置成功
		if (StringUtil.isNotBlank(String.valueOf(redisClient.get(lockResource))) && StringUtil.isNotBlank(redisClient.getSet(lockResource, String.valueOf(timeStamp)))) {
			if (lock || (redisClient.getExpire(lockResource) == -1
					&& now > Long.valueOf(String.valueOf(redisClient.get(lockResource)))
					&& now > Long.valueOf(redisClient.getSet(lockResource, String.valueOf(timeStamp))))) {
				// 设置过期时间
				redisClient.expire(lockResource, timeout);
				return true;
			}
		}

		// 锁获取失败
		return false;
	}


	/**
	 * 释放锁
	 * @param lockResource
	 * @return
	 */
	public void unLock(String lockResource) {
		//删除Key
		redisClient.delete(lockResource);
	}

}
