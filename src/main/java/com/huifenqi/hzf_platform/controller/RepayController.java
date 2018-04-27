package com.huifenqi.hzf_platform.controller;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.RepayRequestHandler;
import com.huifenqi.hzf_platform.utils.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by arison on 2017/9/11.
 * 
 * 支付控制器
 * 
 */
@RestController
public class RepayController {

	@Autowired
	private RepayRequestHandler repayRequestHandler;

	@Autowired
	private Configuration configuration;

	/**
	 * 查询用户所有的合同，以及该合同当前应还期次信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/list", method = RequestMethod.POST)
	public Responses listPay(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.listPay();
	}

	/**
	 * 列出合同的所有分期订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/listsubpay", method = RequestMethod.POST)
	public Responses listSubpay(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.listContractsSubPay();
	}

	/**
	 * 查询订单状态列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/listpaystatus", method = RequestMethod.POST)
	public Responses listPayStatus(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.listPayStatus();
	}

	/**
	 * 支付
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/charge", method = RequestMethod.POST)
	public Responses charge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return repayRequestHandler.charge();
	}

	/**
	 * 支付成功
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/success", method = RequestMethod.POST)
	public Responses chargeSuccess(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.chargeSuccess();
	}

	/**
	 * 取消支付
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/cancel", method = RequestMethod.POST)
	public Object cancelCharge(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.cancelCharge();
	}

	/**
	 * 获取绑定银行卡列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/listbankcard", method = RequestMethod.POST)
	public Object listBankcard(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.listBankcard();
	}

	/**
	 * 绑定银行卡
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/bindbankcard", method = RequestMethod.POST)
	public Object bindBankcard(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.bindBankcard();
	}

	/**
	 * 确认绑定银行卡
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/confirmbindbankcard", method = RequestMethod.POST)
	public Object confirmBindBankcard(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.confirmBindBankcard();
	}

	/**
	 * 解除绑定银行卡
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/unbindbankcard", method = RequestMethod.POST)
	public Object unbindBankcard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return repayRequestHandler.unbindBankcard();
	}

	/**
	 * 确认支付，虽然url中还包含"yeepay"的字段，但扩展后这个接口是支持所有API形式的支付渠道的
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/confirmyeepaycharge", method = RequestMethod.POST)
	public Responses confirmChargeV2(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.confirmChargeV2();
	}

	/**
	 * 查询支付宝开关
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqpayswitch", method = RequestMethod.POST)
	public Responses reqPaySwitch(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqPaySwitch();
	}

	/**
	 * 根据合同号查询合同信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqcontract", method = RequestMethod.POST)
	public Responses reqContract(HttpServletRequest request, HttpServletResponse response) {
		return  repayRequestHandler.reqContract(response);

	}

	/**
	 * 根据合同号确认合同状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/confirmcontractstatus", method = RequestMethod.POST)
	public Responses confirmContractStatus(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.confirmContractStatus();
	}

	/**
	 * 提交绑卡信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/subbindcard", method = RequestMethod.POST)
	public Object subBindCard(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.subBindCardUnpay();
	}

	/**
	 * 查询银行卡信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqsubcardresult", method = RequestMethod.POST)
	public Object reqSubcardResult(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqSubcardResult();
	}

	/**
	 * 查询银行卡信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqbankcardinfo", method = RequestMethod.POST)
	public Object reqBankCardInfo(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqBankCardInfo();
	}

	/**
	 * 查询绑定的银行卡信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqbindbankcard", method = RequestMethod.POST)
	public Object reqBindBankCard(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqBindBankCard();
	}

	/**
	 * @deprecated TODO 检查这个接口是否还有在用，没有用则删除掉
	 * 根据手机号查询合同
	 */
//	@RequestMapping(value = "/pay/api/reqcontractbyphone/", method = RequestMethod.POST)
//	public Object reqContractByPhone() {
//		return repayRequestHandler.handle("/pay/api/reqcontractbyphone/", "reqContractByPhone");
//	}

	/**
	 * 查询电子合同地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqelectcontract", method = RequestMethod.POST)
	public Responses reqElectContract(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqElectContract();
	}

	/**
	 * 查询电子合同地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqelectcontracturl", method = RequestMethod.POST)
	public Object reqElectContractUrl(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqElectContractUrl();
	}

	/**
	 * 查询代扣提示
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqwithholdtip", method = RequestMethod.POST)
	public Object reqWithholdTip(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqWithholdTip();
	}

	/**
	 * 查询是否支付过订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/ifpaid", method = RequestMethod.POST)
	public Object checkIfPaid(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.checkIfPaid();
	}

	
	/**
	 * 根据不同渠道，计算需要支付的金额 
	 * @return
	 */
	@RequestMapping(value = "/pay/api/channelincome/calc", method = RequestMethod.POST)
	public Responses calcPrice(HttpServletRequest request, HttpServletResponse response){
		return repayRequestHandler.calcPrice();
	}
	
	/**
	 * 查询所有支付渠道
	 * @return
	 */
	@RequestMapping(value = "/pay/api/getpaychannels", method = RequestMethod.POST)
	public Responses getPayChannels(HttpServletRequest request, HttpServletResponse response){
		return repayRequestHandler.getPayChannels();
	}

	/**
	 * 查询电子合同列表
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqElectronicContracts", method = RequestMethod.POST)
	public Responses reqElectronicContracts(HttpServletRequest request, HttpServletResponse response){
		return repayRequestHandler.reqElectronicContracts();
	}
	
	/**
	 * 查询支付状态
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqChargeStatus", method = RequestMethod.POST)
	public Responses reqChargeStatus(HttpServletRequest request, HttpServletResponse response){
		return repayRequestHandler.reqChargeStatus();
	}

	/**
	 * 查询银行列表
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqBankList", method = RequestMethod.POST)
	public Object reqBankList(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqBankList();
	}

	/**
	 * 获取最合适的支付渠道
	 * @return
	 */
	@RequestMapping(value = "/pay/api/reqBankCardPayChannel", method = RequestMethod.POST)
	public Object reqBankCardPayChannel(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.reqBankCardPayChannel();
	}

	/**
	 * 查询是否已经发起转租
	 * @return Object
	 */
	@RequestMapping(value = "/pay/api/ifsubletlaunched", method = RequestMethod.POST)
	public Object ifsubletlaunched(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.ifsubletlaunched();
	}

	/**
	 * 发起转租
	 * @return Object
	 */
	@RequestMapping(value = "/pay/api/launchsublet", method = RequestMethod.POST)
	public Object launchsublet(HttpServletRequest request, HttpServletResponse response) {
		return repayRequestHandler.launchsublet();
	}
}
