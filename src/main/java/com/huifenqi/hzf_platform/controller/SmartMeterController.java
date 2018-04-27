package com.huifenqi.hzf_platform.controller;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.SmartMeterRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Arison on 2018年1月18日.
 * 
 * 智能电表相关
 * 
 */
@RestController
public class SmartMeterController {

	@Autowired
	private SmartMeterRequestHandler smartMeterRequestHandler;
	/**
	 * @Title: getMeterList
	 * @Description: 获取电表列表
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/reqmeterlist", method = RequestMethod.POST)
	public Responses getMeterList(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqMeterList();
	}

	/**
	 * @Title: getPayList
	 * @Description: 获取购电金额
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/getpaylist", method = RequestMethod.POST)
	public Responses getPayList(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.getPayList();
	}
	/**
	 * @Title: getPowerdetails
	 * @Description: 电量明细
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/powerdetails", method = RequestMethod.POST)
	public Responses getPowerdetails(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqPowerDetails();
	}

	/**
	 * @Title: createTradeOrder
	 * @Description: 创建订单
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/createorder", method = RequestMethod.POST)
	public Responses createTradeOrder(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqCreateTradeOrder();
	}

	/**
	 * @Title: 获取支付渠道
	 * @Description: 获取支付渠道
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月22日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/meterpaychannels", method = RequestMethod.POST)
	public Responses getPayChannels(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.getPayChannels();
	}



	/**
	 * @Title: 查询渠道费
	 * @Description: 获取购电金额
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月22日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/reqchannelfee", method = RequestMethod.POST)
	public Responses reqChannelFee(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqChannelFee();
	}


	/**
	 * @Title: meterCharge
	 * @Description: 支付
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/charge", method = RequestMethod.POST)
	public Responses meterCharge(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.meterCharge();
	}

	/**
	 * @Title: meterCharge
	 * @Description: 支付
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/chargestatus", method = RequestMethod.POST)
	public Responses reqMeterChargeStatus(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqMeterChargeStatus();
	}

    /**
	 * @Title: meterChargeList
	 * @Description: 获取用户电表充值记录
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月22日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/reqchargelist", method = RequestMethod.POST)
	public Responses reqUserChargeRecordsList(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqUserTraderOrders();
	}


	/**
	 * @Title: meterChargeList
	 * @Description: 获取电表充值记录（暂末用到)
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月22日 下午2:54:03
	 */
	@RequestMapping(value = "/v1/api/meter/reqchargelist222", method = RequestMethod.POST)
	public Responses reqChargeRecordsList(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.reqChargeRecordsList();
	}




	/**
	 * @Title: meterChargeList
	 * @Description: 充值回调接口
	 * ***************************************  for update later by arison
	 * @return Responses
	 * @author Arison
	 * @dateTime 2018年1月17日 下午2:54:03
	 */
	/*@RequestMapping(value = "/v1/api/prepayasyncallback/", method = RequestMethod.POST)
	public Responses meterChargeList(HttpServletRequest request, HttpServletResponse response) {
		return smartMeterRequestHandler.prePayAsynCallBack();
	}*/


}
