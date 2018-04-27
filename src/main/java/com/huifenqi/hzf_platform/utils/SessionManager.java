package com.huifenqi.hzf_platform.utils;

import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.comm.Request;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by arison on 2015/9/23.
 */
@Component
public class SessionManager {

    @Autowired
    private RedisClient redisClient;

    /**
     * app会话过期时间90天
     */
    public static final long APP_SESSION_TIMEOUT = 90 * 24 * 3600 * 1000l;

    /**
     * web会话过期时间90天
     */
    public static final long WEB_SESSION_TIMEOUT = 90 * 24 * 3600 * 1000l;

    /**
     * 测试会话时间：1min
     */
    public static final long TEST_SESSION_TIMEOUT = 1 * 60 * 1000;

    private static Logger logger = LoggerFactory.getLogger(SessionManager.class);



    public long getUserId(String sessionId) {
        Object deserializedUserId = redisClient.get(sessionId);
        return StringUtil.parseLong(String.valueOf(deserializedUserId), 0);
    }

    /**
     * 将user设置到session中
     *
     * @param userId
     * @param timeout
     * @return
     */
    public String setUserId(long userId, long timeout) {
        // TODO 这里为什么获取的是null
//        String sessionId = redisTemplate.randomKey();
        String rawString = userId + "-" + System.currentTimeMillis();
        String sessionId = DigestUtils.md5Hex(rawString);
        redisClient.set(sessionId, userId + "", timeout / 1000, TimeUnit.SECONDS);

        logger.info(LogUtil.formatLog("user has been set in session, session id=" + sessionId + ", timeout=" + timeout));

        return sessionId;
    }

    /**
     * 清除sid
     *
     * @param sessionId
     */
    public void removeSessionId(String sessionId) {
        redisClient.delete(sessionId);

        logger.info(String.format("session id %s has been deleted", sessionId));
    }
    
    /**
	 * @Title: getUserIdFromSession
	 * @Description: 通过Session获取userId
	 * @return long
	 * @author 叶东明
	 * @dateTime 2017年8月21日 上午10:37:30
	 */
	public long getUserIdFromSession() {
		Request request = Request.getRequest();
		return this.getUserId(request.getSessionId());
	}
}
