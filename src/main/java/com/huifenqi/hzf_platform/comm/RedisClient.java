package com.huifenqi.hzf_platform.comm;

import com.huifenqi.hzf_platform.context.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by arison on 2015/10/14.
 */
@Component
public class RedisClient {

	private static Logger logger = LoggerFactory.getLogger(RedisClient.class);

	private static final String LOG_PREFIX = "[REDIS]";
	@Resource(name = "usercommRedisTemplate")
	@Autowired
	private StringRedisTemplate redisTemplate;


	@Autowired
	private UsercommRedisConfig redisConfig ;

	public void setInMap(String key, String hashKey, Object hashValue) {
		String fullKey = getKey(key);
		
		redisTemplate.opsForHash().put(fullKey, hashKey, hashValue.toString());

		logger.info(
				LOG_PREFIX + "cached by using map, key=" + fullKey + ", hashKey=" + hashKey + ", value=" + hashValue);
	}

	public void setAllInMap(String key, Map<String, String> properties) {

		String fullKey = getKey(key);

		redisTemplate.opsForHash().putAll(fullKey, properties);

		logger.info(LOG_PREFIX + "cached by using map, key=" + key + ", properties=" + properties);
	}

	public void setInMap(String key, Map<String, String> properties) {

		redisTemplate.opsForHash().putAll(key, properties);

		logger.info(LOG_PREFIX + "cached by using map, key=" + key + ", properties=" + properties);
	}

	public Object getFromMap(String key, String hashKey) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "get from map, key=" + fullKey + ", hashKey=" + hashKey);

		return redisTemplate.opsForHash().get(fullKey, hashKey);
	}

	public boolean hasKeyFromMap(String key, String hashKey) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "test if has key in map, key=" + fullKey + ", hashKey=" + hashKey);

		return redisTemplate.opsForHash().hasKey(fullKey, hashKey);
	}

	public void deleteFromMap(String key, String hashKey) {
		String fullKey = getKey(key);

		redisTemplate.opsForHash().delete(fullKey, hashKey);

		logger.info(LOG_PREFIX + "delete from map, key=" + fullKey + ", hashKey=" + hashKey);
	}

	public Map<Object, Object> getMap(String key) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "get all map, key=" + fullKey);

		return redisTemplate.opsForHash().entries(fullKey);
	}

	public void set(String key, Object value, long timeout) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "set object, key=" + fullKey + ", value=" + value.toString() + ", timeout=" + timeout);

		redisTemplate.opsForValue().set(fullKey, value.toString(), timeout, TimeUnit.MILLISECONDS);
	}

	public void set(String key, Object value, long timeout, TimeUnit unit) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "set object, key=" + fullKey + ", value=" + value.toString() + ", timeout=" + timeout);

		redisTemplate.opsForValue().set(fullKey, value.toString(), timeout, unit);
	}

	public Object get(String key) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "get object, key=" + fullKey);

		return redisTemplate.opsForValue().get(fullKey);
	}

	public void delete(String key) {
		String fullKey = getKey(key);

		redisTemplate.delete(fullKey);

		logger.info(LOG_PREFIX + "delete object, key=" + fullKey);
	}

	private String getKey(String name) {
		return Constants.CACHE_KEY_PREFIX + "-" + name;
	}

	public void set(String key, Object value) {
		String fullKey = getKey(key);

		logger.info(LOG_PREFIX + "set object, key=" + fullKey + ", value=" + value.toString());

		redisTemplate.opsForValue().set(fullKey, value.toString());
	}

	public Long increment(String key, long delta) {
		String fullKey = getKey(key);

		Long returnValue = redisTemplate.opsForValue().increment(fullKey, delta);

		logger.info(
				LOG_PREFIX + "increase object, key=" + fullKey + ", delta=" + delta + ", returnValue=" + returnValue);

		return returnValue;
	}
	
	public boolean  setNx(String key, String value) {
		String fullKey = getKey(key);
		Boolean returnValue = redisTemplate.opsForValue().setIfAbsent(fullKey, value);
		logger.info(
				LOG_PREFIX + "setnx object, key=" + fullKey + ", value=" + value + ", returnValue=" + returnValue);

		return returnValue;
	}

	public String getSet(String key, String value) {
		String fullKey = getKey(key);
		String returnValue = redisTemplate.opsForValue().getAndSet(fullKey, value);
	
		logger.info(
				LOG_PREFIX + "getSet object, key=" + fullKey + ", value=" + value + ", returnValue=" + returnValue);

		return returnValue;
	}
	
	public void  expire(String key, long timeout) {
		String fullKey = getKey(key);
		
		logger.info(
				LOG_PREFIX + "expire object, key=" + fullKey + ", timeout=" + timeout);
		
		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
	}
	
	public long getExpire(String key) {
		String fullKey = getKey(key);
		
		long returnValue = redisTemplate.getExpire(fullKey, TimeUnit.MILLISECONDS);
		
		logger.info(
				LOG_PREFIX + "getSet object, key=" + fullKey + ", returnValue=" + returnValue);
		
		return returnValue;
	}
	
	/**
	 * 加入队列
	 * @param key
	 * @param value
	 */
	public void lefltPushQueue(String key, String value) {
		String fullKey = getKey(key);
		logger.info(
				LOG_PREFIX + "bLefltPushQueue object, key=" + fullKey + ", value=" + value);
		ListOperations<String, String> op = redisTemplate.opsForList();
		op.leftPush(fullKey, value);
	}
	
	/**
	 * 从队列取数据
	 * @param key
	 * @param timeout 单位为毫秒
	 * @return
	 */
	public String rightPropQueue(String key, long timeout) {
		String fullKey = getKey(key);
		ListOperations<String, String> op = redisTemplate.opsForList();
		String returnValue = op.rightPop(fullKey, timeout, TimeUnit.MILLISECONDS);
		
		logger.info(
				LOG_PREFIX + "getSet object, key=" + fullKey + ", timeout=" + timeout + ", returnValue=" + returnValue);
		
		return returnValue;
	}
	
	/**
	 * 从队列取数据
	 * @param key
	 * @return
	 */
	public String rightPropQueue(String key) {
		String fullKey = getKey(key);
		ListOperations<String, String> op = redisTemplate.opsForList();
		String returnValue = op.rightPop(fullKey);
		
		logger.info(
				LOG_PREFIX + "getSet object, key=" + fullKey + ", returnValue=" + returnValue);
		
		return returnValue;
	}
}
