/** 
 * Project Name: hzf_platform 
 * File Name: HouseSubDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2017年8月8日 上午11:30:31 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseCollection;
import com.huifenqi.hzf_platform.context.entity.house.SlideShowUrl;
import com.huifenqi.hzf_platform.dao.repository.house.*;
import com.huifenqi.hzf_platform.dao.repository.platform.SlideShowUrlRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.entity.house.FootmarkHistory;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.QueryDetailVo;

/**
 * ClassName: HouseSubDao date: 2017年8月8日 上午11:30:31 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Repository
public class HouseSubDao {

	private static final Log logger = LogFactory.getLog(HouseSubDao.class);

	@Autowired
	private HouseDetailRepository houseDetailRepository;

	@Autowired
	private HouseCollectionRepository houseCollectionRepository;

	@Autowired
	private FootmarkHistoryRepository footmarkHistoryRepository;
	
	@Autowired
	private SlideShowUrlRepository slideShowUrlRepository;


	/**
	 * @Title: getBizNameList
	 * @Description: 获取城市房源最多的商圈名TOP12 组成的字符串
	 * @return String
	 * @author 叶东明
	 * @dateTime 2017年8月8日 下午2:07:11
	 */
	public String getBizNameList(long cityId) throws Exception {
		String bizNameString = "";
		PageRequest pageRequest = new PageRequest(0, 12);
		Page<QueryDetailVo> page = houseDetailRepository.getBizNameList(cityId, pageRequest);
		List<QueryDetailVo> bizNameList = page.getContent();
		if (bizNameList != null) {
			for (QueryDetailVo map : bizNameList) {
				String bizName = map.getBizName();
				if (StringUtil.isNotBlank(bizName)) {
					if (bizNameString.length() == 0) {
						bizNameString = bizName;
					} else {
						bizNameString = bizNameString + ";" + bizName;
					}
				}
			}
		}
		return bizNameString;
	}
	
	/**
	 * @Title: getFootmarkHistory
	 * @Description: 通过用户ID获取浏览房源足迹数据
	 * @return List<FootmarkHistory>
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午3:20:11
	 */
	public List<FootmarkHistory> getFootmarkHistory(long userId, String sellId, int roomId) throws Exception {
		return footmarkHistoryRepository.getFootmarkHistory(userId, sellId, roomId);
	}

	/**
	 * @Title: saveFootmarkHistory
	 * @Description: 保存房源浏览足迹数据
	 * @return int
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午3:20:38
	 */
	public long saveFootmarkHistory(FootmarkHistory footmarkHistory) throws Exception {
		footmarkHistory.setUpdateTime(new Date());
		footmarkHistory.setCreateTime(new Date());
		FootmarkHistory footmarkEntity = footmarkHistoryRepository.save(footmarkHistory);
		return footmarkEntity.getId();
	}
	
	/**
	 * @Title: updateFootmarkHistory
	 * @Description: 更新房源浏览足迹数据
	 * @return int
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午3:20:56
	 */
	public int updateFootmarkHistory(long footmarkHistoryId, long userId, String sellId, int roomId) throws Exception {
		return footmarkHistoryRepository.updateFootmarkHistory(footmarkHistoryId, userId, sellId, roomId);
	}
	
	/**
	 * @Title: deleteFootmarkHistory
	 * @Description: 删除房源浏览足迹数据
	 * @return void
	 * @author 叶东明
	 * @dateTime 2017年8月22日 下午5:49:00
	 */
	public void deleteFootmarkHistory(long userId, String sellId, int roomId) throws Exception {
		footmarkHistoryRepository.deleteFootmarkHistory(userId, sellId, roomId);
	}
	
	/**
	 * @Title: findHouseListByUserId
	 * @Description: 通过userId获取浏览房源足迹列表
	 * @return List<FootmarkHistory>
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午11:02:40
	 */
	public List<FootmarkHistory> findHouseListByUserId(long userId) throws Exception {
		return footmarkHistoryRepository.getHouseListByUserId(userId);
	}
	
	/**
	 * @Title: getCountByUserId
	 * @Description: 通过用户ID获取当前用户下房源浏览足迹总条数
	 * @return List
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午6:57:34
	 */
	public List<FootmarkHistory> getCountByUserId(long userId) throws Exception {
		return footmarkHistoryRepository.getCountByUserId(userId);
	}


	/**
	 * @Title: findHouseListByUserId
	 * @Description: 通过userId获取浏览房源足迹列表
	 * @return List<FootmarkHistory>
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午11:02:40
	 */
	public Long countFootmarkHistoryByUserId(long userId) {
		return footmarkHistoryRepository.countByUserId(userId);
	}

//----------------------------收藏房源------------------------------------
	/**
	 * @Title: saveHouseCollection
	 * @Description: 收藏房源
	 * @return int
	 * @author arison
	 * @dateTime 2017年8月9日 下午3:20:38
	 */
	public long saveHouseCollection(HouseCollection houseCollection) throws Exception {
		houseCollection.setCreateTime(new Date());
		houseCollection.setUpdateTime(new Date());
		houseCollection.setState(1);
		HouseCollection savedHouseCollection = houseCollectionRepository.save(houseCollection);
		return savedHouseCollection.getId();
	}

	/**
	 * @Title: updateHouseCollection
	 * @Description: 更新收藏房源数据
	 * @return int
	 * @author arison
	 * @dateTime 2017年8月16日 下午3:20:56
	 */
	public int updateHouseCollection(HouseCollection houseCollection) throws Exception {
		long userId=houseCollection.getUserId();
		String sellId=houseCollection.getSellId();
		int roomId=houseCollection.getRoomId();
		int state=houseCollection.getState();
		return houseCollectionRepository.updateHouseCollectionListByUserId(userId, sellId, roomId,state);
	}

	/**
	 * @Title: getCountByUserId
	 * @Description: 通过用户ID获取当前用户收藏房源数据
	 * @return List
	 * @author arison
	 * @dateTime 2017年8月16日 下午6:57:34
	 */
	public List<HouseCollection> findHouseCollectionListByUserId(long userId) throws Exception {
		return houseCollectionRepository.findHouseCollectionListByUserId(userId);
	}

	/**
	 * @Title: getCountByUserId
	 * @Description: 通过用户ID获取当前用户收藏房源数据
	 * @return List
	 * @author arison
	 * @dateTime 2017年8月16日 下午6:57:34
	 */
	public HouseCollection findHouseCollectionItem(long userId, String sellId, int roomId) throws Exception {
		return houseCollectionRepository.findHouseCollectionItem(userId,sellId,roomId);
	}


	/**
	 * @Title: getCountByUserId
	 * @Description: 通过用户ID获取当前用户收藏房源数据
	 * @return List
	 * @author arison
	 * @dateTime 2017年8月16日 下午6:57:34
	 */
	public HouseCollection saveHouseCollectionItem(HouseCollection houseCollection) throws Exception {
		return houseCollectionRepository.save(houseCollection);
	}
	
	/**
	 * @Title: deleteHouseCollectionItem
	 * @Description: 删除获取当前用户收藏房源数据
	 * @return void
	 * @author arison
	 * @dateTime 2017年8月16日 下午6:57:34
	 */
	public void deleteHouseCollectionItem(long userId, String sellId, int roomId) throws Exception {
	    houseCollectionRepository.deleteHouseCollectionListByUserId(userId,sellId,roomId);
	}
	
	/**
	 * @Title: getSlideShowUrlList
	 * @Description: 查询数据库中轮播图配置
	 * @return List<SlideShowUrl>
	 * @author 叶东明
	 * @dateTime 2017年9月1日 上午11:15:28
	 */
	public List<SlideShowUrl> getSlideShowUrlList() throws Exception {
		return slideShowUrlRepository.getSlideShowUrlList();
	}
	
	/**
	 * @Title: getCityIdListByAgengcyId
	 * @Description: 通过中介公司ID去房源表中统计包含的城市
	 * @return List<QueryDetailVo>
	 * @author 叶东明
	 * @dateTime 2017年9月14日 下午4:11:16
	 */
	public List<QueryDetailVo> getCityIdListByAgengcyId(String agengcyId) throws Exception {
		return houseDetailRepository.getCityIdListByAgengcyId(agengcyId);
	}

	/**
	 * @Title: deleteHouseCollectionItem
	 * @Description: 删除获取当前用户收藏房源数据
	 * @return void
	 * @author arison
	 * @dateTime 2017年8月16日 下午6:57:34
	 */
	public Long countHouseCollectionItemByUserId(long userId){
		return houseCollectionRepository.countByUserId(userId);
	}

}
