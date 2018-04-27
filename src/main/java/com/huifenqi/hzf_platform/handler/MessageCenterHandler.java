package com.huifenqi.hzf_platform.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.comm.UserUtils;
import com.huifenqi.hzf_platform.context.dto.params.PushRecordDto;
import com.huifenqi.hzf_platform.context.exception.NoSuchUserException;
import com.huifenqi.hzf_platform.service.MqService;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.usercomm.domain.User;

@Service
public class MessageCenterHandler {

	@Resource
	private SessionManager sessionManager;

	@Resource
	private MqService mqService;

	@Resource
	private UserUtils userUtils;

	@Resource
	protected Configuration configuration;

	protected static Logger logger = LoggerFactory.getLogger(MessageCenterHandler.class);

	public List<PushRecordDto> getPushMessageByPage(Integer pageSize, Integer pageNum) {

		long userId = sessionManager.getUserIdFromSession();
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：userId=" + userId);
			throw new NoSuchUserException(userId);
		}

		Map<String, String> params = new HashMap<>();
		params.put("page_num", pageNum + "");
		params.put("page_size", pageSize + "");
		params.put("sort_type", "1");
		params.put("order_type", "1");
		params.put("msg_channel", "0");//渠道ID
		params.put("user_type", "0");//0：租户；（客户端用户, 1：经纪人：（broker）
		params.put("create_time_from", DateUtil.formatDateTime(DateUtils.addYears(new Date(), -2)));
		params.put("msg_touser", user.getPhone());
		List<PushRecordDto> list = mqService.getPushMsg(params);
		for(PushRecordDto rec :list){
			handleCreateTime(rec);
			rec.setCreateTimeDesc(handleCreateTime(rec));
		}
		return list;
	}
	
	
	/**
	 * 若是当天，则展现HH：MM 若是一天前，展示的是昨天 HH：MM；若是两天前，则展现 前天 HH：MM
	 * 若是三天之前今年的信息，则展现的是当天的日期“12.20” 若是三天之前年份中的信息，则展现的是当天年月日“2016.12.20”
	 * 
	 * @param record
	 * @return
	 */
	private static String handleCreateTime(PushRecordDto record) {
		String createTime = record.getCreateTime();
		String createTimeDesc = "";
		int difDay = DateUtil.getDiffDays(createTime);
		int difYear = DateUtil.getDiffYear(createTime);
		if (difYear >= 1) {//前年
			return DateUtil.replaceSpliter(createTime.split(" ")[0]);
		}
		Date date = DateUtil.parseDateTime(createTime);
		switch (difDay) {
		case 0://今天
			createTimeDesc = DateUtil.format("HH:mm", date);
			break;
		case -1://昨天
			createTimeDesc = "昨天 " + DateUtil.format("HH:mm", date);
			break;
		case -2://前天
			createTimeDesc = "前天 " + DateUtil.format("HH:mm", date);
			break;
		}
		if (difDay <= -3) {//三天之前,今年的信息
			createTimeDesc = DateUtil.format("MM.dd", date);
		}
		return createTimeDesc;
	}
	
}
