package com.huifenqi.hzf_platform.dao.repository.house;

import com.huifenqi.hzf_platform.context.entity.house.HouseCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HouseCollectionRepository extends JpaRepository<HouseCollection, Long> {
	

    @Query("select h from HouseCollection h where h.userId=?1 and h.state = 1 and h.isRent = 0 order by h.updateTime desc")
    List<HouseCollection> findHouseCollectionListByUserId(Long userId);
   
    @Query("select h from HouseCollection h where h.userId = ?1 and h.state = 1 and h.isRent = 0")
    List<HouseCollection> getHouseCollectionListByUserId(long userId);

    @Query("select h from HouseCollection h  where  h.userId = ?1  and h.sellId =?2 and h.roomId = ?3 and h.state = 1 and h.isRent = 0")
    public  HouseCollection findHouseCollectionItem(long userId, String sellId, int roomId);

    @Transactional
    @Modifying
    @Query("update HouseCollection h set h.state=?4, h.updateTime = CURRENT_TIME() where h.userId = ?1 and h.sellId =?2 and h.roomId = ?3 and h.state = 1 and h.isRent = 0")
    public int updateHouseCollectionListByUserId(long userId, String sellId, int roomId,int state);

    @Transactional
    @Modifying
    @Query("delete HouseCollection h where h.userId = ?1 and h.sellId =?2 and h.roomId = ?3 and h.state = 1 and h.isRent = 0")
    public void deleteHouseCollectionListByUserId(long userId, String sellId, int roomId);
    
    @Transactional
    @Modifying
    @Query("delete HouseCollection h where h.sellId =?1 and h.roomId = ?2 and h.state = 1 and h.isRent = 0")
    public void deleteHouseCollectionList(String sellId, int roomId);

    Long countByUserId(long userId);
    
    /* 这部分接口暂时不用 */
    @Transactional
    @Modifying
    @Query("update HouseCollection h set h.isRent=?3, h.updateTime = CURRENT_TIME() where h.sellId =?1 and h.roomId = ?2 and h.state = 1 and h.isRent = 0")
    public void updateHouseCollectionListByIsRent(String sellId, int roomId, int isRent);
    
    @Transactional
    @Modifying
    @Query("update HouseCollection h set h.state=?3, h.updateTime = CURRENT_TIME() where h.sellId =?1 and h.roomId = ?2 and h.state = 1 and h.isRent = 0")
    public void updateHouseCollectionListByState(String sellId, int roomId, int state);
    
    @Query("select h from HouseCollection h where h.roomId = 0 and h.state = 1 and h.isRent = 0")
    List<HouseCollection> getEhouseCollectionList();
    
    @Query("select h from HouseCollection h where h.roomId > 0 and h.state = 1 and h.isRent = 0")
    List<HouseCollection> getShouseCollectionList();
    /* 这部分接口暂时不用 */
    
    
}
