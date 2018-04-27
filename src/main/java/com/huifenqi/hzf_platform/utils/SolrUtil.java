/** 
 * Project Name: hzf_platform_project 
 * File Name: SolrUtil.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年5月17日下午6:29:49 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.geo.Distance;
import org.springframework.data.solr.core.geo.Point;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Function;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.request.house.IdealRentHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.request.house.MapHouseSearchDto;
import com.huifenqi.hzf_platform.context.enums.ApproveStatusEnum;

/**
 * ClassName: SolrUtil date: 2016年5月17日 下午6:29:49 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class SolrUtil {

	private static final Log logger = LogFactory.getLog(SolrUtil.class);

	/**
	 * 获取公寓ID条件 companyId
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addCompanyIdCriteria(Query query, String comIds, String propertyName) {

		String[] comIdArr = comIds.split(StringUtil.COMMA);
		if (comIdArr == null || comIdArr.length == 0) {
			return;
		}

		Criteria comIdCriteria = null;
		for (String comId : comIdArr) {
			if (comIdCriteria == null) {
				comIdCriteria = new Criteria(propertyName).is(comId);
			} else {
				comIdCriteria = comIdCriteria.or(new Criteria(propertyName).is(comId));
			}
		}
		
		if (comIdCriteria != null) {
			query.addCriteria(comIdCriteria);
		}

	}

	/**
	 * 获取房源ID条件 sellId
	 *
	 * @param query
	 * @param sellIds
     * entireType 1 整租 2 合租
	 * @param propertyName
	 */
	public static void addSellIdCriteria(Query query, String sellIds,int entireType) {

		Criteria sellIdCriteria = null;
		String[] sellIdArr = sellIds.split(StringUtil.COMMA);
		if (sellIdArr == null || sellIdArr.length == 0) {
			sellIdCriteria = new Criteria("hsId").is(-1);
			return;
		}

		for (String sellId : sellIdArr) {
		    if(entireType==1){  //整租时
                String str[]=sellId.split(StringUtil.COLON); //:分隔
                if(str[0]!=null && "0".equals(str[1])) {
                    if (sellIdCriteria == null) {
                        sellIdCriteria = new Criteria("hsId").is(str[0]);
                    } else {
                        sellIdCriteria = sellIdCriteria.or(new Criteria("hsId").is(str[0]));
                    }
                }
                
                if(sellIdCriteria==null)
                {
                	 sellIdCriteria = new Criteria("hsId").is(-1);
                }
            }else{ //合租时
                String str[]=sellId.split(StringUtil.COLON); //:分隔
                if(str[0]!=null && !"0".equals(str[1])) {
                    if (sellIdCriteria == null) {
                        sellIdCriteria = new Criteria("hsId").is(str[0]).and("id").is(str[1]).connect();
                    } else {
						Criteria newSellIdCriteria = new Criteria("hsId").is(str[0]).and("id").is(str[1]);
                        sellIdCriteria = sellIdCriteria.or(newSellIdCriteria);
                    }
                }
                
                if(sellIdCriteria==null)
                {
                	 sellIdCriteria = new Criteria("hsId").is(-1);
                }
            }
		}
		
		if (sellIdCriteria != null) {
			query.addCriteria(sellIdCriteria);
		}
	}
	
	/**
	 * 获取房源ID条件 sellId（收藏/足迹）
	 *
	 * @param query
	 * @param sellIds
     * entireType 1 整租 2 合租
	 * @param propertyName
	 */
	public static void addAllSellIdCriteria(Query query, String sellIds) {
		Criteria sellIdCriteria = null;
		String[] sellIdArr = sellIds.split(StringUtil.COMMA);
		if (sellIdArr == null || sellIdArr.length == 0) {
			sellIdCriteria = new Criteria("hsId").is(-1);
			return;
		}

		for (String sellId : sellIdArr) {
            String str[]=sellId.split(StringUtil.COLON); //:分隔
            if(str[0]!=null && "0".equals(str[1])) {
                if (sellIdCriteria == null) {
                    sellIdCriteria = new Criteria("hsId").is(str[0]);
                } else {
                    sellIdCriteria = sellIdCriteria.or(new Criteria("hsId").is(str[0]));
                }
            } else {
            	if (sellIdCriteria == null) {
                    sellIdCriteria = new Criteria("hsId").is(str[0]).and("roomId").is(str[1]).connect();
                } else {
					Criteria newSellIdCriteria = new Criteria("hsId").is(str[0]).and("roomId").is(str[1]);
                    sellIdCriteria = sellIdCriteria.or(newSellIdCriteria);
                }
            }
            
            if (sellIdCriteria==null) {
            	 sellIdCriteria = new Criteria("hsId").is(-1);
            }
		}
		
		if (sellIdCriteria != null) {
			query.addCriteria(sellIdCriteria);
		}
	}

	/**
	 * 获取房源状态条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addHouseStatusCriteria(Query query, String propertyName) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}

		List<Integer> list = new ArrayList<Integer>();
		list.add(Constants.HouseBase.STATUS_NEW);
//		list.add(Constants.HouseBase.STATUS_PRE_VERIFY);
//		list.add(Constants.HouseBase.STATUS_VERIFY_SUCCESS);
//		list.add(Constants.HouseBase.STATUS_VERIFY_FAIL);
		list.add(Constants.HouseBase.STATUS_PARTLY_RENT); // 部分出租

		addStatusCriteria(query, propertyName, list);

	}
	
	/**
	 * 获取审核状态条件
	 * @param query
	 * @param propertyName
	 */
	public static void addHouseApproveStatusCriteria(Query query, String propertyName) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}
		List<Integer> list = new ArrayList<Integer>();
		list.add(ApproveStatusEnum.SYS_APP_PASS.getCode());
		list.add(ApproveStatusEnum.IMG_APP_PASS.getCode());
		addStatusCriteria(query, propertyName, list);
		
	}


	/**
	 * 获取房源标签条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addRoomStatusCriteria(Query query, String propertyName) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}

		List<Integer> list = new ArrayList<Integer>();
		list.add(Constants.HouseBase.STATUS_NEW);
//		list.add(Constants.HouseBase.STATUS_PRE_VERIFY);
//		list.add(Constants.HouseBase.STATUS_VERIFY_SUCCESS);
//		list.add(Constants.HouseBase.STATUS_VERIFY_FAIL);

		addStatusCriteria(query, propertyName, list);

	}


	/**
	 * 包含该属性
	 * @param query
	 * @param propertyName
	 * @param statusList
	 */
	public static void addStatusCriteria(Query query, String propertyName, List<Integer> statusList) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}
		if (CollectionUtils.isEmpty(statusList)) {
			return;
		}

		Criteria statusCriteria = null;
		for (Integer status : statusList) {
			if (statusCriteria == null) {
				statusCriteria = new Criteria(propertyName).is(status);
			} else {
				statusCriteria = statusCriteria.or(new Criteria(propertyName).is(status));
			}
		}

		if (statusCriteria != null) {
			query.addCriteria(statusCriteria);
		}
	}
	
	/**
	 * 不包含该属性
	 * @param query
	 * @param propertyName
	 * @param statusList
	 */
	public static void addNotStatusCriteria(Query query, String propertyName, List<String> statusList) {
//		Criteria statusCriteria = null;
		for (String status : statusList) {
//			if (statusCriteria == null) {
//				statusCriteria = new Criteria(propertyName).is(status).not();
//			} else {
//				statusCriteria = statusCriteria.and(new Criteria(propertyName).is(status).not());
//			}
			query.addCriteria(new Criteria(propertyName).is(status).not());
		}

//		if (statusCriteria != null) {
//			query.addCriteria(statusCriteria);
//		}
	}
	
	/**
	 * 获取标签类条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addTagCriteria(Query query, String tag, String propertyName) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}
		List<Criteria> houseTagCriteriaList = getTagCriteriaList(tag, propertyName);
		if (CollectionUtils.isEmpty(houseTagCriteriaList)) {
			return;
		}
		for (Criteria criteria : houseTagCriteriaList) {
			query.addCriteria(criteria);
		}
	}

	/**
	 * 获取标签条件
	 * 
	 * @param tag
	 * @param propertyName
	 * @return
	 */
	public static List<Criteria> getTagCriteriaList(String tag, String propertyName) {
		if (StringUtil.isEmpty(tag)) {
			return null;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return null;
		}
		String[] split = tag.split(StringUtil.COMMA);
		if (split == null || split.length == 0) {
			return null;
		}

		List<Criteria> houseTagCriteriaList = new ArrayList<Criteria>();
		for (String value : split) {
			Criteria houseTagCriteria = getSingleTagCriteria(value, propertyName);
			if (houseTagCriteria != null) {
				houseTagCriteriaList.add(houseTagCriteria);
			}
		}
		return houseTagCriteriaList;
	}

	/**
	 * 获取单个房源标签条件
	 * 
	 * @param singleTag
	 * @param propertyName
	 * @return
	 */
	public static Criteria getSingleTagCriteria(String singleTag, String propertyName) {
		if (StringUtil.isEmpty(singleTag)) {
			return null;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return null;
		}
		Criteria criteria = new Criteria(propertyName).is(singleTag);
		criteria = criteria.or(new Criteria(propertyName).startsWith(singleTag + StringUtil.COMMA));
		criteria = criteria.or(new Criteria(propertyName).endsWith(StringUtil.COMMA + singleTag));
		criteria = criteria.or(new Criteria(propertyName).contains(StringUtil.COMMA + singleTag + StringUtil.COMMA));

		return criteria;
	}

	/**
	 * 添加区域条件
	 * 
	 * @param query
	 * @param value
	 */
	public static void addRegionCriteria(Query query, String propertyName, String value) {
		if (query == null) {
			return;
		}
		Object low = getLowValue(value);
		Object high = getHighValue(value);
		Criteria criteria = getRegionCriteria(propertyName, low, high);
		if (criteria != null) {
			query.addCriteria(criteria);
		}
	}

	/**
	 * 获取距离条件
	 * 
	 * @param query
	 * @param value
	 */
	public static void addDistanceCriteria(Query query, String propertyName, String locationValue, int distanceValue) {
		if (query == null) {
			return;
		}
		if (StringUtil.isEmpty(propertyName)) {
			return;
		}
		Point location = getPoint(locationValue);
		if (location == null) {
			logger.error(LogUtils.getCommLog("坐标解析失败, locationValue:" + locationValue));
			return;
		}

		double locationDouble = (double) distanceValue;
		locationDouble = locationDouble / 1000; // 米转千米
        Distance distance = new Distance(locationDouble);
		Criteria criteria = new Criteria(propertyName).within(location, distance);
		if (criteria != null) {
			query.addCriteria(criteria);
		}

		if (query instanceof SimpleQuery) {
			SimpleQuery simpleQuery = (SimpleQuery) query;
			simpleQuery.addProjectionOnField("*");
			simpleQuery.addProjectionOnField("_dist_:" + getGeoDistStr(propertyName, location.getX(), location.getY()));
		}

	}

	/**
	 * 获取距离字符串
	 * 
	 * @param propertyName
	 * @param x
	 * @param y
	 * @return
	 */
	public static String getGeoDistStr(String propertyName, double x, double y) {
		if (StringUtil.isEmpty(propertyName)) {
			return null;
		}
		return "geodist(" + propertyName + "," + x + "," + y + ")";
	}

	/**
	 * 获取距离字符串
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	public static String getLocation(HouseSearchDto houseSearchDto) {
		if (houseSearchDto == null) {
			return null;
		}

		String location = null;

		String currentLocation = houseSearchDto.getLocation();
		String stationPosition = houseSearchDto.getStationLocation();
		if (StringUtil.isNotEmpty(currentLocation)) { // 当前位置
			location = currentLocation;
		} else if (StringUtil.isNotEmpty(stationPosition)) { // 地铁位置
			location = stationPosition;
		}

		return location;
	}
	
	/**
	 * 获取距离字符串
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	public static String getLocation(MapHouseSearchDto houseSearchDto) {
		if (houseSearchDto == null) {
			return null;
		}

		String location = null;

		String currentLocation = houseSearchDto.getLocation();
		String stationPosition = houseSearchDto.getStationLocation();
		if (StringUtil.isNotEmpty(currentLocation)) { // 当前位置
			location = currentLocation;
		} else if (StringUtil.isNotEmpty(stationPosition)) { // 地铁位置
			location = stationPosition;
		}

		return location;
	}

	
	/**
	 * 获取距离字符串
	 * 
	 * @param houseSearchDto
	 * @return
	 */
	public static String getLocation(IdealRentHouseSearchDto idealRentHouseSearchDto) {
		if (idealRentHouseSearchDto == null) {
			return null;
		}

		String location = null;

		String currentLocation = idealRentHouseSearchDto.getLocation();
		String stationPosition = idealRentHouseSearchDto.getStationLocation();
		if (StringUtil.isNotEmpty(currentLocation)) { // 当前位置
			location = currentLocation;
		} else if (StringUtil.isNotEmpty(stationPosition)) { // 地铁位置
			location = stationPosition;
		}

		return location;
	}
	/**
	 * 获取坐标
	 * 
	 * @param locationValue
	 *            传入参数先经度后纬度
	 * @return
	 */
	public static Point getPoint(String locationValue) {
		if (StringUtil.isEmpty(locationValue)) {
			return null;
		}
		String[] split = locationValue.split(StringUtil.COMMA);
		if (split == null || split.length != 2) {
			return null;
		}
		String positionY = split[0];
		String positionX = split[1];
		Double x = StringUtil.parseDouble(positionX);
		if (x == null) {
			return null;
		}
		Double y = StringUtil.parseDouble(positionY);
		if (y == null) {
			return null;
		}
		Point location = new Point(x, y);
		return location;
	}

	/**
	 * 获取区间查询条件
	 * 
	 * @param propertyName
	 * @param low
	 * @param high
	 * @return
	 */
	public static Criteria getRegionCriteria(String propertyName, Object low, Object high) {
		if (StringUtil.isEmpty(propertyName)) {
			return null;
		}
		Criteria criteria = null;
		if (low == null) { // low 为空
			if (high == null) {
				criteria = new Criteria(propertyName).isNotNull();
			} else {
				criteria = new Criteria(propertyName).lessThanEqual(high);
			}
		} else {
			if (high == null) {
				criteria = new Criteria(propertyName).greaterThanEqual(low);
			} else {
				criteria = new Criteria(propertyName).between(low, high);
			}
		}
		return criteria;
	}

	/**
	 * 获取区间较小值
	 * 
	 * @param region
	 * @return
	 */
	public static Object getLowValue(String region) {
		if (StringUtil.isEmpty(region)) {
			return null;
		}
		int leftIndex = region.indexOf(StringUtil.LEFT_SQUARE_BRACKET);
		int middleIndex = region.indexOf(StringUtil.COMMA);
		if (leftIndex >= 0 && middleIndex > leftIndex) {
			String left = region.substring(leftIndex + 1, middleIndex);
			if (StringUtil.STAR.equals(left)) {
				return null;
			} else {
				return left;
			}
		}

		return null;
	}

	/**
	 * 获取区间较大值
	 * 
	 * @param region
	 * @return
	 */
	public static Object getHighValue(String region) {
		if (StringUtil.isEmpty(region)) {
			return null;
		}
		int middleIndex = region.indexOf(StringUtil.COMMA);
		int rightIndex = region.indexOf(StringUtil.RIGHT_SQUARE_BRACKET);
		if (middleIndex >= 0 && rightIndex > middleIndex) {
			String right = region.substring(middleIndex + 1, rightIndex);
			if (StringUtil.STAR.equals(right)) {
				return null;
			} else {
				return right;
			}
		}

		return null;
	}

	/**
	 * 获取Solr日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getSolrDate(String dateStr) {
		Date parseDateTime = DateUtil.parseDateTime(dateStr);
		return parseDateTime;
	}
	
	/**
	 * 获取付款方式条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addPayTypeCriteria(Query query, String payType, String propertyName1, String propertyName2) {
		Criteria criteria = null;
		String[] type = payType.split(",");
		for (String typeCode : type) {
			if (Constants.payType.NO_DEPOSIT == Integer.parseInt(typeCode)) {// 免押金
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(0);
				Criteria periodMonthCriteria = new Criteria(propertyName2).lessThan(6);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				criteria = monthCriteria;
			} else if (Constants.payType.DEPOSIT_ONE_PAY_ONE == Integer.parseInt(typeCode)) {// 押一付一
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(1);
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(1);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else if (Constants.payType.DEPOSIT_ONE_PAY_THREE == Integer.parseInt(typeCode)) {// 押一付三
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(1);
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(3);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else if (Constants.payType.YEAR_PAY == Integer.parseInt(typeCode)) {// 半年/年付
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(6);
				Criteria monthCriteria = periodMonthCriteria.connect().or(new Criteria(propertyName2).is(12));
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else {// 1:支持月付；0:不支持月付
				Criteria monthCriteria = new Criteria("isPayMonth").is(1);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			}
		}
		if (criteria != null) {
			query.addCriteria(criteria);
		}
	}
	
	/**
	 * 获取付款方式条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addrPayTypeCriteria(Query query, String payType, String propertyName1, String propertyName2) {
		Criteria criteria = null;
		String[] type = payType.split(",");
		for (String typeCode : type) {
			if (Constants.payType.NO_DEPOSIT == Integer.parseInt(typeCode)) {// 免押金
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(0);
				Criteria periodMonthCriteria = new Criteria(propertyName2).lessThan(6);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				criteria = monthCriteria;
			} else if (Constants.payType.DEPOSIT_ONE_PAY_ONE == Integer.parseInt(typeCode)) {// 押一付一
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(1);
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(1);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else if (Constants.payType.DEPOSIT_ONE_PAY_THREE == Integer.parseInt(typeCode)) {// 押一付三
				Criteria depositMonthCriteria = new Criteria(propertyName1).is(1);
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(3);
				Criteria monthCriteria = depositMonthCriteria.connect().and(periodMonthCriteria);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else if (Constants.payType.YEAR_PAY == Integer.parseInt(typeCode)) {// 半年/年付
				Criteria periodMonthCriteria = new Criteria(propertyName2).is(6);
				Criteria monthCriteria = periodMonthCriteria.connect().or(new Criteria(propertyName2).is(12));
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			} else {// 1:支持月付；0:不支持月付
				Criteria monthCriteria = new Criteria("risPayMonth").is(1);
				if (criteria == null) {
					criteria = monthCriteria;
				} else {
					criteria = criteria.connect().or(monthCriteria);
				}
			}
		}
		if (criteria != null) {
			query.addCriteria(criteria);
		}
	}
	
	/**
	 * 获取朝向条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addOrientationCriteria(Query query, String orientations, String propertyName) {
		String[] orientationArr = orientations.split(StringUtil.COMMA);
		if (orientationArr == null || orientationArr.length == 0) {
			return;
		}
		Criteria orientationCriteria = null;
		for (String orientation : orientationArr) {
			if (orientationCriteria == null) {
				orientationCriteria = new Criteria(propertyName).is(orientation);
			} else {
				orientationCriteria = orientationCriteria.or(new Criteria(propertyName).is(orientation));
			}
		}
		if (orientationCriteria != null) {
			query.addCriteria(orientationCriteria);
		}
	}
	
	/**
	 * 获取品牌公寓筛选条件
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addCompanyCriteria(Query query, String companyIds, String propertyName, List<String> agencyIdList) {
		Criteria companyCriteria = null;
		if (!"-1".equals(companyIds)) {
			if (StringUtil.isNotEmpty(companyIds)) {
				String[] companyIdArr = companyIds.split(",");
				if (companyIdArr == null || companyIdArr.length == 0) {
					return;
				}
				for (String companyId : companyIdArr) {
					if (companyCriteria == null) {
						companyCriteria = new Criteria(propertyName).is(companyId);
					} else {
						companyCriteria = companyCriteria.or(new Criteria(propertyName).is(companyId));
					}
				}
			}
		} else if ("-1".equals(companyIds)) {
			if (CollectionUtils.isNotEmpty(agencyIdList)) {
				for (String agencyId : agencyIdList) {
					if (companyCriteria == null) {
						companyCriteria = new Criteria(propertyName).is(agencyId);
					} else {
						companyCriteria = companyCriteria.or(new Criteria(propertyName).is(agencyId));
					}
				}
			} else {
				return;
			}
		}
		if (companyCriteria != null) {
			query.addCriteria(companyCriteria);
		}
	}
	
	/**
	 * 是否查询品牌公寓房源（1：品牌公寓房源；2：非品牌公寓房源）
	 * 
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addCompanyQueryCriteria(Query query, String companyType, String propertyName, List<String> agencyIdList) {
		Criteria companyCriteria = null;
		String[] companyTypeArr = companyType.split(",");
		if (companyTypeArr.length == 1) {
			if (Integer.parseInt(companyTypeArr[0]) == 1) {
				if (CollectionUtils.isNotEmpty(agencyIdList)) {
					for (String agencyId : agencyIdList) {
						if (companyCriteria == null) {
							companyCriteria = new Criteria(propertyName).is(agencyId);
						} else {
							companyCriteria = companyCriteria.or(new Criteria(propertyName).is(agencyId));
						}
					}
				} else {
					return;
				}
			} else if (Integer.parseInt(companyTypeArr[0]) == 2) {
				if (CollectionUtils.isNotEmpty(agencyIdList)) {
					for (String agencyId : agencyIdList) {
						Criteria criteria = new Criteria(propertyName).is(agencyId).not();
						query.addCriteria(criteria);
					}
				} else {
					return;
				}
			}
		}
		if (companyCriteria != null) {
			query.addCriteria(companyCriteria);
		}
	}
	
	/**
	 * @Title: addHouseStyleCriteria
	 * @Description: 添加房源查询条件
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addHouseStyleCriteria(Query query, String eBedRoomNums, String sBedRoomNums, String propertyName1, String propertyName2) {
		Criteria bedRoomCriteria = null;
		if (StringUtil.isNotEmpty(eBedRoomNums)) {
			Criteria ebedRoomCriteria = null;
			String[] a = eBedRoomNums.split(",");
			// 数组的第一个元素如果是-1，表示自在整租居室数量不限制
			if (Integer.parseInt(a[0]) != -1) {
				for (String string : a) {
					if (ebedRoomCriteria == null) {
						ebedRoomCriteria = new Criteria(propertyName1).is(string);
					} else {
						ebedRoomCriteria = ebedRoomCriteria.or(new Criteria(propertyName1).is(string));
					}
				}
			}
			Criteria eCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			if (ebedRoomCriteria != null) {
				bedRoomCriteria = ebedRoomCriteria.connect().and(eCompanyCriteria);
			} else {
				bedRoomCriteria = eCompanyCriteria;
			}
		} else if (StringUtil.isEmpty(eBedRoomNums) && StringUtil.isNotEmpty(sBedRoomNums)) {
			Criteria ebedRoomCriteria = new Criteria(propertyName1).is(100);
			Criteria eCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			bedRoomCriteria = ebedRoomCriteria.connect().and(eCompanyCriteria);
		}
		if (bedRoomCriteria != null) {
			query.addCriteria(bedRoomCriteria);
		}
	}
	
	/**
	 * @Title: addRoomStyleCriteria
	 * @Description: 添加房间查询条件
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addRoomStyleCriteria(Query query, String sBedRoomNums, String eBedRoomNums, String propertyName1, String propertyName2) {
		Criteria bedRoomCriteria = null;
		if (StringUtil.isNotEmpty(sBedRoomNums)) {
			Criteria sbedRoomCriteria = null;
			String[] a = sBedRoomNums.split(",");
			// 数组的第一个元素如果是-1，表示优选合租居室数量不限制
			if (Integer.parseInt(a[0]) != -1) {
				for (String string : a) {
					if (sbedRoomCriteria == null) {
						if (Integer.parseInt(string) > 3) {
							sbedRoomCriteria = new Criteria(propertyName1).between(4, string);
						} else {
							sbedRoomCriteria = new Criteria(propertyName1).is(string);
						}
					} else {
						if (Integer.parseInt(string) > 3) {
							sbedRoomCriteria = sbedRoomCriteria.or(new Criteria(propertyName1).between(4, string));
						} else {
							sbedRoomCriteria = sbedRoomCriteria.or(new Criteria(propertyName1).is(string));
						}
					}
				}
			}
			Criteria sCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_SHARE);
			if (sbedRoomCriteria != null) {
				bedRoomCriteria = sbedRoomCriteria.connect().and(sCompanyCriteria);
			} else {
				bedRoomCriteria = sCompanyCriteria;
			}
		} else if (StringUtil.isEmpty(sBedRoomNums) && StringUtil.isNotEmpty(eBedRoomNums)) {
			Criteria sbedRoomCriteria = new Criteria(propertyName1).is(100);
			Criteria sCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_SHARE);
			bedRoomCriteria = sbedRoomCriteria.connect().and(sCompanyCriteria);
		}
		if (bedRoomCriteria != null) {
			query.addCriteria(bedRoomCriteria);
		}
	}


	/**
	 * @Title: addHouseStyleCriteria
	 * @Description: 添加房源查询条件
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addHousesAllStyleCriteria(Query query, String eBedRoomNums, String sBedRoomNums, String propertyName1, String propertyName2) {

		Criteria bedRoomCriteria = null;
		if (StringUtil.isNotEmpty(eBedRoomNums)) {
			Criteria ebedRoomCriteria = null;
			String[] a = eBedRoomNums.split(",");
			// 数组的第一个元素如果是-1，表示自在整租居室数量不限制
			if (Integer.parseInt(a[0]) != -1) {
				for (String string : a) {
					if (ebedRoomCriteria == null) {
						ebedRoomCriteria = new Criteria(propertyName1).is(string);
					} else {
						ebedRoomCriteria = ebedRoomCriteria.or(new Criteria(propertyName1).is(string));
					}
				}
			}
			Criteria eCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			if (ebedRoomCriteria != null) {
				bedRoomCriteria = ebedRoomCriteria.connect().and(eCompanyCriteria);
			} else {
				bedRoomCriteria = eCompanyCriteria;
			}
		}

		Criteria rBedRoomCriteria = null;
		if (StringUtil.isNotEmpty(sBedRoomNums)) {
			Criteria sbedRoomCriteria = null;
			String[] a = sBedRoomNums.split(",");
			// 数组的第一个元素如果是-1，表示优选合租居室数量不限制
			if (Integer.parseInt(a[0]) != -1) {
				for (String string : a) {
					if (sbedRoomCriteria == null) {
						if (Integer.parseInt(string) > 3) {
							sbedRoomCriteria = new Criteria(propertyName1).greaterThanEqual(string);
						} else {
							sbedRoomCriteria = new Criteria(propertyName1).is(string);
						}
					} else {
						if (Integer.parseInt(string) > 3) {
							sbedRoomCriteria = sbedRoomCriteria.or(new Criteria(propertyName1).greaterThanEqual(string));
						} else {
							sbedRoomCriteria = sbedRoomCriteria.or(new Criteria(propertyName1).is(string));
						}
					}
				}
			}
			Criteria sCompanyCriteria = new Criteria(propertyName2).is(Constants.HouseDetail.RENT_TYPE_SHARE);
			if (sbedRoomCriteria != null) {
				rBedRoomCriteria = sbedRoomCriteria.connect().and(sCompanyCriteria);
			} else {
				rBedRoomCriteria = sCompanyCriteria;
			}
		}

		if (bedRoomCriteria != null && rBedRoomCriteria!=null) {
			query.addCriteria(bedRoomCriteria.connect().or(rBedRoomCriteria));
		}else if(bedRoomCriteria != null)
		{
			query.addCriteria(bedRoomCriteria);
		}else if(rBedRoomCriteria != null){
			query.addCriteria(rBedRoomCriteria);
		}
	}

	/**
	 * @Title: addHouseStyleCriteria
	 * @Description: 添加房型对应公寓名查询条件
	 * @param query
	 * @param tag
	 * @param propertyName
	 */
	public static void addCompanyCriteria(Query query, String eBedRoomNums, String sBedRoomNums, String propertyName) {
		Criteria companyCriteria = null;
		if (StringUtil.isNotEmpty(eBedRoomNums)) {
			companyCriteria = new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_ENTIRE);
		}
		if (StringUtil.isNotEmpty(sBedRoomNums)) {
			if (companyCriteria == null) {
				companyCriteria = new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_SHARE);
			} else {
				companyCriteria = companyCriteria.or(new Criteria("entireRent").is(Constants.HouseDetail.RENT_TYPE_SHARE));
			}
		}
		if (companyCriteria != null) {
			query.addCriteria(companyCriteria);
		}
	}
	

}
