package com.huifenqi.hzf_platform.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.NoSuchUserException;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.usercomm.dao.UserInfoRepository;
import com.huifenqi.usercomm.dao.UserRepository;
import com.huifenqi.usercomm.dao.contract.ContractSnapshotRepository;
import com.huifenqi.usercomm.dao.contract.SubpayRepository;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.UserInfo;
import com.huifenqi.usercomm.domain.contract.ContractSnapshot;

/**
 * Created by arison on 2015/11/12.
 */
@Component
public class PaymentService {

	private static Gson gson = GsonUtil.buildGson();

	@Autowired
	private Configuration configuration;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private ContractService contractService;

	@Autowired
	private SubpayRepository subpayRepository;

	@Autowired
	private ContractSnapshotRepository contractSnapshotRepository;

	private Logger logger = LoggerFactory.getLogger(PaymentService.class);
	
	//支付成功
	public static final int ORDER_PAY_STATUS_SUCCESS = 0;
	//支付失败
	public static final int ORDER_PAY_STATUS_FAILED = 1;
	//支付中
	public static final int ORDER_PAY_STATUS_PAYING = 2;

	/**
	 * 查询用户银行卡
	 * 先尝试从userInfo中获取身份证号，然后再尝试从分期合同中获取
	 *
	 * @param userId
	 * @return
	 */
	public JsonObject listBankcard(long userId) {
		String logSuffix = "userId: " + userId;
		String url = configuration.getPaymentServiceUrl() + "api/comm/bankcard/list/";

		// 先尝试从UserInfo中获取
		logger.info("尝试从UserInfo中获取身份证号，" + logSuffix);
		String idCardNo = null;
		UserInfo userInfo = userInfoRepository.findByUserId(userId);
		if (userInfo != null) {
			idCardNo = userInfo.getUserIdNo();
		}

		if (StringUtils.isBlank(idCardNo)) {
			try{
				// 尝试从分期合同中获取
				logger.info("尝试从分期合同中获取身份证号，" + logSuffix);
				idCardNo = getIdCardNo(userId);
			} catch (BaseException e) {
				if (ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED == e.getErrorcode()) {
					// 尝试从User中获取
					logger.info("尝试从User中获取身份证号，" + logSuffix);
					User user = userRepository.findByUseridAndState(userId,1);
					idCardNo = user.getUserIdNo();
				}
			}
		}

		logger.info("用户身份证号：" + idCardNo + ", " + logSuffix);

		Map<String, String> params = new HashMap<>();
		params.put("user_id", userId + "");
		params.put("id_card_no", idCardNo);

		JsonObject resJsonObj = request(url, params);
		JsonObject retObj = getTransformedBankcardJsonObject(resJsonObj);

		return retObj;
	}
	/**
	 * 查询交易结果状态
	 * 
	 * @param orderNo
	 * @return
	 */
	public JsonObject findChargeStatus(String orderNo) {
		String url = configuration.getPaymentServiceUrl() + "api/payment/charge/state/query/";
		Map<String, String> params = new HashMap<>();
		params.put("order_no", orderNo);
		return request(url, params);
	}

	/**
	 * 无验证码支付
	 * 
	 * @param channel
	 * @param amount
	 * @param cardTop
	 * @param cardLast
	 * @param idCardNo
	 * @param tradeTitle
	 * @param userId
	 * @param tradeDesc
	 * @param tradeType
	 * @return
	 */
	public JsonObject chargeWithoutCaptcha(String channel, long amount, String cardTop, String cardLast,
			String idCardNo, String tradeTitle, long userId, String tradeDesc, int tradeType) {
		String url = configuration.getPaymentServiceUrl() + "api/payment/charge/nocaptcha/";
		Map<String, String> params = new HashMap<>();
		params.put("channel", channel);
		params.put("amount", amount + "");
		params.put("card_top", cardTop);
		params.put("card_last", cardLast);
		params.put("id_card_no", idCardNo);
		params.put("trade_title", tradeTitle);
		params.put("user_id", userId + "");
		params.put("trade_desc", tradeDesc);
		params.put("trade_type", tradeType + "");
		return request(url, params);
	}

	/**
	 * 查询银行卡信息
	 * 
	 * @param bankCardNo
	 * @return
	 */
	public JsonObject reqBankCardInfo(String bankCardNo) {

		String url = configuration.getPaymentServiceUrl() + "api/bankcard/info/";
		Map<String, String> params = new HashMap<>();
		params.put("bank_card_no", bankCardNo);
		return request(url, params);
	}

	/**
	 * pingpp支付请求
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject requestPingppCharge(Map<String, String> params) {
		return request(configuration.getPaymentServiceUrl() + "api/pay/pingpp/req/", params);
	}

	/**
	 * 易宝支付请求
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject requestYeepayCharge(Map<String, String> params) {
		return request(configuration.getPaymentServiceUrl() + "api/pay/yeepay/req/", params);
	}

	/**
	 * 易宝确认支付
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject confirmYeepayCharge(Map<String, String> params) {
		return request(configuration.getPaymentServiceUrl() + "api/comm/pay/confirm/", params);
	}

	/**
	 * 请求易宝发送验证码
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject reqYeepayCaptcha(Map<String, String> params) {
		String url = null;
		url = configuration.getPaymentServiceUrl() + "api/comm/pay/captcha/req/";
		return request(url, params);
	}

	/**
	 * 京东支付请求
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject requestJdCharge(Map<String, String> params) {
		return request(configuration.getPaymentServiceUrl() + "api/pay/jd/req/", params);
	}

	private JsonObject request(String url, Map<String, String> params) {

		long timestamp = System.currentTimeMillis();
		String sign = genSign(configuration.servicePaymentAuthid, configuration.servicePaymentToken, timestamp);

		params.put("authid", configuration.servicePaymentAuthid);
		params.put("sign", sign);
		params.put("timestamp", timestamp + "");

		String response = null;

		try {
			response = HttpUtil.post(url, params);
		} catch (Exception e) {
			// throw new ServiceInvokeFailException(e);
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}

		JsonObject serviceRespJo = gson.fromJson(response, JsonObject.class);

		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		String code = statusJo.getAsJsonPrimitive("code").getAsString();
		if (!"0".equals(code) && !"01721000".equals(code)) {
			String errMsg = null;
			if (code.startsWith("017")) {
				errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			} else {
				errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			}
			throw new ServiceInvokeFailException(Integer.parseInt(code),errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}

	private static String genSign(String id, String token, long timestamp) {
		return DigestUtils.md5Hex(id + token + timestamp);
	}

	/**
	 * 根据用户手机号获取身份证号码
	 * 
	 * @param userId
	 * @return
	 */
	private String getIdCardNo(Long userId) {
		//User user = userRepository.findOne(userId);
		User user = userRepository.findByUseridAndState(userId,1);
		String idCardNo = null;
		List<ContractSnapshot> contractList = contractService.findContractSnapshots(user);
		if (contractList != null && !contractList.isEmpty()) {
			for (ContractSnapshot cs : contractList) {
				if (!StringUtil.isBlank(cs.getUserIdNo())) {
					idCardNo = cs.getUserIdNo();
					break;
				}
			}
		} else {
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该用户没有签订合同");
		}
		return idCardNo;
	}

	/**
	 * 转换银行卡JsonObject
	 * 
	 * @param jsonObject
	 * @return
	 */
	private JsonObject getTransformedBankcardJsonObject(JsonObject jsonObject) {

		// TODO 统一转化处理
		// 这里仅对查询用户银行卡接口中变化的字段名进行修改，后续需统一处理

		if (jsonObject == null) {
			return null;
		}

		JsonArray listArray = null;
		try {
			listArray = jsonObject.getAsJsonArray("card_list");
		} catch (Exception e) {
			logger.error("failed to parse card_list");
		}

		if (listArray == null) {
			return null;
		}

		JsonArray newArray = new JsonArray();

		Iterator<JsonElement> iterator = listArray.iterator();
		while (iterator.hasNext()) {
			JsonElement next = iterator.next();
			if (next instanceof JsonObject) {

				JsonObject bankcardObj = (JsonObject) next;

				JsonObject subObj = new JsonObject();

				replaceOrCreateKey(bankcardObj, subObj, "bindid", "bindid");
				replaceOrCreateKey(bankcardObj, subObj, "bank_card_top", "card_top");
				replaceOrCreateKey(bankcardObj, subObj, "bank_card_last", "card_last");
				replaceOrCreateKey(bankcardObj, subObj, "card_name", "card_name");
				replaceOrCreateKey(bankcardObj, subObj, "bank_code", "bankcode");
				replaceOrCreateKey(bankcardObj, subObj, "bindvalidthru", "bindvalidthru");
				replaceOrCreateKey(bankcardObj, subObj, "bind_card_channel", "pay_channel");

				JsonElement jEle = subObj.get("pay_channel");

				if (jEle != null) {
					String bankCardChannel = jEle.getAsString();
					subObj.addProperty("pay_channel", Constants.PayHandleChannel.getPayChannel(bankCardChannel));
				}
				newArray.add(subObj);

			}

		}

		JsonObject retObj = new JsonObject();

		retObj.add("card_list", newArray);
		//add by arison
		retObj.addProperty("card_no", newArray.size());
		return retObj;

	}

	/**
	 * 替换JsonObject字段名
	 * 
	 * @param srcObj
	 * @param dstObj
	 * @param oldKey
	 * @param newKey
	 */
	private void replaceOrCreateKey(JsonObject srcObj, JsonObject dstObj, String oldKey, String newKey) {
		if (srcObj == null || dstObj == null) {
			return;
		}

		if (StringUtil.isEmpty(oldKey) || StringUtil.isEmpty(newKey)) {
			return;
		}

		JsonElement jsonElement = srcObj.get(oldKey);
		if (jsonElement != null) {
			dstObj.add(newKey, jsonElement);
		}
	}

	/**
	 * 根据不同渠道，计算需要支付的金额
	 * 
	 * @param channel
	 * @param price
	 * @param voucher_id
	 * @return
	 */
	public JsonObject calcPrice(String channel, Long price, Long voucher_id) {

		String url = configuration.getPaymentServiceUrl() + "api/pay/pingpp/channelincome/calc/";

		Map<String, String> params = new HashMap<>();
		params.put("channel", channel);
		params.put("price", price + "");
		if (voucher_id != -1) {
			params.put("voucher_id", voucher_id + "");
		}
		return request(url, params);
	}

	/**
	 * 绑定银行卡
	 * 
	 * @param platform
	 * @param userId
	 * @param userName
	 * @param phone
	 * @param idCardNo
	 * @param bankcardNo
	 * @param payChannel
	 * @return
	 */
	public JsonObject bindBankcard(String platform, long userId, String userName, String phone, String idCardNo,
			String bankcardNo, String payChannel, String signContractIdentity) {

		String url = configuration.getPaymentServiceUrl() + "api/comm/bankcard/bind/req/";

		Map<String, String> params = new HashMap<>();
		params.put("user_name", userName);
		params.put("user_id", String.valueOf(userId));
		params.put("phone", phone);
		params.put("id_card_no", idCardNo);
		params.put("bank_card_no", bankcardNo);
		params.put("bind_card_channel", Constants.PayHandleChannel.getBankCardChannel(payChannel));
		params.put("sign_contract_identity", signContractIdentity);

		return request(url, params);
	}

	/**
	 * 解绑银行卡
	 * 
	 * @param bindid
	 * @param userId
	 * @return
	 */
	public JsonObject unbindBankcard(String bindid, long userId, String payChannel) {

		String url = configuration.getPaymentServiceUrl() + "api/comm/bankcard/unbind/";

		User user = userRepository.findByUseridAndState(userId,1);
		if (null == user) {
			throw new NoSuchUserException(userId);
		}

		String idCardNo = "";
		try {
		 	idCardNo=getIdCardNo(user.getUserid());
		 } catch(Exception e) {
		 	idCardNo="";
		 }

		Map<String, String> params = new HashMap<>();
		params.put("bind_id", bindid);
		params.put("id_card_no", idCardNo);
		params.put("user_id", userId+"");
		params.put("hfq_oper", idCardNo); // 操作者，客户端传入用户身份证号，参考"支付2.0"接口
		params.put("bind_card_channel", Constants.PayHandleChannel.getBankCardChannel(payChannel));

		return request(url, params);
	}

	/**
	 * 确认绑定银行卡
	 * 
	 * @param phone
	 * @param captcha
	 * @return
	 */
	public JsonObject confirmBindBandcard(String phone, String captcha, String payChannel) {

		String url = configuration.getPaymentServiceUrl() + "api/comm/bankcard/bind/confirm/";

		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		params.put("captcha", captcha);
		params.put("bind_card_channel", Constants.PayHandleChannel.getBankCardChannel(payChannel));

		return request(url, params);
	}
	
	/**
	 * 统一支付接口
	 * @param params
	 * @return
	 */
	public JsonObject requestCharge(Map<String, String> params) {
        params.put("busi_type",2+"");
		return request(configuration.getPaymentServiceUrl() + "api/comm/pay/req/", params);
	}
	
	public JsonObject reqChargeStatus(Map<String, String> params) {
		String url = configuration.getPaymentServiceUrl() + "api/charge/order/detail/";
		return request(url, params);
	}

	public JsonObject reqBankList (Map<String, String> params) {
		String url = configuration.getPaymentServiceUrl() + "api/payrouter/banks/";
		return request(url, params);
	}

	public JsonObject reqBankCardPayChannel (Map<String,String> params) {
		String url = configuration.getPaymentServiceUrl() + "api/payrouter/channel/";
		return request(url,params);
	}
	
	/**
	 * ping++支付
	 * @param params
	 * @return
	 */
	public JsonObject pingppCharge(Map<String, String> params) {
		String url = configuration.getPaymentServiceUrl() + "api/pay/pingpp/charge";
		return request(url,params);
	}
	
	/**
	 * 订单支付结果查询
	 * @param params
	 * @return
	 */
	public JsonObject reqOrderPayStatus(Map<String, String> params) {
		String url = configuration.getPaymentServiceUrl() + "api/pay/pingpp/charge/query";
		return request(url,params);
	}
}
