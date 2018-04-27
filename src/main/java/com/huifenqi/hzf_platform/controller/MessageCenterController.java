package com.huifenqi.hzf_platform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.huifenqi.hzf_platform.context.dto.params.PushRecordDto;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.MessageCenterHandler;
import com.huifenqi.hzf_platform.utils.RequestUtils;

@RestController
@RequestMapping("/message")
public class MessageCenterController {

	@Resource
	MessageCenterHandler messageCenterHandler;

	@RequestMapping("/getPushMessage")
	public Responses getPushMessage(HttpServletRequest req) throws Exception {

		Integer pageSize = RequestUtils.getParameterInt(req, "pageSize");
		Integer pageNum = RequestUtils.getParameterInt(req, "pageNum");
		List<PushRecordDto> list = messageCenterHandler.getPushMessageByPage(pageSize, pageNum);
		
		ImmutableMap<String, List<PushRecordDto>> map = ImmutableMap.of("records", list);
		Responses responses = new Responses();
		responses.setBody(map);
		return responses;
		
	}

}
