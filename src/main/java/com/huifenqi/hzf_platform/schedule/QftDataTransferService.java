package com.huifenqi.hzf_platform.schedule;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.ScheduleConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHouseDetailQftToLocalDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailQftRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.BeanMapperUtil;

@Service
@Transactional
public class QftDataTransferService {

	@Resource
	private BdHouseDetailQftRepository bdHouseDetailQftRepository;
	@Resource
	private RoomBaseRepository roomBaseRepository;
	@Resource
	private PlatformCustomerRepository platformCustomerRepository;
	@Resource
	private HouseDao houseDao;

	private static Logger logger = LoggerFactory.getLogger(QftDataTransferService.class);

	/**
	 * 先发布房价，再发布房源
	 * 
	 * @param hd
	 */
	public void pulbicHouseAndRoom(BdHouseDetailQft hd) {

		publishHouse(hd);
		publishRoom(hd);
	}

	/**
	 * 整租处理
	 * 
	 * @throws Exception
	 */
	public void publishHouse(BdHouseDetailQft hd) {
		try {
			BdHouseDetailQftToLocalDto transferDto = new BdHouseDetailQftToLocalDto();
			BeanMapperUtil.copy(hd, transferDto);// 复制对象
			HousePublishDto houseDto = buildHouseDto(transferDto);
			logger.info("第三方的房屋ID:{}, 房源状态：{}", transferDto.getOutHouseId(), houseDto.getStatus());
			if (StringUtils.isEmpty(hd.getHouseSellId())) {
				String returnSellId = houseDao.addHousePublishDto(houseDto);
				hd.setHouseSellId(returnSellId);
				logger.info("对应房源id：{}", returnSellId);
				bdHouseDetailQftRepository.updateHouseSellId(hd.getOutHouseId(), hd.getAppId(), returnSellId);
			} else {
				houseDao.updateHousePublishDto(houseDto, hd.getHouseSellId());
				bdHouseDetailQftRepository.updateHouseTrandferFlag(hd.getOutHouseId(), hd.getAppId(),
						ScheduleConstants.QftUtil.IS_FLAG_YES); // 将transferFlag=1
																// ，已同步
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
	public void publishRoom(BdHouseDetailQft hd) {

		try {
			RoomBase rb = roomBaseRepository.findBySellId(hd.getHouseSellId());
			BdHouseDetailQftToLocalDto transferDto = new BdHouseDetailQftToLocalDto();
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
	 * 修改状态
	 * 
	 * @throws Exception
	 */
	public void updateHouseTrandferFlag(String outHouseId, String appId, int flag) {
		bdHouseDetailQftRepository.updateHouseTrandferFlag(outHouseId, appId, flag);
	}

	/**
	 * 修改收集坐标信息
	 * 
	 * @throws Exception
	 */
	public void updateHouseXY(String outHouseId, String appId, int flag, String lng, String lat, int precise,
			int confidence, String level) {
		bdHouseDetailQftRepository.updateHouse(outHouseId, appId, flag, lng, lat, precise, confidence, level);
	}

	/**
	 * 构建HousePublishDto
	 */
	private HousePublishDto buildHouseDto(BdHouseDetailQftToLocalDto transferDto) {
		HousePublishDto houseDto = new HousePublishDto();

		houseDto.setPositionX(transferDto.getyCode());
		houseDto.setPositionY(transferDto.getxCode());
		houseDto.setCommunityName(transferDto.getDistrictName());
		houseDto.setPrice(transferDto.getMonthRent());
		// houseDto.setBonus(0);//服务费
		// houseDto.setDeposit(0);//押金 ，单位为分
		// houseDto.setHasKey(0);//是否有钥匙
		houseDto.setAgencyPhone(transferDto.getAgentPhone());
		houseDto.setAgencyName(transferDto.getAgentName());
		houseDto.setStatus(transferDto.getState() == null ? 0 : Integer.parseInt(transferDto.getState()));
		houseDto.setBedroomNum(transferDto.getBedRoomNum());
		houseDto.setLivingroomNum(transferDto.getLivingRoomNum());
		houseDto.setToiletNum(transferDto.getToiletNum());
		houseDto.setKitchenNum(0);// 厨房数量
		houseDto.setBalconyNum(0);// 阳台数量
		houseDto.setBuildingNo("0");// 楼栋编号
		houseDto.setHouseNo(transferDto.getAddress());//门牌号
		houseDto.setArea(transferDto.getRentRoomArea());
		houseDto.setFlowNo(String.valueOf(transferDto.getHouseFloor()));
		houseDto.setFlowTotal(String.valueOf(transferDto.getTotalFloor()));
		houseDto.setOrientation(Integer.parseInt(transferDto.getFaceToType()));
		houseDto.setBuildingType(transferDto.getBuildType());
		houseDto.setBuildingYear(String.valueOf(transferDto.getBuildYear()));
		houseDto.setCheckInTime(transferDto.getRentStartDate());// 入住时间
		houseDto.setEntireRent(transferDto.getRentType());
		// houseDto.setSettings(transferDto.getDetailPoint());
		// houseDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		houseDto.setDesc(transferDto.getHouseDesc());
		// 租住类型 0分租 1整租 2整分皆可
		if (houseDto.getEntireRent() == 1) { // 整租，图片属于房源，分租属于房间
			houseDto.setImgs(transferDto.getPicUrlList());
		}
		houseDto.setBizName(transferDto.getAreaName());
		houseDto.setCompanyId(transferDto.getCompanyId());
		houseDto.setCompanyName(transferDto.getCompanyName());

		PlatformCustomer customer = platformCustomerRepository.findByAppId(transferDto.getAppId());
		houseDto.setSource(customer.getSource());

		return houseDto;
	}

	/**
	 * 构建RoomPublishDto
	 */
	private RoomPublishDto buildRoomDto(BdHouseDetailQftToLocalDto transferDto) {
		RoomPublishDto roomDto = new RoomPublishDto();

		roomDto.setSellId(transferDto.getHouseSellId());
		roomDto.setPrice(transferDto.getMonthRent());
		// roomDto.setBonus(0);//服务费
		// roomDto.setDeposit(0);//押金 ，单位为分
		// roomDto.setHasKey(0);//是否有钥匙
		roomDto.setAgencyPhone(transferDto.getAgentPhone());
		roomDto.setAgencyName(transferDto.getAgentName());
		roomDto.setStatus(transferDto.getState() == null ? 0 : Integer.parseInt(transferDto.getState()));
		roomDto.setArea(transferDto.getRentRoomArea());
		roomDto.setOrientation(Integer.parseInt(transferDto.getFaceToType()));
		roomDto.setCheckInTime(transferDto.getRentStartDate());// 入住时间
		// roomDto.setSettings(transferDto.getDetailPoint());
		// roomDto.setSettingsAddon(transferDto.getSettingsAddon());// 次要设施
		roomDto.setDesc(transferDto.getHouseDesc());
		roomDto.setImgs(transferDto.getPicUrlList());
		roomDto.setRoomName(transferDto.getRoomName());
		if (StringUtils.isNotEmpty(transferDto.getBedRoomType())) {
			roomDto.setRoomType(Integer.valueOf(transferDto.getBedRoomType()));// 房间类型
		}
		return roomDto;
	}

}
