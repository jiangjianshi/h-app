/** 
 * Project Name: usercomm_project 
 * File Name: BankCardRepository.java 
 * Package Name: com.huifenqi.usercomm.dao 
 * Date: 2016年2月16日下午6:12:57 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.usercomm.charge.dao;

import com.huifenqi.usercomm.charge.domain.BankCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * ClassName: BankCardRepository date: 2016年2月16日 下午6:12:57 Description: 银行卡DAO层
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public interface BankCardRepository extends CrudRepository<BankCard, Long> {

	@Query("select bc from BankCard as bc where bc.bindStatus=1 and bc.idCardNo=?1 and bc.bankCardNo not like '%*%' and bc.bankCardNo != ''")
	public List<BankCard> findBindedCardByIdCardNo(String idCardNo);

	@Query("select bc from BankCard as bc where bc.bindStatus=1 and bc.bankCardNo=?1")
	public BankCard findBindedCardByBankCardNo(String bankCardNo);
}
