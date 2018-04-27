/** 
* Project Name: hzf_platform 
* File Name: SlideShowUrlRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2017年8月31日 上午12:01:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.house.SlideShowUrl;

/**
 * ClassName: SlideShowUrlRepository date: 2017年8月31日 上午12:01:30 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
public interface SlideShowUrlRepository extends JpaRepository<SlideShowUrl, Long> {
	
//	@Query("select ssu from SlideShowUrl ssu where ssu.state = 1 and (ssu.imageType = 1 or ssu.imageType = 2)")
	@Query("select ssu from SlideShowUrl ssu where ssu.state = 1")
	List<SlideShowUrl> getSlideShowUrlList();
	
}
