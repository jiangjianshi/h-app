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
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class IdealRentRoomSolrRepositoryImpl extends SimpleSolrRepository<RoomSolrResult, Long> {

	private static final Log logger = LogFactory.getLog(IdealRentRoomSolrRepositoryImpl.class);

	@Autowired
	private SearchConfiguration searchConfiguration;
	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;
	@Autowired
	private CompanyOffConfigRepository companyOffConfigRepository;


	@Autowired
	public IdealRentRoomSolrRepositoryImpl(@Qualifier("hfqRoomTemplate") SolrOperations solrOperations) {
		super(solrOperations);
	}
	
	public GroupResult<RoomSolrResult> getAllByMultiCondition(IdealRentHouseSearchDto idealRentHouseSearchDto) {
		Query query = new SimpleQuery();
		
		//为"无"时不展示
		Criteria commName = new Criteria("communityName").contains("无").not();
		query.addCriteria(commName);
		Criteria agencyPhone = new Criteria("agencyPhone").contains("无").not();
		query.addCriteria(agencyPhone);
		
		// 不查询被限制渠道的房源
		List<String> denyList = platformCustomerRepository.findAllWithoutPermission();
		if (CollectionUtils.isNotEmpty(denyList)) {
			SolrUtil.addNotStatusCriteria(query, "source", denyList);
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
		
//		//找房平台过滤店铺页房源
//		Criteria appIdCriteria = new Criteria("source").contains(Constants.HouseBase.COMPANY_SAAS_SHOP).not();
//		query.addCriteria(appIdCriteria);

		
		// 城市
		long cityId = idealRentHouseSearchDto.getCityId();
		if (cityId > 0) {
			Criteria cityCriteria = new Criteria("cityId").is(cityId);
			query.addCriteria(cityCriteria);
		}

		
		// 房型
		String eBedRoomNums = idealRentHouseSearchDto.geteBedRoomNums();// 自在整租
		String sBedRoomNums = idealRentHouseSearchDto.getsBedRoomNums();// 优选合租
		SolrUtil.addRoomStyleCriteria(query, sBedRoomNums, eBedRoomNums, "bedroomNums", "entireRent");
		
		// 整租/分租
		if (StringUtil.isEmpty(sBedRoomNums) && idealRentHouseSearchDto.getEntireRent() > -1) {
			int entireRent = idealRentHouseSearchDto.getEntireRent();
			Criteria entireRentCriteria = new Criteria("entireRent").is(entireRent);
			entireRentCriteria = entireRentCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_BOTH));
			query.addCriteria(entireRentCriteria);
		}
		
		
	
		// 价格Id
		String price = idealRentHouseSearchDto.getPrice();
		if (StringUtil.isNotEmpty(price)) {
			SolrUtil.addRegionCriteria(query, "rRentPriceMonth", price);
		}
		
		
		// 付款方式 (默认-1查询全部)
		String payType = idealRentHouseSearchDto.getPayType();
		if (StringUtil.isNotBlank(payType)) {
			SolrUtil.addrPayTypeCriteria(query, payType, "rDepositMonth", "rPeriodMonth");
		}

		// 按离地铁的距离搜索
		String distance = idealRentHouseSearchDto.getDistance();
		if (StringUtil.isNotEmpty(distance)) {
			SolrUtil.addRegionCriteria(query, "subwayDistance", distance);
		}
		
		// 关键词
		String keyword = idealRentHouseSearchDto.getKeyword();
		if (StringUtil.isNotEmpty(keyword)) {
			if (keyword.contains("支持月付") || keyword.contains("分期") || keyword.contains("月付")) {
				Criteria criteria = new Criteria("risPayMonth").is(1);
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
//						criteria = criteria.or(new Criteria("district").expression(containExpression));
				// 公寓名称
				criteria = criteria.or(new Criteria("companyName").expression(containExpression));
				// 出租方式
//						criteria = criteria.or(new Criteria("rentName").expression(containExpression));
				// 查询关键字
				criteria = criteria.or(new Criteria("text").expression(keyword));
				query.addCriteria(criteria);
			}
		}
		// status
		SolrUtil.addRoomStatusCriteria(query, "rStatus");
		//rapproveStatus 审核状态
		SolrUtil.addHouseApproveStatusCriteria(query, "rapproveStatus");
		
		GroupOptions groupOptions = new GroupOptions();
		
		//封装分组请求参数 
		groupOptions.addGroupByField(Constants.IdealrRentConfig.COMPANY_NAME);
		groupOptions.setTotalCount(true);
		
		//设置默认价格升序  取房源价格最小值
		Sort sort = getSort(idealRentHouseSearchDto);
		groupOptions.addSort(sort);
		groupOptions.setLimit(Constants.IdealrRentConfig.DEFAULT_SIZE);
		
		query.setGroupOptions(groupOptions);
		query.setRows(2000);
		
		logger.info("room-solr合租查询请求参数为：" + query.getCriteria());
		
		GroupPage<RoomSolrResult> page = getSolrOperations().queryForGroupPage(query,  RoomSolrResult.class);	
		
		//获取分组数据
		GroupResult<RoomSolrResult> fieldGroup = page.getGroupResult(Constants.IdealrRentConfig.COMPANY_NAME);

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
