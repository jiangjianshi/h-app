/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.entity.house.CompanyPayMonth;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: CompanyDao date: 2017年8月31日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class CompanyDao {

	private static final Log logger = LogFactory.getLog(CompanyDao.class);
	
	
	@Autowired(required = true)
	private CompanyPayMonthRepository companyPayMonthRepository;
	
	/**
	 * 查询月付公司
	 *
	 * @param companyIdHzf
	 * @return CompanyPayMonth
	 */
	public CompanyPayMonth findByCompanyIdHzf(String companyIdHzf) {
		CompanyPayMonth companyPayMonth = null;
		try{
			companyPayMonth = companyPayMonthRepository.findCompanyPayMonthBycompanyIdHzf(companyIdHzf);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("查询月付公司" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "查询月付公司");
		}
		return companyPayMonth;
	}
	

}
