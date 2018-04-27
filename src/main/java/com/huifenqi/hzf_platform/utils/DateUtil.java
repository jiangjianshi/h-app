package com.huifenqi.hzf_platform.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by arison on 2015/9/5.
 */
public class DateUtil extends DateUtils {

	private static final String PATTERN_DATETIME_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	private static final String PATTERN_DATE = "yyyy-MM-dd";
	private static final String PATTERN_MONTH = "yyyy-MM";
	private static final String PATTERN_YEAR = "yyyy";

	public static String formatCurrentDateTime() {
		return formatDateTime(new Date());
	}

	public static String formatCurrentDate() {
		return formatDate(new Date());
	}

	public static String formatDateTime(Date date) {
		return format(PATTERN_DATETIME, date);
	}

	public static String formatDateMonth(Date date) {
		return format(PATTERN_MONTH, date);
	}

	public static String formatDate(Date date) {
		return format(PATTERN_DATE, date);
	}

	public static String formatYear(Date date) {
		return format(PATTERN_YEAR, date);
	}

	public static String formatDateTimeTZ(Date date) {
		return format(PATTERN_DATETIME_TZ, date);
	}

	public static Date parseDateTime(String str) {
		return parse(PATTERN_DATETIME, str);
	}

	public static Date parseDate(String str) {
		return parse(PATTERN_DATE, str);
	}

	public static Date parseYear(String str) {
		return parse(PATTERN_YEAR, str);
	}

	public static Date parseDateTimeTZ(String str) {
		return parse(PATTERN_DATETIME_TZ, str);
	}

	public static String format(String pattern, Date date) {
		if (null == date) {
			return null;
		}

		SimpleDateFormat FORMATTER = new SimpleDateFormat();
		FORMATTER.applyPattern(pattern);
		return FORMATTER.format(date);
	}

	public static Date parse(String pattern, String str) {
		if (StringUtil.isBlank(str)) {
			return null;
		}

		SimpleDateFormat FORMATTER = new SimpleDateFormat();

		FORMATTER.applyPattern(pattern);

		try {
			return FORMATTER.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取当月起止日期时间
	 *
	 * 以2015年10月份为例，返回的是2015-10-01 00:00:00 和 2015-11-01 00:00:00
	 *
	 * @param now
	 * @return
	 */
	public static Pair<Date, Date> getMonthRange(Date now) {
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(now);

		tmp.set(Calendar.DAY_OF_MONTH, 1);
		tmp.set(Calendar.HOUR_OF_DAY, 0);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND, 0);
		Date startDate = tmp.getTime();

		tmp.add(Calendar.MONTH, 1);
		Date endDate = tmp.getTime();

		return Pair.of(startDate, endDate);
	}

	/**
	 * 获取目标时间与当前时间间隔的小时数
	 * 
	 * @param targetDate
	 * @return
	 */
	public static int getDiffHourFromCurrent(Date targetDate) {
		long diff = System.currentTimeMillis() - targetDate.getTime();
		return (int) (diff / 1000 / 60 / 60);
	}

	/**
	 * 获取目标时间与当前时间间隔的小时数
	 *
	 * @param targetDate
	 * @return
	 */
	public static int getDiffMinuteFromCurrent(Date targetDate) {
		long diff = System.currentTimeMillis() - targetDate.getTime();
		return (int) (diff / 1000 / 60);
	}

	/**
	 * 获取某个日期与当前时间距离的天数
	 * @param targetDate
	 * @return
	 */
	public static int getDiffDays(String targetDate) {
		Date tarDate = DateUtil.parse(PATTERN_DATE, targetDate);  
        Date nowDate = DateUtil.parse(PATTERN_DATE, DateUtil.format(PATTERN_DATE, new Date()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(tarDate);
        long time1 = cal.getTimeInMillis();        
        cal.setTime(nowDate);
        long time2 = cal.getTimeInMillis();  
        long betweenDays = (time1-time2)/(1000*3600*24);
        
	    return Integer.parseInt(String.valueOf(betweenDays)); 
	}
	
	/**
	 * 获取某个日期与当前时间距离多少年
	 * @param targetDate
	 * @return
	 */
	public static int getDiffYear(String targetDate) {
		String targetYear = DateUtil.formatYear(DateUtil.parseDate(targetDate));
		String nowYear = DateUtil.formatYear(new Date());
		return Integer.parseInt(nowYear) - Integer.parseInt(targetYear);
	}
	
	/**
	 * 在线缴租  格式化时间 "-"改为"."
	 * @param targetDate
	 * @return
	 */
	public static String formatPayDate(JsonElement targetDate) {
		String retDate = targetDate.isJsonNull()?"":targetDate.getAsString().replace("-", ".");
		return retDate;
	}
	
	/**
	 * 推送消息列表 格式化时间 "-"改为"."
	 * @param targetDate
	 * @return
	 */
	public static String replaceSpliter(String targetDate) {
		String retDate = StringUtils.isEmpty(targetDate) ? "" : targetDate.replace("-", ".");
		return retDate;
	}
}
