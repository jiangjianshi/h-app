/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.IdealRentHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.ideal.IdealRentHouseSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.ideal.IdealRentRoomSolrRepository;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class IdealRentHouseDao {

	private static final Log logger = LogFactory.getLog(IdealRentHouseDao.class);

	@Autowired
	private HouseBaseRepository houseBaseRepository;

	@Autowired
	private RoomBaseRepository roomBaseRepository;

	
	@Autowired
	private IdealRentHouseSolrRepository idealRentHouseSolrRepository;
	
	@Autowired
	private IdealRentRoomSolrRepository idealRentRoomSolrRepository;

	/**
	 * 条件筛选 地图找房
	 * 
	 * @return
	 * @throws Exception 
	 */
	public HouseSearchResultDto getHouseResultDto(IdealRentHouseSearchDto idealRentHouseSearchDto) throws Exception {
		HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();
		int entireRent = idealRentHouseSearchDto.getEntireRent();
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			
			//理想生活圈查询-share
			idealRentHouseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			GroupResult<RoomSolrResult> roomFieldGroup = idealRentRoomSolrRepository.getAllByMultiCondition(idealRentHouseSearchDto);
			
			//理想生活圈查询-entire	
			idealRentHouseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			if(!StringUtil.isEmpty(idealRentHouseSearchDto.getcBedRoomNums())){//租房宝典,白领优选筛选合租主卧和整租一居室的房源
				idealRentHouseSearchDto.setKeyword(null);
			}
			GroupResult<HouseSolrResult> houseFieldGroup  = idealRentHouseSolrRepository.getAllByMultiCondition(idealRentHouseSearchDto);
			
			
			if (houseFieldGroup == null && roomFieldGroup == null) {
				logger.error(LogUtils.getCommLog("理想生活圈搜索房源结果为空"));
				return null;
			}
			
			//获取整租公司数据
			List<GroupEntry<HouseSolrResult>> houseGroupList = houseFieldGroup.getGroupEntries().getContent();
			List<HouseSolrResult> resulHousetList = new ArrayList<HouseSolrResult>();
			
			//遍历公司数据
			if(houseFieldGroup != null){
				boolean houseFalg = false;
				for(int index = 0; index <= Constants.IdealrRentConfig.DEFAULT_SIZE; index++){
					if(houseFalg){
						break;
					}
					//遍历solr结果
					for(GroupEntry<HouseSolrResult>  house : houseGroupList){ 
						HouseSolrResult hs = new HouseSolrResult();
						
						if(house.getResult().getContent().size()>index){//获取每个公司房源
							hs = house.getResult().getContent().get(index);
							if(hs != null){
								if(house.getResult().getContent().get(index).getImgs() != null && !house.getResult().getContent().get(index).getImgs().toString().equals("[]")){
									resulHousetList.add(hs);
								}
							}	
							if(resulHousetList.size() >= Constants.IdealrRentConfig.DEFAULT_SIZE ){
								houseFalg = true;
								break;
							}
						}	
					}
				}
			}
			//获取公司数据-share
			List<GroupEntry<RoomSolrResult>> roomGroupList = roomFieldGroup.getGroupEntries().getContent();
			List<RoomSolrResult> resulRoomtList = new ArrayList<RoomSolrResult>(30);
			if(roomGroupList != null){
				boolean roomFalg = false;
				for(int index = 0; index < Constants.IdealrRentConfig.DEFAULT_SIZE; index++){
					if(roomFalg){
						break;
					}
					//遍历solr结果
					for(GroupEntry<RoomSolrResult> room : roomGroupList ){
						RoomSolrResult rs = new RoomSolrResult();
						if(room.getResult().getContent().size()>index){
							rs = room.getResult().getContent().get(index);
							if(rs != null){
								
//								if(room.getResult().getContent().get(index).getSellId().equals("HF214286523488")){
//									System.out.println("===="+room.getResult().getContent().get(index).getImgs());
//									System.out.println("===="+room.getResult().getContent().get(index).getImgs().isEmpty());
//									System.out.println("===="+room.getResult().getContent().get(index).getImgs().toArray().length);
//									System.out.println("===="+room.getResult().getContent().get(index).getImgs().toString().equals("[]"));
//								}
								 
								if(room.getResult().getContent().get(index).getImgs() != null && !room.getResult().getContent().get(index).getImgs().toString().equals("[]")){
									resulRoomtList.add(rs);
								}
							}	
							
							if(resulRoomtList.size() >= Constants.IdealrRentConfig.DEFAULT_SIZE ){
								roomFalg = true;
								break;
							}
						}	
				}
				}
			}
			
			//合并整租分租结果集
			List<HouseSearchResultInfo> houseSearchResultInfoListAll = new ArrayList<HouseSearchResultInfo>();
			
			List<HouseSearchResultInfo> houseSearchResultInfoListRoom = HouseUtil
					.getHouseSearchResultInfoListByRoom(resulRoomtList);

			List<HouseSearchResultInfo> houseSearchResultInfoListHouse = HouseUtil
					.getHouseSearchResultInfoListByHouse(resulHousetList);

			if (houseSearchResultInfoListHouse != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListHouse);
			}
			if (houseSearchResultInfoListRoom != null) {
				houseSearchResultInfoListAll.addAll(houseSearchResultInfoListRoom);
			}
			
			if (CollectionUtils.isNotEmpty(houseSearchResultInfoListAll)) {
				houseSearchResultDto.setSearchHouses(houseSearchResultInfoListAll);
			}
		}
		return houseSearchResultDto;		
	}
	
	public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

	public static boolean isEmpty(Object[] array) {
	    return (array == null || array.length == 0);
	}

	/**
	 * 查询房源是否存在
	 * 
	 * @param sellId
	 * @return
	 */
	public boolean isHouseExist(String sellId) {

		int count = houseBaseRepository.countBySellId(sellId);

		return count > 0;
	}

	/**
	 * 查询房间是否存在
	 * 
	 * @param roomId
	 * @return
	 */
	public boolean isRoomExist(long roomId) {

		int count = roomBaseRepository.countByRoomId(roomId);

		return count > 0;
	}
	
}
