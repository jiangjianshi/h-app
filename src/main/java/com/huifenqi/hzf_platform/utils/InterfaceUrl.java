package com.huifenqi.hzf_platform.utils;

public enum InterfaceUrl{
    HOUSESUBMIT("/task/houseSubmit", 1),
	HOUSEMODIFY("/task/houseModify", 2),
	ROOMSINGLEMODIFY("/task/roomSingleModify", 3),
	HOUSESUBMITWITHSPLASH("/task/houseSubmit/", 4),
	HOUSEMODIFYWITHSPLASH("/task/houseModify/", 5),
	ROOMSINGLEMODIFYWITHSPLASH("/task/roomSingleModify/", 6),
	HOUSESUBMITQFT("/task/houseSubmitQft", 7),
	HOUSEMODIFYQFT("/task/houseModifyQft", 9),
	ROOMSINGLEMODIFYQFT("/task/roomSingleModifyQft", 9),
	HOUSESUBMITWITHSPLASHQFT("/task/houseSubmitQft/", 10),
	HOUSEMODIFYWITHSPLASHQFT("/task/houseModifyQft/", 11),
	ROOMSINGLEMODIFYWITHSPLASHQFT("/task/roomSingleModifyQft/", 12);
	// 成员变量
	private String name;
	private int index;
	// 构造方法
	private InterfaceUrl(String name, int index) {
		this.name = name;
		this.index = index;
	}
	// 普通方法
	public static String getName(int index) {
		for (InterfaceUrl c : InterfaceUrl.values()) {
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
		for (InterfaceUrl c : InterfaceUrl.values()) {
			if (url.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}
}