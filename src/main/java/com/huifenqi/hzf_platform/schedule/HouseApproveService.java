package com.huifenqi.hzf_platform.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jboss.jandex.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.primitives.Ints;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.CompanyCityMapping;
import com.huifenqi.hzf_platform.context.entity.house.CompanyWhiteConfig;
import com.huifenqi.hzf_platform.context.entity.house.HouseApproveRecord;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.enums.ApproveStatusEnum;
import com.huifenqi.hzf_platform.context.enums.HouseDataErrorEnums;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyCityMappingRepository;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyWhiteConfigRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseApproveRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;

@Service
@Transactional
public class HouseApproveService {

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private HouseDetailRepository houseDetailRepository;

	@Resource
	private HouseApproveRecordRepository houseApproveRecordRepository;

	@Resource
	private RoomBaseRepository roomBaseRepository;

	@Resource
	private CompanyCityMappingRepository companyCityRepository;
	
	@Resource
    private CompanyWhiteConfigRepository companyWhiteConfigRepository;

	private static Logger logger = LoggerFactory.getLogger(HouseApproveService.class);

	public void checkHouseErrorData(HouseDetail hd, HouseBase hb) {
		
		int bedroomNum = hd.getBedroomNum();// 卧室数量
		int monthRent = hb.getMonthRent() / 100;// 月租
		String communityName = hd.getCommunityName();// 小区名
		String agencyPhone = hb.getAgencyPhone();// 中介电话
		Float area = hd.getArea();//面积
		int flowNo = 0;
		try {
			flowNo = Integer.valueOf(hd.getFlowNo());//当前楼层 
		} catch (Exception e) {
			logger.error("数据转换异常，flowNo={}", hd.getFlowNo());
		}
		int flowTotal = Integer.valueOf(hd.getFlowTotal());//总楼层
		String companyName = hb.getCompanyName();//公司名名称
		
		boolean result = false;
		StringBuffer reason = new StringBuffer();// 记录错误日志

		if (area < 5) {
		    result = true;
		    reason.append(HouseDataErrorEnums.HOUSE_AREA.getDesc());
		}
		
		if (area > 500) {
			result = true;
			reason.append(HouseDataErrorEnums.BIG_AREA.getDesc());
		}
		
		if (flowNo > 100) {
		    result = true;
		    reason.append(HouseDataErrorEnums.HOUSE_FLOW_NO.getDesc());
		}
		
		if (flowTotal > 100) {
		    result = true;
		    reason.append(HouseDataErrorEnums.HOUSE_FLOW_TOTAL.getDesc());
		}
		
		if(StringUtils.isNotEmpty(companyName)){
		    if(companyName.contains("测试") || companyName.contains("演示")){
		        result = true;
	            reason.append(HouseDataErrorEnums.COMPANYNAME.getDesc()).append(",");
		    }
		}
		
		if ("无".equals(communityName)) {
			reason.append("小区名是：").append(communityName).append(",");
			result = true;
		}
		//检查中介电话
		boolean phoneFlag = hd.getEntireRent() == Constants.HouseDetail.RENT_TYPE_ENTIRE
				&& checkAgencyPhone(agencyPhone, reason);
		if (phoneFlag) {
			result = true;
		}
		// 如果公司名为空，则默认通过
		if (!StringUtils.isEmpty(hb.getCompanyName())) {
			// 判断当前中介公司是否开通当前城市; 1 为有效状态
			List<CompanyCityMapping> cclist = companyCityRepository.findBycompanyNameAndStatus(hb.getCompanyName(), 1);
			List<Long> cityIdList = new ArrayList<Long>();
			for (CompanyCityMapping cc : cclist) {
				cityIdList.add(cc.getCityId());
			}
			// 如果不包含改城市，则审核不通过
			if (cityIdList.size() != 0 && (!cityIdList.contains(hd.getCityId()))) {
				result = true;
				reason.append(hb.getCompanyName() + "未开通" + hd.getCity()).append(",");
				// append(HouseDataErrorEnums.COMPANY_CITY.getDesc()).append(",");
			}
		}
		
		//校验会管房白名单
		if(hd.getSource().equals("会管房")){
		    if(checkWhiteConpany(hd.getSource(),hb.getCompanyId())){//黑名单
		        result = true;
                reason.append(hd.getSource()+hb.getCompanyId() + "未开放白名单,"); 
		    }
		}
		
		if (bedroomNum == 0) {
			result = true;
			reason.append(HouseDataErrorEnums.BEDROOM.getDesc()).append(",");
		}

		// 只对整租的房源审核价格
		boolean flag = hd.getEntireRent() == Constants.HouseDetail.RENT_TYPE_ENTIRE && ((monthRent > 0 && monthRent < 300) || monthRent > 30000);
		if (flag) {
			result = true;
			reason.append(HouseDataErrorEnums.PRICE_HOUSE.getDesc()).append(",");
		}

		if (hd.getBizName() != null && "东华门".equals(hd.getBizName())) {
			result = true;
			reason.append(HouseDataErrorEnums.DONGHUAMEN.getDesc()).append(",");
		}

		if (result) {
			int rentType = hd.getEntireRent();// 0 合租，1 整租
			logger.info("houseSellId = {},rentType = {}, 房源错误信息：{}", hd.getSellId(), hd.getEntireRent(),
					reason.toString());
			houseDetailRepository.setApproveStatusBySellId(hd.getSellId(), ApproveStatusEnum.SYS_APP_REJECT.getCode());
			if (rentType == Constants.HouseDetail.RENT_TYPE_SHARE) { // 如果房源审核未通过，将其下所有的房间置为审核未通过
				roomBaseRepository.setApproveStatusBySellId(hd.getSellId(), ApproveStatusEnum.SYS_APP_REJECT.getCode());
			}
			HouseApproveRecord record = getApproveRecord(hd.getSellId(), reason.toString(), 0L);
			houseApproveRecordRepository.save(record);

		} else {
			int nowStatus = hd.getApproveStatus();
			int approveStatus = ApproveStatusEnum.SYS_APP_PASS.getCode();
			if (nowStatus == 10) {// 如果处于临时状态，则将审核状态置为最近一次的历史状态
				HouseApproveRecord rec = houseApproveRecordRepository.findBySellIdAndRoomId(hd.getSellId(), 0L);
				if (rec != null) {
					approveStatus = rec.getImageStatus();
					logger.info("sellId={}房源，没有更新图片，审核状态还原为 {}", hd.getSellId(), approveStatus);
				}

			}
			houseDetailRepository.setApproveStatusBySellId(hd.getSellId(), approveStatus);
		}

	}

	public void checkRoomErrorData(RoomBase rb) {
		boolean result = false;

		StringBuffer reason = new StringBuffer();// 记录错误日志

		int roomArea = (int) rb.getArea(); // 房间面积
		int monthRent = rb.getMonthRent() / 100;// 月租
		String agencyPhone = rb.getAgencyPhone();// 房间中介电话
		
		//检查中介电话
		if (checkAgencyPhone(agencyPhone, reason)) {
			result = true;
		}
		
		if ((monthRent > 0 && monthRent < 100) || monthRent > 10000) {
			result = true;
			reason.append(HouseDataErrorEnums.PRICE_ROOM.getDesc()).append(",");
		}

		if (roomArea > 60) {
			result = true;
			reason.append(HouseDataErrorEnums.ROOM_AREA.getDesc());
		}
		
		if (roomArea < 5) {
            result = true;
            reason.append(HouseDataErrorEnums.ROOM_AREA.getDesc());
        }
		
		if (result) {
			logger.info("houseSellId = {},RoomID = {}, 房间错误信息：{}", rb.getSellId(), rb.getId(), reason.toString());
			roomBaseRepository.setApproveStatusByRoomId(rb.getId(), ApproveStatusEnum.SYS_APP_REJECT.getCode());
			HouseApproveRecord record = getApproveRecord(rb.getSellId(), reason.toString(), rb.getId());
			houseApproveRecordRepository.save(record);
		} else {
			int nowStatus = rb.getApproveStatus();
			int approveStatus = ApproveStatusEnum.SYS_APP_PASS.getCode();
			if (nowStatus == 10) {// 如果处于临时状态，则将审核状态置为最近一次的历史状态
				HouseApproveRecord rec = houseApproveRecordRepository.findBySellIdAndRoomId(rb.getSellId(), rb.getId());
				if (rec != null) {
					approveStatus = rec.getImageStatus();
					logger.info("id={}房间，没有更新图片，审核状态还原为 {}", rb.getId(), approveStatus);
				}
			}
			roomBaseRepository.setApproveStatusByRoomId(rb.getId(), approveStatus);// 标记为程序审核通过
		}
	}

	/**
	 * 获取ApproveRecord对象
	 * 
	 * @param hd
	 * @param reason
	 * @param roomId
	 * @return
	 */
	private HouseApproveRecord getApproveRecord(String sellId, String reason, Long roomId) {

		HouseApproveRecord ar = new HouseApproveRecord();
		ar.setSellId(sellId);
		ar.setRoomId(roomId);
		ar.setErrorReason(reason);
		ar.setOperator("sys");
		ar.setApproveDate(new Date());
		ar.setCreateTime(new Date());
		return ar;

	}
	
	/**
	 * 检查中介电话
	 * @param agencyPhone
	 * @param reason
	 * @param result
	 */
	private boolean checkAgencyPhone(String agencyPhone, StringBuffer reason) {

		if (StringUtils.isNotEmpty(agencyPhone)) {
			agencyPhone = agencyPhone.replace("-", "").replace(" ", "");
			if ("13000000000".equals(agencyPhone)) {
				reason.append("中介电话不合法：").append(agencyPhone).append(",");
				return true;
			}
			if ((!RulesVerifyUtil.verifyPhone(agencyPhone)) && (!RulesVerifyUtil.verifyFixedPhone(agencyPhone))) {
				reason.append("中介电话不合法：").append(agencyPhone).append(",");
				return true;
			}
		}
		return false;
	}
	
	private boolean checkWhiteConpany(String source,String companyId) {
	    CompanyWhiteConfig companyWhiteConfig =  companyWhiteConfigRepository.findBySourceAndCompanyId(source,companyId);
	    if(companyWhiteConfig !=null ){
	        return false;
	    }
        return true;
    }
}
