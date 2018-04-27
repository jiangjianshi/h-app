/** 
 * Project Name: usercomm_smart 
 * File Name: TradeOrderRepository.java 
 * Package Name: com.huifenqi.usercomm.dao.huizhaofang 
 * Date: 2017年3月2日下午5:31:53 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.usercomm.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.usercomm.domain.ThreePartUser;

import java.util.Date;
import java.util.List;

/**
 * ClassName: TradeOrderRepository date: 2017年5月11日 下午5:31:53 Description:
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
public interface ThreePartUserRepository extends CrudRepository<ThreePartUser, Long> {

	ThreePartUser findByUserId(long userId);

	@Query("select t  from ThreePartUser t where t.qqId = ?1 and t.qqState=1 ")
	ThreePartUser findByQqId( String  qqId);

	@Query("select t from ThreePartUser t where t.wxId = ?1 and t.wxState=1 ")
	ThreePartUser findByWxId(String wxId);

	@Query("select t from  ThreePartUser t where t.wbId = ?1 and t.wbState=1 ")
	ThreePartUser findByWbId(String wbId);

	@Modifying
	@Transactional
	@Query("update from ThreePartUser t set t.qqState = ?3 ,t.qqNickName='',t.qqId='', t.updateTime=?2 where t.id = ?1 ")
	int updateQqStateByUserId(long id,Date date,int state);

	@Modifying
	@Transactional
	@Query("update from ThreePartUser t set t.wxState =?3 ,t.wxNickName='', t.wxId='' , t.updateTime=?2  where t.id = ?1 ")
	int updateWxStateByUserId(long id,Date date,int state);

	@Modifying
	@Transactional
	@Query("update from ThreePartUser t set t.wbState = ?3 ,t.wbNickName='',t.wbId='', t.updateTime=?2  where t.id = ?1 ")
	int updateWbStateByUserId(long id,Date date,int state);


	@Modifying
	@Transactional
	@Query("update from ThreePartUser t set t.qqState = 0,t.qqNickName='', t.wxState = 0 , t.wxNickName='',t.wbState = 0,t.wbNickName='', t.updateTime=?2  where t.id = ?1 ")
	int invalidUserById(long id,Date date);



}
