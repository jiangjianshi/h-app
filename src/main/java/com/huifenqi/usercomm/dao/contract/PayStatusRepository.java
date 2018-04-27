/** 
 * Project Name: usercomm_project 
 * File Name: PayStatusRepository.java 
 * Package Name: com.huifenqi.usercomm.dao 
 * Date: 2015年12月9日下午2:56:31 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.usercomm.dao.contract;


import com.huifenqi.usercomm.domain.contract.PayStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/** 
 * ClassName: PayStatusRepository
 * date: 2015年12月9日 下午2:56:31
 * Description: 订单接口的查询接口
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public interface PayStatusRepository extends CrudRepository<PayStatus, Long> {
	
	@Query("select p from PayStatus p where p.uid = ?1 and (p.contractNo = ?2 or p.contractNo = -1) order by p.msgTime desc")
	public List<PayStatus> findPayStatusBy(long uid, String contractNo);
}
