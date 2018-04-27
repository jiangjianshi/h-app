package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.ConfigRequestHandler;

/**
 * Created by YDM on 2017/11/3.
 *
 * 配置控制器
 */
@RestController
public class ConfigController {

	@Autowired
	private ConfigRequestHandler configRequestHandler;

	/**
	 * 获取全局配置信息
	 * @return
	 */
	@RequestMapping(value="/config/globalconfig", method = RequestMethod.POST)
	public Responses getGlobalConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return configRequestHandler.getGlobalConfig(request);
	}
	
}
