/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseDataCollectWorker.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:14:47 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.OssConfiguration;
import com.huifenqi.hzf_platform.configuration.SchemaConfiguration;
import com.huifenqi.hzf_platform.context.CompanyConstants;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.CompanyPayMonth;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.location.Area;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.District;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.context.enums.HouseTagsEnum;
import com.huifenqi.hzf_platform.context.enums.RoomTypesEnum;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.location.AreaRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository;
import com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository;
import com.huifenqi.hzf_platform.service.BaiduService;
import com.huifenqi.hzf_platform.service.FileService;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: SchemaHouseDataCollectWorker date: 2016年5月4日 下午6:14:47
 * Description: 收集房源信息
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class SchemaHouseDataCollectWorker implements Runnable {

	private static Log logger = LogFactory.getLog(SchemaHouseDataCollectWorker.class);

	private OssConfiguration ossConfig;

	private LinkedBlockingQueue<HouseDetail> queue;

	private ConcurrentSkipListSet<String> processingHouses;

	private List<HouseDetail> listHouseDetail;

	private HouseDetailRepository houseDetailRepository;

	private HousePictureRepository housePictureRepository;

	private BaiduService baiduService;

	private FileService fileService;

	private SchemaConfiguration schemaConfiguration;

	private SubwayRepository subwayRepository;

	private CityRepository cityRepository;

	private AreaRepository areaRepository;

	private DistrictRepository districtRepository;

	private HouseBaseRepository houseBaseRepository;

	private RoomBaseRepository roomBaseRepository;

	private RedisCacheManager redisCacheManager;

	private CompanyPayMonthRepository companyPayMonthRepository;

	private static final String HOUSE_DATA_COLLECT_FAILED_KEY = "house.data.collect.failed";
	private static final String HOUSE_DATA_COLLECT_SUCCESS_PLACE_KEY = "house.data.collect.success.place";
	private static final String HOUSE_DATA_COLLECT_SUCCESS_SUBWAY_KEY = "house.data.collect.success.subway";
	private static final String HOUSE_DATA_COLLECT_FAIL_SUBWAY_KEY = "house.data.collect.fail.subway";
	private static final String HOUSE_DATA_COLLECT_FAIL_SUBWAY_SELLID_KEY = "house.data.collect.fail.subway.sellid";
	private static final String HOUSE_DATA_COLLECT_SUCCESS_BUS_KEY = "house.data.collect.success.bus";
	private static final String HOUSE_DATA_COLLECT_SUCCESS_SURROUND_KEY = "house.data.collect.success.surround";

	// 地球平均半径
	private static final double EARTH_RADIUS = 6378137;

	// 距离初始化值
	private static final double DISTANCE = 999999999;

	// 无参构造方法
	public SchemaHouseDataCollectWorker() {

	}

	public SchemaHouseDataCollectWorker(LinkedBlockingQueue<HouseDetail> queue,
			ConcurrentSkipListSet<String> processingHouses, HouseDetailRepository houseDetailRepository,
			HousePictureRepository housePictureRepository, BaiduService baiduService, FileService fileService,
			SchemaConfiguration schemaConfiguration, SubwayRepository subwayRepository, CityRepository cityRepository,
			AreaRepository areaRepository, DistrictRepository districtRepository,
			HouseBaseRepository houseBaseRepository, RoomBaseRepository roomBaseRepository,
			RedisCacheManager redisCacheManager, List<HouseDetail> listHouseDetail, OssConfiguration ossConfig,
			CompanyPayMonthRepository companyPayMonthRepository) {
		this.queue = queue;
		this.processingHouses = processingHouses;
		this.houseDetailRepository = houseDetailRepository;
		this.housePictureRepository = housePictureRepository;
		this.baiduService = baiduService;
		this.fileService = fileService;
		this.schemaConfiguration = schemaConfiguration;
		this.subwayRepository = subwayRepository;
		this.cityRepository = cityRepository;
		this.areaRepository = areaRepository;
		this.districtRepository = districtRepository;
		this.houseBaseRepository = houseBaseRepository;
		this.roomBaseRepository = roomBaseRepository;
		this.redisCacheManager = redisCacheManager;
		this.listHouseDetail = listHouseDetail;
		this.ossConfig = ossConfig;
		this.companyPayMonthRepository = companyPayMonthRepository;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(LogUtils.getCommLog(String.format("收集待处理房源的线程出错:%s", e.toString())));
			}
		}
	}

	/**
	 * 工作方法
	 */
	private void doWork() throws Exception {
		HouseDetail house = queue.take();
		try {
			// 重置house tag为空
			house.setHouseTag("");
			boolean cFlag = true;
			boolean successFlag = true;
			StringBuilder errorInfo = new StringBuilder();
			logger.info(LogUtils.getCommLog(String.format("开始收集房间(%s)的数据.", house.getSellId())));
			// String collectFlag =
			// redisCacheManager.getHash(HOUSE_DATA_COLLECT_FAILED_KEY,
			// house.getSellId());

			// if (collectFlag == null) {
				// 收集图片信息
				try {
					collectionPicData(house);
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}

				// 收集位置信息
				try {
					successFlag = getCollectSuccessFlag(HOUSE_DATA_COLLECT_SUCCESS_PLACE_KEY, house.getSellId());
					if (!successFlag) {
						collectionPlaceData(house);
						redisCacheManager.putHash(HOUSE_DATA_COLLECT_SUCCESS_PLACE_KEY, house.getSellId(), "success");
					}
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}

				// 收集地铁信息
				try {
					successFlag = getCollectSuccessFlag(HOUSE_DATA_COLLECT_SUCCESS_SUBWAY_KEY, house.getSellId());
					if (!successFlag) {
						collectionSubwayData(house);
						redisCacheManager.putHash(HOUSE_DATA_COLLECT_SUCCESS_SUBWAY_KEY, house.getSellId(), "success");
					}
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}
				// 收集公交信息
				try {
					successFlag = getCollectSuccessFlag(HOUSE_DATA_COLLECT_SUCCESS_BUS_KEY, house.getSellId());
					if (!successFlag) {
						collectionBusData(house);
						redisCacheManager.putHash(HOUSE_DATA_COLLECT_SUCCESS_BUS_KEY, house.getSellId(), "success");
					}
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}
				// 收集周边信息
				try {
					successFlag = getCollectSuccessFlag(HOUSE_DATA_COLLECT_SUCCESS_SURROUND_KEY, house.getSellId());
					if (!successFlag) {
						collectionPeripheryData(house);
						redisCacheManager.putHash(HOUSE_DATA_COLLECT_SUCCESS_SURROUND_KEY, house.getSellId(),
								"success");
					}
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}
				// 收集房源Tag
				try {
					collectionHouseTag(house);
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}
				// 收集房间Tag
				try {
					collectionRoomTag(house);
				} catch (Exception e) {
					cFlag = false;
					errorInfo.append(e.toString());
					logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
				}
				// 收集成功后，设置房间的状态为已处理,并更新数据库
				if (cFlag) {
					house.setIsRun(Constants.HouseDetail.HOUSE_RUNED);
					redisCacheManager.deleteHash(HOUSE_DATA_COLLECT_FAILED_KEY, house.getSellId());
				} else {
					house.setIsRun(Constants.HouseDetail.HOUSE_FAILD);
					redisCacheManager.putHash(HOUSE_DATA_COLLECT_FAILED_KEY, house.getSellId(), errorInfo.toString());
				}
				house.setUpdateTime(new Date());
				logger.info(LogUtils.getCommLog(String.format("收集待处理队列剩余条数(%s).", queue.size())));
				houseDetailRepository.save(house);
				
				
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("处理房间(%s)出错:%s", house.getSellId(), e.toString())));
		} finally {
			// 从正在处理的集合中移除该房间
			processingHouses.remove(house.getSellId());
			logger.info(LogUtils.getCommLog(String.format("房间(%s)的数据收集结束.", house.getSellId())));
		}
	}

	/**
	 * 收集位置数据
	 * 
	 * @param house
	 */
	private void collectionPlaceData(HouseDetail house) throws Exception {
		// 根据坐标获取城市信息
		JsonObject result = baiduService.getAddressByLocation(house.getBaiduLatitude(), house.getBaiduLongitude());
		JsonObject addressInfo = result.getAsJsonObject("addressComponent");
		String province = addressInfo.getAsJsonPrimitive("province").getAsString();
		String city = addressInfo.getAsJsonPrimitive("city").getAsString();
		String district = addressInfo.getAsJsonPrimitive("district").getAsString();
		house.setProvince(province);
		house.setCity(city);
		house.setDistrict(district);
		// 拼接详细地址
		StringBuilder detailAddress = new StringBuilder();
		detailAddress.append(city).append(district).append(house.getCommunityName());
//		String buidingNo = house.getBuildingNo();
//		if (!StringUtil.isBlank(buidingNo)) {
//			detailAddress.append(buidingNo).append("号楼");
//		}

		house.setAddress(detailAddress.toString());

		// 根据城市名称查询对应的城市ID
		// City tCity = cityRepository.findCityByName(city.replaceAll("市$",
		// ""));
		City tCity = cityRepository.findCityByName(city);
		if (tCity == null) {
			throw new Exception(String.format("该城市(%s)没有对应的城市ID信息", city));
		}
		house.setCityId(tCity.getCityId());

		// 区域ID
		// District tDistrict =
		// districtRepository.findDistrictByName(district.replaceAll("区$", ""));
		// District tDistrict = districtRepository.findDistrictByName(district);
		District tDistrict = districtRepository.findDistrictByNameAndCityId(district, tCity.getCityId());
		if (tDistrict == null) {
			throw new Exception(String.format("该区域(%s)没有对应的区域ID信息", district));
		}
		house.setDistrictId(tDistrict.getDistrictId());

		// 根据坐标计算归属商圈信息
		List<Area> areaList = areaRepository.findAreasByDistrictId(tDistrict.getDistrictId());
		Area zoneArea = null;
		double temp = DISTANCE;
		if (!areaList.isEmpty()) {
			for (Area area : areaList) {
				String la = area.getCenter().substring(area.getCenter().indexOf(",") + 1);
				String lo = area.getCenter().substring(0, area.getCenter().indexOf(","));
				double distance = getDistance(Double.parseDouble(house.getBaiduLongitude()),
						Double.parseDouble(house.getBaiduLatitude()), Double.parseDouble(lo), Double.parseDouble(la));
				if (distance < temp) {
					temp = distance;
					zoneArea = area;
				}
			}
			house.setBizName(zoneArea.getName());
			// 商圈ID
			// Area area = areaRepository.findAreaByName(zoneArea.getName());
			// if (area == null) {
			// throw new Exception(String.format("该商圈(%s)没有对应的商圈ID信息",
			// house.getBizName()));
			// }
			if (zoneArea != null) {
				house.setBizId(zoneArea.getAreaId());
			}
		}
	}

	/**
	 * 收集地铁数据
	 * 
	 * @param house
	 * @throws Exception
	 */
	private void collectionSubwayData(HouseDetail house) throws Exception {
		List<String> keywords = new ArrayList<>();
		keywords.add("地铁站");
		List<String> tags = new ArrayList<>();
		// tags.add("交通设施/地铁站");
		Map<String, String> sort = new HashMap<>();
		sort.put("industry_type", "life");
		sort.put("sort_name", "distance");
		sort.put("sort_rule", "1");
		JsonArray result = baiduService.radiusPlaceSearch(keywords, tags,
				String.valueOf(schemaConfiguration.getSubwayRadius()), house.getBaiduLatitude(),
				house.getBaiduLongitude(), "1", sort);
		// 没有查到数据
		if (result == null || result.size() == 0) {
			house.setSubwayDistance(0);//重置地铁信息
			house.setSubway("");//重置地铁信息
			return;
		}
		JsonObject subwayDetail = result.get(0).getAsJsonObject();
		int distance = subwayDetail.getAsJsonObject("detail_info").getAsJsonPrimitive("distance").getAsInt();
		// 用距离除70m/分钟
//		int time = distance / schemaConfiguration.getWalkSpeed();
		BigDecimal bigDis = new BigDecimal(distance);
		int time = bigDis.divide(new BigDecimal(schemaConfiguration.getWalkSpeed()+""), BigDecimal.ROUND_HALF_UP).intValue();

		// 判断该房源是否属于地铁周边,如果属于则打该tag
//		if (distance <= schemaConfiguration.getSubwayNearRadius()) {
//			addHouseTag(house,  String.valueOf(Constants.HouseDetail.HOUSE_TAG_CLOSE_TO_SUBWAY));
//		}

		StringBuilder subwayInfo = new StringBuilder();

		// subwayInfo.append("距离").append(subwayDetail.getAsJsonPrimitive("name").getAsString());
		subwayInfo.append("距");
//		// 查询该站的地铁线路
//		String lat = subwayDetail.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
//		String lng = subwayDetail.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();
		String uid = subwayDetail.getAsJsonPrimitive("uid").getAsString();
		List<Subway> subways = subwayRepository.findSubWayByUid(uid);
		
		if(CollectionUtils.isEmpty(subways) &&  distance> 0){//地铁站点数据有问题
			house.setSubwayDistance(0);//重置地铁信息
			house.setSubway("");//重置地铁信息
			City city = cityRepository.findCityStatusById(house.getCityId());
			if(city != null){//线上展现城市
				redisCacheManager.putHash(HOUSE_DATA_COLLECT_FAIL_SUBWAY_KEY, house.getCityId()+"-"+subwayDetail.getAsJsonPrimitive("name").getAsString(),subwayDetail.toString());
				redisCacheManager.putHash(HOUSE_DATA_COLLECT_FAIL_SUBWAY_SELLID_KEY, house.getSellId(), "fail");
			}
			
		}
		
		if (CollectionUtils.isNotEmpty(subways)) {
			StringBuilder lineId = new StringBuilder();
			StringBuilder stationId = new StringBuilder();
			StringBuilder st = new StringBuilder();
			for (Subway subway : subways) {
				if (st.length() > 0) {
					st.append("/");
					lineId.append(",");
					stationId.append(",");
				}
				st.append(subway.getSubwayLine());
				lineId.append(subway.getSubwayLineId());
				stationId.append(subway.getId());
				// 设置地铁ID和地铁站ID
				house.setSubwayLineId(lineId.toString());
				house.setSubwayStationId(stationId.toString());
			}
			// subwayInfo.append("(").append(st.toString()).append(")");
			subwayInfo.append(st.toString());
			subwayInfo.append(subwayDetail.getAsJsonPrimitive("name").getAsString());
			subwayInfo.append("站");
			subwayInfo.append(distance).append("米,").append("步行约").append(time).append("分钟");
			house.setSubway(subwayInfo.toString());
			house.setSubwayDistance(distance);
		}
		house.setSubwayDistance(distance);
		// subwayInfo.append(subwayDetail.getAsJsonPrimitive("name").getAsString());
		// subwayInfo.append("站");
		// subwayInfo.append(distance).append("米,").append("步行约").append(time).append("分钟");
		// house.setSubway(subwayInfo.toString());
		// house.setSubwayDistance(distance);
	}

	/**
	 * 收集公交数据
	 * 
	 * @param house
	 * @throws Exception
	 */
	private void collectionBusData(HouseDetail house) throws Exception {
		List<String> keywords = new ArrayList<>();
		keywords.add("公交车站");
		List<String> tags = new ArrayList<>();
		tags.add("交通设施/公交车站");
		Map<String, String> sort = new HashMap<>();
		sort.put("industry_type", "life");
		sort.put("sort_name", "distance");
		sort.put("sort_rule", "1");
		JsonArray result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", sort);
		// 没有查到数据
		if (result == null || result.size() == 0) {
			return;
		}
		StringBuilder stationInfo = new StringBuilder();
		for (int i = 0; i < result.size(); i++) {
			JsonObject busDetail = result.get(i).getAsJsonObject();
			if (stationInfo.indexOf(busDetail.getAsJsonPrimitive("address").getAsString()) != -1) {
				continue;
			}
			if (stationInfo.length() > 0) {
				stationInfo.append(";");
			}
			stationInfo.append(busDetail.getAsJsonPrimitive("address").getAsString());
		}

		house.setBusStations(stationInfo.toString());
	}

	/**
	 * 获取房源周边的信息：如：周边的餐厅和超市
	 * 
	 * @param house
	 * @throws Exception
	 */
	private void collectionPeripheryData(HouseDetail house) throws Exception {

		StringBuilder peripheryInfo = new StringBuilder();
		// 搜索超市
		List<String> keywords = new ArrayList<>();
		keywords.add("超市");
		List<String> tags = new ArrayList<>();
		tags.add("购物/超市");
		JsonArray result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", null);
		spliceData(peripheryInfo, result);

		// 搜索购物中心
		keywords.clear();
		keywords.add("购物");
		tags.clear();
		tags.add("购物/购物中心");
		result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", null);
		spliceData(peripheryInfo, result);

		// 搜索餐厅
		keywords.clear();
		keywords.add("餐厅");
		keywords.add("美食");
		keywords.add("餐馆");
		tags.clear();
		tags.add("美食");
		result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", null);
		spliceData(peripheryInfo, result);

		// 搜索电影院
		keywords.clear();
		keywords.add("电影院");
		tags.clear();
		tags.add("休闲娱乐/电影院");
		result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", null);
		spliceData(peripheryInfo, result);

		// 搜索理发店
		keywords.clear();
		keywords.add("理发店");
		tags.clear();
		tags.add("丽人");
		result = baiduService.radiusPlaceSearch(keywords, tags, null, house.getBaiduLatitude(),
				house.getBaiduLongitude(), "2", null);
		spliceData(peripheryInfo, result);

		house.setSurround(peripheryInfo.toString());
	}

	private void spliceData(StringBuilder data, JsonArray result) {
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if (data.length() > 0) {
					data.append(",");
				}
				JsonObject datailInfo = result.get(i).getAsJsonObject();
				data.append(datailInfo.getAsJsonPrimitive("name"));
			}
		}
	}

	/**
	 * 收集图片数据
	 * 
	 * @param house
	 */
	private void collectionPicData(HouseDetail house) throws Exception {
		List<HousePicture> pictures = housePictureRepository.findAllBySellId(house.getSellId());
		StringBuilder error = new StringBuilder();
		for (HousePicture pic : pictures) {
			try {
				// 如果本地已经有信息,则说明已经收集过,不再进行收集
				if (!StringUtil.isBlank(pic.getPicRootPath())) {
					continue;
				}
				Pageable pageable = new PageRequest(0, 1);// 分页查询，取一张
				
				List<HousePicture> repeatList = null;
				if (StringUtils.isNotEmpty(pic.getPicDhash())) {
					repeatList = housePictureRepository.findByPicDhash(pageable, pic.getPicDhash());
				}
				if (CollectionUtils.isNotEmpty(repeatList)) {//如果有重复的图片，则取下载过的rootPath
					pic.setPicRootPath(repeatList.get(0).getPicRootPath());
				} else {
					String returnUrl = fileService.downImage(pic.getPicWebPath(), pic.getSellId());
					pic.setPicRootPath(ossConfig.getDownloadEndpoint() + File.separator + returnUrl);
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("房间(%s)的图片(%s)下载失败:%s", house.getSellId(),
						pic.getId(), ExceptionUtils.getStackTrace(e))));
				error.append(pic.getId()).append(";");
			}
		}
		// 更新数据库
		housePictureRepository.save(pictures);

		// 记录处理失败的图片
		if (error.length() > 0) {
			throw new Exception(String.format("以下图片处理失败:%s", error.toString()));
		}
	}

	/**
	 * 收集房间的tag信息
	 * 
	 * @param house
	 * @throws Exception
	 */
	private void collectionHouseTag(HouseDetail house) throws Exception {
		StringBuilder hTag = new StringBuilder();
		HouseBase houseBase = houseBaseRepository.findBySellId(house.getSellId());
		house.setIsPayMonth(CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_NO);
		if (houseBase != null) {
			// 判断是否为首次发布
			// if (houseBase.getFirstPubDate() != null &&
			// houseBase.getFirstPubDate().equals(houseBase.getPubDate())) {
			// hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_PUB_FIRST_TIME);
			// }
			// 判断是否为为月付
			if (houseBase.getPeriodMonth() == 1) {
//				hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_PERIOD_MONTH_ONE);
				hTag.append(",").append(HouseTagsEnum.PERIOD_MONTH_ONE.getCode());
			}
			String companyId = houseBase.getCompanyId();
			if (StringUtil.isNotBlank(companyId)) {
				CompanyPayMonth companyPayMonth = companyPayMonthRepository
						.findCompanyPayMonthBycompanyIdHzf(companyId);
				if (companyPayMonth != null) {
					if (companyPayMonth.getPayStatus() == CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_YES) {
//						hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_COMPANY_PAY_STATUS);
						hTag.append(",").append(HouseTagsEnum.COMPANY_PAY_STATUS.getCode());
						house.setIsPayMonth(CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_YES);
					}
				}
			}
		}
		
		
		// 判断朝向是否朝南
		if (house.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH
				|| house.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH_WEST
				|| house.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH_EAST
				|| house.getOrientations() == Constants.HouseBase.ORIENTATION_NORTH_SOUTH) {
//			hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_ORIENTATION_SOUTH);
			hTag.append(",").append(HouseTagsEnum.ORIENTATION_SOUTH.getCode());
		}

		// 判断是否独立卫生间
		// if (house.getToilet() == Constants.HouseBase.INDEPENDENT) {
		// hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_INDEPENDENT_TOILET);
		// }

		// 判断是否独立阳台
		// if (house.getBalcony() == Constants.HouseBase.INDEPENDENT) {
		// hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_INDEPENDENT_BALCONY);
		// }

		// 判断是否为精装修
		// if (house.getDecoration() == Constants.HouseBase.DECORATION_FINE) {
		// hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_DECORATION_FINE);
		// }
		
		// 判断该房源是否属于地铁周边,如果属于则打该tag
		if (house.getSubwayDistance() != 0) {
			int distance = Integer.valueOf(house.getSubwayDistance());
			if (distance <= schemaConfiguration.getSubwayNearRadius()) {
//				hTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_CLOSE_TO_SUBWAY);
				hTag.append(",").append(HouseTagsEnum.CLOSE_TO_SUBWAY.getCode());
			}
		}
		
		hTag.replace(0, 1, "");
		addHouseTag(house, hTag.toString());
	}

	private void collectionRoomTag(HouseDetail house) throws Exception {
		StringBuilder tTag = new StringBuilder();
		boolean flag = false;
		// 判断房间是否离地铁近
		if (house.getSubwayDistance() != 0) {
			int distance = Integer.valueOf(house.getSubwayDistance());
			if (distance <= schemaConfiguration.getSubwayNearRadius()) {
//				tTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_CLOSE_TO_SUBWAY);
				tTag.append(",").append(HouseTagsEnum.CLOSE_TO_SUBWAY.getCode());
			}
		}
		
		List<RoomBase> roomBases = roomBaseRepository.findAllBySellId(house.getSellId());
		if (roomBases == null || roomBases.isEmpty()) {
			return;
		}
		
		//支持月付
		HouseBase houseBase = houseBaseRepository.findBySellId(house.getSellId());
		if (houseBase != null) {
			
			String companyId = houseBase.getCompanyId();
			if (StringUtil.isNotBlank(companyId)) {
				CompanyPayMonth companyPayMonth = companyPayMonthRepository.findCompanyPayMonthBycompanyIdHzf(companyId);
				if (companyPayMonth != null) {
					if (companyPayMonth.getPayStatus() == CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_YES) {
						flag = true;
					}
				}
			}
		}


		Date updateTime = new Date();
		for (RoomBase room : roomBases) {
			StringBuilder rTag = new StringBuilder();
			rTag.append(tTag.toString());
			// 判断是否为首次发布
			// if (room.getFirstPubDate() != null &&
			// room.getFirstPubDate().equals(room.getPubDate())) {
			// rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_PUB_FIRST_TIME);
			// }
			// 判断是否为为月付
			if (room.getPeriodMonth() == 1) {
//				rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_PERIOD_MONTH_ONE);
				rTag.append(",").append(HouseTagsEnum.PERIOD_MONTH_ONE.getCode());
			}
			// 判断朝向是否朝南
			// if (room.getOrientations() ==
			// Constants.HouseBase.ORIENTATION_SOUTH) {
			// rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_ORIENTATION_SOUTH);
			// }

			// 判断朝向是否朝南
			if (room.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH
					|| room.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH_WEST
					|| room.getOrientations() == Constants.HouseBase.ORIENTATION_SOUTH_EAST
					|| room.getOrientations() == Constants.HouseBase.ORIENTATION_NORTH_SOUTH) {
//				rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_ORIENTATION_SOUTH);
				rTag.append(",").append(HouseTagsEnum.ORIENTATION_SOUTH.getCode());
			}
			
			// 判断是否独立卫生间
			if (room.getToilet() == Constants.HouseBase.INDEPENDENT) {
//				rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_INDEPENDENT_TOILET);
				rTag.append(",").append(HouseTagsEnum.INDEPENDENT_TOILET.getCode());
			}

			// 判断是否独立阳台
			if (room.getBalcony() == Constants.HouseBase.INDEPENDENT) {
//				rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_INDEPENDENT_BALCONY);
				rTag.append(",").append(HouseTagsEnum.INDEPENDENT_BALCONY.getCode());
			}

			// 收集月付标签和标记
			room.setIsPayMonth(CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_NO);
			if(flag){
//				rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_COMPANY_PAY_STATUS);
				rTag.append(",").append(HouseTagsEnum.COMPANY_PAY_STATUS.getCode());
				room.setIsPayMonth(CompanyConstants.CompanyHouseDetail.IS_PAY_STATUS_YES);
			}
			
			// 判断是否为主卧
			if (room.getRoomType() == RoomTypesEnum.MASTER.getCode()) {
				rTag.append(",").append(HouseTagsEnum.MASTER_ROOM.getCode());
			}
			// 判断是否为精装修
			// if (room.getDecoration() == Constants.HouseBase.DECORATION_FINE)
			// {
			// rTag.append(",").append(Constants.HouseDetail.HOUSE_TAG_DECORATION_FINE);
			// }
			
			rTag.replace(0, 1, "");
			room.setRoomTag(rTag.toString());
			room.setUpdateTime(updateTime);
		}
		roomBaseRepository.save(roomBases);
	}

	/**
	 * 添加的信息的houseTag
	 * 
	 * @param house
	 * @param tag
	 */
	private void addHouseTag(HouseDetail house, String tag) {
		String houseTag = house.getHouseTag();
		if (StringUtil.isEmpty(houseTag)) {
			houseTag = tag;
		} else {
			houseTag = houseTag + "," + tag;
		}
		house.setHouseTag(houseTag);
	}

	/**
	 * 判断redis的value是否存在
	 * 
	 * @param key
	 * @param houseSellId
	 */
	public boolean getCollectSuccessFlag(String key, String houseSellId) {
		boolean cFlag = true;
		String value = null;
		try {
			value = redisCacheManager.getHash(key, houseSellId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (value == null) {
			cFlag = false;
		}
		return cFlag;
	}

	// 把经纬度转为度（°）
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
}
