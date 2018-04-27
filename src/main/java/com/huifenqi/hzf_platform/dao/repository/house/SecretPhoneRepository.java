package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.phone.SecretPhone;

public interface SecretPhoneRepository extends JpaRepository<SecretPhone, Long> {
	
	@Query("select a from SecretPhone a where a.status = 1 and a.bindStatus = 0 order by a.updateTime")
	public List<SecretPhone> findUnbindSecretPhoneNo();
	
	public SecretPhone findFirst1ByStatusAndBindStatusOrderByUpdateTimeAsc(Integer status, Integer bindStatus);
	
	public List<SecretPhone> findBySecretPhoneNo(String  SecretPhoneNo);
	
	@Modifying 
	@Transactional
	@Query("update SecretPhone t set t.bindStatus = ?1, t.version = ?2, t.updateTime = now() where t.secretPhoneNo = ?3 and t.version = ?4")
	int updateBindStatusByVersion(int bindStatus, long version, String secretPhoneNo, long newVersion);
	
	@Modifying 
	@Transactional
	@Query("update SecretPhone t set t.bindStatus = ?1, t.updateTime = now() where t.id = ?2")
	int updateBindStatusById(int bindStatus, long id);
	
}
