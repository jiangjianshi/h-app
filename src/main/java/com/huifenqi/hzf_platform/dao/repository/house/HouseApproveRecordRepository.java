package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.house.HouseApproveRecord;

public interface HouseApproveRecordRepository extends JpaRepository<HouseApproveRecord, Long>{
	
	@Query("select a from HouseApproveRecord a  where a.sellId=?1 and a.roomId=?2 and a.status=1 and a.operator !='sys'")
	HouseApproveRecord findBySellIdAndRoomId(String sellId, Long roomId);
}
