package com.huifenqi.hzf_platform.utils;

public enum RefererUrl{
	H5_URL_M("http://m.huizhaofang.com", 1), H5_URL("http://h5.huizhaofang.com", 2), H5_URL_TEST("http://h5.hzfapi.com",
			3);
	// 成员变量
	private String name;
	private int index;
	// 构造方法
	private RefererUrl(String name, int index) {
		this.name = name;
		this.index = index;
	}
	// 普通方法
	public static String getName(int index) {
		for (RefererUrl c : RefererUrl.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}
	// get set 方法
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	// 普通方法
	public static boolean isMember(String url) {
		for (RefererUrl c : RefererUrl.values()) {
			if (url.startsWith(c.getName())) {
				return true;
			}
		}
		return false;
	}
}