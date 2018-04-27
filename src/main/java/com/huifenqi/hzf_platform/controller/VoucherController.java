package com.huifenqi.hzf_platform.controller;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.HouseSubDao;
import com.huifenqi.hzf_platform.handler.VoucherRequestHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by arison on 2015/9/9.
 *
 * 代金券控制器
 */
@RestController
public class VoucherController {

	@Autowired
	private VoucherRequestHandler voucherRequestHandler;

	@Autowired
	private HouseSubDao houseSubDao;

	/**
	 * 获取代金券列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/voucher/api/list", method = RequestMethod.POST)
	public Responses listVoucher(HttpServletRequest request, HttpServletResponse response) {

		return voucherRequestHandler.listVoucher();
	}

	/**
	 * 查看是否可以使用代金券
	 *
	 * @return
	 */
	@RequestMapping(value = "/voucher/api/canuse", method = RequestMethod.POST)
	public Object canUse(HttpServletRequest request, HttpServletResponse response) {
		return voucherRequestHandler.canUse();
	}

	/**
	 * 查询是否有新的代金券和消息
	 * @return
	 */
	@RequestMapping(value="/voucher/api/existnew", method = RequestMethod.POST)
	public Responses existNew(HttpServletRequest request, HttpServletResponse response) {
		return voucherRequestHandler.existNew();
	}

	/**
	 * 获取优惠券列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/coupon/api/list", method = RequestMethod.POST)
	public Responses listCoupon(HttpServletRequest request, HttpServletResponse response) {
		return voucherRequestHandler.listCoupon();
	}

	/**
	 * 查询代金券个数
	 * @return
	 */
	@RequestMapping(value="/voucher/api/queryNums", method = RequestMethod.POST)
	public Responses queryNums(HttpServletRequest request, HttpServletResponse response) {
		return voucherRequestHandler.queryNums();
	}
	
	/**
	 * 获取领取大礼包配置信息
	 * @return
	 */
	@RequestMapping(value="/voucher/api/giftpackinfo", method = RequestMethod.POST)
	public Responses getGiftPackInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return voucherRequestHandler.getGiftPackInfo();
	}
	
	/**
	 * 展示优惠券列表
	 * @return
	 */
	@RequestMapping(value="/voucher/api/showcoupontype", method = RequestMethod.POST)
	public Responses showCouponType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return voucherRequestHandler.showCouponType();
	}
	
	/**
	 * 检验用户是否已经领取过大礼包
	 * @return
	 */
	@RequestMapping(value="/voucher/api/checkusergiftpack", method = RequestMethod.POST)
	public Responses checkUserGiftPack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return voucherRequestHandler.checkUserGiftPack(request);
	}
	
	/**
	 * 领取大礼包
	 * @return
	 */
	@RequestMapping(value="/voucher/api/obtaingiftpack", method = RequestMethod.POST)
	public Responses obtainGiftPack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return voucherRequestHandler.obtainGiftPack(request);
	}
	
	/**
	 * 获取用户优惠券列表
	 * @return
	 */
	@RequestMapping(value="/voucher/api/couponlist", method = RequestMethod.POST)
	public Responses getValidCouponList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return voucherRequestHandler.getCouponList(request);
	}
	
}
