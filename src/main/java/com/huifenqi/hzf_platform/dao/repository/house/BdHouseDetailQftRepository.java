/** 
* Project Name: hzf_platform 
* File Name: HouseDetailRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:31:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;

/**
 * ClassName: HouseDetailRepository date: 2016年4月26日 下午2:31:30 Description:
 * 
 * @author changmingwei，arison
 * @version
 * @since JDK 1.8
 */
public interface BdHouseDetailQftRepository extends JpaRepository<BdHouseDetailQft, Long> {

	@Query("select a from BdHouseDetailQft a" + " where a.outHouseId=?1 and a.appId=?2 and a.state="
			+ Constants.BdHouseDetailQft.HOUSE_STATE_UP)
	BdHouseDetailQft findBySellIdandAppIdandState(String sellId, String appId);

	@Query("select a from BdHouseDetailQft a" + " where a.outHouseId=?1 and a.appId=?2")
	BdHouseDetailQft findBySellIdandAppId(String sellId, String appId);

	@Modifying
	@Query("update BdHouseDetailQft a set a.memo=?3, a.state=?4,a.transferFlag=?5,a.updateTime=now() where a.outHouseId=?1 and a.appId=?2 ")
	void setStatusAndMemo(String outHouseId, String appId, String memo, String status, int transferFlag);

	// @Query("select a from BdHouseDetailQft a where a.transferFlag = 0 limit
	// 1000")
	// List<BdHouseDetailQft> findByTimeRegion();

	List<BdHouseDetailQft> findFirst1000ByTransferFlagOrderByUpdateTimeDesc(int transferFlag);

	@Modifying
	@Query("update BdHouseDetailQft a set a.houseSellId=?3, a.transferFlag=1 where a.outHouseId=?1 and a.appId=?2 ")
	void updateHouseSellId(String outHouseId, String appId, String houseSellId);

	@Modifying
	@Transactional
	@Query("update BdHouseDetailQft a set a.transferFlag=?3 where a.outHouseId=?1 and a.appId=?2 ")
	void updateHouseTrandferFlag(String outHouseId, String appId, int transferFlag);

	@Modifying
	@Query("update BdHouseDetailQft a set a.transferFlag=?3,a.lng=?4, a.lat=?5, a.precise=?6,a.confidence=?7,a.level=?8 where a.outHouseId=?1 and a.appId=?2 ")
	void updateHouse(String outHouseId, String appId, int transferFlag, String lng, String lat, int precise,
			int confidence, String level);

}
