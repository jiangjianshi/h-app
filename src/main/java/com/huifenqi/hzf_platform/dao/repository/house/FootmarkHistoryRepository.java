/** 
* Project Name: hzf_platform 
* File Name: FootmarkHistoryRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2017年8月9日 上午12:01:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.entity.house.FootmarkHistory;

/**
 * ClassName: FootmarkHistoryRepository date: 2017年8月9日 上午12:01:30 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
public interface FootmarkHistoryRepository extends JpaRepository<FootmarkHistory, Long> {
	
	@Query("select fh from FootmarkHistory fh where fh.userId = ?1 and fh.sellId = ?2 and fh.roomId = ?3 and fh.state = 1 and fh.isRent = 0")
	List<FootmarkHistory> getFootmarkHistory(long userId, String sellId, int roomId);

	@Transactional
	@Modifying
	@Query("update FootmarkHistory fh set fh.sellId = ?3, fh.roomId = ?4, fh.updateTime = CURRENT_TIME() where fh.id = ?1 and fh.userId = ?2 and fh.state = 1 and fh.isRent = 0")
	public int updateFootmarkHistory(long footmarkHistoryId, long userId, String sellId, int roomId);
	
	@Transactional
	@Modifying
	@Query("delete FootmarkHistory fh where fh.userId = ?1 and fh.sellId = ?2 and fh.roomId = ?3 and fh.state = 1 and fh.isRent = 0")
	public void deleteFootmarkHistory(long userId, String sellId, int roomId);
	
	@Transactional
	@Modifying
	@Query("delete FootmarkHistory fh where fh.sellId = ?1 and fh.roomId = ?2 and fh.state = 1 and fh.isRent = 0")
	public void deleteFootmarkHistoryList(String sellId, int roomId);
	
	@Query("select fh from FootmarkHistory fh where fh.userId = ?1 and fh.state = 1 and fh.isRent = 0 order by updateTime")
	List<FootmarkHistory> getCountByUserId(long userId);
	
	@Query("select fh from FootmarkHistory fh where fh.userId = ?1 and fh.state = 1 and fh.isRent = 0 order by fh.updateTime desc")
	List<FootmarkHistory> getHouseListByUserId(long userId);

	//@Query("select count(fh) from FootmarkHistory fh where fh.userId = ?1 and fh.state = 1")
	Long countByUserId(long userId);

	/* 这部分接口暂时不用 */
	@Transactional
	@Modifying
	@Query("update FootmarkHistory fh set fh.isRent=?3, fh.updateTime = CURRENT_TIME() where fh.sellId =?1 and fh.roomId = ?2 and fh.state = 1 and fh.isRent = 0")
	public void updateFootmarkHistoryListByIsRent(String sellId, int roomId, int isRent);
	
	@Transactional
	@Modifying
	@Query("update FootmarkHistory fh set fh.state=?3, fh.updateTime = CURRENT_TIME() where fh.sellId =?1 and fh.roomId = ?2 and fh.state = 1 and fh.isRent = 0")
	public void updateFootmarkHistoryListByState(String sellId, int roomId, int state);
	
	@Query("select fh from FootmarkHistory fh where fh.roomId = 0 and fh.state = 1 and fh.isRent = 0")
	List<FootmarkHistory> getEfootmarkHistoryList();
	
	@Query("select fh from FootmarkHistory fh where fh.roomId > 0 and fh.state = 1 and fh.isRent = 0")
	List<FootmarkHistory> getSfootmarkHistoryList();
	/* 这部分接口暂时不用 */

}
