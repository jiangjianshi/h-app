package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.RentPayRequestHandler;

/**
 * Created by YDM on 2017/11/21.
 *
 * 在线缴租控制器
 */
@RestController
public class RentPayController {

	@Autowired
	private RentPayRequestHandler rentPayRequestHandler;

	/**
	 * 获取平台支持的银行卡列表
	 * @return
	 */
	@RequestMapping(value="/v1/pay/reqbankcardlist/", method = RequestMethod.POST)
	public Responses bankCardList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.bankCardSupportList(request);
	}
	
	/**
	 * 通过银行卡号获取银行名称（校验身份证号和银行卡号，并根据银行卡号返回银行卡类型）
	 * @return
	 */
	@RequestMapping(value="/v1/pay/reqbankcardinfo/", method = RequestMethod.POST)
	public Responses getBankCardNameByCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.getBankCardNameByCode(request);
	}
	
	/**
	 * 发起绑定银行卡（校验手机号，发送验证码；校验四要素）
	 * @return
	 */
	@RequestMapping(value="/v1/pay/bindbankcardreq/", method = RequestMethod.POST)
	public Responses bindBankCardReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.bindBankCardReq(request);
	}
	
	/**
	 * 绑定银行卡
	 * @return
	 */
	@RequestMapping(value="/v1/pay/bindbankcard/", method = RequestMethod.POST)
	public Responses bindBankCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.bindBankCard(request);
	}
	
	/**
	 * 获取绑定银行卡列表
	 * @return
	 */
	@RequestMapping(value="/v1/pay/listbankcard/", method = RequestMethod.POST)
	public Responses getBankCardList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.getBankCardList(request);
	}
	
	/**
	 * 查询本人所有合同的账单列表（每个合同下展示最近一条未支付的租房账单和所有未支付的其他账单）
	 * @return
	 */
	@RequestMapping(value="/v1/bill/reqpaylist/", method = RequestMethod.POST)
	public Responses reqPayList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.reqPayList(request);
	}
	
	@RequestMapping(value="/v2/bill/reqpaylist/", method = RequestMethod.POST)
    public Responses reqPayListV2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.reqPayListV2(request);
    }
	
	/**
	 * 查询合同下的账单列表
	 * @return
	 */
	@RequestMapping(value="/v1/bill/reqcontractpaylist/", method = RequestMethod.POST)
	public Responses reqContractPayList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.reqContractPayList(request);
	}
	
	/**
	 * 查询账单详情
	 * @return
	 */
	@RequestMapping(value="/v1/bill/reqsubpaydetail/", method = RequestMethod.POST)
	public Responses reqSubPayDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.reqSubPayDetail(request);
	}
	
	/**
     * 轻量版-账单组详情
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/reqrentbugetdetaillist/", method = RequestMethod.POST)
    public Responses reqRentBugetDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.reqRentBugetDetailList(request);
    }
    
    /**
     * 轻量版-子账单详情
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/reqbugetdetail/", method = RequestMethod.POST)
    public Responses reqBugetDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.reqBugetDetail(request);
    }

	/**
	 * 创建订单
	 * @return
	 */
	@RequestMapping(value="/v1/bill/createorder/", method = RequestMethod.POST)
	public Responses createOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.createOrder(request);
	}

	/**
	 * 撤销订单
	 * @return
	 */
	@RequestMapping(value="/v1/bill/cancelorder/", method = RequestMethod.POST)
	public Responses cancleOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.cancelOrder(request);
	}
	
	/**
	 * 查询支付渠道列表
	 * @return
	 */
	@RequestMapping(value="/v1/pay/reqpaymentchannellist/", method = RequestMethod.POST)
	public Responses reqPaymentchannellist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.reqPaymentchannellist(request);
	}
	
	/**
	 * 查询渠道费
	 * @return
	 */
	@RequestMapping(value="/v1/pay/reqchannelfee/", method = RequestMethod.POST)
	public Responses reqChannelFee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.reqChannelFee(request);
	}
	
	/**
	 * 发起支付
	 * @return
	 */
	@RequestMapping(value="/v1/pay/charge/", method = RequestMethod.POST)
	public Responses charge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.charge(request);
	}
	
	/**
     * 确认支付获取验证码
     * @return
     */
    @RequestMapping(value="/v1/pay/captcha/", method = RequestMethod.POST)
    public Responses getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.getCaptcha(request);
    }
	
	/**
	 * 确认支付
	 * @return
	 */
	@RequestMapping(value="/v1/pay/confirmcharge/", method = RequestMethod.POST)
	public Responses confirmcharge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return rentPayRequestHandler.confirmCharge(request);
	}
	
	/**
     * 查询支付状态
     * @return
     */
    @RequestMapping(value="/v1/pay/reqpaymentstatus/", method = RequestMethod.POST)
    public Responses reqPaymentStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.reqPaymentStatus(request);
    }

	/**
     * 轻量版-租客确认账单
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/comfirmbuget/", method = RequestMethod.POST)
    public Responses comfirmBuget(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.comfirmBuget(request);
    }
    
    /**
     * 轻量版-检验租客能否退租
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/checkcancelrent/", method = RequestMethod.POST)
    public Responses checkCancelRent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.checkCancelRent(request);
    }
    
    /**
     * 轻量版-租客退租
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/cancelrent/", method = RequestMethod.POST)
    public Responses cancelRent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.cancelRent(request);
    }
    
    /**
     * 轻量版-设置自动交租
     * @return
     */
    @RequestMapping(value="/v1/bugetlite/setautopayrent/", method = RequestMethod.POST)
    public Responses setAutoPayRent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rentPayRequestHandler.setAutoPayRent(request);
    }
	
}
