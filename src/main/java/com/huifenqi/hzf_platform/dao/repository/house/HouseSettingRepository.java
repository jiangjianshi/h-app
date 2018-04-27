/** 
* Project Name: hzf_platform 
* File Name: HouseSetting.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:33:27 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;

/**
 * ClassName: HouseSettingRepository date: 2016年4月26日 下午2:33:27 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HouseSettingRepository extends JpaRepository<HouseSetting, Long> {

	@Modifying
	@Query("update HouseSetting t set t.isDelete = ?2 where t.sellId = ?1")
	int setIsDeleteBySellId(String sellId, int isDelete);

	@Modifying
	@Query("update HouseSetting t set t.isDelete = ?2 where t.roomId = ?1")
	int setIsDeleteByRoomId(long roomId, int isDelete);
	
	@Modifying
	@Query("update HouseSetting t set t.isDelete = ?2 where t.id in ?1")
	int setIsDeleteByIds(Collection<Long> ids, int isDelete);

	@Query("select a from HouseSetting a" + " where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	List<HouseSetting> findAllBySellId(String sellId);

	@Query("select a from HouseSetting a" + " where a.sellId=?1  and a.roomId=?2 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<HouseSetting> findAllBySellIdAndRoomId(String sellId, long roomId);
	
	@Query("select a from HouseSetting a" + " where a.sellId=?1 and a.categoryType=?2 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<HouseSetting> findAllBySellIdAndCategoryType(String sellId, int categoryType);

}
