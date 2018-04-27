package com.huifenqi.hzf_platform.comm;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.NoSuchUserException;
import com.huifenqi.hzf_platform.service.ContractService;
import com.huifenqi.hzf_platform.utils.*;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.usercomm.dao.UserInfoRepository;
import com.huifenqi.usercomm.dao.UserRepository;
import com.huifenqi.usercomm.dao.contract.ContractSnapshotRepository;
import com.huifenqi.usercomm.dao.contract.InstallmentApplyRepository;
import com.huifenqi.usercomm.dao.contract.InstallmentContractRepository;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.UserInfo;
import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import com.huifenqi.usercomm.domain.contract.InstallmentApply;
import com.huifenqi.usercomm.domain.contract.InstallmentContract;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arison on 2015/10/14.
 */
@Component
public class UserUtils {

	private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

	@Autowired
	private InstallmentApplyRepository installmentApplyRepository;

	@Autowired
	private ContractSnapshotRepository contractSnapshotRepository;

	@Autowired
	private InstallmentContractRepository installmentContractRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private ContractService contractService;

	@Autowired
	private UserInfoRepository userInfoRepository;

	// 用户信息在redis中key的前缀
	private final static String USER_INFO_PREFIX = "user_info.phone";

	// 同步失败信息在redis中key的前缀
	private final static String USER_SYNC_CRM_FAIL_PREFIX = "user_crm.sync.failed";

	private final static String USER_LOCK_PHONE_PREFIX = "user_lock.phone";

	@Autowired
	private LockManager lockManager;

	@Autowired
	private Configuration configuration;


	/**
	 * 用户是否申请过会分期
	 *
	 * @param phone
	 * @return
	 */
	public boolean hasIntentApplied(String phone) {
		List<InstallmentApply> installmentApplyList = installmentApplyRepository
				.findByPhoneAndStateOrderByCreateTimeDesc(phone, 1);

		return CollectionUtils.isNotEmpty(installmentApplyList);
	}

	/**
	 * 用户最后一次申请所填写城市
	 *
	 * @param phone
	 * @return
	 */
	public String getLastIntentCity(String phone) {
		List<InstallmentApply> installmentApplyList = installmentApplyRepository
				.findByPhoneAndStateOrderByCreateTimeDesc(phone, 1);

		// 申请不存在
		if (CollectionUtils.isEmpty(installmentApplyList)) {
			return null;
		}

		InstallmentApply installmentApply = installmentApplyList.get(0);
		if (installmentApply == null) {
			return null;
		}

		return installmentApply.getCity();

	}

	public boolean hasSignedTheContract(Long userId) {
		// 查询该用户是否已经有审核通过的合同
		List<ContractSnapshot> contractSnapshotList = contractSnapshotRepository.findValidContractByUserId(userId);
		if (CollectionUtils.isNotEmpty(contractSnapshotList)) {
			return true;
		}

		// 查询用户是否有审核中的合同
		List<InstallmentContract> installmentContractList = installmentContractRepository.findValidContractsByUserId(userId);
		if (CollectionUtils.isNotEmpty(installmentContractList)) {
			return true;
		}

		return false;
	}

	/**
	 * 注册用户
	 *
	 * @param phone
	 * @param startSource
	 * @param platform
	 * @return
	 */
	public User register(String phone, String startSource, String platform) {
		logger.info(String.format("user %s has been registered, start source=%s", phone, startSource));

		String key = USER_LOCK_PHONE_PREFIX + "." + phone;

		boolean lockFlag = lockManager.lock(key);

		if (!lockFlag) {
			throw new BaseException(ErrorMsgCode.USER_HAVE_EXISTED, "用户已经存在");
		}
		try {
			User user = new User();
			user.setPhone(phone);
			user.setCreateTime(new Date());
			user.setState(1);
			user.setLastLogin(new Date());

			if (StringUtil.isEmpty(startSource)) {
				user.setStartSource(platform);
			} else {
				user.setStartSource(startSource);
			}

			return userRepository.save(user);
		} finally {
			if (lockFlag) {
				lockManager.unLock(key);
			}
		}

	}


	/**
	 * 注册用户
	 *
	 * @param phone
	 * @param startSource
	 * @param platform
	 * @return
	 */
	public User registerNew(String phone, String startSource, String platform) {
		logger.info(String.format("user %s has been registered, start source=%s", phone, startSource));

		String key = USER_LOCK_PHONE_PREFIX + "." + phone;

		boolean lockFlag = lockManager.lock(key);

		if (!lockFlag) {
			throw new BaseException(ErrorMsgCode.USER_HAVE_EXISTED, "用户已经存在");
		}

		User user = new User();
		try {
			/*User user = new User();
			user.setPhone(phone);
			user.setCreateTime(new Date());
			user.setState(1);
			user.setLastLogin(new Date());

			if (StringUtil.isEmpty(startSource)) {
				user.setStartSource(platform);
			} else {
				user.setStartSource(startSource);
			}

			return userRepository.save(user);*/
			Map<String, String> saveUserParams = new HashMap<>();
			if (StringUtil.isEmpty(startSource)) {
				// user.setStartSource(platform);
				saveUserParams.put("startsource", platform);
			} else {
				// user.setStartSource(startSource);
				saveUserParams.put("startsource", startSource);
			}

			saveUserParams.put("phone", phone);
			String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_reg_service/";
			try {
				String postRet = HttpUtil.post(userServiceUrl, saveUserParams);
				ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
				if ("03520004".equals(result.status.code)) {
					throw new BaseException(ErrorMsgCode.USER_HAVE_EXISTED, "用户已存在");
				}
				JsonObject userInfoJo = result.result;
				long userId = userInfoJo.get("user_id").getAsLong();
				logger.debug("in registerNew userid  : " + userId);
				user.setUserid(userId);
			} catch (Exception e) {
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR, "用户注册失败");
			}

		} finally {
			if (lockFlag) {
				lockManager.unLock(key);
			}
		}

		user.setPhone(phone);
		if (StringUtil.isEmpty(startSource)) {
			user.setStartSource(platform);
		} else {
			user.setStartSource(startSource);
		}
		return user;
	}

	public User register(String phone, String platform) {
		return registerNew(phone, null, platform);
	}

	/**
	 * 获取用户信息
	 *
	 * @return
	 */
	public User getUser(long userId) {
		try {
			//User user = userRepository.findOne(userId);
			User user = userRepository.findByUseridAndState(userId,1);
			return user;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 更新缓存中的用户信息
	 *
	 * @param phone
	 *            用户手机号
	 * @param infoStatus
	 *            信息状态
	 * @param userInfo
	 *            用户信息
	 * @throws Exception
	 */
	public void updateUserInfo(String phone, int infoStatus, JsonObject userInfo) throws Exception {
		Map<String, String> properties = new HashMap<>();
		properties.put("info_status", infoStatus + "");
		properties.put("user_info", GsonUtil.buildGson().toJson(userInfo));
		redisClient.setAllInMap(getCacheKey(phone), properties);
	}

	/**
	 * 更新缓存中的用户信息 add by arsion
	 *
	 * @param phone
	 *            用户手机号
	 * @throws Exception
	 */
	public void updateUserInfo(String phone, Map<String, String>  properties) throws Exception {
		redisClient.setAllInMap(getCacheKey(phone), properties);
	}

	/**
	 * 更新缓存中的用户信息
	 *
	 * @param phone
	 *            用户手机号
	 * @param infoStatus
	 *            信息状态
	 * @param userSource
	 *            用户来源
	 * @param userInfo
	 *            用户信息
	 * @throws Exception
	 */
	public void updateUserInfo(String phone, int infoStatus, int userSource, JsonObject userInfo) throws Exception {
		Map<String, String> properties = new HashMap<>();
		properties.put("info_status", infoStatus + "");
		properties.put("user_source", userSource + "");
		properties.put("user_info", GsonUtil.buildGson().toJson(userInfo));
		redisClient.setAllInMap(getCacheKey(phone), properties);
	}

	/**
	 * 更新用户属性
	 *
	 * @param phone
	 * @param key
	 * @param value
	 */
	public void updateUserProperty(String phone, String key, String value) throws Exception {
		redisClient.setInMap(getCacheKey(phone), key, value);
	}

	/**
	 * 获取用户属性
	 *
	 * @param phone
	 * @param key
	 * @return
	 */
	public String getUserProperty(String phone, String key) throws Exception {
		Object result = redisClient.getFromMap(getCacheKey(phone), key);
		if (result == null) {
			return null;
		}
		return result.toString();
	}

	/**
	 * 查询用户信息
	 *
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> getUserInfoFromCache(String phone) throws Exception {
		return redisClient.getMap(getCacheKey(phone));
	}

	/**
	 * 删除用户信息
	 *
	 * @param phone
	 * @throws Exception
	 */
	public void delUserInfoFromCache(String phone) throws Exception {
		redisClient.delete(getCacheKey(phone));
	}

	/**
	 * 更新用户的银行卡信息
	 *
	 * @param userId
	 * @param infoStatus
	 * @param phone
	 * @param bankcardNo
	 * @param bankName
	 * @throws BaseException
	 */
	public void updateCardInfo(long userId, int infoStatus, String phone, String bankcardNo, String bankName)
			throws BaseException {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			Map<Object, Object> infoMap = getUserInfoFromCache(user.getPhone());
			JsonObject properties = null;
			if (infoMap == null || infoMap.isEmpty()) {
				throw new BaseException(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "请先提交身份证信息");
			} else {
				int cacheStatus = Integer.valueOf(infoMap.get("info_status").toString());
				if (cacheStatus == Constants.User.USER_INFO_STATUS_CHARGED) {
					throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "该用户资料已存在,不允许再次提交");
				}
				if (cacheStatus > infoStatus) {
					infoStatus = cacheStatus;
				}
				properties = GsonUtil.buildGson().fromJson((String) infoMap.get("user_info"), JsonObject.class);
			}
			properties.addProperty("user_id", user.getUserid());
			properties.addProperty("bank_bind_phone", phone);
			properties.addProperty("bank_card_no", bankcardNo);
			properties.addProperty("bank_name", bankName);

			int userSource=-1;
			try{
				userSource=Integer.valueOf(infoMap.get("user_source").toString());
			}catch (Exception e)
			{
				logger.error(" user_source change error  " +e.getMessage());
			}

			if(userSource!=-1){
				updateUserInfo(user.getPhone(), infoStatus, userSource,properties);
			}else{
				updateUserInfo(user.getPhone(), infoStatus, properties);
			}

		} catch (BaseException e) {
			logger.error("保存用户信息失败：" + e);
			throw e;
		} catch (Exception e) {
			logger.error("保存用户信息失败：" + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_MATERIAL_SAVE_FAILED, "信息保存失败");
		}
	}

	/**
	 * 判断银行卡信息是否变更
	 *
	 * @param userId
	 * @param bankBindPhone
	 * @param bankCardNo
	 * @return
	 */
	public boolean isChangeBankInfo(long userId, String bankBindPhone, String bankCardNo) {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			Map<Object, Object> infoMap = getUserInfoFromCache(user.getPhone());
			JsonObject properties = null;
			if (infoMap == null || infoMap.isEmpty()) {
				throw new BaseException(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "请先提交信息");
			} else {
				properties = GsonUtil.buildGson().fromJson((String) infoMap.get("user_info"), JsonObject.class);
				if (!properties.has("bank_bind_phone") || !properties.has("bank_card_no")) {
					return true;
				}
				if (properties.get("bank_bind_phone").isJsonNull() || properties.get("bank_card_no").isJsonNull()) {
					return true;
				}
				String cBankBindPhone = properties.getAsJsonPrimitive("bank_bind_phone").getAsString();
				String cBankCardNo = properties.getAsJsonPrimitive("bank_card_no").getAsString();
				if (bankBindPhone.equals(cBankBindPhone) && bankCardNo.equals(cBankCardNo)) {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("获取信息失败：" + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "获取信息失败");
		}

		return true;
	}

	/**
	 * 查询用户的订单号
	 *
	 * @param userId
	 * @return
	 */
	public String reqOrderNoFromCache(long userId) {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			String orderNo = getUserProperty(user.getPhone(), "order_no");
			if (StringUtil.isRedisBlank(orderNo)) {
				return null;
			} else {
				return orderNo;
			}
		} catch (Exception e) {
			logger.error("获取信息失败：" + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "获取信息失败");
		}
	}

	/**
	 * 更新用户订单号
	 *
	 * @param userId
	 * @param orderNo
	 */
	public void updateOrderNo(long userId, String orderNo) {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			updateUserProperty(user.getPhone(), "order_no", orderNo);
		} catch (Exception e) {
			logger.error("保存用户信息失败：" + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_MATERIAL_SAVE_FAILED, "信息保存失败");
		}
	}

	/**
	 * 删除用户信息
	 *
	 * @param phone
	 */
	public void delUserInfo(String phone) {
		try {
			Map<Object, Object> infoMap = getUserInfoFromCache(phone);
			if (infoMap != null && !infoMap.isEmpty()) {
				String status = (String) infoMap.get("info_status");
				String orderNo = (String) infoMap.get("order_no");
				String properties = (String) infoMap.get("user_info");
				logger.debug(String.format("删除用户(phone=%s):status=%s,orderNo=%s,properties=%s", phone, status, orderNo,
						properties));
				delUserInfoFromCache(phone);
			}
		} catch (Exception e) {
			logger.error("删除用户信息失败(phone=" + phone + "):" + e);
		}
	}

	/**
	 * 获取用户详情
	 *
	 * @param userId
	 * @return
	 */
	public JsonObject getUserDetail(long userId) {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			String properties = getUserProperty(user.getPhone(), "user_info");
			if (StringUtil.isRedisBlank(properties)) {
				throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "获取信息失败");
			} else {
				return GsonUtil.buildGson().fromJson(properties, JsonObject.class);
			}
		} catch (Exception e) {
			logger.error("获取信息失败：" + e);
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "获取信息失败");
		}
	}

	/**
	 * 保存同步失败的用户
	 *
	 * @param phone
	 * @param userInfo
	 */
	public void saveFailSyncCrmUser(String phone, String userInfo) {
		redisClient.setInMap(USER_SYNC_CRM_FAIL_PREFIX, phone, userInfo);
	}

	/**
	 * 删除同步成功的问题
	 *
	 * @param phone
	 */
	public void delSyncCrmUser(String phone) {
		redisClient.deleteFromMap(USER_SYNC_CRM_FAIL_PREFIX, phone);
	}

	/**
	 * 获取同步失败的用户信息
	 *
	 * @return
	 */
	public Map<Object, Object> getFailSyncCrmUser() {
		return redisClient.getMap(USER_SYNC_CRM_FAIL_PREFIX);
	}

	/**
	 * 获取用户信息的唯一Key
	 *
	 * @param phone
	 * @return
	 */
	public String getCacheKey(String phone) {
		return USER_INFO_PREFIX + "." + phone;
	}

	/**
	 * 获取用户缓存信息的状态
	 * @return
	 */
	public Integer getUserCacheStatus (long userId) {
		User user = getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}
		try {
			Map<Object, Object> infoMap = getUserInfoFromCache(user.getPhone());
			JsonObject properties = null;
			if (infoMap == null || infoMap.isEmpty()) {
				throw new BaseException(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "请先提交身份证信息");
			} else {
				int cacheStatus = Integer.valueOf(infoMap.get("info_status").toString());
				return cacheStatus;
			}
		} catch (Exception e) {
			logger.error("获取用户状态失败：" + e);
			return null;
		}
	}

	/**
	 * 同步缓存用户信息到数据库
	 * @param userId
	 * @param step
	 * @throws Exception
	 */
	public UserInfo syncUserInfoFromCacheToDB(long userId, int step) throws Exception{
		//User user = userRepository.findOne(userId);
		User user = userRepository.findByUseridAndState(userId,1);
		UserInfo userInfo = userInfoRepository.findByUserId(userId);
		if (userInfo == null) {
			userInfo = new UserInfo();
			userInfo.setUserId(userId);
			//userInfo.setUserMobile(user.getPhone());
			userInfo.setStep(step);
			userInfo.setCreateTime(new Date());
			userInfo.setUpdateTime(new Date());
		}
		Map<Object, Object> infoMap = getUserInfoFromCache(user.getPhone());
		logger.info("infoMap: " + infoMap);

		if (infoMap != null) {
			String cStatus = null;
			if (infoMap.containsKey("info_status") && infoMap.get("info_status") != null) {
				cStatus = infoMap.get("info_status").toString();
			}
			if (!StringUtil.isRedisBlank(cStatus) && step == UserInfo.USER_INFO_STATUS_SCANSIGNED) {
				step = Integer.valueOf(cStatus);
			}

			String userInfoStr = (String) infoMap.get("user_info");
			logger.info("userInfoStr: " + userInfoStr);

			Map<String, Object> map = GsonUtil.buildGson().fromJson(userInfoStr, new TypeToken<HashMap<String, Object>>() {
			}.getType());

			logger.info("map: " + map);

			String name = (String) map.get("name");
			String userIdNo = (String) map.get("user_id_no");
			String qqNo = (String) map.get("qq_no");
			String coAddress = (String) map.get("co_address");
			String coMail = (String) map.get("co_mail");
			String coName = (String) map.get("co_name");
			String position = (String) map.get("position");
			Object coPosiStatus_ = map.get("co_posi_status");
			Double coPosiStatus = null;
			if (coPosiStatus_ != null && !"".equals(coPosiStatus_.toString()) && !"null".equals(coPosiStatus_.toString())) {
				coPosiStatus = (Double) coPosiStatus_;
			}
			String coPosiName = (String) map.get("co_posi_name");
			Object revenueInfo_ = map.get("revenue_info");
			Double revenueInfo = null;
			if (revenueInfo_ != null && !"".equals(revenueInfo_.toString()) && !"null".equals(revenueInfo_.toString())) {
				revenueInfo = (Double) revenueInfo_;
			}

			String linkmanPhone = (String) map.get("linkman_phone");
			String linkmanName = (String) map.get("linkman_name");
			Object linkmanRelation_ = map.get("linkman_relation");
			Double linkmanRelation = null;
			if (linkmanRelation_ != null && !"".equals(linkmanRelation_.toString()) && !"null".equals(linkmanRelation_.toString())) {
				linkmanRelation = (Double) linkmanRelation_;
			}
			String userBankName = (String) map.get("bank_name");
			String userBankAccount = (String) map.get("name");
			String userCardNo = (String) map.get("bank_card_no");
			String userCardMobile = (String) map.get("bank_bind_phone");
			String idPosiName = (String) map.get("id_posi_name");
			String idNegName = (String) map.get("id_neg_name");
			String facePicName = (String) map.get("face_pic_name");

			userInfo.setName(name);
			switch (userIdNo.length()) {
				case 15:
					if (new Integer(userIdNo.charAt(14) + "") % 2 == 0) {
						userInfo.setUserSex(2);
					} else {
						userInfo.setUserSex(1);
					}
					break;
				case 18:
					if (new Integer(userIdNo.charAt(16) + "") % 2 == 0) {
						userInfo.setUserSex(2);
					} else {
						userInfo.setUserSex(1);
					}
					break;
				default:
					userInfo.setUserSex(1);
					break;
			}

			if (!StringUtil.isBlank(qqNo)) {
				userInfo.setQqNo(qqNo);
			}
			if (!StringUtil.isBlank(coAddress)) {
				userInfo.setUserCompanyAddress(coAddress);
			}
			if (!StringUtil.isBlank(coMail)) {
				userInfo.setUserCompanyMail(coMail);
			}
			if (!StringUtil.isBlank(coName)) {
				userInfo.setUserCompanyName(coName);
			}
			if (!StringUtil.isBlank(position)) {
				userInfo.setUserJob(position);
			}
			if (coPosiStatus != null) {
				userInfo.setUserWorkStatus(coPosiStatus.intValue());
			}
			if (!StringUtil.isBlank(coPosiName)) {
				userInfo.setUserCompanyCard(coPosiName);
			}
			if (revenueInfo != null) {
				userInfo.setUserIncome(revenueInfo.intValue());
			}
			if (!StringUtil.isBlank(userIdNo)) {
				userInfo.setUserIdNo(userIdNo);
			}
			if (!StringUtil.isBlank(linkmanPhone)) {
				userInfo.setUserLinkmanMobile(linkmanPhone);
			}
			if (!StringUtil.isBlank(linkmanName)) {
				userInfo.setUserLinkmanName(linkmanName);
			}
			if (linkmanRelation != null) {
				userInfo.setUserLinkmanRelation(linkmanRelation.intValue());
			}
			if (!StringUtil.isBlank(userBankName)) {
				userInfo.setUserBankName(userBankName);
			}
			if (!StringUtil.isBlank(userBankAccount)) {
				userInfo.setUserBankAccount(userBankAccount);
			}
			if (!StringUtil.isBlank(userCardNo)) {
				userInfo.setUserCardNo(userCardNo);
			}
			if (!StringUtil.isBlank(userCardMobile)) {
				userInfo.setUserCardMobile(userCardMobile);
			}
			if (!StringUtil.isBlank(idPosiName)) {
				userInfo.setIdPosiName(idPosiName);
			}
			if (!StringUtil.isBlank(idNegName)) {
				userInfo.setIdNegName(idNegName);
			}
			if (!StringUtil.isBlank(facePicName)) {
				userInfo.setFacePicName(facePicName);
			}

			userInfo.setStep(step);
			userInfo.setUpdateTime(new Date());
		}
		//update by arsion
		userInfo.setUserMobile(user.getPhone());
		logger.info(LogUtil.formatLog(String.format("保存用户详细信息:%s", userInfo)));
		return userInfoRepository.save(userInfo);
	}
}
