/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseDataCollectWorker.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:14:47 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.enums.bd.SettingsEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: SchemaHouseDataCollectWorker date: 2017年4月15日 下午6:14:47
 * Description: 收集房源信息
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class CrawlSchemaHouseDataCollectWorker implements Runnable {

	private static Log logger = LogFactory.getLog(CrawlSchemaHouseDataCollectWorker.class);

	private LinkedBlockingQueue<CrawlHouseDetail> queue;

	private ConcurrentSkipListSet<String> processingCrawlHouses;

	private CrawlHouseDetailRepository crawlHouseDetailRepository;

	private HouseDao houseDao;

	public CrawlSchemaHouseDataCollectWorker(LinkedBlockingQueue<CrawlHouseDetail> queue,
			CrawlHouseDetailRepository crawlHouseDetailRepository, HouseDao houseDao,
			ConcurrentSkipListSet<String> processingCrawlHouses) {
		this.queue = queue;
		this.crawlHouseDetailRepository = crawlHouseDetailRepository;
		this.houseDao = houseDao;
		this.processingCrawlHouses = processingCrawlHouses;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(LogUtils.getCommLog(String.format("收集待处理爬虫房源的线程出错:%s", e.toString())));
			}
		}
	}

	/**
	 * 工作方法
	 */
	private void doWork() throws Exception {
		CrawlHouseDetail crawlHouseDetail = queue.take();
		try {
			if (crawlHouseDetail.getHouseSellId() == null) {
				// 收集爬虫信息
				collectionCrawlHouseData(crawlHouseDetail);
			} else {
				// 更新爬虫信息
				updateCrawlHouseData(crawlHouseDetail);
			}

		} catch (Exception e) {
			logger.error(LogUtils
					.getCommLog(String.format("处理爬虫数据(%s)出错:%s", crawlHouseDetail.getDepartmentId(), e.toString())));
		} finally {
			// 从正在处理的集合中移除该房间
			processingCrawlHouses.remove(crawlHouseDetail.getDepartmentId());
			// 从正在处理的集合中移除该房间
			logger.info(LogUtils.getCommLog(String.format("处理爬虫(%s)的数据收集结束.", crawlHouseDetail.getDepartmentId())));
		}
	}

	/**
	 * 收集爬虫数据
	 * 
	 * @param crawlHouseDetail
	 */
	private void collectionCrawlHouseData(CrawlHouseDetail crawlHouseDetail) throws Exception {
		try {
			// 如果本地没有信息,则说明没收集过,进行收集
			if (crawlHouseDetail != null) {

				logger.info(
						LogUtils.getCommLog(String.format("待处理的爬虫公寓编码为: (%s)", crawlHouseDetail.getDepartmentId())));
				String rentType = crawlHouseDetail.getRentType();

				HousePublishDto housePublishDtoInImgs = null;
				HousePublishDto housePublishDtoNotImgs = null;
				RoomPublishDto roomPublishDto = null;
				try {
					// 验证发布房源参数是否正确

					// 有图片dto
					housePublishDtoInImgs = getHousePublishDto(crawlHouseDetail);

					// 无图片dto
					housePublishDtoNotImgs = getHouseNotImgsPublishDto(crawlHouseDetail);

				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("爬虫房源参数解析失败" + e.getMessage()));
					rethrowBaseException(e);
				}

				if (housePublishDtoNotImgs == null || housePublishDtoInImgs == null) {
					logger.error(LogUtils.getCommLog("爬虫房源参数解析失败"));
					return;
				}
				String sellId = null;
				Long roomId = null;

				try {
					if (rentType.equals(CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE)) {
						sellId = houseDao.addHousePublishDto(housePublishDtoNotImgs);
					} else {
						sellId = houseDao.addHousePublishDto(housePublishDtoInImgs);
					}
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
				}

				if (rentType.equals(CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE)) {
					try {
						roomPublishDto = getRoomPublishDto(housePublishDtoInImgs, sellId, crawlHouseDetail);
					} catch (Exception e) {
						logger.error(LogUtils.getCommLog("房间参数解析失败" + e.getMessage()));
						rethrowBaseException(e);
					}

					if (roomPublishDto == null) {
						logger.error(LogUtils.getCommLog("爬虫房间参数解析失败"));
					}

					try {
						roomId = houseDao.addCrawloomPublishDto(roomPublishDto);
					} catch (Exception e) {
						logger.error(LogUtils.getCommLog("房间保存失败" + e.getMessage()));
						throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_ROOM_FAIL, "爬虫数据发布房间失败");
					}
					crawlHouseDetail.setRoomId(Integer.parseInt(roomId.toString()));
				}

				try {
					crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);
					crawlHouseDetail.setCollectTime(new Date());
					crawlHouseDetail.setHouseSellId(sellId);
					crawlHouseDetailRepository.save(crawlHouseDetail);
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("更新爬虫数据状态失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
				}
			} else {
				// 如果本地已经有信息,则说明已经收集过,不再进行收集
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(
					String.format("爬虫数据ID(%s)下载失败:%s", crawlHouseDetail.getId(), ExceptionUtils.getStackTrace(e))));
		}
	}

	/**
	 * 更新爬虫数据
	 * 
	 * @param bdHousePicture
	 */
	public void updateCrawlHouseData(CrawlHouseDetail crawlHouseDetail) throws Exception {
		try {
			// 如果本地没有信息,则说明没收集过,进行收集
			if (crawlHouseDetail != null) {

				logger.info(LogUtils
						.getCommLog(String.format("爬虫数据更新，待处理更新爬虫数据编码为: (%s)", crawlHouseDetail.getDepartmentId())));
				String rentType = crawlHouseDetail.getRentType();

				HousePublishDto housePublishDtoInImgs = null;
				HousePublishDto housePublishDtoNotImgs = null;
				RoomPublishDto roomPublishDto = null;
				try {
					// 验证发布房源参数是否正确

					// 有图片dto
					housePublishDtoInImgs = getHousePublishDto(crawlHouseDetail);

					// 无图片dto
					housePublishDtoNotImgs = getHouseNotImgsPublishDto(crawlHouseDetail);
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("爬虫房源数据更新参数解析失败" + e.getMessage()));
					rethrowBaseException(e);
				}

				if (housePublishDtoInImgs == null || housePublishDtoNotImgs == null) {
					logger.error(LogUtils.getCommLog("爬虫房源数据更新参数解析失败"));
					return;
				}
				String sellId = crawlHouseDetail.getHouseSellId();
				try {
					if (rentType.equals(CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE)) {
						houseDao.updateCrawlHousePublishDto(housePublishDtoNotImgs, sellId);
					} else {
						houseDao.updateCrawlHousePublishDto(housePublishDtoInImgs, sellId);
					}
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("爬虫房源数据更新保存失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "爬虫房源数据更新失败");
				}

				if (rentType.equals(CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE)) {
					try {
						roomPublishDto = getRoomPublishDto(housePublishDtoInImgs, sellId, crawlHouseDetail);
					} catch (Exception e) {
						logger.error(LogUtils.getCommLog("房间爬虫数据更新参数解析失败" + e.getMessage()));
						rethrowBaseException(e);
					}

					if (roomPublishDto == null) {
						logger.error(LogUtils.getCommLog("房间爬虫房源数据更新参数解析失败"));
					}

					try {
						int roomId = crawlHouseDetail.getRoomId();
						houseDao.updateCrawlRoomPublishDto(roomPublishDto, roomId);
					} catch (Exception e) {
						logger.error(LogUtils.getCommLog("房间爬虫数据更新保存失败" + e.getMessage()));
						throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_ROOM_FAIL, "房间爬虫数据更新保存失败");
					}
				}

				try {
					crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);
					crawlHouseDetail.setCollectTime(new Date());
					crawlHouseDetail.setHouseSellId(sellId);
					crawlHouseDetailRepository.save(crawlHouseDetail);
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog("更新爬虫数据状态失败" + e.getMessage()));
					throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
				}
			} else {
				// 如果本地已经有信息,则说明已经收集过,不再进行收集
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(
					String.format("爬虫数据ID(%s)下载失败:%s", crawlHouseDetail.getId(), ExceptionUtils.getStackTrace(e))));
		}
	}

	/**
	 * 获取房源DTO
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private HousePublishDto getHousePublishDto(CrawlHouseDetail crawlHouseDetail) throws Exception {

		if (crawlHouseDetail == null) {
			return null;
		}

		HousePublishDto housePublishDto = new HousePublishDto();

		// 纬度
		String positionXKey = "positionX";
		String positionX = crawlHouseDetail.getLatitude();
		checkPositionValue(positionX, positionXKey);
		checkLatitude(positionX, positionXKey);
		housePublishDto.setPositionX(positionX);

		String positionYKey = "positionY";
		String positionY = crawlHouseDetail.getLongitude();
		checkPositionValue(positionY, positionYKey);
		checkLongitude(positionY, positionYKey);
		housePublishDto.setPositionY(positionY);

		String communityNameKey = "communityName";
		String[] arr = crawlHouseDetail.getDepartmentName().split("\\s+");
		String communityName = arr[1];
		checkStringMaxLength(communityName, communityNameKey, Constants.HouseDetail.HOUSE_COMMUNITY_NAME_LENGTH_MAX);
		housePublishDto.setCommunityName(communityName);

		String priceKey = "price";
		int indexPrice = crawlHouseDetail.getPrice().indexOf(".");
		int price = -1;
		if (indexPrice > 0) {
			price = Integer.parseInt(crawlHouseDetail.getPrice().substring(0, indexPrice - 1)) * 100;
		} else {
			price = Integer.parseInt(crawlHouseDetail.getPrice()) * 100;
		}
		checkNonNegativeNumber(price, priceKey);
		housePublishDto.setPrice(price);

		// String bonusKey = "bonus";
		// int bonus = RequestUtils.getParameterInt(request, bonusKey);
		// checkNonNegativeNumber(bonus, bonusKey);
		// housePublishDto.setBonus(bonus);

		// String depositKey = "deposit";
		// int deposit = RequestUtils.getParameterInt(request, depositKey);
		// checkNonNegativeNumber(deposit, depositKey);
		// housePublishDto.setDeposit(deposit);

		// String hasKeyKey = "hasKey";
		// int hasKey = RequestUtils.getParameterInt(request, hasKeyKey);
		// checkHaskey(hasKey, hasKeyKey);
		// housePublishDto.setHasKey(hasKey);
		// 由于来源可能是字符串，取消经纪公司ID必填项
		// int companyId = RequestUtils.getParameterInt(request, "companyId");
		// housePublishDto.setCompanyId(companyId);

		// String companyNameKey = "companyName";
		// String companyName = RequestUtils.getParameterString(request,
		// companyNameKey);
		// checkStringMaxLength(companyName, companyNameKey,
		// Constants.HouseBase.HOUSE_COMPANY_NAME_LENGTH_MAX);
		// housePublishDto.setCompanyName(companyName);
		// 由于来源可能是字符串，取消经纪人ID必填项
		// int agencyId = RequestUtils.getParameterInt(request, "agencyId");
		// housePublishDto.setAgencyId(agencyId);

		String agencyPhone = crawlHouseDetail.getPhoneNum();
		checkPhone(agencyPhone, "agencyPhone");
		housePublishDto.setAgencyPhone(agencyPhone);

		housePublishDto.setAgencyName("房管员");

		// String agencyIntroduceKey = "agencyIntroduce";
		// String agencyIntroduce = RequestUtils.getParameterString(request,
		// agencyIntroduceKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyIntroduce, agencyIntroduceKey,
		// Constants.HouseBase.HOUSE_AGENCY_INTRODUCE_LENGTH_MAX);
		// housePublishDto.setAgencyIntroduce(agencyIntroduce);

		// int agencyGender = RequestUtils.getParameterInt(request,
		// "agencyGender");
		// checkGender(agencyGender, "agencyGender");
		// housePublishDto.setAgencyGender(agencyGender);
		//
		// String agencyAvatarKey = "agencyAvatar";
		// String agencyAvatar = RequestUtils.getParameterString(request,
		// agencyAvatarKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyAvatar, agencyAvatarKey,
		// Constants.HouseBase.HOUSE_AGENCY_AVATAR_LENGTH_MAX);
		// housePublishDto.setAgencyAvatar(agencyAvatar);

		// 默认上架
		String statusKey = "status";
		int status = Constants.HouseBase.PUBLISH_STATUS_PRE_RENT;
		checkStatus(status, statusKey);
		housePublishDto.setStatus(status);
		// 卧室
		String bedroomNumKey = "bedroomNum";
		int indexRoom = crawlHouseDetail.getHallRoom().indexOf("室");
		if (indexRoom > 0) {
			int bedroomNum = Integer.parseInt(crawlHouseDetail.getHallRoom().substring(0, indexRoom));
			checkNonNegativeNumber(bedroomNum, bedroomNumKey);
			housePublishDto.setBedroomNum(bedroomNum);
		}
		// 客厅
		String livingroomNumKey = "livingroomNum";
		int indexLiving = crawlHouseDetail.getHallRoom().indexOf("厅");
		if (indexLiving > 0) {
			int livingroomNum = Integer
					.parseInt(crawlHouseDetail.getHallRoom().substring(indexLiving - 1, indexLiving));
			checkNonNegativeNumber(livingroomNum, livingroomNumKey);
			housePublishDto.setLivingroomNum(livingroomNum);
		}
		// String kitchenNumKey = "kitchenNum";
		// int kitchenNum = crawlHouseDetail.getHallRoom();
		// checkNonNegativeNumber(kitchenNum, kitchenNumKey);
		// housePublishDto.setKitchenNum(kitchenNum);
		// 卫生间
		String toiletNumKey = "toiletNum";
		int indexTitle = crawlHouseDetail.getHallRoom().indexOf("卫");
		if (indexTitle > 0) {
			int toiletNum = Integer.parseInt(crawlHouseDetail.getHallRoom().substring(indexTitle - 1, indexTitle));
			checkNonNegativeNumber(toiletNum, toiletNumKey);
			housePublishDto.setToiletNum(toiletNum);
		}

		// String balconyNumKey = "balconyNum";
		// int balconyNum = RequestUtils.getParameterInt(request,
		// balconyNumKey);
		// checkNonNegativeNumber(balconyNum, balconyNumKey);
		// housePublishDto.setBalconyNum(balconyNum);

		// String buildingNoKey = "buildingNo";
		// String buildingNo = RequestUtils.getParameterString(request,
		// buildingNoKey);
		// checkStringMaxLength(buildingNo, buildingNoKey,
		// Constants.HouseDetail.HOUSE_BUILDING_NO_LENGTH_MAX);
		// housePublishDto.setBuildingNo(buildingNo);

		// String unitNoKey = "unitNo";
		// String unitNo = RequestUtils.getParameterString(request, unitNoKey,
		// StringUtil.EMPTY);
		// checkStringMaxLength(unitNo, unitNoKey,
		// Constants.HouseDetail.HOUSE_UNIT_NO_LENGTH_MAX);
		// housePublishDto.setUnitNo(unitNo);

		// String houseNoKey = "houseNo";
		// String houseNo = RequestUtils.getParameterString(request, houseNoKey,
		// StringUtil.EMPTY);
		// checkStringMaxLength(houseNo, houseNoKey,
		// Constants.HouseDetail.HOUSE_HOUSE_NO_LENGTH_MAX);
		// housePublishDto.setHouseNo(houseNo);

		// String buildingNameKey = "buildingName";
		// String buildingName = RequestUtils.getParameterString(request,
		// buildingNameKey);
		// checkStringMaxLength(buildingName, buildingNameKey,
		// Constants.HouseDetail.HOUSE_BUILDING_NAME_LENGTH_MAX);
		// housePublishDto.setBuildingName(buildingName);

		String areaKey = "area";
		float area = Integer
				.parseInt(crawlHouseDetail.getArea().substring(0, crawlHouseDetail.getArea().indexOf("m")).trim());
		checkNonNegativeNumber(area, areaKey);
		checkArea(area, areaKey, Constants.HouseDetail.HOUSE_AREA_MIN);
		housePublishDto.setArea(area);

		String floorNoKey = "floorNo";
		String flowNo = crawlHouseDetail.getFloor().substring(0, crawlHouseDetail.getFloor().indexOf("/"));
		checkStringMaxLength(flowNo, floorNoKey, Constants.HouseDetail.HOUSE_FLOW_NO_LENGTH_MAX);
		housePublishDto.setFlowNo(flowNo);

		String floorTotalKey = "floorTotal";
		String flowTotal = crawlHouseDetail.getFloor()
				.substring(crawlHouseDetail.getFloor().indexOf("/") + 1, crawlHouseDetail.getFloor().indexOf("层"))
				.trim();
		checkStringMaxLength(flowTotal, floorTotalKey, Constants.HouseDetail.HOUSE_FLOW_TOTAL_LENGTH_MAX);
		housePublishDto.setFlowTotal(flowTotal);

		if (CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE.equals(crawlHouseDetail.getRentType())) {
			String orientationKey = "orientation";
			String orientationStr = crawlHouseDetail.getHallRoom().substring(
					crawlHouseDetail.getHallRoom().indexOf("朝") + 1, crawlHouseDetail.getHallRoom().indexOf("["));
			int orientation = CrawlConstants.getOrentationCode(orientationStr);
			checkOrientation(orientation, orientationKey);
			housePublishDto.setOrientation(orientation);
		}

		// String buildingTypeKey = "buildingType";
		// int buildingType = RequestUtils.getParameterInt(request,
		// buildingTypeKey);
		// checkBuildingType(buildingType, buildingTypeKey);
		// housePublishDto.setBuildingType(buildingType);

		// String fitmentTypeKey = "fitmentType";
		// int fitmentType = RequestUtils.getParameterInt(request,
		// fitmentTypeKey);
		// checkDecoration(fitmentType, fitmentTypeKey);
		// housePublishDto.setFitmentType(fitmentType);
		//
		// String buildingYearKey = "buildingYear";
		// String buildingYear = RequestUtils.getParameterString(request,
		// buildingYearKey, StringUtil.EMPTY);
		// checkBuildingYear(buildingYear, buildingYearKey);
		// housePublishDto.setBuildingYear(buildingYear);

		// String toiletKey = "toilet";
		// int toilet = RequestUtils.getParameterInt(request, toiletKey,
		// Constants.HouseBase.NOT_INDEPENDENT);
		// checkIndependent(toilet, toiletKey);
		// housePublishDto.setToilet(toilet);
		//
		// String balconyKey = "balcony";
		// int balcony = RequestUtils.getParameterInt(request, balconyKey,
		// Constants.HouseBase.NOT_INDEPENDENT);
		// checkIndependent(balcony, balconyKey);
		// housePublishDto.setBalcony(balcony);
		//
		// String insuranceKey = "insurance";
		// int insurance = RequestUtils.getParameterInt(request, insuranceKey,
		// Constants.HouseBase.INSURANCE_NOT_EXIST);
		// checkInsurance(insurance, insuranceKey);
		// housePublishDto.setInsurance(insurance);

		String checkInKey = "checkIn";
		Date checkInDate = new Date();
		checkCheckIn(checkInDate, checkInKey);
		housePublishDto.setCheckInTime(checkInDate);

		// String depositMonthKey = "depositMonth";
		// int depositMonth = RequestUtils.getParameterInt(request,
		// depositMonthKey);
		// checkNonNegativeNumber(depositMonth, depositMonthKey);
		// housePublishDto.setDepositMonth(depositMonth);
		//
		// String periodMonthKey = "periodMonth";
		// int periodMonth = RequestUtils.getParameterInt(request,
		// periodMonthKey);
		// checkNonNegativeNumber(periodMonth, periodMonthKey);
		// housePublishDto.setPeriodMonth(periodMonth);

		String entireRentKey = "entireRent";
		int entireRent = -1;
		if (CrawlConstants.CrawlHouseDetail.RENT_TYPE_ENTIRE.equals(crawlHouseDetail.getRentType())) {
			entireRent = CrawlConstants.CrawlHouseDetail.RENT_TYPE_ENTIRE_NUM;
		} else {
			entireRent = CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE_NUM;
		}
		checkEntireRent(entireRent, entireRentKey);
		housePublishDto.setEntireRent(entireRent);

		String settingsKey = "settings";
		if (crawlHouseDetail.getFacilities().length() > 4) {
			String settingStr = crawlHouseDetail.getFacilities()
					.substring(1, crawlHouseDetail.getFacilities().length() - 1).replace("\"", "").replace(" ", "");
			String settings = SettingsEnum.getSettingCodes(settingStr);
			checkSetting(settings, settingsKey);
			housePublishDto.setSettings(SettingsEnum.getSettingCodes(settingStr));
			;
		} else {
			housePublishDto.setSettings("");
		}

		//
		// String settingsAddonKey = "settingsAddon";
		// String settingsAddon = RequestUtils.getParameterString(request,
		// settingsAddonKey, StringUtil.EMPTY);
		// checkSetting(settingsAddon, settingsAddonKey);
		// housePublishDto.setSettingsAddon(settingsAddon);

		String descKey = "desc";
		String desc = crawlHouseDetail.getHouseDesc();
		checkStringMaxLength(desc, descKey, Constants.HouseDetail.HOUSE_COMMENT_LENGTH_MAX);
		housePublishDto.setDesc(desc);

		if (crawlHouseDetail.getImageUrls().length() > 3) {
			String imgsKey = "imgs";
			// 去除【和双引号
			String contents_pic = crawlHouseDetail.getImageUrls()
					.substring(1, crawlHouseDetail.getImageUrls().length() - 1).replace("\"", "").replace(" ", "")
					.replace("?w=1080", "");
			String[] arrImage = contents_pic.split(",");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arrImage.length; i++) {
				if (arrImage[i].indexOf("jpg") != -1 || arrImage[i].indexOf("png") != -1
						|| arrImage[i].indexOf("bmp") != -1) {
					sb.append("http:").append(arrImage[i]);
					if (i != (arrImage.length - 1))
						sb.append(",");
				}
			}
			String imgs = sb.toString();
			checkImgs(imgs, imgsKey);
			housePublishDto.setImgs(imgs);
		} else {
			housePublishDto.setImgs("");
		}

		// 商圈
		String bizNameKey = "bizName";
		String bizName = crawlHouseDetail.getZone();
		checkStringMaxLength(bizName, bizNameKey, Constants.HouseDetail.HOUSE_BIZ_NAME_LENGTH_MAX);
		housePublishDto.setBizName(bizName);

		// 来源
		String source = crawlHouseDetail.getDepartment();
		housePublishDto.setSource(source);
		return housePublishDto;
	}

	/**
	 * 获取房源DTO
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private HousePublishDto getHouseNotImgsPublishDto(CrawlHouseDetail crawlHouseDetail) throws Exception {

		if (crawlHouseDetail == null) {
			return null;
		}

		HousePublishDto housePublishDto = new HousePublishDto();

		// 纬度
		String positionXKey = "positionX";
		String positionX = crawlHouseDetail.getLatitude();
		checkPositionValue(positionX, positionXKey);
		checkLatitude(positionX, positionXKey);
		housePublishDto.setPositionX(positionX);

		String positionYKey = "positionY";
		String positionY = crawlHouseDetail.getLongitude();
		checkPositionValue(positionY, positionYKey);
		checkLongitude(positionY, positionYKey);
		housePublishDto.setPositionY(positionY);

		String communityNameKey = "communityName";
		String[] arr = crawlHouseDetail.getDepartmentName().split("\\s+");
		String communityName = arr[1];
		checkStringMaxLength(communityName, communityNameKey, Constants.HouseDetail.HOUSE_COMMUNITY_NAME_LENGTH_MAX);
		housePublishDto.setCommunityName(communityName);

		String priceKey = "price";
		int indexPrice = crawlHouseDetail.getPrice().indexOf(".");
		int price = -1;
		if (indexPrice > 0) {
			price = Integer.parseInt(crawlHouseDetail.getPrice().substring(0, indexPrice - 1)) * 100;
		} else {
			price = Integer.parseInt(crawlHouseDetail.getPrice()) * 100;
		}
		checkNonNegativeNumber(price, priceKey);
		housePublishDto.setPrice(price);

		// String bonusKey = "bonus";
		// int bonus = RequestUtils.getParameterInt(request, bonusKey);
		// checkNonNegativeNumber(bonus, bonusKey);
		// housePublishDto.setBonus(bonus);

		// String depositKey = "deposit";
		// int deposit = RequestUtils.getParameterInt(request, depositKey);
		// checkNonNegativeNumber(deposit, depositKey);
		// housePublishDto.setDeposit(deposit);

		// String hasKeyKey = "hasKey";
		// int hasKey = RequestUtils.getParameterInt(request, hasKeyKey);
		// checkHaskey(hasKey, hasKeyKey);
		// housePublishDto.setHasKey(hasKey);
		// 由于来源可能是字符串，取消经纪公司ID必填项
		// int companyId = RequestUtils.getParameterInt(request, "companyId");
		// housePublishDto.setCompanyId(companyId);

		// String companyNameKey = "companyName";
		// String companyName = RequestUtils.getParameterString(request,
		// companyNameKey);
		// checkStringMaxLength(companyName, companyNameKey,
		// Constants.HouseBase.HOUSE_COMPANY_NAME_LENGTH_MAX);
		// housePublishDto.setCompanyName(companyName);
		// 由于来源可能是字符串，取消经纪人ID必填项
		// int agencyId = RequestUtils.getParameterInt(request, "agencyId");
		// housePublishDto.setAgencyId(agencyId);

		String agencyPhone = crawlHouseDetail.getPhoneNum();
		checkPhone(agencyPhone, "agencyPhone");
		housePublishDto.setAgencyPhone(agencyPhone);

		housePublishDto.setAgencyName("房管员");

		// String agencyIntroduceKey = "agencyIntroduce";
		// String agencyIntroduce = RequestUtils.getParameterString(request,
		// agencyIntroduceKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyIntroduce, agencyIntroduceKey,
		// Constants.HouseBase.HOUSE_AGENCY_INTRODUCE_LENGTH_MAX);
		// housePublishDto.setAgencyIntroduce(agencyIntroduce);

		// int agencyGender = RequestUtils.getParameterInt(request,
		// "agencyGender");
		// checkGender(agencyGender, "agencyGender");
		// housePublishDto.setAgencyGender(agencyGender);
		//
		// String agencyAvatarKey = "agencyAvatar";
		// String agencyAvatar = RequestUtils.getParameterString(request,
		// agencyAvatarKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyAvatar, agencyAvatarKey,
		// Constants.HouseBase.HOUSE_AGENCY_AVATAR_LENGTH_MAX);
		// housePublishDto.setAgencyAvatar(agencyAvatar);

		// 默认上架
		String statusKey = "status";
		int status = Constants.HouseBase.PUBLISH_STATUS_PRE_RENT;
		checkStatus(status, statusKey);
		housePublishDto.setStatus(status);
		// 卧室
		String bedroomNumKey = "bedroomNum";
		int indexRoom = crawlHouseDetail.getHallRoom().indexOf("室");
		if (indexRoom > 0) {
			int bedroomNum = Integer.parseInt(crawlHouseDetail.getHallRoom().substring(0, indexRoom));
			checkNonNegativeNumber(bedroomNum, bedroomNumKey);
			housePublishDto.setBedroomNum(bedroomNum);
		}
		// 客厅
		String livingroomNumKey = "livingroomNum";
		int indexLiving = crawlHouseDetail.getHallRoom().indexOf("厅");
		if (indexLiving > 0) {
			int livingroomNum = Integer
					.parseInt(crawlHouseDetail.getHallRoom().substring(indexLiving - 1, indexLiving));
			checkNonNegativeNumber(livingroomNum, livingroomNumKey);
			housePublishDto.setLivingroomNum(livingroomNum);
		}
		// String kitchenNumKey = "kitchenNum";
		// int kitchenNum = crawlHouseDetail.getHallRoom();
		// checkNonNegativeNumber(kitchenNum, kitchenNumKey);
		// housePublishDto.setKitchenNum(kitchenNum);
		// 卫生间
		String toiletNumKey = "toiletNum";
		int indexTitle = crawlHouseDetail.getHallRoom().indexOf("卫");
		if (indexTitle > 0) {
			int toiletNum = Integer.parseInt(crawlHouseDetail.getHallRoom().substring(indexTitle - 1, indexTitle));
			checkNonNegativeNumber(toiletNum, toiletNumKey);
			housePublishDto.setToiletNum(toiletNum);
		}

		// String balconyNumKey = "balconyNum";
		// int balconyNum = RequestUtils.getParameterInt(request,
		// balconyNumKey);
		// checkNonNegativeNumber(balconyNum, balconyNumKey);
		// housePublishDto.setBalconyNum(balconyNum);

		// String buildingNoKey = "buildingNo";
		// String buildingNo = RequestUtils.getParameterString(request,
		// buildingNoKey);
		// checkStringMaxLength(buildingNo, buildingNoKey,
		// Constants.HouseDetail.HOUSE_BUILDING_NO_LENGTH_MAX);
		// housePublishDto.setBuildingNo(buildingNo);

		// String unitNoKey = "unitNo";
		// String unitNo = RequestUtils.getParameterString(request, unitNoKey,
		// StringUtil.EMPTY);
		// checkStringMaxLength(unitNo, unitNoKey,
		// Constants.HouseDetail.HOUSE_UNIT_NO_LENGTH_MAX);
		// housePublishDto.setUnitNo(unitNo);

		// String houseNoKey = "houseNo";
		// String houseNo = RequestUtils.getParameterString(request, houseNoKey,
		// StringUtil.EMPTY);
		// checkStringMaxLength(houseNo, houseNoKey,
		// Constants.HouseDetail.HOUSE_HOUSE_NO_LENGTH_MAX);
		// housePublishDto.setHouseNo(houseNo);

		// String buildingNameKey = "buildingName";
		// String buildingName = RequestUtils.getParameterString(request,
		// buildingNameKey);
		// checkStringMaxLength(buildingName, buildingNameKey,
		// Constants.HouseDetail.HOUSE_BUILDING_NAME_LENGTH_MAX);
		// housePublishDto.setBuildingName(buildingName);

		String areaKey = "area";
		float area = Integer
				.parseInt(crawlHouseDetail.getArea().substring(0, crawlHouseDetail.getArea().indexOf("m")).trim());
		checkNonNegativeNumber(area, areaKey);
		checkArea(area, areaKey, Constants.HouseDetail.HOUSE_AREA_MIN);
		housePublishDto.setArea(area);

		String floorNoKey = "floorNo";
		String flowNo = crawlHouseDetail.getFloor().substring(0, crawlHouseDetail.getFloor().indexOf("/"));
		checkStringMaxLength(flowNo, floorNoKey, Constants.HouseDetail.HOUSE_FLOW_NO_LENGTH_MAX);
		housePublishDto.setFlowNo(flowNo);

		String floorTotalKey = "floorTotal";
		String flowTotal = crawlHouseDetail.getFloor()
				.substring(crawlHouseDetail.getFloor().indexOf("/") + 1, crawlHouseDetail.getFloor().indexOf("层"))
				.trim();
		checkStringMaxLength(flowTotal, floorTotalKey, Constants.HouseDetail.HOUSE_FLOW_TOTAL_LENGTH_MAX);
		housePublishDto.setFlowTotal(flowTotal);

		if (CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE.equals(crawlHouseDetail.getRentType())) {
			String orientationKey = "orientation";
			String orientationStr = crawlHouseDetail.getHallRoom().substring(
					crawlHouseDetail.getHallRoom().indexOf("朝") + 1, crawlHouseDetail.getHallRoom().indexOf("["));
			int orientation = CrawlConstants.getOrentationCode(orientationStr);
			checkOrientation(orientation, orientationKey);
			housePublishDto.setOrientation(orientation);
		}

		// String buildingTypeKey = "buildingType";
		// int buildingType = RequestUtils.getParameterInt(request,
		// buildingTypeKey);
		// checkBuildingType(buildingType, buildingTypeKey);
		// housePublishDto.setBuildingType(buildingType);

		// String fitmentTypeKey = "fitmentType";
		// int fitmentType = RequestUtils.getParameterInt(request,
		// fitmentTypeKey);
		// checkDecoration(fitmentType, fitmentTypeKey);
		// housePublishDto.setFitmentType(fitmentType);
		//
		// String buildingYearKey = "buildingYear";
		// String buildingYear = RequestUtils.getParameterString(request,
		// buildingYearKey, StringUtil.EMPTY);
		// checkBuildingYear(buildingYear, buildingYearKey);
		// housePublishDto.setBuildingYear(buildingYear);

		// String toiletKey = "toilet";
		// int toilet = RequestUtils.getParameterInt(request, toiletKey,
		// Constants.HouseBase.NOT_INDEPENDENT);
		// checkIndependent(toilet, toiletKey);
		// housePublishDto.setToilet(toilet);
		//
		// String balconyKey = "balcony";
		// int balcony = RequestUtils.getParameterInt(request, balconyKey,
		// Constants.HouseBase.NOT_INDEPENDENT);
		// checkIndependent(balcony, balconyKey);
		// housePublishDto.setBalcony(balcony);
		//
		// String insuranceKey = "insurance";
		// int insurance = RequestUtils.getParameterInt(request, insuranceKey,
		// Constants.HouseBase.INSURANCE_NOT_EXIST);
		// checkInsurance(insurance, insuranceKey);
		// housePublishDto.setInsurance(insurance);

		String checkInKey = "checkIn";
		Date checkInDate = new Date();
		checkCheckIn(checkInDate, checkInKey);
		housePublishDto.setCheckInTime(checkInDate);

		// String depositMonthKey = "depositMonth";
		// int depositMonth = RequestUtils.getParameterInt(request,
		// depositMonthKey);
		// checkNonNegativeNumber(depositMonth, depositMonthKey);
		// housePublishDto.setDepositMonth(depositMonth);
		//
		// String periodMonthKey = "periodMonth";
		// int periodMonth = RequestUtils.getParameterInt(request,
		// periodMonthKey);
		// checkNonNegativeNumber(periodMonth, periodMonthKey);
		// housePublishDto.setPeriodMonth(periodMonth);

		String entireRentKey = "entireRent";
		int entireRent = -1;
		if (CrawlConstants.CrawlHouseDetail.RENT_TYPE_ENTIRE.equals(crawlHouseDetail.getRentType())) {
			entireRent = CrawlConstants.CrawlHouseDetail.RENT_TYPE_ENTIRE_NUM;
		} else {
			entireRent = CrawlConstants.CrawlHouseDetail.RENT_TYPE_SHARE_NUM;
		}
		checkEntireRent(entireRent, entireRentKey);
		housePublishDto.setEntireRent(entireRent);

		String settingsKey = "settings";
		if (crawlHouseDetail.getFacilities().length() > 4) {
			String settingStr = crawlHouseDetail.getFacilities()
					.substring(1, crawlHouseDetail.getFacilities().length() - 1).replace("\"", "").replace(" ", "");
			String settings = SettingsEnum.getSettingCodes(settingStr);
			checkSetting(settings, settingsKey);
			housePublishDto.setSettings(SettingsEnum.getSettingCodes(settingStr));
			;
		} else {
			housePublishDto.setSettings("");
		}

		//
		// String settingsAddonKey = "settingsAddon";
		// String settingsAddon = RequestUtils.getParameterString(request,
		// settingsAddonKey, StringUtil.EMPTY);
		// checkSetting(settingsAddon, settingsAddonKey);
		// housePublishDto.setSettingsAddon(settingsAddon);

		String descKey = "desc";
		String desc = crawlHouseDetail.getHouseDesc();
		checkStringMaxLength(desc, descKey, Constants.HouseDetail.HOUSE_COMMENT_LENGTH_MAX);
		housePublishDto.setDesc(desc);

		// if(crawlHouseDetail.getImageUrls().length() > 3){
		// String imgsKey = "imgs";
		// //去除【和双引号
		// String contents_pic = crawlHouseDetail.getImageUrls().substring(1,
		// crawlHouseDetail.getImageUrls().length()
		// -1).replace("\"","").replace(" ", "").replace("?w=1080", "");
		// String[] arrImage = contents_pic.split(",");
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < arrImage.length; i++) {
		// if(arrImage[i].indexOf("jpg")!=-1 || arrImage[i].indexOf("png")!=-1
		// || arrImage[i].indexOf("bmp")!=-1){
		// sb.append("http:").append(arrImage[i]);
		// if(i != (arrImage.length-1))
		// sb.append(",");
		// }
		// }
		// String imgs = sb.toString();
		// checkImgs(imgs, imgsKey);
		// housePublishDto.setImgs(imgs);
		// }else{
		// housePublishDto.setImgs("");
		// }

		housePublishDto.setImgs("");
		// 商圈
		String bizNameKey = "bizName";
		String bizName = crawlHouseDetail.getZone();
		checkStringMaxLength(bizName, bizNameKey, Constants.HouseDetail.HOUSE_BIZ_NAME_LENGTH_MAX);
		housePublishDto.setBizName(bizName);

		// 来源
		String source = crawlHouseDetail.getDepartment();
		housePublishDto.setSource(source);
		return housePublishDto;
	}

	/**
	 * 获取房间DTO
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private RoomPublishDto getRoomPublishDto(HousePublishDto housePublishDto, String sellId,
			CrawlHouseDetail crawlHouseDetail) throws Exception {

		if (housePublishDto == null) {
			return null;
		}

		RoomPublishDto roomPublishDto = new RoomPublishDto();

		// String sellIdKey = "sellId";
		// String sellId = RequestUtils.getParameterString(request, sellIdKey);
		roomPublishDto.setSellId(sellId);

		String priceKey = "price";
		int price = housePublishDto.getPrice();
		checkNonNegativeNumber(price, priceKey);
		roomPublishDto.setPrice(price);

		// String bonusKey = "bonus";
		// int bonus = RequestUtils.getParameterInt(request, bonusKey);
		// checkNonNegativeNumber(bonus, bonusKey);
		// roomPublishDto.setBonus(bonus);

		// String depositKey = "deposit";
		// int deposit = RequestUtils.getParameterInt(request, depositKey);
		// checkNonNegativeNumber(deposit, depositKey);
		// roomPublishDto.setDeposit(deposit);

		// String hasKeyKey = "hasKey";
		// int hasKey = RequestUtils.getParameterInt(request, hasKeyKey);
		// checkHaskey(hasKey, hasKeyKey);
		// roomPublishDto.setHasKey(hasKey);

		// String companyIdKey = "companyId";
		// int companyId = RequestUtils.getParameterInt(request, companyIdKey);
		// roomPublishDto.setCompanyId(companyId);

		// String companyNameKey = "companyName";
		// String companyName = RequestUtils.getParameterString(request,
		// companyNameKey);
		// checkStringMaxLength(companyName, companyNameKey,
		// Constants.HouseBase.HOUSE_COMPANY_NAME_LENGTH_MAX);
		// roomPublishDto.setCompanyName(companyName);

		// String agencyIdKey = "agencyId";
		// int agencyId = RequestUtils.getParameterInt(request, agencyIdKey);
		// roomPublishDto.setAgencyId(agencyId);

		String agencyPhoneKey = "agencyPhone";
		String agencyPhone = housePublishDto.getAgencyPhone();
		checkPhone(agencyPhone, agencyPhoneKey);
		roomPublishDto.setAgencyPhone(agencyPhone);

		String agencyNameKey = "agencyName";
		String agencyName = housePublishDto.getAgencyIntroduce();
		checkStringMaxLength(agencyName, agencyNameKey, Constants.HouseBase.HOUSE_AGENCY_NAME_LENGTH_MAX);
		roomPublishDto.setAgencyName(agencyName);

		// String agencyIntroduceKey = "agencyIntroduce";
		// String agencyIntroduce = RequestUtils.getParameterString(request,
		// agencyIntroduceKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyIntroduce, agencyIntroduceKey,
		// Constants.HouseBase.HOUSE_AGENCY_INTRODUCE_LENGTH_MAX);
		// roomPublishDto.setAgencyIntroduce(agencyIntroduce);
		//
		// String agencyGenderKey = "agencyGender";
		// int agencyGender = RequestUtils.getParameterInt(request,
		// agencyGenderKey);
		// checkGender(agencyGender, agencyGenderKey);
		// roomPublishDto.setAgencyGender(agencyGender);
		//
		// String agencyAvatarKey = "agencyAvatar";
		// String agencyAvatar = RequestUtils.getParameterString(request,
		// agencyAvatarKey, StringUtil.EMPTY);
		// checkStringMaxLength(agencyAvatar, agencyAvatarKey,
		// Constants.HouseBase.HOUSE_AGENCY_AVATAR_LENGTH_MAX);
		// roomPublishDto.setAgencyAvatar(agencyAvatar);

		// 默认待出租
		String statusKey = "status";
		int status = Constants.HouseBase.PUBLISH_STATUS_PRE_RENT;
		checkStatus(status, statusKey);
		roomPublishDto.setStatus(status);

		String areaKey = "area";
		float area = housePublishDto.getArea();
		checkNonNegativeNumber(area, areaKey);
		checkArea(area, areaKey, Constants.RoomBase.ROOM_AREA_MIN);
		roomPublishDto.setArea(area);

		String orientationKey = "orientation";
		int orientation = housePublishDto.getOrientation();
		checkOrientation(orientation, orientationKey);
		roomPublishDto.setOrientation(orientation);

		// String fitmentTypeKey = "fitmentType";
		// int fitmentType = RequestUtils.getParameterInt(request,
		// fitmentTypeKey);
		// checkDecoration(fitmentType, fitmentTypeKey);
		// roomPublishDto.setFitmentType(fitmentType);

		String toiletKey = "toilet";
		int toilet = housePublishDto.getToilet();
		if (toilet > 0) {
			checkIndependent(toilet, toiletKey);
			roomPublishDto.setToilet(toilet);
		}

		String balconyKey = "balcony";
		int balcony = housePublishDto.getBalcony();
		checkIndependent(balcony, balconyKey);
		roomPublishDto.setBalcony(balcony);

		String insuranceKey = "insurance";
		int insurance = housePublishDto.getInsurance();
		checkInsurance(insurance, insuranceKey);
		roomPublishDto.setInsurance(insurance);

		String checkInKey = "checkIn";

		Date checkInDate = housePublishDto.getCheckInTime();
		// checkCheckIn(checkInDate, checkInKey);
		roomPublishDto.setCheckInTime(checkInDate);

		// String depositMonthKey = "depositMonth";
		// int depositMonth = housePublishDto.getPrice();
		// checkNonNegativeNumber(depositMonth, depositMonthKey);
		// roomPublishDto.setDepositMonth(depositMonth);

		//
		// String periodMonthKey = "periodMonth";
		// int periodMonth = RequestUtils.getParameterInt(request,
		// periodMonthKey);
		// checkNonNegativeNumber(periodMonth, periodMonthKey);
		// roomPublishDto.setPeriodMonth(periodMonth);

		// String settingsKey = "settings";
		// String settings = RequestUtils.getParameterString(request,
		// settingsKey, StringUtil.EMPTY);
		// checkSetting(settings, settingsKey);
		// roomPublishDto.setSettings(settings);

		// String settingsAddonKey = "settingsAddon";
		// String settingsAddon = RequestUtils.getParameterString(request,
		// settingsAddonKey, StringUtil.EMPTY);
		// checkSetting(settingsAddon, settingsAddonKey);
		// roomPublishDto.setSettingsAddon(settingsAddon);

		String descKey = "desc";
		String desc = housePublishDto.getDesc();
		checkStringMaxLength(desc, descKey, Constants.RoomBase.ROOM_COMMENT_LENGTH_MAX);
		roomPublishDto.setDesc(desc);

		String imgsKey = "imgs";
		String imgs = housePublishDto.getImgs();
		checkImgs(imgs, imgsKey);
		roomPublishDto.setImgs(imgs);

		// String roomNameKey = "roomName";
		// String roomName = RequestUtils.getParameterString(request,
		// roomNameKey);
		// checkStringMaxLength(roomName, roomNameKey,
		// Constants.RoomBase.ROOM_NAME_LENGTH_MAX);
		// roomPublishDto.setRoomName(roomName);

		String roomTypeKey = "roomType";
		int strLength = crawlHouseDetail.getDepartmentName().trim().length();
		String roomTypeStr = crawlHouseDetail.getDepartmentName().trim().substring(strLength - 2, strLength);
		int roomType = -1;
		if (roomTypeStr.equals(CrawlConstants.CrawlRoomDetail.ROOM_TYPE_MASTER_STR)) {
			roomType = CrawlConstants.CrawlRoomDetail.ROOM_TYPE_MASTER;
		}
		if (roomTypeStr.equals(CrawlConstants.CrawlRoomDetail.ROOM_TYPE_SECONDARY_STR)) {
			roomType = CrawlConstants.CrawlRoomDetail.ROOM_TYPE_SECONDARY;
		}
		checkNonNegativeNumber(roomType, roomTypeKey);
		roomPublishDto.setRoomType(roomType);

		String roomNameKey = "roomName";
		String roomName = roomTypeStr;
		checkStringMaxLength(roomName, roomNameKey, Constants.RoomBase.ROOM_NAME_LENGTH_MAX);
		roomPublishDto.setRoomName(roomName);
		return roomPublishDto;
	}

	/**
	 * 校验Token
	 * 
	 * @throws Exception
	 */
	private void checkToken(HttpServletRequest request) throws Exception {
		if (request == null) {
			return;
		}
		String sellId = RequestUtils.getParameterString(request, "sellId");
		String token = RequestUtils.getParameterString(request, "token");
		boolean tokenValid = isTokenValid(sellId, token);
		if (!tokenValid) {
			throw new BaseException(ErrorMsgCode.ERROR_MSG_API_TOEKN_AUTH_FAIL, "接口Token验证失败");
		}
	}

	/**
	 * 校验token是否与缓存一致
	 * 
	 * @param appId
	 * @param token
	 * @return
	 */
	private boolean isTokenValid(String sellId, String token) {

		String cacheToken = null;
		try {
			cacheToken = houseDao.getHouseToken(sellId, 0);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("Token获取失败, sellId:" + sellId + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_API_TOEKN_AUTH_FAIL, "接口Token验证失败");
		}

		boolean isEqual = StringUtil.isNotEmpty(token) && token.equals(cacheToken);
		if (!isEqual) {
			logger.error(LogUtils.getCommLog("Token验证失败, token:" + token + ", cacheToken:" + cacheToken));
		}

		return isEqual;
	}

	/**
	 * 抛出BaseException
	 * 
	 * @param e
	 */
	private void rethrowBaseException(Exception e) {
		if (e instanceof BaseException) {
			throw (BaseException) e;
		}
	}

	/**
	 * 校验坐标值
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkPositionValue(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			boolean valid = RulesVerifyUtil.verifyPositionValue(positionValue);
			if (!valid) {
				throw new InvalidParameterException("参数格式不正确:" + keyName);
			}
		}
	}

	/**
	 * 校验纬度
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkLatitude(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			Double latitude = StringUtil.parseDouble(positionValue);
			boolean valid = latitude != null && latitude >= Constants.Common.LATITUDE_MIN
					&& latitude <= Constants.Common.LATITUDE_MAX;
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName);
			}
		}
	}

	/**
	 * 校验经度
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkLongitude(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			Double longitude = StringUtil.parseDouble(positionValue);
			boolean valid = longitude != null && longitude >= Constants.Common.LONGITUDE_MIN
					&& longitude <= Constants.Common.LONGITUDE_MAX;
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName);
			}
		}
	}

	/**
	 * 校验字符串长度
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkStringMaxLength(String string, String keyName, int maxLength) {
		if (StringUtil.isNotBlank(string)) {
			boolean valid = RulesVerifyUtil.verifyCsMaxLength(string, maxLength);
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName + ",字符串过长");
			}
		}
	}

	/**
	 * 校验设置
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkSetting(String settingValue, String keyName) {
		if (StringUtil.isNotBlank(settingValue)) {
			boolean valid = RulesVerifyUtil.verifySetting(settingValue);
			String msg = "参数异常:" + keyName;
			if (!valid) {
				throw new InvalidParameterException(msg);
			}
			List<String> houseSettingSingleStringList = HouseUtil.getHouseSettingSingleStringList(settingValue);
			if (CollectionUtils.isNotEmpty(houseSettingSingleStringList)) {
				for (String singleString : houseSettingSingleStringList) {
					Pair<String, String> keyAndNum = HouseUtil.getHouseSettingKeyAndNum(singleString);
					if (keyAndNum == null) {
						throw new InvalidParameterException(msg);
					}
					String key = keyAndNum.getLeft();
					Pair<Integer, Integer> typeAndCode = HouseUtil.getHouseSettingTypeAndCode(key);
					if (typeAndCode == null) {
						throw new InvalidParameterException(msg);
					}
					Integer type = typeAndCode.getLeft();
					if (type == null || type == Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED) {
						throw new InvalidParameterException(msg);
					}
					Integer code = typeAndCode.getRight();
					if (code == null || code == Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED) {
						throw new InvalidParameterException(msg);
					}
					String num = keyAndNum.getRight();
					boolean isNumValid = RulesVerifyUtil.verifyNumber(num);
					if (!isNumValid) {
						throw new InvalidParameterException(msg);
					}
				}
			}
		}
	}

	/**
	 * 校验图片
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkImgs(String imgs, String keyName) {
		if (StringUtil.isNotBlank(imgs)) {
			String[] split = imgs.split(StringUtil.COMMA);
			int IMG_MAX = Constants.HousePicture.HOUSE_IMGS_MAX;
			if (split == null || split.length == 0) {
				throw new InvalidParameterException("参数异常:" + keyName);
			} else if (split.length > IMG_MAX) {
				throw new InvalidParameterException("参数异常:" + keyName + ",图片数量过多");
			}
		}
	}

	/**
	 * 校验房源/房间状态
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkStatus(int status, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_NOT_ON_SALE);
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_PRE_RENT);
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_RENT);
		checkIfValid(status, validKeys, keyName);
	}

	/**
	 * 校验数字非负
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkNonNegativeNumber(long number, String keyName) {
		if (number < 0) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}

	/**
	 * 校验数字非负
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkNonNegativeNumber(double number, String keyName) {
		if (number < 0) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}

	/**
	 * 校验面积
	 * 
	 * @param position
	 * @param keyName
	 */
	private void checkArea(float number, String keyName, int minArea) {
		if (number < minArea) {
			throw new InvalidParameterException("参数异常:" + keyName + ",面积过小");
		}
	}

	/**
	 * 校验电话
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkPhone(String phone, String keyName) {
		if (StringUtil.isNotBlank(phone)) {
			// boolean valid = RulesVerifyUtil.verifyPhone(phone);
			boolean valid = RulesVerifyUtil.verifyCsMaxLength(phone, Constants.HouseBase.HOUSE_PHONE_LENGTH_MAX); // 暂时只校验长度
			if (!valid) {
				throw new InvalidParameterException("参数异常:" + keyName);
			}
		}
	}

	/**
	 * 校验是否有钥匙
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkHaskey(int haskey, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.HAS_KEY_NO);
		validKeys.add(Constants.HouseBase.HAS_KEY_YES);
		checkIfValid(haskey, validKeys, keyName);
	}

	/**
	 * 校验性别
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkGender(int gender, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.GENDER_MALE);
		validKeys.add(Constants.HouseBase.GENDER_FEMALE);
		checkIfValid(gender, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkOrientation(int orientation, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.ORIENTATION_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_SOUTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_EAST_WEST);
		checkIfValid(orientation, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkBuildingType(int buildingType, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_TOWER);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB_AND_TOWER);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SINGLE);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_ROW);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_OVERLAY);
		checkIfValid(buildingType, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkDecoration(int decoration, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.DECORATION_FINE);
		validKeys.add(Constants.HouseBase.DECORATION_BRIEF);
		validKeys.add(Constants.HouseBase.DECORATION_ROUGH);
		validKeys.add(Constants.HouseBase.DECORATION_OLD);
		validKeys.add(Constants.HouseBase.DECORATION_LUXURY);
		validKeys.add(Constants.HouseBase.DECORATION_MEDIUM);
		validKeys.add(Constants.HouseBase.DECORATION_NORMAL);
		checkIfValid(decoration, validKeys, keyName);
	}

	/**
	 * 校验是否独立
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkIndependent(int independent, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.INDEPENDENT);
		validKeys.add(Constants.HouseBase.NOT_INDEPENDENT);
		checkIfValid(independent, validKeys, keyName);
	}

	/**
	 * 校验家财险
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkInsurance(int insurance, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.INSURANCE_EXIST);
		validKeys.add(Constants.HouseBase.INSURANCE_NOT_EXIST);
		checkIfValid(insurance, validKeys, keyName);
	}

	/**
	 * 校验入住时间
	 * 
	 * @param date
	 */
	private void checkCheckIn(Date date, String keyName) {
		if (date != null) {
			Date curDate = new Date();
			if (date.before(curDate) && !DateUtil.isSameDay(date, curDate)) {
				throw new InvalidParameterException("参数异常:" + keyName + ",入住时间不能早于今天");
			}
		}
	}

	/**
	 * 校验建筑年份
	 * 
	 * @param date
	 */
	private void checkBuildingYear(String buildingYear, String keyName) {
		if (StringUtil.isNotBlank(buildingYear)) {
			Date year = DateUtil.parseYear(buildingYear);
			if (year == null) {
				throw new InvalidParameterException("参数异常:" + keyName + ",年份解析失败");
			}
		}
	}

	/**
	 * 校验整分租
	 * 
	 * @param haskey
	 * @param keyName
	 */
	private void checkEntireRent(int entireRent, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseDetail.RENT_TYPE_SHARE);
		validKeys.add(Constants.HouseDetail.RENT_TYPE_ENTIRE);
		validKeys.add(Constants.HouseDetail.RENT_TYPE_BOTH);
		checkIfValid(entireRent, validKeys, keyName);
	}

	/**
	 * 校验参数是否有效
	 * 
	 * @param value
	 * @param validValues
	 * @param keyName
	 */
	private void checkIfValid(int value, List<Integer> validValues, String keyName) {
		if (CollectionUtils.isEmpty(validValues)) {
			return;
		}
		if (!validValues.contains(value)) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}
}
