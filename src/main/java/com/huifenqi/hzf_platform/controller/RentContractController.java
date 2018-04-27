package com.huifenqi.hzf_platform.controller;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.RentContractRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Arison on 2017/11/21.
 *
 * 在线缴租合同控制器
 */
@RestController
public class RentContractController {

	@Autowired
	private RentContractRequestHandler rentContractRequestHandler;

	/**
	 * 查询租房合同
	 * @return
	 */
	@RequestMapping(value="/v1/contract/reqrentcontractlist/", method = RequestMethod.POST)
	public Responses getRentContractList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentContractRequestHandler.getRentContractList(request);
	}


	/**
	 * 查询租房合同详情
	 * @return
	 */
	@RequestMapping(value="/v1/contract/reqrentcontractdetail/", method = RequestMethod.POST)
	public Responses getRentContractDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentContractRequestHandler.getRentContractDetail(request);
	}

	/**
	 * 合同二次确认
	 * @return
	 */
	@RequestMapping(value="/v1/contract/confirmcontract/", method = RequestMethod.POST)
	public Responses rentContractReConfirm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentContractRequestHandler.rentReContractConfirm(request);
	}

	/**
	 * 设置自动代扣
	 * @return
	 */
	@RequestMapping(value="/v1/pay/autowithhold/", method = RequestMethod.POST)
	public Responses rentWithhold(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentContractRequestHandler.rentWithhold(request);
	}

	/**
	 * 更新是否开启自动代扣
	 * @return
	 */
	@RequestMapping(value="/v1/pay/updatewithholdstatus/", method = RequestMethod.POST)
	public Responses updateWithholdStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentContractRequestHandler.updateWithholdStatus(request);
	}
}
