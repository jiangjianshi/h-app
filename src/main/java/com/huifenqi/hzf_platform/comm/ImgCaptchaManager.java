package com.huifenqi.hzf_platform.comm;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.huifenqi.hzf_platform.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by YDM
 */
@org.springframework.context.annotation.Configuration
public class ImgCaptchaManager {

    protected static Logger logger = LoggerFactory.getLogger(ImgCaptchaManager.class);

    @Autowired
    private Producer kaptcha;

    @Autowired
    private RedisClient redisClient;

    private static final String IMGCAPTCHA_CACHE_KEY = "image_captcha";

    private static int width = 280;

    private static int height = 70;
    
    private static int MAX_IMAGE_PER_DAY_NUM = 50;
    
    private Object syncObject = new Object();
    
    //图形验证码失效时间为10分钟
    private static long IMAGE_CACHE_TIMEOUT = 10 * 60 * 1000;

    @Bean
    public Producer getImgCaptchaProducer() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.char.string", "0987654321");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.background.clear.from", "gray");
        properties.setProperty("kaptcha.image.width", width + "");
        properties.setProperty("kaptcha.image.height", height + "");

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);

        return kaptcha;
    }

    public Producer getImgCaptchaProducer(int width, int height) {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.char.string", "0987654321");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.background.clear.from", "gray");
        properties.setProperty("kaptcha.image.width", width + "");
        properties.setProperty("kaptcha.image.height", height + "");

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);

        return kaptcha;
    }

    
    public BufferedImage createImage(String id) {
        String text = kaptcha.createText();

        logger.info("[" + id + "]request for image captcha, text=" + text);

//        redisClient.setInMap(IMGCAPTCHA_CACHE_KEY, id, text);
        
        redisClient.set(getImageTextKey(id), text, IMAGE_CACHE_TIMEOUT);

        return kaptcha.createImage(text);
    }
    
    public BufferedImage createImage(String id, int width, int height) {
    	Producer kaptcha = getImgCaptchaProducer(width, height);
        String text = kaptcha.createText();

        logger.info("[" + id + "]request for image captcha, text=" + text);

//        redisClient.setInMap(IMGCAPTCHA_CACHE_KEY, id, text);
        
        redisClient.set(getImageTextKey(id), text, IMAGE_CACHE_TIMEOUT);
        
        return kaptcha.createImage(text);
    }

    public boolean verify(String id, String text) {
//        String cachedText = (String) redisClient.getFromMap(IMGCAPTCHA_CACHE_KEY, id);
    	String cachedText = String.valueOf(redisClient.get(getImageTextKey(id)));

        logger.info("verify img captcha, text=" + text +", cachedText=" + cachedText);

        text = StringUtil.trimToEmpty(text);
        if (StringUtil.equalsIgnoreCase(cachedText, text)) {
//            redisClient.deleteFromMap(IMGCAPTCHA_CACHE_KEY, id);
        	redisClient.delete(getImageTextKey(id));
            return true;
        }

        return false;
    }

    /*/
      返回值 0 验证码错误
      返回值 1 校验成功
      返回值 2 redis中数据已删除，提醒用户重新获取验证码。
     */
    public int verifyImage(String id, String text) {
//        String cachedText = (String) redisClient.getFromMap(IMGCAPTCHA_CACHE_KEY, id);
        String cachedText = String.valueOf(redisClient.get(getImageTextKey(id)));

        logger.info("verify img captcha, text=" + text +", cachedText=" + cachedText);

        if(StringUtil.isEmpty(cachedText)){
            logger.info(" img captcha text=" + text +", cachedText is invalid");
            return 2;
        }

        text = StringUtil.trimToEmpty(text);
        if (StringUtil.equalsIgnoreCase(cachedText, text)) {
//            redisClient.deleteFromMap(IMGCAPTCHA_CACHE_KEY, id);
            redisClient.delete(getImageTextKey(id));
            return 1;
        }

        return 0;
    }
    /**
     * 判断图形验证码是否达到了上线
     * @param clientIp
     * @param phone
     * @return
     */
    public boolean reachImageLimt(String clientIp, String phone) {
    	Object currentIpNum = redisClient.get(getLimitImageKey(clientIp));
    	//update by arison
    	if (currentIpNum == null || StringUtil.isRedisBlank(String.valueOf(currentIpNum))) {
    		return false;
    	}
    	//判断该IP是否达到上限
    	if (Integer.valueOf(String.valueOf(currentIpNum))>= MAX_IMAGE_PER_DAY_NUM * 2) {
    		return true;
    	}
    	
    	Object currentPhoneNum = redisClient.get(getLimitImageKey(phone));
    	if (currentPhoneNum == null || StringUtil.isRedisBlank(String.valueOf(currentPhoneNum))) {
    		return false;
    	}
    	//判断电话号码是否达到上限
    	if (Integer.valueOf(String.valueOf(currentPhoneNum))>= MAX_IMAGE_PER_DAY_NUM) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * 增加图形验证码的请求次数
     * @param clientIp
     * @param phone
     */
    public void increseImageReqLimit(String clientIp, String phone) {
    	//判断IP是否为第一次，如果是则设置超时时间
    	synchronized (syncObject) {
    		Object currentIpNum = redisClient.get(getLimitImageKey(clientIp));
        	if (currentIpNum == null || StringUtil.isRedisBlank(String.valueOf(currentIpNum))) {
        		redisClient.set(getLimitImageKey(clientIp), 0, getTimeOut());
        	}
        	
        	Object currentPhoneNum = redisClient.get(getLimitImageKey(phone));
        	if (currentPhoneNum == null || StringUtil.isRedisBlank(String.valueOf(currentIpNum))) {
        		redisClient.set(getLimitImageKey(phone), 0, getTimeOut());
        	}
        	redisClient.increment(getLimitImageKey(clientIp), 1);
        	
        	redisClient.increment(getLimitImageKey(phone), 1);
		}
    }
    
    
    
    /**
     * 获取上限前缀
     * @param prefix
     * @return
     */
    private String getLimitImageKey(String prefix) {
    	return IMGCAPTCHA_CACHE_KEY + "_" + prefix;
    }
    
    /**
     * 获取图形验证码前缀
     * @param prefix
     * @return
     */
    private String getImageTextKey(String phone) {
    	return IMGCAPTCHA_CACHE_KEY  + "_text_" + phone;
    }
    
	/**
	 * 获取key的超时时间 当天24点前有效
	 * 
	 * @return
	 */
	private long getTimeOut() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		long timeOut = calendar.getTimeInMillis() - System.currentTimeMillis();
		return timeOut;
	}
}
