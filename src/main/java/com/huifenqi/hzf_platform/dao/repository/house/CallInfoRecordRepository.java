package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.phone.CallInfoRecord;

public interface CallInfoRecordRepository extends JpaRepository<CallInfoRecord, Long>{
	
	
	@Query("select a from CallInfoRecord a where a.callDuration > 0 and a.voiceRecordUrl = '' and a.callId != '' and a.createTime < ?1")
	Page<CallInfoRecord> findValidCallInfoRecord(Pageable pageable , Date startTime);
	
}
