package com.huifenqi.hzf_platform.handler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.activity.dao.VoucherRepository;
import com.huifenqi.activity.domain.Voucher;
import com.huifenqi.hzf_platform.comm.*;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.*;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.service.*;
import com.huifenqi.hzf_platform.utils.*;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.hzf_platform.vo.PayChannelVo;
import com.huifenqi.usercomm.charge.dao.ChargeRepository;
import com.huifenqi.usercomm.charge.domain.BailFlow;
import com.huifenqi.usercomm.charge.domain.BankCard;
import com.huifenqi.usercomm.charge.domain.Charge;
import com.huifenqi.usercomm.dao.AgencyConfRepository;
import com.huifenqi.usercomm.dao.UserInfoRepository;
import com.huifenqi.usercomm.dao.UserRepository;
import com.huifenqi.usercomm.charge.dao.BailFlowRepository;
import com.huifenqi.usercomm.charge.dao.BankCardRepository;
import com.huifenqi.usercomm.dao.contract.*;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.UserInfo;
import com.huifenqi.usercomm.domain.contract.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by arison on 2015/9/5.
 * <p>
 * 支付请求处理
 *
 */
@Service
public class RepayRequestHandler extends BaseRequestHandler {

	@Autowired
	private ContractSnapshotRepository contractSnapshotRepository;

	@Autowired
	private SubpayRepository subpayRepository;

	@Autowired
	private VoucherActivityService voucherActivityService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PayStatusRepository payStatusRepository;

	@Autowired
	private GlobalConf globalConf;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private InstallmentContractRepository installmentContractRepository;

	@Autowired
	private InstallmentApplyRepository installmentApplyRepository;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private CrmService crmService;

	@Autowired
	private RcUtil rcUtil;

	@Autowired
	private RcService rcService;

	@Autowired
	private BankCardRepository bankCardRepository;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private ContractService contractService;

	@Autowired
	private ContractExtendRepository contractExtendRepository;

	@Autowired
	private ContractOrderRepository contractOrderRepository;

	@Autowired
	private ContractDepositRepository contractDepositRepository;

	@Autowired
	private AgencyRefundRepository agencyRefundRepository;
	@Autowired
	private ChargeRepository chargeRepository;

	@Autowired
	private BailFlowRepository bailFlowRepository;

	@Autowired
	private AgencyConfRepository agencyConfRepository;

	@Autowired
	private AccountSourceRepository accountSourceRepository;

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private LockManager lockManager;

	@Autowired
	private Configuration configuration;

	public static final String PAY_CHANNEL_CHECK = "pay-channel-check";
	public static final String UNBIND_LOCK = "unbind-lock";
	public static final String DUPLE_CHARGE = "duple-charge";
	public static final String CARD_NO = "card-no";
	/**
	 * 根据用户phone查询出用户所有快照合同
	 *
	 * @param paySummarizeList
	 * @param user
	 * @param userStatus
	 * @param allpaidSummarizeList
	 */
	private List<ContractSnapshot> addContractSnapshotList(List<PaySummarize> paySummarizeList,
														   User user, int userStatus, List<PaySummarize> allpaidSummarizeList) {

		List<ContractSnapshot> contractList = contractService.findContractsSnapshotsForList(user);

		if (CollectionUtils.isNotEmpty(contractList)) {
			for (ContractSnapshot c : contractList) {
				PaySummarize paySummarize = new PaySummarize();
				paySummarize.contractId = c.getSnapshotId();
				paySummarize.contractNo = c.getContractNo();
				paySummarize.userPayType = c.getUserPayType();
				paySummarize.address = c.getAddress();
				paySummarize.installmentRate = c.getInstallmentRate();
				paySummarize.leaseBegin = DateUtil.formatDate(c.getLeaseBegin());
				paySummarize.leaseEnd = DateUtil.formatDate(c.getLeaseEnd());

				// 查询每个订单包含的子订单总数
				long subpayCount = subpayRepository.countByContractSnapshotId(c.getSnapshotId());
				paySummarize.subpayCount = subpayCount;

				// 获取所有子订单状态，从而得出大订单的状态
				List<Subpay> totalSubpayList = subpayRepository.findSubpayOrderByIndex(c.getSnapshotId());
				// ZXZ 没有子订单，认为无效
				if (CollectionUtils.isEmpty(totalSubpayList)) {
					continue;
				}

				int payStatus = 1;
				for (Subpay subpay : totalSubpayList) {
					if (Subpay.PAYSTATE_PAY_ALL != subpay.getPayState() && Subpay.PAYSTATE_PAY_SUBLEASE_PAY_ALL != subpay.getPayState()) {
						payStatus = 0;
						break;
					}
				}

				// 查询当前需要支付的子订单
				List<Subpay> subpayList = subpayRepository.findFirstUncompleteSubpay(c.getSnapshotId(), 1);
				Subpay subpay = totalSubpayList.get(0);
				if (CollectionUtils.isNotEmpty(subpayList)) {
					subpay = subpayList.get(0);
				}

				paySummarize.index = subpay.getIndex();
				paySummarize.basePrice = subpay.getBasePrice();
				paySummarize.serviceFee = subpay.getServiceFee();
				paySummarize.price = subpay.getPrice();
				paySummarize.unpayPrice = subpay.getUnpayPrice();
				paySummarize.startDate = DateUtil.formatDate(subpay.getStartDate());
				paySummarize.endDate = DateUtil.formatDate(subpay.getEndDate());
				paySummarize.subpayId = subpay.getSubpayId();
				paySummarize.payee = "会找房（北京）网络技术有限公司";
				paySummarize.payId = subpay.getContractSnapshotId(); // pay表废弃,payId用于传递contractSnapshotId
				paySummarize.payStatus = payStatus;
				paySummarize.prepayDate = DateUtil.formatDate(subpay.getPayDate());
				paySummarize.isPrePay = 0;
				if (subpay.getPayDate() != null) {
					long diff = subpay.getPayDate().getTime() - DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime();
					if (diff > 0) {
						paySummarize.isPrePay = 1;
					}
				}
				// 滞纳金
				paySummarize.lateFees = subpay.getActualOverDue();

				// 用户状态
				paySummarize.userStatus = userStatus;

				// 合同状态信息 zxz于2016/1/15修改


				if (c.getContractStatus() == ContractSnapshot.STATUS_TERMINATED
						|| c.getContractStatus() == ContractSnapshot.STATUS_RENEGE
						|| c.getContractStatus() == ContractSnapshot.STATUS_SUBLET) { // 已结束
					paySummarize.contractStatus = Constants.UserConstractStatus.APPLY_FINISH;
				} else { // 其他状态
					paySummarize.contractStatus = Constants.UserConstractStatus.APPLY_VERIFY_SUCCESS;
				}

				//如果最后一期已还清，状态也是已结束
				if (payStatus == 1) {
					paySummarize.contractStatus = Constants.UserConstractStatus.APPLY_FINISH;
				}
				paySummarize.contractDes = Constants.UserConstractStatus
						.getContractDesByStatus(paySummarize.contractStatus);

				paySummarize.statusTime = c.getUpdateTime();


				JsonObject retJo = new JsonObject();
				retJo = addContractDepositInfo(retJo, paySummarize.contractNo);
				paySummarize.depositExist = retJo.get("depositExist").getAsInt();
				paySummarize.depositPrice = retJo.get("depositPrice").getAsInt();
				paySummarize.depositStatus = retJo.get("depositStatus").getAsInt();
				paySummarize.depositId = retJo.get("depositId").getAsLong();

				List<AgencyRefund> issues = agencyRefundRepository.findByContractNoAndStateOrderByCreateTimeDesc(c.getContractNo(), 1);
				logger.debug(" find agency refund issues : " + issues);
				//add by arison 包装已退租状态
				if (CollectionUtils.isNotEmpty(issues)) {
					AgencyRefund agencyRefund = issues.get(0);
					if (agencyRefund.getRefundStatus() == 3) {
						paySummarize.contractStatus = Constants.UserConstractStatus.APPLY_FINISH;
					}
				}

				paySummarize.renegeStatus = CollectionUtils.isNotEmpty(issues) ? 1 : 0;
				paySummarize.signDate = DateUtil.formatDate(c.getSignTime());

				AgencyConf conf = agencyConfRepository.findByAgencyid(c.getAgencyId());

				if (c.getContractStatus() == ContractSnapshot.STATUS_SUBLET) {
					paySummarize.isSubleted = 1;
				}
				boolean isShow=true;
				ContractExtend contractExtend = contractExtendRepository.findContractExtendByContractNo(c.getContractNo());
				if(contractExtend!=null){
					if(contractExtend.getManualMark()==1){
						isShow=false;
					}
				}
				if (contractExtend != null && contractExtend.getSublet() == 1) {
					paySummarize.isLaunched = 1;
				}

				//add by arison 20170914
				switch(paySummarize.contractStatus) {
					case Constants.UserConstractStatus.APPLY_ACCEPTING:
						paySummarize.showText="受理中";
						break;
					case Constants.UserConstractStatus.APPLY_VERIFIYING:
						paySummarize.showText="审核中";
						break;
					case Constants.UserConstractStatus.APPLY_VERIFY_SUCCESS:
						if (paySummarize.payStatus == 0) {
							paySummarize.showText=(paySummarize.index-1)+"/"+paySummarize.subpayCount;
							paySummarize.progress=100*(paySummarize.index-1)/paySummarize.subpayCount;
						} else {
							paySummarize.showText="已结束";
						}
						break;
					case Constants.UserConstractStatus.APPLY_VERIFY_REFUSED:
						paySummarize.showText="已拒绝";
						break;
					case Constants.UserConstractStatus.APPLY_SUBLET:
						paySummarize.showText="已转租";
						break;
					case Constants.UserConstractStatus.APPLY_FINISH:
						paySummarize.showText="已结束";
						break;
				}
				//*******************
				if (paySummarize.contractStatus == Constants.UserConstractStatus.APPLY_FINISH) {
					if(isShow) {
						allpaidSummarizeList.add(paySummarize);
					}
				} else {
					if (conf != null) {
						if (conf.getSublet() == 1) {
							InstallmentContract ic = installmentContractRepository.findByContractNo(c.getContractNo());
							if (ic != null && ic.getSource() != null && ic.getSource() == 1) {
								paySummarize.subletStatus = 1;
							}
						}
					}
					if(isShow) {
						paySummarizeList.add(paySummarize);
					}
				}
			}
		}
		return contractList;
	}

	/**
	 * 根据用户phone查询出用户所有未生成快照的合同
	 *
	 * @param paySummarizeList
	 * @param installmentContractList
	 * @param user
	 * @param contractList
	 * @param userStatus
	 */
	private void addInstallmentContractList(List<PaySummarize> paySummarizeList,
											List<InstallmentContract> installmentContractList, User user, int userStatus,
											List<ContractSnapshot> contractList) {
		List<PaySummarize> summarizeList = new ArrayList<>();
		installmentContractList = contractService.findInstallmentContractsByFundType(user, 0);
		//最后一期，代表已拒绝的合同
		PaySummarize last = null;
		// APP 123版本以上才需要返回所有的合同
		if ( CollectionUtils.isNotEmpty(installmentContractList)) {
			for (InstallmentContract ic : installmentContractList) {
				// 过滤掉在快照中存在的合同
				boolean flag = false;
				for (ContractSnapshot cs : contractList) {
					if (cs.getContractNo().equals(ic.getContractNo())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					continue;
				}
				PaySummarize paySummarize = new PaySummarize();
				paySummarize.contractId = ic.getContractSnapshotId();
				paySummarize.contractNo = ic.getContractNo();
				paySummarize.userPayType = ic.getUserPayType();
				paySummarize.address = ic.getAddress();
				paySummarize.basePrice = ic.getMonthlyAmount();
				paySummarize.payee = "会找房（北京）网络技术有限公司";
				paySummarize.leaseBegin = DateUtil.formatDate(ic.getLeaseBegin());
				paySummarize.leaseEnd = DateUtil.formatDate(ic.getLeaseEnd());

				List<subpay> subpays = null;
				try {
					ContractOrder contractOrder = contractOrderRepository
							.findContractOrderByContractNo(ic.getContractNo());
					String subPayListStr = contractOrder == null ? null : contractOrder.getSubpayList();
					subpays = gson.fromJson(subPayListStr, new TypeToken<List<subpay>>() {
					}.getType());
				} catch (Exception e) {
					logger.error("Contract No.=" + ic.getContractNo() + ":" + e.toString());
					continue;
				}
				if (CollectionUtils.isEmpty(subpays)) {
					continue;
				}

				paySummarize.subpayCount = subpays.size();

				Collections.sort(subpays, new Comparator<subpay>() {

					@Override
					public int compare(subpay o1, subpay o2) {
						return o1.index - o2.index;
					}
				});

				subpay sp = subpays.get(0);
				paySummarize.index = sp.index;
				paySummarize.serviceFee = sp.serviceFee;
				paySummarize.price = sp.price;
				paySummarize.unpayPrice = sp.price;
				paySummarize.startDate = sp.startDate;
				paySummarize.endDate = sp.endDate;
				paySummarize.prepayDate = sp.payDate;

				// 用户状态
				paySummarize.userStatus = userStatus;

				// 合同状态信息 zxz于2016/1/15修改

				paySummarize.contractStatus = Constants.UserConstractStatus
						.changeBossStatusToUserStatus(ic.getContractStatus());
				paySummarize.contractDes = Constants.UserConstractStatus
						.getContractDesByStatus(paySummarize.contractStatus);
				paySummarize.statusTime = ic.getUpdateTime();

				JsonObject retJo = new JsonObject();
				retJo = addContractDepositInfo(retJo, paySummarize.contractNo);
				paySummarize.depositExist = retJo.get("depositExist").getAsInt();
				paySummarize.depositPrice = retJo.get("depositPrice").getAsInt();
				paySummarize.depositStatus = retJo.get("depositStatus").getAsInt();
				paySummarize.depositId = retJo.get("depositId").getAsLong();

				paySummarize.renegeStatus = 0;
				paySummarize.signDate = DateUtil.formatDate(ic.getSignTime());

				boolean isShow=true;
				ContractExtend contractExtend = contractExtendRepository.findContractExtendByContractNo(ic.getContractNo());
				if(contractExtend!=null){
					if(contractExtend.getManualMark()==1){
						isShow=false;
					}
				}

				//add by arison
				switch(paySummarize.contractStatus) {
					case Constants.UserConstractStatus.APPLY_ACCEPTING:
						paySummarize.showText="受理中";
						break;
					case Constants.UserConstractStatus.APPLY_VERIFIYING:
						paySummarize.showText="审核中";
						break;
					case Constants.UserConstractStatus.APPLY_VERIFY_SUCCESS:
						if (paySummarize.payStatus == 0) {
							paySummarize.showText=(paySummarize.index-1)+"/"+paySummarize.subpayCount;
							paySummarize.progress=100*(paySummarize.index-1)/paySummarize.subpayCount;
						} else {
							paySummarize.showText="已结束";
						}
						break;
					case Constants.UserConstractStatus.APPLY_VERIFY_REFUSED:
						paySummarize.showText="已拒绝";
						break;
					case Constants.UserConstractStatus.APPLY_SUBLET:
						paySummarize.showText="已转租";
						break;
					case Constants.UserConstractStatus.APPLY_FINISH:
						paySummarize.showText="已结束";
						break;
				}
				//**********************
				if (paySummarize.contractStatus == Constants.UserConstractStatus.APPLY_VERIFY_REFUSED) {
					if(isShow) {
						last = paySummarize;
					}
					continue;
				}
				if(isShow) {
					summarizeList.add(paySummarize);
				}
			}


			Collections.sort(summarizeList, new PaySummarizeConparator());
			paySummarizeList.addAll(summarizeList);
			if (last != null) {
				paySummarizeList.add(last);
			}
		}
	}

	/**
	 * 获取订单列表以及本期房租
	 *
	 * @return
	 */
	public Responses listPay() {
		long userId = sessionManager.getUserIdFromSession();
		//User user = userRepository.findOne(userId);
		User user = userRepository.findByUseridAndState(userId,1);
		if (null == user) {
			throw new NoSuchUserException(userId);
		}
		int pageNo = getDefaultIntParam("pageNo",2);
		logger.info(String.format("Find the user(%s)'s contracts", user.getPhone()));
		List<ContractSnapshot> contractList = new ArrayList<>();
		List<InstallmentContract> installmentContractList = new ArrayList<>();
		List<PaySummarize> paySummarizeList = new ArrayList<>();
		List<PaySummarize> allpaidSummarizeList = new ArrayList<>();

		// 用户信息是否已经完善
		int userStatus = Constants.User.USER_INFO_UNEXIST;
		if (isUserInfo(userId)) {
			userStatus = Constants.User.USER_INFO_EXIST;
		}
		//int appVersion = getClientVersion();
		try {
			// 根据合同查询出用户所有的订单
			contractList = addContractSnapshotList(paySummarizeList, user, userStatus,
					allpaidSummarizeList);
			// 对用户的订单做一个排序，规则：还款日从近到远排序
			Collections.sort(paySummarizeList, new PaySummarizeConparator());
			Collections.sort(allpaidSummarizeList, new PaySummarizeConparator());

			// 合同顺序为：还款中＞已结束＞审核中＞已拒绝
			//首页不展示已结束的合同
			if (pageNo == 2) {
				paySummarizeList.addAll(allpaidSummarizeList);
				// 查询未生成快照的合同
				addInstallmentContractList(paySummarizeList, installmentContractList, user, userStatus,
						contractList);
			}


		} catch (BaseException e) {
			throw new BaseException(e.getErrorcode(),e.getDescription());
		}

		JsonObject retJo = new JsonObject();
		retJo.add("pays", gson.toJsonTree(paySummarizeList));
		retJo.addProperty("userStatus", userStatus);
		//查询用户申请过的合同
		List<InstallmentContract> appliedContractsList =contractService.findAppliedContracts(user);
		List<InstallmentApply> installmentApplyList = installmentApplyRepository
				.findByUserIdAndStateOrderByCreateTimeDesc(user.getUserid(), 1);

		if(CollectionUtils.isNotEmpty(installmentApplyList)||CollectionUtils.isNotEmpty(appliedContractsList)) {
			retJo.addProperty("isApplied", 1);
		}else{
			retJo.addProperty("isApplied", 0);
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 获取子订单列表,只能查到已审核通过的订单列表
	 *
	 * @return
	 */
	public Responses listSubpay() {
		long contractSnapshotId = getLongParam("contractId", "合同id");
		return listSubpayFromCs(contractSnapshotId);
	}

	/**
	 * 查询订单状态
	 *
	 * @return
	 */
	public Responses listPayStatus() {
		long uid = sessionManager.getUserIdFromSession();
		String contractNo = getStringParam("contractNo", "合同号");
		ApiResult result = null;
		JsonObject retJo = new JsonObject();
		JsonArray statusArray = new JsonArray();
		try {
			/*List<PayStatus> statusList = payStatusRepository.findPayStatusBy(uid, contractNo);
			if (statusList != null && !statusList.isEmpty()) {
				statusArray = gson.toJsonTree(statusList).getAsJsonArray();
			}*/
			//List<PayStatus> statusList = null;
			retJo.add("payStatus", statusArray);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw  new BaseException(ErrorMsgCode.ERRCODE_PAY_STATUS_QUERY_FAILED, "订单状态查询失败");
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 支付
	 *
	 * @return
	 */
	public Responses charge() throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String payId = getStringParam("subpayId", "订单id");
		String voucherId = getDefaultStringParam("voucherId", "0");
		String channel = getDefaultStringParam("payChannel", "unknown");
		String productType = getDefaultStringParam("productType", Constants.ProductType.RENT);


		//if (Constants.PayChannel.isWeixinPay(channel)) {
		//	return new ApiResult(ErrorMsgCode.ERRCODE_CHARGE_FAILED, "微信正在维护，请选择其它支付方式!");
		//}
		// 检测代金券
		boolean voucherOutDate = isVoucherOutDate(voucherId);
		if (voucherOutDate) {
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE, "代金劵已经过期了!");
		}

		Map<String, String> params = new HashMap<>();
		params.put("client_ip", NetUtil.getClientIP(getRequest().getHttpServletRequest()));
		params.put("user_agent", getRequest().getHttpServletRequest().getHeader("user-agent"));
		params.put("referer", getRequest().getHttpServletRequest().getHeader("referer"));

		int version = getClientVersion();
		String platform = getPlatform();
		//去掉兼容性测试代码
		long amount_ = getLongParam("amount", "用户实付总金额");
		long pay_price = getLongParam("payPrice", "用户实付金额");
		long payment_channel_income = getLongParam("paymentChannelIncome", "渠道手续费");
		long vouch_price=0;
		// 判断对应需要支付的费用是否正确 ？？
		//add by arison 2017-05-05
		//	String payId = getStringParam("subpay_id", "订单id");
		//update by arison
		if (!"0".equals(voucherId)){
			Voucher voucher = voucherRepository.findOne((Long.parseLong(voucherId)));
			if (voucher != null) {
				vouch_price = voucher.getPrice();
			}
		}

		logger.info(String.format("in charge ->switch-> case 1: amount =%s , pay_price=%s , payment_channel_income= %s , vouch_price=%s ",amount_,pay_price,payment_channel_income,vouch_price));
		if (!justAmount(payId,channel,amount_,pay_price,payment_channel_income,vouch_price)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "支付金额不正确,请重试!");
		}

		params.put("amount", amount_ + "");
		params.put("pay_price", pay_price + "");
		params.put("payment_channel_income", payment_channel_income + "");


		params.put("channel", channel);
		params.put("product_type", productType);
		if (Constants.ProductType.RENT.equals(productType)) {
			params.put("subpay_id", payId);
		} else {
			params.put("deposit_id", payId);
		}
		params.put("user_id", userId + "");
		params.put("vid", voucherId);
		params.put("pay_platform", Constants.platform.getPayPlatform(platform));

		// -----------------------------各渠道特殊处理(是否可用，填充参数)---------------------------------------------
		if (Constants.PayChannel.isUnionPay(channel)) { // 银联支付

			// zxz于2016/1/19修改 后台已经不再支持银联支付，需要提示老版本的APP重新升级
			String unionpaySwitch = globalConf.payment.get("unionpay.switch");
			if (StringUtil.isBlank(unionpaySwitch)
					|| Constants.Alipay.ALIPAY_SWITCH_OFF.equals(unionpaySwitch.trim())) {
				throw new BaseException(ErrorMsgCode.ERRCODE_UNSUPPORT_PARTYB, "不再支持银联支付方式,请升级到最新的APP版本!");
			}

			// 银联pc网页、银联手机网页支付需要传result_url
			if (Constants.PayChannel.UPACP_WAP.equals(channel) || Constants.PayChannel.UPACP_PC.equals(channel)) {
				String resultUrl = getStringParam("resultUrl");
				params.put("result_url", resultUrl);
			}
		} else if (Constants.PayChannel.isAliPay(channel)) { // 支付宝支付

			// 如果是支付宝系列支付，通过全局配置开关来配置是否支持
			String applySwitch = globalConf.payment.get("alipay.switch");
			if (StringUtil.isBlank(applySwitch) || Constants.Alipay.ALIPAY_SWITCH_OFF.equals(applySwitch.trim())) {
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR, "支付宝异常，请尝试其他付款方式");
			}

			// 支付宝pc网页、支付宝手机网页支付需要传success_url
			if (Constants.PayChannel.ALIPAY_WAP.equals(channel) || Constants.PayChannel.ALIPAY_PC.equals(channel)) {
				String successUrl = getStringParam("successUrl");
				String cancelUrl = getDefaultStringParam("cancelUrl", "");

				params.put("success_url", successUrl);
				params.put("cancel_url", cancelUrl);
			}
		} else if (Constants.PayChannel.isWeixinPay(channel)) { // 微信支付

			if (Constants.PayChannel.WX_PUB.equals(channel)) { // 微信公众号
				// 微信公众号，必须有openid
				String openid = CookieUtil.getValue(Request.getRequest().getHttpServletRequest(), "wx_openid");
				if (StringUtil.isEmpty(openid)) {
					throw new LackParameterException("缺少微信公众号openid");
				}

				params.put("wx_openid", openid);
			}
		} else if (Constants.PayChannel.isJdPay(channel)) { // 京东支付
			// 暂无特殊入参
		} else if (Constants.PayChannel.isLianLian(channel)) { //连连支付
			User user = userUtils.getUser(userId);
			if (user == null) {
				logger.error("没有该用户：" + sessionManager.getUserIdFromSession());
				throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
			}
			params.put("id_number", user.getUserIdNo());
		} else {  // 有以api形式调用的渠道
			String amount = params.get("amount");
			if (Long.parseLong(amount) > 0) {
				params.put("card_top", getStringParam("cardTop", "银行卡前6位"));
				params.put("card_last", getStringParam("cardLast", "银行卡后4位"));
			}
		}

		// 判断对应的渠道费是否正确
		//TODO ZXZ 2016-09-30
		if (Constants.PayChannel.isAliPay(channel) || Constants.PayChannel.isWeixinPay(channel)) {
			if (!justChargePrice(getLongParam("payPrice", "用户实付金额"), getLongParam("paymentChannelIncome", "渠道手续费"))) {
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "支付金额不正确,请重试!");
			}
		}

		// -------------------------------调用支付接口----------------------------------------------
		JsonObject resultJo = new JsonObject();
		//支付渠道存入到redis update by arison 20170524
		//logger.debug(String.format("put pay channel '%s' into redis ",channel));
		//redisClient.set("-"+getSessionId()+PAY_CHANNEL_CHECK, channel, 5*60 * 1000);
		if (Constants.PayChannel.isJdPay(channel)) { // 基于京东支付

			JsonObject serviceRetJo = paymentService.requestCharge(params);
			resultJo.add("jdOrderDetail", serviceRetJo.get("order_detail"));

			resultJo.addProperty("needInvokeJd", serviceRetJo.getAsJsonPrimitive("need_invoke_jd").getAsBoolean());

		} else if(Constants.PayChannel.isLianLian(channel)) {
			JsonObject serviceRetJo = paymentService.requestCharge(params);

			resultJo.add("lianlianOrderDetail", serviceRetJo.get("order_detail"));
			resultJo.addProperty("needInvokeLianlian", serviceRetJo.getAsJsonPrimitive("need_invoke_lian_lian").getAsBoolean());
		} else if (Constants.PayChannel.isUnionPay(channel) ||
				Constants.PayChannel.isAliPay(channel) ||
				Constants.PayChannel.isWeixinPay(channel)) { // 基于ping++的支付

			JsonObject serviceRetJo = null;
			serviceRetJo = paymentService.requestCharge(params);
			resultJo.add("charge", serviceRetJo.get("charge"));
			String orderid = serviceRetJo.getAsJsonPrimitive("order_no").getAsString();
			resultJo.addProperty("orderId", orderid);
			resultJo.addProperty("needInvokePingpp",
					serviceRetJo.getAsJsonPrimitive("need_invoke_pingpp").getAsBoolean());
			resultJo.addProperty("needInvokeYeepay", false);
		} else {  // 所有依赖于API的支付
			JsonObject serviceRetJo = null;
			serviceRetJo = paymentService.requestCharge(params);
			boolean needInvokeYeepay = serviceRetJo.getAsJsonPrimitive("need_invoke_yeepay").getAsBoolean();
			String orderId = serviceRetJo.getAsJsonPrimitive("order_no").getAsString();

			if (needInvokeYeepay) {
				params.put("order_no", orderId);
				params.put("phone", getUserPhone());
				paymentService.reqYeepayCaptcha(params);
			}

			resultJo.addProperty("orderId", orderId);
			resultJo.addProperty("needInvokeYeepay", needInvokeYeepay);
			resultJo.addProperty("needInvokePingpp", false);
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(resultJo);
		return responses;
	}

	//update by arison 20170505
	private boolean justAmount(String payId, String channel,long amount,long real_payprice, long payment_channel_income) {
		Subpay subpay=subpayRepository.findValidSubpay(Long.parseLong(payId));
		//支付宝和微信要检验手续费
		if(Constants.PayChannel.isWeixinPay(channel)||Constants.PayChannel.isAliPay(channel)){
			logger.info("支付宝和微信支付：subpay id : "+payId+ " channel : "+channel+ "  amount："+amount+"  real_payprice:"+real_payprice+"  payment_channel_income"+payment_channel_income+"  unpay_price: "+subpay.getUnpayPrice());
			if((real_payprice!=subpay.getUnpayPrice()) ||(subpay.getUnpayPrice()+payment_channel_income)!=amount){
				return false;
			}else{
				return true;
			}
		}else{
			logger.info("银行卡支付：subpay id : "+payId+ " channel : "+channel+ "  amount："+amount+"  real_payprice: "+real_payprice+"  payment_channel_income"+payment_channel_income+"  unpay_price: "+subpay.getUnpayPrice());
			//银行卡支付不应传检验手续费
			if(payment_channel_income!=0){
				logger.info("subpay id : "+payId+ " channel : "+channel+ " 支付渠道不应传值  payment_channel_income "+payment_channel_income);
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "支付金额不正确,请重试!");
			}
			if(subpay.getUnpayPrice()!=amount || subpay.getUnpayPrice()!=real_payprice){
				return false;
			}else {
				return true;
			}
		}
	}


	//update by arison 20170506 vouch
	private boolean justAmount(String payId, String channel,long amount,long real_payprice, long payment_channel_income,long vouch_price) {
		Subpay subpay=subpayRepository.findValidSubpay(Long.parseLong(payId));
		//支付宝和微信要检验手续费
		if(Constants.PayChannel.isWeixinPay(channel)||Constants.PayChannel.isAliPay(channel)){
			logger.info("支付宝和微信支付：subpay id : "+payId+ " channel : "+channel+ "  amount："+amount+"  real_payprice:"+real_payprice+"  payment_channel_income"+payment_channel_income+"  unpay_price: "+subpay.getUnpayPrice() +"  vouch_price: "+vouch_price);
			if(((real_payprice+vouch_price)!=subpay.getUnpayPrice()) ||(subpay.getUnpayPrice()+payment_channel_income-vouch_price)!=amount){
				if(amount==0 &&(vouch_price>=subpay.getUnpayPrice())){  //代金券面额大于待支付金额时
					return true;
				}
				return false;
			}else{
				return true;
			}
		}else{
			logger.info("银行卡支付：subpay id : "+payId+ " channel : "+channel+ "  amount："+amount+"  real_payprice: "+real_payprice+"  payment_channel_income"+payment_channel_income+"  unpay_price: "+subpay.getUnpayPrice()+"  vouch_price: "+vouch_price);
			//银行卡支付不应传检验手续费
			if(payment_channel_income!=0){
				logger.info("subpay id : "+payId+ " channel : "+channel+ " 支付渠道不应传值  payment_channel_income "+payment_channel_income);
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "支付金额不正确,请重试!");
			}
			if((subpay.getUnpayPrice()-vouch_price)!=amount || (real_payprice+vouch_price)!=subpay.getUnpayPrice()){
				//	if(vouch_price>=subpay.getUnpayPrice()){
				if(amount==0 &&(vouch_price>=subpay.getUnpayPrice())){//代金券面额大于待支付金额时
					return true;
				}
				return false;
			}else {
				return true;
			}
		}
	}

	private String getSubpayId(String orderId){
		String url = configuration.getPaymentServiceUrl() + "api/charge/order/detail/";
		Map<String, String> params = new HashMap<>();
		params.put("order_no", orderId);
		params.put("product_type", Constants.ProductType.RENT);
		params.put("pay_type",Constants.ProductType.DEPOSIT); //传1表示不付款不，借用保证金字段

		String postRet=null;
		try {
			postRet = HttpUtil.post(url, params);
		}catch (Exception e){
			throw new BaseException(e);
		}

		ApiResult bankcardList = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		String subPayId=null;
		if(bankcardList.result!=null) {
			JsonElement jsonList = bankcardList.result.get("charges");
			if(jsonList!=null) {
				JsonObject j= jsonList.getAsJsonObject();
				subPayId= j.get("subpay_id").getAsString();
				return subPayId;
			}
		}
		return subPayId;
	}
	/**
	 * 确认支付新版本
	 *
	 * @return
	 */
	public Responses confirmChargeV2() {
		//add by arison 20170704
		String subpayId = getStringParam("subpayId", "分期id");
	/*
	    if(!redisClient.setNx(DUPLE_CHARGE+"-"+subpayId,subpayId)){
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"本期还款支付进行中，请勿重复操作");
		};
		redisClient.expire(DUPLE_CHARGE,2*60*60*1000);  //过期时间两小时
    */



		logger.debug("发起支付的subpay id : ",subpayId);
		String orderid = null;
		String captcha = null;
		String productType=null;

		try{
			orderid = getStringParam("orderId", "订单id");
			captcha = getStringParam("captcha", "验证码");
			productType= getDefaultStringParam("productType", Constants.ProductType.RENT);
			if(StringUtil.isBlank(orderid) || StringUtil.isBlank(captcha)){
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "请先获取验证码");
			}
		}catch(Exception e){
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM, "请先获取验证码");
		}

		String channel = "yeepay";
		if (getClientVersion() >= 170 || getClientVersion() == 0) {
			//update by arison
			channel = getDefaultStringParam("pay_channel", "unknown");
		}


		int minYeepayCaptchaLength = configuration.minYeepayCaptchaLength;
		int maxYeepayCaptchaLength = configuration.maxYeepayCaptchaLength;
		if (!RulesVerifyUtil.verifyCsLength(captcha, minYeepayCaptchaLength, maxYeepayCaptchaLength)) {
			logger.error("验证码位数异常, captcha:" + captcha);
			redisClient.delete("-"+getSessionId()+PAY_CHANNEL_CHECK);
			throw  new BaseException(ErrorMsgCode.ERRCODE_CAPTCHA_INVALID, "验证码不正确");
		}

		//pay channel check ************ add by arison 21070517
		/*Object savedChannel=redisClient.get("-"+getSessionId()+PAY_CHANNEL_CHECK);
		if(savedChannel==null || !channel.equals(savedChannel.toString())){
			redisClient.delete("-"+getSessionId()+PAY_CHANNEL_CHECK);
			throw  new BaseException(ErrorMsgCode.DIFF_PAY_CHANNEL_ERROR, "支付渠道不一致，请重新支付");
		}*/

		//******************
		Map<String, String> params = new HashMap<>();
		params.put("order_no", orderid);
		params.put("captcha", captcha);
		params.put("phone", getUserPhone());
		params.put("channel", channel);
		params.put("product_type", productType);

		String subPayId=getSubpayId(orderid);
		Subpay subpay = subpayRepository.findValidSubpay(Long.parseLong(subPayId));
		AccountSource accountSource= accountSourceRepository.findBySourceCapital(subpay.getSourcesCapital());
		logger.debug(" the query sourcesCapital:　"+subpay.getSourcesCapital());
		String payChannelAccountSetSn=accountSource.getSn();
		params.put("pay_channel_account_set_sn", payChannelAccountSetSn);

		JsonObject serviceRetJo=null;

		final String lockResource=DUPLE_CHARGE+"-"+subpayId;
		boolean lock = lockManager.lock(lockResource,1000*60*2); //锁定两小时

		try {
			if (lock) {
				serviceRetJo = paymentService.confirmYeepayCharge(params);
			}else{
				logger.info(String.format("确认支付订单%s!", subpayId));
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"本期还款支付进行中，请勿重复操作");
			}
		}finally {
			if (lock) {
				lockManager.unLock(lockResource);
			}
		}

		//JsonObject serviceRespJo = gson.fromJson(serviceRetJo, JsonObject.class);

        /*JsonObject statusJo = serviceRetJo.getAsJsonObject("status");
        String code = statusJo.getAsJsonPrimitive("code").getAsString();
        if ("0".equals(code)) {
            /*String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
            throw new ServiceInvokeFailException(statusJo.getAsJsonPrimitive("code").getAsString() + "", errMsg);
            */
		//redisClient.delete("-"+getSessionId()+PAY_CHANNEL_CHECK);
         /* }else{
            logger.debug( " confirm charge error code "+code);
        }*/
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(serviceRetJo);
		return responses;

	}

	/**
	 * 支付成功回调
	 * <p>
	 * 调用该接口才能下发代金券
	 *
	 * @return
	 * @throws BaseException
	 */
	public Responses chargeSuccess() {
		long userId = sessionManager.getUserIdFromSession();

		Voucher voucher = voucherActivityService.activityRepay(userId);
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

		JsonObject retJo = new JsonObject();
		if (null == voucher) {
			retJo.add("voucher", new JsonObject());
		} else {
			retJo.add("voucher", gson.toJsonTree(voucher));
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 取消支付回调
	 * <p>
	 * 该接口在用户取消支付后调用，用于解绑代金券和支付，不会对代金券的状态有任何影响
	 *
	 * @return
	 * @throws Exception
	 */
	public ApiResult cancelCharge() {
		long voucherId = getDefaultLongParam("voucher_id", 0);

		if (voucherId > 0) {
			voucherActivityService.setVoucherNotUsed(voucherId);
		}

		return new ApiResult();
	}

	/**
	 * 查询用户绑定的银行卡信息
	 *
	 * @return
	 */
	public ApiResult listBankcard() {
		long userId = sessionManager.getUserIdFromSession();
		JsonObject serviceRetJo = paymentService.listBankcard(userId);
		int carNo=serviceRetJo.get("card_no").getAsInt();
		String key=userId+"-"+CARD_NO;
		redisClient.set(key,carNo);
		//update by arison
		serviceRetJo.remove("card_no");
		return new ApiResult(serviceRetJo);
	}

	/**
	 * 绑定银行卡
	 *
	 * @return
	 */
	public ApiResult bindBankcard() {
		String userName = getStringParam("user_name", "用户姓名");
		String phone = getStringParam("phone", "手机号");
		String idCardNo = getStringParam("id_card_no", "身份证号").toUpperCase();
		String bankcardNo = getStringParam("bank_card_no", "银行卡号");
		String payChannel = getDefaultStringParam("pay_channel", Constants.PayHandleChannel.HFQ);
		String signContractIdentity = getDefaultStringParam("sign_contract_identity", "0");
		String platform = getPlatform();

		if (!RulesVerifyUtil.verifyPhone(phone)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "手机号码格式不正确");
		}

		long userId = sessionManager.getUserIdFromSession();

		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：" + sessionManager.getUserIdFromSession());
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}

		// 如果用户存在合同，校验身份证号是否与最新一份合同相同
		List<ContractSnapshot> contractSnapshotList = contractService.findTakingEffectContractSnapshots(user);

		if (CollectionUtils.isNotEmpty(contractSnapshotList)) {
			// 取最新的合同进行比较
			ContractSnapshot contractSnapshot = contractSnapshotList.get(0);
			String userIdNo = contractSnapshot.getUserIdNo();
			if (!idCardNo.equals(userIdNo)) {
				logger.error("待绑定身份证号与合同预留身份证号不一致" + ", userIdNo:" + userIdNo);
				throw new BaseException(ErrorMsgCode.ERRCODE_USER_ID_NOT_MATCH, "待绑定身份证号与合同预留身份证号不一致");
			}
		}

		// TODO 去掉对验证码的需求
		// 校验图形验证码, 微信端必须加图形验证码
		// int appVersion = getClientVersion();
		// if (!(Constants.platform.isAppPlatform(platform) && appVersion <=
		// 124)) {
		// String imgCaptcha = getStringParam("img_captcha");
		// if (!imgCaptchaManager.verify(user.phone, imgCaptcha)) {
		// throw new InvalidImgCaptchaException("请输入正确的图形验证码!");
		// }
		// }

		//无纸化流程中的绑卡，只有状态是2的用户才可以提交绑卡请求，防止用户多次提交绑卡
		if ("1".equals(signContractIdentity)) {
			Integer cacheStatus = userUtils.getUserCacheStatus(userId);
			if (cacheStatus != null && cacheStatus.intValue() == 2) {
				paymentService.bindBankcard(platform, userId, userName, phone, idCardNo, bankcardNo, payChannel,
						signContractIdentity);
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR, "合同已确认或已绑卡");
			}
		}else {
			paymentService.bindBankcard(platform, userId, userName, phone, idCardNo, bankcardNo, payChannel,
					signContractIdentity);
		}

		return new ApiResult();
	}

	/**
	 * 确定绑卡
	 *
	 * @return
	 */
	public ApiResult confirmBindBankcard() {
		String phone = getStringParam("phone", "手机号");
		String captcha = getStringParam("captcha", "验证码");
		String payChannel = getDefaultStringParam("pay_channel", Constants.PayHandleChannel.HFQ);

		paymentService.confirmBindBandcard(phone, captcha, payChannel);

		return new ApiResult();
	}


	/**
	 * 获取校解绑银行卡的资源
	 * @param
	 * @return
	 */
	public static String getUnbindLockKey(String userId) {
		StringBuilder sb = new StringBuilder();
		sb.append(userId).append("-").append(UNBIND_LOCK);
		return sb.toString();
	}

	/**
	 * 解绑银行卡
	 *
	 * @return
	 */
	public ApiResult unbindBankcard() throws Exception {
		String bindid = getStringParam("bindid", "绑定Id");
		String payChannel = getDefaultStringParam("pay_channel", Constants.PayHandleChannel.HFQ);
		long userId = sessionManager.getUserIdFromSession();
		String lockResource=getUnbindLockKey(userId+"");
		JsonObject serviceRetJo=null;

		boolean lock = lockManager.lock(lockResource,10*1000);
		if (!lock) {
			logger.info(String.format("%s","解绑频繁，请稍后操作"));
			throw new BaseException(ErrorMsgCode.ERRCODE_PAY_BANKCARD_UNBAND_FAIL,"解绑频繁，请稍后操作");
		}
		try {
			if (lock) {
				//修改解绑银行卡的个数值
				String key=userId+"-"+CARD_NO;
				Object cards=redisClient.get(key);
				if(cards==null){
					throw  new BaseException(ErrorMsgCode.ERRCODE_PAY_BANKCARD_UNBAND_FAIL, "解绑银行卡失败，请稍后操作");
				}

				int cardNo=Integer.parseInt(cards.toString());
				logger.debug("user : "+userId+" card number: "+cardNo);
				if(cardNo<=1)
				{
					throw  new BaseException(ErrorMsgCode.ERRCODE_PAY_BANKCARD_UNBAND_FAIL, "银行卡无法解绑，因为至少要绑定一张银行卡");
				}
				serviceRetJo= paymentService.unbindBankcard(bindid, userId, payChannel);
				redisClient.set(key,--cardNo);
			}
		}catch(BaseException e){
			throw e;
		} finally {
			if (lock) {
				// 释放锁
				lockManager.unLock(lockResource);
			}
		}

		/*if(!redisClient.setNx(userId+"-"+UNBIND_CHECK,strId))
		{
			//redisClient.expire(userId+"-"+UNBIND_CHECK,10*1000);
			throw  new BaseException(ErrorMsgCode.NO_UNBIND_CARD_ERROR, "解绑频繁，请稍后操作");
		}
		redisClient.expire(userId+"-"+UNBIND_CHECK,10*1000);*/
		//同时解绑银行卡的问题
		/*Object obj=redisClient.get("-"+userId+UNBIND_CHECK);
		JsonObject serviceRetJo;
		if(obj==null || strId.equals(obj.toString())){
			//throw  new BaseException(ErrorMsgCode.DIFF_PAY_CHANNEL_ERROR, "支付渠道不一致，请重新支付");
			redisClient.set("-"+userId+"-"+UNBIND_CHECK, userId+"", 60 * 1000);
		*/

		//redisClient.getExpire("-"+userId+"-"+UNBIND_CHECK);
		//redisClient.delete(userId+"-"+UNBIND_CHECK);
		//redisClient.expire("-"+userId+"-"+UNBIND_CHECK,60 * 1000);
		return new ApiResult(serviceRetJo);
	}

	/**
	 * 查询支付宝的接口
	 *
	 * @return
	 */
	public Responses reqPaySwitch() {
		JsonObject retJo = new JsonObject();
		String applySwitch = globalConf.payment.get("alipay.switch");
		if (StringUtil.isBlank(applySwitch)) {
			retJo.addProperty("alipaySwitch", Constants.Alipay.ALIPAY_SWITCH_OFF);
		} else {
			retJo.addProperty("alipaySwitch", applySwitch.trim());
		}

		String unionpaySwitch = globalConf.payment.get("unionpay.switch");
		if (StringUtil.isBlank(unionpaySwitch)) {
			retJo.addProperty("unionpaySwitch", Constants.Alipay.ALIPAY_SWITCH_OFF);
		} else {
			retJo.addProperty("unionpaySwitch", unionpaySwitch.trim());
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 查询合同信息
	 *
	 * @return
	 */
	public Responses reqContract(HttpServletResponse response) {
		String cno = getStringParam("contractNo", "合同号");
		InstallmentContract ic = installmentContractRepository.findByContractNo(cno);
		if (ic == null) {
			try {
				response.sendRedirect(configuration.singedErroUrl + URLEncoder.encode("该合同不存在", "UTF-8"));
			} catch (IOException e) {
				throw new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该合同不存在");
			}
		}
		JsonObject retJo = new JsonObject();
		StringBuilder totalTime = new StringBuilder();
		totalTime.append(ic.getTotalMonths()).append("个月");
		if (ic.getTotalDays() != 0) {
			totalTime.append(ic.getTotalDays()).append("天");
		}
		retJo.addProperty("totalDays", totalTime.toString());
		retJo.addProperty("leaseBegin", DateUtil.formatDate(ic.getLeaseBegin()));
		retJo.addProperty("leaseEnd", DateUtil.formatDate(ic.getLeaseEnd()));
		retJo.addProperty("address", ic.getAddress());
		retJo.addProperty("ownerName", ic.getOwnerName());
		retJo.addProperty("ownerCardNo", ic.getOwnerCardNo());
		retJo.addProperty("ownerPhone", ic.getOwnerPhone());
		retJo.addProperty("monthlyAmount", ic.getMonthlyAmount());
		retJo.addProperty("serviceFee", ic.getServiceFee());
		if (ic.getTransfer() == 0) {
			ContractOrder contractOrder = contractOrderRepository.findContractOrderByContractNo(cno);
			String subPayListStr = contractOrder == null ? null : contractOrder.getSubpayList();
			JsonElement subpayList = gson.toJsonTree(gson.fromJson(subPayListStr, new TypeToken<List<subpay>>() {
			}.getType()));
			retJo.add("subpayList", subpayList);
			retJo.addProperty("isSublet",0);
		} else {
			List<Subpay> subpays = subpayRepository.findByContractNo(ic.getSrcContractNo());
			retJo.add("subpayList", gson.toJsonTree(subpays));
			retJo.addProperty("isSublet",1);
		}

		int contractStatus = 1; // TODO 定义常量 1.新增 2.修改
		if (ic.getContractStatus() == InstallmentContract.CONTRACT_STATUS_WAITING_CONFIRMATION_AFTER_MODIFICATION) {
			contractStatus = 2;
		}
		retJo.addProperty("contractStatus", contractStatus);
		JsonObject depositInfo = new JsonObject();
		depositInfo = addContractDepositInfo(depositInfo, cno);
		retJo.add("deposit", depositInfo);


		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 确认合同状态
	 *
	 * @return
	 */
	public Responses confirmContractStatus() {
		String cno = getStringParam("contractNo", "合同号");

		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}

		InstallmentContract contract = installmentContractRepository.findByContractNo(cno);
		if (contract == null) {
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "contract does not exist");
		}

		try {
			crmService.confirmContractStatus(cno);
		} catch (ContractDeletedException e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该合同已经被删除,请联系经纪人员");
		} catch (Exception e) {
			logger.error("failed to confirm contract status, cno:" + cno + ", " + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_CONFIRM_CONTRACT_STATUS_FAIL, "确认合同状态失败");
		}
		// 删除用户信息
		userUtils.delUserInfo(user.getPhone());
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		return responses;
	}

	/**
	 * 提交银行卡信息(无需扣款)
	 *
	 * @return
	 */
	public ApiResult subBindCardUnpay() {
		String userName = getStringParam("user_name", "姓名");
		String phone = getStringParam("phone", "手机号");
		String bankcardNo = getStringParam("bank_card_no", "银行卡号");
		String idCardNo = getStringParam("id_card_no", "身份证号").toUpperCase();
		String bankName = getStringParam("bank_name", "银行名称");
		String rcToken = getStringParam("rc_token");
		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}

		// 调用确认绑卡接口
		boolean changeCard = userUtils.isChangeBankInfo(userId, phone, bankcardNo);
		if (!changeCard) { // 银行卡信息未变更

			BankCard bankCard = bankCardRepository.findBindedCardByBankCardNo(bankcardNo);

			// 校验银行卡信息
			if (bankCard != null) {
				String queryUsername = bankCard.getUserName();
				String queryIdCardNo = bankCard.getIdCardNo();

				// 姓名非空才校验
				if (StringUtil.isNotEmpty(queryUsername)) {
					boolean isNameEqual = queryUsername.equals(userName);
					if (!isNameEqual) {
						return new ApiResult(ErrorMsgCode.ERRCODE_BANK_CARD_INFO_ERROR, "姓名不正确");
					}
				}

				// 身份证非空才校验
				if (StringUtil.isNotEmpty(queryIdCardNo)) {
					boolean isIdCardEqual = queryIdCardNo.equals(idCardNo);
					if (!isIdCardEqual) {
						return new ApiResult(ErrorMsgCode.ERRCODE_BANK_CARD_INFO_ERROR, "身份证不正确");
					}
				}
			}
		}
		String payChannel = getStringParam("pay_channel", "支付渠道");
		if (!Constants.PayHandleChannel.BOB.equals(payChannel)) { // 不是北银，走原有接口
			confirmBindBankcard();
		}

		// 更新用户状态信息
		userUtils.updateCardInfo(userId, Constants.User.USER_INFO_STATUS_CHARGED, phone, bankcardNo, bankName);

		// 保存用户详细信息
		try {
			userUtils.syncUserInfoFromCacheToDB(user.getUserid(), UserInfo.USER_INFO_STATUS_BIND_CARD);
		} catch (Exception e) {
			logger.error("查询用户信息失败：" + e);
			return new ApiResult(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "查询用户信息失败");
		}

		JsonObject userInfo = null;
		try {
			userInfo = userUtils.getUserDetail(userId);
			crmService.commitUserInfo(userInfo);
			// 支付成功则，从缓存删除用户信息，并记入日志
			userUtils.delUserInfo(user.getPhone());
		} catch (ContractDeletedException e) {
			return new ApiResult(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该合同已经被删除,请联系经纪人员");
		} catch (Exception e) {
			logger.error("提交信息到CRM出错:" + e);
			// 不提示用户，由失败任务来处理，存入到失败表中
			userUtils.saveFailSyncCrmUser(user.getPhone(), gson.toJson(userInfo));
		}

		// 上报风控信息
		RcInfo rcInfo = new RcInfo(phone, idCardNo, rcToken);
		try {
			rcService.reportRcInfo(rcInfo);
		} catch (Exception e) {
			logger.error("上报风控信息失败:" + e.getMessage());
			rcUtil.saveFailedRcInfo(phone, rcInfo);
		}

		return new ApiResult();
	}

	/**
	 * 查询绑卡信息结果
	 *
	 * @return
	 */
	public ApiResult reqSubcardResult() {
		long userId = sessionManager.getUserIdFromSession();
		String orderNo = userUtils.reqOrderNoFromCache(userId);
		if (orderNo == null) {
			return new ApiResult(ErrorMsgCode.ERRCODE_PAY_STATUS_QUERY_FAILED, "订单信息不存在");
		}
		JsonObject orderStatus = paymentService.findChargeStatus(orderNo);
		int status = orderStatus.getAsJsonPrimitive("status").getAsInt();
		// 支付失败
		if (status == Constants.Charge.CHARGE_STATUS_FAILED || status == Constants.Charge.CHARGE_STATUS_CANCLE) {
			return new ApiResult(ErrorMsgCode.ERRCODE_CHARGE_FAILED,
					orderStatus.getAsJsonPrimitive("status_desc").getAsString());
			// 支付成功
		} else if (status == Constants.Charge.CHARGE_STATUS_COMPLETED) {
			JsonObject retJo = new JsonObject();
			retJo.addProperty("bind_result", 1);
			// 调用CRM接口,提交个人信息
			// 如果调用失败，则提示用户重新提交，并不会重新扣款
			User user = userUtils.getUser(userId);
			if (user == null) {
				logger.error("没有该用户：userId=" + userId);
				throw new NoSuchUserException(userId);
			}
			// 更新用户状态
			try {
				userUtils.updateUserProperty(user.getPhone(), "info_status",
						Constants.User.USER_INFO_STATUS_CHARGED + "");
			} catch (Exception e) {
				logger.error("更新用户状态为已完成支付失败:" + e);
			}
			JsonObject userInfo = null;
			try {
				userInfo = userUtils.getUserDetail(userId);
				crmService.commitUserInfo(userInfo);
				// 支付成功则，从缓存删除用户信息，并记入日志
				userUtils.delUserInfo(user.getPhone());
			} catch (Exception e) {
				logger.error("提交信息到CRM出错:" + e);
				// 不提示用户，由失败任务来处理，存入到失败表中
				userUtils.saveFailSyncCrmUser(user.getPhone(), gson.toJson(userInfo));
			}
			return new ApiResult(retJo);
		}
		// 支付进行中
		JsonObject retJo = new JsonObject();
		retJo.addProperty("bind_result", 0);
		return new ApiResult(retJo);
	}

	/**
	 * 根据卡号查询银行卡信息
	 *
	 * @return
	 */
	public ApiResult reqBankCardInfo() {
		String bankcardNo = getStringParam("bank_card_no", "银行卡号");
		JsonObject infoJo = paymentService.reqBankCardInfo(bankcardNo);
		if (infoJo.get("bank_name").isJsonNull()) {
			return new ApiResult(ErrorMsgCode.ERRCODE_BANK_CARD_INFO_ERROR, "不支持的银行或银行卡号错误!");
		}
		JsonObject retJo = new JsonObject();
		String bankName = infoJo.getAsJsonPrimitive("bank_name").getAsString();
		// 如果银行卡不存在，则返回错误
		if (StringUtil.isBlank(bankName)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_BANK_CARD_INFO_ERROR, "不支持的银行或银行卡号错误!");
		}
		retJo.addProperty("bank_name", bankName);
		retJo.addProperty("amount", Constants.Charge.getMinMoney(bankName));
		return new ApiResult(retJo);
	}

	/**
	 * 查询已绑定的银行卡
	 *
	 * @return
	 */
	public ApiResult reqBindBankCard() {
		long userId = sessionManager.getUserIdFromSession();
		UserInfo info = userInfoRepository.findByUserId(userId);
		if (info == null) {
			return new ApiResult(ErrorMsgCode.ERRCODE_BANK_CARD_INFO_ERROR, "未绑定银行卡");
		}
		JsonObject retJo = new JsonObject();
		String idCardNo=info.getUserIdNo();
		String bankCardToken="";
		if(StringUtil.isNotBlank(idCardNo))
		{
			String url = configuration.getPaymentServiceUrl() + "api/bankcard/list/";
			Map<String, String> params = new HashMap<>();
			params.put("id_card_no", idCardNo);
			String postRet=null;
			try {
				postRet = HttpUtil.post(url, params);
			}catch (Exception e){
				throw new BaseException(e);
			}
			ApiResult bankcardList = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
			if(bankcardList.result!=null) {
				JsonElement jsonList = bankcardList.result.get("card_list");
				if(jsonList!=null ) {
					JsonArray ja = jsonList.getAsJsonArray();
					if (ja.size() > 0) {
						for(JsonElement je:ja){
							JsonObject j= je.getAsJsonObject();
							//add by arison
							if(!j.isJsonNull()) {  //已绑过卡
								info.setUserBankAccount(j.get("user_name").getAsString());
								info.setUserCardNo(j.get("bank_card_no").getAsString());
								info.setUserBankName(j.get("card_name").getAsString());
								info.setUserCardMobile(j.get("phone").getAsString());
								bankCardToken=j.get("bank_card_token").getAsString();
							}else{
								info.setUserBankAccount("");
								info.setUserCardNo("");
								info.setUserBankName("");
								info.setUserCardMobile("");
							}
							break;
						}
					}  else {
						info.setUserBankAccount("");
						info.setUserCardNo("");
						info.setUserBankName("");
						info.setUserCardMobile("");
					}
				}
			}else{
				info.setUserBankAccount("");
				info.setUserCardNo("");
				info.setUserBankName("");
				info.setUserCardMobile("");
			}
		}else{
			info.setUserBankAccount("");
			info.setUserCardNo("");
			info.setUserBankName("");
			info.setUserCardMobile("");
		}
		retJo.addProperty("phone",  info.getUserCardMobile());
		retJo.addProperty("bank_card_no", info.getUserCardNo());
		retJo.addProperty("bank_name", info.getUserBankName());
		retJo.addProperty("bank_card_token", bankCardToken);
		retJo.addProperty("amount", Constants.Charge.getMinMoney(info.getUserBankName()));
		return new ApiResult(retJo);
	}

	/**
	 * 根据手机号查询合同 @Title: contractListByPhone @return List
	 * <InstallmentContract> @throws
	 * @deprecated TODO 检查下该函数是否有在用，准备删除
	 */
//	public ApiResult reqContractByPhone() {
//		String phone = getStringParam("phone", "手机号");
//		List<InstallmentContract> contractListByPhone = installmentContractRepository.findAllByUserPhone(phone);
//
//		if (contractListByPhone.size() > 0) {
//			JsonObject json = new JsonObject();
//			String jsons = GsonUtil.buildGson().toJson(contractListByPhone);
//			json.addProperty("contractInfo", jsons);
//			return new ApiResult(json);
//		} else {
//			return new ApiResult(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该用户没有签订合同");
//		}
//
//	}

	/**
	 * 查询电子合同
	 *
	 * @return
	 */
	public Responses reqElectContract() {
		String contractNo = getStringParam("contractNo", "合同号");
		String platform = getPlatform();
		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		String contractUrl = "";
		String subletUrl = "";
		try {
			JsonObject userInfo = userUtils.getUserDetail(userId);
			JsonObject result = contractService.getContractUrl(contractNo, platform, userInfo.get("name").getAsString(),
					userInfo.get("user_id_no").getAsString(), user.getPhone());
			contractUrl = result.get("contract_electronic").getAsString();
			subletUrl = result.get("sublet") == null ? "" : result.get("sublet").getAsString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw  new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "获取电子合同失败!");
		}
		if (StringUtil.isBlank(contractUrl)) {
			throw  new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "获取电子合同失败!");
		}
		try {
			contractUrl = URLEncoder.encode(contractUrl, "UTF-8");
			subletUrl = URLEncoder.encode(subletUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("电子合同编码失败" + e.getMessage());
			throw  new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "获取电子合同失败!");
		}
		JsonObject retJo = new JsonObject();
		retJo.addProperty("contractUrl", contractUrl);
		retJo.addProperty("withholdUrl", configuration.withHoldUrl);
		retJo.addProperty("subletUrl", subletUrl);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 查询电子合同Url
	 *
	 * @return
	 */
	public ApiResult reqElectContractUrl() {

		String contractNo = getStringParam("contract_no", "合同号");
		String platform = getPlatform();

		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}

		String contractUrl = StringUtil.EMPTY;
		try {
			contractUrl = getElectContract(contractNo, platform);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ApiResult(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "获取电子合同失败!");
		}

		if (StringUtil.isBlank(contractUrl)) {
			logger.error("electric contract url is empty, contractNo:" + contractNo);
			return new ApiResult(ErrorMsgCode.ERRCODE_ELEC_CONTRACT_NOT_EXIST, "电子合同不存在");
		}

		JsonObject retJo = new JsonObject();
		retJo.addProperty("contract_url", contractUrl);

		return new ApiResult(retJo);
	}

	/**
	 * 获取子订单列表, 能够查询到所有类型合同的订单信息列表
	 *
	 * @return
	 */
	public Responses listContractsSubPay() {
		// APP的124以前的版本走listSubpay()接口
		//int appVersion = getClientVersion();
		long contractSnapshotId = getDefaultLongParam("contractId", 0);
		if (contractSnapshotId != 0) {
			return listSubpay();
		}
		// 根据合同号从合同表里面查询信息
		String cno = getStringParam("contractNo", "合同号");
		InstallmentContract ic = installmentContractRepository.findByContractNo(cno);
		if (ic == null) {
			// 如果从IC表里面没有查到，再从CS表里面查，做兼容
			ContractSnapshot cs = contractSnapshotRepository.findContractByContractNo(cno);
			if (cs == null) {
				throw  new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "该合同不存在");
			}
			return listSubpayFromCs(cs.getSnapshotId());
		}
		// 还没有生成快照表
		if (ic.getContractSnapshotId() == 0) {
			ContractOrder contractOrder = contractOrderRepository.findContractOrderByContractNo(cno);
			String subPayListStr = contractOrder == null ? null : contractOrder.getSubpayList();
			List<subpay> subpays = gson.fromJson(subPayListStr, new TypeToken<List<subpay>>() {
			}.getType());
			List<Subpay> subpayList = new ArrayList<>();
			for (subpay sp : subpays) {
				Subpay pay = new Subpay();
				pay.setIndex(sp.index);
				pay.setPrice(sp.price);
				pay.setUnpayPrice(sp.price);
				pay.setBasePrice(ic.getMonthlyAmount());
				pay.setServiceFee(sp.serviceFee);
				pay.setInstallmentContractId(ic.getInstallmentContractId());
				pay.setPayDate(DateUtil.parseDate(sp.payDate));
				pay.setStartDate(DateUtil.parseDate(sp.startDate));
				pay.setEndDate(DateUtil.parseDate(sp.endDate));
				subpayList.add(pay);
			}
			JsonObject retJo = new JsonObject();
			retJo.add("subpays", gson.toJsonTree(subpayList).getAsJsonArray());
			JsonObject currentPayInfo = new JsonObject();
			currentPayInfo.addProperty("leaseBegin", DateUtil.formatDateTime(ic.getLeaseBegin()));
			currentPayInfo.addProperty("leaseEnd", DateUtil.formatDateTime(ic.getLeaseEnd()));
			currentPayInfo.addProperty("monthlyAmount", ic.getMonthlyAmount());
			currentPayInfo.addProperty("serviceFee", ic.getServiceFee());
			currentPayInfo.addProperty("userPayType", ic.getUserPayType());
			currentPayInfo.addProperty("payee", "会找房（北京）网络技术有限公司");
			// TODO ZXZ 暂时去掉查询电子合同 2016-07-06
			// currentPayInfo.addProperty("contract_url", getElectContract(cno,
			// getPlatform()));
			currentPayInfo.addProperty("contractUrl", "");
			retJo.add("current", currentPayInfo);

			JsonObject depositInfo = new JsonObject();
			depositInfo = addContractDepositInfo(depositInfo, cno);
			retJo.add("deposit", depositInfo);

			Responses responses = new Responses();
			responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
			responses.setBody(retJo);
			return responses;
			//return new ApiResult(retJo);
		} else {
			return listSubpayFromCs(ic.getContractSnapshotId());
		}

	}

	private Charge getChargeObj(Long subPayId,int status){
		Charge charge=null;

		String url = configuration.getPaymentServiceUrl() + "api/charge/subpayid/order/";
		Map<String, String> params = new HashMap<>();
		params.put("subpay_id", subPayId+"");
		params.put("status", Constants.ProductType.RENT); //status 传2 借用rent常量

		String postRet=null;
		try {
			postRet = HttpUtil.post(url, params);
		}catch (Exception e){
			throw new BaseException(e);
		}

		ApiResult chargeList = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		//String subPayId=null;
		if("0".equals(chargeList.status.code)) {
			JsonElement jsonList = chargeList.result.get("charges");
			if(jsonList!=null) {
				JsonArray ja = jsonList.getAsJsonArray();
				for(JsonElement je:ja){
			        charge=new Charge();
					JsonObject j= je.getAsJsonObject();
					String payTimeStr=j.get("pay_time").getAsString();
					charge.payTime=DateUtil.parseDateTime(payTimeStr);
					charge.voucherPrice=j.get("voucher_price").getAsLong();
					charge.payPrice=j.get("pay_price").getAsLong();
					charge.paymentChannelIncome=j.get("payment_channel_income").getAsLong();
					break;
				}
			}
		}
		return charge;
	}
	
	/**
	 * 依据快照表查询订单信息
	 *
	 * @param contractSnapshotId
	 * @return
	 */
	public Responses listSubpayFromCs(long contractSnapshotId) {
		JsonObject retJo = new JsonObject();
		List<Subpay> subpayList = subpayRepository.findByContractSnapshotId(contractSnapshotId);
		// 用户只要还款后，即使未平账也显示已还款,未还款改成0
		for (Subpay spay : subpayList) {
			if (spay.getPayState() == Subpay.PAYSTATE_PAY_UNBALANCE
					|| spay.getPayState() == Subpay.PAYSTATE_PAY_SUBLEASE_PAY_ALL) {
				spay.setPayState(Subpay.PAYSTATE_PAY_ALL);
				spay.setUnpayPrice(0);
			}
			// TODO ZXZ 适配滞纳金的新的计算方式
			spay.setLateFees(spay.getActualOverDue());
		}
		for (Subpay subpay : subpayList) {
			Charge charge=getChargeObj(subpay.getSubpayId(),2);
			if (charge!=null) {
				subpay.setRealPayDate(charge.payTime);
				subpay.setVoucherPrice(charge.voucherPrice);
				subpay.setRealPayPrice(charge.payPrice+charge.paymentChannelIncome);
			} else if (subpay.getPayState() == Subpay.PAYSTATE_PAY_ALL){
				Date realPayDate = subpay.getOfflinePayTime();
				if (realPayDate == null) {
					realPayDate = subpay.getPayDate();
				}
				subpay.setRealPayDate(realPayDate);
				subpay.setRealPayPrice(subpay.getPrice());
			}
		}
		JsonArray subpayJa = new JsonArray();
		if (CollectionUtils.isNotEmpty(subpayList)) {
			subpayJa = gson.toJsonTree(subpayList).getAsJsonArray();
		}
		retJo.add("subpays", subpayJa);

		// 添加综合信息：收款人+付款方式
		ContractSnapshot contractSnapshot = contractSnapshotRepository.findOne(contractSnapshotId);
		if (null == contractSnapshot) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR, "未找到此" + contractSnapshotId + "对应的合同");
		}

		JsonObject currentPayInfo = new JsonObject();
		currentPayInfo.addProperty("leaseBegin", DateUtil.formatDateTime(contractSnapshot.getLeaseBegin()));
		currentPayInfo.addProperty("leaseEnd", DateUtil.formatDateTime(contractSnapshot.getLeaseEnd()));
		currentPayInfo.addProperty("monthlyAmount", contractSnapshot.getInstallmentAmount());
		currentPayInfo.addProperty("serviceFee", contractSnapshot.getServiceFee());
		currentPayInfo.addProperty("userPayType", contractSnapshot.getUserPayType());
		currentPayInfo.addProperty("payee", "会找房（北京）网络技术有限公司");
		// TODO ZXZ 暂时去掉查询电子合同 2016-07-06
		// currentPayInfo.addProperty("contract_url",
		// getElectContract(contractSnapshot.getContractNo(), getPlatform()));
		currentPayInfo.addProperty("contractUrl", "");
		retJo.add("current", currentPayInfo);

		JsonObject depositInfo = new JsonObject();
		depositInfo = addContractDepositInfo(depositInfo, contractSnapshot.getContractNo());
		retJo.add("deposit", depositInfo);

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 查询代扣提示
	 *
	 * @return
	 */
	public ApiResult reqWithholdTip() {

		String withHoldTip = configuration.withHoldTip;
		int showWithHoldTip = configuration.showWithHoldTip;

		JsonObject retJo = new JsonObject();
		retJo.addProperty("withhold_tip", withHoldTip);
		retJo.addProperty("show_withhold_tip", showWithHoldTip);

		return new ApiResult(retJo);
	}

	/**
	 * 查询是否支付过订单
	 *
	 * @return
	 */
	public ApiResult checkIfPaid() {

		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}

		long count = subpayRepository.countByUserId(userId);

		JsonObject retJo = new JsonObject();
		if (count > 0) {
			retJo.addProperty("paid", 1);
		} else {
			retJo.addProperty("paid", 0);
		}

		return new ApiResult(retJo);
	}

	/**
	 * 判断代金劵是否已经过期
	 *
	 * @param voucherId
	 * @return
	 */
	private boolean isVoucherOutDate(String voucherId) {

		long id = Long.parseLong(voucherId);
		Voucher voucher = voucherRepository.findOne((id));
		if (voucher == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		Date expireDate = voucher.getExpireDate();
		if (null != expireDate && voucher.getVoucherUseState() != Voucher.USE_STATE_EXPIRED) {
			long differ = expireDate.getTime() - today.getTime();
			// 兼容之前活动时间多算一天,否则会对用户造成困扰；
			long day = differ / (3600 * 24 * 1000);
			return day < 0;
		}
		return true;
	}

	private String getUserPhone() {
		//User user = userRepository.findOne(sessionManager.getUserIdFromSession());
		User user = userRepository.findByUseridAndState(sessionManager.getUserIdFromSession(),1);
		return user.getPhone();
	}

	/**
	 * 判断用户是否存在
	 *
	 * @param userId
	 * @return
	 */
	private boolean isUserInfo(long userId) {
		UserInfo info = userInfoRepository.findByUserId(userId);
		return info != null;
	}

	/**
	 * 获取返回电子合同地址
	 *
	 * @param contractNo
	 * @param platform
	 * @return
	 */
	private String getElectContract(String contractNo, String platform) {
		return getElectContract(contractNo, platform, false);
	}

	/**
	 * 获取返回电子合同地址
	 *
	 * @param contractNo
	 * @return
	 */
	private String getElectContract(String contractNo, String platform, boolean checkSuffix) {
		String contractUrl = "";
		try {
			JsonObject result = contractService.getContractUrl(contractNo, platform);
			contractUrl = result.get("contract_electronic").getAsString();
			contractUrl = URLEncoder.encode(contractUrl, "UTF-8");
			if (checkSuffix) {
				if (StringUtil.isNotEmpty(contractUrl)) {
					String lowerUrl = contractUrl.toLowerCase();
					if (!lowerUrl.endsWith(".pdf")) { // 只返回扩展名为pdf的
						logger.debug("电子合同地址不为pdf," + contractUrl);
						contractUrl = StringUtil.EMPTY;
					}
				}
			}
		} catch (Exception e) {
			logger.error(String.format("获取电子合同(%s)失败,原因为:%s", contractNo, e.getMessage()));
		}
		return contractUrl;
	}

	private static class PaySummarize {
		long contractId;
		int userPayType;
		String address;
		int installmentRate;
		long subpayCount;
		int index;
		long basePrice;
		long serviceFee;
		long price;
		long unpayPrice;
		String startDate;
		String endDate;
		long subpayId;
		String payee;
		long payId;
		int payStatus;
		String prepayDate;
		String leaseBegin;
		String leaseEnd;
		int lateFees;
		String contractNo;
		int contractStatus;
		String contractDes;
		Date statusTime;
		int userStatus;
		String contractUrl;
		int depositExist;
		int depositPrice;
		int depositStatus;
		long depositId;
		int renegeStatus;
		String signDate;
		int subletStatus;
		int isSubleted;
		int isLaunched;
		String showText;
		long progress=0l;
		int isPrePay;
	}

	private class subpay {
		int index;
		int price;
		int lateFees;
		long serviceFee;
		String payDate;
		String endDate;
		String startDate;
	}

	private static class PaySummarizeConparator implements Comparator<PaySummarize> {

		@Override
		public int compare(PaySummarize o1, PaySummarize o2) {
			if(o1==null)
			{
				return -1;
			}
			if(o2==null)
			{
				return 1;
			}
			long diff = DateUtil.parseDate(o1.signDate).getTime()
					- DateUtil.parseDate(o2.signDate).getTime();

			int result = 0;
			if (0 != diff) {
				result = diff > 0 ? 1 : -1;
			}

			return result;
		}

		@Override
		public boolean equals(Object obj) {
			return false;
		}
	}

	/**
	 * 根据不同渠道，计算需要支付的金额
	 *
	 * @return
	 */
	public Responses calcPrice() {
		String channel = getStringParam("payChannel");
		Long price = getLongParam("payPrice");
		Long voucher_id = getDefaultLongParam("voucherId", -1);

		//银行卡支付时计算方式按照易宝支付来计算
		if (Constants.PayChannel.BANKCARD.equals(channel)) {
			channel = Constants.PayChannel.YEEPAY;
		}
		JsonObject serviceRetJo = paymentService.calcPrice(channel, price, voucher_id);

		JsonObject retJo = new JsonObject();
		retJo.addProperty("payChannel", serviceRetJo.getAsJsonPrimitive("channel").getAsString());
		retJo.addProperty("payPrice", serviceRetJo.getAsJsonPrimitive("pay_price").getAsLong());
		retJo.addProperty("paymentChannelIncome",
				serviceRetJo.getAsJsonPrimitive("payment_channel_income").getAsLong());
		if (voucher_id != -1) {
			retJo.addProperty("voucherId", serviceRetJo.getAsJsonPrimitive("voucher_id").getAsLong());
		}
		retJo.addProperty("amount", serviceRetJo.getAsJsonPrimitive("amount").getAsLong());
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}




	/**
	 * 获取支付入口列表 update by arison
	 * 20170914
	 *
	 * @return
	 */
	public Responses getPayChannels() {
		// 下面的代码暂时不要删除，支付全面合规的时候需要释放，过渡阶段，需要开放微信、支付宝以防止银行卡还款失败用户无法还款
		/*
        String contractNo = getDefaultStringParam("contract_no", null);
        String fundSponsorChannel = null;
        if (contractNo == null) {
            String subpayId = getStringParam("subpay_id", "子订单号");
            Subpay subpay = subpayRepository.findBySubpayId(Long.parseLong(subpayId));
            fundSponsorChannel = subpay.getSourcesCapital();
        } else {
            ContractExtend contractExtend = contractExtendRepository.findContractExtendByContractNo(contractNo);
            if (contractExtend == null) {
                return new ApiResult(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "合同不存在");
            }
            fundSponsorChannel = contractExtend.getFirstSourcesCapital();
        }
        logger.debug("current supported fundSponsor chanel list: " + configuration.hfqFundsponsorChannel);
        logger.debug("the fundChannel of current transaction is: " + fundSponsorChannel);
        // 如果是会分期的渠道，展示全部入口
        JsonObject retJo = new JsonObject();
        if (configuration.hfqFundsponsorChannelHuifenqi.equalsIgnoreCase(fundSponsorChannel)) {
            retJo.addProperty("bank_card", 1);
            retJo.addProperty("wx", 1);
            retJo.addProperty("alipay", 1);
        }
        // 否则仅展示银行卡入口，然后在选择银行卡的时候决定具体到哪个收款账户
        else {
            retJo.addProperty("bank_card", 1);
            retJo.addProperty("wx", 1);
            retJo.addProperty("alipay", 1);
        }
        */
		List<PayChannelVo> payChannelVos=new ArrayList<>();
		//add by arison
		long userId = sessionManager.getUserIdFromSession();
		JsonObject serviceRetJo = paymentService.listBankcard(userId);
		int carNo=serviceRetJo.get("card_no").getAsInt();
		String key=userId+"-"+CARD_NO;
		redisClient.set(key,carNo);
		//update by arison
		serviceRetJo.remove("card_no");

		JsonArray cardList = null;
		try {
			cardList = serviceRetJo.getAsJsonArray("card_list");
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
					String cardName=bankcardObj.get("card_name").getAsString();
					if(cardName!=null && cardName.indexOf("行")!=-1){
						String bankName=cardName.substring(0,cardName.indexOf("行")+1);
						payChannelVo.setName(bankName+"("+bankcardObj.get("card_last").getAsString()+")");
						payChannelVo.setBankName(bankName);
					}
					payChannelVo.setType("bank_card");
					payChannelVo.setCardTop(bankcardObj.get("card_top").getAsString());
					payChannelVo.setCardLast(bankcardObj.get("card_last").getAsString());
					payChannelVo.setBankCode(bankcardObj.get("bankcode").getAsString());
					//TODO
					payChannelVos.add(payChannelVo);
				}
			}
		}

		//return new ApiResult(serviceRetJo);
		PayChannelVo wxPayChannelVo=new PayChannelVo();
		wxPayChannelVo.setName("微信支付");
		wxPayChannelVo.setType("wx");
		wxPayChannelVo.setDesc("付费支付渠道，收取支付金额0.6%手续费");
		//去掉微信支付渠道
		//payChannelVos.add(wxPayChannelVo);

		PayChannelVo alipayPayChannelVo=new PayChannelVo();
		alipayPayChannelVo.setType("alipay");
		alipayPayChannelVo.setDesc("付费支付渠道，收取支付金额0.6%手续费");
		alipayPayChannelVo.setName("支付宝支付");
		payChannelVos.add(alipayPayChannelVo);
		JsonObject retJo = new JsonObject();
		retJo.add("channels", GsonUtil.buildGson().toJsonTree(payChannelVos));

		// "jd"是当汇款渠道为jd的时候展示，目前我们已经没有京东放款的合同，以后也不会有，所以这里屏蔽京东专用回款渠道入口
		//retJo.addProperty("jd", 0);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 添加保证金信息 (只有审核通过的合同或者待扫码的合同才展示保证金)
	 *
	 * @param retJo
	 * @param contractNo
	 * @return
	 */
	private JsonObject addContractDepositInfo(JsonObject retJo, String contractNo) {
		InstallmentContract ic = installmentContractRepository.findByContractNo(contractNo);

		ContractExtend contractExtend = contractExtendRepository.findContractExtendByContractNo(contractNo);

		// 是否展示保证金
		boolean showFlag = false;
		if (ic != null && (Constants.UserConstractStatus.changeBossStatusToUserStatus(
				ic.getContractStatus()) == Constants.UserConstractStatus.APPLY_VERIFY_SUCCESS
				|| ic.getContractStatus() == Constants.UserConstractStatus.BOSS_APPLY_PRE_CONFIRM
				|| ic.getContractStatus() == Constants.UserConstractStatus.BOSS_APPLY_MOD_PRE_CONFIRM)) {
			showFlag = true;
		}

		if (getClientVersion() >= 143 || getClientVersion() == 0) {
			//合同状态：待缴保证金10、待审核5、审核中0、审核失败1，展示保证金
			if (ic != null && (ic.getContractStatus() == Constants.UserConstractStatus.BOSS_APPLY_PRE_DEPOSIT
					|| ic.getContractStatus() == Constants.UserConstractStatus.BOSS_READY_VERIFY
					|| ic.getContractStatus() == Constants.UserConstractStatus.BOSS_APPLY_VERIFIYING
					|| ic.getContractStatus() == Constants.UserConstractStatus.BOSS_APPLY_FAILED)) {
				showFlag = true;
			}
		}

		ContractDeposit contractDeposit = contractDepositRepository.findContractDepositByContractNo(contractNo);
		if (contractExtend != null && contractExtend.getIsDeposit() != 0 && contractDeposit != null
				&& contractDeposit.getDepositAmount() > 0 && showFlag) {// 有保证金
			Long depositId = contractDeposit.getDepositId();
			List<BailFlow> bailFlows = bailFlowRepository.findBailFlowByDepositIdAndStatusOrderByUpdateTimeDesc(depositId, BailFlow.STATUS_PAID);
			BailFlow bailFlow = null;
			if (bailFlows != null && bailFlows.size() > 0) {
				bailFlow = bailFlows.get(0);
			}
			retJo.addProperty("depositExist", 1);
			retJo.addProperty("depositPrice", contractDeposit.getDepositAmount());
			retJo.addProperty("depositStatus", bailFlow==null ? 0 : bailFlow.status);
			retJo.addProperty("depositId", depositId);
			retJo.addProperty("depositRealPayDate",  bailFlow == null ? "" : DateUtil.formatDateTime(bailFlow.payTime));
			retJo.addProperty("depositPayDate", DateUtil.formatDateTime(new Date()));
			Long channelPrice = bailFlow == null ? 0 : bailFlow.channelPrice;
			retJo.addProperty("depositChannelPrice", channelPrice);
			retJo.addProperty("depositRealPrice", contractDeposit.getDepositAmount() + channelPrice);
		} else {// 没有保证金
			retJo.addProperty("depositExist", 0);
			retJo.addProperty("depositPrice", 0);
			retJo.addProperty("depositStatus", 0);
			retJo.addProperty("depositId", 0);
			retJo.addProperty("depositRealPayDate", "");
			retJo.addProperty("depositPayDate", "");
			retJo.addProperty("depositChannelPrice", 0);
			retJo.addProperty("depositRealPrice", 0);
		}

		return retJo;
	}

	/**
	 * 查询电子合同列表
	 *
	 * @return
	 */
	public Responses reqElectronicContracts() {
		String contractNo = getStringParam("contractNo", "合同号");
		try {
			JsonObject result = contractService.getContracts(contractNo);
			Responses responses = new Responses();
			responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
			responses.setBody(result);
			return responses;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw  new BaseException(ErrorMsgCode.ERRCODE_NO_CONTRACT_FAILED, "获取电子合同失败!");
		}

	}

	/**
	 * 查询支付状态
	 *
	 * @return
	 */
	public Responses reqChargeStatus() {
		String productType = getDefaultStringParam("productType", Constants.ProductType.RENT);
		String payId = getStringParam("payId", "订单id");
		String payType = getStringParam("payType", "支付类型 ");

		Map<String, String> params = new HashMap<>();
		params.put("product_type", productType);
		params.put("order_no", payId);
		params.put("pay_type", payType);
		JsonObject retJo = paymentService.reqChargeStatus(params);
		//找出subpayId 相同的key，从redis 中删除支付成功的订单 add by arison 20170704
		String subpayId="";
		JsonObject chargeJo =retJo.get("charges").getAsJsonObject();
		JsonObject jsb=chargeJo.getAsJsonObject();
		subpayId=jsb.get("subpay_id").getAsString();
		int status=jsb.get("status").getAsInt();

		logger.debug(" in reqChargeStatus subpay_id "+subpayId+" status " +status);
		if(status!=3) {
			redisClient.delete(DUPLE_CHARGE + "-" + subpayId);  //update by arison
			logger.debug("remove key " +DUPLE_CHARGE + "-" + subpayId);
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		JsonObject rtJo =new JsonObject();
		rtJo.addProperty("status",status);
		String statusDesc="";
		if(jsb.get("status_desc")!=null && !jsb.get("status_desc").isJsonNull()) {
			statusDesc=jsb.get("status_desc").getAsString();
		}

		if(StringUtil.isNotEmpty(statusDesc)&&(statusDesc.lastIndexOf("]")!=-1)) {
			rtJo.addProperty("statusDesc", statusDesc.substring(statusDesc.lastIndexOf("]")+1));
		}else{
			rtJo.addProperty("statusDesc", statusDesc);
		}
		responses.setBody(rtJo);
		return responses;
	}

	/**
	 * 查询银行列表
	 *
	 * @return
	 */
	public ApiResult reqBankList () {
		// 版本兼容处理，高于170需要传入payChannelAccountSetSn，低版本只需要传入pca0001即可
		String payChannelAccountSetSn = "pca0001";
		if (getClientVersion() >= 170 || getClientVersion() == 0) {
			Long subpayId = getLongParam("subpay_id", "分期订单");
			Subpay subpay = subpayRepository.findValidSubpay(subpayId);
			if (null == subpay) {
				throw new BaseException(ErrorMsgCode.NO_SUCH_SUBPAY, "不存在的分期订单：" + subpayId);
			}

			//payChannelAccountSetSn = configuration.hfqFundsponsorChannel.get(subpay.getSourcesCapital());
			AccountSource accountSource= accountSourceRepository.findBySourceCapital(subpay.getSourcesCapital());
			logger.debug(" the query sourcesCapital:　"+subpay.getSourcesCapital());
			payChannelAccountSetSn=accountSource.getSn();
		}

		Map<String, String> params = new HashMap<>();
		params.put("pay_channel_account_set_sn", payChannelAccountSetSn);
		JsonObject retJo = paymentService.reqBankList(params);

		return new ApiResult(retJo);
	}

	/**
	 * 匹配最合适的支付渠道
	 *
	 * @return
	 */
	public ApiResult reqBankCardPayChannel () {
		long amount = getLongParam("amount","支付金额");
		String bankCode = getStringParam("bank_code","银行编号");

		String payChannelAccountSetSn = "pca0001";
		if (getClientVersion() >= 170 || getClientVersion() == 0) {
			Long subpayId = getLongParam("subpay_id", "分期订单");
			Subpay subpay = subpayRepository.findValidSubpay(subpayId);
			if (null == subpay) {
				throw new BaseException(ErrorMsgCode.NO_SUCH_SUBPAY, "不存在的分期订单：" + subpayId);
			}

			//payChannelAccountSetSn = configuration.hfqFundsponsorChannel.get(subpay.getSourcesCapital());
			AccountSource accountSource= accountSourceRepository.findBySourceCapital(subpay.getSourcesCapital());
			logger.debug(" the query sourcesCapital:　"+subpay.getSourcesCapital());
			payChannelAccountSetSn=accountSource.getSn();
		}

		Map<String,String> params = new HashMap<>();
		params.put("amount", amount + "");
		params.put("bank_code", bankCode);
		params.put("pay_channel_account_set_sn", payChannelAccountSetSn);
		JsonObject retJo = paymentService.reqBankCardPayChannel(params);

		return new ApiResult(retJo);
	}

	/**
	 * 查询是否已经发起转租
	 * @return
	 */
	public ApiResult ifsubletlaunched () {
		String contractNo = getStringParam("contract_no","合同号");
		ContractExtend contractExtend = contractExtendRepository.findContractExtendByContractNo(contractNo);
		int launched = 0;

		if (contractExtend != null && contractExtend.getSublet() == 1) {
			launched = 1;
		}

		JsonObject retJo = new JsonObject();
		retJo.addProperty("launched",launched);
		return new ApiResult(retJo);
	}

	/**
	 * 发起转租
	 * @return
	 */
	public ApiResult launchsublet () {
		String contractNo = getStringParam("contract_no","合同号");
		HashMap<String,String> params= new HashMap<>();
		params.put("contract_no",contractNo);
		try {
			crmService.launchsublet(params);
		} catch (Exception e) {
			logger.error("提交转租信息到CRM出错:" + e);
			BaseException exception;
			if (e instanceof BaseException) {
				exception = new BaseException(ErrorMsgCode.SUBLET_FAILED, ((BaseException)e).getDescription());
			} else {
				exception = new BaseException(ErrorMsgCode.SUBLET_FAILED, "转租失败");
			}
			return new ApiResult(exception.getErrorcode(),exception.getDescription());
		}
		return new ApiResult();
	}

	/**
	 * 判断渠道费是否争取
	 * @param price
	 * @param channelIncome
	 * @return
	 */
	public boolean justChargePrice(long price, long channelIncome) {
		//租金为1.66元以上就应该有渠道费
		if (price > 166 && channelIncome == 0) {
			return false;
		}
		return true;
	}
}