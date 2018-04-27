/** 
 * Project Name: hzf_platform 
 * File Name: RoomBaseRepository.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository 
 * Date: 2016年4月26日下午2:46:48 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;

/**
 * ClassName: RoomBaseRepository date: 2016年4月26日 下午2:46:48 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface RoomBaseRepository extends JpaRepository<RoomBase, Long> {

	@Modifying
	@Query("update RoomBase t set t.isDelete = ?2 where t.sellId = ?1")
	public int setIsDeleteBySellId(String sellId, int isDelete);

	@Modifying
	@Query("update RoomBase t set t.isDelete = ?2 where t.id = ?1")
	public int setIsDeleteByRoomId(long roomId, int isDelete);

	@Query("select a from RoomBase a where a.id=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	RoomBase findByRoomId(long roomId);
	
	@Query("select a from RoomBase a where a.id=?1")
    RoomBase findALLDeleteByRoomId(long roomId);
	
	//查询程序删除之外的房源
	@Query("select a from RoomBase a where a.id=?1 and a.isDelete !=" + Constants.Common.STATE_IS_DELETE_YES)
	RoomBase findCanUpdateByRoomId(long roomId);

	@Query("select a from RoomBase a where a.sellId=?1 and a.id=?2 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	RoomBase findBySellIdAndRoomId(String sellId, long roomId);
	
	@Query("select a from RoomBase a where a.sellId=?1 and a.id=?2 ")
    RoomBase findALLDeleteBySellIdAndRoomId(String sellId, long roomId);

	@Query("select a from RoomBase a where a.sellId=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	List<RoomBase> findAllBySellId(String sellId);

	@Query("select a from RoomBase a where a.sellId=?1 and a.id!=?2 and " + "a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<RoomBase> findAllBySellIdAndRoomIdNot(String sellId, long roomId);

	@Modifying
	@Query("update RoomBase t set t.status = ?2 where t.id = ?1")
	int setStatusByRoomId(long roomId, int status);

	@Modifying
	@Query("update RoomBase t set t.approveStatus = ?2 where t.id = ?1")
	int setApproveStatusByRoomId(long roomId, int approveStatus);

	/**
	 * 房源审核
	 * @param sellId
	 * @param approveStatus
	 * @return
	 */
	@Modifying
	@Query("update RoomBase t set t.approveStatus = ?2 where t.sellId = ?1")
	int setApproveStatusBySellId(String sellId, int approveStatus);

	@Modifying
	@Query("update RoomBase t set t.status = ?2 where t.sellId = ?1")
	int setStatusBySellId(String sellId, int status);

	@Query("select count(*) from RoomBase a where a.id=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	int countByRoomId(long roomId);

	@Query("select count(*) from RoomBase a where a.sellId=?1 and a.id=?2 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	int countBySellIdAndRoomId(String sellId, long roomId);

	@Query("select a from RoomBase a where a.sellId=?1")
	RoomBase findBySellId(String sellId);

	@Modifying
	@Query("update RoomBase t set t.isTop = ?3 where t.id = ?2 and t.sellId = ?1")
	public int setIsTopByRoomIdAndSellId(String sellId, long roomId, int isTop);

	@Modifying
	@Query("update RoomBase t set t.pubType = ?3 where t.id = ?2 and t.sellId = ?1")
	public int setPubTypeByRoomIdAndSellId(String sellId, long roomId, int pubType);

	// 查询未被审核的数据
	String condition = Constants.PlatformStatus.ALLOW_BOTH + "," + Constants.PlatformStatus.DENY_SOLR;
	@Query("SELECT b FROM HouseDetail a, RoomBase b, PlatformCustomer c"
			+ " where a.sellId = b.sellId and a.source = c.source and c.permissionStatus in ("+condition+") and b.status = 1 and a.isRun = 1 and b.isDelete =0 and b.approveStatus in (0,10)")
	Page<RoomBase> findFirstUnapprove(Pageable pageable);
	
	
	/**
	 * 计算评分时调用
	 * 
	 * @param isDelete
	 * @return
	 */
	@Query("select a from RoomBase a where (a.updateTime >= ?1 and a.isDelete=0) or a.id in (select DISTINCT p.roomId from HousePicture p where p.updateTime >= ?1 and p.roomId <> 0)")
	Page<RoomBase> findByTimeRegion(Pageable pageable, Date startTime);
	
	/**
	 * 查询房源下房间的个数
	 * @param sellId
	 * @return
	 */
	@Query("select count(1) from RoomBase a where a.sellId=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	int sellCountBySellId(String sellId);
	
	/**
	 * 分页查询未被删除的房间
	 * @param pageable
	 * @return
	 */
	@Query("select a from RoomBase a where a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	Page<RoomBase> selectRoomNotDeleted(Pageable pageable);
}
