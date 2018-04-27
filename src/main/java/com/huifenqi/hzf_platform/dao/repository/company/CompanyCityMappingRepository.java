package com.huifenqi.hzf_platform.dao.repository.company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huifenqi.hzf_platform.context.entity.house.CompanyCityMapping;

/**
 * 2017年10月17日15:45:14
 * 公司和城市映射关系
 * @author jjs
 *
 */
public interface CompanyCityMappingRepository extends JpaRepository<CompanyCityMapping, Long>{
	
	/**
	 * 根据城市查询状态有效的映射
	 * @param cityId
	 * @param status
	 * @return
	 */
	List<CompanyCityMapping> findBycompanyNameAndStatus(String comName, Integer status);
}
