/** 
 * Project Name: mq_project 
 * File Name: RedisCacheManager.java 
 * Package Name: com.huifenqi.mq.cache 
 * Date: 2015年11月30日下午7:36:38 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;

/**
 * ClassName: RedisCacheManager date: 2015年11月30日 下午7:36:38 Description:
 * redis操作管理类
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component("redisCacheManager")
public class RedisCacheManager {

    private final static Log log = LogFactory.getLog(RedisCacheManager.class);

    @Resource(name = "hzfRedisTemplate")
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     */
    public void putValue(String key, String value) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();

        valueOps.set(key, value);
        log.info(LogUtils.getCommLog(String.format("[REDIS] put k-v key=%s, value=%s", key, value)));
    }

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     * @throws Exception add by arison
     */
    public void incValue(String key) throws Exception {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.increment(key, 1);
        log.info(LogUtils.getCommLog(String.format("[REDIS] increment k key=%s", key)));
    }

    /**
     * 存储key-value
     * 
     * @param key
     * @param value
     * @param timeout 单位为毫秒
     * @throws Exception
     */
    public void putValue(String key, String value, long timeout) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.set(key, value, timeout, TimeUnit.MILLISECONDS);
        log.info(LogUtils
                .getCommLog(String.format("[REDIS] put k-v  key=%s, value=%s, timeout=%s", key, value, timeout)));
    }

    /**
     * 批量存储key-value
     * 
     * @param values
     * @throws Exception
     */
    public void putValue(Map<String, String> values) throws Exception {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.multiSet(values);
        log.info(LogUtils.getCommLog(String.format("[REDIS] put map Map=%s", values)));
    }

    /**
     * 获取value
     * 
     * @param key
     * @return 不存在时，返回null
     * @throws Exception
     */
    public String getValue(String key) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String result = valueOps.get(key);
        log.info(LogUtils.getCommLog(String.format("[REDIS] Get k-v key=%s, value=%s", key, result)));
        return RedisUtils.getInstance().isBlank(result) ? null : result;
    }

    /**
     * 批量获取值
     * 
     * @param keys
     * @return
     * @throws Exception
     */
    public Map<String, String> multiGetValue(List<String> keys) throws Exception {

        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        List<String> values = valueOps.multiGet(keys);

        HashMap<String, String> results = new HashMap<>();
        if (values != null && !keys.isEmpty()) {
            for (int i = 0; i < keys.size(); i++) {
                String fieldName = keys.get(i);
                String fieldValue = values.get(i);
                //如果对应的字段为null，则不需要放入
                if (!RedisUtils.getInstance().isBlank(fieldValue)) {
                    results.put(fieldName, fieldValue);
                }
            }
        }
        log.info(LogUtils.getCommLog(String.format("[REDIS] Get Map map=%s", results)));
        return results;
    }

    /**
     * 批量删除key
     * 
     * @param keys
     * @throws Exception
     */
    public void delete(Collection<String> keys) throws Exception {

        stringRedisTemplate.delete(keys);

        log.info(LogUtils.getCommLog(String.format("[REDIS] Delete keys=%s", keys)));
    }

    /**
     * 删除key
     * 
     * @param key
     * @throws Exception
     */
    public void delete(String key) throws Exception {
        stringRedisTemplate.delete(key);
        log.info(LogUtils.getCommLog(String.format("[REDIS] Delete key=%s", key)));
    }

    /**
     * 更新hash里面的属性字段
     * 
     * @param key 键
     * @param hashKey 属性名称
     * @param value 属性值
     * @throws Exception
     */
    public void putHash(String key, String hashKey, String value) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        hashOps.put(key, hashKey, value);
        log.info(
                LogUtils.getCommLog(String.format("[REDIS] Put hash key=%s,hashKey=%s,value=%s", key, hashKey, value)));
    }

    /**
     * 批量更新hash里面的属性字段
     * 
     * @param key
     * @param hashProperties 属性集合
     * @throws Exception
     */
    public void putHash(String key, Map<String, String> hashProperties) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        hashOps.putAll(key, hashProperties);
        log.info(LogUtils.getCommLog(String.format("[REDIS] Put hash key=%s,hashKey-value=%s", key, hashProperties)));
    }

    /**
     * 获取hash的字段值
     * 
     * @param key
     * @param hashKey
     * @throws Exception
     */
    public String getHash(String key, String hashKey) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        String result = hashOps.get(key, hashKey);
        log.info(LogUtils
                .getCommLog(String.format("[REDIS] Get hash key=%s,hashKey=%s,value=%s", key, hashKey, result)));
        return RedisUtils.getInstance().isBlank(result) ? null : result;
    }

    /**
     * 批量获取hash的字段值
     * 
     * @param key
     * @param hashKeys
     * @return filed的Map集合,filedName-filedValue
     * @throws Exception
     */
    public Map<String, String> multiGetHash(String key, List<String> hashKeys) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        List<String> values = hashOps.multiGet(key, hashKeys);
        HashMap<String, String> results = new HashMap<String, String>();
        if (values != null && !values.isEmpty()) {
            for (int i = 0; i < hashKeys.size(); i++) {
                String fieldName = hashKeys.get(i);
                String fieldValue = values.get(i);
                //如果对应的字段为null，则不需要放入
                if (!RedisUtils.getInstance().isBlank(fieldValue)) {
                    results.put(fieldName, fieldValue);
                }
            }
        }
        log.info(LogUtils.getCommLog(String.format("[REDIS] Get hash key=%s,hashKey-value=%s", key, results)));
        return results;
    }

    /**
     * 获取结果
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public Map<String, String> getHashValues(String key) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        Map<String, String> results = hashOps.entries(key);
        log.info(LogUtils.getCommLog(String.format("[REDIS] Get hash key=%s,hashKey-value=%s", key, results)));
        return results;
    }

    /**
     * 从hash表里面删除对应的值
     * 
     * @param key
     * @param hashKey
     * @throws Exception
     */
    public void deleteHash(String key, String hashKey) throws Exception {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        hashOps.delete(key, hashKey);
        log.info(LogUtils.getCommLog(String.format("[REDIS] Delete hash key=%s, hashKey=%s", key, hashKey)));
    }

    /**
     * 存储 key-value
     * 
     * @param key
     * @param value
     * @throws Exception
     */
    public void leftPushList(String key, String value) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.leftPush(key, value);
        log.info(LogUtils.getCommLog(String.format("[REDIS] leftPush list key=%s, value=%s", key, value)));
    }

    /**
     * 随机移除某个元素
     * 
     * @param key
     * @throws Exception
     */
    public void leftPopList(String key) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.leftPop(key);
        log.info(LogUtils.getCommLog(String.format("[REDIS] leftPop list key=%s", key)));
    }
    
    /**
     * 随机移除某个元素
     * 
     * @param key
     * @throws Exception
     */
    public String rightPop(String key)throws Exception{
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        log.info(LogUtils.getCommLog(String.format("[REDIS] leftPop list key=%s", key)));
        return listOps.rightPop(key,0,TimeUnit.SECONDS);
    }

    
    /**
     * 移除所有元素-按value
     * 
     * @param key
     * @param value
     * @throws Exception
     */
    //移除等于value的元素，当count>0时，从表头开始查找，移除count个；当count=0时，从表头开始查找，移除所有等于value的；当count<0时，从表尾开始查找，移除|count| 个。
    public void removeAllList(String key, String value) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.remove(key, 0, value);
        log.info(LogUtils.getCommLog(String.format("[REDIS] remove list key=%s", key)));
    }

    /**
     * 移除某个元素-按value
     * 
     * @param key
     * @param value
     * @throws Exception
     */
    //移除等于value的元素，当count>0时，从表头开始查找，移除count个；当count=0时，从表头开始查找，移除所有等于value的；当count<0时，从表尾开始查找，移除|count| 个。
    public void removeOneList(String key, String value) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.remove(key, 1, value);
        log.info(LogUtils.getCommLog(String.format("[REDIS] remove list key=%s", key)));
    }

    /**
     * 查询某个key数量
     * 
     * @param key
     * @throws Exception
     */
    public int rangeCountList(String key) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        log.info(LogUtils.getCommLog(String.format("[REDIS] range list key=%s", key)));
        return listOps.range(key, 0, 1).size();
    }

    /**
     * 查询某个key
     * 
     * @param key
     * @throws Exception
     */
    public String indexOneList(String key) throws Exception {
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        log.info(LogUtils.getCommLog(String.format("[REDIS] index list key=%s", key)));
        return listOps.index(key, 0);
    }
}
