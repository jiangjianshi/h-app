package com.huifenqi.hzf_platform.schedule;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseDetailAszToLocalDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetailAsz;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailAszRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.BeanMapperUtil;

@Service
@Transactional
public class AszDataTransferService {

	@Resource
	private HouseDetailAszRepository aszHouseDetailRepository;
	@Resource
	private RoomBaseRepository roomBaseRepository;
	@Resource
	private PlatformCustomerRepository platformCustomerRepository;
	@Resource
	private HouseDao houseDao;

	private static Logger logger = LoggerFactory.getLogger(AszDataTransferService.class);

	/**
	 * 先发布房源，再发布房间
	 * 
	 * @param hd
	 */
	public void pulbicHouseAndRoom(HouseDetailAsz asz) {

		publishHouse(asz);
		publishRoom(asz);
	}

	/**
	 * 整租处理
	 * 
	 * @throws Exception
	 */
	public void publishHouse(HouseDetailAsz asz) {
		try {
			// 复制对象
			HouseDetailAszToLocalDto transferDto = new HouseDetailAszToLocalDto();
			BeanMapperUtil.copy(asz, transferDto);
			
			//构建房源dto
			HousePublishDto houseDto = aszBuildHouseDto(transferDto);
			logger.info("第三方的房源编号:{}, 房源状态：{}", transferDto.getApartmentCode(), houseDto.getStatus());
			if (StringUtils.isEmpty(asz.getHouseSellId())) {//发布房源
				
				//发布房源
				String returnSellId = houseDao.addHousePublishDto(houseDto);
				asz.setHouseSellId(returnSellId);
				logger.info("房源发布成功返回房源id：{}", returnSellId);
				
				//更新房源ID到原始数据
				aszHouseDetailRepository.updateHouseSellId(asz.getApartmentCode(), returnSellId);
			} else {//更新房源
				
				houseDao.updateHousePublishDto(houseDto, asz.getHouseSellId());
				
				//标记房源已同步
				aszHouseDetailRepository.updateHouseTrandferFlag(asz.getApartmentCode(),AszConstants.HouseDetail.TRANSFER_FLAG_YES); 
			}
		} catch (Exception e) {
			logger.error("爱上租发布房源失败， outHouseId={}", asz.getApartmentCode(), e);
			throw new RuntimeException("爱上租发布房源失败");
		}

	}

	/**
	 * 出租单间
	 * 
	 * @throws Exception
	 */
	public void publishRoom(HouseDetailAsz asz) {

		try {
			//查看房间信息
			RoomBase rb = roomBaseRepository.findBySellId(asz.getHouseSellId());
			
			// 复制对象
			HouseDetailAszToLocalDto transferDto = new HouseDetailAszToLocalDto();
			BeanMapperUtil.copy(asz, transferDto);
			
			RoomPublishDto roomDto = aszBuildRoomDto(transferDto);
			if (rb == null) {//发布房间
				houseDao.addRoomPublishDto(roomDto);
			} else {//更新房间
				houseDao.updateRoomPublishDto(roomDto, rb.getId());
			}
		} catch (Exception e) {
			logger.error("爱上租发布房间失败， apartmentCode={}", asz.getApartmentCode(), e);
			throw new RuntimeException("爱上租发布房间失败");
		}
	}

	/**
	 * 构建HousePublishDto
	 */
	private HousePublishDto aszBuildHouseDto(HouseDetailAszToLocalDto transferDto) {
		HousePublishDto houseDto = new HousePublishDto();

		houseDto.setPositionX(transferDto.getLat());// 纬度
		houseDto.setPositionY(transferDto.getLng());// 经度
		houseDto.setCommunityName(transferDto.getResidentialName());// 小区名称
		houseDto.setPrice(transferDto.getRentPrice());// 月租金
		// houseDto.setBonus(0);//服务费
		// houseDto.setDeposit(0);//押金 ，单位为分
		// houseDto.setHasKey(0);//是否有钥匙
		// houseDto.setCompanyId(0);//经纪公司id
		houseDto.setAgencyPhone(transferDto.getAgentUphone());// 房管员电话
		houseDto.setAgencyName(transferDto.getAgentUname());// 房管员姓名
		houseDto.setStatus(transferDto.getOnlineStatus() == null ? 0 : Integer.parseInt(transferDto.getOnlineStatus()));// 
		houseDto.setBedroomNum(transferDto.getRooms());// 室
		houseDto.setLivingroomNum(transferDto.getLivings());// 厅
		houseDto.setKitchenNum(0);// 厨房数量
		houseDto.setToiletNum(transferDto.getBathRooms());// 卫
		houseDto.setBalconyNum(0);// 阳台数量
		houseDto.setBuildingNo("0");// 楼栋编号
		if (transferDto.getRentType().equals(AszConstants.HouseDetail.RENT_TYPE_ENTIRE)) {// 整租
			houseDto.setArea(transferDto.getTotalArea());
		}
		if (transferDto.getRentType().equals(AszConstants.HouseDetail.RENT_TYPE_SHARE)) {// 合租
			houseDto.setArea(transferDto.getBuildArea());
		}

		houseDto.setFlowNo(String.valueOf(transferDto.getFloor()));// 当前楼层
		houseDto.setFlowTotal(String.valueOf(transferDto.getGroudFloors()));// 总楼层
		houseDto.setOrientation(Integer.valueOf(transferDto.getOrientation()));//朝向
		//houseDto.setBuildingType(transferDt.);//装修类型
		// houseDto.setBuildingYear(String.valueOf(transferDto.getBuildYear()));建筑年限
		houseDto.setCheckInTime(transferDto.getCreateTime());// 入住时间默认当前时间
		houseDto.setEntireRent(Integer.valueOf(transferDto.getRentType()));// 出租方式
		houseDto.setSettings(transferDto.getHouseConfiuTation());
		houseDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		houseDto.setDesc(transferDto.getRemark());//房源描述
		// 租住类型 0分租 1整租 2整分皆可
		if (houseDto.getEntireRent() == 1) { // 整租，图片属于房源，分租属于房间
			houseDto.setImgs(transferDto.getImgList().substring(1, transferDto.getImgList().length()-1).replace("\"", ""));
		}
		houseDto.setBizName(transferDto.getAreaName());

		//PlatformCustomer customer = platformCustomerRepository.findByAppId(transferDto.getAppId());
		houseDto.setSource(AszConstants.ConfigDetail.HOUSE_SOURCE);
		houseDto.setCompanyId(AszConstants.ConfigDetail.APP_ID);
		houseDto.setCompanyName(AszConstants.ConfigDetail.HOUSE_SOURCE);
		return houseDto;
	}

	/**
	 * 构建RoomPublishDto
	 */
	private RoomPublishDto aszBuildRoomDto(HouseDetailAszToLocalDto transferDto) {
		RoomPublishDto roomDto = new RoomPublishDto();

		roomDto.setSellId(transferDto.getHouseSellId());
		roomDto.setPrice(transferDto.getRentPrice());
		// roomDto.setBonus(0);//服务费
		// roomDto.setDeposit(0);//押金 ，单位为分
		// roomDto.setHasKey(0);//是否有钥匙
		// roomDto.setCompanyId(0);//经纪公司id
		roomDto.setAgencyPhone(transferDto.getAgentUphone());
		roomDto.setAgencyName(transferDto.getAgentUname());
		roomDto.setStatus(transferDto.getOnlineStatus() == null ? 0 : Integer.parseInt(transferDto.getOnlineStatus()));
		roomDto.setArea(transferDto.getBuildArea());
		roomDto.setOrientation(Integer.valueOf(transferDto.getOrientation()));
		roomDto.setCheckInTime(transferDto.getCreateTime());
		roomDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		roomDto.setDesc(transferDto.getRemark());
		roomDto.setImgs(transferDto.getImgList().substring(1, transferDto.getImgList().length()-1).replace("\"", ""));
		roomDto.setRoomName(transferDto.getRoomNo());
		
		if(transferDto.getRoomNo().equals(AszConstants.HouseDetail.ROOM_NO_METH)){
			roomDto.setRoomType(AszConstants.HouseDetail.ROOM_TYPE_MAST);//主卧
		}else{
			roomDto.setRoomType(AszConstants.HouseDetail.ROOM_TYPE_SECONDARY);//次卧
		}
		 
		return roomDto;
	}

}
