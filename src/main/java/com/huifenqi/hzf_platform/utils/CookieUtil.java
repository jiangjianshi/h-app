package com.huifenqi.hzf_platform.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by arison on 2015/11/16.
 */
public class CookieUtil {

	/**
	 * 保存cookie
	 * 
	 * @param name
	 * @param value
	 * @param expires
	 * @param domain
	 * @param path
	 * @param response
	 */
	public static void save(String name, String value, int expires, String domain, String path,
			HttpServletResponse response) {

		Cookie sidCookie = new Cookie(name, value);
		sidCookie.setMaxAge(expires);
		sidCookie.setDomain(domain);
		sidCookie.setPath(path);
		response.addCookie(sidCookie);
	}

	/**
	 * 获取指定key的值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String name) {
		Cookie c = getCookie(request, name);

		if (null != c) {
			return c.getValue();
		}

		return null;
	}

	/**
	 * 获取指定key的cookie
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (null == name || name.length() == 0) {
			return null;
		}

		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}

		return null;
	}

	/**
	 * 删除单个cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 * @param path
	 */
	public static void delete(HttpServletRequest request, HttpServletResponse response, String name, String domain,
                              String path) {

		Cookie cookie = getCookie(request, name);

		delete0(response, cookie, domain, path);
	}

	/**
	 * 清空所有cookie
	 * 
	 * @param request
	 * @param response
	 * @param domain
	 * @param path
	 */
	public static void clear(HttpServletRequest request, HttpServletResponse response, String domain, String path) {
		clear(request, response, domain, path, null);
	}

	/**
	 * 清空除白名单外其他所有cookie
	 * 
	 * @param request
	 * @param response
	 * @param domain
	 * @param path
	 * @param excludeCookieNames
	 */
	public static void clear(HttpServletRequest request, HttpServletResponse response, String domain, String path,
                             List<String> excludeCookieNames) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// 删除不在白名单内的cookie
				if (excludeCookieNames == null || !excludeCookieNames.contains(cookie.getName())) {
					delete0(response, cookie, domain, path);
				}
			}
		}
	}

	/**
	 * 删除单个cookie
	 * 
	 * @param response
	 * @param cookie
	 * @param domain
	 * @param path
	 */
	private static void delete0(HttpServletResponse response, Cookie cookie, String domain, String path) {
		if (cookie == null) {
			return;
		}

		cookie.setMaxAge(0);
		cookie.setDomain(domain);
		cookie.setPath(path);

		response.addCookie(cookie);
	}

}
