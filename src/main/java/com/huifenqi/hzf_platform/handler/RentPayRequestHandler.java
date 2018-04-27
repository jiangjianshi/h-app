package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.comm.UserUtils;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.service.PaymentNewService;
import com.huifenqi.hzf_platform.service.PaymentService;
import com.huifenqi.hzf_platform.service.RentPayService;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.vo.PayChannelVo;
import com.huifenqi.hzf_platform.vo.RentBugetDetailVo;
import com.huifenqi.hzf_platform.vo.RentContractBugetVo;

/**
 * Created by YDM on 2017/11/21.
 */
@Service
public class RentPayRequestHandler extends BaseRequestHandler {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentNewService paymentNewService;

	@Autowired
	private RentPayService rentPayService;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private Configuration configuration;
	
	public static final String CARD_NO = "card-no";
	
	/**
	 * @Title: bankCardSupportList
	 * @Description: 获取平台支持的银行卡列表
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午4:27:05
	 */
	public Responses bankCardSupportList(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		int payType = getDefaultIntParam("payType", 9);
		JSONObject retData = new JSONObject();
		String bankSupportList = "目前支持以下银行的储蓄卡：";
		JsonObject jsonData = rentPayService.bankSupportList(userId, payType);
		if (!jsonData.isJsonNull()) {
			JsonArray arrayData = jsonData.getAsJsonArray("list");
			if (arrayData.size() > 0) {
				for (int i = 0; i < arrayData.size(); i++) {
					JsonObject objectData = (JsonObject) arrayData.get(i);
					String bankName = objectData.get("bankName").getAsString();
					bankSupportList = bankSupportList + bankName + "、";
				}
				bankSupportList = bankSupportList.substring(0, bankSupportList.length() - 1);
				bankSupportList = bankSupportList + "。";
			}
		}
		
		retData.put("bankSupportList", bankSupportList);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: getBankCardNameByCode
	 * @Description: 通过银行卡号获取银行名称（校验身份证号和银行卡号，并根据银行卡号返回银行卡类型）
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午4:47:33
	 */
	public Responses getBankCardNameByCode(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String idCardNo = getStringParam("idCardNo", "身份证号").toUpperCase();
		String bankCardNo = getStringParam("bankCardNo", "银行卡号");
		// 校验身份证号
		if (!RulesVerifyUtil.verifyIdNo(idCardNo)) {
			throw  new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "身份证号格式不正确");
		}
		// 校验银行卡：银行卡号长度在10到21之间；输入的银行卡号属于当前支持的银行
		if (!RulesVerifyUtil.verifyBankCardLength(bankCardNo)) {
			throw  new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "银行卡号格式不正确");
		}
		JSONObject retData = new JSONObject();
		String bankCardName = "";
		// 通过银行卡号获取银行卡类型
		JsonObject jsonData = rentPayService.getBankNameByCode(userId, bankCardNo);
		if (!jsonData.isJsonNull()) {
			bankCardName = jsonData.get("bankName").getAsString();
		}
		
		retData.put("bankCardName", bankCardName);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: bindBankCardReq
	 * @Description: 发起绑定银行卡（校验手机号，发送验证码；校验四要素）
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午2:25:58
	 */
	public Responses bindBankCardReq(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String userName = getStringParam("userName", "用户姓名");
		String phone = getStringParam("phone", "手机号");
		String idCardNo = getStringParam("idCardNo", "身份证号").toUpperCase();
		String bankCardNo = getStringParam("bankCardNo", "银行卡号");
		// 校验手机号
		if (!RulesVerifyUtil.verifyPhone(phone)) {
			throw  new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "手机号码格式不正确");
		}
		// 发起对四要素的校验
		JsonObject  retData =paymentNewService.bindBankcard( userId, userName,  phone,  idCardNo,
				bankCardNo, configuration.hzfMerchId);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: bindBankCard
	 * @Description: 确认绑定银行卡
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月21日 下午2:26:14
	 */
	public Responses bindBankCard(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String phone = getStringParam("phone", "手机号");
		String captcha = getStringParam("captcha", "验证码");
		String bankToken = getStringParam("bankToken", "银行卡token");
		JSONObject retData = new JSONObject();
		paymentNewService.confirmBindBandcard(userId,configuration.hzfMerchId, captcha, bankToken);
		retData.put("result", "确认绑卡完成！");
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: getBankCardList
	 * @Description: 获取绑定银行卡列表
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月21日 上午11:48:48
	 */
	public Responses getBankCardList(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		JsonObject serviceRetJo = paymentNewService.listBankcard(userId);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(serviceRetJo);
		return responses;
	}
	
	/**
     * @Title: reqPayList
     * @Description: 查询本人所有合同的账单列表（每个合同下展示最近一条未支付的租房账单和所有未支付的其他账单）
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年11月22日 下午3:17:57
     */
	public Responses reqPayList(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        int financeType = Constants.financeType.ALL_FINANCE_TYPE;
        int payStatus = Constants.payStatus.NOT_FINISHED_STATUS;
        int paymentType = Constants.paymentType.EXPENDITURE_PAYMENT_TYPE;
        String phone = getStringParam("phone", "手机号");
        JSONObject resultData = new JSONObject();
        // 存储最终合同账单list
        List<RentContractBugetVo> contractList = new ArrayList<RentContractBugetVo>();
        // 判断该用户是否绑过卡，如果绑过卡 表示用户已实名，入参增加用户姓名和身份证号，用来过滤账单数据
        String userName = "";
        String idNo = "";
        JsonObject paramRetJo = rentPayService.getIdNoAndUserName(userId);
        if (!paramRetJo.isJsonNull() && paramRetJo.has("userName") && paramRetJo.has("idNo")) {
            userName = paramRetJo.get("userName").getAsString();
            idNo = paramRetJo.get("idNo").getAsString();
        }
        JsonObject jsonData = rentPayService.reqPayList(userId, financeType, payStatus, paymentType, phone, userName, idNo);
        if (jsonData.has("list") && !jsonData.get("list").isJsonNull()) {
            JsonArray arrayData = jsonData.getAsJsonArray("list");
            // ===================获取所有合同的账单列表
            contractList = this.getContractPayList(arrayData);
            if (!CollectionUtils.isEmpty(contractList)) {
                List<RentContractBugetVo> contractList1 = contractList.stream().sorted(Comparator.comparing(RentContractBugetVo::getRentContractCode).reversed()).collect(Collectors.toList());
                contractList = contractList1.stream().sorted(Comparator.comparing(RentContractBugetVo::getContractEffectStatus)).collect(Collectors.toList());
            }
        }
        resultData.put("result", contractList);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(resultData);
        return responses;
    }

	/**
	 * @Title: reqPayListV2
	 * @Description: 查询本人所有合同的账单列表（每个合同下展示最近一条未支付的租房账单和所有未支付的其他账单）V2
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月22日 下午3:17:57
	 */
	public Responses reqPayListV2(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
		int financeType = Constants.financeType.ALL_FINANCE_TYPE;
		int payStatus = Constants.payStatus.NOT_FINISHED_STATUS;
		int paymentType = Constants.paymentType.EXPENDITURE_PAYMENT_TYPE;
		String phone = getStringParam("phone", "手机号");
		JSONObject resultData = new JSONObject();
		// 判断该用户是否绑过卡，如果绑过卡 表示用户已实名，入参增加用户姓名和身份证号，用来过滤账单数据
        String userName = "";
        String idNo = "";
        JsonObject paramRetJo = rentPayService.getIdNoAndUserName(userId);
        if (!paramRetJo.isJsonNull() && paramRetJo.has("userName") && paramRetJo.has("idNo")) {
            userName = paramRetJo.get("userName").getAsString();
            idNo = paramRetJo.get("idNo").getAsString();
        }
		// 存储最终合同账单列表的list
        List<RentContractBugetVo> contractList = new ArrayList<RentContractBugetVo>();
        JsonObject contractData = rentPayService.reqPayList(userId, financeType, payStatus, paymentType, phone, userName, idNo);
        if (contractData.has("list") && !contractData.get("list").isJsonNull()) {
            JsonArray arrayData = contractData.getAsJsonArray("list");
            // ===================获取所有合同的账单列表
            contractList = this.getContractPayList(arrayData);
            if (!CollectionUtils.isEmpty(contractList)) {
                List<RentContractBugetVo> contractList1 = contractList.stream().sorted(Comparator.comparing(RentContractBugetVo::getRentContractCode).reversed()).collect(Collectors.toList());
                contractList = contractList1.stream().sorted(Comparator.comparing(RentContractBugetVo::getContractEffectStatus)).collect(Collectors.toList());
            }
        }
        resultData.put("contractList", contractList);
        
        // 存储最终账单组列表的list
        JsonArray bugetLiteList = new JsonArray();
        // ===================获取账单组数据
        JsonObject bugetLiteData = rentPayService.reqRentBugetList(userId, phone);
        if (bugetLiteData.has("baseList") && !bugetLiteData.get("baseList").isJsonNull()) {
            bugetLiteList = bugetLiteData.getAsJsonArray("baseList");
        }
        resultData.put("bugetLiteList", bugetLiteList);
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(resultData);
		return responses;
	}
	
	public List<RentContractBugetVo> getContractPayList(JsonArray arrayData) {
		List<RentContractBugetVo> contractList = new ArrayList<RentContractBugetVo>();
		if (arrayData.size() > 0) {
			for (int i = 0; i < arrayData.size(); i++) {
				RentContractBugetVo rentContractBugetVo = new RentContractBugetVo();
				JsonObject dataObject = (JsonObject) arrayData.get(i);
				rentContractBugetVo.setRentContractCode(dataObject.get("rentContractCode").isJsonNull()?"":dataObject.get("rentContractCode").getAsString());
				rentContractBugetVo.setAgencyPhone(dataObject.get("brokerPhone").isJsonNull()?"":dataObject.get("brokerPhone").getAsString());
				rentContractBugetVo.setAgencyName(dataObject.get("brokerName").isJsonNull()?"":dataObject.get("brokerName").getAsString());
				StringBuilder address = new StringBuilder();
				String district = dataObject.get("district").isJsonNull()?"":dataObject.get("district").getAsString();
				String communityName = dataObject.get("communityName").isJsonNull()?"":dataObject.get("communityName").getAsString();
				String roomName = dataObject.get("roomName").isJsonNull()?"":dataObject.get("roomName").getAsString();
				address.append(district).append(dataObject.get("apartmentName").isJsonNull()?"":dataObject.get("apartmentName").getAsString()).append(communityName).append(dataObject.get("houseNo").isJsonNull()?"":dataObject.get("houseNo").getAsString()).append(roomName);
				rentContractBugetVo.setAddress(address.toString());
				int isConfirmed = dataObject.get("isConfirmed").getAsInt();
				// 遍历合同下账单列表
				JsonArray arrayDataList = dataObject.getAsJsonArray("bugets");
				List<RentBugetDetailVo> bugetList0 = new ArrayList<RentBugetDetailVo>();
				List<RentBugetDetailVo> bugetList1 = new ArrayList<RentBugetDetailVo>();
				List<RentBugetDetailVo> bugetList = new ArrayList<RentBugetDetailVo>();
				if (arrayDataList.size() > 0) {
					RentBugetDetailVo bugetDetailVo1 = new RentBugetDetailVo();
					// ===============获取第一条租房账单
					JsonObject objectData1 = (JsonObject) arrayDataList.get(0);
					int financeType1 = objectData1.get("financeType").getAsInt();
					if (financeType1 == 102) {
						bugetDetailVo1.setIsConfirmed(isConfirmed);
						bugetDetailVo1.setBugetNo(objectData1.get("bugetNo").isJsonNull()?"":objectData1.get("bugetNo").getAsString());
						bugetDetailVo1.setFinanceType(financeType1);
						bugetDetailVo1.setCurrentIndex(objectData1.get("currentIndex").getAsInt());
						bugetDetailVo1.setTotalIndex(objectData1.get("totalIndex").getAsInt());
						bugetDetailVo1.setIsDelay(objectData1.get("isDelay").getAsInt());
						bugetDetailVo1.setDueFlag(objectData1.get("dueFlag").getAsInt());
						int paystatus1 = objectData1.get("payStatus").getAsInt();
						bugetDetailVo1.setPayStatus(paystatus1);
						int paymenttype1 = objectData1.get("cashierType").getAsInt();
						int bugetStatus1 = objectData1.get("bugetStatus").getAsInt();
						/** B端收付款账单状态cashierType分为：1 付款账单、2 收款账单；
						    B端账单状态payStatus分为：0 未支付、1 欠款、2 已支付；
						    B端账单撤销状态bugetStatus分为：0 正常账单、1 已撤销账单 */
						if (bugetStatus1 == 0) {
							if (paystatus1 == 1) {
								bugetDetailVo1.setPayStatus(Constants.payStatus.NOT_PAID_STATUS);
							}
						} else {
							bugetDetailVo1.setPayStatus(Constants.payStatus.INVALID_STATUS);
							bugetDetailVo1.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
							paystatus1 = Constants.payStatus.INVALID_STATUS;
						}
						int dueFlag = objectData1.get("dueFlag").getAsInt();
						int isDelay = objectData1.get("isDelay").getAsInt();
						if (isConfirmed == 1) {// 合同已确认
							if (paystatus1 == Constants.payStatus.NOT_PAID_STATUS || paystatus1 == Constants.payStatus.DEBT_STATUS) {// 待支付/付款欠款
								if (dueFlag == 0) {// 获取未逾期并且小于?天
									if (isDelay == 0) {// 付款日期是应付款日期
										String payDate = objectData1.get("payDate").isJsonNull()?"":objectData1.get("payDate").getAsString();
										int remainDays = DateUtil.getDiffDays(payDate);
										if (remainDays <= 7 && remainDays > 0) {
											bugetDetailVo1.setRemainDays(remainDays);
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
										} else if (remainDays == 0) {
											bugetDetailVo1.setRemainDays(0);
											if (paymenttype1 == 1) {
												bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(0));
											}
										} else if (remainDays < 0) {
											bugetDetailVo1.setRemainDays(-1);
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(-1));
										} else {
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
										}
										bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("payDate")));
									} else if (isDelay == 1) {// 付款日期是延期付款日期
										String deptPayDate = objectData1.get("deptPayDate").isJsonNull()?"":objectData1.get("deptPayDate").getAsString();
										int remainDays = DateUtil.getDiffDays(deptPayDate);
										if (remainDays <= 7 && remainDays > 0) {
											bugetDetailVo1.setRemainDays(remainDays);
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
										} else if (remainDays == 0) {
											bugetDetailVo1.setRemainDays(0);
											if (paymenttype1 == 1) {
												bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(0));
											}
										} else if (remainDays < 0) {
											bugetDetailVo1.setRemainDays(-1);
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(-1));
										} else {
											bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
										}
										bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("deptPayDate")));
									}
								} else {
									if (isDelay == 0) {// 付款日期是应付款日期
										bugetDetailVo1.setRemainDays(-1);
										bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(-1));
										bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("payDate")));
									} else if (isDelay == 1) {// 付款日期是延期付款日期
										bugetDetailVo1.setRemainDays(-1);
										bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(-1));
										bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("deptPayDate")));
									}
								}
							} else {
								if (isDelay == 0) {// 付款日期是应付款日期
									bugetDetailVo1.setRemainDays(8);
									bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
									bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("payDate")));
								} else if (isDelay == 1) {// 付款日期是延期付款日期
									bugetDetailVo1.setRemainDays(8);
									bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
									bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("deptPayDate")));
								}
							}
						} else {
							if (isDelay == 0) {// 付款日期是应付款日期
								bugetDetailVo1.setRemainDays(8);
								bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
								bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("payDate")));
							} else if (isDelay == 1) {// 付款日期是延期付款日期
								bugetDetailVo1.setRemainDays(8);
								bugetDetailVo1.setRemainDaysName(Constants.remainDaysName.get(8));
								bugetDetailVo1.setPayDate(DateUtil.formatPayDate(objectData1.get("deptPayDate")));
							}
						}
						bugetDetailVo1.setPrice(objectData1.get("price").getAsInt());
						bugetDetailVo1.setPaidPrice(objectData1.get("paidPrice").getAsInt());
						// 欠款字段判断：如果账单状态是欠款，则展示欠款金额；如果账单是未支付，则展示账单总金额
						if (paystatus1 == Constants.payStatus.DEBT_STATUS) {
							bugetDetailVo1.setDeptPrice(objectData1.get("deptPrice").getAsInt());
						} else if (paystatus1 == Constants.payStatus.NOT_PAID_STATUS) {
							bugetDetailVo1.setDeptPrice(objectData1.get("price").getAsInt());
						}
						// 收款/付款话术
						if (paymenttype1 == 1) {
							bugetDetailVo1.setPaymentTypeComment(Constants.paymentTypeComment.get(1));
						} else if (paymenttype1 == 2) {
							bugetDetailVo1.setPaymentTypeComment(Constants.paymentTypeComment.get(0));
						}
						// 账单话术
						if (paystatus1 == Constants.payStatus.PAID_STATUS || bugetStatus1 == 1) {
							bugetDetailVo1.setBugetComment(Constants.bugetComment.get(2));
						} else {
							if (paymenttype1 == 1) {
								bugetDetailVo1.setBugetComment(Constants.bugetComment.get(1));
							} else if (paymenttype1 == 2) {
								bugetDetailVo1.setBugetComment(Constants.bugetComment.get(0));
							}
						}
						// 交易状态对应字体颜色
						if (paystatus1 == Constants.payStatus.NOT_PAID_STATUS) {
							bugetDetailVo1.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
							bugetDetailVo1.setPayStatusName(Constants.payStatusName.get(0));
						} else if (paystatus1 == Constants.payStatus.PAID_STATUS) {
							bugetDetailVo1.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
							bugetDetailVo1.setPayStatusName(Constants.payStatusName.get(2));
						} else if (paystatus1 == Constants.payStatus.DEBT_STATUS) {
							bugetDetailVo1.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
							bugetDetailVo1.setPayStatusName(Constants.payStatusName.get(0));
						} else if (paystatus1 == Constants.payStatus.INVALID_STATUS) {
							bugetDetailVo1.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
							bugetDetailVo1.setPayStatusName(Constants.payStatusName.get(3));
						}
						// 可以发起支付的状态返回
						if (isConfirmed == 1) {
							if (paymenttype1 == 1) {// 待支付账单
								if (paystatus1 == Constants.payStatus.NOT_PAID_STATUS || paystatus1 == Constants.payStatus.DEBT_STATUS) {
									bugetDetailVo1.setSponsorPayStatus(Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
								} else {
									bugetDetailVo1.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
								}
							} else {
								bugetDetailVo1.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
							}
						} else {
							bugetDetailVo1.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
						}
						// B端收入账单，站在用户的角度是去支付账单；反之。
						if (paymenttype1 == 1) {
							bugetDetailVo1.setPaymentType(2);
						} else if (paymenttype1 == 2) {
							bugetDetailVo1.setPaymentType(1);
						}
						bugetDetailVo1.setStartDate(DateUtil.formatPayDate(objectData1.get("startDate")));
						bugetDetailVo1.setEndDate(DateUtil.formatPayDate(objectData1.get("endDate")));
						bugetDetailVo1.setBugetFlag(0);
						bugetList0.add(bugetDetailVo1);
					}
					// ===============获取其他账单
					for (int k = 0; k < arrayDataList.size(); k++) {
						RentBugetDetailVo bugetDetailVo = new RentBugetDetailVo();
						JsonObject objectData = (JsonObject) arrayDataList.get(k);
						int financeType2 = objectData.get("financeType").getAsInt();
						if (financeType2 == 110) {
							bugetDetailVo1.setIsConfirmed(isConfirmed);
							bugetDetailVo.setBugetNo(objectData.get("bugetNo").isJsonNull()?"":objectData.get("bugetNo").getAsString());
							bugetDetailVo.setFinanceType(financeType2);
							bugetDetailVo.setCurrentIndex(objectData.get("currentIndex").getAsInt());
							bugetDetailVo.setTotalIndex(objectData.get("totalIndex").getAsInt());
							bugetDetailVo.setIsDelay(objectData.get("isDelay").getAsInt());
							bugetDetailVo.setDueFlag(objectData.get("dueFlag").getAsInt());
							int paystatus = objectData.get("payStatus").getAsInt();
							bugetDetailVo.setPayStatus(paystatus);
							int paymenttype = objectData.get("cashierType").getAsInt();
							int bugetStatus = objectData.get("bugetStatus").getAsInt();
							// 设置收款账单状态：如果订单是已撤销状态，则 支付状态=3已失效
							if (bugetStatus == 0) {
								if (paystatus == 1) {
									bugetDetailVo.setPayStatus(Constants.payStatus.NOT_PAID_STATUS);
								}
							} else {
								bugetDetailVo.setPayStatus(Constants.payStatus.INVALID_STATUS);
								bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
								paystatus = Constants.payStatus.INVALID_STATUS;
							}
							int dueFlag = objectData.get("dueFlag").getAsInt();
							int isDelay = objectData.get("isDelay").getAsInt();
							if (isConfirmed == 1) {// 合同已确认
								if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {// 待支付/欠款账单
									if (dueFlag == 0) {// 获取未逾期并且小于?天
										if (isDelay == 0) {// 付款日期是应付款日期
											String payDate = objectData.get("payDate").isJsonNull()?"":objectData.get("payDate").getAsString();
											int remainDays = DateUtil.getDiffDays(payDate);
											if (remainDays <= 7 && remainDays > 0) {
												bugetDetailVo.setRemainDays(remainDays);
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
											} else if (remainDays == 0) {
												bugetDetailVo.setRemainDays(0);
												if (paymenttype == 1) {
													bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
												}
											} else if (remainDays < 0) {
												bugetDetailVo.setRemainDays(-1);
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
											} else {
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
											}
											bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
										} else if (isDelay == 1) {// 付款日期是延期付款日期
											String deptPayDate = objectData.get("deptPayDate").isJsonNull()?"":objectData.get("deptPayDate").getAsString();
											int remainDays = DateUtil.getDiffDays(deptPayDate);
											if (remainDays <= 7 && remainDays > 0) {
												bugetDetailVo.setRemainDays(remainDays);
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
											} else if (remainDays == 0) {
												bugetDetailVo.setRemainDays(0);
												if (paymenttype == 1) {
													bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
												}
											} else if (remainDays < 0) {
												bugetDetailVo.setRemainDays(-1);
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
											} else {
												bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
											}
											bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
										}
									} else {
										if (isDelay == 0) {// 付款日期是应付款日期
											bugetDetailVo.setRemainDays(-1);
											bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
											bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
										} else if (isDelay == 1) {// 付款日期是延期付款日期
											bugetDetailVo.setRemainDays(-1);
											bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
											bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
										}
									}
								} else {
									if (isDelay == 0) {// 付款日期是应付款日期
										bugetDetailVo.setRemainDays(8);
										bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
										bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
									} else if (isDelay == 1) {// 付款日期是延期付款日期
										bugetDetailVo.setRemainDays(8);
										bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
										bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
									}
								}
							} else {
								if (isDelay == 0) {// 付款日期是应付款日期
									bugetDetailVo.setRemainDays(8);
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
									bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
								} else if (isDelay == 1) {// 付款日期是延期付款日期
									bugetDetailVo.setRemainDays(8);
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
									bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
								}
							}
							bugetDetailVo.setPrice(objectData.get("price").getAsInt());
							bugetDetailVo.setPaidPrice(objectData.get("paidPrice").getAsInt());
							// 欠款字段判断：如果账单状态是欠款，则展示欠款金额；如果账单是未支付，则展示账单总金额
							if (paystatus == Constants.payStatus.DEBT_STATUS) {
								bugetDetailVo.setDeptPrice(objectData.get("deptPrice").getAsInt());
							} else if (paystatus == Constants.payStatus.NOT_PAID_STATUS) {
								bugetDetailVo.setDeptPrice(objectData.get("price").getAsInt());
							}
							// 收款/付款话术
							if (paymenttype == 1) {
								bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(1));
							} else if (paymenttype == 2) {
								bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(0));
							}
							// 账单话术
							if (paystatus == Constants.payStatus.PAID_STATUS || bugetStatus == 1) {
								bugetDetailVo.setBugetComment(Constants.bugetComment.get(2));
							} else {
								if (paymenttype == 1) {
									bugetDetailVo.setBugetComment(Constants.bugetComment.get(1));
								} else if (paymenttype == 2) {
									bugetDetailVo.setBugetComment(Constants.bugetComment.get(0));
								}
							}
							// 交易状态对应字体颜色
							if (paystatus == Constants.payStatus.NOT_PAID_STATUS) {
								bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
								bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
							} else if (paystatus == Constants.payStatus.PAID_STATUS) {
								bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
								bugetDetailVo.setPayStatusName(Constants.payStatusName.get(2));
							} else if (paystatus == Constants.payStatus.DEBT_STATUS) {
								bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
								bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
							} else if (paystatus == Constants.payStatus.INVALID_STATUS) {
								bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
								bugetDetailVo.setPayStatusName(Constants.payStatusName.get(3));
							}
							// 可以发起支付的状态返回
							if (isConfirmed == 1) {
								if (paymenttype == 1) {// 待支付账单
									if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
										bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
									} else {
										bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
									}
								} else {
									bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
								}
							} else {
								bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
							}
							// B端收入账单，站在用户的角度是去支付账单；反之。
							if (paymenttype == 1) {
								bugetDetailVo.setPaymentType(2);
							} else if (paymenttype == 2) {
								bugetDetailVo.setPaymentType(1);
							}
							bugetDetailVo.setBugetFlag(0);
							bugetList1.add(bugetDetailVo);
						}
					}
					// 按照支付状态、应付款日期升序排序
					List<RentBugetDetailVo> bugetTempList = bugetList1.stream().sorted(Comparator.comparing(RentBugetDetailVo::getPayDate)).collect(Collectors.toList());
					bugetList1 = bugetTempList.stream().sorted(Comparator.comparing(RentBugetDetailVo::getPayStatus)).collect(Collectors.toList());
					bugetList.addAll(bugetList0);
					bugetList.addAll(bugetList1);
					
				}
				rentContractBugetVo.setBugets(bugetList);
				contractList.add(rentContractBugetVo);
			}
		}
		
		return contractList;
	}
	
	/**
	 * @Title: reqContractPayList
	 * @Description: 查询合同下的账单列表
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月22日 下午3:18:41
	 */
	public Responses reqContractPayList(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String rentContractCode = getStringParam("rentContractCode", "出租合同号");
		int financeType = getDefaultIntParam("financeType", Constants.financeType.ALL_FINANCE_TYPE);
		int payStatus = getDefaultIntParam("payStatus", Constants.payStatus.ALL_PAY_STATUS);
		int paymentType = getDefaultIntParam("paymentType", Constants.paymentType.ALL_PAYMENT_TYPE);
		String phone = getStringParam("phone", "手机号");
		JsonObject jsonData = rentPayService.reqContractPayList(userId, rentContractCode, financeType, payStatus, paymentType, phone);
		JSONObject retData = new JSONObject();
		List<RentBugetDetailVo> bugetRentList = new ArrayList<RentBugetDetailVo>();
		List<RentBugetDetailVo> bugetOtherList = new ArrayList<RentBugetDetailVo>();
		
		String agencyName = jsonData.get("brokerName").isJsonNull()?"":jsonData.get("brokerName").getAsString();
		String agencyPhone = jsonData.get("brokerPhone").isJsonNull()?"":jsonData.get("brokerPhone").getAsString();
		if (jsonData.has("list") && !jsonData.get("list").isJsonNull()) {
			JsonArray arrayData = jsonData.getAsJsonArray("list");
			int isConfirmed = jsonData.get("isConfirmed").getAsInt();
			if (arrayData.size() > 0) {
				// 获取指定账单类型下的账单列表
				if (financeType == Constants.financeType.RENT_FINANCE_TYPE) {
					// ===================获取租房账单列表
					bugetRentList = this.getBugetRentList(isConfirmed, arrayData);
					// 按照账单类型、支付状态、应付款日期升序排序
					if (!CollectionUtils.isEmpty(bugetRentList)) {
						List<RentBugetDetailVo> bugetTempList1 = bugetRentList.stream().sorted(Comparator.comparing(RentBugetDetailVo::getPayDate)).collect(Collectors.toList());
						List<RentBugetDetailVo> bugetTempList2 = bugetTempList1.stream().sorted(Comparator.comparing(RentBugetDetailVo::getStatusOrder)).collect(Collectors.toList());
						bugetRentList = bugetTempList2.stream().sorted(Comparator.comparing(RentBugetDetailVo::getFinanceType)).collect(Collectors.toList());
					}
					// 已确认的合同下的租房账单，最小时间允许去支付
					if (!CollectionUtils.isEmpty(bugetRentList)) {
						RentBugetDetailVo rentDetail = bugetRentList.get(0);
						int paystatus = rentDetail.getPayStatus();
						int paymenttype = rentDetail.getPaymentType();
						// 可以发起支付的状态返回
						if (isConfirmed == 1) {
							if (paymenttype == 2) {// 待支付账单
								if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
									rentDetail.setSponsorPayStatus(Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
								} else {
									rentDetail.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
								}
							} else {
								rentDetail.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
							}
						} else {
							rentDetail.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
						}
						bugetRentList.remove(0);
						bugetRentList.add(0, rentDetail);
					}
					retData.put("bugetResult", bugetRentList);
				} else {
					// ===================获取其他账单列表
					bugetOtherList = this.getBugetOtherList(isConfirmed, arrayData);
					// 按照账单类型、支付状态、应付款日期升序排序
					if (!CollectionUtils.isEmpty(bugetOtherList)) {
						List<RentBugetDetailVo> bugetTempList1 = bugetOtherList.stream().sorted(Comparator.comparing(RentBugetDetailVo::getPayDate)).collect(Collectors.toList());
						List<RentBugetDetailVo> bugetTempList2 = bugetTempList1.stream().sorted(Comparator.comparing(RentBugetDetailVo::getStatusOrder)).collect(Collectors.toList());
						bugetOtherList = bugetTempList2.stream().sorted(Comparator.comparing(RentBugetDetailVo::getFinanceType)).collect(Collectors.toList());
					}
					retData.put("bugetResult", bugetOtherList);
				}
			}
		}
		retData.put("agencyName", agencyName);
		retData.put("agencyPhone", agencyPhone);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		
		return responses;
	}
	
	public List<RentBugetDetailVo> getBugetRentList(int isConfirmed, JsonArray arrayData) {
		List<RentBugetDetailVo> bugetRentList = new ArrayList<RentBugetDetailVo>();
		for (int i = 0; i < arrayData.size(); i++) {
			RentBugetDetailVo bugetDetailVo = new RentBugetDetailVo();
			JsonObject objectData = (JsonObject) arrayData.get(i);
			bugetDetailVo.setIsConfirmed(isConfirmed);
			bugetDetailVo.setBugetNo(objectData.get("bugetNo").isJsonNull()?"":objectData.get("bugetNo").getAsString());
			bugetDetailVo.setFinanceType(objectData.get("financeType").getAsInt());
			bugetDetailVo.setCurrentIndex(objectData.get("currentIndex").getAsInt());
			bugetDetailVo.setTotalIndex(objectData.get("totalIndex").getAsInt());
			bugetDetailVo.setIsDelay(objectData.get("isDelay").getAsInt());
			bugetDetailVo.setDueFlag(objectData.get("dueFlag").getAsInt());
			bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
			int paystatus = objectData.get("payStatus").getAsInt();
			bugetDetailVo.setPayStatus(paystatus);
			int paymenttype = objectData.get("cashierType").getAsInt();
			int bugetStatus = objectData.get("bugetStatus").getAsInt();
			/** B端收付款账单状态cashierType分为：1 付款账单、2 收款账单；
			    B端账单状态payStatus分为：0 未支付、1 欠款、2 已支付；
			    B端账单撤销状态bugetStatus分为：0 正常账单、1 已撤销账单 */
			if (bugetStatus == 0) {
				if (paymenttype == 2) {
					if (paystatus == 0) {
						bugetDetailVo.setPayStatus(Constants.payStatus.NOT_RECEIPT_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
						paystatus = Constants.payStatus.NOT_RECEIPT_STATUS;
					} else if (paystatus == 1) {
						bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPT_DEBT_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
						paystatus = Constants.payStatus.RECEIPT_DEBT_STATUS;
					} else if (paystatus == 2) {
						bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPTED_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.RECEIPTED_STATUS);
						paystatus = Constants.payStatus.RECEIPTED_STATUS;
					}
				} else {
					if (paystatus == 0) {
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
					} else if (paystatus == 1) {
						bugetDetailVo.setPayStatus(Constants.payStatus.NOT_PAID_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
					} else if (paystatus == 2) {
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.PAID_STATUS);
					}
				}
			} else {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
				bugetDetailVo.setPayStatus(Constants.payStatus.INVALID_STATUS);
				paystatus = Constants.payStatus.INVALID_STATUS;
				bugetDetailVo.setStatusOrder(Constants.payStatusReference.INVALID_STATUS);
			}
			int dueFlag = objectData.get("dueFlag").getAsInt();
			int isDelay = objectData.get("isDelay").getAsInt();
			if (isConfirmed == 1) {// 合同已确认
				if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {// 待支付/付款欠款
					if (dueFlag == 0) {// 获取未逾期并且小于?天
						if (isDelay == 0) {// 付款日期是应付款日期
							String payDate = objectData.get("payDate").isJsonNull()?"":objectData.get("payDate").getAsString();
							int remainDays = DateUtil.getDiffDays(payDate);
							if (remainDays <= 7 && remainDays > 0) {
								bugetDetailVo.setRemainDays(remainDays);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								bugetDetailVo.setRemainDays(0);
								if (paymenttype == 1) {
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								bugetDetailVo.setRemainDays(-1);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							} else {
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
							}
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							String deptPayDate = objectData.get("deptPayDate").isJsonNull()?"":objectData.get("deptPayDate").getAsString();
							int remainDays = DateUtil.getDiffDays(deptPayDate);
							if (remainDays <= 7 && remainDays > 0) {
								bugetDetailVo.setRemainDays(remainDays);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								bugetDetailVo.setRemainDays(0);
								if (paymenttype == 1) {
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								bugetDetailVo.setRemainDays(-1);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							} else {
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
							}
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
						}
					} else {
						if (isDelay == 0) {// 付款日期是应付款日期
							bugetDetailVo.setRemainDays(-1);
							bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							bugetDetailVo.setRemainDays(-1);
							bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
						}
					}
				} else {
					if (isDelay == 0) {// 付款日期是应付款日期
						bugetDetailVo.setRemainDays(8);
						bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
					} else if (isDelay == 1) {// 付款日期是延期付款日期
						bugetDetailVo.setRemainDays(8);
						bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
					}
				}
			} else {
				if (isDelay == 0) {// 付款日期是应付款日期
					bugetDetailVo.setRemainDays(8);
					bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
					bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
				} else if (isDelay == 1) {// 付款日期是延期付款日期
					bugetDetailVo.setRemainDays(8);
					bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
					bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
				}
			}
			bugetDetailVo.setPrice(objectData.get("price").getAsInt());
			bugetDetailVo.setPaidPrice(objectData.get("paidPrice").getAsInt());
			// 欠款字段判断：如果账单状态是欠款，则展示欠款金额；如果账单是未支付，则展示账单总金额
			if (paystatus == Constants.payStatus.DEBT_STATUS || paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				bugetDetailVo.setDeptPrice(objectData.get("deptPrice").getAsInt());
			} else if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.NOT_RECEIPT_STATUS) {
				bugetDetailVo.setDeptPrice(objectData.get("price").getAsInt());
			}
			// 收款/付款话术
			if (paymenttype == 1) {
				bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(1));
			} else if (paymenttype == 2) {
				bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(0));
			}
			// 账单话术
			if (paystatus == Constants.payStatus.PAID_STATUS || paystatus == Constants.payStatus.RECEIPTED_STATUS || bugetStatus == 1) {
				bugetDetailVo.setBugetComment(Constants.bugetComment.get(2));
			} else {
				if (paymenttype == 1) {
					bugetDetailVo.setBugetComment(Constants.bugetComment.get(1));
				} else if (paymenttype == 2) {
					bugetDetailVo.setBugetComment(Constants.bugetComment.get(0));
				}
			}
			// 交易状态对应字体颜色；账单状态对应的名称
			if (paystatus == Constants.payStatus.NOT_PAID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
			} else if (paystatus == Constants.payStatus.PAID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(2));
			} else if (paystatus == Constants.payStatus.DEBT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
			} else if (paystatus == Constants.payStatus.INVALID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(3));
			} else if (paystatus == Constants.payStatus.NOT_RECEIPT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(4));
			} else if (paystatus == Constants.payStatus.RECEIPTED_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(5));
			} else if (paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(4));
			}
			// B端收入账单，站在用户的角度是去支付账单；反之。
			if (paymenttype == 1) {
				bugetDetailVo.setPaymentType(2);
			} else if (paymenttype == 2) {
				bugetDetailVo.setPaymentType(1);
			}
			if (objectData.get("financeType").getAsInt() == Constants.financeType.RENT_FINANCE_TYPE) {
				bugetDetailVo.setStartDate(DateUtil.formatPayDate(objectData.get("startDate")));
				bugetDetailVo.setEndDate(DateUtil.formatPayDate(objectData.get("endDate")));
			}
			bugetDetailVo.setBugetFlag(0);
			bugetRentList.add(bugetDetailVo);
		}
		
		return bugetRentList;
	}
	
	public List<RentBugetDetailVo> getBugetOtherList(int isConfirmed, JsonArray arrayData) {
		List<RentBugetDetailVo> bugetOtherList = new ArrayList<RentBugetDetailVo>();
		for (int i = 0; i < arrayData.size(); i++) {
			RentBugetDetailVo bugetDetailVo = new RentBugetDetailVo();
			JsonObject objectData = (JsonObject) arrayData.get(i);
			bugetDetailVo.setIsConfirmed(isConfirmed);
			bugetDetailVo.setBugetNo(objectData.get("bugetNo").isJsonNull()?"":objectData.get("bugetNo").getAsString());
			bugetDetailVo.setFinanceType(objectData.get("financeType").getAsInt());
			bugetDetailVo.setCurrentIndex(objectData.get("currentIndex").getAsInt());
			bugetDetailVo.setTotalIndex(objectData.get("totalIndex").getAsInt());
			bugetDetailVo.setIsDelay(objectData.get("isDelay").getAsInt());
			bugetDetailVo.setDueFlag(objectData.get("dueFlag").getAsInt());
			int paystatus = objectData.get("payStatus").getAsInt();
			bugetDetailVo.setPayStatus(paystatus);
			int paymenttype = objectData.get("cashierType").getAsInt();
			int bugetStatus = objectData.get("bugetStatus").getAsInt();
			/** B端收付款账单状态cashierType分为：1 付款账单、2 收款账单；
			    B端账单状态payStatus分为：0 未支付、1 欠款、2 已支付；
			    B端账单撤销状态bugetStatus分为：0 正常账单、1 已撤销账单 */
			if (bugetStatus == 0) {
				if (paymenttype == 2) {
					if (paystatus == 0) {
						bugetDetailVo.setPayStatus(Constants.payStatus.NOT_RECEIPT_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
						paystatus = Constants.payStatus.NOT_RECEIPT_STATUS;
					} else if (paystatus == 1) {
						bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPT_DEBT_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
						paystatus = Constants.payStatus.RECEIPT_DEBT_STATUS;
					} else if (paystatus == 2) {
						bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPTED_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.RECEIPTED_STATUS);
						paystatus = Constants.payStatus.RECEIPTED_STATUS;
					}
				} else {
					if (paystatus == 0) {
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
					} else if (paystatus == 1) {
						bugetDetailVo.setPayStatus(Constants.payStatus.NOT_PAID_STATUS);
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
					} else if (paystatus == 2) {
						bugetDetailVo.setStatusOrder(Constants.payStatusReference.PAID_STATUS);
					}
				}
			} else {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
				bugetDetailVo.setPayStatus(Constants.payStatus.INVALID_STATUS);
				paystatus = Constants.payStatus.INVALID_STATUS;
				bugetDetailVo.setStatusOrder(Constants.payStatusReference.INVALID_STATUS);
			}
			int dueFlag = objectData.get("dueFlag").getAsInt();
			int isDelay = objectData.get("isDelay").getAsInt();
			if (isConfirmed == 1) {// 合同已确认
				if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {// 待支付/付款欠款
					if (dueFlag == 0) {// 获取未逾期并且小于?天
						if (isDelay == 0) {// 付款日期是应付款日期
							String payDate = objectData.get("payDate").isJsonNull()?"":objectData.get("payDate").getAsString();
							int remainDays = DateUtil.getDiffDays(payDate);
							if (remainDays <= 7 && remainDays > 0) {
								bugetDetailVo.setRemainDays(remainDays);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								bugetDetailVo.setRemainDays(0);
								if (paymenttype == 1) {
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								bugetDetailVo.setRemainDays(-1);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							} else {
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
							}
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							String deptPayDate = objectData.get("deptPayDate").isJsonNull()?"":objectData.get("deptPayDate").getAsString();
							int remainDays = DateUtil.getDiffDays(deptPayDate);
							if (remainDays <= 7 && remainDays > 0) {
								bugetDetailVo.setRemainDays(remainDays);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								bugetDetailVo.setRemainDays(0);
								if (paymenttype == 1) {
									bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								bugetDetailVo.setRemainDays(-1);
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							} else {
								bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
							}
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
						}
					} else {
						if (isDelay == 0) {// 付款日期是应付款日期
							bugetDetailVo.setRemainDays(-1);
							bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							bugetDetailVo.setRemainDays(-1);
							bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(-1));
							bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
						}
					}
				} else {
					if (isDelay == 0) {// 付款日期是应付款日期
						bugetDetailVo.setRemainDays(-1);
						bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
					} else if (isDelay == 1) {// 付款日期是延期付款日期
						bugetDetailVo.setRemainDays(-1);
						bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
					}
				}
			} else {
				if (isDelay == 0) {// 付款日期是应付款日期
					bugetDetailVo.setRemainDays(-1);
					bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
					bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
				} else if (isDelay == 1) {// 付款日期是延期付款日期
					bugetDetailVo.setRemainDays(-1);
					bugetDetailVo.setRemainDaysName(Constants.remainDaysName.get(8));
					bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
				}
			}
			bugetDetailVo.setPrice(objectData.get("price").getAsInt());
			bugetDetailVo.setPaidPrice(objectData.get("paidPrice").getAsInt());
			// 欠款字段判断：如果账单状态是欠款，则展示欠款金额；如果账单是未支付，则展示账单总金额
			if (paystatus == Constants.payStatus.DEBT_STATUS || paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				bugetDetailVo.setDeptPrice(objectData.get("deptPrice").getAsInt());
			} else if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.NOT_RECEIPT_STATUS) {
				bugetDetailVo.setDeptPrice(objectData.get("price").getAsInt());
			}
			// 收款/付款话术
			if (paymenttype == 1) {
				bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(1));
			} else if (paymenttype == 2) {
				bugetDetailVo.setPaymentTypeComment(Constants.paymentTypeComment.get(0));
			}
			// 账单话术
			if (paystatus == Constants.payStatus.PAID_STATUS || paystatus == Constants.payStatus.RECEIPTED_STATUS || bugetStatus == 1) {
				bugetDetailVo.setBugetComment(Constants.bugetComment.get(2));
			} else {
				if (paymenttype == 1) {
					bugetDetailVo.setBugetComment(Constants.bugetComment.get(1));
				} else if (paymenttype == 2) {
					bugetDetailVo.setBugetComment(Constants.bugetComment.get(0));
				}
			}
			// 交易状态对应字体颜色；账单状态对应的名称
			if (paystatus == Constants.payStatus.NOT_PAID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
			} else if (paystatus == Constants.payStatus.PAID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(2));
			} else if (paystatus == Constants.payStatus.DEBT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(0));
			} else if (paystatus == Constants.payStatus.INVALID_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(3));
			} else if (paystatus == Constants.payStatus.NOT_RECEIPT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(4));
			} else if (paystatus == Constants.payStatus.RECEIPTED_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_SUCCESS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(5));
			} else if (paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.WAIT_DEAL_STATUS);
				bugetDetailVo.setPayStatusName(Constants.payStatusName.get(4));
			}
			// 可以发起支付的状态返回
			if (isConfirmed == 1) {
				if (paymenttype == 1) {// 待支付账单
					if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
						bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
					} else {
						bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
					}
				} else {
					bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
				}
			} else {
				bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
			}
			// B端收入账单，站在用户的角度是去支付账单；反之。
			if (paymenttype == 1) {
				bugetDetailVo.setPaymentType(2);
			} else if (paymenttype == 2) {
				bugetDetailVo.setPaymentType(1);
			}
			if (objectData.get("financeType").getAsInt() == Constants.financeType.RENT_FINANCE_TYPE) {
				bugetDetailVo.setStartDate(DateUtil.formatPayDate(objectData.get("startDate")));
				bugetDetailVo.setEndDate(DateUtil.formatPayDate(objectData.get("endDate")));
			}
			bugetDetailVo.setBugetFlag(0);
			bugetOtherList.add(bugetDetailVo);
		}
		
		return bugetOtherList;
	}
	
	/**
	 * @Title: reqSubPayDetail
	 * @Description: 查询账单详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月23日 下午2:18:41
	 */
	public Responses reqSubPayDetail(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String bugetNo = getStringParam("bugetNo", "账单编号");
		int bugetFlag = getIntParam("bugetFlag", "合同账单标识");
		JSONObject retData = new JSONObject();
		if (bugetFlag == 0) {// 标准版账单详情
			JsonObject jsonData = rentPayService.reqSubPayDetail(userId, bugetNo);
			retData = this.getRentBugetDetail(bugetNo, userId, jsonData);
		} else if (bugetFlag == 1) {// 简版账单详情
			
		}
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: getRentBugetDetail
	 * @Description: 获取标准版账单详情
	 * @return JSONObject
	 * @author 叶东明
	 * @dateTime 2017年12月20日 上午11:39:18
	 */
	public JSONObject getRentBugetDetail(String bugetNo, long userId, JsonObject jsonData) {
		JSONObject retData = new JSONObject();
		if (jsonData != null) {
			int isConfirmed = jsonData.get("isConfirmed").getAsInt();
			retData.put("isConfirmed", isConfirmed);
			retData.put("bugetNo", jsonData.get("bugetNo").isJsonNull()?"":jsonData.get("bugetNo").getAsString());
			retData.put("currentIndex", jsonData.get("currentIndex").getAsInt());
			retData.put("totalIndex", jsonData.get("totalIndex").getAsInt());
			retData.put("dueFlag", jsonData.get("dueFlag").getAsInt());
			retData.put("isDelay", jsonData.get("isDelay").getAsInt());
			retData.put("financeType", jsonData.get("financeType").getAsInt());
			int paystatus = jsonData.get("payStatus").getAsInt();
			retData.put("payStatus", paystatus);
			int paymenttype = jsonData.get("cashierType").getAsInt();
			int bugetStatus = jsonData.get("bugetStatus").getAsInt();
			/** B端收付款账单状态cashierType分为：1 付款账单、2 收款账单；
			    B端账单状态payStatus分为：0 未支付、1 欠款、2 已支付；
			    B端账单撤销状态bugetStatus分为：0 正常账单、1 已撤销账单 */
			if (bugetStatus == 0) {
				if (paymenttype == 2) {
					if (paystatus == 0) {
						retData.put("payStatus", Constants.payStatus.NOT_RECEIPT_STATUS);
						paystatus = Constants.payStatus.NOT_RECEIPT_STATUS;
					} else if (paystatus == 2) {
						retData.put("payStatus", Constants.payStatus.RECEIPTED_STATUS);
						paystatus = Constants.payStatus.RECEIPTED_STATUS;
					} else if (paystatus == 1) {
						retData.put("payStatus", Constants.payStatus.RECEIPT_DEBT_STATUS);
						paystatus = Constants.payStatus.RECEIPT_DEBT_STATUS;
					}
				} else {
					if (paystatus == 1) {
						retData.put("payStatus", Constants.payStatus.NOT_PAID_STATUS);
					}
				}
			} else {
				retData.put("payStatus", Constants.payStatus.INVALID_STATUS);
				paystatus = Constants.payStatus.INVALID_STATUS;
			}
			retData.put("bugetStatus", bugetStatus);
			int dueFlag = jsonData.get("dueFlag").getAsInt();
			int isDelay = jsonData.get("isDelay").getAsInt();
			if (isConfirmed == 1) {// 合同已确认
				if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {// 待支付/付款欠款
					if (dueFlag == 0) {// 获取未逾期并且小于?天
						if (isDelay == 0) {// 付款日期是应付款日期
							String payDate = jsonData.get("payDate").isJsonNull()?"":jsonData.get("payDate").getAsString();
							int remainDays = DateUtil.getDiffDays(payDate);
							if (remainDays <= 7 && remainDays > 0) {
								retData.put("remainDays", remainDays);
								retData.put("remainDaysName", Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								retData.put("remainDays", 0);
								if (paymenttype == 1) {
									retData.put("remainDaysName", Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								retData.put("remainDays", -1);
								retData.put("remainDaysName", Constants.remainDaysName.get(-1));
							} else {
								retData.put("remainDaysName", Constants.remainDaysName.get(8));
							}
							retData.put("payDate", DateUtil.formatPayDate(jsonData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							String deptPayDate = jsonData.get("deptPayDate").isJsonNull()?"":jsonData.get("deptPayDate").getAsString();
							int remainDays = DateUtil.getDiffDays(deptPayDate);
							if (remainDays <= 7 && remainDays > 0) {
								retData.put("remainDays", remainDays);
								retData.put("remainDaysName", Constants.remainDaysName.get(1) + remainDays + "天");
							} else if (remainDays == 0) {
								retData.put("remainDays", 0);
								if (paymenttype == 1) {
									retData.put("remainDaysName", Constants.remainDaysName.get(0));
								}
							} else if (remainDays < 0) {
								retData.put("remainDays", -1);
								retData.put("remainDaysName", Constants.remainDaysName.get(-1));
							} else {
								retData.put("remainDaysName", Constants.remainDaysName.get(8));
							}
							retData.put("payDate", DateUtil.formatPayDate(jsonData.get("deptPayDate")));
						}
					} else {
						if (isDelay == 0) {// 付款日期是应付款日期
							retData.put("remainDays", -1);
							retData.put("remainDaysName", Constants.remainDaysName.get(-1));
							retData.put("payDate", DateUtil.formatPayDate(jsonData.get("payDate")));
						} else if (isDelay == 1) {// 付款日期是延期付款日期
							retData.put("remainDays", -1);
							retData.put("remainDaysName", Constants.remainDaysName.get(-1));
							retData.put("payDate", DateUtil.formatPayDate(jsonData.get("deptPayDate")));
						}
					}
				} else {
					if (isDelay == 0) {// 付款日期是应付款日期
						retData.put("remainDays", -1);
						retData.put("remainDaysName", Constants.remainDaysName.get(8));
						retData.put("payDate", DateUtil.formatPayDate(jsonData.get("payDate")));
					} else if (isDelay == 1) {// 付款日期是延期付款日期
						retData.put("remainDays", -1);
						retData.put("remainDaysName", Constants.remainDaysName.get(8));
						retData.put("payDate", DateUtil.formatPayDate(jsonData.get("deptPayDate")));
					}
				}
			} else {
				if (isDelay == 0) {// 付款日期是应付款日期
					retData.put("remainDays", -1);
					retData.put("remainDaysName", Constants.remainDaysName.get(8));
					retData.put("payDate", DateUtil.formatPayDate(jsonData.get("payDate")));
				} else if (isDelay == 1) {// 付款日期是延期付款日期
					retData.put("remainDays", -1);
					retData.put("remainDaysName", Constants.remainDaysName.get(8));
					retData.put("payDate", DateUtil.formatPayDate(jsonData.get("deptPayDate")));
				}
			}
			retData.put("price", jsonData.get("price").getAsInt());
			retData.put("paidPrice", jsonData.get("paidPrice").getAsInt());
			// 欠款字段判断：如果账单状态是欠款，则展示欠款金额；如果账单是未支付，则展示账单总金额
			if (paystatus == Constants.payStatus.DEBT_STATUS || paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				retData.put("deptPrice", jsonData.get("deptPrice").getAsInt());
			} else if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.NOT_RECEIPT_STATUS) {
				retData.put("deptPrice", jsonData.get("price").getAsInt());
			}
			// 拼接地址信息
			StringBuilder address = new StringBuilder();
			String district = jsonData.get("district").isJsonNull()?"":jsonData.get("district").getAsString();
			String communityName = jsonData.get("communityName").isJsonNull()?"":jsonData.get("communityName").getAsString();
			String roomName = jsonData.get("roomName").isJsonNull()?"":jsonData.get("roomName").getAsString();
			address.append(district).append(jsonData.get("apartmentName").isJsonNull()?"":jsonData.get("apartmentName")).append(communityName).append(jsonData.get("houseNo").isJsonNull()?"":jsonData.get("houseNo").getAsString()).append(roomName);
			retData.put("address", address.toString());
			// 账单状态对应的名称
			if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
				retData.put("payStatusName", Constants.payStatusName.get(0));
				retData.put("payDate", retData.get("payDate").toString());
			} else if (paystatus == Constants.payStatus.PAID_STATUS) {
				retData.put("payStatusName", Constants.payStatusName.get(2));
			} else if (paystatus == Constants.payStatus.INVALID_STATUS) {
				retData.put("payStatusName", Constants.payStatusName.get(3));
				retData.put("payDate", retData.get("payDate").toString());
			} else if (paystatus == Constants.payStatus.NOT_RECEIPT_STATUS || paystatus == Constants.payStatus.RECEIPT_DEBT_STATUS) {
				retData.put("payStatusName", Constants.payStatusName.get(4));
				retData.put("payDate", retData.get("payDate").toString());
			} else if (paystatus == Constants.payStatus.RECEIPTED_STATUS) {
				retData.put("payStatusName", Constants.payStatusName.get(5));
				retData.put("payDate", retData.get("payDate").toString());
			}
			// 可以发起支付的状态返回
			if (isConfirmed == 1) {
				if (paymenttype == 1) {// 待支付账单
					if (jsonData.get("financeType").getAsInt() == Constants.financeType.RENT_FINANCE_TYPE) {// 租房账单
						if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
							// 遍历合同下的所有租房账单，判断当前账单是否需要去支付：如果当前账单号和列表中第一条账单号相等，则需要去支付；反之。
							String rentContractCode = getStringParam("rentContractCode", "出租合同号");
							int financeType = Constants.financeType.RENT_FINANCE_TYPE;
							int payStatus = Constants.payStatus.ALL_PAY_STATUS;
							int paymentType = Constants.paymentType.EXPENDITURE_PAYMENT_TYPE;
							String phone = getStringParam("phone", "手机号");
							List<RentBugetDetailVo> bugetList = new ArrayList<RentBugetDetailVo>();
							try {
								bugetList = this.reqOrderPayList(userId, rentContractCode, financeType, payStatus, paymentType, phone);
							} catch (Exception e) {
								throw new ServiceInvokeFailException("查询账单数据失败！");
							}
							if (!CollectionUtils.isEmpty(bugetList)) {
								RentBugetDetailVo bugetDetail = bugetList.get(0);
								if (bugetNo.equals(bugetDetail.getBugetNo())) {
									retData.put("sponsorPayStatus", Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
								} else {
									retData.put("sponsorPayStatus", Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
								}
							}
						} else {
							retData.put("sponsorPayStatus", Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
						}
					} else {// 其他账单
						if (paystatus == Constants.payStatus.NOT_PAID_STATUS || paystatus == Constants.payStatus.DEBT_STATUS) {
							retData.put("sponsorPayStatus", Constants.sponsorPayStatus.IS_SPONSOR_STATUS);
						} else {
							retData.put("sponsorPayStatus", Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
						}
					}
				} else {
					retData.put("sponsorPayStatus", Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
				}
			} else {
				retData.put("sponsorPayStatus", Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
			}
			// B端收入账单，站在用户的角度是去支付账单；反之。
			if (paymenttype == 1) {
				retData.put("paymentType", 2);
			} else if (paymenttype == 2) {
				retData.put("paymentType", 1);
			}
			// 账单金额明细列表判断金额：当前账单类型cashierType=paymentType，金额为正；否则，金额为负
			JSONArray itemArray = new JSONArray();
			JsonArray itemList = jsonData.getAsJsonArray("itemList");
			if (itemList.size() > 0) {
				for (int i = 0; i < itemList.size(); i++) {
					JSONObject itemObject = new JSONObject();
					JsonObject itemData = (JsonObject) itemList.get(i);
					int paymtType = itemData.get("paymentType").isJsonNull()?0:itemData.get("paymentType").getAsInt();
					int bugetItemPrice = itemData.get("bugetItemPrice").isJsonNull()?0:itemData.get("bugetItemPrice").getAsInt();
					if (paymenttype != paymtType && bugetItemPrice > 0) {
						bugetItemPrice = 0 - itemData.get("bugetItemPrice").getAsInt();
					}
					itemObject.put("paymtType", paymtType);
					itemObject.put("bugetItemPrice", bugetItemPrice);
					itemObject.put("bugetItemTypeDesc", itemData.get("bugetItemTypeDesc").isJsonNull()?"":itemData.get("bugetItemTypeDesc").getAsString());
					itemObject.put("bugetItemComment", itemData.get("bugetItemComment").isJsonNull()?"":itemData.get("bugetItemComment").getAsString());
					itemArray.add(itemObject);
				}
			}
			retData.put("itemList", itemArray);
			if (jsonData.get("financeType").getAsInt() == Constants.financeType.RENT_FINANCE_TYPE) {
				retData.put("startDate", DateUtil.formatPayDate(jsonData.get("startDate")));
				retData.put("endDate", DateUtil.formatPayDate(jsonData.get("endDate")));
			}
			retData.put("billProof", "");
			retData.put("completeDate", DateUtil.formatPayDate(jsonData.get("completeDate")));
		}
		return retData;
	}
	
	/**
	 * @Title: reqOrderPayList
	 * @Description: 按照账单类型、支付状态、应付款日期升序排序账单数据
	 * @return List<RentBugetDetailVo>
	 * @author 叶东明
	 * @dateTime 2017年12月8日 下午5:37:56
	 */
	public List<RentBugetDetailVo> reqOrderPayList(long userId, String rentContractCode, int financeType, int payStatus, int paymentType, String phone) throws Exception {
		JsonObject jsonData = rentPayService.reqContractPayList(userId, rentContractCode, financeType, payStatus, paymentType, phone);
		List<RentBugetDetailVo> bugetRentList = new ArrayList<RentBugetDetailVo>();
		if (jsonData.has("list") && !jsonData.get("list").isJsonNull()) {
			JsonArray arrayData = jsonData.getAsJsonArray("list");
			if (arrayData.size() > 0) {
				for (int i = 0; i < arrayData.size(); i++) {
					RentBugetDetailVo bugetDetailVo = new RentBugetDetailVo();
					JsonObject objectData = (JsonObject) arrayData.get(i);
					bugetDetailVo.setBugetNo(objectData.get("bugetNo").isJsonNull()?"":objectData.get("bugetNo").getAsString());
					bugetDetailVo.setFinanceType(objectData.get("financeType").getAsInt());
					bugetDetailVo.setCurrentIndex(objectData.get("currentIndex").getAsInt());
					bugetDetailVo.setTotalIndex(objectData.get("totalIndex").getAsInt());
					bugetDetailVo.setIsDelay(objectData.get("isDelay").getAsInt());
					bugetDetailVo.setDueFlag(objectData.get("dueFlag").getAsInt());
					bugetDetailVo.setSponsorPayStatus(Constants.sponsorPayStatus.NOT_SPONSOR_STATUS);
					bugetDetailVo.setPrice(objectData.get("price").getAsInt());
					bugetDetailVo.setPaidPrice(objectData.get("paidPrice").getAsInt());
					int paystatus = objectData.get("payStatus").getAsInt();
					int isDelay = objectData.get("isDelay").getAsInt();
					int paymenttype = objectData.get("cashierType").getAsInt();
					int bugetStatus = objectData.get("bugetStatus").getAsInt();
					if (isDelay == 0) {// 付款日期是应付款日期
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("payDate")));
					} else if (isDelay == 1) {// 付款日期是延期付款日期
						bugetDetailVo.setPayDate(DateUtil.formatPayDate(objectData.get("deptPayDate")));
					}
					/** B端收付款账单状态cashierType分为：1 付款账单、2 收款账单；
	                B端账单状态payStatus分为：0 未支付、1 欠款、2 已支付；
	                B端账单撤销状态bugetStatus分为：0 正常账单、1 已撤销账单 */
    	            if (bugetStatus == 0) {
    	                if (paymenttype == 2) {
    	                    if (paystatus == 0) {
    	                        bugetDetailVo.setPayStatus(Constants.payStatus.NOT_RECEIPT_STATUS);
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
    	                        paystatus = Constants.payStatus.NOT_RECEIPT_STATUS;
    	                    } else if (paystatus == 1) {
    	                        bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPT_DEBT_STATUS);
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_RECEIPT_STATUS);
    	                        paystatus = Constants.payStatus.RECEIPT_DEBT_STATUS;
    	                    } else if (paystatus == 2) {
    	                        bugetDetailVo.setPayStatus(Constants.payStatus.RECEIPTED_STATUS);
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.RECEIPTED_STATUS);
    	                        paystatus = Constants.payStatus.RECEIPTED_STATUS;
    	                    }
    	                } else {
    	                    if (paystatus == 0) {
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
    	                    } else if (paystatus == 1) {
    	                        bugetDetailVo.setPayStatus(Constants.payStatus.NOT_PAID_STATUS);
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.NOT_PAID_STATUS);
    	                    } else if (paystatus == 2) {
    	                        bugetDetailVo.setStatusOrder(Constants.payStatusReference.PAID_STATUS);
    	                    }
    	                }
    	            } else {
    	                bugetDetailVo.setPaymentTypeColor(Constants.paymentTypeColor.DEAL_INVALID);
    	                bugetDetailVo.setPayStatus(Constants.payStatus.INVALID_STATUS);
    	                paystatus = Constants.payStatus.INVALID_STATUS;
    	                bugetDetailVo.setStatusOrder(Constants.payStatusReference.INVALID_STATUS);
    	            }
					// B端收入账单，站在用户的角度是去支付账单；反之。
					if (paymenttype == 1) {
						bugetDetailVo.setPaymentType(2);
					} else if (paymenttype == 2) {
						bugetDetailVo.setPaymentType(1);
					}
					if (objectData.get("financeType").getAsInt() == Constants.financeType.RENT_FINANCE_TYPE) {
						bugetDetailVo.setStartDate(DateUtil.formatPayDate(objectData.get("startDate")));
						bugetDetailVo.setEndDate(DateUtil.formatPayDate(objectData.get("endDate")));
					}
					bugetDetailVo.setPayStatus(paystatus);
					bugetDetailVo.setBugetFlag(0);
					bugetRentList.add(bugetDetailVo);
				}
			}
		}
		// 按照账单类型、支付状态、应付款日期生序排序
		if (!CollectionUtils.isEmpty(bugetRentList)) {
			List<RentBugetDetailVo> bugetTempList1 = bugetRentList.stream().sorted(Comparator.comparing(RentBugetDetailVo::getPayDate)).collect(Collectors.toList());
			List<RentBugetDetailVo> bugetTempList2 = bugetTempList1.stream().sorted(Comparator.comparing(RentBugetDetailVo::getStatusOrder)).collect(Collectors.toList());
			bugetRentList = bugetTempList2.stream().sorted(Comparator.comparing(RentBugetDetailVo::getFinanceType)).collect(Collectors.toList());
		}
		return bugetRentList;
	}
	
	/**
	 * @Title: reqRentBugetDetailList
	 * @Description: 查询轻量版-账单组详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月23日 上午11:21:20
	 */
	public Responses reqRentBugetDetailList(HttpServletRequest request) throws Exception {
	    long userId = sessionManager.getUserIdFromSession();
	    String bugetBaseNo = getStringParam("bugetBaseNo", "组账单账号");
	    // 判断该用户是否绑过卡，如果绑过卡 表示用户已实名，入参增加用户姓名和身份证号，用来过滤账单数据
        String userName = "";
        String idNo = "";
        JsonObject paramRetJo = rentPayService.getIdNoAndUserName(userId);
        if (!paramRetJo.isJsonNull() && paramRetJo.has("userName") && paramRetJo.has("idNo")) {
            userName = paramRetJo.get("userName").getAsString();
            idNo = paramRetJo.get("idNo").getAsString();
        }
	    JsonObject retData = rentPayService.reqRentBugetDetailList(userId, bugetBaseNo, userName, idNo);
	    
	    Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
	}
	
	/**
	 * @Title: reqBugetDetail
	 * @Description: 查询轻量版-子账单详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月23日 上午11:26:02
	 */
	public Responses reqBugetDetail(HttpServletRequest request) throws Exception {
        String bugetNo = getStringParam("bugetNo", "子账单账号");
        JsonObject retData = rentPayService.reqBugetDetail(bugetNo);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }

	/**
	 * @Title: createOrder
	 * @Description: 创建订单
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月23日 下午2:18:41
	 */
	public Responses createOrder(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String bugetNo = getStringParam("bugetNo", "账单编号");
		String price = getStringParam("price", "订单金额");
		String platform = getPlatform();
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
		String operationTerminal = Constants.operateTerminal.WEB;
		if(platform.equals(Constants.platform.ANDROID)){
			operationTerminal = Constants.operateTerminal.ANDROID;
		}else if (platform.equals(Constants.platform.IOS)){
			operationTerminal = Constants.operateTerminal.IOS;
		}
		JSONObject retData = new JSONObject();
		JsonObject jsonData = null;
		if (bugetFlag == 0) {
		    jsonData = rentPayService.createOrder(userId, bugetNo, price, operationTerminal);
		} else {
		    jsonData = rentPayService.createLiteOrder(userId, bugetNo, price, operationTerminal);
		}
		if (!jsonData.isJsonNull()) {
			retData.put("orderNo", jsonData.get("orderNo").getAsString());
		}
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}

	/**
	 * @Title: createOrder
	 * @Description: 撤销订单
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月23日 下午2:18:41
	 */
	public Responses cancelOrder(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = getStringParam("orderNo", "订单编号");
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
        if (bugetFlag == 0) {
            rentPayService.cancelOrder(userId, orderNo);
        } else {
            rentPayService.cancelLiteOrder(userId, orderNo);
        }
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody("撤销账单成功！");
		return responses;
	}

	private List<PayChannelVo> getchannellist(JsonArray channels,long userId,String userName){
		List<PayChannelVo> payChannelVoList = new ArrayList<>();
		Iterator<JsonElement> chit = channels.iterator();
		while (chit.hasNext()) {
			JsonObject channelObje = chit.next().getAsJsonObject();
			String str=channelObje.get("paymentChannel").getAsString();
			int channelStyle=channelObje.get("channelStyle").getAsInt();
			int channelRate=channelObje.get("channelRate").getAsInt();
			String platform=this.getPlatform();
			if(Constants.platform.ANDROID.equals(platform) ||Constants.platform.IOS.equals(platform) ) {
				if("alipayApp".equals(str)){
					PayChannelVo alipayPayChannelVo = new PayChannelVo();
					alipayPayChannelVo.setType("alipayApp");
					if(channelStyle==2) {
						alipayPayChannelVo.setDesc("付费支付渠道，收取支付金额" + ((channelRate * 1.0) / 100.00) + "%手续费");
					}else{
						alipayPayChannelVo.setDesc("");
					}
					//update by arison
					alipayPayChannelVo.setPhone("");
					alipayPayChannelVo.setRealName(userName);
					alipayPayChannelVo.setDesc("");
					alipayPayChannelVo.setName("支付宝支付");
					alipayPayChannelVo.setScheme("wx8c50e3bf6eb30b60");
					payChannelVoList.add(alipayPayChannelVo);
				}else if("wechatApp".equals(str)){
					PayChannelVo wxPayChannelVo = new PayChannelVo();
					wxPayChannelVo.setName("微信支付");
					wxPayChannelVo.setType("wechatApp");
					if(channelStyle==2) {
						wxPayChannelVo.setDesc("付费支付渠道，收取支付金额"+((channelRate*1.0)/100.00)+"%手续费");
					}else{
						wxPayChannelVo.setDesc("");
					}
					//update by arison
					wxPayChannelVo.setPhone("");
					wxPayChannelVo.setRealName(userName);
					wxPayChannelVo.setScheme("HuizhaofangAPP");
					payChannelVoList.add(wxPayChannelVo);
				}
			}else{
				if("wechatPublic".equals(str)){
					PayChannelVo wxPayChannelVo = new PayChannelVo();
					wxPayChannelVo.setName("微信支付");
					wxPayChannelVo.setType("wechatPublic");
					if(channelStyle==2) {
						wxPayChannelVo.setDesc("付费支付渠道，收取支付金额" + ((channelRate * 1.0) / 100.00) + "%手续费");
					}else{
						wxPayChannelVo.setDesc("");
					}
					//update by arison
					wxPayChannelVo.setPhone("");
					wxPayChannelVo.setRealName(userName);
					payChannelVoList.add(wxPayChannelVo);
				}else if("alipayWap".equals(str)){
					PayChannelVo alipayPayChannelVo = new PayChannelVo();
					alipayPayChannelVo.setType("alipayWap");
					if(channelStyle==2) {
						alipayPayChannelVo.setDesc("付费支付渠道，收取支付金额" + ((channelRate * 1.0) / 100.00) + "%手续费");
					}else{
						alipayPayChannelVo.setDesc("");
					}
					//update by arison
					alipayPayChannelVo.setPhone("");
					alipayPayChannelVo.setRealName(userName);
					alipayPayChannelVo.setName("支付宝支付");
					payChannelVoList.add(alipayPayChannelVo);
				}
			}

			if("bankCard".equals(str)){
					//处理银行卡业务
					JsonObject serviceRetJo = paymentNewService.listBankcard(userId);
					//int carNo = serviceRetJo.get("card_no").getAsInt();
					JsonArray cardList = null;
					try {
						cardList = serviceRetJo.getAsJsonArray("cardList");
					} catch (Exception e) {
						logger.error("failed to parse card_list");
					}

					if (cardList != null) {
						JsonArray newArray = new JsonArray();
						Iterator<JsonElement> iterator = cardList.iterator();
						while (iterator.hasNext()) {
							JsonElement next = iterator.next();
							if (next instanceof JsonObject) {
								JsonObject bankcardObj = (JsonObject) next;
								PayChannelVo payChannelVo=new PayChannelVo();
								String cardName=bankcardObj.get("bankCardName").getAsString();
								if(cardName!=null && cardName.indexOf("行")!=-1){
									String bankName=cardName.substring(0,cardName.indexOf("行")+1);
									payChannelVo.setName(bankName+"("+bankcardObj.get("cardLast").getAsString()+")");
									payChannelVo.setBankName(bankName);
								}
								if(channelStyle==2) {
									payChannelVo.setDesc("付费支付渠道，收取支付金额"+((channelRate*1.0)/100.00)+"%手续费");
								}else{
									payChannelVo.setDesc("");
								}
								payChannelVo.setType("bankCard");
								payChannelVo.setCardTop(bankcardObj.get("cardTop").getAsString());
								payChannelVo.setCardLast(bankcardObj.get("cardLast").getAsString());
								payChannelVo.setBankCode(bankcardObj.get("bankCode").getAsString());
								payChannelVo.setPhone(bankcardObj.get("phone").getAsString());
								payChannelVo.setRealName(bankcardObj.get("realName").getAsString());
								payChannelVo.setBankToken(bankcardObj.get("bankToken").getAsString());
								payChannelVo.setIdNo(bankcardObj.get("idNo").getAsString());
								payChannelVoList.add(0,payChannelVo);
							}
						}
					}
				}
		}
		return payChannelVoList;
	}
	
	/**
	 * @Title: reqPaymentchannellist
	 * @Description: 查询支付渠道列表
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月24日 下午3:10:51
	 */
	public Responses reqPaymentchannellist(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = getDefaultStringParam("orderNo", "");
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
		JsonArray channels = null;
		String userName = "";
		List<PayChannelVo> payChannelVoList = new ArrayList<>();
		JsonObject jsonData = null;
		if (bugetFlag == 0) {
		    jsonData = rentPayService.reqPaymentchannellist(userId, orderNo);
		    JsonObject userNameJsonData = rentPayService.getUserNameByOrderNo(userId, orderNo);
		    userName = userNameJsonData.get("name").getAsString();
		} else {
		    jsonData = rentPayService.reqLitePaymentchannellist();
		}
		try {
		    channels = jsonData.getAsJsonArray("paymentChannels");
		} catch (Exception e) {
		    logger.error("failed to parse card_list");
		}
		logger.debug("user name is: " + userName);
		if (channels != null && !channels.isJsonNull()) {
			payChannelVoList = getchannellist(channels, userId, userName);
		}

		JsonObject retJo = new JsonObject();
		retJo.add("channels", GsonUtil.buildGson().toJsonTree(payChannelVoList));
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * @Title: reqChannelFee
	 * @Description: 查询渠道费
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月24日 下午3:33:36
	 */
	public Responses reqChannelFee(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = getStringParam("orderNo", "订单编号");
		String paymentChannel = getStringParam("paymentChannel", "支付渠道");
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
		int bugetFlag = getDefaultIntParam("bugetFlag", 0);
		JSONObject retData = new JSONObject();
		JsonObject jsonData = null;
		if (bugetFlag == 0) {
		    jsonData = rentPayService.reqChannelFee(userId, orderNo, paymentChannel);
		} else {
		    jsonData = rentPayService.reqLiteChannelFee(userId, orderNo, paymentChannel);
		}
		if (!jsonData.isJsonNull()) {
			retData.put("payChannel", jsonData.get("paymentChannel").getAsString());
			retData.put("payPrice", jsonData.get("price").getAsInt());
			retData.put("channelStyle", jsonData.get("channelStyle").getAsInt());
			int channelStyle = jsonData.get("channelStyle").getAsInt();
			retData.put("channelRate", jsonData.get("channelRate").getAsInt() / 100.00 + "%");
			if (channelStyle == 2) {
				retData.put("paymentChannelIncome", jsonData.get("paymentChannelIncome").getAsInt());
				retData.put("amount", jsonData.get("totalPrice").getAsInt());
			} else {
				retData.put("paymentChannelIncome", 0);
				retData.put("amount", jsonData.get("totalPrice").getAsInt() - jsonData.get("paymentChannelIncome").getAsInt());
			}
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retData);
		return responses;
	}
	
	/**
	 * @Title: charge
	 * @Description: 发起支付
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月25日 上午11:42:06
	 */
	public Responses charge(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = getStringParam("orderNo", "订单编号");
		String paymentChannel = getStringParam("paymentChannel", "支付渠道");
		String bankToken = getDefaultStringParam("bankToken", "");
		int totalPrice = getIntParam("totalPrice", "支付总金额");
		String clientIp = request.getHeader("X-Real-IP");
		String userAgent = request.getHeader("User-Agent");
		String phone = getDefaultStringParam("phone", "");
		String openId = getDefaultStringParam("openId", "");
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
		JsonObject jsonData = null;
        if (bugetFlag == 0) {
            jsonData = rentPayService.charge(userId, orderNo, paymentChannel, bankToken, totalPrice, clientIp, userAgent, phone, openId);
        } else {
            jsonData = rentPayService.chargeLite(userId, orderNo, paymentChannel, bankToken, totalPrice, clientIp, userAgent, phone, openId);
        }
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(jsonData);
		return responses;
	}
	
	/**
     * @Title: getCaptcha
     * @Description: 发送验证码
     * @return Responses
     * @author Arison
     * @dateTime 2017年11月25日 上午11:42:26
     */
    public Responses getCaptcha(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        String orderNo = getStringParam("orderNo", "订单编号");
        String busiOrderNo = getStringParam("busiOrderNo", "业务订单号");
        String phone = getStringParam("phone", "短信手机号");
        String platform = getPlatform();
        String operationTerminal = Constants.operateTerminal.WEB;
        if(platform.equals(Constants.platform.ANDROID)){
            operationTerminal = Constants.operateTerminal.ANDROID;
        }else if (platform.equals(Constants.platform.IOS)){
            operationTerminal = Constants.operateTerminal.IOS;
        }
        String clientIp = request.getHeader("X-Real-IP");
        String referer = request.getHeader("Referer");
        String userAgent = request.getHeader("User-Agent");
        String bankToken = getStringParam("bankToken", "银行卡标识");
        // 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
        JsonObject jsonData = null;
        if (bugetFlag == 0) {
            jsonData = rentPayService.getCaptcha(userId, orderNo, busiOrderNo, phone, clientIp, userAgent, referer, operationTerminal, bankToken);
        } else {
            jsonData = rentPayService.getLiteCaptcha(userId, orderNo, busiOrderNo, phone, clientIp, userAgent, referer, operationTerminal, bankToken);
        }

        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(jsonData);
        return responses;
    }
	
	/**
	 * @Title: confirmCharge
	 * @Description: 确认支付
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月25日 上午11:42:26
	 */
	public Responses confirmCharge(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = getStringParam("orderNo", "订单编号");
		String busiOrderNo = getStringParam("busiOrderNo", "业务订单号");
		String phone = getStringParam("phone", "短信手机号");
		String captcha = getStringParam("captcha", "验证码");
		// 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
        if (bugetFlag == 0) {
            rentPayService.confirmCharge(userId, orderNo, busiOrderNo, phone, captcha);
        } else {
            rentPayService.confirmChargeLite(userId, orderNo, busiOrderNo, phone, captcha);
        }
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody("短信验证码验证通过！");
		return responses;
	}

	/**
     * @Title: reqPaymentStatus
     * @Description: 查询支付状态
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年11月25日 上午11:41:19
     */
    public Responses reqPaymentStatus(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        String orderNo = getStringParam("orderNo", "订单编号");
        // 标准版和简版合同账单标识：0 （默认）标准版；1 轻量版
        int bugetFlag = getDefaultIntParam("bugetFlag", 0);
        JSONObject retData = new JSONObject();
        JsonObject jsonData = null;
        if (bugetFlag == 0) {
            jsonData = rentPayService.reqPaymentStatus(userId, orderNo);
        } else {
            jsonData = rentPayService.reqLitePaymentStatus(userId, orderNo);
        }
        if (!jsonData.isJsonNull()) {
            int payStatus = jsonData.get("status").getAsInt();
            if (payStatus == 1 || payStatus == 2) {
                retData.put("status", Constants.paymentStatus.PAYING_STATUS);
            } else if (payStatus == 3) {
                retData.put("status", Constants.paymentStatus.SUCCESS_PAID_STATUS);
            } else if (payStatus == 4 || payStatus == 6) {
                retData.put("status", Constants.paymentStatus.FAIL_PAID_STATUS);
                retData.put("statusDesc", jsonData.get("message").isJsonNull()?"":jsonData.get("message").getAsString());
            }
        }
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }
	
	/**
	 * @Title: comfirmBuget
	 * @Description: 轻量版-租客确认账单
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午2:22:43
	 */
	public Responses comfirmBuget(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        String bugetBaseNo = getStringParam("bugetBaseNo", "组账单编号");
        JsonObject retData = rentPayService.comfirmBuget(userId, bugetBaseNo);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }
	
	/**
	 * @Title: checkCancelRent
	 * @Description: 轻量版-检验租客能否退租
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月27日 上午11:23:34
	 */
	public Responses checkCancelRent(HttpServletRequest request) throws Exception {
	    long userId = sessionManager.getUserIdFromSession();
        String agencyId = getStringParam("agencyId", "中介公司ID");
        String signerPhone = getStringParam("signerPhone", "租客手机号吗");
        // 判断该用户是否绑过卡，如果绑过卡 表示用户已实名，入参增加用户姓名和身份证号，用来过滤账单数据
        String userName = "";
        String idNo = "";
        JsonObject paramRetJo = rentPayService.getIdNoAndUserName(userId);
        if (!paramRetJo.isJsonNull() && paramRetJo.has("userName") && paramRetJo.has("idNo")) {
            userName = paramRetJo.get("userName").getAsString();
            idNo = paramRetJo.get("idNo").getAsString();
        }
        JsonObject retData = rentPayService.checkCancelRent(userId, agencyId, signerPhone, userName, idNo);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }
	
	/**
	 * @Title: cancelRent
	 * @Description: 轻量版-租客退租
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午4:48:24
	 */
	public Responses cancelRent(HttpServletRequest request) throws Exception {
	    long userId = sessionManager.getUserIdFromSession();
	    String agencyId = getStringParam("agencyId", "中介公司ID");
        JsonObject retData = rentPayService.cancelRent(userId, agencyId);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }
	
	/**
	 * @Title: setAutoPayRent
	 * @Description: 轻量版-设置自动交租
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年12月25日 下午4:55:34
	 */
	public Responses setAutoPayRent(HttpServletRequest request) throws Exception {
	    long userId = sessionManager.getUserIdFromSession();
        String agencyId = getStringParam("agencyId", "中介公司ID");
        int isWithhold = getIntParam("isWithhold", "自动交租标识");
        String bankToken = getDefaultStringParam("bankToken", "");
        JsonObject retData = rentPayService.setAutoPayRent(userId, agencyId, isWithhold, bankToken);
        
        Responses responses = new Responses();
        responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
        responses.setBody(retData);
        return responses;
    }
	
}
