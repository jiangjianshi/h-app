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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.comm.Request;
import com.huifenqi.hzf_platform.configuration.QueryConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.ComplaintSaveDto;
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
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;
import com.huifenqi.hzf_platform.context.entity.house.HouseToken;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository;
import com.huifenqi.hzf_platform.dao.repository.house.ComplaintRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseCollectionRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseSettingRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseTokenRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.saas.SaasHouseSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.saas.SaasRoomSolrRepository;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.QueryDetailVo;
import com.huifenqi.usercomm.dao.AgencyConfRepository;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Repository
public class SaasHouseDao {

	private static final Log logger = LogFactory.getLog(SaasHouseDao.class);

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
	private SaasHouseSolrRepository saasHouseSolrRepository;

	@Autowired
	private SaasRoomSolrRepository saasRoomSolrRepository;

	@Autowired
	private QueryConfiguration queryConfiguration;

	@Autowired
	private CrawlHouseDetailRepository crawlHouseDetailRepository;

	@Autowired
	private HouseCollectionRepository houseCollectionRepository;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private AgencyManageRepository agencyManageRepository;

	/**
	 * 搜索房源
	 * 
	 * @return
	 */
	public HouseSearchResultDto getHouseSearchResultDto(HouseSearchDto houseSearchDto) {
		// long userId = sessionManager.getUserIdFromSession();
		long userId = 0;
		// 获取用户收藏房源标识
		List<String> collectIdList = new ArrayList<String>();
		List<HouseCollection> houseCollectionList = houseCollectionRepository.getHouseCollectionListByUserId(userId);
		if (CollectionUtils.isNotEmpty(houseCollectionList)) {
			for (HouseCollection houseCollection : houseCollectionList) {
				String sellId = houseCollection.getSellId();
				if (!collectIdList.contains(sellId)) {
					collectIdList.add(sellId);
				}
			}
		}
		int entireRent = houseSearchDto.getEntireRent();

		HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();

		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			Page<HouseSolrResult> resultPage = saasHouseSolrRepository.findAllByMultiCondition(houseSearchDto);

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
			Page<RoomSolrResult> resultPage = saasRoomSolrRepository.findAllByMultiCondition(houseSearchDto);

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
			Page<RoomSolrResult> resultRoomPage = saasRoomSolrRepository.findAllByMultiCondition(houseSearchDto);

			// 查询整租数据
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			Page<HouseSolrResult> resultHousePage = saasHouseSolrRepository.findAllByMultiCondition(houseSearchDto);
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
	 * 查询房源详情
	 * 
	 * @param sellId
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public HouseQueryDto getHouseQueryDto(String sellId, long roomId, long userId, List<String> agencyNameList) {

		HouseQueryDto houseQueryDto = getHouseQueryDtoFromSolr(sellId, roomId, userId, agencyNameList);

		return houseQueryDto;
	}

	/**
	 * 从Solr中查询房源详情
	 * 
	 * @param sellId
	 * @param roomId
	 * @return
	 */
	private HouseQueryDto getHouseQueryDtoFromSolr(String sellId, long roomId, long userId,
			List<String> agencyNameList) {

		HouseQueryDto houseQueryDto = null;
		// 查询该房源/房间 针对当前用户是否被收藏
		int collectFlag = 0;
		HouseCollection houseCollection = houseCollectionRepository.findHouseCollectionItem(userId, sellId,
				new Long(roomId).intValue());
		if (houseCollection != null) {
			collectFlag = 1;
		}
		if (roomId == 0) { // 从hfqHouse查询
			HouseSolrResult houseSolrResult = saasHouseSolrRepository.findBySellId(sellId);
			// 新增同小区房源总数、公寓下所有房源总数、公寓下所有城市总数，放到houseSolrResult中
			QueryDetailVo communityHouseCountVo = houseDetailRepository
					.getCommunityHouseCountVo(houseSolrResult.getCommunityName());
			QueryDetailVo companyHouseCountVo = houseDetailRepository
					.getCompanyHouseCountVo(houseSolrResult.getCompanyId());
			QueryDetailVo companyCityCountVo = houseDetailRepository
					.getCompanyCityCountVo(houseSolrResult.getCompanyId());
			int communityHouseCount = (int) communityHouseCountVo.getCommunityHouseCount();
			int companyHouseCount = (int) companyHouseCountVo.getCompanyHouseCount();
			int companyCityCount = (int) companyCityCountVo.getCompanyCityCount();
			// 获取品牌公寓简称，拼接到房源标题前面
			Agency agency = agencyManageRepository.getAgencyByCompanyIdAndCityId(houseSolrResult.getCompanyId(),
					houseSolrResult.getCityId());
			String companyName = "";
			if (agency != null) {
				companyName = agency.getCompanyName();
			}
			
			String imageCss = "";
			houseQueryDto = HouseUtil.getHouseQueryDto(houseSolrResult, communityHouseCount, companyHouseCount,
					companyCityCount, collectFlag, agencyNameList, companyName,imageCss);
		} else { // 从hfqRoom查询

			// 查询房间信息
			RoomSolrResult roomSolrResult = saasRoomSolrRepository.findBySellIdAndId(sellId, roomId);
			// 新增同小区房源总数、公寓下所有房源总数、公寓下所有城市总数，放到RoomSolrResult中
			QueryDetailVo communityHouseCountVo = houseDetailRepository
					.getCommunityHouseCountVo(roomSolrResult.getCommunityName());
			QueryDetailVo companyHouseCountVo = houseDetailRepository
					.getCompanyHouseCountVo(roomSolrResult.getCompanyId());
			QueryDetailVo companyCityCountVo = houseDetailRepository
					.getCompanyCityCountVo(roomSolrResult.getCompanyId());
			int communityHouseCount = (int) communityHouseCountVo.getCommunityHouseCount();
			int companyHouseCount = (int) companyHouseCountVo.getCompanyHouseCount();
			int companyCityCount = (int) companyCityCountVo.getCompanyCityCount();
			// 获取品牌公寓简称，拼接到房源标题前面
			Agency agency = agencyManageRepository.getAgencyByCompanyIdAndCityId(roomSolrResult.getCompanyId(),
					roomSolrResult.getCityId());
			String companyName = "";
			if (agency != null) {
				companyName = agency.getCompanyName();
			}
			
			String imageCss = "";
			houseQueryDto = HouseUtil.getHouseQueryDto(roomSolrResult, communityHouseCount, companyHouseCount,
					companyCityCount, collectFlag, agencyNameList, companyName,imageCss);

			if (houseQueryDto != null) {
				// 查询所有房间
				HouseSolrResult houseSolrResult = saasHouseSolrRepository.findBySellId(sellId);
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
			housePictureRepository.save(housePictureList);
		}

		String sellId = returnedHouseBase.getSellId();

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
		int status = houseDto.getStatus();
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
		int status = houseDto.getStatus();
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

		updateHousePics(oldHousePics, newHousePics);

	}

	/**
	 * 更新房间图片
	 */
	private void updateRoomPics(RoomPublishDto houseDto, String sellId, long roomId) {

		// 旧的房间图片
		List<HousePicture> oldHousePics = housePictureRepository.findAllBySellIdAndRoomId(sellId, roomId);

		// 新的房间图片
		List<HousePicture> newHousePics = HouseUtil.getHousePictureList(houseDto.getImgs(), sellId, roomId);

		updateHousePics(oldHousePics, newHousePics);

	}

	/**
	 * 更新房源图片
	 * 
	 * @param oldHousePics
	 * @param newHousePics
	 */
	private void updateHousePics(List<HousePicture> oldHousePics, List<HousePicture> newHousePics) {
		List<HousePicture> addPics = new ArrayList<HousePicture>();// 需新增图片
		List<HousePicture> deletePics = new ArrayList<HousePicture>(); // 需删除图片

		HouseUtil.analyzeHousePics(oldHousePics, newHousePics, addPics, deletePics,true);

		if (CollectionUtils.isNotEmpty(addPics)) {
			housePictureRepository.save(addPics);
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
			housePictureRepository.save(housePictureList);
		}

		// 发完房间之后，重置所在房源isRun
		houseDetailRepository.setIsRunBySellId(sellId, Constants.HouseBase.IS_RUN_NO);

		long roomId = returnedRoomBase.getId();

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
	public void removeRoom(long roomId) {

		// 删除房间图片信息
		housePictureRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房间设置信息
		houseSettingRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);

		// 删除房间信息
		roomBaseRepository.setIsDeleteByRoomId(roomId, Constants.Common.STATE_IS_DELETE_YES);

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
	/*
	 * public HouseRecommendQueryDto getHouseRecommendQueryDto(long cityId) {
	 * 
	 * List<HouseRecommend> houseRecommendList = null; int size =
	 * queryConfiguration.getRecommendHouseLimit(); if (size ==
	 * Constants.Common.QUERY_LIMIT_ALL) { houseRecommendList =
	 * houseRecommendRepository.findHouseRecommends(cityId); } else {
	 * PageRequest pageRequest = new PageRequest(0, size); houseRecommendList =
	 * houseRecommendRepository.findHouseRecommends(cityId, pageRequest); }
	 * 
	 * HouseRecommendQueryDto houseRecommendQueryDto =
	 * getHouseRecommendQueryDto(houseRecommendList);
	 * 
	 * return houseRecommendQueryDto; }
	 */

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
	/*
	 * private HouseRecommendQueryDto
	 * getHouseRecommendQueryDto(List<HouseRecommend> houseRecommendList) {
	 * HouseRecommendQueryDto houseRecommendQueryDto = new
	 * HouseRecommendQueryDto();
	 * 
	 * List<HouseRecommendInfo> houseRecommendInfoList = new
	 * ArrayList<HouseRecommendInfo>(); if
	 * (CollectionUtils.isNotEmpty(houseRecommendList)) { for (HouseRecommend
	 * houseRecommend : houseRecommendList) { HouseRecommendInfo
	 * houseRecommendInfo = getHouseRecommendInfo(houseRecommend); if
	 * (houseRecommendInfo != null) {
	 * houseRecommendInfoList.add(houseRecommendInfo); } } }
	 * houseRecommendQueryDto.setHouseRecommends(houseRecommendInfoList);
	 * 
	 * return houseRecommendQueryDto; }
	 */

	/**
	 * 获取单个推荐房源/房间
	 * 
	 * @param complaint
	 * @return
	 */
	/*
	 * private HouseRecommendInfo getHouseRecommendInfo(HouseRecommend
	 * houseRecommend) { return getHouseRecommendInfoFromSolr(houseRecommend); }
	 */

	/**
	 * 从Solr查询推荐房源/房间
	 * 
	 * @param houseRecommend
	 * @return
	 */
	/*
	 * private HouseRecommendInfo getHouseRecommendInfoFromSolr(HouseRecommend
	 * houseRecommend) { if (houseRecommend == null) { return null; }
	 * 
	 * String sellId = houseRecommend.getSellId(); if
	 * (StringUtil.isBlank(sellId)) { return null; }
	 * 
	 * HouseSolrResult houseSolrResult =
	 * saasHouseSolrRepository.findBySellId(sellId); if (houseSolrResult ==
	 * null) { logger.error(LogUtils.getCommLog("houseSolrResult查询失败, sellId:" +
	 * sellId)); return null; }
	 * 
	 * HouseRecommendInfo houseRecommendInfo = new HouseRecommendInfo();
	 * 
	 * houseRecommendInfo.setSellId(sellId);
	 * 
	 * long roomId = houseRecommend.getRoomId();
	 * houseRecommendInfo.setRoomId(roomId);
	 * 
	 * if (roomId == 0) { // 房源
	 * 
	 * houseRecommendInfo.setPrice(houseSolrResult.getPrice());
	 * houseRecommendInfo.setArea(HouseUtil.getAreaStr((float)
	 * (houseSolrResult.getArea())));
	 * 
	 * houseRecommendInfo.setHouseTag(houseSolrResult.getHouseTag());
	 * 
	 * String houseTitle = HouseUtil.getHouseListTitle(houseSolrResult);
	 * houseRecommendInfo.setTitle(houseTitle);
	 * 
	 * String dateStr = houseSolrResult.getPubDate(); Date pubDate =
	 * SolrUtil.getSolrDate(dateStr); String pubDesc =
	 * HouseUtil.getPubDesc(pubDate); houseRecommendInfo.setPubDesc(pubDesc);
	 * 
	 * // 2017-06-14 11:46:22 jjs
	 * houseRecommendInfo.setEntireRentDesc(houseSolrResult.getRentName()); int
	 * ori = houseSolrResult.getOrientations();
	 * houseRecommendInfo.setOrientationName(HouseUtil.getOrientationsStr(ori));
	 * } else { // 房间
	 * 
	 * RoomSolrResult roomSolrResult =
	 * saasRoomSolrRepository.findBySellIdAndId(sellId, roomId); if
	 * (roomSolrResult == null) {
	 * logger.error(LogUtils.getCommLog("roomSolrResult查询失败, roomId:" +
	 * roomId)); return null; }
	 * 
	 * houseRecommendInfo.setPrice(roomSolrResult.getPrice());
	 * houseRecommendInfo.setArea(HouseUtil.getAreaStr((float)
	 * (roomSolrResult.getArea())));
	 * 
	 * houseRecommendInfo.setHouseTag(roomSolrResult.getRoomTag());
	 * 
	 * String houseTitle = HouseUtil.getHouseListTitle(roomSolrResult);
	 * houseRecommendInfo.setTitle(houseTitle);
	 * 
	 * String dateStr = roomSolrResult.getPubDate(); Date pubDate =
	 * SolrUtil.getSolrDate(dateStr); String pubDesc =
	 * HouseUtil.getPubDesc(pubDate); houseRecommendInfo.setPubDesc(pubDesc); //
	 * 2017-06-14 11:46:22 jjs
	 * houseRecommendInfo.setEntireRentDesc(roomSolrResult.getRentName()); int
	 * ori = roomSolrResult.getOrientations();
	 * houseRecommendInfo.setOrientationName(HouseUtil.getOrientationsStr(ori));
	 * }
	 * 
	 * houseRecommendInfo.setPic(houseRecommend.getPicRootPath());
	 * 
	 * String flowNo = houseSolrResult.getFlowNo(); String flowTotal =
	 * houseSolrResult.getFlowTotal();
	 * 
	 * String floorStr = HouseUtil.getFloorStr(flowNo, flowTotal);
	 * houseRecommendInfo.setFloor(floorStr);
	 * 
	 * String subway = houseSolrResult.getSubway(); if
	 * (StringUtils.isNotEmpty(subway)) {// 如果3000米内有地铁，则显示地铁名 String
	 * simplifiedSubway = HouseUtil.getSimplifiedSubway(subway);
	 * houseRecommendInfo.setSubway(simplifiedSubway); } else { if
	 * (StringUtils.isNotEmpty(houseSolrResult.getBizName())) {
	 * houseRecommendInfo.setSubway("位于  " + houseSolrResult.getBizName());//
	 * 无地铁显示商圈 } else { houseRecommendInfo.setSubway(""); } }
	 * 
	 * // 推荐房源列表页显示小图 ，2017年06月23日19:07:25 jjs if
	 * (StringUtils.isNotEmpty(houseRecommendInfo.getPic())) { String pic =
	 * houseRecommendInfo.getPic() + "?x-oss-process=image/resize,h_120";
	 * houseRecommendInfo.setPic(pic); } return houseRecommendInfo; }
	 */

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
}
