/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.comm.Request;
import com.huifenqi.hzf_platform.configuration.QueryConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.ComplaintSaveDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseOptHistoryRedisDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.SaasHousePublishDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ComplaintQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseRecommendInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseRecommendQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.RoomQueryDto;
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.context.entity.house.Apartment;
import com.huifenqi.hzf_platform.context.entity.house.Complaint;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseCollection;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseOpereteRecord;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;
import com.huifenqi.hzf_platform.context.entity.house.HouseToken;
import com.huifenqi.hzf_platform.context.entity.house.OrderCustom;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.enums.ApproveStatusEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository;
import com.huifenqi.hzf_platform.dao.repository.house.ComplaintRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseCollectionRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseOperateRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseSettingRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseTokenRepository;
import com.huifenqi.hzf_platform.dao.repository.house.OrderCustomRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.HouseSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.HzfHousesSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.RoomSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.usercomm.dao.AgencyConfRepository;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 * 
 */
@Repository
public class HouseDao {

	private static final Log logger = LogFactory.getLog(HouseDao.class);

	@Autowired
	private HouseBaseRepository houseBaseRepository;

	@Autowired
	private HouseDetailRepository houseDetailRepository;

	@Autowired
	private HouseSettingRepository houseSettingRepository;

	@Autowired
	private HousePictureRepository housePictureRepository;

	@Autowired
	private RoomBaseRepository roomBaseRepository;

	@Autowired
	private HouseTokenRepository houseTokenRepository;

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private ApartmentRepository apartmentRepository;

	@Autowired
	private HouseRecommendRepository houseRecommendRepository;

	@Autowired
	private HouseSolrRepository houseSolrRepository;

	@Autowired
	private HzfHousesSolrRepository hzfHousesSolrRepository;

	@Autowired
	private RoomSolrRepository roomSolrRepository;

	@Autowired
	private QueryConfiguration queryConfiguration;

	@Autowired
	private OrderCustomRepository orderCustomRepository;

	@Autowired
	private CrawlHouseDetailRepository crawlHouseDetailRepository;

	@Autowired
	private AgencyConfRepository agencyConfRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private RedisCacheManager redisCacheManager;
	private static final String AGENCY_SUPPORT_HFQ_KEY = "agency.support.hfq";
	
	private static final String HOUSE_OPT_HISTORY_KEY = "house.opt.history";
	private static final String HOUSE_OPT_RECORD_KEY = "house.opt.record";

	@Autowired
	private HouseCollectionRepository houseCollectionRepository;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private AgencyManageRepository agencyManageRepository;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;

	/**
	 * 搜索房源
	 * 
	 * @return
	 */
	public HouseSearchResultDto getHouseSearchResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList,List<String> collectIdList) {

		int entireRent = houseSearchDto.getEntireRent();

		HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();

		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			Page<HouseSolrResult> resultPage = houseSolrRepository.findAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房源结果为空"));
				return null;
			}

			List<HouseSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByHouse(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
			Page<RoomSolrResult> resultPage = roomSolrRepository.findAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByRoom(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			List<HouseSearchResultInfo> houseSearchResultInfoListAll = new ArrayList<>();

			// 查询合租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			Page<RoomSolrResult> resultRoomPage = roomSolrRepository.findAllByMultiCondition(houseSearchDto);

			// 查询整租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			Page<HouseSolrResult> resultHousePage = houseSolrRepository.findAllByMultiCondition(houseSearchDto);
			if (resultRoomPage == null && resultHousePage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resulRoomtList = resultRoomPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
					.getHouseSearchResultInfoListByRoom(resulRoomtList, collectIdList);

			List<HouseSolrResult> resulHousetList = resultHousePage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
					.getHouseSearchResultInfoListByHouse(resulHousetList, collectIdList);

			if (houseSearchResultInfoListHouse != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListHouse);
			}
			if (houseSearchResultInfoListRoom != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListRoom);
			}

			if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll);
			}
		}

		return houseSearchResultDto;
	}
	

	/**
     * 中介打散搜索房源
     * changmingwei
     * @return HouseSearchResultDto
     */
    public HouseSearchResultDto getNewHouseSearchResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList) {              
                HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();    
                //获取房源打散 中介公司循环次数
                int forCount = Constants.HouseListConfig.FOREACH_COUNT;
                City city = cityRepository.findCityById(houseSearchDto.getCityId());
                if(city != null){
                    forCount = city.getForCount();
                }
                
                int entireRent = houseSearchDto.getEntireRent();
                if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
                    GroupResult<HouseSolrResult> houseFieldGroup  = houseSolrRepository.findNewAllByMultiCondition(houseSearchDto);
                    if (houseFieldGroup == null) {
                        logger.error(LogUtils.getCommLog("房源打散搜索房源结果为空"));
                        return null;
                    }
                    houseSearchResultDto = getHouseSearchResultDto(forCount,houseFieldGroup,Constants.HouseListConfig.RESULT_COUNT_SEARCH);
                }
                if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
                    GroupResult<RoomSolrResult> roomFieldGroup = roomSolrRepository.findNewAllByMultiCondition(houseSearchDto);
                    if (roomFieldGroup == null) {
                        logger.error(LogUtils.getCommLog("房源打散搜索房源结果为空"));
                        return null;
                    }
                    houseSearchResultDto = getRoomSearchResultDto(forCount,roomFieldGroup,Constants.HouseListConfig.RESULT_COUNT_SEARCH);
                }
                if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
                    
                    //房源打散-share
                    HouseSearchResultDto  roomResult = new HouseSearchResultDto();
                    houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
                    GroupResult<RoomSolrResult> roomFieldGroup = roomSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                    roomResult = getRoomSearchResultDto(forCount,roomFieldGroup,Constants.HouseListConfig.MONTHLY_PAY_HOUSE_COUNT);
                    
                    //房源打散-entire
                    HouseSearchResultDto  houseResult = new HouseSearchResultDto();
                    houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
                    if(!StringUtil.isEmpty(houseSearchDto.getcBedRoomNums())){//租房宝典,白领优选筛选合租主卧和整租一居室的房源
                        houseSearchDto.setKeyword(null);
                    }
                    GroupResult<HouseSolrResult> houseFieldGroup  = houseSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                    //还原entireRent的值, 否则会导致以后的查询只有整租的问题
                    houseSearchDto.setEntireRent(entireRent);
                    
                    if (houseFieldGroup == null && roomFieldGroup == null) {
                        logger.error(LogUtils.getCommLog("房源打散搜索房源结果为空"));
                        return null;
                    }
                    
                    houseResult = getHouseSearchResultDto(forCount,houseFieldGroup,Constants.HouseListConfig.MONTHLY_PAY_HOUSE_COUNT);
                    
                    if (roomResult == null && houseResult == null) {
                        logger.error(LogUtils.getCommLog("房源打散搜索房源结果为空"));
                        return null;
                    }
                    
                    //合并整租分租结果集
                    List<HouseSearchResultInfo> infoAllList = new ArrayList<HouseSearchResultInfo>();
                    if (roomResult != null) {
                        infoAllList.addAll(roomResult.getSearchHouses());
                    }
                    if (houseResult != null) {
                        infoAllList.addAll(houseResult.getSearchHouses());
                    }
                    
                    if (CollectionUtils.isNotEmpty(infoAllList)) {
                        houseSearchResultDto.setSearchHouses(infoAllList);
                    }
                }
                return houseSearchResultDto;        
    }

	/**
	 * 搜索房源 update by arison
	 *
	 * @return
	 */
	public HouseSearchResultDto getHouseSearchResultDtoNew(HouseSearchDto houseSearchDto, List<String> agencyIdList) {
		// 获取用户收藏房源标识
		List<String> collectIdList = new ArrayList<String>();
		List<HouseCollection> houseCollectionList = new ArrayList<HouseCollection>();
		long userId = 0;
		String sessionId = "";
		String platform = "";
		try {
			platform = RequestUtils.getParameterString(Request.getRequest().getHttpServletRequest(), "platform", null);
			sessionId = RequestUtils.getParameterString(Request.getRequest().getHttpServletRequest(), "sid", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(platform) && !StringUtil.isEmpty(sessionId)) {
			userId = sessionManager.getUserIdFromSession();
			houseCollectionList = houseCollectionRepository.getHouseCollectionListByUserId(userId);
			if (CollectionUtils.isNotEmpty(houseCollectionList)) {
				for (HouseCollection houseCollection : houseCollectionList) {
					String sellId = houseCollection.getSellId();
					if (!collectIdList.contains(sellId)) {
						collectIdList.add(sellId);
					}
				}
			}
		}

		int entireRent = houseSearchDto.getEntireRent();

		HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();

		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			Page<HouseSolrResult> resultPage = houseSolrRepository.findAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房源结果为空"));
				return null;
			}

			List<HouseSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByHouse(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
			Page<RoomSolrResult> resultPage = roomSolrRepository.findAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByRoom(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			List<HouseSearchResultInfo> houseSearchResultInfoListAll = new ArrayList<>();
			// houseSearchDto.setPageSize(houseSearchDto.getPageSize() / 2);

			// 查询合租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			Page<RoomSolrResult> resultRoomPage = roomSolrRepository.findAllByMultiCondition(houseSearchDto);

			// 查询整租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			Page<HouseSolrResult> resultHousePage = houseSolrRepository.findAllByMultiCondition(houseSearchDto);
			if (resultRoomPage == null && resultHousePage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resulRoomtList = resultRoomPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
					.getHouseSearchResultInfoListByRoom(resulRoomtList, collectIdList);

			List<HouseSolrResult> resulHousetList = resultHousePage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
					.getHouseSearchResultInfoListByHouse(resulHousetList, collectIdList);

			if (houseSearchResultInfoListHouse != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListHouse);
			}
			if (houseSearchResultInfoListRoom != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListRoom);
			}

			if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll);
			}
			/*
			 * List<HouseSearchResultInfo> houseSearchResultInfoListAll = new
			 * ArrayList<>();
			 * houseSearchDto.setPageSize(houseSearchDto.getPageSize());
			 * Page<HzfHousesSolrResult>
			 * resulHousetList=hzfHousesSolrRepository.findAllByMultiCondition(
			 * houseSearchDto);
			 * 
			 * List<HouseSearchResultInfo> housesSearchResultInfoListHouse =
			 * HouseUtil
			 * .getHzfHousesSearchResultInfo(resulHousetList.getContent(),
			 * collectIdList);
			 * 
			 * if (housesSearchResultInfoListHouse != null) {
			 * houseSearchResultInfoListAll.addAll(
			 * housesSearchResultInfoListHouse); }
			 * 
			 * if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
			 * houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll
			 * ); }
			 */
		}

		return houseSearchResultDto;
	}

	/**
	 * 条件筛选 搜索房源
	 * 
	 * @return
	 */
	public HouseSearchResultDto getHouseResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList,List<String> collectIdList) {

		int entireRent = houseSearchDto.getEntireRent();

		HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();

		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			Page<HouseSolrResult> resultPage = houseSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房源结果为空"));
				return null;
			}

			List<HouseSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByHouse(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
			Page<RoomSolrResult> resultPage = roomSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByRoom(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			List<HouseSearchResultInfo> houseSearchResultInfoListAll = new ArrayList<>();
			// houseSearchDto.setPageSize(houseSearchDto.getPageSize() / 2);

			// 查询合租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			Page<RoomSolrResult> resultRoomPage = roomSolrRepository.getAllByMultiCondition(houseSearchDto,
					agencyIdList);

			// 查询整租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			if (!StringUtil.isEmpty(houseSearchDto.getcBedRoomNums())) {// 租房宝典,白领优选筛选合租主卧和整租一居室的房源
				houseSearchDto.setKeyword(null);
			}
			Page<HouseSolrResult> resultHousePage = houseSolrRepository.getAllByMultiCondition(houseSearchDto,
					agencyIdList);

			if (resultRoomPage == null && resultHousePage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resulRoomtList = resultRoomPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
					.getHouseSearchResultInfoListByRoom(resulRoomtList, collectIdList);

			List<HouseSolrResult> resulHousetList = resultHousePage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
					.getHouseSearchResultInfoListByHouse(resulHousetList, collectIdList);

			if (houseSearchResultInfoListHouse != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListHouse);
			}
			if (houseSearchResultInfoListRoom != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListRoom);
			}

			if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll);
			}
		}

		return houseSearchResultDto;
	}

	/**
	 * 收藏夹和足迹列表 
	 * 搜索房源
	 * 
	 * @return
	 */
	public HouseSearchResultDto searchHouseSearchResultDto(HouseSearchDto houseSearchDto) {
		// 获取用户收藏房源标识
		List<String> collectIdList = new ArrayList<String>();
		List<HouseCollection> houseCollectionList = new ArrayList<HouseCollection>();
		long userId = 0;
		String sessionId = "";
		String platform = "";
		try {
			platform = RequestUtils.getParameterString(Request.getRequest().getHttpServletRequest(), "platform", null);
			sessionId = RequestUtils.getParameterString(Request.getRequest().getHttpServletRequest(), "sid", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(platform) && !StringUtil.isEmpty(sessionId)) {
			userId = sessionManager.getUserIdFromSession();
			houseCollectionList = houseCollectionRepository.getHouseCollectionListByUserId(userId);
			if (CollectionUtils.isNotEmpty(houseCollectionList)) {
				for (HouseCollection houseCollection : houseCollectionList) {
					String sellId = houseCollection.getSellId();
					if (!collectIdList.contains(sellId)) {
						collectIdList.add(sellId);
					}
				}
			}
		}

		int entireRent = houseSearchDto.getEntireRent();

		HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();

		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			Page<HouseSolrResult> resultPage = houseSolrRepository.searchAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房源结果为空"));
				return null;
			}

			List<HouseSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByHouse(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
			Page<RoomSolrResult> resultPage = roomSolrRepository.searchAllByMultiCondition(houseSearchDto);

			if (resultPage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resultList = resultPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoList = HouseUtil
					.getHouseSearchResultInfoListByRoom(resultList, collectIdList);
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoList)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoList);
			}
		}
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			List<HouseSearchResultInfo> houseSearchResultInfoListAll = new ArrayList<>();
			// houseSearchDto.setPageSize(houseSearchDto.getPageSize() / 2);

			// 查询合租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			Page<RoomSolrResult> resultRoomPage = roomSolrRepository.searchAllByMultiCondition(houseSearchDto);

			// 查询整租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			Page<HouseSolrResult> resultHousePage = houseSolrRepository.searchAllByMultiCondition(houseSearchDto);
			if (resultRoomPage == null && resultHousePage == null) {
				logger.error(LogUtils.getCommLog("搜索房间结果为空"));
				return null;
			}

			List<RoomSolrResult> resulRoomtList = resultRoomPage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
					.getHouseSearchResultInfoListByRoom(resulRoomtList, collectIdList);

			List<HouseSolrResult> resulHousetList = resultHousePage.getContent();
			List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
					.getHouseSearchResultInfoListByHouse(resulHousetList, collectIdList);

			if (houseSearchResultInfoListHouse != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListHouse);
			}
			if (houseSearchResultInfoListRoom != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListRoom);
			}

			if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll);
			}
			/*
			 * List<HouseSearchResultInfo> houseSearchResultInfoListAll = new
			 * ArrayList<>();
			 * houseSearchDto.setPageSize(houseSearchDto.getPageSize());
			 * Page<HzfHousesSolrResult>
			 * resulHousetList=hzfHousesSolrRepository.searchAllByMultiCondition
			 * (houseSearchDto);
			 * 
			 * List<HouseSearchResultInfo> housesSearchResultInfoListHouse =
			 * HouseUtil
			 * .getHzfHousesSearchResultInfo(resulHousetList.getContent(),
			 * collectIdList);
			 * 
			 * if (housesSearchResultInfoListHouse != null) {
			 * houseSearchResultInfoListAll.addAll(
			 * housesSearchResultInfoListHouse); }
			 * 
			 * if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
			 * houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll
			 * ); }
			 */
		}

		return houseSearchResultDto;
	}

	/**
	 * 查询房源详情
	 * 
	 * @param sellId
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public HouseQueryDto getHouseQueryDto(String sellId, int roomId, long userId) {

		// 查询品牌公寓集合
		List<String> agencyIdList = new ArrayList<String>();
		List<Agency> agencyList = agencyManageRepository.findAllAgency();
		if (CollectionUtils.isNotEmpty(agencyList)) {
			for (Agency agency : agencyList) {
				if (!agencyIdList.contains(agency.getCompanyId())) {
					agencyIdList.add(agency.getCompanyId());
				}
			}
		}

		// 查询房源详情
		HouseQueryDto houseQueryDto = getHouseQueryDtoFromSolr(sellId, roomId, userId, agencyIdList);

		return houseQueryDto;
	}

	/**
	 * 从Solr中查询房源详情
	 * 
	 * @param sellId
	 * @param roomId
	 * @param userId
	 * @return
	 */
	private HouseQueryDto getHouseQueryDtoFromSolr(String sellId, int roomId, long userId, List<String> agencyIdList) {
		HouseQueryDto houseQueryDto = null;

		// 收藏标识
		int collectFlag = 0;
		// 用户登录-查询房源是否被收藏
		if (userId > 0) {
			HouseCollection houseCollection = houseCollectionRepository.findHouseCollectionItem(userId, sellId, roomId);
			if (houseCollection != null) {
				collectFlag = 1;
			}
		}

		if (roomId == 0) { // 整租查询
			HouseSolrResult houseSolrResult = houseSolrRepository.findBySellId(sellId);
			if (houseSolrResult != null) {
				int communityHouseCount = 0;
				int companyHouseCount = 0;
				int companyCityCount = 0;

				// 品牌公寓详情数量需求取消
				// if(agencyIdList.contains(houseSolrResult.getCompanyId())){
				// //小区数量
				// QueryDetailVo communityHouseCountVo =
				// houseDetailRepository.getCommunityHouseCountVo(houseSolrResult.getCommunityName());
				//
				// //品牌公寓房源数量
				// QueryDetailVo companyHouseCountVo =
				// houseDetailRepository.getCompanyHouseCountVo(houseSolrResult.getCompanyId());
				//
				// //品牌公寓城市数量
				// QueryDetailVo companyCityCountVo =
				// houseDetailRepository.getCompanyCityCountVo(houseSolrResult.getCompanyId());
				//
				// communityHouseCount = (int)
				// communityHouseCountVo.getCommunityHouseCount();
				// companyHouseCount = (int)
				// companyHouseCountVo.getCompanyHouseCount();
				// companyCityCount = (int)
				// companyCityCountVo.getCompanyCityCount();
				// }
				// 获取品牌公寓简称--拼接房源标题
				String companyName = "";
				// 查询品牌公寓-by公司ID&&城市ID
				// Agency agency =
				// agencyManageRepository.getAgencyByCompanyIdAndCityId(houseSolrResult.getCompanyId(),
				// houseSolrResult.getCityId());
				// if (agency != null) {
				// companyName = agency.getCompanyName();
				// }

				// 判读图片是否美化
				String imageCss = "";
				PlatformCustomer customer = platformCustomerRepository.findBySource(houseSolrResult.getSource());
				if (customer != null) {
					if (customer.getIsImg() == Constants.platform.IS_IMG_YES) {
						imageCss = customer.getImageCss();
					}
				}
				houseQueryDto = HouseUtil.getHouseQueryDto(houseSolrResult, communityHouseCount, companyHouseCount,
						companyCityCount, collectFlag, agencyIdList, companyName, imageCss);
			}
		} else { // 合租查询
			// 查询房间信息
			RoomSolrResult roomSolrResult = roomSolrRepository.findBySellIdAndId(sellId, roomId);
			if (roomSolrResult != null) {

				int communityHouseCount = 0;
				int companyHouseCount = 0;
				int companyCityCount = 0;

				// 品牌公寓详情数量需求取消
				// if(agencyIdList.contains(roomSolrResult.getCompanyId())){//判断是否品牌公寓
				// //小区数量
				// QueryDetailVo communityHouseCountVo =
				// houseDetailRepository.getCommunityHouseCountVo(roomSolrResult.getCommunityName());
				//
				// //品牌公寓数量
				// QueryDetailVo companyHouseCountVo =
				// houseDetailRepository.getCompanyHouseCountVo(roomSolrResult.getCompanyId());
				//
				// //品牌公寓城市数量
				// QueryDetailVo companyCityCountVo =
				// houseDetailRepository.getCompanyCityCountVo(roomSolrResult.getCompanyId());
				//
				// communityHouseCount = (int)
				// communityHouseCountVo.getCommunityHouseCount();
				// companyHouseCount = (int)
				// companyHouseCountVo.getCompanyHouseCount();
				// companyCityCount = (int)
				// companyCityCountVo.getCompanyCityCount();
				// }

				// 查询品牌公寓简称-拼接房源标题
				String companyName = "";
				// 查询品牌公寓-by公司ID&&城市ID
				// Agency agency =
				// agencyManageRepository.getAgencyByCompanyIdAndCityId(roomSolrResult.getCompanyId(),
				// roomSolrResult.getCityId());
				// if (agency != null) {
				// companyName = agency.getCompanyName();
				// }

				// 判读图片是否美化
				String imageCss = "";
				PlatformCustomer customer = platformCustomerRepository.findBySource(roomSolrResult.getSource());
				if (customer != null) {
					if (customer.getIsImg() == Constants.platform.IS_IMG_YES) {
						imageCss = customer.getImageCss();
					}
				}

				houseQueryDto = HouseUtil.getHouseQueryDto(roomSolrResult, communityHouseCount, companyHouseCount,
						companyCityCount, collectFlag, agencyIdList, companyName, imageCss);
			}
			if (houseQueryDto != null) {
				// 查询所有房间
				HouseSolrResult houseSolrResult = houseSolrRepository.findBySellId(sellId);
				List<RoomQueryDto> referHouse = HouseUtil.getReferHouse(houseSolrResult, null);
				houseQueryDto.setReferHouse(referHouse);
			}
		}

		return houseQueryDto;
	}

	/**
	 * 添加房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
	public String addHousePublishDto(HousePublishDto houseDto) {

		// 添加房源基础信息
		HouseBase houseBase = HouseUtil.getHouseBase(houseDto);
		HouseBase returnedHouseBase = houseBaseRepository.save(houseBase);

		// 添加房源详细信息
		HouseDetail houseDetail = HouseUtil.getHouseDetail(houseDto, returnedHouseBase);
		houseDetailRepository.save(houseDetail);

		// 添加房源设置
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(houseDto, returnedHouseBase);
		if (CollectionUtils.isNotEmpty(houseSettingList)) {
			houseSettingRepository.save(houseSettingList);
		}

		// 添加房源图片
		List<HousePicture> housePictureList = HouseUtil.getHousePictureList(houseDto, returnedHouseBase);
		if (CollectionUtils.isNotEmpty(housePictureList)) {
			if (houseDto.getEntireRent() == Constants.HouseDetail.RENT_TYPE_ENTIRE) {// 设置首图
				housePictureList.get(0).setIsDefault(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			}
			housePictureRepository.save(housePictureList);
		}

		String sellId = returnedHouseBase.getSellId();

		try {
		    addHouseOptHistoryRedis(sellId,0,Constants.HouseOptTypeUtil.HOUSE_OPT_ADD); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到消息队列失败" + e.getMessage()));
        }
		return sellId;
	}

	/**
	 * 添加爬虫房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
	@Transactional
	public String addCrawlHousePublishDto(HousePublishDto houseDto) {

		// 添加房源基础信息
		HouseBase houseBase = HouseUtil.getHouseBase(houseDto);
		HouseBase returnedHouseBase = houseBaseRepository.save(houseBase);

		// 添加房源详细信息
		HouseDetail houseDetail = HouseUtil.getHouseDetail(houseDto, returnedHouseBase);
		houseDetailRepository.save(houseDetail);

		// 添加房源设置
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(houseDto, returnedHouseBase);
		if (CollectionUtils.isNotEmpty(houseSettingList)) {
			houseSettingRepository.save(houseSettingList);
		}

		// 添加房源图片
		List<HousePicture> housePictureList = HouseUtil.getHousePictureList(houseDto, returnedHouseBase);
		if (CollectionUtils.isNotEmpty(housePictureList)) {
			housePictureRepository.save(housePictureList);
		}

		String sellId = returnedHouseBase.getSellId();

		return sellId;
	}

	/**
	 * 更新房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 * @throws Exception
	 */
	public String updateHousePublishDto(HousePublishDto houseDto, String sellId) throws Exception {

		// 查询房源基础信息
		// HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		HouseBase houseBase = houseBaseRepository.findCanUpdateBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		// 更新房源基础信息
		HouseBase publishHouseBase = HouseUtil.getHouseBase(houseDto, sellId);
		houseBase = HouseUtil.updateHouseBase(houseBase, publishHouseBase);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase更新失败, sellId:" + sellId));
			throw new Exception("houseBase更新失败, sellId:" + sellId);
		}
		HouseBase returnedHouseBase = houseBaseRepository.save(houseBase);

		// 查询房源详细信息
		// HouseDetail houseDetail = houseDetailRepository.findBySellId(sellId);
		HouseDetail houseDetail = houseDetailRepository.findCanUpdateBySellId(sellId);
		if (houseDetail == null) {
			logger.error(LogUtils.getCommLog("houseDetail查询失败, sellId:" + sellId));
			throw new Exception("houseDetail查询失败, sellId:" + sellId);
		}

		// 更新房源详细信息
		HouseDetail publishHouseDetail = HouseUtil.getHouseDetail(houseDto, returnedHouseBase);
		int status = houseDto.getStatus();// 获取房源状态
		houseDetail = HouseUtil.updateHouseDetail(houseDetail, publishHouseDetail, status);
		if (houseDetail == null) {
			logger.error(LogUtils.getCommLog("houseDetail更新失败, sellId:" + sellId));
			throw new Exception("houseDetail更新失败, sellId:" + sellId);
		}
		houseDetailRepository.save(houseDetail);

		// 更新房源设置
		updateHouseSettings(houseDto, sellId);

		// 更新房源图片
		updateHousePics(houseDto, sellId);

		// 更新房源之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		// if (houseDto.getEntireRent() ==
		// Constants.HouseDetail.RENT_TYPE_ENTIRE) {// 整租
		// // 删除房间
		// roomBaseRepository.setIsDeleteBySellId(sellId,
		// Constants.Common.STATE_IS_DELETE_YES);
		// }
		// if (houseDto.getEntireRent() ==
		// Constants.HouseDetail.RENT_TYPE_SHARE) {// 分租
		// // 回滚已删除房间
		// roomBaseRepository.setIsDeleteBySellId(sellId,
		// Constants.Common.STATE_IS_DELETE_NO);
		// }

		try {
            addHouseOptHistoryRedis(sellId,0,Constants.HouseOptTypeUtil.HOUSE_OPT_UPDATE); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到消息队列失败" + e.getMessage()));
        }
		
		return sellId;
	}

	/**
	 * 更新房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 * @throws Exception
	 */
	@Transactional
	public String updateCrawlHousePublishDto(HousePublishDto houseDto, String sellId) throws Exception {

		// 查询房源基础信息
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		// 更新房源基础信息
		HouseBase publishHouseBase = HouseUtil.getHouseBase(houseDto, sellId);
		houseBase = HouseUtil.updateHouseBase(houseBase, publishHouseBase);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase更新失败, sellId:" + sellId));
			throw new Exception("houseBase更新失败, sellId:" + sellId);
		}
		HouseBase returnedHouseBase = houseBaseRepository.save(houseBase);

		// 查询房源详细信息
		HouseDetail houseDetail = houseDetailRepository.findBySellId(sellId);
		if (houseDetail == null) {
			logger.error(LogUtils.getCommLog("houseDetail查询失败, sellId:" + sellId));
			throw new Exception("houseDetail查询失败, sellId:" + sellId);
		}

		// 更新房源详细信息
		HouseDetail publishHouseDetail = HouseUtil.getHouseDetail(houseDto, returnedHouseBase);
		int status = houseDto.getStatus();// 获取房源状态
		houseDetail = HouseUtil.updateHouseDetail(houseDetail, publishHouseDetail, status);
		if (houseDetail == null) {
			logger.error(LogUtils.getCommLog("houseDetail更新失败, sellId:" + sellId));
			throw new Exception("houseDetail更新失败, sellId:" + sellId);
		}
		houseDetailRepository.save(houseDetail);

		// 更新房源设置
		updateHouseSettings(houseDto, sellId);

		// 更新房源图片
		updateHousePics(houseDto, sellId);

		// 更新房源之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		return sellId;
	}

	/**
	 * 更新房源设置
	 */
	private void updateHouseSettings(HousePublishDto houseDto, String sellId) {

		// 旧的房源设置
		List<HouseSetting> oldHouseSettings = houseSettingRepository.findAllBySellIdAndRoomId(sellId, 0);

		// 新的房源设置
		List<HouseSetting> newHouseSettings = HouseUtil.getHouseSettingList(houseDto.getSettings(),
				houseDto.getSettingsAddon(), sellId, null);

		updateHouseSettings(oldHouseSettings, newHouseSettings);

	}

	/**
	 * 更新房间设置
	 */
	private void updateRoomSettings(RoomPublishDto roomDto, String sellId, long roomId) {

		// 旧的房间设置
		List<HouseSetting> oldHouseSettings = houseSettingRepository.findAllBySellIdAndRoomId(sellId, roomId);

		// 新的房间设置
		List<HouseSetting> newHouseSettings = HouseUtil.getHouseSettingList(roomDto.getSettings(),
				roomDto.getSettingsAddon(), sellId, roomId);

		updateHouseSettings(oldHouseSettings, newHouseSettings);

	}

	/**
	 * 更新房源设置
	 * 
	 * @param oldHouseSettings
	 * @param newHouseSettings
	 */
	private void updateHouseSettings(List<HouseSetting> oldHouseSettings, List<HouseSetting> newHouseSettings) {
		List<HouseSetting> addSettings = new ArrayList<HouseSetting>();// 需新增设置
		List<HouseSetting> updateSettings = new ArrayList<HouseSetting>(); // 需修改设置
		List<HouseSetting> deleteSettings = new ArrayList<HouseSetting>(); // 需删除设置

		HouseUtil.analyzeHouseSettings(oldHouseSettings, newHouseSettings, addSettings, updateSettings, deleteSettings);

		if (CollectionUtils.isNotEmpty(addSettings)) {
			houseSettingRepository.save(addSettings);
		}

		if (CollectionUtils.isNotEmpty(updateSettings)) {
			houseSettingRepository.save(updateSettings);
		}

		if (CollectionUtils.isNotEmpty(deleteSettings)) {
			List<Long> delSettingIds = new ArrayList<Long>();
			for (HouseSetting setting : deleteSettings) {
				long settingId = setting.getId();
				delSettingIds.add(settingId);
			}
			houseSettingRepository.setIsDeleteByIds(delSettingIds, Constants.Common.STATE_IS_DELETE_YES);
		}
	}

	/**
	 * 更新房源图片
	 */
	private void updateHousePics(HousePublishDto houseDto, String sellId) {

		// 旧的房源图片
		List<HousePicture> oldHousePics = housePictureRepository.findAllBySellIdAndRoomId(sellId, 0);

		// 新的房源图片
		List<HousePicture> newHousePics = HouseUtil.getHousePictureList(houseDto.getImgs(), sellId, null);

		boolean isShare = Constants.HouseDetail.RENT_TYPE_SHARE == houseDto.getEntireRent();// 是否为分租房源，注意是分租房源，不是房间
		updateHousePics(oldHousePics, newHousePics, houseDto.getStatus(), isShare);

	}

	/**
	 * 更新房间图片
	 */
	private void updateRoomPics(RoomPublishDto houseDto, String sellId, long roomId) {

		// 旧的房间图片
		List<HousePicture> oldHousePics = housePictureRepository.findAllBySellIdAndRoomId(sellId, roomId);

		// 新的房间图片
		List<HousePicture> newHousePics = HouseUtil.getHousePictureList(houseDto.getImgs(), sellId, roomId);

		updateHousePics(oldHousePics, newHousePics, houseDto.getStatus(), false);

	}

	/**
	 * 更新房源图片
	 * 
	 * @param oldHousePics
	 * @param newHousePics
	 */
	private void updateHousePics(List<HousePicture> oldHousePics, List<HousePicture> newHousePics, int houseStatus,
			boolean isShare) {
		List<HousePicture> addPics = new ArrayList<HousePicture>();// 需新增图片
		List<HousePicture> deletePics = new ArrayList<HousePicture>(); // 需删除图片

		HouseUtil.analyzeHousePics(oldHousePics, newHousePics, addPics, deletePics, isShare);

		if (CollectionUtils.isNotEmpty(addPics)) {
			housePictureRepository.save(addPics);
		} else if ((houseStatus != Constants.HouseBase.PUBLISH_STATUS_RENT) && CollectionUtils.isEmpty(addPics)) {// 如果房源没有更新图片，则把审核状态设为10，以便在审核定时任务处理
			try {
				if (CollectionUtils.isNotEmpty(oldHousePics)) {
					if (oldHousePics.get(0).getRoomId() == 0) { // 房源图片
						houseDetailRepository.setApproveStatusBySellId(oldHousePics.get(0).getSellId(),
								ApproveStatusEnum.IMG_APP_TEMP.getCode());
					} else { // roomId > 0 房间图片
						roomBaseRepository.setApproveStatusByRoomId(oldHousePics.get(0).getRoomId(),
								ApproveStatusEnum.IMG_APP_TEMP.getCode());
					}
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog("没有更新图片，审核状态设为临时状态失败"));
			}
		}

		if (CollectionUtils.isNotEmpty(deletePics)) {
			List<Long> delPicIds = new ArrayList<Long>();
			for (HousePicture pic : deletePics) {
				long picId = pic.getId();
				delPicIds.add(picId);
			}
			housePictureRepository.setIsDeleteByIds(delPicIds, Constants.Common.STATE_IS_DELETE_YES);
		}
	}

	/**
	 * 更新房源状态
	 * 
	 * @param houseDto
	 * @return sellId
	 * @throws Exception
	 */
	public void updateHouseStatus(String sellId, int status) throws Exception {

		// 检测房源是否存在
		boolean isExist = isHouseExist(sellId);
		if (!isExist) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		houseBaseRepository.setStatusBySellId(sellId, status);

	}

	/**
	 * 删除房源信息
	 * 
	 * @param sellId
	 * @return
	 */
	public void removeHouse(String sellId) {

		// 删除房间信息
		roomBaseRepository.setIsDeleteBySellId(sellId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房源图片信息
		housePictureRepository.setIsDeleteBySellId(sellId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房源设置信息
		houseSettingRepository.setIsDeleteBySellId(sellId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房源详情信息
		houseDetailRepository.setIsDeleteBySellId(sellId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房源基础信息
		houseBaseRepository.setIsDeleteBySellId(sellId, Constants.Common.STATE_IS_DELETE_YES);
		
		try {
            addHouseOptHistoryRedis(sellId,0,Constants.HouseOptTypeUtil.HOUSE_OPT_DELETE); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到闲鱼消息队列失败" + e.getMessage()));
        }
	
	}

	/**
	 * 下架爬虫房源信息
	 * 
	 * @param sellId
	 * @return
	 */
	@Transactional
	public void crawlDelateHouse(CrawlHouseDetail crawlHouseDetail) {

		// 下架房间信息
		roomBaseRepository.setStatusBySellId(crawlHouseDetail.getHouseSellId(), Constants.HouseBase.STATUS_RENT);

		// 下架房源基础信息
		houseBaseRepository.setStatusBySellId(crawlHouseDetail.getHouseSellId(), Constants.HouseBase.STATUS_RENT);

		crawlHouseDetail.setCollDelTime(new Date());
		crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);// 标识已经下架
		crawlHouseDetailRepository.save(crawlHouseDetail);
	}

	/**
	 * 添加房间信息
	 * 
	 * @param roomDto
	 * @return roomId
	 * @throws Exception
	 */
	public Long addRoomPublishDto(RoomPublishDto roomDto) throws Exception {

		// 检查sellId是否存在
		String sellId = roomDto.getSellId();
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		HouseDetail houseDetail = houseDetailRepository.findBySellId(sellId);
		if (houseDetail.getEntireRent() != Constants.HouseDetail.RENT_TYPE_SHARE) {
			logger.error(LogUtils.getCommLog("房源出租类型非合租，禁止发布房间, sellId:" + sellId));
			throw new Exception("房源出租类型非合租，禁止发布房间, sellId:" + sellId);
		}

		// 添加房间基础信息
		RoomBase roomBase = HouseUtil.getRoomBase(roomDto);
		RoomBase returnedRoomBase = roomBaseRepository.save(roomBase);

		// 添加房间设置
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(roomDto, returnedRoomBase);
		if (CollectionUtils.isNotEmpty(houseSettingList)) {
			houseSettingRepository.save(houseSettingList);
		}

		// 添加房间图片
		List<HousePicture> housePictureList = HouseUtil.getHousePictureList(roomDto, returnedRoomBase);
		if (CollectionUtils.isNotEmpty(housePictureList)) {
			housePictureList.get(0).setIsDefault(Constants.HouseDetail.RENT_TYPE_ENTIRE);// 设置首图
			housePictureRepository.save(housePictureList);
		}

		// 发完房间之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		long roomId = returnedRoomBase.getId();

		try {
            addHouseOptHistoryRedis(sellId,roomId,Constants.HouseOptTypeUtil.ROOM_OPT_ADD); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到消息队列失败" + e.getMessage()));
        }
		
		return roomId;
	}

	/**
	 * 添加爬虫房间信息
	 * 
	 * @param roomDto
	 * @return roomId
	 * @throws Exception
	 */
	@Transactional
	public Long addCrawloomPublishDto(RoomPublishDto roomDto) throws Exception {

		// 检查sellId是否存在
		String sellId = roomDto.getSellId();
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		// 添加房间基础信息
		RoomBase roomBase = HouseUtil.getRoomBase(roomDto);
		RoomBase returnedRoomBase = roomBaseRepository.save(roomBase);

		// 添加房间设置
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(roomDto, returnedRoomBase);
		if (CollectionUtils.isNotEmpty(houseSettingList)) {
			houseSettingRepository.save(houseSettingList);
		}

		// 添加房间图片
		List<HousePicture> housePictureList = HouseUtil.getHousePictureList(roomDto, returnedRoomBase);
		if (CollectionUtils.isNotEmpty(housePictureList)) {
			housePictureRepository.save(housePictureList);
		}

		// 发完房间之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		long roomId = returnedRoomBase.getId();

		return roomId;
	}

	/**
	 * 更新房间信息
	 * 
	 * @param roomDto
	 * @return roomId
	 * @throws Exception
	 */
	public Long updateRoomPublishDto(RoomPublishDto roomDto, long roomId) throws Exception {

		// 检查sellId是否存在
		String sellId = roomDto.getSellId();
		// HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		HouseBase houseBase = houseBaseRepository.findCanUpdateBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		// 查询房间基础信息
		// RoomBase roomBase = roomBaseRepository.findByRoomId(roomId);
		RoomBase roomBase = roomBaseRepository.findCanUpdateByRoomId(roomId);
		if (roomBase == null) {
			logger.error(LogUtils.getCommLog("roomBase查询失败, roomId:" + roomId));
			throw new Exception("roomBase查询失败, roomId:" + roomId);
		}

		// 更新房间基础信息
		RoomBase publishRoomBase = HouseUtil.getRoomBase(roomDto);
		roomBase = HouseUtil.updateRoomBase(roomBase, publishRoomBase);
		if (roomBase == null) {
			logger.error(LogUtils.getCommLog("roomBase更新失败, roomId:" + roomId));
			throw new Exception("roomBase更新失败, roomId:" + roomId);
		}
		roomBaseRepository.save(roomBase);

		// 更新房间设置
		updateRoomSettings(roomDto, sellId, roomId);

		// 更新房间图片
		updateRoomPics(roomDto, sellId, roomId);

		// 发完房间之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		try {
            addHouseOptHistoryRedis(sellId,roomId,Constants.HouseOptTypeUtil.ROOM_OPT_UPDATE); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到闲鱼消息队列失败" + e.getMessage()));
        }
		
		return roomId;
	}

	/**
	 * 更新爬虫房间信息
	 * 
	 * @param roomDto
	 * @return roomId
	 * @throws Exception
	 */
	@Transactional
	public Long updateCrawlRoomPublishDto(RoomPublishDto roomDto, long roomId) throws Exception {

		// 检查sellId是否存在
		String sellId = roomDto.getSellId();
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		if (houseBase == null) {
			logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
			throw new Exception("houseBase查询失败, sellId:" + sellId);
		}

		// 查询房间基础信息
		RoomBase roomBase = roomBaseRepository.findByRoomId(roomId);
		if (roomBase == null) {
			logger.error(LogUtils.getCommLog("roomBase查询失败, roomId:" + roomId));
			throw new Exception("roomBase查询失败, roomId:" + roomId);
		}

		// 更新房间基础信息
		RoomBase publishRoomBase = HouseUtil.getRoomBase(roomDto);
		roomBase = HouseUtil.updateRoomBase(roomBase, publishRoomBase);
		if (roomBase == null) {
			logger.error(LogUtils.getCommLog("roomBase更新失败, roomId:" + roomId));
			throw new Exception("roomBase更新失败, roomId:" + roomId);
		}
		roomBaseRepository.save(roomBase);

		// 更新房间设置
		updateRoomSettings(roomDto, sellId, roomId);

		// 更新房间图片
		updateRoomPics(roomDto, sellId, roomId);

		// 发完房间之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		return roomId;
	}

	/**
	 * 更新房间信息
	 * 
	 * @param roomDto
	 * @return roomId
	 * @throws Exception
	 */
	public void updateRoomStatus(long roomId, int status) throws Exception {

		// 检测房间是否存在
		boolean isExist = isRoomExist(roomId);
		if (!isExist) {
			logger.error(LogUtils.getCommLog("roomBase查询失败, roomId:" + roomId));
			throw new Exception("roomBase查询失败, roomId:" + roomId);
		}

		roomBaseRepository.setStatusByRoomId(roomId, status);
	}

	/**
	 * 删除房间信息
	 * 
	 * @param sellId
	 * @return
	 */
	public void removeRoom(String sellId,long roomId) {

		// 删除房间图片信息
		housePictureRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房间设置信息
		houseSettingRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房间信息
		roomBaseRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);
		
		try {
            addHouseOptHistoryRedis(sellId,roomId,Constants.HouseOptTypeUtil.ROOM_OPT_DELETE); 
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存历史数据到消息队列失败" + e.getMessage()));
        }
	}

	/**
	 * 查询房源是否存在
	 * 
	 * @param sellId
	 * @return
	 */
	public boolean isHouseExist(String sellId) {

		int count = houseBaseRepository.countBySellId(sellId);

		return count > 0;
	}

	/**
	 * 查询房间是否存在
	 * 
	 * @param roomId
	 * @return
	 */
	public boolean isRoomExist(long roomId) {

		int count = roomBaseRepository.countByRoomId(roomId);

		return count > 0;
	}

	/**
	 * 查询房间是否存在
	 * 
	 * @param sellId
	 * @param roomId
	 * @return
	 */
	public boolean isRoomExist(String sellId, long roomId) {

		int count = roomBaseRepository.countBySellIdAndRoomId(sellId, roomId);

		return count > 0;
	}

	/**
	 * 获取房源/房间token
	 * 
	 * @param cityId
	 */
	public String getHouseToken(String sellId, long roomId) {
		String token = houseTokenRepository.findTokenBySellIdAndRoomId(sellId, roomId);
		return token;
	}

	/**
	 * 保存房源/房间token
	 * 
	 * @param sellId
	 * @param roomId
	 */
	public void addHouseToken(String sellId, long roomId, String token) {

		HouseToken houseToken = new HouseToken();
		houseToken.setSellId(sellId);
		houseToken.setRoomId(roomId);
		houseToken.setToken(token);

		Date date = new Date();
		houseToken.setCreateTime(date);
		houseToken.setUpdateTime(date);

		houseToken.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);

		houseTokenRepository.save(houseToken);
	}

	/**
	 * 删除房源/房间token
	 * 
	 * @param sellId
	 * @param roomId
	 */
	public void removeHouseToken(String sellId, long roomId) {

		houseTokenRepository.setIsDeleteBySellIdAndRoomId(sellId, roomId, Constants.Common.STATE_IS_DELETE_YES);
	}

	/**
	 * 查询投诉列表
	 * 
	 * @param sellId
	 * @return
	 */
	public ComplaintQueryDto getComplaintQueryDto(String uid, String sellId, long roomId) {

		List<Complaint> complaintList = complaintRepository.findAllByUidAndSellIdAndRoomId(uid, sellId, roomId);

		ComplaintQueryDto complaintQueryDto = HouseUtil.getComplaintQueryDto(complaintList);

		return complaintQueryDto;
	}

	/**
	 * 查询是否已投诉
	 * 
	 * @param sellId
	 * @return
	 */
	public Integer getComplaintExistValue(String uid, String sellId, long roomId) {

		int complaintExist = Constants.Complaint.COMPLAINT_NOT_EXIST;

		int count = complaintRepository.getNumByUidAndSellIdAndRoomId(uid, sellId, roomId);
		if (count > 0) {
			complaintExist = Constants.Complaint.COMPLAINT_EXIST;
		}

		return complaintExist;
	}

	/**
	 * 添加投诉
	 * 
	 * @param sellId
	 * @return
	 */
	public Long addCompliant(ComplaintSaveDto complaintSaveDto) {
		// 添加投诉信息
		Complaint complaint = HouseUtil.getComplaint(complaintSaveDto);
		Complaint returnedComplaint = complaintRepository.save(complaint);

		long id = returnedComplaint.getId();
		return id;
	}

	/**
	 * 查询热门公寓列表
	 * 
	 * @param sellId
	 * @return
	 */
	public ApartmentQueryDto getApartmentQueryDto(long cityId) {

		List<Apartment> apartmentList = null;
		int size = queryConfiguration.getHotApartmentLimit();
		if (size == Constants.Common.QUERY_LIMIT_ALL) {
			apartmentList = apartmentRepository.findHotApartments(cityId);
		} else {
			PageRequest pageRequest = new PageRequest(0, size);
			apartmentList = apartmentRepository.findHotApartments(cityId, pageRequest);
		}

		ApartmentQueryDto apartmentQueryDto = HouseUtil.getApartmentQueryDto(apartmentList);

		return apartmentQueryDto;
	}

	/**
	 * 添加热门公寓
	 * 
	 * @param apartment
	 * @return
	 */
	public Long addApartment(Apartment apartment) {

		Apartment returnedApartment = apartmentRepository.save(apartment);

		long id = returnedApartment.getId();
		return id;
	}

	/**
	 * 删除热门公寓
	 * 
	 * @param apartment
	 * @return
	 */
	public void removeApartment(long id) {

		Apartment apartment = apartmentRepository.findOne(id);

		if (apartment == null) {
			logger.error(LogUtils.getCommLog("apartment查询失败, id:" + id));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_REMOVE_APARTMENT_FAIL, "公寓不存在");
		}

		long cityId = apartment.getCityId();

		apartmentRepository.setIsDeleteByIdAndCityId(id, cityId, Constants.Common.STATE_IS_DELETE_YES);
	}

	/**
	 * 清除城市所有热门公寓
	 * 
	 * @param apartment
	 * @return
	 */
	public void clearApartment(long cityId) {

		apartmentRepository.setIsDeleteByCityId(cityId, Constants.Common.STATE_IS_DELETE_YES);
	}

	/**
	 * 查询推荐房源
	 * 
	 * @param sellId
	 * @return
	 */
	public HouseRecommendQueryDto getHouseRecommendQueryDto(long cityId) {

		List<HouseRecommend> houseRecommendList = null;
		int size = queryConfiguration.getRecommendHouseLimit();
		if (size == Constants.Common.QUERY_LIMIT_ALL) {
			houseRecommendList = houseRecommendRepository.findHouseRecommends(cityId);
		} else {
			PageRequest pageRequest = new PageRequest(0, size);
			houseRecommendList = houseRecommendRepository.findHouseRecommends(cityId, pageRequest);
		}

		HouseRecommendQueryDto houseRecommendQueryDto = getHouseRecommendQueryDto(houseRecommendList);

		return houseRecommendQueryDto;
	}

	/**
	 * 添加推荐房源
	 * 
	 * @param apartment
	 * @return
	 */
	public Long addHouseRecommend(HouseRecommend houseRecommend) {

		HouseRecommend returnedHouseRecommend = houseRecommendRepository.save(houseRecommend);

		long id = returnedHouseRecommend.getId();
		return id;
	}

	/**
	 * 删除推荐房源
	 * 
	 * @param apartment
	 * @return
	 */
	public void removeHouseRecommend(long id) {

		HouseRecommend houseRecommend = houseRecommendRepository.findOne(id);

		if (houseRecommend == null) {
			logger.error(LogUtils.getCommLog("houseRecommend查询失败, id:" + id));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_REMOVE_RECOMMEND_HOUSE_FAIL, "推荐房源不存在");
		}

		long cityId = houseRecommend.getCityId();

		houseRecommendRepository.setIsDeleteByIdAndCityId(id, cityId, Constants.Common.STATE_IS_DELETE_YES);
	}

	/**
	 * 清除城市所有推荐房源
	 * 
	 * @param apartment
	 * @return
	 */
	public void clearHouseRecommend(long cityId) {

		houseRecommendRepository.setIsDeleteByCityId(cityId, Constants.Common.STATE_IS_DELETE_YES);
	}

	/**
	 * 获取推荐房源
	 * 
	 * @param houseRecommendList
	 * @return
	 */
	private HouseRecommendQueryDto getHouseRecommendQueryDto(List<HouseRecommend> houseRecommendList) {
		HouseRecommendQueryDto houseRecommendQueryDto = new HouseRecommendQueryDto();

		List<HouseRecommendInfo> houseRecommendInfoList = new ArrayList<HouseRecommendInfo>();
		if (CollectionUtils.isNotEmpty(houseRecommendList)) {
			for (HouseRecommend houseRecommend : houseRecommendList) {
				HouseRecommendInfo houseRecommendInfo = getHouseRecommendInfo(houseRecommend);
				if (houseRecommendInfo != null) {
					houseRecommendInfoList.add(houseRecommendInfo);
				}
			}
		}
		houseRecommendQueryDto.setHouseRecommends(houseRecommendInfoList);

		return houseRecommendQueryDto;
	}

	/**
	 * 获取单个推荐房源/房间
	 * 
	 * @param complaint
	 * @return
	 */
	private HouseRecommendInfo getHouseRecommendInfo(HouseRecommend houseRecommend) {
		return getHouseRecommendInfoFromSolr(houseRecommend);
	}

	/**
	 * 从Solr查询推荐房源/房间
	 * 
	 * @param houseRecommend
	 * @return
	 */
	private HouseRecommendInfo getHouseRecommendInfoFromSolr(HouseRecommend houseRecommend) {
		if (houseRecommend == null) {
			return null;
		}

		String sellId = houseRecommend.getSellId();
		if (StringUtil.isBlank(sellId)) {
			return null;
		}

		HouseSolrResult houseSolrResult = houseSolrRepository.findBySellId(sellId);
		if (houseSolrResult == null) {
			logger.error(LogUtils.getCommLog("houseSolrResult查询失败, sellId:" + sellId));
			return null;
		}

		HouseRecommendInfo houseRecommendInfo = new HouseRecommendInfo();

		houseRecommendInfo.setSellId(sellId);

		long roomId = houseRecommend.getRoomId();
		houseRecommendInfo.setRoomId(roomId);

		if (roomId == 0) { // 房源

			houseRecommendInfo.setPrice(houseSolrResult.getPrice());
			houseRecommendInfo.setArea(HouseUtil.getAreaStr((float) (houseSolrResult.getArea())));

			houseRecommendInfo.setHouseTag(houseSolrResult.getHouseTag());

			String houseTitle = HouseUtil.getHouseListTitle(houseSolrResult);
			houseRecommendInfo.setTitle(houseTitle);

			String dateStr = houseSolrResult.getPubDate();
			Date pubDate = SolrUtil.getSolrDate(dateStr);
			String pubDesc = HouseUtil.getPubDesc(pubDate);
			houseRecommendInfo.setPubDesc(pubDesc);

			// 2017-06-14 11:46:22 jjs
			houseRecommendInfo.setEntireRentDesc(houseSolrResult.getRentName());
			int ori = houseSolrResult.getOrientations();
			houseRecommendInfo.setOrientationName(HouseUtil.getOrientationsStr(ori));
		} else { // 房间

			RoomSolrResult roomSolrResult = roomSolrRepository.findBySellIdAndId(sellId, roomId);
			if (roomSolrResult == null) {
				logger.error(LogUtils.getCommLog("roomSolrResult查询失败, roomId:" + roomId));
				return null;
			}

			houseRecommendInfo.setPrice(roomSolrResult.getPrice());
			houseRecommendInfo.setArea(HouseUtil.getAreaStr((float) (roomSolrResult.getArea())));

			houseRecommendInfo.setHouseTag(roomSolrResult.getRoomTag());

			String houseTitle = HouseUtil.getHouseListTitle(roomSolrResult);
			houseRecommendInfo.setTitle(houseTitle);

			String dateStr = roomSolrResult.getPubDate();
			Date pubDate = SolrUtil.getSolrDate(dateStr);
			String pubDesc = HouseUtil.getPubDesc(pubDate);
			houseRecommendInfo.setPubDesc(pubDesc);
			// 2017-06-14 11:46:22 jjs
			houseRecommendInfo.setEntireRentDesc(roomSolrResult.getRentName());
			int ori = roomSolrResult.getOrientations();
			houseRecommendInfo.setOrientationName(HouseUtil.getOrientationsStr(ori));
		}

		houseRecommendInfo.setPic(houseRecommend.getPicRootPath());

		String flowNo = houseSolrResult.getFlowNo();
		String flowTotal = houseSolrResult.getFlowTotal();

		String floorStr = HouseUtil.getFloorStr(flowNo, flowTotal);
		houseRecommendInfo.setFloor(floorStr);

		String subway = houseSolrResult.getSubway();
		if (StringUtils.isNotEmpty(subway)) {// 如果3000米内有地铁，则显示地铁名
			String simplifiedSubway = HouseUtil.getSimplifiedSubway(subway);
			houseRecommendInfo.setSubway(simplifiedSubway);
		} else {
			if (StringUtils.isNotEmpty(houseSolrResult.getBizName())) {
				houseRecommendInfo.setSubway("位于  " + houseSolrResult.getBizName());// 无地铁显示商圈
			} else {
				houseRecommendInfo.setSubway("");
			}
		}

		// 推荐房源列表页显示小图 ，2017年06月23日19:07:25 jjs
		if (StringUtils.isNotEmpty(houseRecommendInfo.getPic())) {
			String pic = houseRecommendInfo.getPic() + "?x-oss-process=image/resize,h_120";
			houseRecommendInfo.setPic(pic);
		}
		return houseRecommendInfo;
	}

	public void isTopHouse(SaasHousePublishDto saasHousePublishDto) throws Exception {
		String sellId = saasHousePublishDto.getSellId();
		int roomId = saasHousePublishDto.getRoomId();
		int isTop = saasHousePublishDto.getIsTop();

		if (roomId > 0) {// 房间置顶设置
			// 查询置顶房源信息是否存在
			RoomBase roomBase;
			roomBase = roomBaseRepository.findBySellIdAndRoomId(sellId, roomId);
			if (roomBase == null) {
				logger.error(String.format("saas平台置顶房源失败, sellId:%s, appId:%s", saasHousePublishDto.getSellId(),
						saasHousePublishDto.getAppId()));
				throw new Exception("置顶不存在的房源/或者已删除, roomId:" + roomId);
			}

			roomBaseRepository.setIsTopByRoomIdAndSellId(sellId, roomId, isTop);
		} else {// 房源置顶设置

			// 查询置顶房源信息是否存在
			HouseDetail houseDetail;
			houseDetail = houseDetailRepository.findBySellId(saasHousePublishDto.getSellId());
			if (houseDetail == null) {
				logger.error(String.format("saas平台置顶房源失败, sellId:%s, appId:%s", saasHousePublishDto.getSellId(),
						saasHousePublishDto.getAppId()));
				throw new Exception("置顶不存在的房源/或者已删除, sellId:" + sellId);
			}
			houseDetailRepository.setIsTopBySellId(sellId, isTop);
		}
	}

	public void setPubType(SaasHousePublishDto saasHousePublishDto) throws Exception {
		String sellId = saasHousePublishDto.getSellId();
		int roomId = saasHousePublishDto.getRoomId();
		int pubType = saasHousePublishDto.getPubType();

		if (roomId > 0) {// 房间发布类型设置
			// 查询房源信息是否存在
			RoomBase roomBase;
			roomBase = roomBaseRepository.findBySellIdAndRoomId(sellId, roomId);
			if (roomBase == null) {
				logger.error(String.format("saas平台设置发布类型失败, sellId:%s, appId:%s", saasHousePublishDto.getSellId(),
						saasHousePublishDto.getAppId()));
				throw new Exception("设置不存在的房源/或者已删除, roomId:" + roomId);
			}

			roomBaseRepository.setPubTypeByRoomIdAndSellId(sellId, roomId, pubType);
		} else {// 房源发布类型设置
			// 查询房源信息是否存在
			HouseDetail houseDetail;
			houseDetail = houseDetailRepository.findBySellId(saasHousePublishDto.getSellId());
			if (houseDetail == null) {
				logger.error(String.format("saas平台设置发布类型失败, sellId:%s, appId:%s", saasHousePublishDto.getSellId(),
						saasHousePublishDto.getAppId()));
				throw new Exception("设置不存在的房源/或者已删除, sellId:" + sellId);
			}
			houseDetailRepository.setPubTypeBySellId(sellId, pubType);
		}
	}

	/**
	 * @Title: saveOrderCustom
	 * @Description: 保存用户订制数据
	 * @return long
	 * @author 叶东明
	 * @dateTime 2017年8月18日 上午11:07:30
	 */
	public long saveOrderCustom(OrderCustom orderCustom) throws Exception {
		orderCustom.setUpdateTime(new Date());
		orderCustom.setCreateTime(new Date());
		OrderCustom orderCustomEntity = orderCustomRepository.save(orderCustom);
		return orderCustomEntity.getId();
	}

	/**
	 * @Title: updateOrderCustom
	 * @Description: 更新用户订制数据
	 * @return int
	 * @author 叶东明
	 * @dateTime 2017年8月18日 下午2:41:30
	 */
	public int updateOrderCustom(long userId, String location, long minPrice, long maxPrice, String checkInTime,
			String phone) throws Exception {
		return orderCustomRepository.updateOrderCustom(userId, location, minPrice, maxPrice, checkInTime, phone);
	}

	/**
	 * @Title: getOrderCustomByUserId
	 * @Description: 通过userId查找订制详细信息
	 * @return OrderCustom
	 * @author 叶东明
	 * @dateTime 2017年8月18日 上午10:24:22
	 */
	public OrderCustom getOrderCustomByUserId(long userId) throws Exception {
		return orderCustomRepository.getOrderCustomByUserId(userId);
	}

	/**
	 * 获取下架房源数量
	 * 
	 * @return
	 */
	public int getCompanyOffCount(HouseSearchDto houseSearchDto) {

		// 查询合租数据
		houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
		Page<RoomSolrResult> resultRoomPage = roomSolrRepository.findCompanyOffCount(houseSearchDto);

		// 查询整租数据
		houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);

		Page<HouseSolrResult> resultHousePage = houseSolrRepository.findCompanyOffCount(houseSearchDto);

		if (resultRoomPage == null && resultHousePage == null) {
			logger.error(LogUtils.getCommLog("搜索下架公司结果为空"));
			return 0;
		}

		Long count = resultHousePage.getTotalElements() + resultRoomPage.getTotalElements();
		return Integer.valueOf(count.toString());
	}

	// 获取打散房源
	public HouseSearchResultDto getNewHouseResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList) {
		return null;
	}

	
	//获取打散房源
    public HouseSearchResultDto getNewAopHouseResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList) {
            
            HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();
            
            //获取房源打散 中介公司循环次数
            int forCount = Constants.HouseListConfig.FOREACH_COUNT;
            City city = cityRepository.findCityById(houseSearchDto.getCityId());
            if(city != null){
                forCount = city.getForCount();
            }
            
            int entireRent = houseSearchDto.getEntireRent();
            if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { //整租
                
                GroupResult<HouseSolrResult> houseFieldGroup  = houseSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                if (houseFieldGroup == null) {
                    logger.error(LogUtils.getCommLog("理想生活圈搜索房源结果为空"));
                    return null;
                }
                houseSearchResultDto = getHouseSearchResultDto(forCount,houseFieldGroup,Constants.HouseListConfig.RESULT_COUNT_HOUSELIST);
            }
            
            if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { //合租
                GroupResult<RoomSolrResult> roomFieldGroup = roomSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                if (roomFieldGroup == null) {
                    logger.error(LogUtils.getCommLog("理想生活圈搜索房源结果为空"));
                    return null;
                }
                houseSearchResultDto = getRoomSearchResultDto(forCount,roomFieldGroup,Constants.HouseListConfig.RESULT_COUNT_HOUSELIST);
            }
            
            if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { //全部
                
                //slor查询-share
                HouseSearchResultDto  roomResult = new HouseSearchResultDto();
                houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
                GroupResult<RoomSolrResult> roomFieldGroup = roomSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                roomResult = getRoomSearchResultDto(forCount,roomFieldGroup,Constants.HouseListConfig.RESULT_COUNT_HOUSELIST);
                
                //solr查询-entire 
                HouseSearchResultDto  houseResult = new HouseSearchResultDto();
                houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
                if(!StringUtil.isEmpty(houseSearchDto.getcBedRoomNums())){//租房宝典,白领优选筛选合租主卧和整租一居室的房源
                    houseSearchDto.setKeyword(null);
                }
                GroupResult<HouseSolrResult> houseFieldGroup  = houseSolrRepository.getNewAllByMultiCondition(houseSearchDto,agencyIdList);
                houseResult = getHouseSearchResultDto(forCount,houseFieldGroup,Constants.HouseListConfig.RESULT_COUNT_HOUSELIST);
            
                if (roomResult == null && houseResult == null) {
                    logger.error(LogUtils.getCommLog("房源列表搜索房源结果为空"));
                    return null;
                }
                
                //合并整租分租结果集
                List<HouseSearchResultInfo> infoAllList = new ArrayList<HouseSearchResultInfo>();
                if (roomResult != null) {
                    infoAllList.addAll(roomResult.getSearchHouses());
                }
                if (houseResult != null) {
                    infoAllList.addAll(houseResult.getSearchHouses());
                }
                
                if (CollectionUtils.isNotEmpty(infoAllList)) {
                    houseSearchResultDto.setSearchHouses(infoAllList);
                }
            }
            return houseSearchResultDto;        
        }
    
    
    public HouseSearchResultDto getHouseSearchResultDto(int forCount ,GroupResult<HouseSolrResult> houseFieldGroup,int resultCount){
        if(houseFieldGroup == null){//非空校验
            logger.error(LogUtils.getCommLog("整租房源列表搜索房源结果为空"));
            return null;
        }
        
        HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();
        //获取整租公司数据
        List<GroupEntry<HouseSolrResult>> houseGroupList = houseFieldGroup.getGroupEntries().getContent();
        List<HouseSolrResult> resulHousetList = new ArrayList<HouseSolrResult>();
        
        //遍历公司数据
        if(houseFieldGroup != null){
            boolean houseFalg = false;
            for(int index = 0; index <= resultCount; index++){
                if(houseFalg){
                    break;
                }
                int number = 0;
                //遍历solr结果
                for(GroupEntry<HouseSolrResult>  house : houseGroupList){
                    if(number >= forCount){
                        break;
                    }
                    HouseSolrResult hs = new HouseSolrResult();
                    
                    if(house.getResult().getContent().size()>index){//获取每个公司房源
                        hs = house.getResult().getContent().get(index);
                        if(hs != null){
                            //if(house.getResult().getContent().get(index).getImgs() != null && !house.getResult().getContent().get(index).getImgs().toString().equals("[]")){
                                resulHousetList.add(hs);
                            //}
                        }   
                        if(resulHousetList.size() >= resultCount ){
                            houseFalg = true;
                            break;
                        }
                    }
                    number++;
                }
            }
        }
        List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
                .getHouseSearchResultInfoListByHouse(resulHousetList);
        if (CollectionUtils.isNotEmpty(houseSearchResultInfoListHouse)) {
            houseSearchResultDto.setSearchHouses(houseSearchResultInfoListHouse);
        }
        
        return houseSearchResultDto;
    }
    
    public HouseSearchResultDto getRoomSearchResultDto(int forCount ,GroupResult<RoomSolrResult> roomFieldGroup,int resultCount){
        if(roomFieldGroup == null){//非空校验
            logger.error(LogUtils.getCommLog("合租房源列表搜索房源结果为空"));
            return null;
        }
        
        HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();

        //获取公司数据-share
        List<GroupEntry<RoomSolrResult>> roomGroupList = roomFieldGroup.getGroupEntries().getContent();
        List<RoomSolrResult> resulRoomtList = new ArrayList<RoomSolrResult>(resultCount);
        if(roomGroupList != null){
            boolean roomFalg = false;
            for(int index = 0; index < resultCount; index++){
                if(roomFalg){
                    break;
                }
                int number = 0;
                //遍历solr结果
                for(GroupEntry<RoomSolrResult> room : roomGroupList ){
                    if(number >= forCount){
                        break;
                    }
                    
                    RoomSolrResult rs = new RoomSolrResult();
                    if(room.getResult().getContent().size()>index){
                        rs = room.getResult().getContent().get(index);
                        if(rs != null){
                            //if(room.getResult().getContent().get(index).getImgs() != null && !room.getResult().getContent().get(index).getImgs().toString().equals("[]")){
                                resulRoomtList.add(rs);
                            //}
                        }   
                        
                        if(resulRoomtList.size() >= resultCount ){
                            roomFalg = true;
                            break;
                        }
                    }
                    number++;
                }
            }
        }
        List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
                .getHouseSearchResultInfoListByRoom(resulRoomtList);
        
        if (CollectionUtils.isNotEmpty(houseSearchResultInfoListRoom)) {
            houseSearchResultDto.setSearchHouses(houseSearchResultInfoListRoom);
        }   
        return houseSearchResultDto;
    }
    
    public  void addHouseOptHistoryRedis(String sellId,long roomId,int optType){     
        //封装房源历史对象
        HouseOptHistoryRedisDto hisOpt = new HouseOptHistoryRedisDto();
        hisOpt.setSellId(sellId);
        hisOpt.setRoomId(roomId);
        hisOpt.setOptType(optType);
        
        String key = RedisUtils.getInstance().getKey(HOUSE_OPT_HISTORY_KEY);//闲鱼
        try {
            redisCacheManager.leftPushList(key, GsonUtils.getInstace().toJson(hisOpt));
            logger.info(String.format("保存房源操作历史到闲鱼消息队列完成, sellId:%s, appId:%s", sellId,roomId));

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String hiskey = RedisUtils.getInstance().getKey(HOUSE_OPT_RECORD_KEY);//房源历史 
        try {
            redisCacheManager.leftPushList(hiskey, GsonUtils.getInstace().toJson(hisOpt));
            logger.info(String.format("保存房源操作历史到消息队列完成, sellId:%s, appId:%s", sellId,roomId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}	
	
