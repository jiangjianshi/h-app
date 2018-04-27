package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.handler.CrawlHouseRequestHandler;


@RestController
public class CrawlHouseCronller {
	
	@Autowired
	private CrawlHouseRequestHandler crawlHouseRequestHandler;
	
	/**
	 * 爬虫数据初始化
	 * 
	 */
	@RequestMapping(value = "/crawl/initMongodbData", method = RequestMethod.POST)
	public BdResponses initMongodbData(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = crawlHouseRequestHandler.initMongodbData(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "爬虫数据初始化失败");
		}
		return bdResponses;
	}
	

}
