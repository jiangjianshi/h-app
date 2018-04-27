package com.huifenqi.hzf_platform.schedule;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;

@Service
@Transactional
public class HandleRepeatDefaultImageService {

	@Resource
	private HouseDetailRepository houseDetailRepository;
	@Resource
	private HousePictureRepository housePictureRepository;

	private static Logger logger = LoggerFactory.getLogger(HandleRepeatDefaultImageService.class);

	public void handleRepeatDefaultImg(HousePicture pic) {

		List<HousePicture> picList = housePictureRepository.findNotDefaultBySellIdAndRoomId(pic.getSellId(),
				pic.getRoomId());
		if (picList.size() >= 1) {
			int index = new Random().nextInt(picList.size());// 随机选取一张作为首图
			HousePicture newDefaultImage = picList.get(index);
			newDefaultImage.setIsDefault(1);
			housePictureRepository.save(newDefaultImage);
			pic.setIsDefault(0);
			housePictureRepository.save(pic);
			logger.info("sellId={}, roomId={}的首图由{}改为{}", pic.getSellId(), pic.getRoomId(), pic.getId(),
					newDefaultImage.getId());
		} else {
			logger.info("本房源只有一张图片，不做重复首图处理。sellId={}, roomId={}", pic.getSellId(), pic.getRoomId());
		}
	}

}
