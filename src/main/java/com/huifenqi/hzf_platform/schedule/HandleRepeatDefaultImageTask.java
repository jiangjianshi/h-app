package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;

@Service
public class HandleRepeatDefaultImageTask {

    @Resource
    private TaskConfiguration taskConfiguration;

    @Resource
    private HouseDetailRepository houseDetailRepository;

    @Resource
    private HousePictureRepository housePictureRepository;

    @Resource
    private HandleRepeatDefaultImageService handleRepeatDefaultImageService;

    private static Logger logger = LoggerFactory.getLogger(HandleRepeatDefaultImageTask.class);

    @Scheduled(cron = "0 15 23 ? * *") // 每天23:15分执行
    public void filterHouseErrorData() {
        
        if (!taskConfiguration.isScheduleStatus()) {
            logger.info("定时任务未开启，不能执行设置首图定时任务");
            return;
        }
        
        // 处理重复首图
        long startTime = System.currentTimeMillis();

        int totalPage = 1;
        for (int i = 0; i < totalPage; i++) {
            Pageable pageable = new PageRequest(i, 200);// 分页查询
            Page<HousePicture> pageList = housePictureRepository.findRepeatDefaultImage(pageable);
            if (totalPage == 1) {
                totalPage = pageList.getTotalPages();
            }
            List<HousePicture> pictureList = pageList.getContent();
            for (HousePicture pic : pictureList) {

                if (pic.getRoomId() == 0) {
                    HouseDetail detail = houseDetailRepository.findBySellId(pic.getSellId());
                    if (detail == null) {
                        continue;
                    }
                    if (detail.getEntireRent() == 0) {
                        continue;
                    }
                }
                try {
                    handleRepeatDefaultImageService.handleRepeatDefaultImg(pic);
                } catch (Exception e) {
                    logger.error("处理重复首图失败。sellId={}, roomId={}", pic.getSellId(), pic.getRoomId(), e);
                }

            }
            if (i == totalPage) {
                break;
            }
            logger.info("处理重复首图第{}页完毕，共{}页", i + 1, totalPage);
        }

        logger.error("本次处理重复首图任务结束，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

}
