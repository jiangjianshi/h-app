package com.huifenqi.hzf_platform.schedule;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHouseDetailToLocalDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.BeanMapperUtil;

@Service
@Transactional
public class DataTransferService {

	@Resource
	private BdHouseDetailRepository bdHouseDetailRepository;
	@Resource
	private RoomBaseRepository roomBaseRepository;
	@Resource
	private PlatformCustomerRepository platformCustomerRepository;
	@Resource
	private HouseDao houseDao;

	private static Logger logger = LoggerFactory.getLogger(DataTransferService.class);

	/**
	 * 先发布房价，再发布房源
	 * 
	 * @param hd
	 */
	public void pulbicHouseAndRoom(BdHouseDetail hd) {

		publishHouse(hd);
		publishRoom(hd);
	}

	/**
	 * 整租处理
	 * 
	 * @throws Exception
	 */
	public void publishHouse(BdHouseDetail hd) {
		try {
			BdHouseDetailToLocalDto transferDto = new BdHouseDetailToLocalDto();
			BeanMapperUtil.copy(hd, transferDto);// 复制对象
			HousePublishDto houseDto = buildHouseDto(transferDto);
			logger.info("第三方的房屋ID:{}, 房源状态：{}", transferDto.getOutHouseId(), houseDto.getStatus());
			if (StringUtils.isEmpty(hd.getHouseSellId())) {
				String returnSellId = houseDao.addHousePublishDto(houseDto);
				hd.setHouseSellId(returnSellId);
				logger.info("对应房源id：{}", returnSellId);
				bdHouseDetailRepository.updateHouseSellId(hd.getOutHouseId(), hd.getAppId(), returnSellId);
			} else {
				houseDao.updateHousePublishDto(houseDto, hd.getHouseSellId());
				bdHouseDetailRepository.updateHouseTrandferFlag(hd.getOutHouseId(), hd.getAppId()); //将transferFlag=1 ，已同步
			}
		} catch (Exception e) {
			logger.error("发布房源失败， outHouseId={}，appId={}", hd.getOutHouseId(), hd.getAppId(), e);
			throw new RuntimeException("发布房源失败");
		}

	}

	/**
	 * 出租单间
	 * 
	 * @throws Exception
	 */
	public void publishRoom(BdHouseDetail hd) {

		try {
			RoomBase rb = roomBaseRepository.findBySellId(hd.getHouseSellId());
			BdHouseDetailToLocalDto transferDto = new BdHouseDetailToLocalDto();
			BeanMapperUtil.copy(hd, transferDto);// 复制对象
			RoomPublishDto roomDto = buildRoomDto(transferDto);
			if (rb == null) {
				houseDao.addRoomPublishDto(roomDto);
			} else {
				houseDao.updateRoomPublishDto(roomDto, rb.getId());
			}
		} catch (Exception e) {
			logger.error("发布房间失败， outHouseId={}，appId={}", hd.getOutHouseId(), hd.getAppId(), e);
			throw new RuntimeException("发布房间失败");
		}
	}

	/**
	 * 构建HousePublishDto
	 */
	private HousePublishDto buildHouseDto(BdHouseDetailToLocalDto transferDto) {
		HousePublishDto houseDto = new HousePublishDto();

		houseDto.setPositionX(transferDto.getyCode());
		houseDto.setPositionY(transferDto.getxCode());
		houseDto.setCommunityName(transferDto.getDistrictName());
		houseDto.setPrice(transferDto.getMonthRent());
		// houseDto.setBonus(0);//服务费
		// houseDto.setDeposit(0);//押金 ，单位为分
		// houseDto.setHasKey(0);//是否有钥匙
		// houseDto.setCompanyId(0);//经纪公司id
		houseDto.setAgencyPhone(transferDto.getAgentPhone());
		houseDto.setAgencyName(transferDto.getAgentName());
		houseDto.setStatus(transferDto.getState() == null ? 0 : Integer.parseInt(transferDto.getState()));
		houseDto.setBedroomNum(transferDto.getBedRoomNum());
		houseDto.setLivingroomNum(transferDto.getLivingRoomNum());
		houseDto.setKitchenNum(0);// 厨房数量
		houseDto.setToiletNum(transferDto.getToiletNum());
		houseDto.setBalconyNum(0);// 阳台数量
		houseDto.setBuildingNo("0");// 楼栋编号
		houseDto.setHouseNo(transferDto.getAddress());//门牌号
		houseDto.setArea(transferDto.getRentRoomArea());
		houseDto.setFlowNo(String.valueOf(transferDto.getHouseFloor()));
		houseDto.setFlowTotal(String.valueOf(transferDto.getTotalFloor()));
		houseDto.setOrientation(transferDto.getFaceToType());
		houseDto.setBuildingType(transferDto.getBuildType());
		houseDto.setBuildingYear(String.valueOf(transferDto.getBuildYear()));
		houseDto.setCheckInTime(transferDto.getRentStartDate());// 入住时间
		houseDto.setEntireRent(transferDto.getRentType());
		houseDto.setSettings(transferDto.getDetailPoint());
		houseDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		houseDto.setDesc(transferDto.getHouseDesc());
		// 租住类型 0分租 1整租 2整分皆可
		if (houseDto.getEntireRent() == 1) { // 整租，图片属于房源，分租属于房间
			houseDto.setImgs(transferDto.getPicUrlList());
		}
		houseDto.setBizName(transferDto.getAreaName());

		PlatformCustomer customer = platformCustomerRepository.findByAppId(transferDto.getAppId());
		houseDto.setSource(customer.getSource());
		if(customer.getIsSaas() == Constants.BdHouseDetail.IS_SAAS_NO){//品牌公寓
			houseDto.setCompanyId(customer.getAppId());
			houseDto.setCompanyName(customer.getSource());
		}else{//saas公司
			houseDto.setCompanyId(transferDto.getCompanyId());
			houseDto.setCompanyName(transferDto.getCompanyName());
		}
		
		return houseDto;
	}

	/**
	 * 构建RoomPublishDto
	 */
	private RoomPublishDto buildRoomDto(BdHouseDetailToLocalDto transferDto) {
		RoomPublishDto roomDto = new RoomPublishDto();

		roomDto.setSellId(transferDto.getHouseSellId());
		roomDto.setPrice(transferDto.getMonthRent());
		// roomDto.setBonus(0);//服务费
		// roomDto.setDeposit(0);//押金 ，单位为分
		// roomDto.setHasKey(0);//是否有钥匙
		// roomDto.setCompanyId(0);//经纪公司id
		roomDto.setAgencyPhone(transferDto.getAgentPhone());
		roomDto.setAgencyName(transferDto.getAgentName());
		roomDto.setStatus(transferDto.getState() == null ? 0 : Integer.parseInt(transferDto.getState()));
		roomDto.setArea(transferDto.getRentRoomArea());
		roomDto.setOrientation(transferDto.getFaceToType());
		roomDto.setCheckInTime(transferDto.getRentStartDate());// 入住时间
		roomDto.setSettings(transferDto.getDetailPoint());
		roomDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		roomDto.setDesc(transferDto.getHouseDesc());
		roomDto.setImgs(transferDto.getPicUrlList());
		roomDto.setRoomName(transferDto.getRoomName());
		roomDto.setRoomType(transferDto.getBedRoomType());//房价类型 2017-06-27 14:40:24 jjs
		return roomDto;
	}

}
