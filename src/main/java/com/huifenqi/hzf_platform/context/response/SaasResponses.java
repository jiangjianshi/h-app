/**
 * Project Name: hzf_platform_project
 * File Name: Responses.java
 * Package Name: com.huifenqi.hzf_platform.context
 * Date: 2016年4月25日上午11:52:24
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved.
 *
 */
package com.huifenqi.hzf_platform.context.response;

/**
 * ClassName: Responses date: 2016年4月25日 上午11:52:24 Description: 响应结果
 *
 * @author arison
 * @version
 * @since JDK 1.8
 */
public class SaasResponses {
    private int code;
    private String msg;
    public SaasResponses(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SaasResponses() {
    }


    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BdResponses{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
