package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;

@Service
public class SetDefaultImageTask {

    @Resource
    private HouseDetailRepository houseDetailRepository;

    @Resource
    private RoomBaseRepository roomBaseRepository;

    @Resource
    private HousePictureRepository housePictureRepository;

    @Resource
    private TaskConfiguration taskConfiguration;

    private static Logger logger = LoggerFactory.getLogger(SetDefaultImageTask.class);

    @Scheduled(cron = "0 20 15 25 12 *") // 每五分钟执行一次
    public void setDefaultImageTask() {

		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("定时任务未开启，不能执行设置首图定时任务");
			return;
		}

        // 设置房源首图
        long startTime = System.currentTimeMillis();

        int totalPage = 1;
        for (int i = 0; i < totalPage; i++) {
            Pageable pageable = new PageRequest(i, 200);// 分页查询
            Page<HouseDetail> pageList = houseDetailRepository.selectHouseNotDeleted(pageable, 1);
            if (totalPage == 1) {
                totalPage = pageList.getTotalPages();
            }
            List<HouseDetail> detailList = pageList.getContent();
            for (HouseDetail detail : detailList) {
                List<HousePicture> picList = housePictureRepository.findVaildBySellIdAndRoomId(detail.getSellId(), 0,
                        Constants.Common.STATE_IS_DELETE_NO);
                try {
                    handelDefaultImg(picList);
                } catch (Exception e) {
                    logger.error("设置首图失败，sellId={}", detail.getSellId());
                }
            }
            if (i == totalPage) {
                break;
            }
            logger.info("房源第{}页完毕，共{}页", i + 1, totalPage);
        }

        // 设置房间首图
        totalPage = 1;
        for (int i = 0; i < totalPage; i++) {
            Pageable pageR = new PageRequest(i, 200);// 分页查询
            Page<RoomBase> roomPageList = roomBaseRepository.selectRoomNotDeleted(pageR);
            if (totalPage == 1) {
                totalPage = roomPageList.getTotalPages();
            }
            List<RoomBase> roomBaseList = roomPageList.getContent();
            for (RoomBase room : roomBaseList) {
                List<HousePicture> picList = housePictureRepository.findVaildBySellIdAndRoomId(room.getSellId(),
                        room.getId(), Constants.Common.STATE_IS_DELETE_NO);

                try {
                    handelDefaultImg(picList);
                } catch (Exception e) {
                    logger.error("设置首图失败，roomId={}", room.getId());
                }

            }
            if (i == totalPage) {
                break;
            }
            logger.info("房间第{}页完毕，共{}页", i + 1, totalPage);
        }
        logger.error("本次设置首图任务结束，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    /**
     * 如果没有首图，则选择第一张为首图
     * 
     * @param picList
     */
    private void handelDefaultImg(List<HousePicture> picList) {
        if (CollectionUtils.isNotEmpty(picList)) {
            boolean flag = true;
            for (HousePicture pic : picList) {
                if (pic.getIsDefault() == 1) {
                    flag = false;
                }
            }
            HousePicture picture = picList.get(0);
            if (flag) {
                picture.setIsDefault(1);
                housePictureRepository.save(picture);
                logger.info("sell={}, roomId={}已将id={}的图片设为首图", picture.getSellId(), picture.getRoomId(),
                        picture.getId());
            } else {
                logger.info("已存在首图,sell={}, roomId={}", picture.getSellId(), picture.getRoomId());
            }
        }
    }
}
