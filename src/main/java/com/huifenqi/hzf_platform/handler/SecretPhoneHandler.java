package com.huifenqi.hzf_platform.handler;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse.SecretBindDTO;
import com.huifenqi.hzf_platform.configuration.SecretPhoneConfiguration;
import com.huifenqi.hzf_platform.context.entity.phone.BindRecord;
import com.huifenqi.hzf_platform.context.entity.phone.CallInfoRecord;
import com.huifenqi.hzf_platform.context.entity.phone.CallReport;
import com.huifenqi.hzf_platform.context.entity.phone.Message;
import com.huifenqi.hzf_platform.context.entity.phone.SecretPhone;
import com.huifenqi.hzf_platform.context.enums.SecretNoAssignStatus;
import com.huifenqi.hzf_platform.dao.repository.house.BindRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CallInfoRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.SecretPhoneRepository;
import com.huifenqi.hzf_platform.monitor.UnbindConsumer;
import com.huifenqi.hzf_platform.service.SecretPhoneService;
import com.huifenqi.hzf_platform.service.SmsService;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.SecretPhoneUtil;

@Service
public class SecretPhoneHandler {

	private static Logger logger = LoggerFactory.getLogger(SecretPhoneHandler.class);

	@Resource
	private SecretPhoneRepository secretPhoneRepository;

	@Resource
	private CallInfoRecordRepository callInfoRecordRepository;
	
	@Resource
	private BindRecordRepository bindRecordRepository;
	
	@Resource
	private SecretPhoneService secretPhoneService;
	
	@Resource
	private SmsService smsService;
	
	@Resource
	private SecretPhoneConfiguration secretconfig;

	static  DelayQueue<Message> queue = new DelayQueue<Message>();//延时队列
	
	static ExecutorService executor = null;//初始化线程池
	static {
		executor = Executors.newFixedThreadPool(2);
	}
	
	
	/**
	 * 获取虚拟号
	 * 
	 * @param req
	 * @return
	 */
	public String getUnbingSectetPhone(String sellId, String roomId, String agencyPhone) {

		String secretNo = agencyPhone;//保护号
		Integer assignStatus = SecretNoAssignStatus.ASSIGNED.getCode();//默认成功
		
		BindRecord record = new BindRecord();//绑定记录

		if (SecretPhoneUtil.checkTelPhone(agencyPhone, false)) {//如果不是手机号，则返回原来的号码

			int updateSucc = 0;
			SecretPhone secret = secretPhoneRepository.findFirst1ByStatusAndBindStatusOrderByUpdateTimeAsc(1, 0);
			if (!Objects.isNull(secret)) {
				updateSucc = secretPhoneRepository.updateBindStatusByVersion(1, secret.getVersion() + 1,
						secret.getSecretPhoneNo(), secret.getVersion());
				while (updateSucc == 0) {//解决并发的时绑定冲突的问题
					secret = secretPhoneRepository.findFirst1ByStatusAndBindStatusOrderByUpdateTimeAsc(1, 0);
					if (!Objects.isNull(secret)) {
						updateSucc = secretPhoneRepository.updateBindStatusByVersion(1, secret.getVersion() + 1,
								secret.getSecretPhoneNo(), secret.getVersion());
					}
				}
				String outId = getOutId();//唯一绑定号
				SecretBindDTO bindDto = secretPhoneService.bindAxn(agencyPhone.replace("-", ""), secret.getSecretPhoneNo(), outId);
				if (Objects.nonNull(bindDto)) {
					secretNo = bindDto.getSecretNo();//给虚拟号赋值

					record.setSecretNo(bindDto.getSecretNo());
					record.setSubId(bindDto.getSubsId());
					record.setOutId(outId);
					//延时解绑
					offerUnbindMsg(bindDto.getSubsId(), bindDto.getSecretNo());
				} else {
					logger.error("虚拟号绑定失败。");
					assignStatus = SecretNoAssignStatus.BIND_FAIL.getCode();
					secretPhoneRepository.updateBindStatusById(0, secret.getId());//绑定失败后，将更新状态改为未绑定
				}
			} else {
				assignStatus = SecretNoAssignStatus.SECRETNO_NOT_ENOUGH.getCode();
				logger.error("没有未绑定的虚拟号。");
			}
		} else {
			assignStatus = SecretNoAssignStatus.PHONE_NOT_ALLOW.getCode();
			logger.error("此经纪人电话无法转接，agencyPhone={}", agencyPhone);
		}

		record.setSellId(sellId);
		record.setRoomId(Long.parseLong(roomId));
		record.setAgencyPhone(agencyPhone);
		record.setAssignStatus(assignStatus);
		bindRecordRepository.save(record);
		return secretNo;
	}

	/**
	 * 生产延时解绑消息
	 * 
	 * @param secretId
	 */
	private void offerUnbindMsg(String subId, String secretNo) {

		Message unbindMsg = new Message(subId, secretNo, 40000);
		queue.offer(unbindMsg);
		executor.execute(new UnbindConsumer(queue, this));
	}

	/**
	 * 更新绑定状态，解绑
	 * 
	 * @param secretId
	 */
	public boolean unbind(String subsId, String secretNo) {
		boolean result = true;
		if (secretPhoneService.unbind(subsId, secretNo)) {
			List<SecretPhone> phoneList = secretPhoneRepository.findBySecretPhoneNo(secretNo);
			if (CollectionUtils.isNotEmpty(phoneList)) {
				SecretPhone phone = phoneList.get(0);
				phone.setBindStatus(0);
				phone.setUpdateTime(new Date());
				secretPhoneRepository.save(phone);
			}
		} else {
			result = false;
			logger.error("解绑失败, subsId={},secretNo={}", subsId, secretNo);
		}
		return result;
	}

	public void saveCallInfoRecord(CallReport report) {
		
		sendSms(report);//发送短信
		
		String outId = report.getOut_id();
		Date startTime = DateUtil.parseDateTime(report.getStart_time());
		Date relaseTime = DateUtil.parseDateTime(report.getRelease_time());
		long diffsec = (relaseTime.getTime() - startTime.getTime()) / 1000;
		
		CallInfoRecord callInfo = new CallInfoRecord();
		callInfo.setOutId(outId);
		callInfo.setCallId(report.getCall_id());
		callInfo.setCustomPhone(report.getPeer_no());
		callInfo.setReleaseDir(report.getRelease_dir());
		callInfo.setCallDuration(Integer.parseInt(diffsec + ""));
		callInfo.setVoiceRecordUrl("");
		callInfo.setCallTime(report.getCall_time());
		callInfo.setCreateTime(new Date());
		callInfo.setUpdateTime(new Date());
		callInfoRecordRepository.save(callInfo);
	}
	
	/**
	 * 发送短信
	 * @param report
	 */
	private void sendSms(CallReport report){
		if (SecretPhoneUtil.checkTelPhone(report.getPhone_no(), true)) {
			JSONObject datas = new JSONObject();
			datas.put("k1", report.getPeer_no());
			try {
				smsService.sendCommonSMS(report.getPhone_no(), secretconfig.secretPhoneSmsTemplateId,
						datas.toJSONString());
			} catch (Exception e) {
				logger.error("短信发送失败.");
			}
		}
		
	}
	
	/**
	 * 获取唯一业务编号，来区分唯一绑定
	 * @return
	 */
	private String getOutId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/**
	 * 获取通话录音
	 * @param callId
	 * @param callTime
	 */
	public String getVoiceRecordUrl(String callId, String callTime){
		
		return secretPhoneService.getVoiceRecordUrl(callId, callTime);
	}
}
