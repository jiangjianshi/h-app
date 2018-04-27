package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.ScheduleConstants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDicitem;

public interface HouseDicitemRepository extends JpaRepository<HouseDicitem, Long> {
	
	/**
	 * 根据房间类型名称获取编码
	 * 
	 * @param houseTypeDesc
	 * @return
	 */
	@Query("select a from HouseDicitem a where a.dicName=?1 and a.isDelete=" + ScheduleConstants.QftUtil.IS_DELEE_NO)
	HouseDicitem findHouseDicitemByDicName(String dicName);
	
}
