package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.handler.BdHouseRequestHandler;


@RestController
public class BdHouseCronller {
	
	@Autowired
	private BdHouseRequestHandler bdHouseRequestHandler;
	
	/**
	 * 模拟百度发布房源接口
	 */
	@RequestMapping(value = "/task/houseSubmit", method = RequestMethod.POST)
	public BdResponses bdFeedHouse(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = bdHouseRequestHandler.bdFeedHouse(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "发布房源失败");
		}
		return bdResponses;
	}
	
	/**
	 * 模拟百度房源上下架接口
	 */
	@RequestMapping(value = "/task/houseModify", method = RequestMethod.POST)
	public BdResponses houseModify(HttpServletRequest request) throws Exception {
		return bdHouseRequestHandler.houseModify(request);
	}
	
	/**
	 * 模拟百度修改房源接口
	 */
	@RequestMapping(value = "/task/roomSingleModify", method = RequestMethod.POST)
	public BdResponses roomSingleModify(HttpServletRequest request) throws Exception {
		return bdHouseRequestHandler.roomSingleModify(request);
	}

	/**
	 * 模拟百度发布房源接口-QFT
	 */
	@RequestMapping(value = "/task/houseSubmitQft", method = RequestMethod.POST)
	public BdResponses bdFeedHouseQft(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try {
			bdResponses = bdHouseRequestHandler.bdFeedHouseQft(request);
		} catch (Exception e) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "发布房源失败");
		}
		return bdResponses;
	}

	/**
	 * 模拟百度房源上下架接口-QFT
	 */
	@RequestMapping(value = "/task/houseModifyQft", method = RequestMethod.POST)
	public BdResponses houseModifyQft(HttpServletRequest request) throws Exception {
		return bdHouseRequestHandler.houseModifyQft(request);
	}

	/**
	 * 模拟百度修改房源接口-QFT
	 */
	@RequestMapping(value = "/task/roomSingleModifyQft", method = RequestMethod.POST)
	public BdResponses roomSingleModifyQft(HttpServletRequest request) throws Exception {
		return bdHouseRequestHandler.roomSingleModifyQft(request);
	}

}
