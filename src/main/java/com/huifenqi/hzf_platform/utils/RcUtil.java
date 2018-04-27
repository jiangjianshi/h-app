/** 
* Project Name: trunk 
* File Name: RcUtil.java 
* Package Name: com.huifenqi.usercomm.utils 
* Date: 2016年4月12日下午3:56:14 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.utils;

import com.google.gson.Gson;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.usercomm.domain.contract.RcInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: RcUtil date: 2016年4月12日 下午3:56:14 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Component
public class RcUtil {

	private static Logger logger = LoggerFactory.getLogger(RcUtil.class);

	private Gson gson = GsonUtil.buildGson();

	@Autowired
	private RedisClient redisClient;

	// 上报风控失败信息在redis中key的前缀
	private final static String RC_REPORT_FAIL_PREFIX = "rc.report.failed";

	/**
	 * 保存上报失败的风控信息
	 * 
	 * @param phone
	 * @param userInfo
	 */
	public void saveFailedRcInfo(String phone, RcInfo rcInfo) {
		if (rcInfo == null) {
			logger.error("rcInfo is null");
			return;
		}
		redisClient.setInMap(RC_REPORT_FAIL_PREFIX, phone, gson.toJson(rcInfo));
	}

	/**
	 * 删除上报失败的风控信息
	 * 
	 * @param phone
	 */
	public void delFailedRcInfo(String phone) {
		redisClient.deleteFromMap(RC_REPORT_FAIL_PREFIX, phone);
	}

	/**
	 * 获取上报失败的风控信息集合
	 * 
	 * @return
	 */
	public Map<String, RcInfo> getFailedRcInfoMap() {
		Map<Object, Object> map = redisClient.getMap(RC_REPORT_FAIL_PREFIX);
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}

		Map<String, RcInfo> rcInfoMap = new HashMap<String, RcInfo>();

		Set<Object> keySet = map.keySet();
		for (Object key : keySet) {
			Object value = map.get(key);
			if (key instanceof String && value instanceof String) {
				String phone = (String) key;
				String json = (String) value;
				RcInfo rcInfo = GsonUtil.jsonToBean(json, RcInfo.class);
				if (rcInfo != null) {
					rcInfoMap.put(phone, rcInfo);
				}
			}
		}

		return rcInfoMap;
	}

}
