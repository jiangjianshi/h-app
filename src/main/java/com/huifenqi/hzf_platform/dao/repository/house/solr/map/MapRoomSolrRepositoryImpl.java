/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseSolrRepositoryImpl.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.house.solr 
 * Date: 2016年5月12日下午12:19:49 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house.solr.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.huifenqi.hzf_platform.configuration.SearchConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.MapHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.map.RoomMapSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.CompanyOffConfig;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: RoomSolrRepositoryImpl date: 2016年5月12日 下午12:19:49 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class MapRoomSolrRepositoryImpl extends SimpleSolrRepository<RoomSolrResult, Long> {

	private static final Log logger = LogFactory.getLog(MapRoomSolrRepositoryImpl.class);

	@Autowired
	private SearchConfiguration searchConfiguration;
	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;
	@Autowired
	private CompanyOffConfigRepository companyOffConfigRepository;
	

	@Autowired
	public MapRoomSolrRepositoryImpl(@Qualifier("hfqRoomTemplate") SolrOperations solrOperations) {
		super(solrOperations);
	}
	
	public GroupResult<RoomMapSolrResult> getAllByMultiCondition(MapHouseSearchDto houseSearchDto, List<String> agencyIdList) {
		Query query = new SimpleQuery();
		
		//为"无"时不展示
		Criteria commName = new Criteria("communityName").contains("无").not();
		query.addCriteria(commName);
		Criteria agencyPhone = new Criteria("agencyPhone").contains("无").not();
		query.addCriteria(agencyPhone);
		
		// 过滤saas线上体验账号
//		Criteria companyId10580 = new Criteria("companyId").contains(Constants.HouseBase.COMPANY_ID_10580).not();
//		query.addCriteria(companyId10580);
//		Criteria companyId10907 = new Criteria("companyId").contains(Constants.HouseBase.COMPANY_ID_10907).not();
//		query.addCriteria(companyId10907);
		
		// 不查询被限制渠道的房源
		List<String> denyList = platformCustomerRepository.findAllWithoutPermission();
		if (CollectionUtils.isNotEmpty(denyList)) {
			SolrUtil.addNotStatusCriteria(query, "source", denyList);
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
				
//		//找房平台过滤店铺页房源
//		Criteria appIdCriteria = new Criteria("source").contains(Constants.HouseBase.COMPANY_SAAS_SHOP).not();
//		query.addCriteria(appIdCriteria);

		
		// 城市
		long cityId = houseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		// 关键词
		String keyword = houseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if ("支持月付".equals(keyword) || "分期".equals(keyword) || "月付".equals(keyword)) {
				Criteria criteria = new Criteria("risPayMonth").is(1);
				query.addCriteria(criteria);
			} else {
				keyword = ClientUtils.escapeQueryChars(keyword);
				String containExpression = "*" + keyword + "*";
				// 小区名
				Criteria criteria = new Criteria("communityName").expression(containExpression);
				// 地址
				criteria = criteria.or(new Criteria("address").expression(containExpression));
				// 商圈
				criteria = criteria.or(new Criteria("bizname").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
				criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 房间类型名称
				criteria = criteria.or(new Criteria("rtName").expression(containExpression));
				/*// 查询关键字
				Criteria criteria = new Criteria("text").expression(containExpression);*/
				query.addCriteria(criteria);
			}
		}

		// 房型
		String eBedRoomNums = houseSearchDto.geteBedRoomNums();// 自在整租
		String sBedRoomNums = houseSearchDto.getsBedRoomNums();// 优选合租
		SolrUtil.addRoomStyleCriteria(query, sBedRoomNums, eBedRoomNums, "bedroomNums", "entireRent");
		
		// 整租/分租
		if (StringUtil.isEmpty(sBedRoomNums) && houseSearchDto.getEntireRent() > -1) {
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
		if (StringUtil.isNotEmpty(location)) {//当前位置
				int nearybyDistance = searchConfiguration.getNearybyDistance();
				if(!houseSearchDto.getDistance().equals("0") && !houseSearchDto.getDistance().equals("")){//地图找房参数
					 nearybyDistance = Integer.valueOf(houseSearchDto.getDistance());
				}
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		}
		
		// 价格Id
		String price = houseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rRentPriceMonth", price);
		}
		
		// 朝向
		String orientations = houseSearchDto.getOrientationStr();
		if (StringUtil.isNotEmpty(orientations)) {
			SolrUtil.addOrientationCriteria(query, orientations, "roomori");
		}

		// houseTag
		String houseTag = houseSearchDto.getHouseTag();
		if (StringUtil.isNotEmpty(houseTag)) {
			SolrUtil.addTagCriteria(query, houseTag, "roomtag");
		}
		
		// 付款方式 (默认-1查询全部)
		String payType = houseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addrPayTypeCriteria(query, payType, "rDepositMonth", "rPeriodMonth");
		}
		
		// 是否查询品牌公寓房源（1：品牌公寓房源；2：非品牌公寓房源）
		String companyType = houseSearchDto.getCompanyType();
		if (StringUtil.isNotBlank(companyType)) {
			SolrUtil.addCompanyQueryCriteria(query, companyType, "companyId", agencyIdList);
		}

		// status
		SolrUtil.addRoomStatusCriteria(query, "rStatus");
		//rapproveStatus 审核状态
		SolrUtil.addHouseApproveStatusCriteria(query, "rapproveStatus");
		
		GroupOptions groupOptions = new GroupOptions();
		
		//封装分组请求参数 
		int level = houseSearchDto.getLevel();
		groupOptions.addGroupByField(Constants.MapHouse.getLevelName(level));
		groupOptions.setTotalCount(true);
		
		//设置默认价格升序  取房源价格最小值
		houseSearchDto.setOrderType(Constants.Search.ORDER_TYPE_PRICE);
		houseSearchDto.setOrder(Constants.Search.ORDER_ASC);
		Sort sort = getSort(houseSearchDto);
		groupOptions.addSort(sort);
		groupOptions.setLimit(1);
		
		query.setGroupOptions(groupOptions);
		query.setRows(2000);
		
		logger.info("room-solr合租查询请求参数为：" + query.getCriteria());
		
		GroupPage<RoomMapSolrResult> page = getSolrOperations().queryForGroupPage(query,  RoomMapSolrResult.class);	
		
		//获取分组数据
		GroupResult<RoomMapSolrResult> fieldGroup = page.getGroupResult(Constants.MapHouse.getLevelName(level));

		return fieldGroup;
	}
	

	/**
	 * 获取排序规则
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	private Sort getSort(MapHouseSearchDto houseSearchDto) {
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
//			if(Direction.ASC.equals(direction)&&Constants.Search.ORDER_TYPE_PRICE.equals(orderType))
//			{
//				Order inputOrder = new Order(Direction.DESC, "rRentPriceZero");
//				orderList.add(inputOrder);
//			}

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
