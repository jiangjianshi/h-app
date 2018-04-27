package com.huifenqi.hzf_platform.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YDM on 2017/11/21.
 * Update by Arison on 2017/11/27.
 */
@Component
public class RentPayService {

	@Autowired
	private Configuration configuration;
	
	@Autowired
    private PaymentNewService paymentNewService;
	   
	/**
	 * @Title: bankSupportList
	 * @Description: 获取平台支持的银行卡列表
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午4:44:09
	 */
	public JsonObject bankSupportList(long userId, int platType) throws Exception {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqbanklist/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("payType", String.valueOf(platType));
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: getBankNameByCode
	 * @Description: 通过银行卡号获取银行名称
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午4:48:18
	 */
	public JsonObject getBankNameByCode(long userId, String bankCode) throws Exception {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqbankcardinfo/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("bankCardNo", bankCode);
		return requestRemote(userServiceUrl, params);
	}

	/**
	 * @Title: getRentContractList
	 * @Description: 查询出租合同列表
	 * @return ApiResult
	 * @author Arison
	 * @dateTime 2017年11月22日 下午4:48:18
	 */
	public JsonObject getRentContractList(String userId, String phone, String name, String idNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqrentcontractlist/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		params.put("userId", userId);
		params.put("name", name);
		params.put("idNo", idNo);
		return requestRemote(userServiceUrl,params);
	}

	/**
	 * @Title: getRentContractDetail
	 * @Description: 查询出租合同详情
	 * @return ApiResult
	 * @author Arison
	 * @dateTime 2017年11月22日 下午4:48:18
	 */
	public JsonObject getRentContractDetail(String userId,String rentContractCode) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqrentcontractdetail/";
		Map<String, String> params = new HashMap<>();
		params.put("rentContractCode", rentContractCode);
		params.put("userId", userId);
		return requestRemote(userServiceUrl,params);
	}


	/**
	 * @Title: getRentContractList
	 * @Description: 绑定租客
	 * @return ApiResult
	 * @author Arison
	 * @dateTime 2017年11月22日 下午4:48:18
	 */
	public JsonObject rentWithhold(String userId, String rentContractCode, int isUseWithhold, String bankToken) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/bindrentcontractuser/";
		Map<String, String> params = new HashMap<>();
		params.put("rentContractCode", rentContractCode);
		params.put("userId", userId);
		params.put("isUseWithhold", String.valueOf(isUseWithhold));
		params.put("bankToken", bankToken);
		if(isUseWithhold==1) {
			if(StringUtils.isNotBlank(bankToken)) {
				params.put("bankToken", bankToken);
			}else{
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"bankToken 不能为空");
			}
		}
		return requestRemote(userServiceUrl,params);
	}

	/**
	 * @Title: updateWithholdStatus
	 * @Description: 更新是否开启自动代扣
	 * @return ApiResult
	 * @author Arison
	 * @dateTime 2017年11月22日 下午4:48:18
	 */
	public JsonObject updateWithholdStatus(String userId,String rentContractCode,int isUseWithhold ,String bankToken) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/withholdswitch/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		params.put("rentContractCode", rentContractCode);
		params.put("isUseWithhold", isUseWithhold+"");
		if(isUseWithhold==1) {
			if(StringUtils.isNoneBlank(bankToken)) {
				params.put("bankToken", bankToken);
			}else{
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"bankToken 不能为空");
			}
		}
		return requestRemote(userServiceUrl,params);
	}

	/**
	 * @Title: getRentContractList
	 * @Description: 确认修改出租合同
	 * @return ApiResult
	 * @author Arison
	 * @dateTime 2017年11月22日 下午4:48:18
	 */
	public JsonObject rentReContractConfirm(String userId,String rentContractCode) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/confirmupdaterentcontract/";
		Map<String, String> params = new HashMap<>();
		params.put("rentContractCode", rentContractCode);
		params.put("userId", userId);
		return requestRemote(userServiceUrl,params);
	}
	
	/**
	 * @Title: reqPayList
	 * @Description: 查询账单列表
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月22日 下午3:52:18
	 */
	public JsonObject reqPayList(long userId, int financeType, int payStatus, int paymentType, String phone, String name, String idNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqsubpaylist/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("financeType", String.valueOf(financeType));
		params.put("payStatus", String.valueOf(payStatus));
		params.put("cashierType", String.valueOf(paymentType));
		params.put("phone", phone);
		params.put("name", name);
		params.put("idNo", idNo);
		return requestRemote(userServiceUrl, params);
	}

	/**
     * @Title: reqRentBugetList
     * @Description: 获取轻量版账单组列表
     * @return JsonObject
     * @author 叶东明
     * @dateTime 2017年12月22日 下午2:54:03
     */
    public JsonObject reqRentBugetList(long userId, String phone) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/reqrentbugetlist/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("signerPhone", phone);
        return requestRemote(userServiceUrl, params);
    }
    
    /**
     * @Title: reqContractPayList
     * @Description: 查询合同下的账单列表
     * @return JsonObject
     * @author 叶东明
     * @dateTime 2017年11月22日 下午4:17:34
     */
    public JsonObject reqContractPayList(long userId, String rentContractCode, int financeType, int payStatus, int paymentType, String phone) {
        String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqcontractsubpaylist/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("rentContractCode", rentContractCode);
        params.put("financeType", String.valueOf(financeType));
        params.put("payStatus", String.valueOf(payStatus));
        params.put("paymentType", String.valueOf(paymentType));
        params.put("phone", phone);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: reqSubPayDetail
	 * @Description: 查询账单详情
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月23日 下午2:21:21
	 */
	public JsonObject reqSubPayDetail(long userId, String bugetNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqsubpaydetail/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("bugetNo", bugetNo);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: reqRentBugetDetailList
	 * @Description: 查询轻量版-账单组详情
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月23日 上午11:22:46
	 */
	public JsonObject reqRentBugetDetailList(long userId, String bugetBaseNo, String userName, String idNo) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/reqrentbugetdetaillist/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("bugetBaseNo", bugetBaseNo);
        params.put("signerName", userName);
        params.put("identityId", idNo);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: reqBugetDetail
	 * @Description: 查询轻量版-子账单详情
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月23日 上午11:27:17
	 */
	public JsonObject reqBugetDetail(String bugetNo) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/reqbugetdetail/";
        Map<String, String> params = new HashMap<>();
        params.put("bugetNo", bugetNo);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: createOrder
	 * @Description: 标准版-创建订单
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2017年11月23日 下午2:21:21
	 */
	public JsonObject createOrder(long userId, String bugetNo, String price, String operationTerminal) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/createorder/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("bugetNo", bugetNo);
		params.put("price", price);
		params.put("operationTerminal", operationTerminal);
		String postRet = "";
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		String code = result.status.code;
		// 解锁B端账单锁定
		if ("2620231".equals(code)) {
			String unlockServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/unlockfinancebuget/";
			Map<String, String> unlockParams = new HashMap<>();
			unlockParams.put("userId", String.valueOf(userId));
			unlockParams.put("bugetNo", bugetNo);
			try {
				HttpUtil.post(unlockServiceUrl, unlockParams);
			} catch (Exception e) {
				throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "服务暂时不可用");
			}
			throw new ServiceInvokeFailException(Integer.parseInt(code), result.status.description);
		} else if (!"0".equals(code)) {
			throw new ServiceInvokeFailException(Integer.parseInt(code), result.status.description);
		}
		return result.result;
	}
	
	/**
	 * @Title: createLiteOrder
	 * @Description: 轻量版-创建订单
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月3日 下午7:02:09
	 */
	public JsonObject createLiteOrder(long userId, String bugetNo, String price, String operationTerminal) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/createorder/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("bugetNo", bugetNo);
        params.put("price", price);
        params.put("operationTerminal", operationTerminal);
        return requestRemote(userServiceUrl, params);
    }

	/**
	 * @Title: cancleOrder
	 * @Description: 标准版-撤销订单
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2017年11月23日 下午2:21:21
	 */
	public JsonObject cancelOrder(long userId, String orderNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/cancelorder/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: cancelLiteOrder
	 * @Description: 轻量版-撤销订单
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午10:29:56
	 */
	public JsonObject cancelLiteOrder(long userId, String orderNo) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/cancelorder/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("orderNo", orderNo);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: reqPaymentchannellist
	 * @Description: 标准版-查询支付渠道列表
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2017年11月24日 下午3:30:55
	 */
	public JsonObject reqPaymentchannellist(long userId, String orderNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqpaymentchannellist/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: reqLitePaymentchannellist
	 * @Description: 轻量版-查询支付渠道列表
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月3日 下午6:13:31
	 */
	public JsonObject reqLitePaymentchannellist() {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/reqpaymentchannellist/";
        Map<String, String> params = new HashMap<>();
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: reqChannelFee
	 * @Description: 标准版-查询渠道费
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月24日 下午3:34:59
	 */
	public JsonObject reqChannelFee(long userId, String orderNo, String paymentChannel) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqchannelfee/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		params.put("paymentChannel", paymentChannel);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: reqLiteChannelFee
	 * @Description: 轻量版-查询渠道费
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午10:34:54
	 */
	public JsonObject reqLiteChannelFee(long userId, String orderNo, String paymentChannel) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/reqchannelfee/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("orderNo", orderNo);
        params.put("paymentChannel", paymentChannel);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: reqPaymentStatus
	 * @Description: 标准版-查询支付状态
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月25日 上午11:44:19
	 */
	public JsonObject reqPaymentStatus(long userId, String orderNo) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqpaymentstatus/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: reqLitePaymentStatus
	 * @Description: 轻量版-查询支付状态
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午11:06:50
	 */
	public JsonObject reqLitePaymentStatus(long userId, String orderNo) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/reqpaymentstatus/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("orderNo", orderNo);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: charge
	 * @Description: 标准版-发起支付
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月25日 上午11:48:03
	 */
	public JsonObject charge(long userId, String orderNo, String paymentChannel, String bankToken, int totalPrice, 
			String clientIp, String userAgent, String phone, String openId) {
	    // 发起支付之前检验该订单状态如果是支付完成，则不允许再次发起支付
	    String paymentStatusUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqpaymentstatus/";
	    Map<String, String> paymentStatusParams = new HashMap<>();
	    paymentStatusParams.put("userId", String.valueOf(userId));
	    paymentStatusParams.put("orderNo", orderNo);
        String postRet = "";
        try {
            postRet = HttpUtil.post(paymentStatusUrl, paymentStatusParams);
        } catch (Exception e) {
            throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "服务暂时不可用");
        }
        ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
        int paymentStatus = result.result.get("status").getAsInt();
        if (paymentStatus == 3) {
            throw new ServiceInvokeFailException(ErrorMsgCode.REPEAT_PAY_ERROR, "不允许重复支付");
        } else {
            String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/charge/";
            Map<String, String> params = new HashMap<>();
            params.put("userId", String.valueOf(userId));
            params.put("orderNo", orderNo);
            params.put("paymentChannel", paymentChannel);
            params.put("bankToken", bankToken);
            params.put("totalPrice", String.valueOf(totalPrice));
            params.put("clientIp", clientIp);
            params.put("userAgent", "Huifenqi");
            params.put("referer", "huizhaofang.com");
            params.put("phone", phone);
            params.put("openId", openId);
            return requestRemote(userServiceUrl, params);
        }
	}
	
	/**
	 * @Title: chargeLite
	 * @Description: 轻量版-发起支付
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午10:53:45
	 */
	public JsonObject chargeLite(long userId, String orderNo, String paymentChannel, String bankToken, int totalPrice, 
             String clientIp, String userAgent, String phone, String openId) {
         String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/charge/";
         Map<String, String> params = new HashMap<>();
         params.put("userId", String.valueOf(userId));
         params.put("orderNo", orderNo);
         params.put("paymentChannel", paymentChannel);
         params.put("bankToken", bankToken);
         params.put("totalPrice", String.valueOf(totalPrice));
         params.put("clientIp", clientIp);
         params.put("userAgent", "Huifenqi");
         params.put("referer", "huizhaofang.com");
         params.put("phone", phone);
         params.put("openId", openId);
         return requestRemote(userServiceUrl, params);
     }
	
	/**
	 * @Title: confirmCharge
	 * @Description: 标准版-确认支付
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年11月25日 上午11:48:25
	 */
	public JsonObject confirmCharge(long userId, String orderNo, String busiOrderNo, String phone, String captcha) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/confirmcharge/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		params.put("busiOrderNo", busiOrderNo);
		params.put("phone", phone);
		params.put("captcha", captcha);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: confirmChargeLite
	 * @Description: 轻量版-确认支付
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午11:02:31
	 */
	public JsonObject confirmChargeLite(long userId, String orderNo, String busiOrderNo, String phone, String captcha) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/confirmcharge/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("orderNo", orderNo);
        params.put("busiOrderNo", busiOrderNo);
        params.put("phone", phone);
        params.put("captcha", captcha);
        return requestRemote(userServiceUrl, params);
    }

	/**
	 * @Title: getCaptcha
	 * @Description: 标准版-发送验证码
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2017年11月25日 上午11:48:25
	 */
	public JsonObject getCaptcha(long userId, String orderNo, String busiOrderNo, String phone, 
			String clientIp, String userAgent, String refer, String operationTerminal, String bankToken) {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/captcha/";
		Map<String, String> params = new HashMap<>();
		params.put("userId", String.valueOf(userId));
		params.put("orderNo", orderNo);
		params.put("busiOrderNo", busiOrderNo);
		params.put("phone", phone);
		params.put("clientIp", clientIp);
		params.put("refer", refer);
		params.put("userAgent", userAgent);
		params.put("operationTerminal", operationTerminal);
		params.put("bankToken", bankToken);
		return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: getLiteCaptcha
	 * @Description: 轻量版-发送验证码
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2018年1月4日 上午11:00:05
	 */
	public JsonObject getLiteCaptcha(long userId, String orderNo, String busiOrderNo, String phone, 
             String clientIp, String userAgent, String refer, String operationTerminal, String bankToken) {
         String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/captcha/";
         Map<String, String> params = new HashMap<>();
         params.put("userId", String.valueOf(userId));
         params.put("orderNo", orderNo);
         params.put("busiOrderNo", busiOrderNo);
         params.put("phone", phone);
         params.put("clientIp", clientIp);
         params.put("refer", refer);
         params.put("userAgent", userAgent);
         params.put("operationTerminal", operationTerminal);
         params.put("bankToken", bankToken);
         return requestRemote(userServiceUrl, params);
     }
	
	/**
	 * @Title: comfirmBuget
	 * @Description: 轻量版-租客确认账单
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午2:25:06
	 */
	public JsonObject comfirmBuget(long userId, String bugetBaseNo) {
	    String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/comfirmbuget/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("bugetBaseNo", bugetBaseNo);
        return requestRemote(userServiceUrl, params);
	}
	
	/**
	 * @Title: checkCancelRent
	 * @Description: 轻量版-检验租客能否退租
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月27日 上午11:26:23
	 */
	public JsonObject checkCancelRent(long userId, String agencyId, String signerPhone, String userName, String idNo) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/checkretrent/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("agencyId", agencyId);
        params.put("signerPhone", signerPhone);
        params.put("identityId", idNo);
        params.put("signerName", userName);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: cancelRent
	 * @Description: 轻量版-租客退租
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午4:48:15
	 */
	public JsonObject cancelRent(long userId, String agencyId) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/retrent/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("agencyId", agencyId);
        return requestRemote(userServiceUrl, params);
    }
	
	/**
	 * @Title: setAutoPayRent
	 * @Description: 轻量版-设置自动交租
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午4:58:42
	 */
	public JsonObject setAutoPayRent(long userId, String agencyId, int isWithhold, String bankToken) {
        String userServiceUrl = configuration.hfqHzfSaaSLiteServiceHost + "/saaslite/hzf/buget/setwithhold/";
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("agencyId", agencyId);
        params.put("isWithhold", String.valueOf(isWithhold));
        params.put("bankToken", bankToken);
        return requestRemote(userServiceUrl, params);
    }
	

	/**
	 * @Title: requestRemote
	 * @Description: 获取POST请求的jsonObject
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2017年11月22日 上午10:23:11
	 */
	public JsonObject requestRemote(String url,Map<String, String> params ) {
		String postRet = "";
		try {
			postRet = HttpUtil.post(url, params);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		String code = result.status.code;
		if (!"0".equals(code)) {
			throw new ServiceInvokeFailException(Integer.parseInt(code), result.status.description);
		}
		return result.result;
	}
	
	/**
	 * @Title: getIdNoAndUserName
	 * @Description: 查询用户ID对应的身份证号和用户名称
	 * @return JsonObject
	 * @author 叶东明
	 * @dateTime 2017年12月26日 上午11:46:08
	 */
	public JsonObject getIdNoAndUserName(long userId) {
	    JsonObject retData = new JsonObject();
	    JsonObject serviceRetJo = paymentNewService.listBankcard(userId);
        if (serviceRetJo.has("cardList") && !serviceRetJo.get("cardList").isJsonNull()) {
            JsonArray cardList = serviceRetJo.getAsJsonArray("cardList");
            if (cardList.size() > 0) {
                JsonObject cardObject = (JsonObject) cardList.get(0);
                retData.addProperty("userName", cardObject.get("realName").isJsonNull()?"":cardObject.get("realName").getAsString());
                retData.addProperty("idNo", cardObject.get("idNo").isJsonNull()?"":cardObject.get("idNo").getAsString());
            }
        }
        return retData;
	}
	
	/**
	 * @Title: getBankNameByCode
	 * @Description: 通过银行卡号获取银行名称
	 * @return JsonObject
	 * @author Arison
	 * @dateTime 2018年1月22日 下午4:48:18
	 */
	public JsonObject getUserNameByOrderNo(long userId,String orderNo) throws Exception {
		String userServiceUrl = configuration.hfqHzfSassServiceHost + "/v3/hzf/reqtenantnamebyorderno/";
		Map<String, String> params = new HashMap<>();
		params.put("orderNo", orderNo);
		params.put("userId", userId+"");

		return requestRemote(userServiceUrl, params);
	}
}
