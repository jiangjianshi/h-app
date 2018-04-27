package com.huifenqi.hzf_platform.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by arison on 2015/9/9.
 *
 * 扩展org.apache.commons.lang3.StringUtils工具集
 */
public class StringUtil extends StringUtils {

	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String TAB = "	";

	public static final String ACUTE = "`";
	public static final String TILDE = "~";
	public static final String EXCLAMATION = "!";
	public static final String AT = "@";
	public static final String NUMBER = "#";
	public static final String DOLLAR = "$";
	public static final String PERCENT = "%";
	public static final String CARET = "^";
	public static final String AND = "&";
	public static final String STAR = "*";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	// public static final String HYPHEN = "-";
	public static final String HYPHEN = " ";
	public static final String QFT = "全房通";
	public static final String QFT_HYPHEN = "-";
	public static final String UNDERSCORE = "_";
	public static final String EQUALS = "=";
	public static final String PLUS = "+";

	public static final String LEFT_SQUARE_BRACKET = "[";
	public static final String RIGHT_SQUARE_BRACKET = "]";
	public static final String LEFT_CURLY_BRACE = "{";
	public static final String RIGHT_CURLY_BRACE = "}";
	public static final String BACKSLASH = "\\";
	public static final String VERTICAL_BAR = "|";
	public static final String SEMI_COLON = ";";
	public static final String COLON = ":";
	public static final String SINGLE_QUOTATION = "'";
	public static final String DOUBLE_QUOTATION = "\"";
	public static final String COMMA = ",";
	public static final String DOT = ".";
	public static final String DOT2 = "·";
	public static final String DOT3 = "。";
	public static final String LEFT_ANGLE_BRACKET = "<";
	public static final String RIGHT_ANGLE_BRACKET = ">";
	public static final String SLASH = "/";
	public static final String QUESTION = "?";

	public static Double parseDouble(String doubleStr) {
		try {
			return Double.parseDouble(doubleStr);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Float parseFloat(String floatStr) {
		try {
			return Float.parseFloat(floatStr);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long parseLong(String longStr) {
		try {
			return Long.parseLong(longStr);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Integer parseInt(String intStr) {
		try {
			return Integer.parseInt(intStr);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 将string转为long类型，若string为null、空，或者无法解析为数字，则返回给定的默认值
	 *
	 * @param longStr
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(String longStr, long defaultValue) {
		try {
			return Long.parseLong(longStr);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 将string转为int类型，若string为null、空，或者无法解析为数字，则返回给定的默认值
	 *
	 * @param intStr
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(String intStr, int defaultValue) {
		try {
			return Integer.parseInt(intStr);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * redis值是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isRedisBlank(String cs) {
		if (isBlank(cs) || "null".equals(cs.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 根据指定的正则表达式，对字符串进行校验
	 * 
	 * @param cs
	 * @param regex
	 * @return
	 */
	public static boolean verifyString(String cs, String regex) throws NullPointerException {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cs);
		return matcher.matches();
	}

	/**
	 * 判断两个字符串的值是否相等，包括null
	 * 
	 * @return
	 */
	public static boolean isStringEquals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		} else {
			return str1.equals(str2);
		}
	}
	
	/**
	 * SQL查询 参数拼接（List转String）
	 * 
	 * @return
	 */
	public static String changeIntegerListToSqlStringNotEmpty(List<Integer> list) {
		if(CollectionUtils.isEmpty(list)){
			return "''";
		}
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < list.size(); i++) {
			if(sb.length()==0){
				sb.append("'"+list.get(i)+"'");
			}else{
				sb.append(",'"+list.get(i)+"'");
			}
		}
		if(StringUtils.isBlank(sb.toString())){
			return "''";
		}
		return sb.toString();
	 }

}
