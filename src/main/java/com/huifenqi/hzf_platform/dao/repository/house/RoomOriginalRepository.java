/** 
 * Project Name: hzf_platform 
 * File Name: RoomBaseRepository.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository 
 * Date: 2016年4月26日下午2:46:48 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.RoomOriginal;

/**
 * 
 * @author jjs
 *
 */
public interface RoomOriginalRepository extends JpaRepository<RoomOriginal, Long> {
	
	@Query("select a from RoomOriginal a" + " where a.roomId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	RoomOriginal findByRoomId(Long roomId);
	
	@Modifying
	@Query("update RoomOriginal t set t.isDelete = ?2 where t.roomId = ?1")
	public int setIsDeleteByRoomId(Long roomId, int isDelete);

	@Modifying 
	@Query("update RoomOriginal t set t.status = ?2 where t.roomId = ?1")
	int setStatusByRoomId(Long roomId, int status);
}
