package com.huifenqi.hzf_platform.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.context.exception.LackParameterException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author chenshuai
 * @Date 2017/4/15 0015 16:57
 */
public class BdRequestUtils {

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static String getParameterString(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        return value.trim();
    }

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static String getParameterString(Map<String,String> map, String key, String defaultValue)
            throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value.trim();
    }

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static long getParameterLong(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Long longValue = null;
        try {
            longValue = Long.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return longValue;
    }

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static long getParameterLong(Map<String,String> map, String key, long defaultValue) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        Long longValue = null;
        try {
            longValue = Long.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return longValue;
    }

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static int getParameterInt(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Integer intValue = null;
        try {
            intValue = Integer.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return intValue;
    }

    /**
     * 获取参数
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static int getParameterInt(Map<String,String> map, String key, int defaultValue) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        Integer intValue = null;
        try {
            intValue = Integer.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return intValue;
    }

    /**
     * 获取double参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static double getParameterDouble(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Double doubleValue = null;
        try {
            doubleValue = Double.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return doubleValue;
    }

    /**
     * 获取double参数
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static double getParameterDouble(Map<String,String> map, String key, int defaultValue) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        Double doubleValue = null;
        try {
            doubleValue = Double.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return doubleValue;
    }

    /**
     * 获取float参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static float getParameterFloat(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Float floatValue = null;
        try {
            floatValue = Float.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return floatValue;
    }

    /**
     * 获取float参数
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static float getParameterFloat(Map<String,String> map, String key, int defaultValue) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        Float floatValue = null;
        try {
            floatValue = Float.valueOf(value.trim());
        } catch (Exception e) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return floatValue;
    }

    /**
     * 获取日期参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static Date getParameterDate(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Date parseDate = DateUtil.parseDate(value);
        if (parseDate == null) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return parseDate;
    }

    /**
     * 获取日期时间参数
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    public static Date getParameterDateTime(Map<String,String> map, String key) throws Exception {
        String value = map.get(key);
        if (StringUtils.isBlank(value)) {
            throw new LackParameterException("缺少参数:" + key);
        }
        Date parseDate = DateUtil.parseDateTime(value);
        if (parseDate == null) {
            throw new InvalidParameterException("参数异常:" + key);
        }
        return parseDate;
    }

    /**
     * 获取API参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getApiParams(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>();
        // 从HtpServletRequest中解析原始参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = "";
            if (entry.getValue() != null && entry.getValue().length > 0) {
                value = entry.getValue()[0];
            }

            // 参数放入的时候对两端的空格做处理
            paramsMap.put(entry.getKey(), StringUtil.trim(value));
        }
        return paramsMap;
    }

    /**
     * 获取data参数  封装到map
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String,String> handlerDataToMap(HttpServletRequest request) throws Exception {

		/*HzfRequestWrapper wrapper = new HzfRequestWrapper(request);
		String body = wrapper.getBody();

		JsonObject json = new Gson().fromJson(body, JsonObject.class);
		Set<Map.Entry<String, JsonElement>> entries = json.entrySet();*/
    	Map<String, String> resultMap = null;
		try{
			Gson gson = new Gson();
		    resultMap = new HashMap<>();
		    String data = RequestUtils.getParameterString(request, "data");
		    JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
		    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
		    for (Map.Entry<String, JsonElement> entry : entries) {
		        if(entry.getValue() instanceof JsonArray){
		            resultMap.put(entry.getKey(), gson.toJson(entry.getValue()));
		        }else{
		            resultMap.put(entry.getKey(), entry.getValue().getAsString());
		        }
		    }
		}catch(Exception e){
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, String.format("解析data请求异常"));
		}
        return resultMap;
    }

    public static String getParameterFormMap(HttpServletRequest request, String key) throws Exception {

        if (StringUtils.isBlank(key))
            return null;

        Map<String, String> map = handlerDataToMap(request);
        return map.get(key);
    }
}
