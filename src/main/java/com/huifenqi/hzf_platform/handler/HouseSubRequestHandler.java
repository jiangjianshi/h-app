/** 
 * Project Name: hzf_platform 
 * File Name: HouseSubRequestHandler.java 
 * Package Name: com.huifenqi.hzf_platform.handler 
 * Date: 2017年8月8日 上午11:23:45 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.SearchConfiguration;
import com.huifenqi.hzf_platform.context.dto.params.CustomRentingDto;
import com.huifenqi.hzf_platform.context.entity.house.ActivityAgency;
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.context.entity.house.CustomCityPrice;
import com.huifenqi.hzf_platform.context.entity.house.CustomRenting;
import com.huifenqi.hzf_platform.context.entity.house.FootmarkHistory;
import com.huifenqi.hzf_platform.context.entity.house.SlideShowUrl;
import com.huifenqi.hzf_platform.context.enums.HouseTagsEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.HouseSubDao;
import com.huifenqi.hzf_platform.dao.repository.house.ActivityAgencyRepository;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CustomCityPriceRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CustomRentingRepository;
import com.huifenqi.hzf_platform.utils.BeanMapperUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.QueryDetailVo;

/**
 * ClassName: HouseSubRequestHandler date: 2017年8月8日 上午11:23:45 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Service
public class HouseSubRequestHandler {

	private static final Log logger = LogFactory.getLog(HouseSubRequestHandler.class);

	@Autowired
	private HouseSubDao houseSubDao;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private AgencyManageRepository agencyManageRepository;

	@Autowired
	private ActivityAgencyRepository activityAgencyRepository;

	@Autowired
	private CustomRentingRepository customRentingRepository;

	@Autowired
	private CustomCityPriceRepository customCityPriceRepository;
	
	@Resource
	private RedisCacheManager redisCacheManager;
	
	@Autowired
	private SearchConfiguration searchConfiguration;
	
	//开屏页配置在Redis中的key
	private static String OPENPAGE_DISPLAYKEY = "openPage:isDisplay";
	private static String OPENPAGE_DURATION = "openPage:displayDuration";

	/**
	 * @Title: getBizNameList
	 * @Description: “热门搜索” 展示本城市房源最多的商圈名TOP12
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月8日 下午2:03:36
	 */
	public Responses getBizNameList(HttpServletRequest request) throws Exception {
		Map<String, String> returnMap = new HashMap<String, String>();
		long cityId = RequestUtils.getParameterLong(request, "cityId");
		String bizNameString = houseSubDao.getBizNameList(cityId);
		returnMap.put("bizNameString", bizNameString);
		Responses responses = new Responses(returnMap);
		return responses;
	}

	/**
	 * @Title: addFootmarkHistory
	 * @Description: 添加浏览房源足迹记录(请求参数包括：房源字段必填，房间字段(存在 浏览的是房间，否则浏览的是房源)，用户ID)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午2:43:04
	 */
	public Responses addFootmarkHistory(HttpServletRequest request) throws Exception {
		String sellId = RequestUtils.getParameterString(request, "sellId");
		int roomId = RequestUtils.getParameterInt(request, "roomId", 0);
		long userId = sessionManager.getUserIdFromSession();
		// long userId = RequestUtils.getParameterLong(request, "userId", 0);
		// 客户端浏览足迹ID 作为返回标识
		long footmarkHistoryId = 0;
		// 先查询当前用户的houseId和homeId下是否存在浏览房源足迹记录，如果存在 执行更新；否则：判断条数是否大于20条，如果不大于
		// 执行插入；否则 执行更新最旧的一条数据
		List<FootmarkHistory> footmarkHistoryList = new ArrayList<FootmarkHistory>();
		FootmarkHistory footmarkHistory = null;
		try {
			footmarkHistoryList = houseSubDao.getFootmarkHistory(userId, sellId, roomId);
			if (CollectionUtils.isNotEmpty(footmarkHistoryList)) {
				footmarkHistory = footmarkHistoryList.get(0);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("数据解析失败" + e.getMessage()));
			if (e instanceof BaseException) {
				throw (BaseException) e;
			}
		}
		if (footmarkHistory != null) {
			// 执行更新：更新时间操作
			try {
				footmarkHistoryId = houseSubDao.updateFootmarkHistory(footmarkHistory.getId(), userId, sellId, roomId);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog("浏览房源足迹记录更新失败" + e.getMessage()));
				throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL, "浏览房源足迹记录更新失败");
			}
		} else {
			// 判断条数是否大于20条，如果小于 执行插入；否则 执行更新最旧的一条数据
			List<FootmarkHistory> footmarkList = houseSubDao.getCountByUserId(userId);
			if ((CollectionUtils.isEmpty(footmarkList) || footmarkList.size() < 20)) {
				footmarkHistory = new FootmarkHistory();
				footmarkHistory.setUserId(userId);
				footmarkHistory.setSellId(sellId);
				footmarkHistory.setRoomId(roomId);
				footmarkHistory.setState(1);
				try {
					footmarkHistoryId = houseSubDao.saveFootmarkHistory(footmarkHistory);
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("浏览房源足迹记录保存失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_FMH_UPDATE_FAIL, "浏览房源足迹记录保存失败");
				}
			} else {
				footmarkHistoryId = houseSubDao.updateFootmarkHistory(footmarkList.get(0).getId(), userId, sellId,
						roomId);
				try {
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("浏览房源足迹记录更新失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL, "浏览房源足迹记录更新失败");
				}
			}
		}
		Responses responses = new Responses();
		Map<String, Long> returnMap = new HashMap<String, Long>();
		returnMap.put("footmarkHistoryId", footmarkHistoryId);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: deleteFootmarkHistory
	 * @Description: 删除浏览房源足迹数据
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月22日 下午5:44:32
	 */
	public Responses deleteFootmarkHistory(HttpServletRequest request) throws Exception {
		String sellId = RequestUtils.getParameterString(request, "sellId");
		int roomId = RequestUtils.getParameterInt(request, "roomId", 0);
		long userId = sessionManager.getUserIdFromSession();
		// long userId = RequestUtils.getParameterLong(request, "userId", 0);
		long footmarkHistoryId = 0;
		// 执行更新：删除浏览房源足迹操作
		try {
			houseSubDao.deleteFootmarkHistory(userId, sellId, roomId);
			footmarkHistoryId = 1;
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("浏览房源足迹记录删除失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_FMH_UPDATE_FAIL, "浏览房源足迹记录删除失败");
		}
		Responses responses = new Responses();
		Map<String, Long> returnMap = new HashMap<String, Long>();
		returnMap.put("footmarkHistoryId", footmarkHistoryId);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: getSlideshowUrl
	 * @Description: 获取主页(会找房和会分期)的轮播图
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午10:10:29
	 */
	public Responses getSlideshowUrl() throws Exception {
		Map<String, Object> returnMap = Maps.newHashMap();
		// 查询轮播图（包含首页会找房和会分期轮播图）
		List<SlideShowUrl> slideShowUrlList = houseSubDao.getSlideShowUrlList();
		// 查询公寓表中参加活动的公寓数据
		List<Agency> agencyList = agencyManageRepository.findActivityAgency();
		if (CollectionUtils.isNotEmpty(agencyList)) {
			// 当参加活动的公寓数量>=6时，随机取六条数据；否则会找房默认banner展示在最后
			if (agencyList.size() >= 6) {
				JSONArray fangArray = new JSONArray();
				JSONArray stagesArray = new JSONArray();
				for (int i = 0; i < 6; i++) {
					int randomNum = (int) (Math.random() * agencyList.size());
					Agency agency = agencyList.get(randomNum);
					JSONObject fangObject = new JSONObject();
					fangObject.put("imgUrl", agency.getPicSxPath());
					fangObject.put("pxImgUrl", agency.getPxPicPath());
					fangObject.put("destinationUrl", agency.getDestinationUrl());
					fangObject.put("pageName", agency.getPageName());
					fangObject.put("companyId", agency.getCompanyId());
					fangArray.add(fangObject);
					agencyList.remove(randomNum);
				}
				if (CollectionUtils.isNotEmpty(slideShowUrlList)) {
					for (SlideShowUrl slideShowUrl : slideShowUrlList) {
						int imageType = slideShowUrl.getImageType();
						if (imageType == 2) {// 2：会分期轮播图
							JSONObject stagesObject = new JSONObject();
							stagesObject.put("imgUrl", slideShowUrl.getImageurl());
							stagesObject.put("destinationUrl", slideShowUrl.getDestinationUrl());
							stagesObject.put("pageName", slideShowUrl.getPageName());
							stagesArray.add(stagesObject);
						}
					}
				}
				returnMap.put("fangDetail", fangArray);
				returnMap.put("stagesDetail", stagesArray);
			} else {
				JSONArray fangArray = new JSONArray();
				JSONArray stagesArray = new JSONArray();
				for (int i = 0; i < agencyList.size(); i++) {
					Agency agency = agencyList.get(i);
					JSONObject fangObject = new JSONObject();
					fangObject.put("imgUrl", agency.getPicSxPath());
					fangObject.put("pxImgUrl", agency.getPxPicPath());
					fangObject.put("destinationUrl", agency.getDestinationUrl());
					fangObject.put("pageName", agency.getPageName());
					fangObject.put("companyId", agency.getCompanyId());
					fangArray.add(fangObject);
				}
				if (CollectionUtils.isNotEmpty(slideShowUrlList)) {
					for (SlideShowUrl slideShowUrl : slideShowUrlList) {
						int imageType = slideShowUrl.getImageType();
						if (imageType == 1) {// 1：会找房轮播图
							JSONObject fangObject = new JSONObject();
							fangObject.put("imgUrl", slideShowUrl.getImageurl());
							fangObject.put("pxImgUrl", slideShowUrl.getPxImageurl());
							fangObject.put("destinationUrl", slideShowUrl.getDestinationUrl());
							fangObject.put("pageName", slideShowUrl.getPageName());
							fangObject.put("companyId", "");
							fangArray.add(fangObject);
						} else if (imageType == 2) {// 2：会分期轮播图
							JSONObject stagesObject = new JSONObject();
							stagesObject.put("imgUrl", slideShowUrl.getImageurl());
							stagesObject.put("destinationUrl", slideShowUrl.getDestinationUrl());
							stagesObject.put("pageName", slideShowUrl.getPageName());
							stagesArray.add(stagesObject);
						}
					}
				}
				returnMap.put("fangDetail", fangArray);
				returnMap.put("stagesDetail", stagesArray);
			}
		} else {
			if (CollectionUtils.isNotEmpty(slideShowUrlList)) {
				JSONArray fangArray = new JSONArray();
				JSONArray stagesArray = new JSONArray();
				for (SlideShowUrl slideShowUrl : slideShowUrlList) {
					int imageType = slideShowUrl.getImageType();
					if (imageType == 1) {// 1：会找房轮播图
						JSONObject fangObject = new JSONObject();
						fangObject.put("imgUrl", slideShowUrl.getImageurl());
						fangObject.put("pxImgUrl", slideShowUrl.getPxImageurl());
						fangObject.put("destinationUrl", slideShowUrl.getDestinationUrl());
						fangObject.put("pageName", slideShowUrl.getPageName());
						fangObject.put("companyId", "");
						fangArray.add(fangObject);
					} else if (imageType == 2) {// 2：会分期轮播图
						JSONObject stagesObject = new JSONObject();
						stagesObject.put("imgUrl", slideShowUrl.getImageurl());
						stagesObject.put("destinationUrl", slideShowUrl.getDestinationUrl());
						stagesObject.put("pageName", slideShowUrl.getPageName());
						stagesArray.add(stagesObject);
					}
				}
				returnMap.put("fangDetail", fangArray);
				returnMap.put("stagesDetail", stagesArray);
			}
		}
		//开屏页可配置
		Map<String, Object> openObj = new HashMap<>();
		for (SlideShowUrl slideShowUrl : slideShowUrlList) {
			int imageType = slideShowUrl.getImageType();
			if (imageType == 3) {// 1：开屏页轮播图
				openObj.put("imgUrl", slideShowUrl.getImageurl());
				openObj.put("destinationUrl", slideShowUrl.getDestinationUrl());
				openObj.put("pageName", slideShowUrl.getPageName());
			}
		}
		
		String displayValue = redisCacheManager.getValue(OPENPAGE_DISPLAYKEY);
		String durationValue = redisCacheManager.getValue(OPENPAGE_DURATION);
		int isDisplay = StringUtils.isEmpty(displayValue) ? 0 : Integer.parseInt(displayValue);// 默认为0 不展示
		int duration = StringUtils.isEmpty(durationValue) ? 3 : Integer.parseInt(durationValue);// 默认为3秒
		ImmutableMap<String, Object> openMap = ImmutableMap.of("isDisplay", isDisplay, "time", duration, "openDetail", openObj);
		returnMap.put("openPage", openMap);
		Responses responses = new Responses(returnMap);
		return responses;
	}

	/**
	 * @Title: operateAgency
	 * @Description: 操作品牌公寓数据
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月14日 下午2:56:18
	 */
	public Responses operateAgency(HttpServletRequest request) throws Exception {
		int operateFlag = 0;
		String agencyId = RequestUtils.getParameterString(request, "companyId");
		// 通过中介公司ID去房源表中统计包含的城市
		List<Long> cityIdList = new ArrayList<Long>();
		List<QueryDetailVo> voList = houseSubDao.getCityIdListByAgengcyId(agencyId);
		if (CollectionUtils.isNotEmpty(voList)) {
			for (int i = 0; i < voList.size(); i++) {
				long cityId = voList.get(i).getCityId();
				if (cityId != 0) {
					cityIdList.add(cityId);
				}
			}
		}
		// 获取当前中介公司对应品牌公寓下的所有数据
		List<Agency> agencyList = agencyManageRepository.queryListByAgencyId(agencyId);
		// 通过中介公司ID搜索其对应的所有数据：如果不存在 则执行遍历便利入库操作；
		if (CollectionUtils.isEmpty(agencyList)) {
			// 循环保存品牌公寓数据
			for (int i = 0; i < cityIdList.size(); i++) {
				Agency agency = new Agency();
				String agencyName = RequestUtils.getParameterString(request, "companyName");
				String picRootPath = RequestUtils.getParameterString(request, "picRootPath");
				String picWebPath = RequestUtils.getParameterString(request, "picWebPath");
				Date createTime = new Date();
				agency.setCityId(cityIdList.get(i));
				agency.setCompanyId(agencyId);
				agency.setCompanyName(agencyName);
				agency.setPicRootPath(picRootPath);
				agency.setPicWebPath(picWebPath);
				agency.setCreateTime(createTime);
				agency.setUpdateTime(createTime);
				agencyManageRepository.save(agency);
			}
			operateFlag = 1;
			// 否则：1、通过中介公司ID去房源表中统计包含的城市，统计到的城市ID集合完全包含当前库中当前中介公司对应的城市ID，则执行插入；
			// 2、如果统计到的城市ID集合包含部分当前中介公司对应的城市ID，则要先把“包含部分”之外的城市ID对应的数据删除，然后再执行插入。
		} else {
			for (int i = 0; i < agencyList.size(); i++) {
				long cityIdTemp = agencyList.get(i).getCityId();
				// 删除不包含在cityIdList中的品牌公寓数据；移除cityidList中当前cityId
				if (!cityIdList.contains(cityIdTemp)) {
					if (!"-1".equals(agencyList.get(i).getCompanyId()) && agencyList.get(i).getCityId() != -1) {
						agencyManageRepository.delete(agencyList.get(i));
					}
					cityIdList.remove(cityIdTemp);
				} else {
					cityIdList.remove(cityIdTemp);
				}
			}
			// 遍历剩下的cityidList中的cityId，执行保存
			if (CollectionUtils.isNotEmpty(cityIdList)) {
				for (int i = 0; i < cityIdList.size(); i++) {
					Agency agency = new Agency();
					String agencyName = RequestUtils.getParameterString(request, "companyName");
					String picRootPath = RequestUtils.getParameterString(request, "picRootPath");
					String picWebPath = RequestUtils.getParameterString(request, "picWebPath");
					Date createTime = new Date();
					agency.setCityId(cityIdList.get(i));
					agency.setCompanyId(agencyId);
					agency.setCompanyName(agencyName);
					agency.setPicRootPath(picRootPath);
					agency.setPicWebPath(picWebPath);
					agency.setCreateTime(createTime);
					agency.setUpdateTime(createTime);
					agencyManageRepository.save(agency);
				}
			}
			operateFlag = 1;
		}

		Responses responses = new Responses();
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		returnMap.put("operateFlag", operateFlag);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: getAgencyActivity
	 * @Description: 通过品牌公寓ID获取对应的活动详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月28日 下午2:26:58
	 */
	public Responses getActivityAgency(HttpServletRequest request) throws Exception {
		String companyId = RequestUtils.getParameterString(request, "companyId");
		// long cityId = RequestUtils.getParameterLong(request, "cityId");
		ActivityAgency agencyActivity = activityAgencyRepository.getActivityAgency(companyId);
		if (agencyActivity == null) {
			return new Responses(ErrorMsgCode.ERROR_MSG_QUERY_ACTIVITY_FAIL, "活动数据不存在");
		}
		// 拼接返回数据
		JSONObject jsonData = new JSONObject();
		jsonData.put("companyId", agencyActivity.getCompanyId());
		jsonData.put("pageName", agencyActivity.getPageName());
		jsonData.put("activityName", agencyActivity.getActivityName());
		jsonData.put("activityDesc", agencyActivity.getActivityDesc());
		jsonData.put("cityId", agencyActivity.getCityId());
		jsonData.put("companyName", agencyActivity.getCompanyName());
		jsonData.put("companyDesc", agencyActivity.getCompanyDesc());
		jsonData.put("companyLogo", agencyActivity.getCompanyLogo());
		JSONArray jsonArray = new JSONArray();
		if (StringUtil.isNotBlank(agencyActivity.getImgUrl1())) {
			jsonArray.add(agencyActivity.getImgUrl1());
		}
		if (StringUtil.isNotBlank(agencyActivity.getImgUrl2())) {
			jsonArray.add(agencyActivity.getImgUrl2());
		}
		if (StringUtil.isNotBlank(agencyActivity.getImgUrl3())) {
			jsonArray.add(agencyActivity.getImgUrl3());
		}
		if (StringUtil.isNotBlank(agencyActivity.getImgUrl4())) {
			jsonArray.add(agencyActivity.getImgUrl4());
		}
		if (StringUtil.isNotBlank(agencyActivity.getImgUrl5())) {
			jsonArray.add(agencyActivity.getImgUrl5());
		}
		jsonData.put("imgUrl", jsonArray);
		jsonData.put("activityStartTime", agencyActivity.getActivityStartTime());
		jsonData.put("activityEndTime", agencyActivity.getActivityEndTime());

		Responses responses = new Responses(jsonData);
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		return responses;
	}

	/**
	 * 获取租房宝典的类别列表
	 * 
	 * @param request
	 * @return
	 */
	public Responses getCustomRentingList(HttpServletRequest request) {
		String cityId = request.getParameter("cityId");

		int defaultPrice = 150000;// 单位为分
		CustomCityPrice cityPrice = customCityPriceRepository.findByCityIdAndStatus(Long.parseLong(cityId), 1);
		long[] modleIdsArr = new long[3];
		if (cityPrice != null) {
			defaultPrice = cityPrice.getLimitPrice();
			String excludeModle = cityPrice.getExcludeModelId().trim();
			if (!StringUtils.isEmpty(excludeModle)) {
				String[] idsArr = excludeModle.split(",");
				for (int i = 0; i < idsArr.length; i++) {
					modleIdsArr[i] = Long.parseLong(idsArr[i]);
				}
			}
		}
		List<CustomRenting> customList = customRentingRepository.findAllValid(modleIdsArr);
		if(!CollectionUtils.isEmpty(customList) && customList.size() <=2){
			List<CustomRenting> pleaseWait = customRentingRepository.findAllInvalid();
			customList.addAll(pleaseWait);
		}
		List<CustomRentingDto> dtoList = new ArrayList<CustomRentingDto>(); // 返回对象
		for (CustomRenting renting : customList) {

			CustomRentingDto rentDto = new CustomRentingDto();
			Map<String, Object> params = new HashMap<String, Object>();// 返回给前端的参数
			BeanMapperUtil.copy(renting, rentDto);// 复制对象
			rentDto.setPicWebPath(renting.getBannerUrl());
			rentDto.setInnerPicWebPath(renting.getInnerBannerUrl());
			long id = renting.getId();
			switch (String.valueOf(id)) {
			case "1": // 千元好房
				String priceRange = "[1," + defaultPrice + "]";
				params.put("cityId", cityId);
				params.put("price", priceRange);
				
				Map<String, Object> priceSelect = Maps.newHashMap();//进入列表后需要点亮的帅选项
				priceSelect.put("price", priceRange);
				rentDto.setSelectMap(priceSelect);
				break;
			case "2":// 轻奢优选
				params.put("cityId", cityId);
				params.put("q", "主卧");
				params.put("cBedRoomNums", "1");
				
				Map<String, Object> selectMap = Maps.newHashMap();//进入列表后需要点亮的帅选项
				selectMap.put("sBedRoomNums", "-1");
				selectMap.put("eBedRoomNums", "1");
				selectMap.put("houseTag", "13");
				rentDto.setSelectMap(selectMap);
				break;
			case "3":// 临近地铁
				params.put("cityId", cityId);
				params.put("distance", "[1," + searchConfiguration.getStationDistance() + "]");
				
				Map<String, Object> tagSelect = Maps.newHashMap();//进入列表后需要点亮的帅选项
				tagSelect.put("houseTag", HouseTagsEnum.CLOSE_TO_SUBWAY.getCode());
				rentDto.setSelectMap(tagSelect);
				break;
			case "4":// 敬请期待
				rentDto.setIsClick(0);
				break;
			default:
				break;

			}
			rentDto.setParams(params);
			dtoList.add(rentDto);
		}
		Responses responses = new Responses();
		responses.setBody(dtoList);
		return responses;
	}
}
