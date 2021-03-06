/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseSolrRepositoryImpl.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.house.solr 
 * Date: 2016年5月12日下午12:19:49 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.geo.Point;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.huifenqi.hzf_platform.configuration.SearchConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.CompanyOffConfig;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: HouseSolrRepositoryImpl date: 2016年5月12日 下午12:19:49 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseSolrRepositoryImpl extends SimpleSolrRepository<HouseSolrResult, Long> {

	private static final Log logger = LogFactory.getLog(HouseSolrRepositoryImpl.class);

	@Autowired
	private SearchConfiguration searchConfiguration;
	
	@Autowired
	private CompanyOffConfigRepository companyOffConfigRepository;
	
	@Autowired
	private Configuration configuration;

	@Autowired
	public HouseSolrRepositoryImpl(@Qualifier("hfqHouseTemplate") SolrOperations solrOperations) {
		super(solrOperations);
	}

	//查询下架公司房源
	public Page<HouseSolrResult> findCompanyOffCount(HouseSearchDto houseSearchDto) {

		Query query = new SimpleQuery();
		// cityId
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		String companyId = houseSearchDto.getCompanyId();
		if (StringUtil.isNotEmpty(companyId)) {
 			SolrUtil.addCompanyIdCriteria(query, companyId, "companyId");
		}
	
		// 整租/分租
		int entireRent = houseSearchDto.getEntireRent();
		Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
		entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
		query.addCriteria(entireRentCriteria);

		// status
		SolrUtil.addHouseStatusCriteria(query, "status");
		//approveStatus 审核状态
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		ScoredPage<HouseSolrResult> queryForPage = getSolrOperations().queryForPage(query, HouseSolrResult.class);
		return queryForPage;
	}
	
	public Page<HouseSolrResult> findAllByMultiCondition(HouseSearchDto houseSearchDto) {

		Query query = new SimpleQuery();

		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
		if (pageRequest == null) {
			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
		} else {
			query.setPageRequest(pageRequest);
		}
		
		//不查询被限制渠道的房源
		if (CollectionUtils.isNotEmpty(houseSearchDto.getDenyList())) {
			SolrUtil.addNotStatusCriteria(query, "fsource", houseSearchDto.getDenyList());
		}
		
		//过滤下架公司的房源
//		if (CollectionUtils.isNotEmpty(houseSearchDto.getCompanyOffList())) {
//			SolrUtil.addNotStatusCriteria(query, "companyId", houseSearchDto.getCompanyOffList());
//		}
		
		//按城市+公司过滤
		List<CompanyOffConfig> companyOffList =  companyOffConfigRepository.findCompanyOffConfigByCityId(houseSearchDto.getCityId());
		if(!companyOffList.isEmpty()){
			List<String>  offList = new ArrayList<String>();
			for(CompanyOffConfig config : companyOffList){
				offList.add(config.getCompanyId());
			}
			SolrUtil.addNotStatusCriteria(query, "companyId", offList);		
		}

		String sellIds = houseSearchDto.getSellId();
		if (StringUtil.isNotEmpty(sellIds)) {
			SolrUtil.addSellIdCriteria(query, sellIds, 1);
		}

		// cityId
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		String companyId = houseSearchDto.getCompanyId();
		if (StringUtil.isNotEmpty(companyId)) {
 			SolrUtil.addCompanyIdCriteria(query, companyId, "companyId");
		}

		// // 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if (keyword.contains("支持月付") || keyword.contains("分期") || keyword.contains("月付")) {
				Criteria criteria = new Criteria("isPayMonth").is(1);
				query.addCriteria(criteria);
			} else {
				keyword = ClientUtils.escapeQueryChars(keyword);
				String containExpression = "*" + keyword + "*";
				// 小区名
				Criteria criteria = new Criteria("communityName").expression(containExpression);
				// 地址
				criteria = criteria.or(new Criteria("address").expression(containExpression));
				// 商圈名
				criteria = criteria.or(new Criteria("bizname").expression(containExpression));
				// 行政区
//				criteria = criteria.or(new Criteria("district").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
//				criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 查询关键字
				criteria = criteria.or(new Criteria("text").expression(keyword));
				query.addCriteria(criteria);
			}
		}

		// 商圈Id
		long bizId = houseSearchDto.getBizId();
		if (bizId != 0) {
			Criteria criteria = new Criteria("bizId").is(bizId);
			query.addCriteria(criteria);
		}

		// 行政区Id
		long disId = houseSearchDto.getDistrictId();
		if (disId != 0) {
			Criteria criteria = new Criteria("districtId").is(disId);
			query.addCriteria(criteria);
		}

		// 地铁线路Id
		long lineId = houseSearchDto.getLineId();
		if (lineId != 0) {
			String lineIdTag = String.valueOf(lineId);
			SolrUtil.addTagCriteria(query, lineIdTag, "subwayLineId");
		}

		// 地铁站Id
		long stationId = houseSearchDto.getStationId();
		if (stationId != 0) {
			String stationIdTag = String.valueOf(stationId);
			SolrUtil.addTagCriteria(query, stationIdTag, "subwayStationId");
		}

		// 价格Id
		String price = houseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rentPriceMonth", price);
		}

		// 朝向
		int orientation = houseSearchDto.getOrientation();
		if (orientation != 0) {
			Criteria criteria = new Criteria("orientations").is(orientation);
			query.addCriteria(criteria);
		}

		// 面积
		String area = houseSearchDto.getArea();
		if (StringUtil.isNotEmpty(area)) {
			SolrUtil.addRegionCriteria(query, "fArea", area);
		}

		// 整租/分租
		int entireRent = houseSearchDto.getEntireRent();
		Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
		entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
		query.addCriteria(entireRentCriteria);

		// 距离
		String distanceKey = "google";
		String location = houseSearchDto.getLocation();
		if (StringUtil.isNotEmpty(location)) { // 当前位置
			int nearybyDistance = searchConfiguration.getNearybyDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		} else if (stationId != 0) { // 地铁位置
			String stationPosition = houseSearchDto.getStationLocation();
			int stationDistance = searchConfiguration.getStationDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, stationPosition, stationDistance);
		}

		// 居室数量
		String bedroomNum = houseSearchDto.getBedroomNum();
		if (StringUtil.isNotEmpty(bedroomNum)) {
			SolrUtil.addRegionCriteria(query, "bedroomNums", bedroomNum);
		}

		// houseTag
		String houseTag = houseSearchDto.getHouseTag();
		if (StringUtil.isNotEmpty(houseTag)) {
			SolrUtil.addTagCriteria(query, houseTag, "housedTag");
		}
		
		// 付款方式 (默认-1查询全部)
		String payType = houseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addPayTypeCriteria(query, payType, "depositMonth", "periodMonth");
		}
		
		// 搜索附近房源时，排除当前房源
		String sellerId = houseSearchDto.getSellerId();
		if (StringUtil.isNotEmpty(sellerId)) {
			Criteria houseCriteria = new Criteria("hsId").is(sellerId).not();
			query.addCriteria(houseCriteria);
		}

		// status
		SolrUtil.addHouseStatusCriteria(query, "status");
		//approveStatus 审核状态
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		ScoredPage<HouseSolrResult> queryForPage = getSolrOperations().queryForPage(query, HouseSolrResult.class);
		return queryForPage;
	}
	
	
	public GroupResult<HouseSolrResult> findNewAllByMultiCondition(HouseSearchDto houseSearchDto) {

		Query query = new SimpleQuery();

		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
		if (pageRequest == null) {
			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
		} else {
			query.setPageRequest(pageRequest);
		}
		
		//不查询被限制渠道的房源
		if (CollectionUtils.isNotEmpty(houseSearchDto.getDenyList())) {
			SolrUtil.addNotStatusCriteria(query, "fsource", houseSearchDto.getDenyList());
		}
		
		//过滤下架公司的房源
//		if (CollectionUtils.isNotEmpty(houseSearchDto.getCompanyOffList())) {
//			SolrUtil.addNotStatusCriteria(query, "companyId", houseSearchDto.getCompanyOffList());
//		}
		
		//按城市+公司过滤
		List<CompanyOffConfig> companyOffList =  companyOffConfigRepository.findCompanyOffConfigByCityId(houseSearchDto.getCityId());
		if(!companyOffList.isEmpty()){
			List<String>  offList = new ArrayList<String>();
			for(CompanyOffConfig config : companyOffList){
				offList.add(config.getCompanyId());
			}
			SolrUtil.addNotStatusCriteria(query, "companyId", offList);		
		}

		String sellIds = houseSearchDto.getSellId();
		if (StringUtil.isNotEmpty(sellIds)) {
			SolrUtil.addSellIdCriteria(query, sellIds, 1);
		}

		// cityId
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		String companyId = houseSearchDto.getCompanyId();
		if (StringUtil.isNotEmpty(companyId)) {
 			SolrUtil.addCompanyIdCriteria(query, companyId, "companyId");
		}

		// // 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if (keyword.contains("支持月付") || keyword.contains("分期") || keyword.contains("月付")) {
				Criteria criteria = new Criteria("isPayMonth").is(1);
				query.addCriteria(criteria);
			} else {
				keyword = ClientUtils.escapeQueryChars(keyword);
				String containExpression = "*" + keyword + "*";
				// 小区名
				Criteria criteria = new Criteria("communityName").expression(containExpression);
				// 地址
				criteria = criteria.or(new Criteria("address").expression(containExpression));
				// 商圈名
				criteria = criteria.or(new Criteria("bizname").expression(containExpression));
				// 行政区
//				criteria = criteria.or(new Criteria("district").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
//				criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 查询关键字
				criteria = criteria.or(new Criteria("text").expression(keyword));
				query.addCriteria(criteria);
			}
		}

		// 商圈Id
		long bizId = houseSearchDto.getBizId();
		if (bizId != 0) {
			Criteria criteria = new Criteria("bizId").is(bizId);
			query.addCriteria(criteria);
		}

		// 行政区Id
		long disId = houseSearchDto.getDistrictId();
		if (disId != 0) {
			Criteria criteria = new Criteria("districtId").is(disId);
			query.addCriteria(criteria);
		}

		// 地铁线路Id
		long lineId = houseSearchDto.getLineId();
		if (lineId != 0) {
			String lineIdTag = String.valueOf(lineId);
			SolrUtil.addTagCriteria(query, lineIdTag, "subwayLineId");
		}

		// 地铁站Id
		long stationId = houseSearchDto.getStationId();
		if (stationId != 0) {
			String stationIdTag = String.valueOf(stationId);
			SolrUtil.addTagCriteria(query, stationIdTag, "subwayStationId");
		}

		// 价格Id
		String price = houseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rentPriceMonth", price);
		}

		// 朝向
		int orientation = houseSearchDto.getOrientation();
		if (orientation != 0) {
			Criteria criteria = new Criteria("orientations").is(orientation);
			query.addCriteria(criteria);
		}

		// 面积
		String area = houseSearchDto.getArea();
		if (StringUtil.isNotEmpty(area)) {
			SolrUtil.addRegionCriteria(query, "fArea", area);
		}

		// 整租/分租
		int entireRent = houseSearchDto.getEntireRent();
		Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
		entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
		query.addCriteria(entireRentCriteria);

		// 距离
		String distanceKey = "google";
		String location = houseSearchDto.getLocation();
		if (StringUtil.isNotEmpty(location)) { // 当前位置
			int nearybyDistance = searchConfiguration.getNearybyDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		} else if (stationId != 0) { // 地铁位置
			String stationPosition = houseSearchDto.getStationLocation();
			int stationDistance = searchConfiguration.getStationDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, stationPosition, stationDistance);
		}

		// 居室数量
		String bedroomNum = houseSearchDto.getBedroomNum();
		if (StringUtil.isNotEmpty(bedroomNum)) {
			SolrUtil.addRegionCriteria(query, "bedroomNums", bedroomNum);
		}

		// houseTag
		String houseTag = houseSearchDto.getHouseTag();
		if (StringUtil.isNotEmpty(houseTag)) {
			SolrUtil.addTagCriteria(query, houseTag, "housedTag");
		}
		
		// 付款方式 (默认-1查询全部)
		String payType = houseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addPayTypeCriteria(query, payType, "depositMonth", "periodMonth");
		}
		
		// 搜索附近房源时，排除当前房源
		String sellerId = houseSearchDto.getSellerId();
		if (StringUtil.isNotEmpty(sellerId)) {
			Criteria houseCriteria = new Criteria("hsId").is(sellerId).not();
			query.addCriteria(houseCriteria);
		}

		// status
		SolrUtil.addHouseStatusCriteria(query, "status");
		//approveStatus 审核状态
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		
		GroupOptions groupOptions = new GroupOptions();
		//封装分组请求参数 
		groupOptions.addGroupByField(Constants.IdealrRentConfig.COMPANY_NAME);
		groupOptions.setTotalCount(true);
		groupOptions.setLimit(Constants.HouseListConfig.RESULT_COUNT_SEARCH);
		query.setGroupOptions(groupOptions);
		query.setRows(2000);
		
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		GroupPage<HouseSolrResult> page = getSolrOperations().queryForGroupPage(query,  HouseSolrResult.class);	
		
		//获取分组数据
		GroupResult<HouseSolrResult> fieldGroup = page.getGroupResult(Constants.IdealrRentConfig.COMPANY_NAME);
		

		return fieldGroup;
	}
	/**
	 * 获取中介打散房源
	 * 
	 * @param houseSearchDto
	 * @return Page<HouseSolrResult>
	 */
	public GroupResult<HouseSolrResult> getNewAllByMultiCondition(HouseSearchDto houseSearchDto, List<String> agencyIdList) {
		Query query = new SimpleQuery();
		
		// 不查询被限制渠道的房源
		if (CollectionUtils.isNotEmpty(houseSearchDto.getDenyList())) {
			SolrUtil.addNotStatusCriteria(query, "fsource", houseSearchDto.getDenyList());
		}

//		//过滤下架公司的房源
//		if (CollectionUtils.isNotEmpty(houseSearchDto.getCompanyOffList())) {
//			SolrUtil.addNotStatusCriteria(query, "companyId", houseSearchDto.getCompanyOffList());
//		}
		
		//按城市+公司过滤
		List<CompanyOffConfig> companyOffList =  companyOffConfigRepository.findCompanyOffConfigByCityId(houseSearchDto.getCityId());
		if(!companyOffList.isEmpty()){
			List<String>  offList = new ArrayList<String>();
			for(CompanyOffConfig config : companyOffList){
				offList.add(config.getCompanyId());
			}
			SolrUtil.addNotStatusCriteria(query, "companyId", offList);		
		}
				
		// 城市
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}
		
		// 房型
		String eBedRoomNums = houseSearchDto.geteBedRoomNums();// 自在整租
		String sBedRoomNums = houseSearchDto.getsBedRoomNums();// 优选合租
		SolrUtil.addHouseStyleCriteria(query, eBedRoomNums, sBedRoomNums, "bedroomNums", "entireRent");
		
		// 租房宝典,白领优选筛选合租主卧和整租一居室的房源
		String cBedRoomNums = houseSearchDto.getcBedRoomNums();
		if (!StringUtil.isEmpty(cBedRoomNums)) {
			Criteria cBedRoomNumsCriteria = new Criteria("bedroomNums").is(Integer.parseInt(cBedRoomNums));
			query.addCriteria(cBedRoomNumsCriteria);
		}
				
		// 整租/分租
		if (StringUtil.isEmpty(eBedRoomNums) && houseSearchDto.getEntireRent() > -1) {
			int entireRent = houseSearchDto.getEntireRent();
			Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
			entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
			query.addCriteria(entireRentCriteria);
		}
		
		// 品牌公寓筛选
		String companyId = houseSearchDto.getCompanyId();
		SolrUtil.addCompanyCriteria(query, companyId, "companyId", agencyIdList);
		
		// 商圈Id
		long bizId = houseSearchDto.getBizId();
		if (bizId != 0) {
			Criteria criteria = new Criteria("bizId").is(bizId);
			query.addCriteria(criteria);
		}
		
		// 行政区Id
		long disId = houseSearchDto.getDistrictId();
		if (disId != 0) {
			Criteria criteria = new Criteria("districtId").is(disId);
			query.addCriteria(criteria);
		}
		
		// 地铁线路Id
		long lineId = houseSearchDto.getLineId();
		if (lineId != 0) {
			String lineIdTag = String.valueOf(lineId);
			SolrUtil.addTagCriteria(query, lineIdTag, "subwayLineId");
		}
		
		// 地铁站Id
		long stationId = houseSearchDto.getStationId();
		if (stationId != 0) {
			String stationIdTag = String.valueOf(stationId);
			SolrUtil.addTagCriteria(query, stationIdTag, "subwayStationId");
		}
		
		// 附近距离
		String distanceKey = "google";
		String location = houseSearchDto.getLocation();
		if (StringUtil.isNotEmpty(location)) { // 当前位置
			int nearybyDistance = searchConfiguration.getNearybyDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		} else if (stationId != 0) { // 地铁位置
			String stationPosition = houseSearchDto.getStationLocation();
			int stationDistance = searchConfiguration.getStationDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, stationPosition, stationDistance);
		}
		
		//按离地铁的距离搜索
		String distance = houseSearchDto.getDistance();
		if (StringUtil.isNotEmpty(distance)) {
			SolrUtil.addRegionCriteria(query, "subwayDistance", distance);
		}
		
		// 价格Id
		String price = houseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rentPriceMonth", price);
		}
		
		// 朝向
		String orientations = houseSearchDto.getOrientationStr();
		if (StringUtil.isNotEmpty(orientations)) {
			SolrUtil.addOrientationCriteria(query, orientations, "orientations");
		}

		// houseTag
		String houseTag = houseSearchDto.getHouseTag();
		if (StringUtil.isNotEmpty(houseTag)) {
			SolrUtil.addTagCriteria(query, houseTag, "housedTag");
		}
		
		// 付款方式 (默认-1查询全部)
		String payType = houseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addPayTypeCriteria(query, payType, "depositMonth", "periodMonth");
		}

		// 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if (keyword.contains("支持月付") || keyword.contains("分期") || keyword.contains("月付")) {
				Criteria criteria = new Criteria("isPayMonth").is(1);
				query.addCriteria(criteria);
			} else {
				keyword = ClientUtils.escapeQueryChars(keyword);
				String containExpression = "*" + keyword + "*";
				// 小区名
				Criteria criteria = new Criteria("communityName").expression(containExpression);
				// 地址
				criteria = criteria.or(new Criteria("address").expression(containExpression));
				// 商圈名
				criteria = criteria.or(new Criteria("bizname").expression(containExpression));
				// 行政区
//				criteria = criteria.or(new Criteria("district").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
//				criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 查询关键字
				criteria = criteria.or(new Criteria("text").expression(keyword));
				query.addCriteria(criteria);
			}
		}
		
		// 小区名称
		String communityName = houseSearchDto.getCommunityName();
		if (StringUtil.isNotEmpty(communityName)) {
			communityName = ClientUtils.escapeQueryChars(communityName);
			String containExpression = "*" + communityName + "*";
			Criteria criteria = new Criteria("communityName").expression(containExpression);
			query.addCriteria(criteria);
		}
		
		// 是否查询品牌公寓房源（1：品牌公寓房源；2：非品牌公寓房源）
		String companyType = houseSearchDto.getCompanyType();
		if (StringUtil.isNotBlank(companyType)) {
			SolrUtil.addCompanyQueryCriteria(query, companyType, "companyId", agencyIdList);
		}
		
		// 1 新房源，6 部分出租
		SolrUtil.addHouseStatusCriteria(query, "status");
		// 1 程序审核通过；3 图片审核通过
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		
		GroupOptions groupOptions = new GroupOptions();
		//封装分组请求参数 
		groupOptions.addGroupByField(Constants.IdealrRentConfig.COMPANY_NAME);
		groupOptions.setTotalCount(true);
		groupOptions.setLimit(Constants.HouseListConfig.RESULT_COUNT_HOUSELIST);
		query.setGroupOptions(groupOptions);
		query.setRows(2000);
		
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		GroupPage<HouseSolrResult> page = getSolrOperations().queryForGroupPage(query,  HouseSolrResult.class);	
		
		//获取分组数据
		GroupResult<HouseSolrResult> fieldGroup = page.getGroupResult(Constants.IdealrRentConfig.COMPANY_NAME);
		

		return fieldGroup;
	}
	
	public Page<HouseSolrResult> searchAllByMultiCondition(HouseSearchDto houseSearchDto) {

		Query query = new SimpleQuery();

		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
		if (pageRequest == null) {
			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
		} else {
			query.setPageRequest(pageRequest);
		}

		String sellIds = houseSearchDto.getSellId();
		if (StringUtil.isNotEmpty(sellIds)) {
			SolrUtil.addSellIdCriteria(query, sellIds, 1);
		}

		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		ScoredPage<HouseSolrResult> queryForPage = getSolrOperations().queryForPage(query, HouseSolrResult.class);
		return queryForPage;
	}

	
	public Page<HouseSolrResult> getAllByMultiCondition(HouseSearchDto houseSearchDto, List<String> agencyIdList) {
		Query query = new SimpleQuery();
		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
		if (pageRequest == null) {
			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
		} else {
			query.setPageRequest(pageRequest);
		}
		
		Criteria criteriaTemp = new Criteria();
		query.addCriteria(criteriaTemp);
		
		
		// 不查询被限制渠道的房源
		if (CollectionUtils.isNotEmpty(houseSearchDto.getDenyList())) {
			SolrUtil.addNotStatusCriteria(query, "fsource", houseSearchDto.getDenyList());
		}

		
		//按城市+公司过滤
		List<CompanyOffConfig> companyOffList =  companyOffConfigRepository.findCompanyOffConfigByCityId(houseSearchDto.getCityId());
		if(!companyOffList.isEmpty()){
			List<String>  offList = new ArrayList<String>();
			for(CompanyOffConfig config : companyOffList){
				offList.add(config.getCompanyId());
			}
			SolrUtil.addNotStatusCriteria(query, "companyId", offList);		
		}
				
		// 城市
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}
		
		// 房型
		String eBedRoomNums = houseSearchDto.geteBedRoomNums();// 自在整租
		String sBedRoomNums = houseSearchDto.getsBedRoomNums();// 优选合租
		SolrUtil.addHouseStyleCriteria(query, eBedRoomNums, sBedRoomNums, "bedroomNums", "entireRent");
		
		// 租房宝典,白领优选筛选合租主卧和整租一居室的房源
		String cBedRoomNums = houseSearchDto.getcBedRoomNums();
		if (!StringUtil.isEmpty(cBedRoomNums)) {
			Criteria cBedRoomNumsCriteria = new Criteria("bedroomNums").is(Integer.parseInt(cBedRoomNums));
			query.addCriteria(cBedRoomNumsCriteria);
		}
				
		// 整租/分租
		if (StringUtil.isEmpty(eBedRoomNums) && houseSearchDto.getEntireRent() > -1) {
			int entireRent = houseSearchDto.getEntireRent();
			Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
			entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
			query.addCriteria(entireRentCriteria);
		}
		
		// 品牌公寓筛选
		String companyId = houseSearchDto.getCompanyId();
		SolrUtil.addCompanyCriteria(query, companyId, "companyId", agencyIdList);
		
		//默认第一页房源过滤
		if (CollectionUtils.isNotEmpty(houseSearchDto.getHouseList())) {
			SolrUtil.addNotStatusCriteria(query, "hsId", houseSearchDto.getHouseList());
		}
		
		
		// 商圈Id
		long bizId = houseSearchDto.getBizId();
		if (bizId != 0) {
			Criteria criteria = new Criteria("bizId").is(bizId);
			query.addCriteria(criteria);
		}
		
		// 行政区Id
		long disId = houseSearchDto.getDistrictId();
		if (disId != 0) {
			Criteria criteria = new Criteria("districtId").is(disId);
			query.addCriteria(criteria);
		}
		
		// 地铁线路Id
		long lineId = houseSearchDto.getLineId();
		if (lineId != 0) {
			String lineIdTag = String.valueOf(lineId);
			SolrUtil.addTagCriteria(query, lineIdTag, "subwayLineId");
		}
		
		// 地铁站Id
		long stationId = houseSearchDto.getStationId();
		if (stationId != 0) {
			String stationIdTag = String.valueOf(stationId);
			SolrUtil.addTagCriteria(query, stationIdTag, "subwayStationId");
		}
		
		// 附近距离
		String distanceKey = "google";
		String location = houseSearchDto.getLocation();
		if (StringUtil.isNotEmpty(location)) { // 当前位置
			int nearybyDistance = searchConfiguration.getNearybyDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		} else if (stationId != 0) { // 地铁位置
			String stationPosition = houseSearchDto.getStationLocation();
			int stationDistance = searchConfiguration.getStationDistance();
			SolrUtil.addDistanceCriteria(query, distanceKey, stationPosition, stationDistance);
		}
		
		//按离地铁的距离搜索
		String distance = houseSearchDto.getDistance();
		if (StringUtil.isNotEmpty(distance)) {
			SolrUtil.addRegionCriteria(query, "subwayDistance", distance);
		}
		
		// 价格Id
		String price = houseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rentPriceMonth", price);
		}
		
		// 朝向
		String orientations = houseSearchDto.getOrientationStr();
		if (StringUtil.isNotEmpty(orientations)) {
			SolrUtil.addOrientationCriteria(query, orientations, "orientations");
		}

		// houseTag
		String houseTag = houseSearchDto.getHouseTag();
		if (StringUtil.isNotEmpty(houseTag)) {
			SolrUtil.addTagCriteria(query, houseTag, "housedTag");
		}
		
		// 付款方式 (默认-1查询全部)
		String payType = houseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addPayTypeCriteria(query, payType, "depositMonth", "periodMonth");
		}

		// 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if (keyword.contains("支持月付") || keyword.contains("分期") || keyword.contains("月付")) {
				Criteria criteria = new Criteria("isPayMonth").is(1);
				query.addCriteria(criteria);
			} else {
				keyword = ClientUtils.escapeQueryChars(keyword);
				String containExpression = "*" + keyword + "*";
				// 小区名
				Criteria criteria = new Criteria("communityName").expression(containExpression);
				// 地址
				criteria = criteria.or(new Criteria("address").expression(containExpression));
				// 商圈名
				criteria = criteria.or(new Criteria("bizname").expression(containExpression));
				// 行政区
//				criteria = criteria.or(new Criteria("district").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
//				criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 查询关键字
				criteria = criteria.or(new Criteria("text").expression(keyword));
				query.addCriteria(criteria);
			}
		}
		
		// 小区名称
		String communityName = houseSearchDto.getCommunityName();
		if (StringUtil.isNotEmpty(communityName)) {
			communityName = ClientUtils.escapeQueryChars(communityName);
			String containExpression = "*" + communityName + "*";
			Criteria criteria = new Criteria("communityName").expression(containExpression);
			query.addCriteria(criteria);
		}
		
		// 是否查询品牌公寓房源（1：品牌公寓房源；2：非品牌公寓房源）
		String companyType = houseSearchDto.getCompanyType();
		if (StringUtil.isNotBlank(companyType)) {
			SolrUtil.addCompanyQueryCriteria(query, companyType, "companyId", agencyIdList);
		}
		
		// 1 新房源，6 部分出租
		SolrUtil.addHouseStatusCriteria(query, "status");
		// 1 程序审核通过；3 图片审核通过
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		ScoredPage<HouseSolrResult> queryForPage = getSolrOperations().queryForPage(query, HouseSolrResult.class);
		return queryForPage;
	}
	
	/**
	 * 获取搜索查询分页条件
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	private PageRequest getSearchPageRequest(HouseSearchDto houseSearchDto) {
		if (houseSearchDto == null) {
			return null;
		}

		int pageNum = houseSearchDto.getPageNum();
		int page = pageNum - 1;
		page = page < 0 ? 0 : page;

		int pageSize = houseSearchDto.getPageSize();
		int size = pageSize;
		size = size < 0 ? 0 : size;

		Sort sort = getSort(houseSearchDto);

		PageRequest pageRequest = new PageRequest(page, size, sort);

		return pageRequest;

	}

	/**
	 * 获取排序规则
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	private Sort getSort(HouseSearchDto houseSearchDto) {
		if (houseSearchDto == null) {
			return null;
		}

		String orderType = houseSearchDto.getOrderType();
		String order = houseSearchDto.getOrder();
		String location = SolrUtil.getLocation(houseSearchDto);

		List<Order> orderList = getOrderList(orderType, order, location);

		if (CollectionUtils.isEmpty(orderList)) {
			return null;
		}

		Sort sort = new Sort(orderList);

		return sort;
	}

	/**
	 * 获取排方向
	 * 
	 * @param orderType
	 * @param order
	 * @param location
	 * @return
	 */
	private List<Order> getOrderList(String orderType, String order, String location) {

		List<Order> orderList = new ArrayList<Order>();

		// 传入的排序字段(主要排序条件)
		String houseOrderType = getHouseOrderType(orderType);
		if (StringUtil.isNotEmpty(houseOrderType)) {
			// 排序方向
			Direction direction = getDirection(orderType, order);
			//add by arison 20170904
			if(Direction.ASC.equals(direction)&&Constants.Search.ORDER_TYPE_PRICE.equals(orderType))
			{
				Order inputOrder = new Order(Direction.DESC, "rentPriceZero");
				orderList.add(inputOrder);
			}
			Order inputOrder = new Order(direction, houseOrderType);
			orderList.add(inputOrder);
		}

		// 按距离搜索，需要按距离进行排序(次要排序条件)
		if (StringUtil.isNotEmpty(location)) {
			Point point = SolrUtil.getPoint(location);
			if (point != null) {
				String distanceOrderName = SolrUtil.getGeoDistStr("google", point.getX(), point.getY());
				Direction distanceDirection = Direction.ASC;
				Order distanceOrder = new Order(distanceDirection, distanceOrderName);
				orderList.add(distanceOrder);
			}
		}

		return orderList;
	}

	/**
	 * 获取排序字段
	 * 
	 * @param orderType
	 * @return
	 */
	private String getHouseOrderType(String orderType) {

		if (StringUtil.isEmpty(orderType)) {
			return null;
		}

		if (orderType.equals(Constants.Search.ORDER_TYPE_PRICE)) {
			return "rentPriceMonth";
		} else if (orderType.equals(Constants.Search.ORDER_TYPE_AREA)) {
			return "fArea";
		} else if (orderType.equals(Constants.Search.ORDER_TYPE_PUBDATE)) {
			return "pubDate";
		} else {
			return null;
		}
	}

	/**
	 * 获取排序方向
	 * 
	 * @param orderType
	 * @param order
	 * @return
	 */
	private Direction getDirection(String orderType, String order) {
		Direction direction = Direction.ASC;

		if (StringUtil.isEmpty(order)) {
			// TODO 排序方向入参为空，是否针对配需字段对排序方向特殊处理?
		} else {
			if (Constants.Search.ORDER_DESC.equals(order)) {
				direction = Direction.DESC;
			}
		}

		return direction;
	}

}
