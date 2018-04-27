/** 
 * Project Name: hzf_platform 
 * File Name: ComplaintRepository.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository 
 * Date: 2016年4月26日下午2:37:25 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.Complaint;

/**
 * ClassName: ComplaintRepository date: 2016年4月26日 下午2:37:25 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	@Query("select a from Complaint a" + " where a.uid=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	List<Complaint> findAllByUid(String uid);

	@Query("select a from Complaint a" + " where a.uid=?1  and a.sellId=?2 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<Complaint> findAllByUidAndSellId(String uid, String sellId);

	@Query("select a from Complaint a" + " where a.uid=?1  and a.sellId=?2 and a.roomId=?3 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<Complaint> findAllByUidAndSellIdAndRoomId(String uid, String sellId, long roomId);

	@Query("select count(a) from Complaint a" + " where a.uid=?1  and a.sellId=?2 and a.roomId=?3 and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	int getNumByUidAndSellIdAndRoomId(String uid, String sellId, long roomId);

}
