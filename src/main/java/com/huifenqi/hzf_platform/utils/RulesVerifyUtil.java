/** 
 * Project Name: usercomm_project 
 * File Name: RulesVerifyUtil.java 
 * Package Name: com.huifenqi.usercomm.utils 
 * Date: 2015年12月23日下午5:12:00 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: RulesVerifyUtil date: 2015年12月23日 下午5:12:00 Description:
 * 规则校验工具：参数长度、电话号码等
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class RulesVerifyUtil {

	/**
	 * 校验逗号分隔数字
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifySeparatedNumber(String cs) {
		String regex = "\\d*(\\,\\d*)*";
		boolean verifyString = StringUtil.verifyString(cs, regex);
		return verifyString;
	}

	/**
	 * 校验坐标
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyPosition(String cs) {
		String regex = "-?\\d+\\.\\d+\\,-?\\d+\\.\\d+";
		boolean verifyString = StringUtil.verifyString(cs, regex);
		return verifyString;
	}

	/**
	 * 校验坐标值
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyPositionValue(String cs) {
		String regex = "-?\\d+\\.\\d+";
		boolean verifyString = StringUtil.verifyString(cs, regex);
		return verifyString;
	}

	/**
	 * 校验数字区间
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyNumberRegion(String cs) {
		String regex = "^\\[(\\*|\\d+)\\,(\\*|\\d+)\\]$";
		boolean verifyString = StringUtil.verifyString(cs, regex);
		return verifyString;
	}

	/**
	 * 校验设置字段
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifySetting(String cs) {
		String regex = "\\w+:\\d+(\\,\\w+:\\d+)*";
		boolean verifyString = StringUtil.verifyString(cs, regex);
		return verifyString;
	}

	/**
	 * 校验名字 最大长度默认是50,只能包含汉字和(·)
	 * 
	 * @return
	 */
	public static boolean verifyName(String cs) {
		return verifyName(cs, 50);
	}
	
	/**
	 * 校验名字
	 * @param cs 名字
	 * @param verifyChinese 是否校验汉子或(·)
	 * @return
	 */
	public static boolean verifyName(String cs, boolean verifyChinese) {
		return verifyName(cs, 50, verifyChinese);
	}
	
	/**
	 * 校验名字
	 * 只能包含汉字和(·)
	 * @param cs
	 * @param length
	 * @return
	 */
	public static boolean verifyName(String cs, int length, boolean verifyChinese) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		if (cs.length() > length) {
			return false;
		}
		if (verifyChinese) {
			String regex = "^[\u4E00-\u9FA5]+(?:·|[\u4E00-\u9FA5]+)*([\u4E00-\u9FA5])$";
			return StringUtil.verifyString(cs, regex);
		}
		return true;
	}

	/**
	 * 校验名字 只能包含汉字和(·)
	 * 
	 * @param cs
	 * @param length
	 * @return
	 */
	public static boolean verifyName(String cs, int length) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		if (cs.length() > length) {
			return false;
		}
		String regex = "^[\u4E00-\u9FA5]+(?:·|[\u4E00-\u9FA5]+)*([\u4E00-\u9FA5])$";
		return StringUtil.verifyString(cs, regex);
	}

	/**
	 * 校验QQ号 长度为5-11
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyQQNo(String cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		String regex = "^[1-9]\\d{4,10}$";
		return StringUtil.verifyString(cs, regex);
	}

	/**
	 * 校验身份证号
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyIdNo(String cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		cs = cs.toLowerCase();
		String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
		return StringUtil.verifyString(cs, regex);
	}

	/**
	 * 校验电话码
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyPhone(String cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		String regex = "^1((3[0-9])|(4[0-9])|(5[0-9])|(6[6])|(7[0-9])|(8[0-9])|(9[8-9]))\\d{8}$";
		return StringUtil.verifyString(cs, regex);
	}
	
	/**
	 * 校验固话
	 * @param cs
	 * @return
	 */
	public static boolean verifyFixedPhone(String cs) {
//		String regex = "^[0][1-9]{2,3}[0-9]{5,10}$";
		String regex = "";
		if(cs.length() == 11){
			regex = "^0[0-9]{10}$";//普通固话
		}else{
			regex = "^400[0-9]{7}$"; //400电话
		}
		return StringUtil.verifyString(cs, regex);
	}
	
	/**
	 * 校验银行卡位数
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyBankCardLength(String cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		if (cs.length() >= 10 && cs.length() <= 21) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证字符串的最小和最大长度, 包含边界值
	 * 
	 * @param cs
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean verifyCsLength(String cs, int minLength, int maxLength) {
		if (cs == null) {
			return false;
		}
		if (cs.length() >= minLength && cs.length() <= maxLength)
			return true;

		return false;
	}

	/**
	 * 验证字符串的最小值, 包含边界值
	 * 
	 * @param cs
	 * @param minLength
	 * @return
	 */
	public static boolean verifyCsMinLength(String cs, int minLength) {
		if (cs == null) {
			return false;
		}
		if (cs.length() >= minLength)
			return true;

		return false;
	}

	/**
	 * 验证字符串的最大长度, 包含边界值
	 * 
	 * @param cs
	 * @param maxLength
	 * @return
	 */
	public static boolean verifyCsMaxLength(String cs, int maxLength) {
		if (cs == null) {
			return false;
		}
		if (length(cs) <= maxLength)
			return true;

		return false;
	}

	/**
	 * 获取字符串长度
	 * 
	 * @param cs
	 * @return int
	 */
	public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
	
	/**
	 * 验证数字的大小,包含边界值
	 * 
	 * @param cs
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean verifyNumberScale(String cs, long min, long max) {
		if (verifyNumber(cs) && Long.valueOf(cs) >= min && Long.valueOf(cs) <= max) {
			return true;
		}
		return false;
	}

	/**
	 * 验证数字的最小值，包含边界值
	 * 
	 * @param cs
	 * @param min
	 * @return
	 */
	public static boolean verifyNumberMin(String cs, long min) {
		if (verifyNumber(cs) && Long.valueOf(cs) >= min) {
			return true;
		}
		return false;
	}

	/**
	 * 验证数字的最大值，包含边界值
	 * 
	 * @param cs
	 * @param min
	 * @return
	 */
	public static boolean verifyNumberMax(String cs, long max) {
		if (verifyNumber(cs) && Long.valueOf(cs) <= max) {
			return true;
		}
		return false;
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyNumber(String cs) {
		String regex = "(0)|([1-9]\\d*)";
		return StringUtil.verifyString(cs, regex);
	}

	public static boolean verifyEmail(String cs) {
		// String regex =
		// "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		String regex = "^\\w(?:(.\\w+)|(-\\w+))*\\w+@\\w+(?:(.\\w+)|(-\\w+))*(\\.\\w{2,3})$";
		return StringUtil.verifyString(cs, regex);
	}
	
	
	/**
	 * 校验数字长度为6-10
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean verifyNoSixToTen(String cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		String regex = "^[1-9]\\d{5,9}$";
		return StringUtil.verifyString(cs, regex);
	}
	
}
