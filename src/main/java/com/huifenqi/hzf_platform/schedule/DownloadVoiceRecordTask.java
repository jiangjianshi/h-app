package com.huifenqi.hzf_platform.schedule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.context.entity.phone.CallInfoRecord;
import com.huifenqi.hzf_platform.dao.repository.house.CallInfoRecordRepository;
import com.huifenqi.hzf_platform.handler.SecretPhoneHandler;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.HttpFileUtil;
import com.huifenqi.hzf_platform.utils.OSSClientUtils;

@Service
public class DownloadVoiceRecordTask {

	@Resource
	private CallInfoRecordRepository callInfoRecordRepository;
	@Resource
	private SecretPhoneHandler secretPhoneHandler;
	@Resource
	private OSSClientUtils OSSClient;

	private static Logger logger = LoggerFactory.getLogger(DownloadVoiceRecordTask.class);

	private static final String voice_path = "voice_record/";

	@Scheduled(cron = "${hfq.voice.record.download}") // 每五分钟执行一次
	public void downloadVoiceRecord() {

		int totalPage = 1;
		for (int i = 0; i < totalPage; i++) {
			Pageable pageable = new PageRequest(i, 200);// 分页查询
			Date date = DateUtils.addMinutes(new Date(), 59);//59分钟之前
			Page<CallInfoRecord> pageList = callInfoRecordRepository.findValidCallInfoRecord(pageable, date);
			if (totalPage == 1) {
				
				totalPage = pageList.getTotalPages();
			}
			List<CallInfoRecord> callInfoList = pageList.getContent();
			for (CallInfoRecord call : callInfoList) {

				String downloadUrl = secretPhoneHandler.getVoiceRecordUrl(call.getCallId(), call.getCallTime());
				String storePath = "";
				try {
					storePath = uploadToOss(downloadUrl);
				} catch (Exception e) {
					logger.error("下载录音出错.");
				}

				if (!StringUtils.isEmpty(storePath)) {
					call.setVoiceRecordUrl(storePath);
					call.setUpdateTime(new Date());
					callInfoRecordRepository.save(call);
				}
			}
			if (i == totalPage) {
				break;
			}
			logger.info("录音第{}页完毕，共{}页", i + 1, totalPage);
		}
	}

	private String uploadToOss(String downloadUrl) {

		if (StringUtils.isEmpty(downloadUrl)) {
			return "";
		}
		String ext = HttpFileUtil.getUrlExtendtion(downloadUrl);
		String ossPath = voice_path + DateUtil.formatCurrentDate() + File.separator + System.currentTimeMillis() + "_"
				+ new Random().nextInt(1000) + ext;

		File voiceRecord = new File("/tmp/voice_record");
		try {
			FileUtils.copyInputStreamToFile(HttpFileUtil.getInputStreamByUrl(downloadUrl), voiceRecord);
			OSSClient.upload(voiceRecord, ossPath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下载录音失败", e);
		}

		return ossPath;
	}
}
