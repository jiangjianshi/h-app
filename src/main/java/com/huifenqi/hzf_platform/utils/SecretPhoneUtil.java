package com.huifenqi.hzf_platform.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author jjs
 *
 */
public class SecretPhoneUtil {

	
	/**
	 * 检查是否是手机号码
	 * @param agencyPhone
	 * @param flag
	 * @return
	 */
	public static boolean checkTelPhone(String agencyPhone, boolean flag) {
		boolean result = true;
		if (StringUtils.isEmpty(agencyPhone)) {
			result = false;
		}
		if (agencyPhone.startsWith("400")) {
			result = false;
		}
		if (agencyPhone.startsWith("0") && flag) {
			result = false;
		}
		return result;
	}
}
