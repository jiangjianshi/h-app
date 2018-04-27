/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseSolrRepositoryImpl.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.house.solr 
 * Date: 2016年5月12日下午12:19:49 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house.solr.ideal;

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
import com.huifenqi.hzf_platform.context.dto.request.house.IdealRentHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.request.house.MapHouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.CompanyOffConfig;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.SolrUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: HouseSolrRepositoryImpl date: 2017年10月09日 下午12:19:49 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class IdealRentHouseSolrRepositoryImpl extends SimpleSolrRepository<HouseSolrResult, Long> {

	private static final Log logger = LogFactory.getLog(IdealRentHouseSolrRepositoryImpl.class);
	
	@Autowired
	private SearchConfiguration searchConfiguration;
	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;
	@Autowired
	private CompanyOffConfigRepository companyOffConfigRepository;
	
	@Autowired
	public IdealRentHouseSolrRepositoryImpl(@Qualifier("hfqHouseTemplate") SolrOperations solrOperations) {
		super(solrOperations);
	}

	
	public GroupResult<HouseSolrResult> getAllByMultiCondition(IdealRentHouseSearchDto idealRentHouseSearchDto) {
		Query query = new SimpleQuery();
		
//		PageRequest pageRequest = getSearchPageRequest(houseSearchDto);
//		if (pageRequest == null) {
//			logger.error(LogUtils.getCommLog("pageRequest构造失败"));
//		} else {
//			query.setPageRequest(pageRequest);
//		}
		//小区名和手机号过滤
		Criteria commName = new Criteria("communityName").contains("无").not();
		query.addCriteria(commName);
		Criteria agencyPhone = new Criteria("agencyPhone").contains("无").not();
		query.addCriteria(agencyPhone);
		
		//不查询被限制渠道的房源
		List<String> denyList = platformCustomerRepository.findAllWithoutPermission();
		if (CollectionUtils.isNotEmpty(denyList)) {
			SolrUtil.addNotStatusCriteria(query, "fsource", denyList);
		}

		//按城市+公司过滤
		List<CompanyOffConfig> companyOffList =  companyOffConfigRepository.findCompanyOffConfigByCityId(idealRentHouseSearchDto.getCityId());
		if(!companyOffList.isEmpty()){
			List<String>  offList = new ArrayList<String>();
			for(CompanyOffConfig config : companyOffList){
				offList.add(config.getCompanyId());
			}
			SolrUtil.addNotStatusCriteria(query, "companyId", offList);		
		}
				
		//城市ID
		long cityId = idealRentHouseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}
		
		// 房型
		String eBedRoomNums = idealRentHouseSearchDto.geteBedRoomNums();// 自在整租
		String sBedRoomNums = idealRentHouseSearchDto.getsBedRoomNums();// 优选合租
		SolrUtil.addHouseStyleCriteria(query, eBedRoomNums, sBedRoomNums, "bedroomNums", "entireRent");
		
		// 租房宝典,白领优选筛选合租主卧和整租一居室的房源
		String cBedRoomNums = idealRentHouseSearchDto.getcBedRoomNums();
		if (!StringUtil.isEmpty(cBedRoomNums)) {
			Criteria cBedRoomNumsCriteria = new Criteria("bedroomNums").is(Integer.parseInt(cBedRoomNums));
			query.addCriteria(cBedRoomNumsCriteria);
		}
					
				
		// 整租/分租
		if (StringUtil.isEmpty(eBedRoomNums) && idealRentHouseSearchDto.getEntireRent() > -1) {
			int entireRent = idealRentHouseSearchDto.getEntireRent();
			Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
			entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
			query.addCriteria(entireRentCriteria);
		}
		
		//按离地铁的距离搜索
		String distance = idealRentHouseSearchDto.getDistance();
		if (StringUtil.isNotEmpty(distance)) {
			SolrUtil.addRegionCriteria(query, "subwayDistance", distance);
		}
		
		// 附近距离
		String distanceKey = "google";
		String location = idealRentHouseSearchDto.getLocation();
		if (StringUtil.isNotEmpty(location)) {//当前位置
				int nearybyDistance = searchConfiguration.getNearybyDistance();
				if(!idealRentHouseSearchDto.getDistance().equals("0") && !idealRentHouseSearchDto.getDistance().equals("")){//地图找房参数
					 nearybyDistance = Integer.valueOf(idealRentHouseSearchDto.getDistance());
				}
			SolrUtil.addDistanceCriteria(query, distanceKey, location, nearybyDistance);
		} 
		
		// 价格Id
		String price = idealRentHouseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rentPriceMonth", price);
		}
		
		
		// 付款方式 (默认-1查询全部)
		String payType = idealRentHouseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addPayTypeCriteria(query, payType, "depositMonth", "periodMonth");
		}

		
		// 1 新房源，6 部分出租
		SolrUtil.addHouseStatusCriteria(query, "status");
		// 1 程序审核通过；3 图片审核通过
		SolrUtil.addHouseApproveStatusCriteria(query, "approveStatus");
		
		
		GroupOptions groupOptions = new GroupOptions();
		
		//封装分组请求参数 
		groupOptions.addGroupByField(Constants.IdealrRentConfig.COMPANY_NAME);
		groupOptions.setTotalCount(true);
		
		//设置默认价格排序  取房源价格最小值
		//houseSearchDto.setOrderType(Constants.Search.ORDER_TYPE_PRICE);
		//houseSearchDto.setOrder(Constants.Search.ORDER_ASC);
		Sort sort = getSort(idealRentHouseSearchDto);
		groupOptions.addSort(sort);
		groupOptions.setLimit(Constants.IdealrRentConfig.DEFAULT_SIZE);
		query.setGroupOptions(groupOptions);
		query.setRows(2000);
		
		logger.info("solr整租查询请求参数为：" + query.getCriteria());
		GroupPage<HouseSolrResult> page = getSolrOperations().queryForGroupPage(query,  HouseSolrResult.class);	
		
		//获取分组数据
		GroupResult<HouseSolrResult> fieldGroup = page.getGroupResult(Constants.IdealrRentConfig.COMPANY_NAME);
		

		return fieldGroup;
	}


	/**
	 * 获取排序规则
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	private Sort getSort(IdealRentHouseSearchDto idealRentHouseSearchDto) {
		if (idealRentHouseSearchDto == null) {
			return null;
		}

		String orderType = idealRentHouseSearchDto.getOrderType();
		String order = idealRentHouseSearchDto.getOrder();
		String location = SolrUtil.getLocation(idealRentHouseSearchDto);

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
//				Order inputOrder = new Order(Direction.DESC, "rentPriceZero");
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
