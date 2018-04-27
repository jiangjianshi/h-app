package com.huifenqi.usercomm.dao;

import java.util.List;

import com.huifenqi.usercomm.domain.DoorLock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: DoorLockRepository date: 2017年9月9日 下午4:31:53 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
public interface DoorLockRepository extends CrudRepository<DoorLock, Long> {

	@Query("select dl from DoorLock dl where dl.userId = ?1 and dl.state = 1 ")
	DoorLock getDoorLockByUserId(long userId);

	@Modifying
    @Transactional
	@Query("update DoorLock dl set dl.gesturePassword = ?2, dl.phone = ?3, dl.touchId = ?4, dl.updateTime=CURRENT_TIME() where dl.userId = ?1 and dl.state = 1 ")
	int updateDoorLock(long userId, String gesturePwd, String phone, int touchId);
	
	@Query("select dl from DoorLock dl where dl.phone = ?1 and dl.state = 1 ")
	DoorLock getDoorLockByPhone(String phone);
	
	@Query("select dl from DoorLock dl where dl.phone = ?1 and dl.state = 1 ")
	List<DoorLock> getDoorLockListByPhone(String phone);
	
	@Modifying
	@Transactional
	@Query("update DoorLock dl set dl.state = 0 where dl.phone = ?1 and dl.state = 1 ")
	int updateDoorLockByPhone(String phone);

}
