package com.huifenqi.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.huifenqi.activity.domain.ActivityLink;

/**
 * Created by YDM on 2017/10/12.
 */
public interface ActivityLinkRepository extends CrudRepository<ActivityLink, Long> {

	/**
	 * 获取4：优惠券链接
	 * @param 
	 * @return
	 */
	@Query("select al from ActivityLink al where al.state = 1")
	List<ActivityLink> getActivityLinkList();
	
	
}
