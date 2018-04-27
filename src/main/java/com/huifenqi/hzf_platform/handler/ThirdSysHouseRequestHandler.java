package com.huifenqi.hzf_platform.handler;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.AlibabaSysHouseConfiguration;
import com.huifenqi.hzf_platform.configuration.BaiduSysHouseConfiguration;
import com.huifenqi.hzf_platform.context.BdConstantsEnum;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.AliSysHouseDetailToLocalDto;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.entity.third.SaasApartmentInfo;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysCompany;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysFile;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysRecord;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUser;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUserRecord;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.controller.ThirdSysHouseCronller;
import com.huifenqi.hzf_platform.dao.ThirdSysDao;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysUserRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysUserRepository;
import com.huifenqi.hzf_platform.utils.HttpFileUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.ThirdSysUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest.Extrainfo;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest.RentAddressDto;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest.RentItemApiDto;
import com.taobao.api.request.AlibabaIdleHouseItemUpdateRequest;
import com.taobao.api.request.AlibabaIdleHouseMediaAddRequest;
import com.taobao.api.response.AlibabaIdleHouseItemAddResponse;
import com.taobao.api.response.AlibabaIdleHouseItemUpdateResponse;
import com.taobao.api.response.AlibabaIdleHouseMediaAddResponse;

/**
 * ClassName: HouseRequestHandler date: 2016年4月26日 下午4:40:45 Description:
 *
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Component
public class ThirdSysHouseRequestHandler {

	private static final Log logger = LogFactory.getLog(ThirdSysHouseRequestHandler.class);


	@Autowired
	private BaiduSysHouseConfiguration baiduSysHouseConfiguration;

	@Autowired
	private AlibabaSysHouseConfiguration alibabaSysHouseConfiguration;

	@Autowired
	private ThirdSysDao thirdSysDao;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;

	@Autowired
	private ThirdSysUserRepository thirdSysUserRepository;

	@Autowired
	private ThirdSysUserRecordRepository thirdSysUserRecordRepository;

	@Autowired
	private RedisCacheManager redisCacheManager;


	/**
	 * 初始化闲鱼用户发布数量
	 *
	 * @return
	 * @throws Exception
	 */
	public BdResponses initAliUser(HttpServletRequest request) {
		String appId = null;
		String companyId = null;
		String isClean = null;
		int count = 0;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
			companyId = RequestUtils.getParameterString(request, "companyId");
			isClean = RequestUtils.getParameterString(request, "isClean");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}



		try {
			String key = RedisUtils.getInstance().getKey(ThridSysConstants.ThirdSysUserUtil.THIRD_SYS_USER_COMPANY);
			if(isClean.equals(ThridSysConstants.ThirdSysUserUtil.IS_CLEAN_USER)){//清除公司所有账号
				redisCacheManager.delete(key+companyId);
			}

			//获取需要同步到第三方的房源
			List<ThirdSysUser> tsuList = thirdSysUserRepository.findInitUserByCompanyId(companyId);
			if(CollectionUtils.isNotEmpty(tsuList)){
				for(ThirdSysUser thirdSysUser : tsuList){
					//默认初始化账号数量
					count = ThridSysConstants.ThirdSysUserUtil.USER_MAX_FEED_COUNT;
					//根据用户统计发布房源数量
					int feedcount = thirdSysUserRecordRepository.findCountByUserId(thirdSysUser.getCompanyId(),thirdSysUser.getUserId());

					if(feedcount > 0){//重新收集账号可发布房源数量
						count = count-feedcount;
						redisCacheManager.removeAllList(key+thirdSysUser.getCompanyId(),thirdSysUser.getUserId());
					}

					if(count < 0 ){//账号使用次数超限
						logger.error(LogUtils.getCommLog(String.format("闲鱼账号异常使用次数超过限制,user:%s", thirdSysUser.getUserId())));
						continue;
					}
					//初始化账号数量
					for(int i = 1; i <= count;i++){
						redisCacheManager.leftPushList(key+thirdSysUser.getCompanyId(), thirdSysUser.getUserId());
					}

					//更新账号信息
					thirdSysUser.setIsUse(ThridSysConstants.ThirdSysUserUtil.IS_USER_USE_FINSH);
					thirdSysUser.setUserCount(feedcount);//账号使用次数
					thirdSysUserRepository.save(thirdSysUser);
				}
			}
			logger.info(LogUtils.getCommLog(String.format("初始化闲鱼账号到队列完成")));
		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.CREATE_USER_FAIL);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("初始化闲鱼账号到队列完成");
		return bdResponses;
	}

	/**
	 * 同步账号到闲鱼
	 *
	 * @return
	 * @throws Exception
	 */
	public BdResponses sysAliUser(HttpServletRequest request) {
		String appId = null;
		String companyId = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
			companyId = RequestUtils.getParameterString(request, "companyId");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		try {
			//获取需要同步到闲鱼的账号
			List<ThirdSysUser> tsuList = thirdSysUserRepository.findAllUserByCompanyId(companyId);
			if(CollectionUtils.isNotEmpty(tsuList)){
				for(ThirdSysUser thirdSysUser : tsuList){
					switch (thirdSysUser.getIsDelete()) {//账号状态
						case ThridSysConstants.ThirdSysUserUtil.IS_DELETE_USER_NO:
							addAliUser(thirdSysUser);
							break;
						case ThridSysConstants.ThirdSysUserUtil.IS_DELETE_USER_YES:
							delAliUser(thirdSysUser);
							break;
						default:
							logger.error(LogUtils.getCommLog(String.format("账号同步类型未知,isdelete:%s",thirdSysUser.getIsDelete())));
					}
				}
			}

			logger.info(LogUtils.getCommLog(String.format("同步闲鱼账户完成")));
		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("同步闲鱼账户完成");
		return bdResponses;
	}




	/**
	 * 发布图片到百度saas平台
	 *
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses imageUpload(HttpServletRequest request) {
		String appId = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		try {
			//获取请求url
			String url = ThirdSysUtil.getUrl(baiduSysHouseConfiguration.getImgUrl(),baiduSysHouseConfiguration.getAppId(),baiduSysHouseConfiguration.getAk());
			String picPath = "e:/1492767928296Hpt2O7R0.jpg";

			//获取请求参数
			Map<String,Object> param= ThirdSysUtil.getImgParam(picPath);

			//上传图片到百度sass平台
			String resPonse = HttpFileUtil.post(url, param);
			JSONObject myJsonObject = new JSONObject(resPonse);
			JSONObject myJsonStatus = new JSONObject(myJsonObject.getString("status"));
			JSONObject myJsonData = new JSONObject(myJsonObject.getString("data"));

			String picKey = null;
			//获取sass平台返回的图片key
			if(myJsonStatus.getString("code").equals(ThridSysConstants.BaiduUtil.IMG_UPLOAD_SUCCESS)){
				picKey = myJsonData.getString("pic_key");
			}
			logger.info(LogUtils.getCommLog(String.format("上传图片到百度sass平台完成")));
		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("上传图片到百度sass平台完成");
		return bdResponses;
	}


	/**
	 * 公寓信息发布到百度sass平台
	 *
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses apartmentApply(HttpServletRequest request) {
		String appId = null;
		String apartId = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
			apartId = RequestUtils.getParameterString(request, "apartId");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}

		if (apartId == null) {
			logger.error(LogUtils.getCommLog(String.format("公寓信息不能为空,apartId:%s", apartId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "公寓ID不能为空");
		}
		try {

			//获取需要审核的公寓信息
			SaasApartmentInfo saasApartmentInfo =thirdSysDao.findByApartId(apartId);
			if(saasApartmentInfo == null){
				logger.error(LogUtils.getCommLog(String.format("未找到公寓信息,apartId:%s", apartId)));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到对应的公寓信息");
			}
			//获取请求url
			String url = ThirdSysUtil.getUrl(baiduSysHouseConfiguration.getAtUrl(),baiduSysHouseConfiguration.getAppId(),baiduSysHouseConfiguration.getAk());
			//获取请求参数
			Map<String,String> param= ThirdSysUtil.getApartmentParam(saasApartmentInfo,baiduSysHouseConfiguration.getBackUrl());
			//上传公寓信息到百度sass平台
			String resPonse = HttpUtil.post(url, param);
			logger.info(LogUtils.getCommLog(String.format("上传公寓信息到百度sass平台返回结果:%s",resPonse)));

			JSONObject myJsonObject = new JSONObject(resPonse);
			JSONObject myJsonStatus = new JSONObject(myJsonObject.getString("status"));

			if(myJsonStatus.getString("code").equals(ThridSysConstants.BaiduUtil.APARTMENT_APPLY_SUCCESS)){
				saasApartmentInfo.setIsRun(ThridSysConstants.BaiduUtil.APARTMENT_IS_RUN);
				thirdSysDao.updateSaasApartmentInfo(saasApartmentInfo);
				logger.info(LogUtils.getCommLog(String.format("上传公寓信息到百度sass平台完成,等待saas平台回调")));
			}else{
				BdResponses bdResponses = new BdResponses();
				bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
				bdResponses.setMsg(myJsonStatus.getString("msg"));
				return bdResponses;
			}

		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("公寓信息上传成功");
		return bdResponses;
	}

	/**
	 * 百度sass平台回调
	 *
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses apartmentApplyBack(HttpServletRequest request) {
		String appId = null;
		String data = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
			data = RequestUtils.getParameterString(request, "data");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		if(data == null){
			logger.error(LogUtils.getCommLog(String.format("请求data:%s", data)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "请求data不能为空");
		}
		try {
			String resPonse = ThirdSysUtil.decrypt(data, baiduSysHouseConfiguration.getAesKey());
			JSONObject myJsonObject = new JSONObject(resPonse);

			//获取需要审核的公寓信息
			SaasApartmentInfo saasApartmentInfo =thirdSysDao.findByApartId(myJsonObject.getString("apart_id"));
			if(saasApartmentInfo == null){
				logger.error(LogUtils.getCommLog(String.format("未找到公寓信息,apartId:%s", myJsonObject.getString("apart_id"))));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到对应的公寓信息");
			}

			if(myJsonObject.getString("review_status").equals(ThridSysConstants.BaiduUtil.APARTMENT_BACK_SUCCESS)){//审核通过
				saasApartmentInfo.setSassId(myJsonObject.getString("saas_id"));
				saasApartmentInfo.setAk(myJsonObject.getString("ak"));
				saasApartmentInfo.setAppId(myJsonObject.getString("app_id"));
				saasApartmentInfo.setBackTime(new Date());
			}else{//审核不通过
				saasApartmentInfo.setSassId(myJsonObject.getString("saas_id"));
				saasApartmentInfo.setGiveBackReason(myJsonObject.getString("give_back_reason"));
				saasApartmentInfo.setBackTime(new Date());
			}

		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("");
		return bdResponses;
	}

	/**
	 * 同步找房房源到第三方
	 *
	 * @return
	 * @throws Exception
	 */
	public BdResponses thirdSysData(HttpServletRequest request) {
		String appId = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		try {
			//获取需要同步到第三方的房源
			List<ThirdSysRecord> tsrList = thirdSysDao.findByOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT);
			if(CollectionUtils.isNotEmpty(tsrList)){
				for(ThirdSysRecord thirdSysRecord : tsrList){
					switch (thirdSysRecord.getOptTargetCode()) {//目标方
						case ThridSysConstants.ChannelUtil.OPT_TARGER_CODE_BD:
							sysBaiDu(thirdSysRecord);
							break;
						case ThridSysConstants.ChannelUtil.OPT_TARGER_CODE_ALI:
							sysAlibabaHouse(thirdSysRecord);
							break;
						default:
							logger.error(LogUtils.getCommLog(String.format("渠道类型未知,optTargetCode:%s",thirdSysRecord.getOptTargetCode())));
					}

				}
			}
			logger.info(LogUtils.getCommLog(String.format("同步找房房源数据到第三方完成")));
		} catch (Exception e) {
			e.printStackTrace();
			BdResponses bdResponses = new BdResponses();
			bdResponses.setCode(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL);
			bdResponses.setMsg(e.toString());
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("同步找房房源数据到第三方完成");
		return bdResponses;
	}

	/**
	 * 同步找房房源到第三方
	 *
	 * @return
	 * @throws Exception
	 */
	public void thirdSysDataHistory(){
		try {
			//获取需要同步到第三方的房源
			List<ThirdSysRecord> tsrList = thirdSysDao.findByOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT);
			if(CollectionUtils.isNotEmpty(tsrList)){
				for(ThirdSysRecord thirdSysRecord : tsrList){
					switch (thirdSysRecord.getOptTargetCode()) {//目标方
						case ThridSysConstants.ChannelUtil.OPT_TARGER_CODE_BD:
							sysBaiDu(thirdSysRecord);
							break;
						case ThridSysConstants.ChannelUtil.OPT_TARGER_CODE_ALI:
							sysAlibabaHouse(thirdSysRecord);
							break;
						default:
							logger.error(LogUtils.getCommLog(String.format("渠道类型未知,optTargetCode:%s",thirdSysRecord.getOptTargetCode())));
					}

				}
			}
			logger.info(LogUtils.getCommLog(String.format("同步找房房源数据到第三方完成")));
		}catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 发布闲鱼账户
	 *
	 * @return
	 * @throws Exception
	 */
	public  void addAliUser(ThirdSysUser thirdSysUser){

		try {
			String rsp = thirdSysDao.getAliUserAddDto(thirdSysUser);
			if(rsp == null){
				logger.error(LogUtils.getCommLog(String.format("添加闲鱼账户异常,公司ID:%s, 闲鱼账户user:%s",thirdSysUser.getCompanyId(),thirdSysUser.getUserId())));
			}else{
				sysAliUserFinsh(rsp,thirdSysUser);
			}

		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼添加账号失败" + e.getMessage()));
			e.printStackTrace();
		}
	}

	/**
	 * 删除闲鱼账户
	 *
	 * @return
	 * @throws Exception
	 */
	public  void delAliUser(ThirdSysUser thirdSysUser){

		try {
			String rsp = thirdSysDao.getAliUserDelDto(thirdSysUser);
			if(rsp == null ){
				logger.error(LogUtils.getCommLog(String.format("删除闲鱼账户异常,公司ID:%s, 闲鱼账户user:%s",thirdSysUser.getCompanyId(),thirdSysUser.getUserId())));
			}else{
				//处理账号同步状态
				sysAliUserFinsh(rsp,thirdSysUser);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼删除账户失败" + e.getMessage()));
			e.printStackTrace();
		}
	}

	/**
	 * 发布房源同步更新百度
	 *
	 * @return
	 * @throws Exception
	 */
	public  String houseSubmit(String sellId,int roomId,int rentType,String ak,String saasAppId){
		String hsUrl = baiduSysHouseConfiguration.getHsUrl();
		Map<String,String> paramInsert = thirdSysDao.getHouseSubmit(sellId, roomId,rentType,ak,saasAppId);
		String hsResponse = null;
		if(paramInsert == null){
			return null;
		}
		try {
			hsResponse = HttpUtil.post(hsUrl, paramInsert);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("百度api发布房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return hsResponse;
	}

	/**
	 * 发布房源同步更新闲鱼
	 *
	 * @return
	 * @throws Exception
	 */
	public  String aliHouseSubmit(String sellId,int roomId,int rentType){
		String rsp = null;
		try {

			//1.获取需要发布的房源dto
			AliSysHouseDetailToLocalDto dto = thirdSysDao.getAliHouseSubmit(sellId, roomId,rentType);
			if(dto == null){
				logger.error(LogUtils.getCommLog(String.format("未找到需要发布的房源,房源编号:%s,房间编号:%s", sellId,roomId)));
				return null;
			}
			
			//2.判断房源发布渠道
            ThirdSysCompany thirdSysCompany = thirdSysDao.findBySourceAndCompanyId(dto.getSource(),dto.getCompanyId());
            if(thirdSysCompany == null){
                logger.error(LogUtils.getCommLog(String.format("该公司非白名单,发布房源受限,公司id:%s", dto.getCompanyId())));
                return null;
            }
            if(thirdSysCompany.getType() == ThridSysConstants.ThirdSysCompanyUtil.COMPANY_TYPE_INNER){
                dto.setCompanyId(ThridSysConstants.ThirdSysUserUtil.THIRD_SYS_USER_COMPANYID);//会分期公司
            }
			
			//3。校验公司队列是否有账号
			String key = RedisUtils.getInstance().getKey(ThridSysConstants.ThirdSysUserUtil.THIRD_SYS_USER_COMPANY);		
			String newKey = key+dto.getCompanyId();
			int submitCount = redisCacheManager.rangeCountList(newKey);
			if(submitCount == 0){
				logger.error(LogUtils.getCommLog(String.format("该公司下闲鱼账号发布次数受限,公司id:%s", dto.getCompanyId())));
				return null;
			}
			//4.从队列获取发布账号
			String userId = redisCacheManager.indexOneList(newKey);
			if(userId == null){
				logger.error(LogUtils.getCommLog(String.format("该公司下闲鱼账号发布次数受限,公司id:%s", dto.getCompanyId())));
				return null;
			}
			dto.setUserNick(userId);

			//5.验证图片是否发送过、已发送、获取图片ID
			List<String> imageList = dto.getImageHzfList();
			List<Long> addImageList = getImageIds(userId,imageList);
			dto.setImageIds(addImageList);

			//6.发布房源
			rsp = aliHouseSubmit(dto);

			if(rsp != null && !rsp.contains("error") ){//发布成功
				sysAliDataSuccess(dto,newKey,rsp);
			}

		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼发布房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return rsp;
	}

	/**
	 * 更新房源同步更新闲鱼
	 *
	 * @return
	 * @throws Exception
	 */
	public String aliRoomSingleModify(String sellId,int roomId,int rentType){
		String rsp = null;
		try {
			AliSysHouseDetailToLocalDto dto = thirdSysDao.getAliHouseSubmit(sellId, roomId,rentType);

			//1.获取需要更新的房源 闲鱼账户  房源ID
			ThirdSysUserRecord thirdSysUserRecord = thirdSysDao.findThirdSysUserRecordUsedId(sellId, roomId);

			if(thirdSysUserRecord == null){
				logger.error(LogUtils.getCommLog(String.format("未找到需要更新的房源,房源编号:%s,房间编号:%s", sellId,roomId)));
				return null;
			}
			dto.setUserNick(thirdSysUserRecord.getUserId());
			dto.setItemId(Long.valueOf(thirdSysUserRecord.getItemId()));

			//2.验证图片是否发送过、已发送、获取图片ID
			List<String> imageList = dto.getImageHzfList();
			List<Long> addImageList = getImageIds(dto.getUserNick(),imageList);
			dto.setImageIds(addImageList);
			
			//2.1闲鱼单方面下架房源
			if(thirdSysUserRecord.getIsOff() == ThridSysConstants.ThirdSysUserRecordUtil.IS_OFF_YES){//闲鱼下架
			    dto.setHouseStatus(Long.valueOf(Constants.HouseBase.STATUS_RENT));
			}
			
			//3.更新房源
			rsp = aliRoomSingleModify(dto);

			if(rsp!= null && !rsp.contains("error_response")){//更新房源成功
				//4.获取上次房态
				Long afterSatus = thirdSysUserRecord.getHouseStatus();

				//5.更新房源账号明细
				thirdSysUserRecord.setHouseStatus(dto.getHouseStatus());
				thirdSysDao.addThirdSysUserRecord(thirdSysUserRecord);
				

				//6.判断房源发布渠道
	            ThirdSysCompany thirdSysCompany = thirdSysDao.findBySourceAndCompanyId(dto.getSource(),dto.getCompanyId());
	            if(thirdSysCompany == null){
	                logger.error(LogUtils.getCommLog(String.format("该公司非白名单,更新房源受限,公司id:%s", dto.getCompanyId())));
	                return null;
	            }
	            if(thirdSysCompany.getType() == ThridSysConstants.ThirdSysCompanyUtil.COMPANY_TYPE_INNER){
	                dto.setCompanyId(ThridSysConstants.ThirdSysUserUtil.THIRD_SYS_USER_COMPANYID);//会分期公司
	            }
	            
				dto.setCompanyId(dto.getCompanyId());//外部公司
				String key = RedisUtils.getInstance().getKey(ThridSysConstants.ThirdSysUserUtil.THIRD_SYS_USER_COMPANY);
				String newKey = key+dto.getCompanyId();
				if(dto.getHouseStatus().longValue() == ThridSysConstants.ThirdSysUserUtil.STATUS_ALI_RENTED.longValue() &&
						ThridSysConstants.ThirdSysUserUtil.STATUS_ALI_WAIT.longValue()==afterSatus.longValue()){
					//7.增加账号到队列
					redisCacheManager.leftPushList(newKey, dto.getUserNick());
				}
				if(dto.getHouseStatus().longValue() == ThridSysConstants.ThirdSysUserUtil.STATUS_ALI_WAIT.longValue() &&
						afterSatus.longValue()  == ThridSysConstants.ThirdSysUserUtil.STATUS_ALI_RENTED.longValue()){
					//8.队列删除账号
					redisCacheManager.removeOneList(newKey, dto.getUserNick());
				}

			}

		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼更新房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return rsp;
	}

	/**
	 * 更新房源同步更新百度
	 *
	 * @return
	 * @throws Exception
	 */
	public String roomSingleModify(String sellId,int roomId,int rentType,String ak,String saasAppId){
		String url = baiduSysHouseConfiguration.getRsmUrl();
		String paramUpdate = thirdSysDao.getRoomSingleModify(sellId, roomId,rentType,ak,saasAppId);
		String rsmUrl =url+paramUpdate;
		String hsResponse = null;
		try {
			hsResponse = HttpUtil.get(rsmUrl, null);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("百度公寓更新房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return hsResponse;
	}

	/**
	 * 房源上架同步更新百度
	 *
	 * @return
	 * @throws Exception
	 */
	public String houseModifyUp(String sellId,int roomId,String ak,String saasAppId){
		String hmUrl = baiduSysHouseConfiguration.getHmUrl();
		Map<String,String> paramDel = thirdSysDao.getHouseModify(sellId,roomId,BdConstantsEnum.StatusEnum.PUT_AWAY.getIndex(),BdConstantsEnum.StatusEnum.PUT_AWAY.getName(),ak,saasAppId);
		String hmResponse = null;
		try {
			hmResponse = HttpUtil.post(hmUrl, paramDel);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("百度公寓上架房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return hmResponse;
	}

	/**
	 * 房源上架同步更新闲鱼
	 *
	 * @return
	 * @throws Exception
	 */
	public String aliHouseModifyUp(String sellId,int roomId,int rentType){
		return null;
	}

	/**
	 * 房源下架同步更新百度
	 *
	 * @return
	 * @throws Exception
	 */
	public String houseModifyDown(String sellId,int roomId,String ak,String saasAppId){
		String hmUrl = baiduSysHouseConfiguration.getHmUrl();
		Map<String,String> paramDel = thirdSysDao.getHouseModify(sellId,roomId,BdConstantsEnum.StatusEnum.SOLD_OUT.getIndex(),BdConstantsEnum.StatusEnum.SOLD_OUT.getName(),ak,saasAppId);
		String hmResponse = null;
		try {
			hmResponse = HttpUtil.post(hmUrl, paramDel);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("百度公寓下架房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		return hmResponse;
	}

	/**
	 * 闲鱼房源下架
	 *
	 * @return
	 * @throws Exception
	 */
	public String aliHouseModifyDown(String sellId,int roomId,int rentType){
		return null;
	}

	@Transactional
	public void sysBaiDu(ThirdSysRecord thirdSysRecord) throws JSONException{

		SaasApartmentInfo saasApartmentInfo = thirdSysDao.findByApartId(thirdSysRecord.getHouseSourceAppid());
		if(saasApartmentInfo == null || saasApartmentInfo.getAk() == null || saasApartmentInfo.getAppId() == null){
			logger.error(LogUtils.getCommLog(String.format("saas平台没有该公寓信息上传权限,apartId:%s", thirdSysRecord.getHouseSourceAppid())));
		}
		String ak = saasApartmentInfo.getAk();
		String sassAppId = saasApartmentInfo.getAppId();
		String sellId = thirdSysRecord.getHouseSellId();//房源ID
		int roomId = thirdSysRecord.getRoomId();//房间ID
		int rentType = thirdSysRecord.getHouseEntireRent();//出租类型
		int optType = thirdSysRecord.getOptType();//操作类型
		String response = null;
		switch (optType) {
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_INSERT:
				response = houseSubmit(sellId, roomId,rentType,ak,sassAppId);//发布房源
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UPDATE:
				response = roomSingleModify(sellId, roomId,rentType,ak,sassAppId);//更新房源
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UP:
				response = houseModifyUp(sellId, roomId,ak,sassAppId);//房源上架
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_DOWN:
				response = houseModifyDown(sellId, roomId,ak,sassAppId);//房源下架
				break;
			default:
				logger.error(LogUtils.getCommLog(String.format("操作类型错误,optType:%s",optType)));
		}

		if(response == null ){
			logger.error(LogUtils.getCommLog(String.format("同步房源信息失败,sellId:%s, roomId:%s",sellId,roomId)));
		}

		JSONObject myJsonObject  = new JSONObject(response);

		if(myJsonObject.getString("code").equals(ThridSysConstants.BaiduUtil.APARTMENT_APPLY_SUCCESS)){
			thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_SUCCESS);
			thirdSysRecord.setErrorMsg(myJsonObject.getString("msg"));
			if(optType == ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_INSERT){
				thirdSysRecord.setSuccessUrl(myJsonObject.getString("url"));
			}
			thirdSysDao.updateThirdSysRecord(thirdSysRecord);
		}else{
			thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_FARIL);
			thirdSysRecord.setErrorMsg(myJsonObject.getString("msg"));
			thirdSysDao.updateThirdSysRecord(thirdSysRecord);
		}

	}

	@Transactional
	public void sysAlibabaHouse(ThirdSysRecord thirdSysRecord) throws JSONException{
		String sellId = thirdSysRecord.getHouseSellId();//房源ID
		int roomId = thirdSysRecord.getRoomId();//房间ID
		int rentType = thirdSysRecord.getHouseEntireRent();//出租类型
		int optType = thirdSysRecord.getOptType();//操作类型
		String response = null;
		switch (optType) {
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_INSERT:
				response = aliHouseSubmit(sellId, roomId,rentType);//发布房源
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UPDATE:
				response = aliRoomSingleModify(sellId, roomId,rentType);//更新房源
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UP:
				response = aliRoomSingleModify(sellId, roomId,rentType);//房源上架
				break;
			case ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_DOWN:
				response = aliRoomSingleModify(sellId, roomId,rentType);//房源下架
				break;
			default:
				logger.error(LogUtils.getCommLog(String.format("操作类型错误,optType:%s",optType)));
		}

		if(response == null ){
			logger.error(LogUtils.getCommLog(String.format("同步房源信息异常,sellId:%s, roomId:%s",sellId,roomId)));
		}else{
			//同步房源完成处理后续事宜
			sysAliDateFinsh(response,thirdSysRecord);
		}

	}

	public boolean checkHouseEntire(ThirdSysRecord thirdSysRecord){
		if(thirdSysRecord == null){//非空校验
			return false;
		}
		if(thirdSysRecord.getHouseEntireRent() != Constants.HouseDetail.RENT_TYPE_ENTIRE){//整租校验
			return false;
		}
		return true;
	}

	/**
	 * 闲鱼发布房源
	 *
	 * @param AliSysHouseDetailToLocalDto dto
	 * @return String
	 */
	public String aliHouseSubmit(AliSysHouseDetailToLocalDto dto){
		String url = alibabaSysHouseConfiguration.getUrl();
		String appkey =alibabaSysHouseConfiguration.getAliAppkey();
		String secret = alibabaSysHouseConfiguration.getAliAppSecret();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaIdleHouseItemAddRequest req = new AlibabaIdleHouseItemAddRequest();
		RentItemApiDto obj1 = new RentItemApiDto();
		obj1.setAroundConfig(dto.getAroundConfig());
		obj1.setDecorateLevel(dto.getDecorateLevel());
		obj1.setRoomArea(dto.getRoomArea());
		obj1.setFace(dto.getFace());
		obj1.setDesc(dto.getDesc());
		obj1.setDeposit(dto.getDeposit());
		obj1.setTotalAreas(dto.getTotalAreas());
		obj1.setRoomClass(dto.getRoomClass());
		obj1.setHouseClass(dto.getHouseClass());
		obj1.setLivingRoomCnt(dto.getLivingRoomCnt());
		obj1.setRentType(dto.getRentType());
		obj1.setBedRoomCnt(dto.getBedRoomCnt());
		obj1.setTitle(dto.getTitle());
		obj1.setTotalFloor(dto.getTotalFloor());
		obj1.setSettlingTime(dto.getSettlingTime());
		obj1.setVideoId(dto.getVideoId());
		obj1.setRent(dto.getRent());
		obj1.setBuildingAddr(dto.getBuildingAddr());
		obj1.setBathRoomCnt(dto.getBathRoomCnt());
		obj1.setPayModePaid(dto.getPayModePaid());
		obj1.setRoomConfig(dto.getRoomConfig());
		obj1.setDetailAddress(dto.getDetailAddress());
		obj1.setFloor(dto.getFloor());
		obj1.setVideoCoverId(dto.getVideoCoverId());
		obj1.setHouseType(dto.getHouseType());
		obj1.setPayModePre(dto.getPayModePre());
		obj1.setRentMode(dto.getRentMode());
		obj1.setCommunityName(dto.getCommunityName());
		obj1.setImageIds(dto.getImageIds());
		obj1.setBathRoomType(dto.getBathRoomType());
		obj1.setHouseStatus(dto.getHouseStatus());
		RentAddressDto rentAddressDTO = new RentAddressDto();
		rentAddressDTO.setLat(dto.getLat());
		rentAddressDTO.setLng(dto.getLng());
		obj1.setRentAddressDTO(rentAddressDTO);
		Extrainfo obj2 = new Extrainfo();
		obj2.setExtra1("1");
		obj1.setExtraInfo(obj2);
		obj1.setUserNick(dto.getUserNick());
		req.setRentItemParam(obj1);
		AlibabaIdleHouseItemAddResponse rsp = null;
		try {
			logger.info(LogUtils.getCommLog("闲鱼api发布房源，请求信息" + req.getRentItemParam()));
			rsp = client.execute(req);
		} catch (ApiException e) {
			logger.error(LogUtils.getCommLog("闲鱼api发布房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		logger.error(LogUtils.getCommLog("闲鱼api发布房源,返回消息:" + rsp.getBody()));
		return rsp.getBody();
	}


	/**
	 * 闲鱼更新房源房源
	 *
	 * @param AliSysHouseDetailToLocalDto dto
	 * @return String
	 */
	public String aliRoomSingleModify(AliSysHouseDetailToLocalDto dto){
		String url = alibabaSysHouseConfiguration.getUrl();
		String appkey =alibabaSysHouseConfiguration.getAliAppkey();
		String secret = alibabaSysHouseConfiguration.getAliAppSecret();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaIdleHouseItemUpdateRequest req = new AlibabaIdleHouseItemUpdateRequest();
		AlibabaIdleHouseItemUpdateRequest.RentItemApiDto obj1 = new AlibabaIdleHouseItemUpdateRequest.RentItemApiDto();
		obj1.setAroundConfig(dto.getAroundConfig());
		obj1.setDecorateLevel(dto.getDecorateLevel());
		obj1.setRoomArea(dto.getRoomArea());
		obj1.setFace(dto.getFace());
		obj1.setDesc(dto.getDesc());
		obj1.setDeposit(dto.getDeposit());
		obj1.setRoomClass(dto.getRoomClass());
		obj1.setTotalAreas(dto.getTotalAreas());
		obj1.setHouseClass(dto.getHouseClass());
		obj1.setLivingRoomCnt(dto.getLivingRoomCnt());
		obj1.setRentType(dto.getRentType());
		obj1.setRentMode(dto.getRentMode());
		obj1.setBedRoomCnt(dto.getBedRoomCnt());
		obj1.setTitle(dto.getTitle());
		obj1.setTotalFloor(dto.getTotalFloor());
		obj1.setSettlingTime(dto.getSettlingTime());
		obj1.setVideoId(dto.getVideoId());
		obj1.setRent(dto.getRent());
		obj1.setBuildingAddr(dto.getBuildingAddr());
		obj1.setBathRoomCnt(dto.getBathRoomCnt());
		obj1.setPayModePaid(dto.getPayModePaid());
		obj1.setRoomConfig(dto.getRoomConfig());
		obj1.setDetailAddress(dto.getDetailAddress());
		obj1.setFloor(dto.getFloor());
		obj1.setVideoCoverId(dto.getVideoCoverId());
		obj1.setHouseType(dto.getHouseType());
		obj1.setPayModePre(dto.getPayModePre());
		obj1.setRentMode(dto.getRentMode());
		obj1.setCommunityName(dto.getCommunityName());
		obj1.setImageIds(dto.getImageIds());
		obj1.setBathRoomType(dto.getBathRoomType());
		obj1.setHouseStatus(dto.getHouseStatus());
		obj1.setItemId(dto.getItemId());//房源编号
		obj1.setUserNick(dto.getUserNick());

		AlibabaIdleHouseItemUpdateRequest.RentAddressDto rentAddressDTO = new AlibabaIdleHouseItemUpdateRequest.RentAddressDto();
		rentAddressDTO.setLat(dto.getLat());
		rentAddressDTO.setLng(dto.getLng());
		obj1.setRentAddressDTO(rentAddressDTO);
		AlibabaIdleHouseItemUpdateRequest.Extrainfo obj2 = new AlibabaIdleHouseItemUpdateRequest.Extrainfo();
		obj2.setExtra1("扩展信息");
		obj1.setExtraInfo(obj2);
		req.setRentItemParam(obj1);
		AlibabaIdleHouseItemUpdateResponse rsp = null;
		try {
			logger.info(LogUtils.getCommLog("闲鱼api更新房源，请求信息" + req.getRentItemParam()));
			rsp = client.execute(req);
		} catch (ApiException e) {
			logger.error(LogUtils.getCommLog("闲鱼api更新房源失败" + e.getMessage()));
			e.printStackTrace();
		}
		logger.info(LogUtils.getCommLog("闲鱼api更新房源返回消息:" + rsp.getBody()));

		return rsp.getBody();
	}
	/**
	 * 闲鱼上传文件
	 *
	 * @param userId picUrl
	 * @return String rsp
	 */
	public String aliMediaAdd(String userId,String picUrl){
		AlibabaIdleHouseMediaAddResponse rsp = null;
		String url = alibabaSysHouseConfiguration.getUrl();
		String appkey =alibabaSysHouseConfiguration.getAliAppkey();
		String secret = alibabaSysHouseConfiguration.getAliAppSecret();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaIdleHouseMediaAddRequest req = new AlibabaIdleHouseMediaAddRequest();
		req.setMediaData(new FileItem(picUrl));
		req.setMediaType(0L);
		req.setUserNick(userId);
		try {
			logger.info(LogUtils.getCommLog("闲鱼api上传文件完成，请求信息" + "用户信息："+userId+"文件地址："+picUrl));
			rsp = client.execute(req);
		} catch (ApiException e) {
			logger.error(LogUtils.getCommLog("闲鱼api上传文件失败" + e.getMessage()));
			e.printStackTrace();
		}
		logger.info(LogUtils.getCommLog("闲鱼api上传文件完成，返回信息" + rsp.getBody()));
		return rsp.getBody();
	}

	/**
	 * 获取需要上传的闲鱼上传文件
	 *
	 * @param userId picUrl
	 * @return String rsp
	 */
	public   List<Long> getImageIds(String userId,List<String> imgsUrl ){
		String imgrsp = null;
		List<Long> imageIds = new ArrayList<Long>();
		try {
			if(CollectionUtils.isNotEmpty(imgsUrl)){
				for(String image :imgsUrl){
					//1验证图片是否发送过
					ThirdSysFile  thirdSysFile = thirdSysDao.findAllByUserIdandImgUrl(userId,image);
					if(thirdSysFile != null){
						imageIds.add(Long.valueOf(thirdSysFile.getFileId()));
					}else{
						//2.（图片未发送）下载并
						File localImg = ThirdSysUtil.downImageNew(image);

						if(localImg != null){
							//3.发布图片
							imgrsp = aliMediaAdd(userId,localImg.toString());
						}

						//删除本地图片
						localImg.delete();

						//4.发送成功
						if(imgrsp !=null && !imgrsp.contains("error_response")){
							Long imageId = mediaAddSuccess(userId,image,imgrsp);
							if(imageId != null){
								imageIds.add(imageId);
							}
						}else{
							logger.error(LogUtils.getCommLog("闲鱼上传文件失败" + imgrsp+"图片url："+image+"下载文件路径："+localImg.toString()));
						}

					}
				}
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼上传文件记录失败" + e.getMessage()));
			e.printStackTrace();
		}
		return imageIds;
	}

	public void sysAliUserFinsh(String rsp,ThirdSysUser thirdSysUser){
		try {
			if(!rsp.contains("error_response")){//添加账号成功
				//更新账号信息
				Date date = new Date();
				thirdSysUser.setUpdateTime(date);
				thirdSysUser.setErrorCode("success");
				thirdSysUser.setErrorMsg("true");
				thirdSysUser.setStatus(ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_SUCCESS);
				thirdSysUserRepository.save(thirdSysUser);
			} else{//添加账号失败
				//同步失败
				JSONObject responseObject  = new JSONObject(rsp);
				JSONObject errorResponseObject  = new JSONObject(responseObject.getString("error_response"));
				Date date = new Date();
				thirdSysUser.setUpdateTime(date);
				thirdSysUser.setStatus(ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_FARIL);
				thirdSysUser.setErrorMsg(errorResponseObject.getString("sub_msg"));
				thirdSysUser.setErrorCode(errorResponseObject.getString("sub_code"));
				thirdSysUserRepository.save(thirdSysUser);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼账号添加成功后，更新账号记录信息失败" + e.getMessage()));
			e.printStackTrace();
		}
	}

	public void sysAliDateFinsh(String rsp,ThirdSysRecord thirdSysRecord){
		try {
			Date updateTime = new Date();
			logger.info(LogUtils.getCommLog(String.format("同步房源信息完成,sellId:%s, roomId:%s",thirdSysRecord.getHouseSellId(),thirdSysRecord.getRoomId())));


			if(rsp != null && rsp.contains("error_response")){ //同步失败
				JSONObject responseObject  = new JSONObject(rsp);
				JSONObject errorResponseObject  = new JSONObject(responseObject.getString("error_response"));
				thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_FARIL);//更新同步状态
				thirdSysRecord.setErrorMsg(errorResponseObject.getString("sub_msg"));
				thirdSysRecord.setErrorCode(errorResponseObject.getString("sub_code"));
				thirdSysRecord.setUpdateTime(updateTime);
				thirdSysDao.updateThirdSysRecord(thirdSysRecord);
			}if(rsp != null && !rsp.contains("error_response")){
//              {"alibaba_idle_house_item_add_response":{"result":{"error_code":"ADDRESS_PARAMS_ERROR","error_msg":"输入地址参数错误",
//              "success":true},"request_id":"rxnh00nu1qdd"}}

				JSONObject responseObject  = new JSONObject(rsp);
				JSONObject successResponseObject = new JSONObject();
				//默认设置成功
				if(thirdSysRecord.getOptType() == ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_INSERT){
					successResponseObject  = new JSONObject(responseObject.getString(alibabaSysHouseConfiguration.getItemAdd()+"_response"));
				}else{
					successResponseObject  = new JSONObject(responseObject.getString(alibabaSysHouseConfiguration.getItemUpdate()+"_response"));
				}

				if(successResponseObject.getString("result").contains("error")){
					JSONObject resultResponseObject  = new JSONObject(successResponseObject.getString("result"));
					thirdSysRecord.setErrorCode(resultResponseObject.getString("error_code"));
					thirdSysRecord.setErrorMsg(resultResponseObject.getString("error_msg"));
					thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_FARIL);
				}else{
					thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_SUCCESS);
				}

				thirdSysRecord.setUpdateTime(updateTime);
				thirdSysDao.updateThirdSysRecord(thirdSysRecord);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼发布房源后，更新记录信息失败" + e.getMessage()));
			e.printStackTrace();
		}
	}

	public void sysAliDataSuccess(AliSysHouseDetailToLocalDto dto,String newKey,String rsp){
		try {
			logger.info(LogUtils.getCommLog(String.format("同步房源信息完成,sellId:%s, roomId:%s",dto.getSellId(),dto.getRoomId())));
			JSONObject responseObject  = new JSONObject(rsp);
			JSONObject itemAddResponseObject  = new JSONObject(responseObject.getString(alibabaSysHouseConfiguration.getItemAdd()+"_response"));
			JSONObject resultResponseObject  = new JSONObject(itemAddResponseObject.getString("result"));

			//6.记录发布房源账号明细
			ThirdSysUserRecord thirdSysUserRecord = new ThirdSysUserRecord();
			thirdSysUserRecord.setCompanyId(dto.getCompanyId());
			thirdSysUserRecord.setHouseSellId(dto.getSellId());
			thirdSysUserRecord.setRoomId(dto.getRoomId());
			thirdSysUserRecord.setHouseEntire(dto.getRentMode());
			thirdSysUserRecord.setHouseStatus(dto.getHouseStatus());
			thirdSysUserRecord.setStatus(ThridSysConstants.ThirdSysUserRecordUtil.ThIRd_SYS_STATUS_SUCCESS);
			thirdSysUserRecord.setUserId(dto.getUserNick());
			thirdSysUserRecord.setItemId(resultResponseObject.getString("data"));
			thirdSysDao.addThirdSysUserRecord(thirdSysUserRecord);
			//7.移除公司队列内对应账号
			redisCacheManager.removeOneList(newKey, dto.getUserNick());
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼发布房源成功后，更新发布记录信息失败" + e.getMessage()));
			e.printStackTrace();
		}
	}

	public Long mediaAddSuccess(String userId,String imgurl,String rsp){
		try {
			JSONObject responseObject  = new JSONObject(rsp);
			JSONObject mediaResponseObject  = new JSONObject(responseObject.getString("alibaba_idle_house_media_add_response"));
			JSONObject resultResponseObject  = new JSONObject(mediaResponseObject.getString("result"));

			//4.2 记录发布图片信息
			ThirdSysFile inserthirdSysFile = new ThirdSysFile();
			inserthirdSysFile.setUserId(userId);
			inserthirdSysFile.setFileId(String.valueOf(resultResponseObject.getLong("data")));
			inserthirdSysFile.setPicRootPath(imgurl);
			thirdSysDao.addThirdSysFile(inserthirdSysFile);
			return resultResponseObject.getLong("data");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("闲鱼文件上传成功后，更新文件记录信息失败" + e.getMessage()));
			e.printStackTrace();
		}
		return null;
	}

}


