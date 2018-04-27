package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.house.HouseRankScore;

public interface HouseRankScoreRepository extends JpaRepository<HouseRankScore, Long>{
	
	
	@Query("select a from HouseRankScore a where a.sellId=?1  and a.roomId=?2")
	HouseRankScore findBySellIdAndRoomId(String sellId, Long roomId);
}
