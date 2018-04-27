package com.huifenqi.hzf_platform.schema;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseOptHistoryRedisDto;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseOpereteRecord;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.enums.ApproveStatusEnum;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseOperateRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;

public class RedisSaveHouseOptRecordWorker implements Runnable {

	private HouseOperateRecordRepository operateRecordRepository;
	private HouseBaseRepository houseBaseRepository;
	private HouseDetailRepository houseDetailRepository;
	private RoomBaseRepository roomBaseRepository;
	private RedisCacheManager redisCacheManager;

	private static Log logger = LogFactory.getLog(RedisSaveHouseOptRecordWorker.class);

	private static String key = "hzf_platform-house.opt.record";

	public RedisSaveHouseOptRecordWorker(HouseOperateRecordRepository operateRecordRepository,
			RedisCacheManager redisCacheManager,
			HouseDetailRepository houseDetailRepository,
			HouseBaseRepository houseBaseRepository,
			RoomBaseRepository roomBaseRepository)  {
		this.operateRecordRepository = operateRecordRepository;
		this.redisCacheManager = redisCacheManager;
		this.houseDetailRepository = houseDetailRepository;
        this.houseBaseRepository = houseBaseRepository;
        this.roomBaseRepository = roomBaseRepository;
        
	}

	@Override
	public void run() {

		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(LogUtils.getCommLog(String.format("待处理房源操作记录的线程出错:%s", e.toString())));
			}
		}
	}

	private void doWork() {
		String result = "";
		try {
			result = redisCacheManager.rightPop(key);
			HouseOptHistoryRedisDto opt = GsonUtils.getInstace().fromJson(result, HouseOptHistoryRedisDto.class);
			try {
			    collectionHistoryHouse(opt);
			} catch (Exception e) {
				redisCacheManager.leftPushList(key, result);
			}

		} catch (Exception e) {
			logger.error("操作记录保存到数据库失败" + e.getMessage());
		}
	}

	
	/**
     * 收集房源操作历史
     * 
     * @param crawlHouseDetail
     */
    private void collectionHistoryHouse(HouseOptHistoryRedisDto dto) throws Exception {
        boolean  savaFlag = false;
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
                    savaFlag = true;
                }

                //3.查询房源详细信息
                HouseDetail houseDetail = houseDetailRepository.findAllDeleteBySellId(dto.getSellId());
                if (houseDetail == null) {
                    logger.error(LogUtils.getCommLog("房源操作历史houseDetail查询失败,数据库事务未提交, sellId:" + sellId));
                    savaFlag = true;
                }else{
                    if(houseDetail.getApproveStatus() == ApproveStatusEnum.WAITTING_APPROVE.getCode() 
                            || houseDetail.getApproveStatus() == ApproveStatusEnum.IMG_APP_TEMP.getCode()){//房源未机审
                        logger.error(LogUtils.getCommLog("roomBase房源未审核，待查看原因, roomId:" + roomId));
                        savaFlag = true;
                    }  
                    
                    
                    if(houseDetail.getIsRun() == Constants.HouseBase.IS_RUN_NO){//房源未收集
                        savaFlag = true;
                    }
                }

              //4.查询房源信息
                RoomBase roomBase = null;
                if (dto.getRoomId() > 0) {
                    roomBase = roomBaseRepository.findALLDeleteByRoomId(roomId);
                    if (roomBase == null) {
                        logger.error(LogUtils.getCommLog("roomBase查询失败,数据库事务未提交, roomId:" + roomId));
                        savaFlag = true;
                    }else{
                        if(roomBase.getApproveStatus() == ApproveStatusEnum.WAITTING_APPROVE.getCode() 
                                || roomBase.getApproveStatus() == ApproveStatusEnum.IMG_APP_TEMP.getCode()){//房源未机审
                            logger.error(LogUtils.getCommLog("roomBase房源未审核，待查看原因, roomId:" + roomId));
                            savaFlag = true;
                        }  
                    }
                } 
                
                
                
                if(savaFlag){//消息回退到队列
                    redisCacheManager.leftPushList(key, GsonUtils.getInstace().toJson(dto)); 
                    TimeUnit.SECONDS.sleep(60);
                    return;
                }
                
                saveHisRecord(houseDetail,houseBase,roomBase,dto);
            }
            
            logger.info(LogUtils.getCommLog(String.format("保存房源操作记录房源ID(%s)房间ID(%s)完成", dto.getSellId(),dto.getRoomId())));
        } catch (Exception e) {
            redisCacheManager.leftPushList(key, GsonUtils.getInstace().toJson(dto));
            logger.error(LogUtils.getCommLog(
                    String.format("房源操作历史数据ID(%s)处理失败:%s", dto.getSellId(), ExceptionUtils.getStackTrace(e))));
            TimeUnit.SECONDS.sleep(30);
        }
    
    }
    
    public void saveHisRecord(HouseDetail houseDetail, HouseBase houseBase, RoomBase roomBase,
                                 HouseOptHistoryRedisDto dto) {

        //房源
        if (dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_ADD 
                || dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_UPDATE 
                || dto.getOptType() == Constants.HouseOptTypeUtil.HOUSE_OPT_DELETE) {
            buildHouseOptDto(houseBase,houseDetail,dto.getOptType());
        }
        
       //房间
        if (dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_ADD 
                || dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_UPDATE 
                || dto.getOptType() == Constants.HouseOptTypeUtil.ROOM_OPT_DELETE) {
            buildRoomOptDto(houseDetail,houseBase,roomBase,dto.getOptType());
        }
    }
    
    /**
     * 构建房源操作记录对象
     * @param pubDto
     * @param sellId
     * @param roomId
     * @return
     */
    private void buildHouseOptDto(HouseBase houseBase, HouseDetail houseDetail,Integer optType) {

        HouseOpereteRecord optRec = new HouseOpereteRecord();
        optRec.setCompanyId(houseBase.getCompanyId());
        optRec.setCompanyName(houseBase.getCompanyName());
        optRec.setSource(houseDetail.getSource());
        optRec.setCity(houseDetail.getCity());
        optRec.setCityId(houseDetail.getCityId());
        optRec.setDistrict(houseDetail.getDistrict());
        optRec.setDistrictId(houseDetail.getDistrictId());
        optRec.setBizId(houseDetail.getBizId());
        optRec.setBizName(houseDetail.getBizName());
        optRec.setHouseSellId(houseBase.getSellId());
        optRec.setApproveStatus(houseDetail.getApproveStatus());
        optRec.setEntireRent(houseDetail.getEntireRent());
        optRec.setHouseStatus(houseBase.getStatus());
        optRec.setBaiduLa(houseDetail.getBaiduLatitude());
        optRec.setBaiduLo(houseDetail.getBaiduLongitude());
        optRec.setCreateTime(houseDetail.getUpdateTime());
        optRec.setUpdateTime(houseDetail.getUpdateTime());
        optRec.setOptType(optType);
        operateRecordRepository.save(optRec);
    }
    
    /**
     * 构建房间操作记录对象
     * @param pubDto
     * @param sellId
     * @param roomId
     * @return
     */
    private void buildRoomOptDto(HouseDetail houseDetail,HouseBase houseBase,RoomBase roomBase, Integer optType) {

        HouseOpereteRecord optRec = new HouseOpereteRecord();
        optRec.setCompanyId(houseBase.getCompanyId());
        optRec.setCompanyName(houseBase.getCompanyName());
        optRec.setEntireRent(houseDetail.getEntireRent());
        optRec.setHouseStatus(roomBase.getStatus());
        optRec.setSource(houseDetail.getSource());
        optRec.setHouseSellId(roomBase.getSellId());
        optRec.setRoomId(roomBase.getId());
        optRec.setCity(houseDetail.getCity());
        optRec.setCityId(houseDetail.getCityId());
        optRec.setDistrict(houseDetail.getDistrict());
        optRec.setDistrictId(houseDetail.getDistrictId());
        optRec.setBizId(houseDetail.getBizId());
        optRec.setBizName(houseDetail.getBizName());
        optRec.setApproveStatus(roomBase.getApproveStatus());
        optRec.setOptType(optType);   
        optRec.setCreateTime(roomBase.getUpdateTime());
        optRec.setUpdateTime(roomBase.getUpdateTime());
        operateRecordRepository.save(optRec);
    }
    
}
