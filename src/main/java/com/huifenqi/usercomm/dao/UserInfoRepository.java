/** 
 * Project Name: usercomm_project 
 * File Name: UserInfoRepository.java 
 * Package Name: com.huifenqi.usercomm.dao 
 * Date: 2015年12月23日下午8:23:22 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.usercomm.dao;

import com.huifenqi.usercomm.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * ClassName: UserInfoRepository
 * date: 2015年12月23日 下午8:23:22
 * Description: 用户信息DAO层
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
	
	/**
	 * 根据用户ID查询
	 * @param userId
	 * @return
	 */
	public UserInfo findByUserId(long userId);

	public UserInfo findByUserIdAndUserMobile(long userId, String userMobile);


}
