/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseDataCollectWorker.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:14:47 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseOptHistoryRedisDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysCompany;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysRecord;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUserRecord;
import com.huifenqi.hzf_platform.context.enums.bd.SettingsEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.controller.ThirdSysHouseCronller;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.ThirdSysDao;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.handler.ThirdSysHouseRequestHandler;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: SchemaHouseDataCollectWorker date: 2017年4月15日 下午6:14:47
 * Description: 收集房源信息
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class RedisHistorySchemaDeleteHouseDataCollerWorker implements Runnable {

    private static Log logger = LogFactory.getLog(RedisHistorySchemaDeleteHouseDataCollerWorker.class);

    private ThirdSysHouseRequestHandler thirdSysHouseRequestHandler;
    private HouseDao houseDao;
    private ThirdSysDao thirdSysDao;
    private RedisCacheManager redisCacheManager;
    private HouseDetailRepository houseDetailRepository;
    private HouseBaseRepository houseBaseRepository;
    private RoomBaseRepository roomBaseRepository;

    public RedisHistorySchemaDeleteHouseDataCollerWorker(RedisCacheManager redisCacheManager, HouseDao houseDao,
                                                         ThirdSysDao thirdSysDao,
                                                         HouseDetailRepository houseDetailRepository,
                                                         HouseBaseRepository houseBaseRepository,
                                                         RoomBaseRepository roomBaseRepository,
                                                         ThirdSysHouseRequestHandler thirdSysHouseRequestHandler) {

        this.redisCacheManager = redisCacheManager;
        this.houseDao = houseDao;
        this.thirdSysDao = thirdSysDao;
        this.houseDetailRepository = houseDetailRepository;
        this.houseBaseRepository = houseBaseRepository;
        this.roomBaseRepository = roomBaseRepository;
        this.thirdSysHouseRequestHandler = thirdSysHouseRequestHandler;

    }

    @Override
    public void run() {
        while (true) {
            try {
                doWork();
            } catch (Throwable e) {
                logger.error(LogUtils.getCommLog(String.format("待处理房源操作历史的线程出错:%s", e.toString())));
            }
        }
    }

    /**
     * 工作方法
     */
    private void doWork() throws Exception {
        //TimeUnit.SECONDS.sleep(5);//延迟加载 解决数据库提交事务慢问题
        String result = redisCacheManager.rightPop("hzf_platform-house.opt.history");
        HouseOptHistoryRedisDto dto = null;
        try {
            if (result != null) {
                dto = GsonUtils.getInstace().fromJson(result, HouseOptHistoryRedisDto.class);
                //处理同步记录(发布、更新)
                boolean flag = collectionHistoryHouseDataSysAli(dto);
                
                if(flag){
                  //实时同步房源到闲鱼
                    thirdSysHouseRequestHandler.thirdSysDataHistory();  
                }
                
            }
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog(String.format("处理房源历史操作数据(%s)出错:%s", dto.getSellId(), e.toString())));
        } finally {
            // 从正在处理的集合中移除该房间
            logger.info(LogUtils.getCommLog(String.format("处理房源历史操作(%s)的数据收集结束.", dto.getSellId())));
        }
    }

    /**
     * 收集房源操作历史同步ali
     * 
     * @param crawlHouseDetail
     */
    private boolean collectionHistoryHouseDataSysAli(HouseOptHistoryRedisDto dto) throws Exception {
        boolean msgBackFlag = false;//是否回退消息
        boolean sytFlag = false;//是否发起同步闲鱼
        try {
            //如果dto有信息,则说明没收集过,进行收集
            if (dto != null) {
                //1.获取操作记录
                String sellId = dto.getSellId();
                long roomId = dto.getRoomId();

                //2.查询房源基础信息
                HouseBase houseBase = houseBaseRepository.findAllDeleteBySellId(sellId);
                if (houseBase == null) {
                    logger.error(LogUtils.getCommLog("房源操作历史houseBase查询失败,数据库事务未提交, sellId:" + sellId));
                    msgBackFlag =  true;
                }

                //3.查询房源详细信息
                HouseDetail houseDetail = houseDetailRepository.findAllDeleteBySellId(dto.getSellId());
                if (houseDetail == null) {
                    logger.error(LogUtils.getCommLog("房源操作历史houseDetail查询失败,数据库事务未提交, sellId:" + sellId));
                    msgBackFlag =  true;
                }

              //4.查询房源信息
                RoomBase roomBase = null;
                if (dto.getRoomId() > 0) {
                    roomBase = roomBaseRepository.findALLDeleteByRoomId(roomId);
                    if (roomBase == null) {
                        logger.error(LogUtils.getCommLog("roomBase查询失败,数据库事务未提交, roomId:" + roomId));
                        msgBackFlag =  true;
                    }
                }
                
                if(!msgBackFlag){
                    //5校验中介是否可以同步闲鱼
                    ThirdSysCompany thirdSysCompany = thirdSysDao.findBySourceAndCompanyId(houseDetail.getSource(),houseBase.getCompanyId());
                    
                    if (thirdSysCompany != null) {
                        //6验证房源信息是否收集完成
                        if(houseDetail.getIsRun() != Constants.HouseBase.IS_RUN_NO){  
                          //7同步记录
                            createSysAliDate(houseDetail, houseBase, roomBase, dto);
                            sytFlag = true;
                        }else{
                            msgBackFlag =  true; 
                        }  
                    }else{
                        logger.info(LogUtils.getCommLog(String.format("同步房源信息到闲鱼时，房源(%s)归属中介非白名单", dto.getSellId())));  
                    }     
                }
               
            }
            
            if(msgBackFlag){//查询异常 回滚redis消息
                redisCacheManager.leftPushList("hzf_platform-house.opt.history", GsonUtils.getInstace().toJson(dto));
                logger.error(LogUtils.getCommLog(String.format("同步房源信息到闲鱼时房源收集(%s)未完成,或数据库事务未提交，线程休眠", dto.getSellId())));
                //则休眠10秒
                TimeUnit.SECONDS.sleep(30);
            }
            
            logger.error(LogUtils.getCommLog(String.format("创建闲鱼房源操作记录房源ID(%s)房间ID(%s)完成", dto.getSellId(),dto.getRoomId())));
        } catch (Exception e) {
            redisCacheManager.leftPushList("hzf_platform-house.opt.history", GsonUtils.getInstace().toJson(dto));
            logger.error(LogUtils.getCommLog(
                    String.format("房源操作历史数据ID(%s)处理失败:%s", dto.getSellId(), ExceptionUtils.getStackTrace(e))));
        }
        return sytFlag;
    }

    public void addAliSysRecord(HouseDetail houseDetail, HouseBase houseBase, RoomBase roomBase) {
        ThirdSysRecord thirdSysRecord = new ThirdSysRecord();
        thirdSysRecord.setOptType(ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_INSERT);
        thirdSysRecord.setOptTargetName(ThridSysConstants.ThirdSysRecordUtil.OPT_TARGET_NAME_ALI);
        thirdSysRecord.setOptTargetCode(ThridSysConstants.ThirdSysRecordUtil.OPT_TARGET_CODE_ALI);
        thirdSysRecord.setHouseSourceName(houseDetail.getSource());
        thirdSysRecord.setHouseSourceAppid("");
        thirdSysRecord.setHouseEntireRent(houseDetail.getEntireRent());
        thirdSysRecord.setHouseSellId(houseDetail.getSellId());
        thirdSysRecord.setRoomId(0);
        if (roomBase != null) {
            thirdSysRecord.setRoomId((int) roomBase.getId());
        }
        thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT);
        thirdSysDao.updateThirdSysRecord(thirdSysRecord);
    }

    public void updateAliSysRecord(ThirdSysRecord thirdSysRecord) {
        if(thirdSysRecord.getOptStatus() == ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_FARIL){//如果发布或更新失败，只更新同步状态
            thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT); 
        }else{
            thirdSysRecord.setOptType(ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UPDATE);
            thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT); 
        }
        
        thirdSysDao.updateThirdSysRecord(thirdSysRecord);
    }
    
    public void updateAliSysRecord(List<ThirdSysRecord> sysList){
 /*       List<ThirdSysRecord> sysList =  new ArrayList<ThirdSysRecord>();
        for(ThirdSysRecord thirdSysRecord : sysList){     
            thirdSysRecord.setOptType(ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UPDATE);
            thirdSysRecord.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT); 
        }*/
       
        sysList=sysList.stream().map((ThirdSysRecord x)->{x.setOptType(ThridSysConstants.ThirdSysRecordUtil.OPT_TYPE_UPDATE);
        x.setOptStatus(ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_INIT);
        return x;}).collect(Collectors.toList());
        
        thirdSysDao.updateThirdSysRecord(sysList);
    }

    public void createSysAliDate(HouseDetail houseDetail, HouseBase houseBase, RoomBase roomBase,
                                 HouseOptHistoryRedisDto dto) {
        //验证记录是否同步过
        ThirdSysRecord thirdSysRecord = thirdSysDao.findBySellIdRoomId(dto.getSellId(), (int) dto.getRoomId());

        //发布房源
        if (dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_ADD) {
            if (houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_ENTIRE) {
                if (thirdSysRecord == null) {
                    //新增同步记录
                    addAliSysRecord(houseDetail, houseBase, roomBase);
                } else {
                    //更新同步记录
                    updateAliSysRecord(thirdSysRecord);
                }
            }
        }

        //发布房间  更新房间
        boolean flagA  = dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_UPDATE|| dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_ADD;
        if (flagA) {
            if (thirdSysRecord == null) {
                //新增同步记录
                addAliSysRecord(houseDetail, houseBase, roomBase);
            } else {
                //更新同步记录
                updateAliSysRecord(thirdSysRecord);
            }
        }
        
        //更新房源 删除房源
        boolean flagB  = dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_UPDATE || dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_DELETE;
        if (flagB) {
            List<ThirdSysRecord> sysList = thirdSysDao.findBySellId(dto.getSellId());
            if (CollectionUtils.isNotEmpty(sysList)) {
                //更新同步记录
                updateAliSysRecord(sysList);
            }
        }

        //删除房间
        if (dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_DELETE) {
            //验证记录是否同步过        
            if (thirdSysRecord != null) {
                //更新同步记录
                updateAliSysRecord(thirdSysRecord);
            }
        }
    }
}
