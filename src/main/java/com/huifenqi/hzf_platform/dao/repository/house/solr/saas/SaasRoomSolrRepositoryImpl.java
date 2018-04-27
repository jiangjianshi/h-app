/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseSolrRepositoryImpl.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.house.solr 
 * Date: 2016年5月12日下午12:19:49 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house.solr.saas;

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
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.huifenqi.hzf_platform.configuration.SearchConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: RoomSolrRepositoryImpl date: 2016年5月12日 下午12:19:49 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class SaasRoomSolrRepositoryImpl extends SimpleSolrRepository<RoomSolrResult, Long> {

	private static final Log logger = LogFactory.getLog(SaasRoomSolrRepositoryImpl.class);

	@Autowired
	private SearchConfiguration searchConfiguration;

	@Autowired
	public SaasRoomSolrRepositoryImpl(@Qualifier("hfqRoomTemplate") SolrOperations solrOperations) {
		super(solrOperations);
	}

	public Page<RoomSolrResult> findAllByMultiCondition(HouseSearchDto houseSearchDto) {

		Query query = new SimpleQuery();

		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
		if (pageRequest == null) {
			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
		} else {
			query.setPageRequest(pageRequest);
		}

		//店铺页只查SaaS平台房源
		Criteria appIdCriteria = new Criteria("source").contains(Constants.HouseBase.COMPANY_SAAS_SHOP);
		query.addCriteria(appIdCriteria);
		
		// cityId
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		String companyId = houseSearchDto.getCompanyId();
		if (StringUtil.isNotEmpty(companyId)) {
			// Criteria criteria = new Criteria("companyId").is(companyId);
			// query.addCriteria(criteria);
			SolrUtil.addCompanyIdCriteria(query, companyId, "companyId");
			String isTop=houseSearchDto.getIsTop();
			if(StringUtil.isNotEmpty(isTop)){
				Criteria criteria = new Criteria("risTop").is(isTop);
				query.addCriteria(criteria);
			}
		}

		// 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			keyword = ClientUtils.escapeQueryChars(keyword);
			String containExpression = "*" + keyword + "*";
			Criteria criteria = new Criteria("communityName").expression(containExpression);
			// 地址
			criteria = criteria.or(new Criteria("address").expression(containExpression));
			// 小区名
			criteria = criteria.or(new Criteria("bizname").expression(containExpression));
			// 商圈
			query.addCriteria(criteria);
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
			SolrUtil.addRegionCriteria(query, "rRentPriceMonth", price);
		}

		// 朝向
		int orientation = houseSearchDto.getOrientation();
		if (orientation != 0) {
			Criteria criteria = new Criteria("roomori").is(orientation);
			query.addCriteria(criteria);
		}

		// 面积
		String area = houseSearchDto.getArea();
		if (StringUtil.isNotEmpty(area)) {
			SolrUtil.addRegionCriteria(query, "rArea", area);
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
			SolrUtil.addTagCriteria(query, houseTag, "roomtag");
		}

		// 发布类型
		long pubType = houseSearchDto.getPubType();
		if (pubType != 0) {
			Criteria criteria = new Criteria("rpubType").is(pubType);
			query.addCriteria(criteria);
		}

		// status
		SolrUtil.addRoomStatusCriteria(query, "rStatus");
		logger.info("solr合租查询请求参数为：" + query.getCriteria());
		ScoredPage<RoomSolrResult> queryForPage = getSolrOperations().queryForPage(query, RoomSolrResult.class);
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
			return "rRentPriceMonth";
		} else if (orderType.equals(Constants.Search.ORDER_TYPE_AREA)) {
			return "rArea";
		} else if (orderType.equals(Constants.Search.ORDER_TYPE_PUBDATE)) {
			return "rpubdate";
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
