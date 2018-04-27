package com.huifenqi.hzf_platform.utils;

import org.apache.commons.lang3.StringUtils;

import com.huifenqi.hzf_platform.comm.Request;

/**
 * Created by arison on 2015/9/5.
 */
public class LogUtil {

    /**
     * 个性化定义日志输出
     * TODO 应该获取并输出用户的ip和phone，获取的代码还有待完善
     *
     * @param msg
     * @return
     */
    public static String formatLog(String msg) {
        Request request = Request.getRequest();

        String id = null ;
        if (request != null) {
        	id = request.getId();
        }
        String log = "";
        if (StringUtils.isNotEmpty(id)) {
            log += "[" + id + "]";
        }

        log += msg;

        return log;
    }
}
