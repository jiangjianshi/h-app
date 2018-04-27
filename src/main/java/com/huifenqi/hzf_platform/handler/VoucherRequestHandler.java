package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.huifenqi.activity.dao.ActivityCouponRepository;
import com.huifenqi.activity.dao.ActivityLinkRepository;
import com.huifenqi.activity.dao.CouponRepository;
import com.huifenqi.activity.dao.VoucherRepository;
import com.huifenqi.activity.domain.ActivityCoupon;
import com.huifenqi.activity.domain.ActivityLink;
import com.huifenqi.activity.domain.Coupon;
import com.huifenqi.activity.domain.Voucher;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.comm.UserUtils;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.NoSuchUserException;
import com.huifenqi.hzf_platform.context.response.ResponseMeta;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.HouseSubDao;
import com.huifenqi.hzf_platform.service.CommUserService;
import com.huifenqi.hzf_platform.service.MqService;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.usercomm.domain.User;

/**
 * Created by arison on 2017/10/5.
 */
@Service
public class VoucherRequestHandler extends BaseRequestHandler {

	@Autowired
    private RedisClient redisClient;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private MqService mqService;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private HouseSubDao houseSubDao;
	
	@Autowired
	private CommUserService commUserService;
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private ActivityCouponRepository activityCouponRepository;
	
	@Autowired
	private ActivityLinkRepository activityLinkRepository;

	/**
	 * 是否可以使用代金券
	 *
	 * @return
	 */
	public Responses canUse() {
		long userId = sessionManager.getUserIdFromSession();
		int scene = getIntParam("scene", "使用场景");
		// 如果用户没有可用的代金券，直接返回0
		List<Voucher> myVoucherList = voucherRepository.findByUserIdAndVoucherUseState(userId,
				Voucher.USE_STATE_NOTUSED);
		logger.debug(String.format("user %d now has %d vouchers available", userId, myVoucherList.size()));

		JsonObject dataJo = new JsonObject();
		// 默认不可使用
		dataJo.addProperty("canuse", 0);
		if (CollectionUtils.isNotEmpty(myVoucherList)) {
			// 如果是还款场景，需要获取支付订单id
			// 还款的规则：用户每月每单只能使用一次代金券
			if (scene == Constants.voucher_use_scene.REPAY) {
				int contractSnapshotId = getIntParam("pay_id", "订单Id"); // pay表废弃,payId用于传递contractSnapshotId

				Pair<Date, Date> range = DateUtil.getMonthRange(new Date());

				logger.debug("should use voucher, startDate=" + DateUtil.formatDate(range.getLeft()) + ", endDate="
						+ DateUtil.formatDate(range.getRight()));

				List<Voucher> voucherList = voucherRepository.getUsableVoucherListInRegion(userId, contractSnapshotId,
						range.getLeft(), range.getRight());

				// 列表为空，证明该订单本月从未使用过代金券，可以再次使用
				if (CollectionUtils.isEmpty(voucherList)) {
					dataJo.addProperty("canuse", 1);
					//return new ApiResult(dataJo);
				}
			}
		}

		Responses responses = new Responses();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result",dataJo);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(returnMap);

		return responses;
	}

	/**
	 * 获取指定用户指定状态的代金券列表
	 *
	 * @return
	 */
	public Responses listVoucher() {
		long userId = sessionManager.getUserIdFromSession();
		// TODO 保存当前时间到缓存
		redisClient.set("userVouchers-" + userId, DateUtil.formatDateTime(new Date()));
		int voucherUseState = getDefaultIntParam("voucher_use_state", -1);

		List<Voucher> voucherList = null;
		// 返回该用户的所有代金券，包括已使用过的
		if (-1 == voucherUseState) {
			voucherList = voucherRepository.findByUserId(userId);
		}

		// 返回指定状态的代金券
		else {
			voucherList = voucherRepository.findByUserIdAndVoucherUseState(userId, voucherUseState);
		}

		filterExpiredVoucher(voucherList);

		// 如果查询可以使用的代金券,则过滤掉不可使用的
		if (voucherUseState == Voucher.USE_STATE_NOTUSED && !CollectionUtils.isEmpty(voucherList)) {
			List<Voucher> tempList = new ArrayList<>();
			for (Voucher v : voucherList) {
				if (v.getDefferDayNum() >= 0) {
					tempList.add(v);
				}
			}
			voucherList = tempList;
		}

		JsonObject retJo = new JsonObject();
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
		retJo.add("voucherList", gson.toJsonTree(voucherList));
		Responses responses = new Responses();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result",retJo);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * 过滤代金券列表，将已过期的代金券状态变为已过期
	 *
	 * @param vouchers
	 */
	private void filterExpiredVoucher(List<Voucher> vouchers) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();

		for (Voucher voucher : vouchers) {
			Date expireDate = voucher.getExpireDate();
			if (null != expireDate && voucher.getVoucherUseState() != Voucher.USE_STATE_EXPIRED) {
				long differ = expireDate.getTime() - today.getTime();
				// 兼容之前活动时间多算一天,否则会对用户造成困扰；
				long day = 0;
				day = differ / (3600 * 24 * 1000);
				voucher.setDefferDayNum(day);
			} else {
				voucher.setDefferDayNum(-1);
			}
		}

		logger.info("expired voucher list size=" + vouchers.size());

		if (CollectionUtils.isNotEmpty(vouchers)) {
			for (Voucher v : vouchers) {
				// 更新已过期的代金券
				if (v.getDefferDayNum() < 0) {
					v.setVoucherUseState(Voucher.USE_STATE_EXPIRED);
					voucherRepository.save(v);
				}
			}
		}
	}

	public Responses existNew() {
		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		JsonObject retJo = new JsonObject();

		Date d1 = DateUtil.parseDateTime((String)redisClient.get("userVouchers-" + userId));
		if (d1 == null) {
			d1 = new Date();
		}
		List<Voucher> voucherList = voucherRepository.findByUserIdAndCreateTimeAfter(userId, d1);
		if (CollectionUtils.isNotEmpty(voucherList)) {
			retJo.addProperty("voucher", 1);
			retJo.addProperty("voucher_num", voucherList.size());
		} else {
			retJo.addProperty("voucher", 0);
			retJo.addProperty("voucher_num", 0);
		}

		Date d2 = DateUtil.parseDateTime((String)redisClient.get("userMessages-" + userId));
		if (d2 == null) {
			d2 = new Date();
		}
		Map<String, String> params = new HashMap<>();
		params.put("page_num", "1");
		params.put("page_size", "1");
		params.put("msg_touser", user.getPhone());
		params.put("msg_channel", "0");
		params.put("user_type", "0");
		params.put("create_time_from", DateUtil.formatDateTime(d2));
		JsonObject msgs = mqService.getMsg(params);

		if (msgs.has("total_elements")) {
			int totalElements = msgs.getAsJsonPrimitive("total_elements").getAsInt();
			if (totalElements > 0) {
				retJo.addProperty("message", 1);
				retJo.addProperty("message_num", totalElements);
				//return new ApiResult(retJo);
			}else{
				retJo.addProperty("message", 0);
				retJo.addProperty("message_num", 0);
			}
		}else {
			retJo.addProperty("message", 0);
			retJo.addProperty("message_num", 0);
		}

		Responses responses = new Responses();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result",retJo);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(returnMap);
		return responses;
	}

	public Responses queryNums() {
		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		JsonObject retJo = new JsonObject();
		Date d1 = new Date();
		List<Voucher> voucherList = voucherRepository.findByUserIdAndCreateTimeAfter(userId,d1);
		int vCount=voucherList.size();

		Date currentDay=DateUtil.parseDate(DateUtil.formatCurrentDate());
		List<Coupon> couponList = couponRepository.findByUserIdAndExpiredDateAfterOrderByExpiredDateAsc(userId, DateUtil.addDays(currentDay, -1));
		 vCount+=couponList.size();

		if (vCount>0) {
			retJo.addProperty("gift", 1);
			retJo.addProperty("giftNum", vCount);
		} else {
			retJo.addProperty("gift", 0);
			retJo.addProperty("giftNum", 0);
		}
/*
		Date d2 = DateUtil.parseDateTime((String)redisClient.get("userMessages-" + userId));
		if (d2 == null) {
			d2 = new Date();
		}
		Map<String, String> params = new HashMap<>();
		params.put("page_num", "1");
		params.put("page_size", "1");
		params.put("msg_touser", user.getPhone());
		params.put("msg_channel", "0");
		params.put("user_type", "0");
		params.put("create_time_from", DateUtil.formatDateTime(d2));
		JsonObject msgs = mqService.getMsg(params);

		if (msgs.has("total_elements")) {
			int totalElements = msgs.getAsJsonPrimitive("total_elements").getAsInt();
			if (totalElements > 0) {
				retJo.addProperty("message", 1);
				retJo.addProperty("message_num", totalElements);
				//return new ApiResult(retJo);
			}else{
				retJo.addProperty("message", 0);
				retJo.addProperty("message_num", 0);
			}
		}else {
			retJo.addProperty("message", 0);
			retJo.addProperty("message_num", 0);
		}
*/


		Long footMarkNum=houseSubDao.countFootmarkHistoryByUserId(userId);
		if(footMarkNum!=null && footMarkNum>0)
		{
			retJo.addProperty("footmark", 1);
			retJo.addProperty("footmarkNum", footMarkNum);
		}else{
			retJo.addProperty("footmark", 0);
			retJo.addProperty("footmarkNum", 0);
		}

		Long collectionNum=houseSubDao.countHouseCollectionItemByUserId(userId);
		if(collectionNum!=null && collectionNum>0)
		{
			retJo.addProperty("collection", 1);
			retJo.addProperty("collectionNum", collectionNum);
		}else{
			retJo.addProperty("collection", 0);
			retJo.addProperty("collectionNum", 0);
		}

		Responses responses = new Responses();
		//Map<String, Object> returnMap = new HashMap<String, Object>();
		//returnMap.put("result",retJo);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}




	/**
	 * 获取指定用户指定状态的优惠券列表
	 *
	 * @return
	 */
	public Responses listCoupon() {
		long userId = sessionManager.getUserIdFromSession();

		int expired = getIntParam("expired");
		Date currentDay=DateUtil.parseDate(DateUtil.formatCurrentDate());
		List<Coupon> couponList = null;
		JsonObject retJo = new JsonObject();
		// 返回该用户的所有优惠券
		if (1 == expired) {  //已过期
			couponList = couponRepository.findByUserIdAndExpiredDateBeforeOrderByExpiredDateAsc(userId, currentDay);
			int count=couponList.size();
			if(count>0)
			{
				for(int i=0;i<count;i++)
				{
					Coupon cp=couponList.get(i);
					cp.setGoingExpire(2);
				}
			}
		}else {//未过期
			couponList = couponRepository.findByUserIdAndExpiredDateAfterOrderByExpiredDateAsc(userId, DateUtil.addDays(currentDay,-1));
			long totalCount=couponList.size();
			if(totalCount>0) {
				//3天内过期
				String str=totalCount+"张可用";
				Long count=0l;
				count= couponRepository.countByUserIdAndExpiredDateBefore(userId,  DateUtil.addDays(currentDay,3));
				if(count>0)
				{
					str=str+",有"+count+"张将在3天内过期";
					for(int i=0;i<count;i++)
					{
						Coupon cp=couponList.get(i);
						cp.setGoingExpire(1);
					}
				}
				retJo.addProperty("description",str);
			}else{
				retJo.addProperty("description","您还没有优惠券哦");
			}
		}

		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
		retJo.add("couponList", gson.toJsonTree(couponList));
		Responses responses = new Responses();
		//Map<String, Object> returnMap = new HashMap<String, Object>();
		//returnMap.put("result",retJo);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}
	
	/**
	 * @Title: getGiftPackInfo
	 * @Description: 获取领取大礼包配置信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年10月13日 下午6:38:55
	 */
	public Responses getGiftPackInfo() {
		JSONObject retJo = new JSONObject();
		String activityShowFlag = configuration.activityShowFlag;
		String destinationUrl = configuration.destinationUrl;
		String giftImgUrl = configuration.giftImgUrl;
		String showImgUrl = configuration.showImgUrl;
		retJo.put("showFlag", activityShowFlag);
		retJo.put("destinationUrl", destinationUrl);
		retJo.put("giftImgUrl", giftImgUrl);
		retJo.put("showImgUrl", showImgUrl);
		retJo.put("title", "会找房I领取大礼包");
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}
	
	/**
	 * @Title: showCouponType
	 * @Description: 展示可领取的优惠券列表
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年10月12日 下午6:02:14
	 */
	public Responses showCouponType() {
		JsonObject retJo = new JsonObject();
		// 查询当前用户可领取的优惠券
		List<Map<String, String>> couponList = new ArrayList<Map<String, String>>();
		// 获取1：善之泉净水器1682元抵用券
		List<ActivityCoupon> activityCouponList1 = activityCouponRepository.getActivityCouponList(1);
		if (CollectionUtils.isNotEmpty(activityCouponList1)) {
			ActivityCoupon activityCoupon = activityCouponList1.get(0);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponName", activityCoupon.getCouponName());
				couponList.add(map);
			}
		}
		// 获取2：天然鸡翅木手串87元抵用券
		List<ActivityCoupon> activityCouponList2 = activityCouponRepository.getActivityCouponList(2);
		if (CollectionUtils.isNotEmpty(activityCouponList2)) {
			ActivityCoupon activityCoupon = activityCouponList2.get(0);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponName", activityCoupon.getCouponName());
				couponList.add(map);
			}
		}
		// 获取3：纯银项链246元抵用券
		List<ActivityCoupon> activityCouponList3 = activityCouponRepository.getActivityCouponList(3);
		if (CollectionUtils.isNotEmpty(activityCouponList3)) {
			ActivityCoupon activityCoupon = activityCouponList3.get(0);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponName", activityCoupon.getCouponName());
				couponList.add(map);
			}
		}
		// 获取4：我买网59减20抵用券
		List<ActivityCoupon> activityCouponList4 = activityCouponRepository.getActivityCouponList(4);
		if (CollectionUtils.isNotEmpty(activityCouponList4)) {
			ActivityCoupon activityCoupon = activityCouponList4.get(0);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponName", activityCoupon.getCouponName());
				couponList.add(map);
			}
		}
		// 获取:带领取链接的优惠券数据
		List<ActivityLink> activityLinkList = activityLinkRepository.getActivityLinkList();
		if (CollectionUtils.isNotEmpty(activityLinkList)) {
			for (int i = 0; i < activityLinkList.size(); i++) {
				ActivityLink activityLink = activityLinkList.get(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponName", activityLink.getCouponName());
				couponList.add(map);
			}
		}
		
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
		retJo.add("couponList", gson.toJsonTree(couponList));
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}
	
	/**
	 * @Title: checkUserGiftPack
	 * @Description: 检验用户是否已经领取过大礼包
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年10月12日 下午3:53:10
	 */
	public Responses checkUserGiftPack(HttpServletRequest request) throws Exception {
		Responses responses = new Responses();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String phone = RequestUtils.getParameterString(request, "phone");
		// 查询当前用户下是否存在优惠券，用来判定用户是否领取过大礼包
		List<Coupon> couponList = couponRepository.getCouponListByPhone(phone);
		if (CollectionUtils.isEmpty(couponList)) {
			returnMap.put("result", "用户未领取大礼包");
			responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
			responses.getMeta().setErrorMessage("用户未领取大礼包");
		} else {
			returnMap.put("result", "用户已经领取大礼包");
			responses.getMeta().setErrorCode(ErrorMsgCode.USER_OBTAIN_GIFTPACK);
			responses.getMeta().setErrorMessage("用户已经领取大礼包");
		}
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: obtainGiftPack
	 * @Description: 获取大礼包
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年10月11日 下午4:01:20
	 */
	public Responses obtainGiftPack(HttpServletRequest request) throws Exception {
		Responses responses = new Responses();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String phone = RequestUtils.getParameterString(request, "phone");
		// 判断：如果手机号不存在，就执行直接领取的逻辑；否则，先判断该手机号是否注册过，如果未注册，则注册之后再领取，如果已经注册了，就直接领取。
		if (StringUtil.isBlank(phone)) {
			// 领取大礼包：
			long userId = sessionManager.getUserIdFromSession();
			// 查询当前用户下是否存在优惠券，用来判定用户是否领取过大礼包
			List<Coupon> couponList = couponRepository.getCouponListByPhone(phone);
			if (CollectionUtils.isEmpty(couponList)) {
				this.executeCouponInfo(userId, phone);
			} else {
				returnMap.put("result", "用户已经领取大礼包");
				responses.getMeta().setErrorCode(ErrorMsgCode.USER_OBTAIN_GIFTPACK);
				responses.getMeta().setErrorMessage("用户已经领取大礼包");
				responses.setBody(returnMap);
				return responses;
			}
		} else {
			// 调起验证手机号是否注册接口
			ApiResult userResult = (ApiResult) commUserService.isPhoneRegistered(request);
			if ("2330044".equals(userResult.status.code)) {// 用户已存在
				// 通过手机号查询用户信息，先获取用户的user_id
				String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
				Map<String, String> params = new HashMap<>();
				params.put("phone", phone);
				String postRet = HttpUtil.post(userServiceUrl, params);
				ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
				if("0".equals(result.status.code)){
					// 领取大礼包：
					long userId = Long.parseLong(JSONObject.parseObject(result.result.toString()).getString("user_id"));
					// 查询当前用户下是否存在优惠券，用来判定用户是否领取过大礼包
					List<Coupon> couponList = couponRepository.getCouponListByPhone(phone);
					if (CollectionUtils.isEmpty(couponList)) {
						this.executeCouponInfo(userId, phone);
					} else {
						returnMap.put("result", "用户已经领取大礼包");
						responses.getMeta().setErrorCode(ErrorMsgCode.USER_OBTAIN_GIFTPACK);
						responses.getMeta().setErrorMessage("用户已经领取大礼包");
						responses.setBody(returnMap);
						return responses;
					}
				}
			} else if ("0".equals(userResult.status.code)) {// 用户未注册
				// 调起用户注册接口
				ApiResult result = (ApiResult) commUserService.newUserRegister(request);
				if("0".equals(result.status.code)){
					// 领取大礼包：
					long userId = Long.parseLong(JSONObject.parseObject(result.result.toString()).getString("userId"));
					this.executeCouponInfo(userId, phone);
				} else {
					throw new BaseException(ErrorMsgCode.USER_FAIL_REGISTER, "用户注册失败");
				}
			}
		}
		
		returnMap.put("result", "领取大礼包成功！");
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("领取大礼包成功！");
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: executeCouponInfo
	 * @Description: 执行优惠券相关操作
	 * @return void
	 * @author 叶东明
	 * @dateTime 2017年10月12日 下午3:19:56
	 */
	public void executeCouponInfo(long userId, String phone) {
		List<ActivityCoupon> activityCouponList = activityCouponRepository.getAllActivityCouponList();
		List<ActivityCoupon> activityCouponList1 = new ArrayList<ActivityCoupon>();
		List<ActivityCoupon> activityCouponList2 = new ArrayList<ActivityCoupon>();
		List<ActivityCoupon> activityCouponList3 = new ArrayList<ActivityCoupon>();
		List<ActivityCoupon> activityCouponList4 = new ArrayList<ActivityCoupon>();
		if (CollectionUtils.isNotEmpty(activityCouponList)) {
			for (ActivityCoupon activityCoupon : activityCouponList) {
				if (activityCoupon.getCouponType() == 1) {
					activityCouponList1.add(activityCoupon);
				} else if (activityCoupon.getCouponType() == 2) {
					activityCouponList2.add(activityCoupon);
				} else if (activityCoupon.getCouponType() == 3) {
					activityCouponList3.add(activityCoupon);
				} else if (activityCoupon.getCouponType() == 4) {
					activityCouponList4.add(activityCoupon);
				}
			}
		}
		
		// 获取1：善之泉净水器1682元抵用券
		if (CollectionUtils.isNotEmpty(activityCouponList1)) {
			Random random = new Random();
			int num = random.nextInt(activityCouponList1.size());
			ActivityCoupon activityCoupon = activityCouponList1.get(num);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				// 保存优惠券信息到Coupon表中
				Coupon entity = new Coupon();
				entity.setUserId(userId);
				entity.setPhone(phone);
				if (activityCoupon.getCouponCode().contains(",")) {
					entity.setCode(activityCoupon.getCouponCode().split(",")[0]);
					entity.setPassword(activityCoupon.getCouponCode().split(",")[1]);
				} else {
					entity.setCode(activityCoupon.getCouponCode());
				}
				entity.setTitle(activityCoupon.getCouponName());
				entity.setCouponType(activityCoupon.getCouponType());
				entity.setDesc(activityCoupon.getCouponDesc());
				entity.setCouponLink(activityCoupon.getCouponLink());
				entity.setIndexLink(activityCoupon.getIndexLink());
				entity.setStartDate(activityCoupon.getStartDate());
				entity.setExpiredDate(activityCoupon.getExpiredDate());
				entity.setState(1);
				couponRepository.save(entity);
				// 把该优惠券置为无效
				activityCouponRepository.updateActivityCoupon(activityCoupon.getId());
			}
		}
		// 获取2：天然鸡翅木手串87元抵用券
		if (CollectionUtils.isNotEmpty(activityCouponList2)) {
			Random random = new Random();
			int num = random.nextInt(activityCouponList2.size());
			ActivityCoupon activityCoupon = activityCouponList2.get(num);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				// 保存优惠券信息到Coupon表中
				Coupon entity = new Coupon();
				entity.setUserId(userId);
				entity.setPhone(phone);
				if (activityCoupon.getCouponCode().contains(",")) {
					entity.setCode(activityCoupon.getCouponCode().split(",")[0]);
					entity.setPassword(activityCoupon.getCouponCode().split(",")[1]);
				} else {
					entity.setCode(activityCoupon.getCouponCode());
				}
				entity.setTitle(activityCoupon.getCouponName());
				entity.setCouponType(activityCoupon.getCouponType());
				entity.setDesc(activityCoupon.getCouponDesc());
				entity.setCouponLink(activityCoupon.getCouponLink());
				entity.setIndexLink(activityCoupon.getIndexLink());
				entity.setStartDate(activityCoupon.getStartDate());
				entity.setExpiredDate(activityCoupon.getExpiredDate());
				entity.setState(1);
				couponRepository.save(entity);
				// 把该优惠券置为无效
				activityCouponRepository.updateActivityCoupon(activityCoupon.getId());
			}
		}
		// 获取3：纯银项链246元抵用券
		if (CollectionUtils.isNotEmpty(activityCouponList3)) {
			Random random = new Random();
			int num = random.nextInt(activityCouponList3.size());
			ActivityCoupon activityCoupon = activityCouponList3.get(num);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				// 保存优惠券信息到Coupon表中
				Coupon entity = new Coupon();
				entity.setUserId(userId);
				entity.setPhone(phone);
				if (activityCoupon.getCouponCode().contains(",")) {
					entity.setCode(activityCoupon.getCouponCode().split(",")[0]);
					entity.setPassword(activityCoupon.getCouponCode().split(",")[1]);
				} else {
					entity.setCode(activityCoupon.getCouponCode());
				}
				entity.setTitle(activityCoupon.getCouponName());
				entity.setCouponType(activityCoupon.getCouponType());
				entity.setDesc(activityCoupon.getCouponDesc());
				entity.setCouponLink(activityCoupon.getCouponLink());
				entity.setIndexLink(activityCoupon.getIndexLink());
				entity.setStartDate(activityCoupon.getStartDate());
				entity.setExpiredDate(activityCoupon.getExpiredDate());
				entity.setState(1);
				couponRepository.save(entity);
				// 把该优惠券置为无效
				activityCouponRepository.updateActivityCoupon(activityCoupon.getId());
			}
		}
		// 获取4：我买网59减20抵用券
		if (CollectionUtils.isNotEmpty(activityCouponList4)) {
			Random random = new Random();
			int num = random.nextInt(activityCouponList4.size());
			ActivityCoupon activityCoupon = activityCouponList4.get(num);
			// 优惠券未过期才执行
			if (activityCoupon.getExpiredDate().getTime() >= DateUtil.parseDate(DateUtil.formatCurrentDate()).getTime()) {
				// 保存优惠券信息到Coupon表中
				Coupon entity = new Coupon();
				entity.setUserId(userId);
				entity.setPhone(phone);
				if (activityCoupon.getCouponCode().contains(",")) {
					entity.setCode(activityCoupon.getCouponCode().split(",")[0]);
					entity.setPassword(activityCoupon.getCouponCode().split(",")[1]);
				} else {
					entity.setCode(activityCoupon.getCouponCode());
				}
				entity.setTitle(activityCoupon.getCouponName());
				entity.setCouponType(activityCoupon.getCouponType());
				entity.setDesc(activityCoupon.getCouponDesc());
				entity.setCouponLink(activityCoupon.getCouponLink());
				entity.setIndexLink(activityCoupon.getIndexLink());
				entity.setStartDate(activityCoupon.getStartDate());
				entity.setExpiredDate(activityCoupon.getExpiredDate());
				entity.setState(1);
				couponRepository.save(entity);
				// 把该优惠券置为无效
				activityCouponRepository.updateActivityCoupon(activityCoupon.getId());
			}
		}
		// 获取:链接优惠券
		List<ActivityLink> activityLinkList = activityLinkRepository.getActivityLinkList();
		if (CollectionUtils.isNotEmpty(activityLinkList)) {
			for (int i = 0; i < activityLinkList.size(); i++) {
				ActivityLink activityLink = activityLinkList.get(i);
				// 保存优惠券信息到Coupon表中
				Coupon entity = new Coupon();
				entity.setUserId(userId);
				entity.setPhone(phone);
				entity.setTitle(activityLink.getCouponName());
				entity.setCouponType(activityLink.getCouponType());
				entity.setDesc(activityLink.getCouponDesc());
				entity.setCouponLink(activityLink.getCouponLink());
				entity.setIndexLink(activityLink.getIndexLink());
				entity.setStartDate(activityLink.getStartDate());
				entity.setExpiredDate(activityLink.getExpiredDate());
				entity.setState(1);
				couponRepository.save(entity);
			}
		}
	}
	
	/**
	 * @Title: getValidCouponList
	 * @Description: 获取用户优惠券列表
	 * @return Responses
	 * @author 叶东明
	 * @throws Exception 
	 * @dateTime 2017年10月12日 下午5:23:30
	 */
	public Responses getCouponList(HttpServletRequest request) throws Exception {
		String phone = RequestUtils.getParameterString(request, "phone");
		// 优惠券使用状态：0 未过期；1 已过期；2 即将过期（3天内）
		int expired = RequestUtils.getParameterInt(request, "expired");
		JsonObject retJo = new JsonObject();
		// 查询当前用户下有效的优惠券
		List<Coupon> couponList = new ArrayList<Coupon>();
		if (expired == 0) {// 未过期
			List<Coupon> validCouponList = couponRepository.getValidCouponListByPhone(phone);
			if (CollectionUtils.isNotEmpty(validCouponList)) {
				int count = 0;// 计数器：统计即将过期的优惠券
				for (Coupon coupon : validCouponList) {
					// 添加即将过期提示
					Date currentTime = DateUtil.parseDate(DateUtil.formatCurrentDate());
					long timeDiff = DateUtil.parseDate(DateUtil.format("yyyy-MM-dd", coupon.getExpiredDate())).getTime() - currentTime.getTime();
					int dayNum = (int) (timeDiff / (3600 * 24 * 1000));
					if (dayNum <= 3) {
						coupon.setState(2);
						count++;
					}
					couponList.add(coupon);
				}
				// 跑马灯提示用户优惠券信息
				String str = validCouponList.size() + "张优惠券可用";
				if (count > 0) {
					str = str +"，有"+ count +"张优惠券将在3天内过期";
				}
				retJo.addProperty("description",str);
			}
		} else {// 已过期
			List<Coupon> expiredCouponList = couponRepository.getExpiredCouponListByPhone(phone);
			if (CollectionUtils.isNotEmpty(expiredCouponList)) {
				for (Coupon coupon : expiredCouponList) {
					coupon.setState(0);
					couponList.add(coupon);
				}
			}
		}
		
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
		retJo.add("couponList", gson.toJsonTree(couponList));
		Responses responses = new Responses();
		ResponseMeta meta = new ResponseMeta();
		meta.setRows(couponList.size());
		meta.setTotalRows(couponList.size());
		meta.setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setMeta(meta);
		responses.setBody(retJo);
		return responses;
	}

}
