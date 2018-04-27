/** 
* Project Name: hzf_platform 
* File Name: OrderCustomRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2017年8月18日 上午10:15:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.house.OrderCustom;

/**
 * ClassName: OrderCustomRepository date: 2017年8月18日 上午10:15:30 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
public interface OrderCustomRepository extends JpaRepository<OrderCustom, Long> {
	
	@Modifying
	@Query("update OrderCustom oc set oc.location = ?2, oc.minPrice = ?3, oc.maxPrice = ?4, oc.checkInTime = ?5, oc.phone = ?6, oc.updateTime = CURRENT_TIME() where oc.userId = ?1 and oc.state = 1")
	public int updateOrderCustom(long userId, String location, long minPrice, long maxPrice, String checkInTime, String phone);
	
	@Query("select oc from OrderCustom oc where oc.userId = ?1 and oc.state = 1")
	OrderCustom getOrderCustomByUserId(long userId);

}
