/** 
* Project Name: hzf_platform 
* File Name: DistrictRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:51:02 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.CompanyConstants;
import com.huifenqi.hzf_platform.context.entity.house.CompanyPayMonth;

/**
 * ClassName: CompanyPayMonthRepository date: 2017年8月31日 下午2:51:02 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface CompanyPayMonthRepository extends JpaRepository<CompanyPayMonth, Long> {

	/**
	 * 查询指定的公司名称
	 * 
	 * @param run
	 * @return
	 */
	@Query("select c from CompanyPayMonth c where c.run=" + CompanyConstants.CompanyHouseDetail.IS_RUN_NO
			+ " and c.isDelete=" + CompanyConstants.CompanyHouseDetail.IS_DELETE_NO)
	public List<CompanyPayMonth> findCompanys();
	


	/**
	 * 查询指定的公司名称
	 * 
	 * @param companyIdHzf
	 * @return CompanyPayMonth
	 */
	@Query("select c from CompanyPayMonth c where c.companyIdHzf= ?1" 
			+ " and c.isDelete=" + CompanyConstants.CompanyHouseDetail.IS_DELETE_NO)
	public CompanyPayMonth findCompanyPayMonthBycompanyIdHzf(String companyIdHzf);

	/**
	 * 查询指定的公司名称
	 * 
	 * @param companyIdHzf
	 * @return CompanyPayMonth
	 */
	@Modifying
	@Query("update CompanyPayMonth t set t.run = ?2 where t.companyIdHzf = ?1")
	int setIsRunByCompanyIdHzf(String companyIdHzf, int isRun);
}
