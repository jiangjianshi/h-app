/**
 * Project Name: hzf_platform
 * File Name: HouseRequestHandler.java
 * Package Name: com.huifenqi.hzf_platform.handler
 * Date: 2016年4月26日下午4:40:45
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved.
 *
 */
package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.comm.LockManager;
import com.huifenqi.hzf_platform.configuration.SearchServiceConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.ComplaintSaveDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseOptHistoryRedisDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ComplaintInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.ComplaintQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseRecommendInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseRecommendQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.context.entity.house.Apartment;
import com.huifenqi.hzf_platform.context.entity.house.CompanyOffConfig;
import com.huifenqi.hzf_platform.context.entity.house.FootmarkHistory;
import com.huifenqi.hzf_platform.context.entity.house.HouseCollection;
import com.huifenqi.hzf_platform.context.entity.house.HouseOriginal;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;
import com.huifenqi.hzf_platform.context.entity.house.OrderCustom;
import com.huifenqi.hzf_platform.context.entity.house.RoomOriginal;
import com.huifenqi.hzf_platform.context.entity.location.PhoneCallRecord;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.context.response.ResponseMeta;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.HouseSubDao;
import com.huifenqi.hzf_platform.dao.SaasHouseDao;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseCollectionRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseOriginalRepository;
import com.huifenqi.hzf_platform.dao.repository.house.PhoneCallRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomOriginalRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.email.SchedulingConfig;
import com.huifenqi.hzf_platform.utils.BeanMapperUtil;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.MsgUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.ResponseUtils;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;

/**
 * ClassName: HouseRequestHandler date: 2016年4月26日 下午4:40:45 Description:
 *
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Service
public class HouseRequestHandler {

    private static final Log logger = LogFactory.getLog(HouseRequestHandler.class);

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private HouseSubDao houseSubDao;

    @Autowired
    private SaasHouseDao saasHouseDao;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PlatformCustomerRepository platformCustomerRepository;

    @Autowired
    private CompanyOffConfigRepository companyOffConfigRepository;

    @Autowired
    private PhoneCallRecordRepository phoneCallRecordRepository;

    @Autowired
    private AgencyManageRepository agencyManageRepository;

    @Autowired
    private HouseOriginalRepository houseOriginalRepository;

    @Autowired
    private RoomOriginalRepository roomOriginalRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private Configuration configuration;

    @Autowired
    private LockManager lockManager;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private SchedulingConfig schedulingConfig;

    @Autowired
    private HouseCollectionRepository houseCollectionRepository;
    
    @Autowired
    private SearchServiceConfiguration searchServiceConfiguration;

    public static final String FOOTMARK = "footmark";

    /**
     * 搜索房源
     *
     * @return
     */
    public Responses searchHouse(HttpServletRequest request) throws Exception {
        HouseSearchDto houseSearchDto = getHouseSearchDto(request);
        
        houseSearchDto.setIsHomePage(1);
        return newSearchHouseList(houseSearchDto);
        //return commSearch(houseSearchDto); 
    }

    /**
     * 条件筛选 搜索房源
     * 
     * @return
     */
    public Responses searchHouseList(HttpServletRequest request) throws Exception {
        HouseSearchDto houseSearchDto = getHouseSearchDto(request);
        
        return newSearchHouseList(houseSearchDto);

        //return searchHouseList(houseSearchDto);
        
    }

    /**
     * 店铺页搜索房源
     *
     * @return
     */
    public Responses saasSearchHouse(HttpServletRequest request, String agencyId) throws Exception {
        HouseSearchDto houseSearchDto = getSaasHouseSearchDto(request);
        houseSearchDto.setCompanyId(agencyId);
        // 若地铁站id不为空，查询地铁站坐标
        // TODO 确定查询方式，db or cache?
        long stationId = houseSearchDto.getStationId();
        if (stationId != 0) {
            Subway subway = subwayRepository.findSubWayByStationId(stationId);
            String stationPosition = HouseUtil.getPosition(subway);
            if (StringUtil.isEmpty(stationPosition)) {
                logger.error(LogUtils.getCommLog("地铁站坐标查询失败 ,stationId:" + stationId));
                return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
            }
            houseSearchDto.setStationLocation(stationPosition);
        }

        // TODO 实现搜索功能
        logger.info(LogUtils.getCommLog("搜房源请求参数 ,houseSearchDto:" + houseSearchDto));
        HouseSearchResultDto houseSearchResultDto = saasHouseDao.getHouseSearchResultDto(houseSearchDto);
        if (houseSearchResultDto == null) {
            logger.error(LogUtils.getCommLog("搜索房源结果不存在"));
            return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
        }

        Responses responses = new Responses(houseSearchResultDto);
        List<HouseSearchResultInfo> searchHouses = houseSearchResultDto.getSearchHouses();

        // 对 isTop 进行排序，值为1的排前
        Collections.sort(searchHouses, new Comparator<HouseSearchResultInfo>() {
            @Override
            public int compare(HouseSearchResultInfo o1, HouseSearchResultInfo o2) {
                // return o1.getIsTop() > o2.getIsTop() ? -1 : 1;
                Integer topF = o1.getIsTop();
                Integer topB = o2.getIsTop();
                return topB.compareTo(topF);
            }
        });

        if (CollectionUtils.isNotEmpty(searchHouses)) {
            for (HouseSearchResultInfo info : searchHouses) {
                // 推荐房源列表页显示小图 ，2017年06月23日19:07:25 jjs
                if (StringUtils.isNotEmpty(info.getPic())) {
                    String pic = info.getPic() + "?x-oss-process=image/resize,h_120";
                    info.setPic(pic);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(searchHouses)) {
            ResponseUtils.fillRow(responses.getMeta(), searchHouses.size(), searchHouses.size());
        }

        return responses;
    }

    public Responses getHouseDetail(HttpServletRequest request) throws Exception {
        
       return getNewHouseInfo(request); 

        //return getHouseInfo(request);
 
    }
    /**
     * 查询房源信息
     *
     * @return
     */
    public Responses getHouseInfo(HttpServletRequest request) throws Exception {
        final String sellId = RequestUtils.getParameterString(request, "sellId");
        final int roomId = RequestUtils.getParameterInt(request, "roomId", 0);
        final String platform = RequestUtils.getParameterString(request, "platform", null);
        final String sessionId = RequestUtils.getParameterString(request, "sid", null);
        long userId = 0;

        //判断用户是否登录		
        if (!StringUtil.isEmpty(platform) && !StringUtil.isEmpty(sessionId)) {
            userId = sessionManager.getUserIdFromSession();
        }
        final long userId2 = userId;

        HouseQueryDto houseDto = houseDao.getHouseQueryDto(sellId, roomId, userId);
        if (houseDto == null) {
            return new Responses(ErrorMsgCode.ERROR_MSG_QUERY_HOUSE_FAIL, "房源不存在");
        }

        String companyIds = configuration.searchAgencyId;
        if (!companyIds.contains(houseDto.getCompanyId())) {
            houseDto.setCompanyName("");
        }
		//查询同小区的房源数量
		HouseSearchDto houseSearchDto = new HouseSearchDto();
		houseSearchDto.setCityId(houseDto.getCityId());
		houseSearchDto.setCommunityName(houseDto.getCommunityName());
		houseSearchDto.setOrderType("price");
		houseSearchDto.setPageSize(500);
		Responses res = searchHouseList(houseSearchDto);
		houseDto.setCommunityHouseCount(res.getMeta().getTotalRows());
        Responses responses = new Responses(houseDto);
        ResponseUtils.fillRow(responses.getMeta(), 1, 1);

        // 添加浏览房源足迹记录(请求参数包括：房源字段必填，房间字段(存在 浏览的是房间，否则浏览的是房源)，用户ID)
        if (userId2 > 0) {
            new Thread(new Runnable() {
                String lockResource = FOOTMARK + String.valueOf(userId2);
                // 获取锁
                boolean lock = lockManager.lock(lockResource);

                @Override
                public void run() {
                    try {
                        if (lock) {
                            // 先查询当前用户的houseId和homeId下是否存在浏览房源足迹记录，如果存在
                            // 执行更新；否则：判断条数是否大于20条，如果不大于 执行插入；否则 执行更新最旧的一条数据
                            List<FootmarkHistory> footmarkHistoryList = new ArrayList<FootmarkHistory>();
                            FootmarkHistory footmarkHistory = null;
                            try {
                                footmarkHistoryList = houseSubDao.getFootmarkHistory(userId2, sellId,
                                        new Long(roomId).intValue());
                                if (CollectionUtils.isNotEmpty(footmarkHistoryList)) {
                                    footmarkHistory = footmarkHistoryList.get(0);
                                }
                            } catch (Exception e) {
                                logger.error(LogUtils.getCommLog("数据解析失败" + e.getMessage()));
                                if (e instanceof BaseException) {
                                    throw (BaseException) e;
                                }
                            }
                            if (footmarkHistory != null) {
                                // 执行更新：更新时间操作
                                try {
                                    houseSubDao.updateFootmarkHistory(footmarkHistory.getId(), userId2, sellId,
                                            new Long(roomId).intValue());
                                } catch (Exception e) {
                                    logger.error(LogUtils.getCommLog("浏览房源足迹记录更新失败" + e.getMessage()));
                                    throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL, "浏览房源足迹记录更新失败");
                                }
                            } else {
                                // 判断条数是否大于20条，如果小于 执行插入；否则 执行更新最旧的一条数据
                                List<FootmarkHistory> footmarkList = houseSubDao.getCountByUserId(userId2);
                                if (CollectionUtils.isEmpty(footmarkList) || footmarkList.size() < 20) {
                                    footmarkHistory = new FootmarkHistory();
                                    footmarkHistory.setUserId(userId2);
                                    footmarkHistory.setSellId(sellId);
                                    footmarkHistory.setRoomId(new Long(roomId).intValue());
                                    footmarkHistory.setState(1);
                                    try {
                                        houseSubDao.saveFootmarkHistory(footmarkHistory);
                                    } catch (Exception e) {
                                        logger.error(LogUtils.getCommLog("浏览房源足迹记录保存失败" + e.getMessage()));
                                        throw new BaseException(ErrorMsgCode.ERROR_FMH_UPDATE_FAIL, "浏览房源足迹记录保存失败");
                                    }
                                } else {
                                    houseSubDao.updateFootmarkHistory(footmarkList.get(0).getId(), userId2, sellId,
                                            new Long(roomId).intValue());
                                    try {
                                    } catch (Exception e) {
                                        logger.error(LogUtils.getCommLog("浏览房源足迹记录更新失败" + e.getMessage()));
                                        throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL, "浏览房源足迹记录更新失败");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(LogUtils.getCommLog("浏览房源足迹记录更新失败" + e.getMessage()));
                        throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL, "浏览房源足迹记录更新失败");
                    } finally {
                        if (lock) {// 释放锁
                            lockManager.unLock(lockResource);
                        }
                    }
                }
            }).start();
        }

        return responses;

    }

    /**
     * 发布房源
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public Responses feedHouse(HttpServletRequest request) {
        HousePublishDto housePublishDto = null;

        try {
            // 验证发布房源参数是否正确
            housePublishDto = getHousePublishDto(request);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
            rethrowBaseException(e);
        }

        String appId = null;
        try {
            appId = RequestUtils.getParameterString(request, "appId");
        } catch (Exception e) {
        }
        // 验证是否具备访问权限
        PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
        if (platformCustomer == null) {
            logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
            return new Responses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
        }
        String source = platformCustomer.getSource();
        housePublishDto.setSource(source);

        String sellId = null;
        try {
            sellId = houseDao.addHousePublishDto(housePublishDto);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
        }

        // 获取Token
        String token = null;
        try {
            token = houseDao.getHouseToken(sellId, 0);
            if (StringUtil.isEmpty(token)) {
                token = MsgUtils.generateNoncestr(32);
                houseDao.addHouseToken(sellId, 0, token);
            }
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("Token获取失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
        }

        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("sellId", sellId);

        try {// 保存原始数据
            saveHouseOriginal(housePublishDto, sellId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("保存原始数据失败" + e.getMessage()));
        }

        responses.setBody(returnMap);
        ResponseMeta meta = responses.getMeta();
        meta.setToken(token);

        return responses;
    }

    /**
     * 保存原始数据
     * 
     * @param housePublishDto
     * @param sellId
     */
    private void saveHouseOriginal(HousePublishDto housePublishDto, String sellId) {

        HouseOriginal original = new HouseOriginal();
        original.setSellId(sellId);
        BeanMapperUtil.copy(housePublishDto, original);// 复制对象
        original.setCreateTime(new Date());
        houseOriginalRepository.save(original);
    }

    /**
     * 发布房间
     *
     * @return
     */
    @Transactional
    public Responses feedRoom(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        RoomPublishDto roomPublishDto = null;

        try {
            roomPublishDto = getRoomPublishDto(request);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房间参数解析失败" + e.getMessage()));
            rethrowBaseException(e);
        }

        Long roomId = null;
        try {
            roomId = houseDao.addRoomPublishDto(roomPublishDto);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房间保存失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_ROOM_FAIL, "发布房间失败");
        }

        Responses responses = new Responses();
        Map<String, Long> returnMap = new HashMap<String, Long>();
        returnMap.put("roomId", roomId);
        responses.setBody(returnMap);

        try {
            saveRoomOriginal(roomPublishDto, roomId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("保存房间原始信息失败"));
        }

        return responses;
    }

    /**
     * 保存房间原始信息
     * 
     * @param roomPublishDto
     * @param roomId
     */
    private void saveRoomOriginal(RoomPublishDto roomPublishDto, Long roomId) {
        RoomOriginal ro = new RoomOriginal();
        ro.setCreateTime(new Date());
        ro.setRoomId(roomId);
        BeanMapperUtil.copy(roomPublishDto, ro);// 复制对象
        roomOriginalRepository.save(ro);
    }

    /**
     * 修改房源
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public Responses updateHouse(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        String sellId = RequestUtils.getParameterString(request, "sellId");

        HousePublishDto housePublishDto = null;

        try {
            housePublishDto = getHousePublishDto(request);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房源参数解析失败" + e.getMessage()));
            rethrowBaseException(e);
        }

        String appId = null;
        try {
            appId = RequestUtils.getParameterString(request, "appId");
        } catch (Exception e) {
        }
        PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
        if (platformCustomer == null) {
            logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
            return new Responses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
        }
        String source = platformCustomer.getSource();
        housePublishDto.setSource(source);

        try {
            houseDao.updateHousePublishDto(housePublishDto, sellId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房源失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_MODIFY_HOUSE_FAIL, "更新房源失败");
        }

        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("sellId", sellId);
        responses.setBody(returnMap);

        try {// 更新原始数据
            updateHouseOriginal(housePublishDto, sellId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房源原始数据失败 " + e.getMessage()));
        }

        return responses;
    }

    /**
     * 更新原始数据
     * 
     * @param housePublishDto
     * @param sellId
     */
    private void updateHouseOriginal(HousePublishDto housePublishDto, String sellId) {

        HouseOriginal original = houseOriginalRepository.findBySellId(sellId);
        if (original == null) {
            logger.error(LogUtils.getCommLog("未找到房源原始数据 "));
            return;
        }
        BeanMapperUtil.copy(housePublishDto, original);// 复制对象
        original.setUpdateTime(new Date());
        houseOriginalRepository.save(original);
    }

    /**
     * 更新房间
     *
     * @return
     */
    @Transactional
    public Responses updateRoom(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        long roomId = RequestUtils.getParameterLong(request, "roomId");

        RoomPublishDto roomPublishDto = null;

        try {
            roomPublishDto = getRoomPublishDto(request);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("房间参数解析失败" + e.getMessage()));
            rethrowBaseException(e);
        }

        if (roomPublishDto == null) {
            return new Responses(ErrorMsgCode.ERROR_MSG_MODIFY_ROOM_FAIL, "房间参数解析失败");
        }

        try {
            houseDao.updateRoomPublishDto(roomPublishDto, roomId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房间失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_MODIFY_ROOM_FAIL, "更新房间失败");
        }

        Responses responses = new Responses();
        Map<String, Long> returnMap = new HashMap<String, Long>();
        returnMap.put("roomId", roomId);
        responses.setBody(returnMap);

        try {//更新房间原始信息
            updateRoomOriginal(roomPublishDto, roomId);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房间原始信息失败 " + e));
        }
        return responses;
    }

    /**
     * 更新房间原始数据
     * 
     * @param housePublishDto
     * @param sellId
     */
    private void updateRoomOriginal(RoomPublishDto roomPublishDto, Long roomId) {

        RoomOriginal original = roomOriginalRepository.findByRoomId(roomId);
        if (original == null) {
            logger.error(LogUtils.getCommLog("未找到房间原始数据 "));
            return;
        }
        BeanMapperUtil.copy(roomPublishDto, original);// 复制对象
        original.setUpdateTime(new Date());
        roomOriginalRepository.save(original);
    }

    /**
     * 修改房源状态
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public Responses updateHouseStatus(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        String sellId = RequestUtils.getParameterString(request, "sellId");

        String statusKey = "status";
        int publishStatus = RequestUtils.getParameterInt(request, statusKey);
        checkStatus(publishStatus, statusKey);

        int status = HouseUtil.getStatus(publishStatus);

        try {
            houseDao.updateHouseStatus(sellId, status);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房源状态失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_MODIFY_HOUSE_FAIL, "更新房源状态失败");
        }

        Responses responses = new Responses();
        return responses;
    }

    /**
     * 更新房间状态
     *
     * @return
     */
    @Transactional
    public Responses updateRoomStatus(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        long roomId = RequestUtils.getParameterLong(request, "roomId");

        String statusKey = "status";
        int publishStatus = RequestUtils.getParameterInt(request, statusKey);
        checkStatus(publishStatus, statusKey);

        int status = HouseUtil.getStatus(publishStatus);

        try {
            houseDao.updateRoomStatus(roomId, status);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新房间状态失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_MODIFY_ROOM_FAIL, "更新房间状态失败");
        }

        Responses responses = new Responses();
        return responses;
    }

    /**
     * 删除房源
     *
     * @return
     */
    @Transactional
    public Responses delHouse(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        String sellId = RequestUtils.getParameterString(request, "sellId");

        houseDao.removeHouse(sellId);

        // 删除Token
        try {
            houseDao.removeHouseToken(sellId, 0);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("删除Token失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_REMOVE_HOUSE_FAIL, "删除房源失败");
        }

        try {//删除原始房源数据
            houseOriginalRepository.setIsDeleteBySellId(sellId, 1);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("删除原始房源数据失败 " + e.getMessage()));
        }

        return new Responses();
    }

    /**
     * 删除房间
     *
     * @return
     */
    @Transactional
    public Responses delRoom(HttpServletRequest request) throws Exception {

        // 校验Token
        checkToken(request);

        long roomId = RequestUtils.getParameterLong(request, "roomId");
        String sellId = RequestUtils.getParameterString(request, "sellId");

        houseDao.removeRoom(sellId,roomId);

        try {//删除原始房间数据
            roomOriginalRepository.setIsDeleteByRoomId(roomId, 1);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("删除原始房间数据 " + e.getMessage()));
        }

        return new Responses();
    }

    /**
     * 获取投诉列表
     *
     * @return
     */
    public Responses getComplaintList(HttpServletRequest request) throws Exception {

        String sellId = RequestUtils.getParameterString(request, "sellId");
        String uid = RequestUtils.getParameterString(request, "uid");
        long roomId = RequestUtils.getParameterLong(request, "roomId", 0); // roomId可选

        ComplaintQueryDto complaintQueryDto = houseDao.getComplaintQueryDto(uid, sellId, roomId);

        Responses responses = new Responses(complaintQueryDto);
        List<ComplaintInfo> complaintInfoList = complaintQueryDto.getComplaints();
        if (CollectionUtils.isNotEmpty(complaintInfoList)) {
            ResponseUtils.fillRow(responses.getMeta(), complaintInfoList.size(), complaintInfoList.size());
        }

        return responses;
    }

    /**
     * 查询是否已投诉
     *
     * @return
     */
    public Responses checkComplaint(HttpServletRequest request) throws Exception {

        String sellId = RequestUtils.getParameterString(request, "sellId");
        String uid = RequestUtils.getParameterString(request, "uid");
        long roomId = RequestUtils.getParameterLong(request, "roomId", 0); // roomId可选

        Integer complaintExist = houseDao.getComplaintExistValue(uid, sellId, roomId);

        Map<String, Integer> returnMap = new HashMap<String, Integer>();
        returnMap.put("complaintExist", complaintExist);

        Responses responses = new Responses(returnMap);

        return responses;
    }

    /**
     * 保存投诉列表
     *
     * @return
     */
    @Transactional
    public Responses saveComplaint(HttpServletRequest request) throws Exception {

        ComplaintSaveDto complaintSaveDto = null;

        try {
            complaintSaveDto = getComplaintSaveDto(request);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("投诉参数解析失败" + e.getMessage()));
            rethrowBaseException(e);
        }

        if (complaintSaveDto == null) {
            return new Responses(ErrorMsgCode.ERROR_MSG_ADD_COMPLAINT_FAIL, "投诉参数解析失败");
        }

        Integer complaintExist = houseDao.getComplaintExistValue(complaintSaveDto.getUid(),
                complaintSaveDto.getSellId(), complaintSaveDto.getRoomId());

        if (complaintExist > 0) {
            return new Responses(ErrorMsgCode.ERROR_MSG_ADD_COMPLAINT_FAIL, "已投诉过");
        }

        Long complaintId = null;
        try {
            complaintId = houseDao.addCompliant(complaintSaveDto);
        } catch (Exception e) {
            return new Responses(ErrorMsgCode.ERROR_MSG_ADD_COMPLAINT_FAIL, "投诉保存失败");
        }

        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("complaintId", String.valueOf(complaintId));

        responses.setBody(returnMap);

        return responses;
    }

    /**
     * 获取公寓列表
     *
     * @return
     */
    public Responses getHotApartment(HttpServletRequest request) throws Exception {

        long cityId = RequestUtils.getParameterLong(request, "cityId");

        ApartmentQueryDto apartmentQueryDto = houseDao.getApartmentQueryDto(cityId);

        Responses responses = new Responses(apartmentQueryDto);
        List<ApartmentInfo> apartmentInfoList = apartmentQueryDto.getApartments();
        if (CollectionUtils.isNotEmpty(apartmentInfoList)) {
            ResponseUtils.fillRow(responses.getMeta(), apartmentInfoList.size(), apartmentInfoList.size());
        }

        return responses;
    }

    /**
     * 获取推荐房源
     *
     * @return
     */
    public Responses getRecommendHouse(HttpServletRequest request) throws Exception {

        long cityId = RequestUtils.getParameterLong(request, "cityId");

        HouseRecommendQueryDto houseRecommendQueryDto = houseDao.getHouseRecommendQueryDto(cityId);

        Responses responses = new Responses(houseRecommendQueryDto);
        List<HouseRecommendInfo> houseRecommendInfos = houseRecommendQueryDto.getHouseRecommends();
        if (CollectionUtils.isNotEmpty(houseRecommendInfos)) {
            ResponseUtils.fillRow(responses.getMeta(), houseRecommendInfos.size(), houseRecommendInfos.size());
        }

        return responses;
    }

    public Responses staticPhoneCall(String phone, String sellId, long roomId) {
        PhoneCallRecord record = new PhoneCallRecord();
        record.setPhone(phone);
        record.setSellId(sellId);
        record.setRoomId(roomId);
        record.setCreateTime(new Date());
        phoneCallRecordRepository.save(record);
        Responses responses = new Responses();
        return responses;
    }

    /**
     * 添加热门公寓
     *
     * @return
     */
    @Transactional
    public Responses addHotApartment(HttpServletRequest request) throws Exception {

        Apartment apartment = getApartment(request);
        apartment.setStatus(Constants.ApartmentStatus.STATUS_HOT);

        Long apartmentId = null;
        try {
            apartmentId = houseDao.addApartment(apartment);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("公寓保存失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_APARTMENT_FAIL, "公寓保存失败");
        }

        Responses responses = new Responses();
        Map<String, Long> returnMap = new HashMap<String, Long>();
        returnMap.put("apartmentId", apartmentId);

        responses.setBody(returnMap);

        return responses;
    }

    /**
     * 获取公寓信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    private Apartment getApartment(HttpServletRequest request) throws Exception {

        Apartment apartment = new Apartment();

        String cityIdKey = "cityId";
        int cityId = RequestUtils.getParameterInt(request, cityIdKey);
        checkNonNegativeNumber(cityId, cityIdKey);
        apartment.setCityId(cityId);

        String typeKey = "type";
        int type = RequestUtils.getParameterInt(request, typeKey);
        checkApartmentType(type, typeKey);
        apartment.setType(type);

        String nameKey = "name";
        String name = RequestUtils.getParameterString(request, nameKey);
        apartment.setName(name);

        String imgKey = "img";
        String img = RequestUtils.getParameterString(request, imgKey);
        apartment.setPicRootPath(img);

        apartment.setPicWebPath(StringUtil.EMPTY);

        Date date = new Date();
        apartment.setCreateTime(date);
        apartment.setUpdateTime(date);

        apartment.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);

        return apartment;
    }

    /**
     * 校验公寓类型
     *
     * @param apartmentType
     * @param keyName
     */
    private void checkApartmentType(int apartmentType, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.ApartmentStatus.TYPE_APARTMENT);
        validKeys.add(Constants.ApartmentStatus.TYPE_AREA);
        checkIfValid(apartmentType, validKeys, keyName);
    }

    /**
     * 删除热门公寓
     *
     * @return
     */
    @Transactional
    public Responses delHotApartment(HttpServletRequest request) throws Exception {

        long apartmentId = RequestUtils.getParameterLong(request, "apartmentId");

        houseDao.removeApartment(apartmentId);

        Responses responses = new Responses();
        return responses;
    }

    /**
     * 清除城市所有热门公寓
     *
     * @return
     */
    @Transactional
    public Responses clearHotApartment(HttpServletRequest request) throws Exception {

        long cityId = RequestUtils.getParameterLong(request, "cityId");

        houseDao.clearApartment(cityId);

        Responses responses = new Responses();
        return responses;
    }

    /**
     * 添加推荐房源
     *
     * @return
     */
    @Transactional
    public Responses addRecommendHouse(HttpServletRequest request) throws Exception {

        HouseRecommend houseRecommend = getHouseRecommend(request);

        String sellId = houseRecommend.getSellId();
        long roomId = houseRecommend.getRoomId();
        if (roomId == 0) {
            // 检查房源是否存在
            boolean houseExist = houseDao.isHouseExist(sellId);
            if (!houseExist) {
                logger.error(LogUtils.getCommLog("houseBase查询失败, sellId:" + sellId));
                throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_RECOMMEND_HOUSE_FAIL, "房源不存在");
            }

        } else {
            // 检查房间是否存在
            boolean roomExist = houseDao.isRoomExist(sellId, roomId);
            if (!roomExist) {
                logger.error(LogUtils.getCommLog("roomBase查询失败, sellId:" + sellId + ", roomId:" + roomId));
                throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_RECOMMEND_HOUSE_FAIL, "房间不存在");
            }
        }

        Long recommendId = null;
        try {
            recommendId = houseDao.addHouseRecommend(houseRecommend);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("推荐房源保存失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_RECOMMEND_HOUSE_FAIL, "推荐房源保存失败");
        }

        Responses responses = new Responses();
        Map<String, Long> returnMap = new HashMap<String, Long>();
        returnMap.put("recommendId", recommendId);

        responses.setBody(returnMap);

        return responses;
    }

    /**
     * 获取推荐房源信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    private HouseRecommend getHouseRecommend(HttpServletRequest request) throws Exception {

        HouseRecommend houseRecommend = new HouseRecommend();

        String cityIdKey = "cityId";
        int cityId = RequestUtils.getParameterInt(request, cityIdKey);
        checkNonNegativeNumber(cityId, cityIdKey);
        houseRecommend.setCityId(cityId);

        String sellIdKey = "sellId";
        String sellId = RequestUtils.getParameterString(request, sellIdKey);
        houseRecommend.setSellId(sellId);

        String roomIdKey = "roomId";
        long roomId = RequestUtils.getParameterLong(request, roomIdKey, 0);
        houseRecommend.setRoomId(roomId);

        String imgKey = "img";
        String img = RequestUtils.getParameterString(request, imgKey);
        houseRecommend.setPicRootPath(img);

        houseRecommend.setPicWebPath(StringUtil.EMPTY);

        Date date = new Date();
        houseRecommend.setCreateTime(date);
        houseRecommend.setUpdateTime(date);

        houseRecommend.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);

        return houseRecommend;
    }

    /**
     * 删除推荐房源
     *
     * @return
     */
    @Transactional
    public Responses delRecommendHouse(HttpServletRequest request) throws Exception {

        long recommendId = RequestUtils.getParameterLong(request, "recommendId");

        houseDao.removeHouseRecommend(recommendId);

        Responses responses = new Responses();
        return responses;
    }

    /**
     * 清除城市所有推荐房源
     *
     * @return
     */
    @Transactional
    public Responses clearRecommendHouse(HttpServletRequest request) throws Exception {

        long cityId = RequestUtils.getParameterLong(request, "cityId");

        houseDao.clearHouseRecommend(cityId);

        Responses responses = new Responses();
        return responses;
    }

    private HouseSearchDto getHouseSearchDto(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

        HouseSearchDto houseSearchDto = new HouseSearchDto();

        int appId = RequestUtils.getParameterInt(request, "appId");
        houseSearchDto.setAppId(appId);

        // 店铺页中介公司ID
        String companyId = RequestUtils.getParameterString(request, "companyId", StringUtil.EMPTY);
        houseSearchDto.setCompanyId(companyId);

        // 只有cityId是必选 ！！！ 去掉cityId必传属性
        long cityId = RequestUtils.getParameterLong(request, "cityId", 0);
        if (cityId != 0) {
            houseSearchDto.setCityId(cityId);
        }

        String keyword = RequestUtils.getParameterString(request, "q", StringUtil.EMPTY);
        houseSearchDto.setKeyword(keyword);

        long disId = RequestUtils.getParameterLong(request, "disId", 0);
        houseSearchDto.setDistrictId(disId);

        long bizId = RequestUtils.getParameterLong(request, "bizId", 0);
        houseSearchDto.setBizId(bizId);

        long lineId = RequestUtils.getParameterLong(request, "lineId", 0);
        houseSearchDto.setLineId(lineId);

        long stationId = RequestUtils.getParameterLong(request, "stationId", 0);
        houseSearchDto.setStationId(stationId);

        String price = RequestUtils.getParameterString(request, "price", StringUtil.EMPTY);
        checkRegionNumber(price, "price");
        houseSearchDto.setPrice(price);

        int orientation = RequestUtils.getParameterInt(request, "orientation", 0);
        if (orientation != 0) {
            checkOrientation(orientation, "orientation");
        }
        houseSearchDto.setOrientation(orientation);

        String area = RequestUtils.getParameterString(request, "area", StringUtil.EMPTY);
        checkRegionNumber(area, "area");
        houseSearchDto.setArea(area);

        String location = RequestUtils.getParameterString(request, "location", StringUtil.EMPTY);
        checkPosition(location, "location");
        houseSearchDto.setLocation(location);

        String distance = RequestUtils.getParameterString(request, "distance", StringUtil.EMPTY);
        checkRegionNumber(distance, "distance");
        houseSearchDto.setDistance(distance);

        String orderType = RequestUtils.getParameterString(request, "orderType", StringUtil.EMPTY);
        checkOrderType(orderType, "orderType");
        houseSearchDto.setOrderType(orderType);

        String order = RequestUtils.getParameterString(request, "order", StringUtil.EMPTY);
        checkOrder(order, "order");
        houseSearchDto.setOrder(order);

        int pageNum = RequestUtils.getParameterInt(request, "pageNum", 1);
        checkNonNegativeNumber(pageNum, "pageNum");
        houseSearchDto.setPageNum(pageNum);

        int pageSize = RequestUtils.getParameterInt(request, "pageSize", 10);
        checkNonNegativeNumber(pageSize, "pageSize");
        checkPageSize(pageSize, "pageSize");
        houseSearchDto.setPageSize(pageSize);

        // 整租/分租,可选，默认全部
        int entireRent = RequestUtils.getParameterInt(request, "entireRent", Constants.HouseDetail.RENT_TYPE_ALL);
        checkEntireRent(entireRent, "entireRent");
        houseSearchDto.setEntireRent(entireRent);

        String bedroomNums = RequestUtils.getParameterString(request, "bedroomNums", StringUtil.EMPTY);
        checkRegionNumber(bedroomNums, "bedroomNums");
        houseSearchDto.setBedroomNum(bedroomNums);

        String houseTag = RequestUtils.getParameterString(request, "houseTag", StringUtil.EMPTY);
        checkSeparatedNumber(houseTag, "houseTag");
        houseSearchDto.setHouseTag(houseTag);

        String orientationStr = RequestUtils.getParameterString(request, "orientationStr", StringUtil.EMPTY);
        checkSeparatedNumber(orientationStr, "orientationStr");
        houseSearchDto.setOrientationStr(orientationStr);

        String eBedRoomNums = RequestUtils.getParameterString(request, "eBedRoomNums", "0");
        String sBedRoomNums = RequestUtils.getParameterString(request, "sBedRoomNums", "0");
        if (!"0".equals(eBedRoomNums) && !"0".equals(sBedRoomNums)) {
            houseSearchDto.seteBedRoomNums(eBedRoomNums);
            houseSearchDto.setsBedRoomNums(sBedRoomNums);
            houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ALL);
        } else if (!"0".equals(eBedRoomNums)) {
            houseSearchDto.seteBedRoomNums(eBedRoomNums);
            houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
        } else if (!"0".equals(sBedRoomNums)) {
            houseSearchDto.setsBedRoomNums(sBedRoomNums);
            houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
        }

        String cBedRoomNums = RequestUtils.getParameterString(request, "cBedRoomNums", StringUtil.EMPTY);
        houseSearchDto.setcBedRoomNums(cBedRoomNums);

        String payType = RequestUtils.getParameterString(request, "payType", StringUtil.EMPTY);
        houseSearchDto.setPayType(payType);

        String sellerId = RequestUtils.getParameterString(request, "sellerId", StringUtil.EMPTY);
        if (StringUtil.isNotBlank(sellerId)) {
            houseSearchDto.setSellerId(sellerId);
        }

        String communityName = RequestUtils.getParameterString(request, "communityName", StringUtil.EMPTY);
        if (StringUtil.isNotBlank(communityName)) {
            houseSearchDto.setCommunityName(communityName);
        }

        int roomerId = RequestUtils.getParameterInt(request, "roomerId", 0);
        houseSearchDto.setRoomerId(roomerId);

        String companyType = RequestUtils.getParameterString(request, "companyType", StringUtil.EMPTY);
        houseSearchDto.setCompanyType(companyType);
        
        //获取用户ID
        long userId = getLoginUserId();
        houseSearchDto.setUserId(userId);
        

        return houseSearchDto;

    }

    private HouseSearchDto getSaasHouseSearchDto(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

        HouseSearchDto houseSearchDto = new HouseSearchDto();

        // 店铺页中介公司ID
        /*
         * String companyId = RequestUtils.getParameterString(request,
         * "agencyId"); houseSearchDto.setCompanyId(companyId);
         */
        // 只有cityId是必选
        // long cityId = RequestUtils.getParameterLong(request, "cityId");
        // houseSearchDto.setCityId(cityId);

        String keyword = RequestUtils.getParameterString(request, "q", StringUtil.EMPTY);
        houseSearchDto.setKeyword(keyword);

        long disId = RequestUtils.getParameterLong(request, "disId", 0);
        houseSearchDto.setDistrictId(disId);

        long bizId = RequestUtils.getParameterLong(request, "bizId", 0);
        houseSearchDto.setBizId(bizId);

        long lineId = RequestUtils.getParameterLong(request, "lineId", 0);
        houseSearchDto.setLineId(lineId);

        long stationId = RequestUtils.getParameterLong(request, "stationId", 0);
        houseSearchDto.setStationId(stationId);

        String price = RequestUtils.getParameterString(request, "price", StringUtil.EMPTY);
        checkRegionNumber(price, "price");
        houseSearchDto.setPrice(price);

        int orientation = RequestUtils.getParameterInt(request, "orientation", 0);
        if (orientation != 0) {
            checkOrientation(orientation, "orientation");
        }
        houseSearchDto.setOrientation(orientation);

        String area = RequestUtils.getParameterString(request, "area", StringUtil.EMPTY);
        checkRegionNumber(area, "area");
        houseSearchDto.setArea(area);

        // 整租/分租,可选，默认全部
        int entireRent = RequestUtils.getParameterInt(request, "entireRent", Constants.HouseDetail.RENT_TYPE_ALL);
        checkEntireRent(entireRent, "entireRent");
        houseSearchDto.setEntireRent(entireRent);

        String location = RequestUtils.getParameterString(request, "location", StringUtil.EMPTY);
        checkPosition(location, "location");
        houseSearchDto.setLocation(location);

        String distance = RequestUtils.getParameterString(request, "distance", StringUtil.EMPTY);
        checkRegionNumber(distance, "distance");
        houseSearchDto.setDistance(distance);

        String orderType = RequestUtils.getParameterString(request, "orderType", StringUtil.EMPTY);
        checkOrderType(orderType, "orderType");
        houseSearchDto.setOrderType(orderType);

        String order = RequestUtils.getParameterString(request, "order", StringUtil.EMPTY);
        checkOrder(order, "order");
        houseSearchDto.setOrder(order);

        int pageNum = RequestUtils.getParameterInt(request, "pageNum", 1);
        checkNonNegativeNumber(pageNum, "pageNum");
        houseSearchDto.setPageNum(pageNum);

        int pageSize = RequestUtils.getParameterInt(request, "pageSize", 10);
        checkNonNegativeNumber(pageSize, "pageSize");
        houseSearchDto.setPageSize(pageSize);

        String bedroomNums = RequestUtils.getParameterString(request, "bedroomNums", StringUtil.EMPTY);
        checkRegionNumber(bedroomNums, "bedroomNums");
        houseSearchDto.setBedroomNum(bedroomNums);

        String houseTag = RequestUtils.getParameterString(request, "houseTag", StringUtil.EMPTY);
        checkSeparatedNumber(houseTag, "houseTag");
        houseSearchDto.setHouseTag(houseTag);

        String isTop = RequestUtils.getParameterString(request, "isTop", StringUtil.EMPTY);
        houseSearchDto.setIsTop(isTop);

        int releaseTypeId = RequestUtils.getParameterInt(request, "releaseTypeId", 0);
        if (releaseTypeId != 0) {
            houseSearchDto.setPubType(releaseTypeId);
        }
        return houseSearchDto;

    }

    /**
     * 校验区域字段
     *
     * @param regionNumber
     * @param keyName
     */
    private void checkRegionNumber(String regionNumber, String keyName) {
        if (StringUtil.isNotBlank(regionNumber)) {
            boolean valid = RulesVerifyUtil.verifyNumberRegion(regionNumber);
            if (!valid) {
                throw new InvalidParameterException("参数格式不正确:" + keyName);
            }
        }
    }

    /**
     * 校验坐标字段
     *
     * @param position
     * @param keyName
     */
    private void checkPosition(String position, String keyName) {
        if (StringUtil.isNotBlank(position)) {
            boolean valid = RulesVerifyUtil.verifyPosition(position);
            if (!valid) {
                throw new InvalidParameterException("参数格式不正确:" + keyName);
            }
        }
    }

    /**
     * 校验排序类型字段
     *
     * @param regionNumber
     * @param keyName
     */
    private void checkOrderType(String orderType, String keyName) {
        if (StringUtil.isNotBlank(orderType)) {
            if (!Constants.Search.containsOrderType(orderType)) {
                throw new InvalidParameterException("参数异常:" + keyName);
            }
        }
    }

    /**
     * 校验排序字段
     *
     * @param regionNumber
     * @param keyName
     */
    private void checkOrder(String order, String keyName) {
        if (StringUtil.isNotBlank(order)) {
            if (!Constants.Search.containsOrder(order)) {
                throw new InvalidParameterException("参数异常:" + keyName);
            }
        }
    }

    /**
     * 校验逗号分隔数字
     *
     * @param regionNumber
     * @param keyName
     */
    private void checkSeparatedNumber(String regionNumber, String keyName) {
        if (StringUtil.isNotBlank(regionNumber)) {
            boolean valid = RulesVerifyUtil.verifySeparatedNumber(regionNumber);
            if (!valid) {
                throw new InvalidParameterException("参数格式不正确:" + keyName);
            }
        }
    }

    /**
     * 获取发布房源DTO
     *
     * @param request
     * @return
     * @throws Exception
     */
    private HousePublishDto getHousePublishDto(HttpServletRequest request) throws Exception {

        HousePublishDto housePublishDto = new HousePublishDto();

        String positionXKey = "positionX";
        String positionX = RequestUtils.getParameterString(request, positionXKey);
        checkPositionValue(positionX, positionXKey);
        checkLatitude(positionX, positionXKey);
        housePublishDto.setPositionX(positionX);

        String positionYKey = "positionY";
        String positionY = RequestUtils.getParameterString(request, positionYKey);
        checkPositionValue(positionY, positionYKey);
        checkLongitude(positionY, positionYKey);
        housePublishDto.setPositionY(positionY);

        String communityNameKey = "communityName";
        String communityName = RequestUtils.getParameterString(request, communityNameKey);
        checkStringMaxLength(communityName, communityNameKey, Constants.HouseDetail.HOUSE_COMMUNITY_NAME_LENGTH_MAX);
        housePublishDto.setCommunityName(communityName);

        String priceKey = "price";
        int price = RequestUtils.getParameterInt(request, priceKey);
        checkNonNegativeNumber(price, priceKey);
        housePublishDto.setPrice(price);

        String bonusKey = "bonus";
        int bonus = RequestUtils.getParameterInt(request, bonusKey);
        checkNonNegativeNumber(bonus, bonusKey);
        housePublishDto.setBonus(bonus);

        String depositKey = "deposit";
        int deposit = RequestUtils.getParameterInt(request, depositKey);
        checkNonNegativeNumber(deposit, depositKey);
        housePublishDto.setDeposit(deposit);

        String hasKeyKey = "hasKey";
        int hasKey = RequestUtils.getParameterInt(request, hasKeyKey);
        checkHaskey(hasKey, hasKeyKey);
        housePublishDto.setHasKey(hasKey);

        // 中介公司ID saas 必填，其他选填
        String companyIdKey = "companyId";
        String companyId = RequestUtils.getParameterString(request, companyIdKey, StringUtil.EMPTY);
        checkStringMaxLength(companyId, companyIdKey, Constants.HouseBase.HOUSE_COMPANY_ID_LENGTH_MAX);
        housePublishDto.setCompanyId(companyId);

        String companyNameKey = "companyName";
        String companyName = RequestUtils.getParameterString(request, companyNameKey, StringUtil.EMPTY);
        checkStringMaxLength(companyName, companyNameKey, Constants.HouseBase.HOUSE_COMPANY_NAME_LENGTH_MAX);
        housePublishDto.setCompanyName(companyName);
        // 由于来源可能是字符串，取消经纪人ID必填项
        // int agencyId = RequestUtils.getParameterInt(request, "agencyId");
        // housePublishDto.setAgencyId(agencyId);

        String agencyPhone = RequestUtils.getParameterString(request, "agencyPhone");
        checkPhone(agencyPhone, "agencyPhone");
        housePublishDto.setAgencyPhone(agencyPhone);

        String agencyNameKey = "agencyName";
        String agencyName = RequestUtils.getParameterString(request, agencyNameKey);
        checkStringMaxLength(agencyName, agencyNameKey, Constants.HouseBase.HOUSE_AGENCY_NAME_LENGTH_MAX);
        housePublishDto.setAgencyName(agencyName);

        String agencyIntroduceKey = "agencyIntroduce";
        String agencyIntroduce = RequestUtils.getParameterString(request, agencyIntroduceKey, StringUtil.EMPTY);
        checkStringMaxLength(agencyIntroduce, agencyIntroduceKey,
                Constants.HouseBase.HOUSE_AGENCY_INTRODUCE_LENGTH_MAX);
        housePublishDto.setAgencyIntroduce(agencyIntroduce);

        int agencyGender = RequestUtils.getParameterInt(request, "agencyGender", 0);
        checkGender(agencyGender, "agencyGender");
        housePublishDto.setAgencyGender(agencyGender);

        String agencyAvatarKey = "agencyAvatar";
        String agencyAvatar = RequestUtils.getParameterString(request, agencyAvatarKey, StringUtil.EMPTY);
        checkStringMaxLength(agencyAvatar, agencyAvatarKey, Constants.HouseBase.HOUSE_AGENCY_AVATAR_LENGTH_MAX);
        housePublishDto.setAgencyAvatar(agencyAvatar);

        String statusKey = "status";
        int status = RequestUtils.getParameterInt(request, statusKey);
        checkStatus(status, statusKey);
        housePublishDto.setStatus(status);

        String bedroomNumKey = "bedroomNum";
        int bedroomNum = RequestUtils.getParameterInt(request, bedroomNumKey);
        checkNonNegativeNumber(bedroomNum, bedroomNumKey);
        housePublishDto.setBedroomNum(bedroomNum);

        String livingroomNumKey = "livingroomNum";
        int livingroomNum = RequestUtils.getParameterInt(request, livingroomNumKey);
        checkNonNegativeNumber(livingroomNum, livingroomNumKey);
        housePublishDto.setLivingroomNum(livingroomNum);

        String kitchenNumKey = "kitchenNum";
        int kitchenNum = RequestUtils.getParameterInt(request, kitchenNumKey);
        checkNonNegativeNumber(kitchenNum, kitchenNumKey);
        housePublishDto.setKitchenNum(kitchenNum);

        String toiletNumKey = "toiletNum";
        int toiletNum = RequestUtils.getParameterInt(request, toiletNumKey);
        checkNonNegativeNumber(toiletNum, toiletNumKey);
        housePublishDto.setToiletNum(toiletNum);

        String balconyNumKey = "balconyNum";
        int balconyNum = RequestUtils.getParameterInt(request, balconyNumKey);
        checkNonNegativeNumber(balconyNum, balconyNumKey);
        housePublishDto.setBalconyNum(balconyNum);

        String buildingNoKey = "buildingNo";
        String buildingNo = RequestUtils.getParameterString(request, buildingNoKey, StringUtil.EMPTY);
        checkStringMaxLength(buildingNo, buildingNoKey, Constants.HouseDetail.HOUSE_BUILDING_NO_LENGTH_MAX);
        housePublishDto.setBuildingNo(buildingNo);

        String unitNoKey = "unitNo";
        String unitNo = RequestUtils.getParameterString(request, unitNoKey, StringUtil.EMPTY);
        checkStringMaxLength(unitNo, unitNoKey, Constants.HouseDetail.HOUSE_UNIT_NO_LENGTH_MAX);
        housePublishDto.setUnitNo(unitNo);

        String houseNoKey = "houseNo";
        String houseNo = RequestUtils.getParameterString(request, houseNoKey, StringUtil.EMPTY);
        checkStringMaxLength(houseNo, houseNoKey, Constants.HouseDetail.HOUSE_HOUSE_NO_LENGTH_MAX);
        housePublishDto.setHouseNo(houseNo);

        String buildingNameKey = "buildingName";
        String buildingName = RequestUtils.getParameterString(request, buildingNameKey, StringUtil.EMPTY);
        checkStringMaxLength(buildingName, buildingNameKey, Constants.HouseDetail.HOUSE_BUILDING_NAME_LENGTH_MAX);
        housePublishDto.setBuildingName(buildingName);

        String areaKey = "area";
        float area = RequestUtils.getParameterFloat(request, areaKey);
        checkNonNegativeNumber(area, areaKey);
        //checkArea(area, areaKey, Constants.HouseDetail.HOUSE_AREA_MIN);
        housePublishDto.setArea(area);

        String floorNoKey = "floorNo";
        String flowNo = RequestUtils.getParameterString(request, floorNoKey);
        checkStringMaxLength(flowNo, floorNoKey, Constants.HouseDetail.HOUSE_FLOW_NO_LENGTH_MAX);
        housePublishDto.setFlowNo(flowNo);

        String floorTotalKey = "floorTotal";
        String flowTotal = RequestUtils.getParameterString(request, floorTotalKey);
        checkStringMaxLength(flowTotal, floorTotalKey, Constants.HouseDetail.HOUSE_FLOW_TOTAL_LENGTH_MAX);
        housePublishDto.setFlowTotal(flowTotal);

        String orientationKey = "orientation";
        int orientation = RequestUtils.getParameterInt(request, orientationKey);
        checkOrientation(orientation, orientationKey);
        housePublishDto.setOrientation(orientation);

        String buildingTypeKey = "buildingType";
        int buildingType = RequestUtils.getParameterInt(request, buildingTypeKey);
        checkBuildingType(buildingType, buildingTypeKey);
        housePublishDto.setBuildingType(buildingType);

        String fitmentTypeKey = "fitmentType";
        int fitmentType = RequestUtils.getParameterInt(request, fitmentTypeKey);
        checkDecoration(fitmentType, fitmentTypeKey);
        housePublishDto.setFitmentType(fitmentType);

        String buildingYearKey = "buildingYear";
        String buildingYear = RequestUtils.getParameterString(request, buildingYearKey, StringUtil.EMPTY);
        checkBuildingYear(buildingYear, buildingYearKey);
        housePublishDto.setBuildingYear(buildingYear);

        // String toiletKey = "toilet";
        // int toilet = RequestUtils.getParameterInt(request, toiletKey,
        // Constants.HouseBase.NOT_INDEPENDENT);
        // checkIndependent(toilet, toiletKey);
        // housePublishDto.setToilet(toilet);
        //
        // String balconyKey = "balcony";
        // int balcony = RequestUtils.getParameterInt(request, balconyKey,
        // Constants.HouseBase.NOT_INDEPENDENT);
        // checkIndependent(balcony, balconyKey);
        // housePublishDto.setBalcony(balcony);
        //
        // String insuranceKey = "insurance";
        // int insurance = RequestUtils.getParameterInt(request, insuranceKey,
        // Constants.HouseBase.INSURANCE_NOT_EXIST);
        // checkInsurance(insurance, insuranceKey);
        // housePublishDto.setInsurance(insurance);

        String checkInKey = "checkIn";
        Date checkInDate = RequestUtils.getParameterDate(request, checkInKey);
        //checkCheckIn(checkInDate, checkInKey);
        housePublishDto.setCheckInTime(checkInDate);

        String depositMonthKey = "depositMonth";
        int depositMonth = RequestUtils.getParameterInt(request, depositMonthKey);
        checkNonNegativeNumber(depositMonth, depositMonthKey);
        housePublishDto.setDepositMonth(depositMonth);

        String periodMonthKey = "periodMonth";
        int periodMonth = RequestUtils.getParameterInt(request, periodMonthKey);
        checkNonNegativeNumber(periodMonth, periodMonthKey);
        housePublishDto.setPeriodMonth(periodMonth);

        String entireRentKey = "entireRent";
        int entireRent = RequestUtils.getParameterInt(request, entireRentKey);
        checkEntireRent(entireRent, entireRentKey);
        housePublishDto.setEntireRent(entireRent);

        String settingsKey = "settings";
        String settings = RequestUtils.getParameterString(request, settingsKey, StringUtil.EMPTY);
        checkSetting(settings, settingsKey);
        housePublishDto.setSettings(settings);

        String settingsAddonKey = "settingsAddon";
        String settingsAddon = RequestUtils.getParameterString(request, settingsAddonKey, StringUtil.EMPTY);
        checkSetting(settingsAddon, settingsAddonKey);
        housePublishDto.setSettingsAddon(settingsAddon);

        String descKey = "desc";
        String desc = RequestUtils.getParameterString(request, descKey, StringUtil.EMPTY);
        checkStringMaxLength(desc, descKey, Constants.HouseDetail.HOUSE_COMMENT_LENGTH_MAX);
        housePublishDto.setDesc(desc);

        String imgsKey = "imgs";
        String imgs = RequestUtils.getParameterString(request, imgsKey, StringUtil.EMPTY);
        checkImgs(imgs, imgsKey);
        housePublishDto.setImgs(imgs);

        // 商圈找房计算获得
        // String bizNameKey = "bizName";
        // String bizName = RequestUtils.getParameterString(request,
        // bizNameKey);
        // checkStringMaxLength(bizName, bizNameKey,
        // Constants.HouseDetail.HOUSE_BIZ_NAME_LENGTH_MAX);
        // housePublishDto.setBizName(bizName);

        // 房源类型
        String houseTypeKey = "houseType";
        int houseType = RequestUtils.getParameterInt(request, houseTypeKey, 0);
        housePublishDto.setHouseType(houseType);

        // 集中式房源编号
        if (houseType == Constants.HouseDetail.HOUSE_TYPE_CENTRAL) {// 集中式校验房源编号
            // 集中式房源编号
            String focusCodeKey = "focusCode";
            String focusCode = RequestUtils.getParameterString(request, focusCodeKey);
            checkStringMaxLength(focusCode, focusCodeKey, Constants.HouseDetail.HOUSE_FOCUSCODE_LENGTH_MAX);
            housePublishDto.setFocusCode(focusCode);
        }

        // saas发布类型
        String pubTypeKey = "pubType";
        int pubType = RequestUtils.getParameterInt(request, pubTypeKey, 0);
        housePublishDto.setPubType(pubType);

        return housePublishDto;
    }

    /**
     * 获取房间DTO
     *
     * @param request
     * @return
     * @throws Exception
     */
    private RoomPublishDto getRoomPublishDto(HttpServletRequest request) throws Exception {

        RoomPublishDto roomPublishDto = new RoomPublishDto();

        String sellIdKey = "sellId";
        String sellId = RequestUtils.getParameterString(request, sellIdKey);
        roomPublishDto.setSellId(sellId);

        String priceKey = "price";
        int price = RequestUtils.getParameterInt(request, priceKey);
        checkNonNegativeNumber(price, priceKey);
        roomPublishDto.setPrice(price);

        String bonusKey = "bonus";
        int bonus = RequestUtils.getParameterInt(request, bonusKey);
        checkNonNegativeNumber(bonus, bonusKey);
        roomPublishDto.setBonus(bonus);

        String depositKey = "deposit";
        int deposit = RequestUtils.getParameterInt(request, depositKey);
        checkNonNegativeNumber(deposit, depositKey);
        roomPublishDto.setDeposit(deposit);

        String hasKeyKey = "hasKey";
        int hasKey = RequestUtils.getParameterInt(request, hasKeyKey);
        checkHaskey(hasKey, hasKeyKey);
        roomPublishDto.setHasKey(hasKey);

        String companyIdKey = "companyId";
        String companyId = RequestUtils.getParameterString(request, companyIdKey, StringUtil.EMPTY);
        roomPublishDto.setCompanyId(companyId);

        String companyNameKey = "companyName";
        String companyName = RequestUtils.getParameterString(request, companyNameKey, StringUtil.EMPTY);
        checkStringMaxLength(companyName, companyNameKey, Constants.HouseBase.HOUSE_COMPANY_NAME_LENGTH_MAX);
        roomPublishDto.setCompanyName(companyName);

        // String agencyIdKey = "agencyId";
        // int agencyId = RequestUtils.getParameterInt(request, agencyIdKey);
        // roomPublishDto.setAgencyId(agencyId);

        String agencyPhoneKey = "agencyPhone";
        String agencyPhone = RequestUtils.getParameterString(request, agencyPhoneKey);
        checkPhone(agencyPhone, agencyPhoneKey);
        roomPublishDto.setAgencyPhone(agencyPhone);

        String agencyNameKey = "agencyName";
        String agencyName = RequestUtils.getParameterString(request, agencyNameKey);
        checkStringMaxLength(agencyName, agencyNameKey, Constants.HouseBase.HOUSE_AGENCY_NAME_LENGTH_MAX);
        roomPublishDto.setAgencyName(agencyName);

        String agencyIntroduceKey = "agencyIntroduce";
        String agencyIntroduce = RequestUtils.getParameterString(request, agencyIntroduceKey, StringUtil.EMPTY);
        checkStringMaxLength(agencyIntroduce, agencyIntroduceKey,
                Constants.HouseBase.HOUSE_AGENCY_INTRODUCE_LENGTH_MAX);
        roomPublishDto.setAgencyIntroduce(agencyIntroduce);

        String agencyGenderKey = "agencyGender";
        int agencyGender = RequestUtils.getParameterInt(request, agencyGenderKey, 0);
        checkGender(agencyGender, agencyGenderKey);
        roomPublishDto.setAgencyGender(agencyGender);

        String agencyAvatarKey = "agencyAvatar";
        String agencyAvatar = RequestUtils.getParameterString(request, agencyAvatarKey, StringUtil.EMPTY);
        checkStringMaxLength(agencyAvatar, agencyAvatarKey, Constants.HouseBase.HOUSE_AGENCY_AVATAR_LENGTH_MAX);
        roomPublishDto.setAgencyAvatar(agencyAvatar);

        String statusKey = "status";
        int status = RequestUtils.getParameterInt(request, statusKey);
        checkStatus(status, statusKey);
        roomPublishDto.setStatus(status);

        String areaKey = "area";
        float area = RequestUtils.getParameterFloat(request, areaKey);
        checkNonNegativeNumber(area, areaKey);
        //checkArea(area, areaKey, Constants.RoomBase.ROOM_AREA_MIN);
        roomPublishDto.setArea(area);

        String orientationKey = "orientation";
        int orientation = RequestUtils.getParameterInt(request, orientationKey);
        checkOrientation(orientation, orientationKey);
        roomPublishDto.setOrientation(orientation);

        String fitmentTypeKey = "fitmentType";
        int fitmentType = RequestUtils.getParameterInt(request, fitmentTypeKey);
        checkDecoration(fitmentType, fitmentTypeKey);
        roomPublishDto.setFitmentType(fitmentType);

        String toiletKey = "toilet";
        int toilet = RequestUtils.getParameterInt(request, toiletKey);
        checkIndependent(toilet, toiletKey);
        roomPublishDto.setToilet(toilet);

        String balconyKey = "balcony";
        int balcony = RequestUtils.getParameterInt(request, balconyKey);
        checkIndependent(balcony, balconyKey);
        roomPublishDto.setBalcony(balcony);

        // String insuranceKey = "insurance";
        // int insurance = RequestUtils.getParameterInt(request, insuranceKey,
        // Constants.HouseBase.INSURANCE_NOT_EXIST);
        // checkInsurance(insurance, insuranceKey);
        // roomPublishDto.setInsurance(insurance);

        String checkInKey = "checkIn";
        Date checkInDate = RequestUtils.getParameterDate(request, checkInKey);
        // checkCheckIn(checkInDate, checkInKey);
        roomPublishDto.setCheckInTime(checkInDate);

        String depositMonthKey = "depositMonth";
        int depositMonth = RequestUtils.getParameterInt(request, depositMonthKey);
        checkNonNegativeNumber(depositMonth, depositMonthKey);
        roomPublishDto.setDepositMonth(depositMonth);

        String periodMonthKey = "periodMonth";
        int periodMonth = RequestUtils.getParameterInt(request, periodMonthKey);
        checkNonNegativeNumber(periodMonth, periodMonthKey);
        roomPublishDto.setPeriodMonth(periodMonth);

        String settingsKey = "settings";
        String settings = RequestUtils.getParameterString(request, settingsKey, StringUtil.EMPTY);
        checkSetting(settings, settingsKey);
        roomPublishDto.setSettings(settings);

        String settingsAddonKey = "settingsAddon";
        String settingsAddon = RequestUtils.getParameterString(request, settingsAddonKey, StringUtil.EMPTY);
        checkSetting(settingsAddon, settingsAddonKey);
        roomPublishDto.setSettingsAddon(settingsAddon);

        String descKey = "desc";
        String desc = RequestUtils.getParameterString(request, descKey, StringUtil.EMPTY);
        checkStringMaxLength(desc, descKey, Constants.RoomBase.ROOM_COMMENT_LENGTH_MAX);
        roomPublishDto.setDesc(desc);

        String imgsKey = "imgs";
        String imgs = RequestUtils.getParameterString(request, imgsKey, StringUtil.EMPTY);
        checkImgs(imgs, imgsKey);
        roomPublishDto.setImgs(imgs);

        String roomNameKey = "roomName";
        String roomName = RequestUtils.getParameterString(request, roomNameKey);
        checkStringMaxLength(roomName, roomNameKey, Constants.RoomBase.ROOM_NAME_LENGTH_MAX);
        roomPublishDto.setRoomName(roomName);

        String roomTypeKey = "roomType";
        int roomType = RequestUtils.getParameterInt(request, roomTypeKey);
        checkNonNegativeNumber(roomType, roomTypeKey);
        roomPublishDto.setRoomType(roomType);

        String pubTypeKey = "pubType";
        int pubType = RequestUtils.getParameterInt(request, pubTypeKey, 0);
        roomPublishDto.setPubType(pubType);

        return roomPublishDto;
    }

    /**
     * 获取投诉DTO
     *
     * @param request
     * @return
     * @throws Exception
     */
    private ComplaintSaveDto getComplaintSaveDto(HttpServletRequest request) throws Exception {
        if (request == null) {
            throw new Exception("请求为参数为空");
        }

        ComplaintSaveDto complaintSaveDto = new ComplaintSaveDto();

        String sellId = RequestUtils.getParameterString(request, "sellId");
        complaintSaveDto.setSellId(sellId);

        // roomId可选
        long roomId = RequestUtils.getParameterLong(request, "roomId", 0);
        complaintSaveDto.setRoomId(roomId);

        String uidKey = "uid";
        String uid = RequestUtils.getParameterString(request, uidKey);
        checkStringMaxLength(uid, uidKey, Constants.Complaint.COMPLAINT_UID_LENGTH_MAX);
        complaintSaveDto.setUid(uid);

        int complaint = RequestUtils.getParameterInt(request, "complaint");
        complaintSaveDto.setComplaint(complaint);

        // comment可选
        String commentKey = "comment";
        String comment = RequestUtils.getParameterString(request, commentKey, StringUtil.EMPTY);
        checkStringMaxLength(comment, commentKey, Constants.Complaint.COMPLAINT_COMMENT_LENGTH_MAX);
        complaintSaveDto.setComment(comment);

        return complaintSaveDto;

    }

    /**
     * 校验Token
     *
     * @throws Exception
     */
    private void checkToken(HttpServletRequest request) throws Exception {
        if (request == null) {
            return;
        }
        String sellId = RequestUtils.getParameterString(request, "sellId");
        String token = RequestUtils.getParameterString(request, "token");
        boolean tokenValid = isTokenValid(sellId, token);
        if (!tokenValid) {
            throw new BaseException(ErrorMsgCode.ERROR_MSG_API_TOEKN_AUTH_FAIL, "接口Token验证失败");
        }
    }

    /**
     * 校验token是否与缓存一致
     *
     * @param appId
     * @param token
     * @return
     */
    private boolean isTokenValid(String sellId, String token) {

        String cacheToken = null;
        try {
            cacheToken = houseDao.getHouseToken(sellId, 0);
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("Token获取失败, sellId:" + sellId + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_API_TOEKN_AUTH_FAIL, "接口Token验证失败");
        }

        boolean isEqual = StringUtil.isNotEmpty(token) && token.equals(cacheToken);
        if (!isEqual) {
            logger.error(LogUtils.getCommLog("Token验证失败, token:" + token + ", cacheToken:" + cacheToken));
        }

        return isEqual;
    }

    /**
     * 抛出BaseException
     *
     * @param e
     */
    private void rethrowBaseException(Exception e) {
        if (e instanceof BaseException) {
            throw (BaseException) e;
        }
    }

    /**
     * 校验坐标值
     *
     * @param position
     * @param keyName
     */
    private void checkPositionValue(String positionValue, String keyName) {
        if (StringUtil.isNotBlank(positionValue)) {
            boolean valid = RulesVerifyUtil.verifyPositionValue(positionValue);
            if (!valid) {
                throw new InvalidParameterException("参数格式不正确:" + keyName);
            }
        }
    }

    /**
     * 校验纬度
     *
     * @param position
     * @param keyName
     */
    private void checkLatitude(String positionValue, String keyName) {
        if (StringUtil.isNotBlank(positionValue)) {
            Double latitude = StringUtil.parseDouble(positionValue);
            boolean valid = latitude != null && latitude >= Constants.Common.LATITUDE_MIN
                    && latitude <= Constants.Common.LATITUDE_MAX;
            if (!valid) {
                throw new InvalidParameterException("参数不合法:" + keyName);
            }
        }
    }

    /**
     * 校验经度
     *
     * @param position
     * @param keyName
     */
    private void checkLongitude(String positionValue, String keyName) {
        if (StringUtil.isNotBlank(positionValue)) {
            Double longitude = StringUtil.parseDouble(positionValue);
            boolean valid = longitude != null && longitude >= Constants.Common.LONGITUDE_MIN
                    && longitude <= Constants.Common.LONGITUDE_MAX;
            if (!valid) {
                throw new InvalidParameterException("参数不合法:" + keyName);
            }
        }
    }

    /**
     * 校验字符串长度
     *
     * @param position
     * @param keyName
     */
    private void checkStringMaxLength(String string, String keyName, int maxLength) {
        if (StringUtil.isNotBlank(string)) {
            boolean valid = RulesVerifyUtil.verifyCsMaxLength(string, maxLength);
            if (!valid) {
                throw new InvalidParameterException("参数不合法:" + keyName + ",字符串过长");
            }
        }
    }

    /**
     * 校验设置
     *
     * @param position
     * @param keyName
     */
    private void checkSetting(String settingValue, String keyName) {
        if (StringUtil.isNotBlank(settingValue)) {
            boolean valid = RulesVerifyUtil.verifySetting(settingValue);
            String msg = "参数异常:" + keyName;
            if (!valid) {
                throw new InvalidParameterException(msg);
            }
            List<String> houseSettingSingleStringList = HouseUtil.getHouseSettingSingleStringList(settingValue);
            if (CollectionUtils.isNotEmpty(houseSettingSingleStringList)) {
                for (String singleString : houseSettingSingleStringList) {
                    Pair<String, String> keyAndNum = HouseUtil.getHouseSettingKeyAndNum(singleString);
                    if (keyAndNum == null) {
                        throw new InvalidParameterException(msg);
                    }
                    String key = keyAndNum.getLeft();
                    Pair<Integer, Integer> typeAndCode = HouseUtil.getHouseSettingTypeAndCode(key);
                    if (typeAndCode == null) {
                        throw new InvalidParameterException(msg);
                    }
                    Integer type = typeAndCode.getLeft();
                    if (type == null || type == Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED) {
                        throw new InvalidParameterException(msg);
                    }
                    Integer code = typeAndCode.getRight();
                    if (code == null || code == Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED) {
                        throw new InvalidParameterException(msg);
                    }
                    String num = keyAndNum.getRight();
                    boolean isNumValid = RulesVerifyUtil.verifyNumber(num);
                    if (!isNumValid) {
                        throw new InvalidParameterException(msg);
                    }
                }
            }
        }
    }

    /**
     * 校验图片
     *
     * @param position
     * @param keyName
     */
    private void checkImgs(String imgs, String keyName) {
        if (StringUtil.isNotBlank(imgs)) {
            String[] split = imgs.split(StringUtil.COMMA);
            int IMG_MAX = Constants.HousePicture.HOUSE_IMGS_MAX;
            if (split == null || split.length == 0) {
                throw new InvalidParameterException("参数异常:" + keyName);
            } else if (split.length > IMG_MAX) {
                throw new InvalidParameterException("参数异常:" + keyName + ",图片数量过多");
            }
        }
    }

    /**
     * 校验房源/房间状态
     *
     * @param haskey
     * @param keyName
     */
    private void checkStatus(int status, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.PUBLISH_STATUS_NOT_ON_SALE);
        validKeys.add(Constants.HouseBase.PUBLISH_STATUS_PRE_RENT);
        validKeys.add(Constants.HouseBase.PUBLISH_STATUS_RENT);
        checkIfValid(status, validKeys, keyName);
    }

    /**
     * 校验数字非负
     *
     * @param position
     * @param keyName
     */
    private void checkNonNegativeNumber(long number, String keyName) {
        if (number < 0) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }

    }

    /**
     * 校验分页最大条数
     *
     * @param position
     * @param keyName
     */
    private void checkPageSize(long number, String keyName) {
        if (number > 100) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }

    }

    /**
     * 校验数字非负
     *
     * @param position
     * @param keyName
     */
    private void checkNonNegativeNumber(double number, String keyName) {
        if (number < 0) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }
    }

    /**
     * 校验面积
     *
     * @param position
     * @param keyName
     */
    private void checkArea(float number, String keyName, int minArea) {
        if (number < minArea) {
            throw new InvalidParameterException("参数异常:" + keyName + ",面积过小");
        }
    }

    /**
     * 校验电话
     *
     * @param haskey
     * @param keyName
     */
    private void checkPhone(String phone, String keyName) {
        if (StringUtil.isNotBlank(phone)) {
            // boolean valid = RulesVerifyUtil.verifyPhone(phone);
            boolean valid = RulesVerifyUtil.verifyCsMaxLength(phone, Constants.HouseBase.HOUSE_PHONE_LENGTH_MAX); // 暂时只校验长度
            if (!valid) {
                throw new InvalidParameterException("参数异常:" + keyName);
            }
        }
    }

    /**
     * 校验是否有钥匙
     *
     * @param haskey
     * @param keyName
     */
    private void checkHaskey(int haskey, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.HAS_KEY_NO);
        validKeys.add(Constants.HouseBase.HAS_KEY_YES);
        checkIfValid(haskey, validKeys, keyName);
    }

    /**
     * 校验性别
     *
     * @param haskey
     * @param keyName
     */
    private void checkGender(int gender, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.GENDER_MALE);
        validKeys.add(Constants.HouseBase.GENDER_FEMALE);
        validKeys.add(Constants.HouseBase.GENDER_INIT);
        checkIfValid(gender, validKeys, keyName);
    }

    /**
     * 校验朝向
     *
     * @param haskey
     * @param keyName
     */
    private void checkOrientation(int orientation, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.ORIENTATION_EAST);
        validKeys.add(Constants.HouseBase.ORIENTATION_WEST);
        validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH);
        validKeys.add(Constants.HouseBase.ORIENTATION_NORTH);
        validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_WEST);
        validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_WEST);
        validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_EAST);
        validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_EAST);
        validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_SOUTH);
        validKeys.add(Constants.HouseBase.ORIENTATION_EAST_WEST);
        validKeys.add(Constants.HouseBase.ORIENTATION_INIT);
        checkIfValid(orientation, validKeys, keyName);
    }

    /**
     * 校验朝向
     *
     * @param haskey
     * @param keyName
     */
    private void checkBuildingType(int buildingType, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_TOWER);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB_AND_TOWER);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SINGLE);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_ROW);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_OVERLAY);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_INIT);
        checkIfValid(buildingType, validKeys, keyName);
    }

    /**
     * 校验朝向
     *
     * @param haskey
     * @param keyName
     */
    private void checkDecoration(int decoration, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.DECORATION_FINE);
        validKeys.add(Constants.HouseBase.DECORATION_BRIEF);
        validKeys.add(Constants.HouseBase.DECORATION_ROUGH);
        validKeys.add(Constants.HouseBase.DECORATION_OLD);
        validKeys.add(Constants.HouseBase.DECORATION_LUXURY);
        validKeys.add(Constants.HouseBase.DECORATION_MEDIUM);
        validKeys.add(Constants.HouseBase.DECORATION_NORMAL);
        validKeys.add(Constants.HouseBase.DECORATION_INIT);
        checkIfValid(decoration, validKeys, keyName);
    }

    /**
     * 校验是否独立
     *
     * @param haskey
     * @param keyName
     */
    private void checkIndependent(int independent, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.INDEPENDENT);
        validKeys.add(Constants.HouseBase.NOT_INDEPENDENT);
        checkIfValid(independent, validKeys, keyName);
    }

    /**
     * 校验家财险
     *
     * @param haskey
     * @param keyName
     */
    private void checkInsurance(int insurance, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.INSURANCE_EXIST);
        validKeys.add(Constants.HouseBase.INSURANCE_NOT_EXIST);
        checkIfValid(insurance, validKeys, keyName);
    }

    /**
     * 校验入住时间
     *
     * @param date
     */
    private void checkCheckIn(Date date, String keyName) {
        if (date != null) {
            Date curDate = new Date();
            if (date.before(curDate) && !DateUtil.isSameDay(date, curDate)) {
                throw new InvalidParameterException("参数异常:" + keyName + ",入住时间不能早于今天");
            }
        }
    }

    /**
     * 校验建筑年份
     *
     * @param date
     */
    private void checkBuildingYear(String buildingYear, String keyName) {
        if (StringUtil.isNotBlank(buildingYear)) {
            Date year = DateUtil.parseYear(buildingYear);
            if (year == null) {
                throw new InvalidParameterException("参数异常:" + keyName + ",年份解析失败");
            }
        }
    }

    /**
     * 校验整分租
     *
     * @param haskey
     * @param keyName
     */
    private void checkEntireRent(int entireRent, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseDetail.RENT_TYPE_SHARE);
        validKeys.add(Constants.HouseDetail.RENT_TYPE_ENTIRE);
        validKeys.add(Constants.HouseDetail.RENT_TYPE_BOTH);
        validKeys.add(Constants.HouseDetail.RENT_TYPE_ALL);
        checkIfValid(entireRent, validKeys, keyName);
    }

    /**
     * 校验参数是否有效
     *
     * @param value
     * @param validValues
     * @param keyName
     */
    private void checkIfValid(int value, List<Integer> validValues, String keyName) {
        if (CollectionUtils.isEmpty(validValues)) {
            return;
        }
        if (!validValues.contains(value)) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }
    }

    /**
     * 获取服务公寓列表
     *
     * @return
     */
    //	public Responses getServiceApartmentList(HttpServletRequest request) throws Exception {
    //		long cityId = RequestUtils.getParameterLong(request, "cityId", 0);
    //		List<Agency> agencies = new ArrayList<Agency>();
    //		List<Agency> agenciesTemp = agencyManageRepository.findAgencies(cityId);
    //		if (CollectionUtils.isNotEmpty(agenciesTemp)) {
    //			if (agenciesTemp.size() == 1) {
    //				Agency agencyDanke = new Agency();
    //				agencyDanke.setCityId(cityId);
    //				agencyDanke.setCompanyId("110005");
    //				agencyDanke.setCompanyName("蛋壳公寓");
    //				agencyDanke.setPicRootPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/danke.png");
    //				agencyDanke.setPicWebPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/danke.png");
    //				agencies.add(agencyDanke);
    //				Agency agencyMeiliwu = new Agency();
    //				agencyMeiliwu.setCityId(cityId);
    //				agencyMeiliwu.setCompanyId("12");
    //				agencyMeiliwu.setCompanyName("美丽屋");
    //				agencyMeiliwu.setPicRootPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/meiliwu.png");
    //				agencyMeiliwu.setPicWebPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/meiliwu.png");
    //				agencies.add(agencyMeiliwu);
    //				agencies.add(agenciesTemp.get(0));
    //			} else {
    //				agencies.addAll(agenciesTemp);
    //			}
    //		} else {
    //			Agency agencyDanke = new Agency();
    //			agencyDanke.setCityId(0);
    //			agencyDanke.setCompanyId("110005");
    //			agencyDanke.setCompanyName("蛋壳公寓");
    //			agencyDanke.setPicRootPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/danke.png");
    //			agencyDanke.setPicWebPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/danke.png");
    //			agencies.add(agencyDanke);
    //			Agency agencyMeiliwu = new Agency();
    //			agencyMeiliwu.setCityId(0);
    //			agencyMeiliwu.setCompanyId("12");
    //			agencyMeiliwu.setCompanyName("美丽屋");
    //			agencyMeiliwu.setPicRootPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/meiliwu.png");
    //			agencyMeiliwu.setPicWebPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/meiliwu.png");
    //			agencies.add(agencyMeiliwu);
    //			Agency agencyGengduo = new Agency();
    //			agencyGengduo.setCityId(0);
    //			agencyGengduo.setCompanyId("-1");
    //			agencyGengduo.setCompanyName("更多");
    //			agencyGengduo.setPicRootPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/gengduo.png");
    //			agencyGengduo.setPicWebPath("http://hzf-image.oss-cn-beijing.aliyuncs.com/company_image/gengduo.png");
    //			agencies.add(agencyGengduo);
    //		}
    //		
    //		JsonObject result = new JsonObject();
    //		JsonArray jsonAgencies = new JsonArray();
    //		if (CollectionUtils.isNotEmpty(agencies)) { 
    //			jsonAgencies = GsonUtils.getInstace().toJsonTree(agencies).getAsJsonArray();
    //		}
    //		result.add("companyList", jsonAgencies);
    //		logger.info("print result object++++++++" + result.toString());
    //		Responses responses = new Responses(result);
    //		if (CollectionUtils.isNotEmpty(agencies)) {
    //			ResponseUtils.fillRow(responses.getMeta(), agencies.size(), agencies.size());
    //		}
    //		return responses;
    //	}

    /**
     * 获取服务公寓列表
     *
     * @return
     */
    public Responses getServiceApartmentList(HttpServletRequest request) throws Exception {
        //获取请求参数城市ID
        long cityId = RequestUtils.getParameterLong(request, "cityId", 0);
        logger.info("请求服务式公寓接口,城市ID:" + cityId);

        //根据城市ID获取品牌公寓列表
        List<Agency> agencyList = agencyManageRepository.getAgencyListByCityId(cityId);

        //拼接更多品牌
        List<Agency> gdList = agencyManageRepository.queryListByAgencyId(Constants.ServiceApartment.DEFAULT_COMPANY_ID);
        if (CollectionUtils.isNotEmpty(gdList)) {
            Agency aitaAgency = gdList.get(0);
            agencyList.add(aitaAgency);
        }

        //封装返回BODY
        JsonObject result = new JsonObject();
        JsonArray jsonAgencies = new JsonArray();
        if (CollectionUtils.isNotEmpty(agencyList)) {
            jsonAgencies = GsonUtils.getInstace().toJsonTree(agencyList).getAsJsonArray();
        }
        result.add("companyList", jsonAgencies);
        Responses responses = new Responses(result);
        logger.info("服务式公寓返回列表为:" + result.toString());

        //封装返回META
        if (CollectionUtils.isNotEmpty(agencyList)) {
            ResponseUtils.fillRow(responses.getMeta(), agencyList.size(), agencyList.size());
        }
        return responses;
    }

    /**
     * 获取收藏房源列表
     *
     * @return
     */
    public Responses getCollectHouseList(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        ;
        //long userId = RequestUtils.getParameterLong(request, "userId", 0);
        HouseSearchDto houseSearchDto = getHouseSearchDto(request);
        List<HouseCollection> houseCollections = houseSubDao.findHouseCollectionListByUserId(userId);
        List<String> sellIdList = new ArrayList<String>();
        StringBuilder sbd = new StringBuilder();
        // update by arison
        for (HouseCollection houseCollection : houseCollections) {
            sbd.append(houseCollection.getSellId()).append(StringUtil.COLON).append(houseCollection.getRoomId())
                    .append(",");
            sellIdList.add(houseCollection.getSellId());
        }

        if (sbd.length() > 1) {
            String sellIds = sbd.substring(0, sbd.length() - 1);
            houseSearchDto.setSellId(sellIds);
        } else {
            String sellIds = "\"\":\"\"";
            houseSearchDto.setSellId(sellIds);
        }
        houseSearchDto.setPageSize(Integer.MAX_VALUE);
        return commNativeSearch(houseSearchDto, sellIdList);
    }

    /**
     * 修改收藏房源状态
     *
     * @return
     */
    public Responses updateCollectHouse(HttpServletRequest request) throws Exception {
        String sellId = RequestUtils.getParameterString(request, "sellId");
        int roomId = RequestUtils.getParameterInt(request, "roomId");
        long userId = sessionManager.getUserIdFromSession();
        // long userId = RequestUtils.getParameterLong(request, "userId", 0);
        int state = RequestUtils.getParameterInt(request, "state");
        if (state == 0) {
            houseSubDao.deleteHouseCollectionItem(userId, sellId, roomId);
        } else {
            HouseCollection houseCollection = new HouseCollection();
            houseCollection.setRoomId(roomId);
            houseCollection.setSellId(sellId);
            houseCollection.setUserId(userId);
            houseCollection.setUpdateTime(new Date());
            houseCollection.setCreateTime(new Date());
            houseCollection.setState(1);
            houseSubDao.saveHouseCollectionItem(houseCollection);
        }
        Responses responses = new Responses();
        return responses;
    }

    private Responses commSearch(HouseSearchDto houseSearchDto) {

        // 若地铁站id不为空，查询地铁站坐标
        long stationId = houseSearchDto.getStationId();
        if (stationId != 0) {
            Subway subway = subwayRepository.findSubWayByStationId(stationId);
            String stationPosition = HouseUtil.getPosition(subway);
            if (StringUtil.isEmpty(stationPosition)) {
                logger.error(LogUtils.getCommLog("地铁站坐标查询失败 ,stationId:" + stationId));
                return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
            }
            houseSearchDto.setStationLocation(stationPosition);
        }

        // 查询品牌公寓集合，在房源标题中做校验：只显示包含在里面的公寓名称
        List<String> agencyIdList = new ArrayList<String>();
        List<Agency> agencyList = agencyManageRepository.findAllAgency();
        if (CollectionUtils.isNotEmpty(agencyList)) {
            for (Agency agency : agencyList) {
                if (!agencyIdList.contains(agency.getCompanyId())) {
                    agencyIdList.add(agency.getCompanyId());
                }
            }
        }

        //限制查询列表
        List<String> denyList = platformCustomerRepository.findAllWithoutPermission();
        houseSearchDto.setDenyList(denyList);

        // TODO 实现搜索功能
        logger.info(LogUtils.getCommLog("搜房源请求参数 ,houseSearchDto:" + houseSearchDto));
        //HouseSearchResultDto houseSearchResultDto = houseDao.getHouseSearchResultDto(houseSearchDto, agencyIdList);

        //获取用户ID
        long userId = getLoginUserId();
        //获取用户收藏列表
        List<String> collectIdList = getCollectList(userId);

        HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();
        
        //查询第一页
        if (houseSearchDto.getPageNum() == 1) {
            if(houseSearchDto.getOrderType().isEmpty()){//未排序   房源打散
                houseSearchResultDto = houseDao.getNewHouseSearchResultDto(houseSearchDto, agencyIdList);
                
                //房源列表增加收藏标识
                if (CollectionUtils.isNotEmpty(collectIdList) && houseSearchResultDto != null) {
                    houseSearchResultDto = getCollectSearchResultDto(houseSearchResultDto, collectIdList);
                }

                //月付房源打散
                if (houseSearchDto.getPayType().contains(Constants.HouseDetail.PAY_TYPE) && houseSearchResultDto != null) {
                    houseSearchResultDto = getPaySearchResultDto(houseSearchResultDto, houseSearchDto, agencyIdList);
                } 
                
                //推荐房源
                if(houseSearchDto.getSellerId() != null){
                    houseSearchResultDto = houseDao.getHouseSearchResultDto(houseSearchDto, agencyIdList, collectIdList);
                }
            }else{//排序
                houseSearchResultDto = houseDao.getHouseSearchResultDto(houseSearchDto, agencyIdList, collectIdList);
            }
        }

        //第二页
        if (houseSearchDto.getPageNum() > 1) {
            if(houseSearchDto.getOrderType().isEmpty()){//未排序房源打散
                houseSearchResultDto = houseDao.getNewHouseSearchResultDto(houseSearchDto, agencyIdList);

                //过滤第一页
                houseSearchDto = getPageOneHouseResultDto(houseSearchResultDto, houseSearchDto);
                houseSearchResultDto = houseDao.getHouseSearchResultDto(houseSearchDto, agencyIdList, collectIdList);  
            }else{//排序不打散
                houseSearchResultDto = houseDao.getHouseSearchResultDto(houseSearchDto, agencyIdList, collectIdList);
            }
           
        }
        
        if (houseSearchResultDto == null) {
            logger.error(LogUtils.getCommLog("搜索房源结果不存在"));
            return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
        }

        //封装返回结果
        Responses responses = new Responses(houseSearchResultDto);
        List<HouseSearchResultInfo> searchHouses = houseSearchResultDto.getSearchHouses();

        //请求图片优化渠道列表
        List<PlatformCustomer> customerList = platformCustomerRepository.findByisImgs();
        Map<String, String> map = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(customerList)) {
            for (PlatformCustomer customer : customerList) {
                map.put(customer.getSource(), customer.getImageCss());
            }
        }

        if (CollectionUtils.isNotEmpty(searchHouses)) {
            for (HouseSearchResultInfo info : searchHouses) {
                if (StringUtils.isNotEmpty(info.getPic())) {
                    if (map != null) {//图片美化
                        if (map.containsKey(info.getSource())) {
                            String pic = info.getPic() + "?x-oss-process=image/resize,h_240"
                                    + map.get(info.getSource());
                            info.setPic(pic);
                        } else {// 推荐房源列表页显示小图 
                            String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
                            info.setPic(pic);
                        }
                    } else {
                        String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
                        info.setPic(pic);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(searchHouses)) {
            ResponseUtils.fillRow(responses.getMeta(), searchHouses.size(), searchHouses.size());
        }
        return responses;
    }

    private Responses commNativeSearch(HouseSearchDto houseSearchDto, List<String> sellIdList) {
        // 若地铁站id不为空，查询地铁站坐标
        long stationId = houseSearchDto.getStationId();
        if (stationId != 0) {
            Subway subway = subwayRepository.findSubWayByStationId(stationId);
            String stationPosition = HouseUtil.getPosition(subway);
            if (StringUtil.isEmpty(stationPosition)) {
                logger.error(LogUtils.getCommLog("地铁站坐标查询失败 ,stationId:" + stationId));
                return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
            }
            houseSearchDto.setStationLocation(stationPosition);
        }
        // TODO 实现搜索功能
        logger.info(LogUtils.getCommLog("搜房源请求参数 ,houseSearchDto:" + houseSearchDto));
        HouseSearchResultDto houseSearchResultDto = houseDao.searchHouseSearchResultDto(houseSearchDto);
        if (houseSearchResultDto == null) {
            logger.error(LogUtils.getCommLog("搜索房源结果不存在"));
            return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
        }

        Responses responses = new Responses(houseSearchResultDto);
        List<HouseSearchResultInfo> searchHouses = houseSearchResultDto.getSearchHouses();
        List<HouseSearchResultInfo> searchHousesResult = new ArrayList<HouseSearchResultInfo>();
        Map<String, Object> houseMap = new HashMap<String, Object>();
        // 获取的房源降序排列
        if (CollectionUtils.isNotEmpty(searchHouses)) {
            for (HouseSearchResultInfo houseSearchResultInfo : searchHouses) {
                houseMap.put(houseSearchResultInfo.getSellId(), houseSearchResultInfo);
            }
        }
        if (CollectionUtils.isNotEmpty(sellIdList)) {
            for (int i = 0; i < sellIdList.size(); i++) {
                HouseSearchResultInfo houseSearchResultInfo = (HouseSearchResultInfo) houseMap.get(sellIdList.get(i));
                if (houseSearchResultInfo != null) {
                    searchHousesResult.add(houseSearchResultInfo);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(searchHousesResult)) {
            for (HouseSearchResultInfo info : searchHousesResult) {
                // 推荐房源列表页显示小图 ，2017年06月23日19:07:25 jjs
                if (StringUtils.isNotEmpty(info.getPic())) {
                    String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
                    info.setPic(pic);
                }
            }
        }

        houseSearchResultDto.setSearchHouses(searchHousesResult);
        ResponseMeta meta = new ResponseMeta();
        meta.setRows(searchHousesResult.size());
        meta.setTotalRows(searchHousesResult.size());
        responses.setMeta(meta);
        responses.setBody(houseSearchResultDto);
        return responses;
    }

    /**
     * @Title: searchHouseList
     * @Description: 条件搜索房源数据
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年8月17日 下午4:07:37
     */
    private Responses searchHouseList(HouseSearchDto houseSearchDto) {
        // 若地铁站id不为空，查询地铁站坐标
        long stationId = houseSearchDto.getStationId();
        if (stationId != 0) {
            Subway subway = subwayRepository.findSubWayByStationId(stationId);
            String stationPosition = HouseUtil.getPosition(subway);
            if (StringUtil.isEmpty(stationPosition)) {
                logger.error(LogUtils.getCommLog("地铁站坐标查询失败 ,stationId:" + stationId));
                return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
            }
            houseSearchDto.setStationLocation(stationPosition);
        }
        // 查询当前城市的品牌公寓集合
        List<String> agencyIdList = new ArrayList<String>();
        if ("-1".equals(houseSearchDto.getCompanyId()) || StringUtil.isNotBlank(houseSearchDto.getCompanyType())) {
            List<Agency> agencyList = agencyManageRepository.findAgenciesByCityId(houseSearchDto.getCityId());
            if (CollectionUtils.isNotEmpty(agencyList)) {
                for (Agency agency : agencyList) {
                    if (!agencyIdList.contains(agency.getCompanyId())) {
                        agencyIdList.add(agency.getCompanyId());
                    }
                }
            }
        }

        //按渠道过滤
        List<String> denyList = platformCustomerRepository.findAllWithoutPermission();
        houseSearchDto.setDenyList(denyList);

        // 实现搜索功能
        logger.info(LogUtils.getCommLog("搜房源请求参数 ,houseSearchDto:" + houseSearchDto));
        HouseSearchResultDto houseSearchResultDto = CretateHouseSearchResultDto();

        //获取用户ID
        long userId = getLoginUserId();
        //获取用户收藏列表
        List<String> collectIdList = getCollectList(userId);

        //获取中介打散房源
        if (houseSearchDto.getPageNum() == 1 && houseSearchDto.getOrderType().isEmpty()) {//默认排序中介房源打散	
            houseSearchResultDto = houseDao.getNewHouseResultDto(houseSearchDto, agencyIdList);

            if (CollectionUtils.isNotEmpty(collectIdList) && houseSearchResultDto != null) {//房源列表增加收藏标识
                houseSearchResultDto = getCollectSearchResultDto(houseSearchResultDto, collectIdList);
            }

        }
        if (houseSearchDto.getPageNum() == 1 && !houseSearchDto.getOrderType().isEmpty()) {//排序按原逻辑查询
            houseSearchResultDto = houseDao.getHouseResultDto(houseSearchDto, agencyIdList, collectIdList);
        }

        //第二页开始展示新房源
        if (houseSearchDto.getPageNum() > 1 && houseSearchDto.getOrderType().isEmpty()) {
            houseSearchResultDto = houseDao.getNewHouseResultDto(houseSearchDto, agencyIdList);
            //获取第一页房源过滤
            houseSearchDto = getPageOneHouseResultDto(houseSearchResultDto, houseSearchDto);
            houseSearchResultDto = houseDao.getHouseResultDto(houseSearchDto, agencyIdList, collectIdList);

        }
        if (houseSearchDto.getPageNum() > 1 && !houseSearchDto.getOrderType().isEmpty()) {
            houseSearchResultDto = houseDao.getHouseResultDto(houseSearchDto, agencyIdList, collectIdList);
        }

        if (houseSearchResultDto == null) {
            logger.error(LogUtils.getCommLog("搜索房源结果不存在"));
            return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
        }

        Responses responses = new Responses(houseSearchResultDto);
        List<HouseSearchResultInfo> searchHouses = houseSearchResultDto.getSearchHouses();

        //请求图片优化渠道列表
        List<PlatformCustomer> customerList = platformCustomerRepository.findByisImgs();
        Map<String, String> map = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(customerList)) {
            for (PlatformCustomer customer : customerList) {
                map.put(customer.getSource(), customer.getImageCss());
            }
        }
        if (CollectionUtils.isNotEmpty(searchHouses)) {
            for (HouseSearchResultInfo info : searchHouses) {
                if (StringUtils.isNotEmpty(info.getPic())) {
                    if (map != null) {//图片美化

                        if (map.containsKey(info.getSource())) {
                            String pic = info.getPic() + "?x-oss-process=image/resize,h_240"
                                    + map.get(info.getSource());
                            info.setPic(pic);
                        } else {// 推荐房源列表页显示小图 
                            String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
                            info.setPic(pic);
                        }
                    } else {
                        String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
                        info.setPic(pic);
                    }
                }
            }
        }
        
        if (CollectionUtils.isNotEmpty(searchHouses)) {
            ResponseUtils.fillRow(responses.getMeta(), searchHouses.size(), searchHouses.size());
        }
        return responses;
    }

    /**
     * @Title: getFootmarkHistoryList
     * @Description: 获取浏览足迹列表
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年8月17日 上午10:59:24
     */
    public Responses getFootmarkHistoryList(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        // long userId = RequestUtils.getParameterLong(request, "userId", 0);
        HouseSearchDto houseSearchDto = getHouseSearchDto(request);
        List<FootmarkHistory> houseList = houseSubDao.findHouseListByUserId(userId);
        List<String> sellIdList = new ArrayList<String>();
        StringBuilder sbd = new StringBuilder();
        for (FootmarkHistory footmarkHistory : houseList) {
            sbd.append(footmarkHistory.getSellId()).append(StringUtil.COLON).append(footmarkHistory.getRoomId())
                    .append(",");
            sellIdList.add(footmarkHistory.getSellId());
        }
        if (sbd.length() > 1) {
            String sellIds = sbd.substring(0, sbd.length() - 1);
            houseSearchDto.setSellId(sellIds);
        } else {
            String sellIds = "\"\":\"\"";
            houseSearchDto.setSellId(sellIds);
        }
        houseSearchDto.setPageSize(20);
        return commNativeSearch(houseSearchDto, sellIdList);
    }

    /**
     * @Title: addOrderCustom
     * @Description: 添加用户订制数据
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年8月18日 上午10:44:59
     */
    public Responses addOrderCustom(HttpServletRequest request) throws Exception {
        String location = RequestUtils.getParameterString(request, "location");
        String price = RequestUtils.getParameterString(request, "price");
        String checkInTime = RequestUtils.getParameterString(request, "checkInTime");
        String phone = RequestUtils.getParameterString(request, "phone");
        long userId = sessionManager.getUserIdFromSession();
        // long userId = RequestUtils.getParameterLong(request, "userId", 0);

        long orderCustomId = 0;
        OrderCustom orderCustom = houseDao.getOrderCustomByUserId(userId);
        if (orderCustom == null) {
            orderCustom = new OrderCustom();
            orderCustom.setUserId(userId);
            orderCustom.setLocation(location);
            orderCustom.setMinPrice(Long.parseLong(price.split(",")[0]));
            orderCustom.setMaxPrice(Long.parseLong(price.split(",")[1]));
            orderCustom.setCheckInTime(DateUtil.parseDate(checkInTime));
            orderCustom.setPhone(phone);
            try {
                orderCustomId = houseDao.saveOrderCustom(orderCustom);
            } catch (Exception e) {
                logger.error(LogUtils.getCommLog("用户订制数据保存失败" + e.getMessage()));
                throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_ORDER_CUSTOM_FAIL, "保存用户订制数据失败");
            }
        } else {
            try {
                orderCustomId = houseDao.updateOrderCustom(userId, location, Long.parseLong(price.split(",")[0]),
                        Long.parseLong(price.split(",")[1]), checkInTime, phone);
            } catch (Exception e) {
                logger.error(LogUtils.getCommLog("用户订制数据更新失败" + e.getMessage()));
                throw new BaseException(ErrorMsgCode.ERROR_MSG_UPDATE_ORDER_CUSTOM_FAIL, "更新用户订制数据失败");
            }
        }
        Responses responses = new Responses();
        Map<String, Long> returnMap = new HashMap<String, Long>();
        returnMap.put("orderCustomId", orderCustomId);
        responses.setBody(returnMap);
        return responses;
    }

    /**
     * @Title: getOrderCustomByUserId
     * @Description: 通过userId查询用户订制详情
     * @return Responses
     * @author 叶东明
     * @dateTime 2017年8月18日 上午10:28:53
     */
    public Responses getOrderCustomByUserId(HttpServletRequest request) throws Exception {
        long userId = sessionManager.getUserIdFromSession();
        // long userId = RequestUtils.getParameterLong(request, "userId", 0);
        OrderCustom orderCustom = houseDao.getOrderCustomByUserId(userId);
        if (orderCustom == null) {
            return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_ORDER_CUSTOM_FAIL, "订制数据不存在");
        }
        Responses responses = new Responses(orderCustom);
        ResponseUtils.fillRow(responses.getMeta(), 1, 1);
        return responses;
    }

    /**
     * @Title: getGlobalConf
     * @Description: 获取全局配置
     * @return Responses
     * @author 李强
     * @dateTime 2017年8月29日 上午10:44:59
     */
    public Responses getGlobalConf(HttpServletRequest request) throws Exception {
        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("agreementPath", configuration.agreementPath);
        returnMap.put("privateAgreementPath", configuration.privateAgreementPath);
        responses.setBody(returnMap);
        return responses;
    }

    /**
     * @Title: webStatics
     * @Description: 获取全局配置
     * @return Responses
     * @author 李强
     * @dateTime 2017年8月29日 上午10:44:59
     */
    public Responses webStatics(HttpServletRequest request) throws Exception {
        String prefix = Constants.statisticReport.statisticReport;
        String cityId = RequestUtils.getParameterString(request, "cityId");
        String source = RequestUtils.getParameterString(request, "source");
        Date currentDay = DateUtil.parseDate(DateUtil.formatCurrentDate());
        //Date yesterDay=DateUtil.addDays(currentDay,-1);
        String dataStr = DateUtil.format("yyyyMMdd", currentDay);
        String key = RedisUtils.getInstance().getKey(prefix + ":" + dataStr + ":" + cityId + ":" + source);

        /*
         * boolean lock = lockManager.lock(lockKey); 页面访问数量 ftol pv 页面访问数量 ftol
         * pv 页面访问数量 fpv 搜索框点 fss banner点击 fbdj 金刚 fjg 品牌公寓 fppgy 合租 fhz 整租 fzz
         * 分期 ffq try { while(!lock) { Thread.sleep(1000); } } catch (Exception
         * e) { throw new BaseException(ErrorMsgCode.ERROR_FMH_ADD_FAIL,
         * "浏览房源足迹记录更新失败"); } finally { if (lock) {// 释放锁
         * lockManager.unLock(lockKey); } }
         */
        redisCacheManager.incValue(key);
        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        responses.setBody(returnMap);
        return responses;
    }

    /**
     * @Title: webStatics
     * @Description: 获取全局配置
     * @return Responses
     * @author 李强
     * @dateTime 2017年8月29日 上午10:44:59
     */
    public Responses testScheduler(HttpServletRequest request) throws Exception {
        schedulingConfig.scheduler();
        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        responses.setBody(returnMap);
        return responses;
    }

    /**
     * @Title: webStatics
     * @Description: 获取全局配置
     * @return Responses
     * @author 李强
     * @dateTime 2017年8月29日 上午10:44:59
     */
    public Responses testReportScheduler(HttpServletRequest request) throws Exception {
        schedulingConfig.reportScheduler();
        Responses responses = new Responses();
        Map<String, String> returnMap = new HashMap<String, String>();
        responses.setBody(returnMap);
        return responses;
    }

    /**
     * @Title: webStatics
     * @Description: 计算下架房源数量
     * @return Responses
     * @author 常明炜
     * @dateTime 2017年8月29日 上午10:44:59
     */
    public void companyOffCount(HttpServletRequest request) {
        // 获取下架房源数量
        List<CompanyOffConfig> offList = companyOffConfigRepository.findAll();
        if (CollectionUtils.isNotEmpty(offList)) {
            for (CompanyOffConfig config : offList) {
                //封装请求参数
                HouseSearchDto houseSearchDto = new HouseSearchDto();
                houseSearchDto.setCityId(config.getCityId());
                houseSearchDto.setCompanyId(config.getCompanyId());
                logger.info(LogUtils.getCommLog("获取下架房源数量 ,houseSearchDto:" + houseSearchDto));

                //计算下架房源数量
                int offCount = houseDao.getCompanyOffCount(houseSearchDto);
                logger.info(LogUtils.getCommLog("获取下架房源数量 ,count:" + offCount));

                //保存数量
                config.setOffCount(offCount);
                companyOffConfigRepository.save(config);

            }
        }
    }

    //月付房源打散策率
    public HouseSearchResultDto getPaySearchResultDto(HouseSearchResultDto houseSearchResultDto,
                                                      HouseSearchDto houseSearchDto, List<String> agencyIdList) {
        //获取房源打散 中介公司循环次数
        //		int forCount = Constants.HouseListConfig.FOREACH_COUNT;
        //		City city = cityRepository.findCityById(houseSearchDto.getCityId());
        //		if(city != null){
        //			forCount = city.getForCount();
        //		}
        //		
        //		int number = 0;
        //		List<HouseSearchResultInfo> infoList = houseSearchResultDto.getSearchHouses();
        //		List<HouseSearchResultInfo> tempInfoList = new ArrayList<HouseSearchResultInfo>();
        //		List<HouseSearchResultInfo> afterInfoList = new ArrayList<HouseSearchResultInfo>();
        //		if(CollectionUtils.isNotEmpty(infoList)){
        //			for(HouseSearchResultInfo info : infoList){
        //				if(number > forCount){
        //					afterInfoList.add(info);
        //				}
        //				tempInfoList.add(info);
        //				number++;
        //			}
        //		}
        //		if(CollectionUtils.isNotEmpty(afterInfoList)){
        //			infoList.addAll(afterInfoList);
        //		}
        //		if(CollectionUtils.isNotEmpty(tempInfoList)){
        //			infoList.addAll(tempInfoList);
        //		}
        //		
        //		houseSearchResultDto.setSearchHouses(infoList);
        //		
        //		return houseSearchResultDto;
        List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
        infoList = houseSearchResultDto.getSearchHouses();

        //获取整租数据
        List<String> entireList = new ArrayList<String>();
        HouseSearchResultDto entireDto = new HouseSearchResultDto();
        houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
        entireDto = houseDao.getNewHouseSearchResultDto(houseSearchDto, agencyIdList);
        if (entireDto != null && CollectionUtils.isNotEmpty(entireDto.getSearchHouses())) {
            for (HouseSearchResultInfo info : entireDto.getSearchHouses()) {
                entireList.add(info.getSellId().trim());
            }
        }

        //获取合租数据
        List<String> shareList = new ArrayList<String>();
        HouseSearchResultDto shareDto = new HouseSearchResultDto();
        houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
        shareDto = houseDao.getNewHouseSearchResultDto(houseSearchDto, agencyIdList);
        if (shareDto != null && CollectionUtils.isNotEmpty(shareDto.getSearchHouses())) {
            for (HouseSearchResultInfo info : shareDto.getSearchHouses()) {
                shareList.add(info.getSellId().trim());
            }
        }

        //数据合并
        shareList.addAll(entireList);
        List<HouseSearchResultInfo> newInfoList = new ArrayList<HouseSearchResultInfo>();
        for (HouseSearchResultInfo info : infoList) {
            if (shareList.contains(info.getSellId().trim())) {
                newInfoList.add(info);
            }
        }
        infoList.removeAll(newInfoList);

        houseSearchResultDto.setSearchHouses(infoList);
        return houseSearchResultDto;

    }

    //用户登录增加收藏标识
    public HouseSearchResultDto getCollectSearchResultDto(HouseSearchResultDto houseSearchResultDto,
                                                          List<String> collectIdList) {
        List<HouseSearchResultInfo> infoList = houseSearchResultDto.getSearchHouses();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (HouseSearchResultInfo info : infoList) {
                if (collectIdList.contains(info.getSellId())) {
                    info.setCollectFlag(Constants.HouseDetail.COLLECT_FLAG);
                }
            }
        }
        houseSearchResultDto.setSearchHouses(infoList);
        return houseSearchResultDto;
    }

    public long getLoginUserId() {
        return sessionManager.getUserIdFromSession();
    }

    //获取用户收藏列表
    public List<String> getCollectList(long userId) {
        if (userId <= 0) {//未登录校验
            return null;
        }

        List<String> collectIdList = new ArrayList<String>();
        List<HouseCollection> houseCollectionList = new ArrayList<HouseCollection>();
        try {
            //根据用户ID获取收藏列表
            houseCollectionList = houseCollectionRepository.getHouseCollectionListByUserId(userId);
            if (CollectionUtils.isNotEmpty(houseCollectionList)) {
                for (HouseCollection houseCollection : houseCollectionList) {
                    String sellId = houseCollection.getSellId();
                    if (!collectIdList.contains(sellId)) {
                        collectIdList.add(sellId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("用户登录成功查询收藏列表异常") + e);
            return null;
        }
        return collectIdList;
    }

    //获取第一页房源
    private HouseSearchDto getPageOneHouseResultDto(HouseSearchResultDto houseSearchResultDto,
                                                    HouseSearchDto houseSearchDto) {
        List<HouseSearchResultInfo> infoList = houseSearchResultDto.getSearchHouses();
        List<String> houseList = new ArrayList<String>();
        List<String> roomList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (HouseSearchResultInfo info : infoList) {
                if (info.getEntireRent() == Constants.HouseDetail.RENT_TYPE_ENTIRE) {
                    houseList.add(info.getSellId());
                }
                if (info.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE) {
                    roomList.add(String.valueOf(info.getRoomId()));
                }
            }
            houseSearchDto.setHouseList(houseList);
            houseSearchDto.setRoomList(roomList);
        }
        return houseSearchDto;
    }

    //创建房源结果对象
    private HouseSearchResultDto CretateHouseSearchResultDto() {
        HouseSearchResultDto houseSearchResultDto = new HouseSearchResultDto();
        return houseSearchResultDto;
    }
    
    
    /**
     * 搜索服务发起请求
     */
    public Responses newSearchHouseList(HouseSearchDto houseSearchDto) {
        String url = String.format("http://%s/search/searchHouseList/", searchServiceConfiguration.getIp());
         
        Map<String, String> params = createHouseListMap(houseSearchDto);
        String postRet = null;
        try {
            postRet = HttpUtil.post(url, params);
        } catch (Exception e) {
            throw new BaseException(e);
        }
        
        //获取返回body
        JsonElement element =new JsonParser().parse(postRet); 
        JsonObject obj = element.getAsJsonObject();
        JsonObject body = obj.get("body").getAsJsonObject();
        
        int size = obj.get("meta").getAsJsonObject().get("totalRows").getAsInt();
        //返回对象
        Responses responses = new Responses(body);
        ResponseUtils.fillRow(responses.getMeta(), size, size);

        return responses;

    }
    
    
    /**
     * 搜索服务发起请求
     */
    public Responses getNewHouseInfo(HttpServletRequest request) {
        String url = String.format("http://%s/search/getHouseDetail/", searchServiceConfiguration.getIp());
         
        Map<String, String> params = createGetHouseDetailMap(request);
        String postRet = null;
        try {
            postRet = HttpUtil.post(url, params);
        } catch (Exception e) {
            throw new BaseException(e);
        }
        
        //获取返回body
        JsonElement element =new JsonParser().parse(postRet); 
        JsonObject obj = element.getAsJsonObject();
        JsonObject body = obj.get("body").getAsJsonObject();
        
        int size = obj.get("meta").getAsJsonObject().get("totalRows").getAsInt();
        //返回对象
        Responses responses = new Responses(body);
        ResponseUtils.fillRow(responses.getMeta(), size, size);

        return responses;

    }
    
    /**
     * 搜索服务发起请求
     */
    public Map<String, String> createHouseListMap(HouseSearchDto houseSearchDto) {
        
        Map<String, String> params = new HashMap<>();
        params.put("appId", String.valueOf(houseSearchDto.getAppId()));
        params.put("cityId", String.valueOf(houseSearchDto.getCityId()));
        params.put("q", String.valueOf(houseSearchDto.getKeyword()));
        params.put("disId", String.valueOf(houseSearchDto.getDistrictId()));
        params.put("bizId", String.valueOf(houseSearchDto.getBizId()));
        params.put("lineId", String.valueOf(houseSearchDto.getLineId()));
        params.put("stationId", String.valueOf(houseSearchDto.getStationId()));
        params.put("price", String.valueOf(houseSearchDto.getPrice()));
        params.put("orientation", String.valueOf(houseSearchDto.getOrientation()));
        params.put("area", String.valueOf(houseSearchDto.getArea()));
        params.put("location", String.valueOf(houseSearchDto.getLocation()));
        params.put("distance", String.valueOf(houseSearchDto.getDistance()));
        params.put("orderType", String.valueOf(houseSearchDto.getOrderType()));
        params.put("order", String.valueOf(houseSearchDto.getOrder()));
        params.put("pageNum", String.valueOf(houseSearchDto.getPageNum()));
        params.put("pageSize", String.valueOf(houseSearchDto.getPageSize()));
        params.put("entireRent", String.valueOf(houseSearchDto.getEntireRent()));
        params.put("bedroomNums", String.valueOf(houseSearchDto.getcBedRoomNums()));
        params.put("houseTag", String.valueOf(houseSearchDto.getHouseTag()));
        params.put("orientationStr", String.valueOf(houseSearchDto.getOrientationStr()));
        params.put("eBedRoomNums", String.valueOf(houseSearchDto.geteBedRoomNums()));
        params.put("sBedRoomNums", String.valueOf(houseSearchDto.getsBedRoomNums()));
        params.put("cBedRoomNums", String.valueOf(houseSearchDto.getcBedRoomNums()));
        params.put("payType", String.valueOf(houseSearchDto.getPayType()));
        params.put("sellerId", String.valueOf(houseSearchDto.getSellerId()));
        params.put("communityName", String.valueOf(houseSearchDto.getCommunityName()));
        params.put("roomerId", String.valueOf(houseSearchDto.getRoomerId()));
        params.put("companyType", String.valueOf(houseSearchDto.getCompanyType()));
        params.put("isHomePage", String.valueOf(houseSearchDto.getIsHomePage()));
        params.put("userId", String.valueOf(houseSearchDto.getUserId()));
        
        //删除空value
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();  
        while(it.hasNext()){  
            Map.Entry<String, String> entry=it.next();  
            String key=entry.getKey();
            if(params.get(key).isEmpty() || "null".equals(params.get(key))){//valua is null 
                it.remove();        //delete  
            }  
        } 
        return params;
        
    }

    /**
     * 搜索服务发起请求
     */
    public Map<String, String> createGetHouseDetailMap(HttpServletRequest request) {
        
        Map<String, String> params = new HashMap<>();
        try {
            params.put("appId", RequestUtils.getParameterString(request, "appId"));
            params.put("sellId", RequestUtils.getParameterString(request, "sellId"));
            params.put("roomId", RequestUtils.getParameterString(request, "roomId", "0"));
            params.put("platform", RequestUtils.getParameterString(request, "platform"));
            params.put("sid", RequestUtils.getParameterString(request, "sid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return params;
        
    }
}
