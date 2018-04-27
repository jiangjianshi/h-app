package com.huifenqi.hzf_platform.service;

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
import com.huifenqi.jedi.security.sign.exception.RsaEncryptorException;
import com.huifenqi.usercomm.dao.UserInfoRepository;
import com.huifenqi.usercomm.dao.UserRepository;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by arison on 2017/11/12.
 */
@Component
public class PaymentNewService {

	private static Gson gson = GsonUtil.buildGson();

	@Autowired
	private Configuration configuration;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private PaymentManager paymentManager;

	@Autowired
	private ContractService contractService;

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
		String url = configuration.getPaymentNewServiceUrl() + "api/v1/bankcard/querymine/";
		String merch_id=configuration.hzfMerchId;
		Map<String, String> params = new HashMap<>();
		params.put("user_id", userId + "");
		params.put("merch_id", merch_id);
		params.put("version", "1.0");
		String sign = "";
		try {
			sign = paymentManager.sign(params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
		}
		if(StringUtil.isBlank(sign)){
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
		}

		params.put("sign", sign);
		String jsonString = GsonUtil.buildGson().toJson(params);
		Map<String, String> newParam = new HashMap<String, String>();
		String content="";
		try {
			content= Base64.encodeBase64String(paymentManager.encrypt(jsonString).getBytes());
		} catch (RsaEncryptorException e) {
			e.printStackTrace();
		}
		newParam.put("merchant_id", configuration.hzfMerchId);
		newParam.put("content",content);

		String result=null;
		try {
			String tresultStrlDecryp = HttpUtil.post(url, newParam);
			if (StringUtil.isNotBlank(tresultStrlDecryp)) {
				result = paymentManager.decrypt(tresultStrlDecryp).replace("\\", "").replace("\"[", "[")
						.replace("]\"", "]").replace("\\\"","\"")
						.replace("\"{","{").replace("}\"","}");
			}
		} catch (Exception e) {
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}
		JsonObject serviceRespJo = gson.fromJson(result, JsonObject.class);
		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		String code = statusJo.getAsJsonPrimitive("code").getAsString();
		if("050130".equals(code)){
			JsonObject j= new JsonObject();
			j.add("cardList",new JsonArray());
			return j;
		}

		if (!"0".equals(code)) {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			throw new ServiceInvokeFailException(Integer.parseInt(code),errMsg);
		}

		JsonObject retObj = getTransformedBankcardJsonObject(serviceRespJo.getAsJsonObject("result"));
		return retObj;
	}

	/**
	 * 绑定银行卡
	 * @param userId
	 * @param userName
	 * @param phone
	 * @param idCardNo
	 * @param bankcardNo
	 * @return
	 */

	public JsonObject bindBankcard( long userId, String userName, String phone, String idCardNo,
								   String bankcardNo, String merch_id) {

		String urlInfo = configuration.getPaymentNewServiceUrl() + "api/v1/bankcard/info/";
		Map<String, String> paramsInfo = new HashMap<>();
		paramsInfo.put("bank_card_no", bankcardNo);

		JsonObject bankInfo=requestInvokeReq(urlInfo, paramsInfo);
		String bankName="";
		String bankSn="";
		String binType="";

		if(bankInfo!=null && !bankInfo.isJsonNull()){
			bankName=bankInfo.get("bankName").getAsString();
			bankSn=bankInfo.get("bankSn").getAsString();
			binType=bankInfo.get("binType").getAsString();
		}

		String url = configuration.getPaymentNewServiceUrl() + "api/v1/bankcard/bind/req/";
		Map<String, String> params = new HashMap<>();
		params.put("real_name", userName);
		params.put("user_id", String.valueOf(userId));
		params.put("mobile_no", phone);
		params.put("cert_no", idCardNo);
		params.put("bank_card_no", bankcardNo);
		params.put("bank_card_type", binType);
		params.put("id_card_type", String.valueOf(111));
		params.put("acc_type", String.valueOf(0));
		params.put("bank_sn", bankSn);
		params.put("merch_id", merch_id);
		JsonObject ret= requestInvokeReq(url, params);
		JsonObject retNew= new JsonObject();
		String bankToken=ret.get("data").getAsJsonObject().get("bank_token").getAsString();
		retNew.addProperty("bankToken",bankToken);
		return retNew;
	}
	/**
	 * 确认绑定银行卡
	 *
	 * @param captcha
	 * @return
	 */
	public JsonObject confirmBindBandcard(long userId,String merchId, String captcha, String bankToken) {
		String url = configuration.getPaymentNewServiceUrl() + "api/v1/bankcard/bind/confirm/";
		Map<String, String> params = new HashMap<>();
		params.put("user_id", String.valueOf(userId));
		params.put("merch_id", merchId);
		params.put("captcha", captcha);
		params.put("bank_token", bankToken);
		return requestInvokeConfirm(url, params);
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



	private JsonObject requestInvokeReq(String url, Map<String, String> params) {
		params.put("version", "1.0");
		String sign = "";
		try {
			sign = paymentManager.sign(params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
		}
		if(StringUtil.isBlank(sign)){
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
		}
		params.put("sign", sign);


		String jsonString = GsonUtil.buildGson().toJson(params);
		Map<String, String> newParam = new HashMap<String, String>();
		String content="";
		try {
			content= Base64.encodeBase64String(paymentManager.encrypt(jsonString).getBytes());
		} catch (RsaEncryptorException e) {
			e.printStackTrace();
		}
		newParam.put("merchant_id", configuration.hzfMerchId);
		newParam.put("content",content);

		String result=null;
		try {
			String tresultStrlDecryp = HttpUtil.post(url, newParam);
			result = paymentManager.decrypt(tresultStrlDecryp).replace("\\", "").replace("\"[", "[")
				.replace("]\"", "]").replace("\\\"","\"")
			.replace("\"{","{").replace("}\"","}");
		} catch (Exception e) {
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}
		JsonObject serviceRespJo = gson.fromJson(result, JsonObject.class);
		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		String code = statusJo.getAsJsonPrimitive("code").getAsString();
		if (!"0".equals(code)) {
            if ("3005".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"该银行卡已被绑定，请换张卡试试");
            } else if ("3007".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"请输入正确的验证码");
            } else if ("3001".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"身份信息或银行预留手机号有误，请核对后重试");
            }else if ("3014".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"请绑定实名人的银行卡");
            }else if ("3015".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"暂不支持绑定该银行卡，请换张卡再试");
            }else {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"绑卡失败，请稍后再试");
            }
        }

		return serviceRespJo.getAsJsonObject("result");
	}

    private JsonObject requestInvokeConfirm(String url, Map<String, String> params) {
        params.put("version", "1.0");
        String sign = "";
        try {
            sign = paymentManager.sign(params);
        } catch (Exception e) {
            throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
        }
        if(StringUtil.isBlank(sign)){
            throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE,"requestInvoke 接口调用失败");
        }
        params.put("sign", sign);


        String jsonString = GsonUtil.buildGson().toJson(params);
        Map<String, String> newParam = new HashMap<String, String>();
        String content="";
        try {
            content= Base64.encodeBase64String(paymentManager.encrypt(jsonString).getBytes());
        } catch (RsaEncryptorException e) {
            e.printStackTrace();
        }
        newParam.put("merchant_id", configuration.hzfMerchId);
        newParam.put("content",content);

        String result=null;
        try {
            String tresultStrlDecryp = HttpUtil.post(url, newParam);
            result = paymentManager.decrypt(tresultStrlDecryp).replace("\\", "").replace("\"[", "[")
                    .replace("]\"", "]").replace("\\\"","\"")
                    .replace("\"{","{").replace("}\"","}");
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ServiceInvokeFailException("服务暂时不可用");
        }
        JsonObject serviceRespJo = gson.fromJson(result, JsonObject.class);
        JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
        String code = statusJo.getAsJsonPrimitive("code").getAsString();
        if (!"0".equals(code)) {
            if ("3101".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"请输入正确的验证码");
            }else if ("3105".equals(code)) {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"请绑定实名人的银行卡");
            }else {
                throw new BaseException(ErrorMsgCode.BINDCARD_ERROR,"绑卡失败，请稍后再试");
            }
        }

        return serviceRespJo.getAsJsonObject("result");
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
			listArray = jsonObject.get("bank_card_list").getAsJsonArray();
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
				//绑卡ID
				replaceOrCreateKey(bankcardObj, subObj, "bind_id", "bindId");
				replaceOrCreateKey(bankcardObj, subObj, "card_name", "cardName");
				replaceOrCreateKey(bankcardObj, subObj, "bank_code", "bankCode");
				replaceOrCreateKey(bankcardObj, subObj, "bank_card_name", "bankCardName");
				replaceOrCreateKey(bankcardObj, subObj, "bank_card_type", "bankCardType");
				replaceOrCreateKey(bankcardObj, subObj, "bank_token", "bankToken");
				replaceOrCreateKey(bankcardObj, subObj, "mobile_no", "phone");
				//新加用户真实姓名
				replaceOrCreateKey(bankcardObj, subObj, "real_name", "realName");
				replaceOrCreateKey(bankcardObj, subObj, "cert_no", "idNo");

				String bankCardNo=bankcardObj.get("bank_card_no").getAsString();
				if(StringUtil.isNotBlank(bankCardNo)){
					String cardTop=bankCardNo.substring(0,4);
					String cardLast=bankCardNo.substring(bankCardNo.length()-4,bankCardNo.length());
					subObj.addProperty("cardTop",cardTop);
					subObj.addProperty("cardLast",cardLast);
				}
				newArray.add(subObj);
			}
		}
		JsonObject retObj = new JsonObject();

		retObj.add("cardList", newArray);
		//add by arison
		retObj.addProperty("cardNum", newArray.size());
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

	/**
	 * 根据不同渠道，计算需要支付的金额
	 *
	 * @param channel
	 * @param price
	 * @return
	 */
	public JsonObject calcChannelFee(String channel, Long price) {
		String url = configuration.getPaymentServiceUrl() + "api/pay/pingpp/channelincome/calc/";
		Map<String, String> params = new HashMap<>();
		params.put("channel", channel);
		params.put("price", price + "");
	/*	if (voucher_id != -1) {
			params.put("voucher_id", voucher_id + "");
		}*/
		return request(url, params);
	}


}
