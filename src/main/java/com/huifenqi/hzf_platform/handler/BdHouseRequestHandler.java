package com.huifenqi.hzf_platform.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.BdConstantsEnum;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDtoQft;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.dao.BdHouseDao;
import com.huifenqi.hzf_platform.dao.BdHouseQftDao;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.BdHouseCheckUtil;
import com.huifenqi.hzf_platform.utils.BdHouseQftCheckUtil;
import com.huifenqi.hzf_platform.utils.BdRequestUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseRequestHandler date: 2016年4月26日 下午4:40:45 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Service
public class BdHouseRequestHandler {

	private static final Log logger = LogFactory.getLog(BdHouseRequestHandler.class);

	@Autowired
	private BdHouseQftDao bdHouseQftDao;

	@Autowired
	private BdHouseDao bdHouseDao;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;
	
	/**
	 * 模拟百度发布房源
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses bdFeedHouse(HttpServletRequest request) {
		BdHousePublishDto dbhousePublishDto = null;
	
		try {
			//验证模拟百度发布房源参数是否正确
			dbhousePublishDto = BdHouseCheckUtil.getHousePublishDtoAdd(request);
		} catch (Exception e) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, e.getMessage());
		}
				
		if (dbhousePublishDto == null) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, "房源参数解析失败");
		}

		String appId = null;
		try {	
			//appId = RequestUtils.getParameterString(request, "appId");
			appId = BdRequestUtils.getParameterFormMap(request, "appId");
		} catch (Exception e) {
		}
		//验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		
		//saas公司标识
		dbhousePublishDto.setIsSaas(platformCustomer.getIsSaas());
		
		// 查询房源基础信息是否存在
		BdHouseDetail houseBase;
		try {
			houseBase = bdHouseDao.findBySellIdandAppIdandState(dbhousePublishDto.getOutHouseId(),dbhousePublishDto.getAppId());
			if (houseBase != null) {
				logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(), dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_EXIST_SAME_HOUSE, "重复发布房源");
			}
		} catch (Exception e1) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "发布房源失败");
		}
		
		//发布房源
		try {
			bdHouseDao.addBdHousePublishDto(dbhousePublishDto);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("发布成功");
		
		return bdResponses;
	}

	/**
	 * 模拟百度房源信息修改接口
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses roomSingleModify(HttpServletRequest request) throws Exception {
		
//		String outHouseId = RequestUtils.getParameterString(request, "outHouseId");
//		String appId = RequestUtils.getParameterString(request, "appId");	
		String appId = BdRequestUtils.getParameterFormMap(request, "appId");
		String outHouseId = BdRequestUtils.getParameterFormMap(request, "outHouseId");
		BdHousePublishDto dbhousePublishDto = null;

		try {
			//验证和获取更新数据
			dbhousePublishDto = BdHouseCheckUtil.getHousePublishDtoUpdate(request);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, e.getMessage());
		}

		if (dbhousePublishDto == null) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, "房源参数解析失败");
		}

		//验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		
		//saas公司标识
		dbhousePublishDto.setIsSaas(platformCustomer.getIsSaas());
		
		// 查询房源基础信息是否存在
		BdHouseDetail houseBase;
		try {
			houseBase = bdHouseDao.findBySellIdandAppIdandState(dbhousePublishDto.getOutHouseId(),dbhousePublishDto.getAppId());
			if (houseBase == null) {
				logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(), dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_NO_EXIST_HOUSE, "修改不存在的房源");
			}
		} catch (Exception e1) {
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "更新房源失败");
		}
		
		try {
			bdHouseDao.roomSingleModify(dbhousePublishDto,outHouseId,appId);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源更新失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "更新房源失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("修改成功");
		
		return bdResponses;
	}


	/**
	 * 模拟百度房源上下架接口
	 *
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses houseModify(HttpServletRequest request) throws Exception {

		BdHousePublishDto dbhousePublishDto = null;
		String outHouseId = null;
		String appId = null;
		String memo = null;
		String status = null;
		int transferFlag = BdConstantsEnum.TransferFlagEnum.INIT.getIndex();
		try {
			//验证和获取更新数据
			dbhousePublishDto = BdHouseCheckUtil.getHousePublishDtoStatus(request);
			outHouseId = dbhousePublishDto.getOutHouseId();
			appId = dbhousePublishDto.getAppId();
			memo = dbhousePublishDto.getMemo();
			status = dbhousePublishDto.getStatus();
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_FIELD_ERROR, e.getMessage());
		}
		
		//验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		// 查询房源基础信息是否存在
		BdHouseDetail houseBase;
		try {
			houseBase = bdHouseDao.findBySellIdandAppId(dbhousePublishDto.getOutHouseId(),dbhousePublishDto.getAppId());
			if (houseBase == null) {
				logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(), dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_NO_EXIST_HOUSE, "修改不存在的房源");
			}
			
			bdHouseDao.houseModify(outHouseId, appId, memo, status, transferFlag);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源信息修改失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_PROG_EXCEPION, "房源上下架失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("修改成功");

		return bdResponses;
	}

	/**
	 * 模拟百度发布房源-QFT
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses bdFeedHouseQft(HttpServletRequest request) {
		BdHousePublishDtoQft dbhousePublishDto = null;

		try {
			// 验证模拟百度发布房源参数是否正确
			dbhousePublishDto = BdHouseQftCheckUtil.getHousePublishDtoAdd(request);
		} catch (Exception e) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, e.getMessage());
		}

		if (dbhousePublishDto == null) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, "房源参数解析失败");
		}

		String appId = null;
		try {
			// appId = RequestUtils.getParameterString(request, "appId");
			appId = BdRequestUtils.getParameterFormMap(request, "appId");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}

		// 查询房源基础信息是否存在
		BdHouseDetailQft houseBase;
		try {
			houseBase = bdHouseQftDao.findBySellIdandAppIdandState(dbhousePublishDto.getOutHouseId(),
					dbhousePublishDto.getAppId());
			if (houseBase != null) {
				logger.error(String.format("房源发布失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(),
						dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_EXIST_SAME_HOUSE, "重复发布房源");
			}
		} catch (Exception e1) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "发布房源失败");
		}

		// 发布房源
		try {
			bdHouseQftDao.addBdHousePublishDtoQft(dbhousePublishDto);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("发布成功");

		return bdResponses;
	}

	/**
	 * 模拟百度房源信息修改接口-Qft
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses roomSingleModifyQft(HttpServletRequest request) throws Exception {

		// String outHouseId = RequestUtils.getParameterString(request,
		// "outHouseId");
		// String appId = RequestUtils.getParameterString(request, "appId");
		String appId = BdRequestUtils.getParameterFormMap(request, "appId");
		String outHouseId = BdRequestUtils.getParameterFormMap(request, "outHouseId");
		BdHousePublishDtoQft dbhousePublishDto = null;

		try {
			// 验证和获取更新数
			dbhousePublishDto = BdHouseQftCheckUtil.getHousePublishDtoUpdate(request);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, e.getMessage());
		}

		if (dbhousePublishDto == null) {
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_FIELD_ERROR, "房源参数解析失败");
		}

		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}

		// 查询房源基础信息是否存在
		BdHouseDetailQft houseBase;
		try {
			houseBase = bdHouseQftDao.findBySellIdandAppIdandState(dbhousePublishDto.getOutHouseId(),
					dbhousePublishDto.getAppId());
			if (houseBase == null) {
				logger.error(String.format("房源更新失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(),
						dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_NO_EXIST_HOUSE, "修改不存在的房源");
			}
		} catch (Exception e1) {
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "更新房源失败");
		}

		try {
			bdHouseQftDao.roomSingleModify(dbhousePublishDto, outHouseId, appId);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源更新失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "更新房源失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("修改成功");

		return bdResponses;
	}

	/**
	 * 模拟百度房源上下架接口Qft
	 *
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses houseModifyQft(HttpServletRequest request) throws Exception {

		BdHousePublishDtoQft dbhousePublishDto = null;
		String outHouseId = null;
		String appId = null;
		String memo = null;
		String status = null;
		int transferFlag = BdConstantsEnum.TransferFlagEnum.INIT.getIndex();
		try {
			// 验证和获取更新数据
			dbhousePublishDto = BdHouseQftCheckUtil.getHousePublishDtoStatus(request);
			outHouseId = dbhousePublishDto.getOutHouseId();
			appId = dbhousePublishDto.getAppId();
			memo = dbhousePublishDto.getMemo();
			status = dbhousePublishDto.getStatus();
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_FIELD_ERROR, e.getMessage());
		}

		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		// 查询房源基础信息是否存在
		BdHouseDetailQft houseBase;
		try {
			houseBase = bdHouseQftDao.findBySellIdandAppId(dbhousePublishDto.getOutHouseId(),
					dbhousePublishDto.getAppId());
			if (houseBase == null) {
				logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", dbhousePublishDto.getOutHouseId(),
						dbhousePublishDto.getAppId()));
				return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_NO_EXIST_HOUSE, "修改不存在的房源");
			}

			bdHouseQftDao.houseModify(outHouseId, appId, memo, status, transferFlag);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源信息修改失败" + e.getMessage()));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_PROG_EXCEPION, "房源上下架失败");
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("修改成功");

		return bdResponses;
	}

}
