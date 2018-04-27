/** 
* Project Name: hzf_platform_project 
* File Name: HouseTokenRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository.house 
* Date: 2016年6月21日下午5:38:37 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseToken;

/**
 * ClassName: HouseTokenRepository date: 2016年6月21日 下午5:38:37 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HouseTokenRepository extends JpaRepository<HouseToken, Long> {

	@Modifying
	@Query("update HouseToken t set t.isDelete = ?3 where t.sellId = ?1 and t.roomId = ?2")
	public int setIsDeleteBySellIdAndRoomId(String sellId, long roomId, int isDelete);

	@Query("select t.token from HouseToken t where t.sellId=?1 and t.roomId=?2 and t.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	String findTokenBySellIdAndRoomId(String sellId, long roomId);

}
