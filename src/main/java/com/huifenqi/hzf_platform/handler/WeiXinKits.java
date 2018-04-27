package com.huifenqi.hzf_platform.handler;

import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by stargaer on 2015/11/15.
 */
@Component
public class WeiXinKits {

	private static Logger logger = LoggerFactory.getLogger(WeiXinKits.class);

	public static final Set<String> WX_CHANNEL_CONF = new HashSet<>();

	public static final String AUTH_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";

	public static final String AUTH_GET_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

	// 获取微信接口凭证的地址
	public static final String AUTH_GET_API_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	// 获取微信接口的jsapi_ticket
	public static final String AUTH_GET_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

	// 从微信下载文件
	public static final String WX_FILE_DOWNLOAD = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";

	// 授权时间,1个小时有效
	public static final long AUTH_TIMEOUT = 1 * 60 * 60 * 1000;

	public static final String WX_API_ACCESSTOKEN_PREFIX = "wx.api.accesstoken";

	public static final String WX_JS_TICKET_PREFIX = "wx.jsapi.ticket";

	@Autowired
	private Configuration configuration;

	@Autowired
	private RedisClient redisClient;


	// 获取token的锁
	private Object accecctokenSysnc = new Object();

	// 获取ticket的锁
	private Object ticketSync = new Object();
	/**
	 * 微信公众号授权信息
	 */
	public static class WeiXinPubAuth {
		public String unionId;
		public String openId;
	}


	public static final String WX_HZF = "HZF";

	static {
		// 目前仅支持会分期公众号
		WX_CHANNEL_CONF.add(WX_HZF);
	}

	/**
	 * 获取code，该接口返回的url必须重定向
	 *
	 * @param channel
	 * @param targetUrl
	 * @return
	 * @throws Exception
	 */
	public String createGetCodeUrl(String channel, String targetUrl) throws Exception {
		if (!WX_CHANNEL_CONF.contains(channel)) {
			throw new Exception("不存在的公众号：" + channel + "，可选的值为：" + WX_CHANNEL_CONF);
		}

		String encodedTargetUrl = URLEncoder.encode(targetUrl, "UTF-8");

		return String.format(AUTH_GET_CODE, configuration.hzfAppid, encodedTargetUrl, channel);
	}

	/**
	 * 获取微信对应渠道的unionid和openid
	 *
	 * @param code
	 * @param channel
	 * @return
	 */
	public WeiXinPubAuth requestAuth(String code, String channel) throws Exception {

		if (!WX_CHANNEL_CONF.contains(channel)) {
			throw new Exception("不存在的公众号：" + channel + "，可选的值为：" + WX_CHANNEL_CONF);
		}

		Map<String, String> params = new HashMap<>();
		params.put("appid", configuration.hzfAppid);
		params.put("secret", configuration.hzfSecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");

		String response = HttpUtil.get(AUTH_GET_ACCESSTOKEN, params);
		Map<String, Object> retMap = GsonUtil.buildGson().fromJson(response, new TypeToken<Map<String, Object>>() {
		}.getType());

		String openid = (String) retMap.get("openid");
		if (StringUtil.isEmpty(openid)) {
			throw new Exception("failed to get openid");
		}

		WeiXinPubAuth pubAuth = new WeiXinPubAuth();
		pubAuth.unionId = (String) retMap.get("unionid");
		pubAuth.openId = (String) retMap.get("openid");

		return pubAuth;
	}

	/**
	 * 获取js的授权信息
	 * 
	 * @return
	 */
	public Map<String, String> reqJsSDKAuth(String url) throws Exception {
		String token = reqWxAccessToken();
		if (StringUtil.isBlank(token)) {
			throw new Exception("微信接口授权失败");
		}
		String ticket = reqWxApiTicket(token);
		if (StringUtil.isBlank(ticket)) {
			throw new Exception("微信接口授权失败");
		}
		String signatureSeed = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
		String noncestr = generateNoncestr();
		//String timestamp = String.valueOf(System.currentTimeMillis());
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		//add by arison
		url=java.net.URLDecoder.decode(url,"UTF-8");
		String tempS = String.format(signatureSeed, ticket, noncestr, timestamp, url);
		logger.debug("JS API 签名URL" + tempS);
		String signature = DigestUtils.sha1Hex(tempS);
		Map<String, String> result = new HashMap<>();
		result.put("timestamp", timestamp);
		result.put("nonceStr", noncestr);
		result.put("signature", signature);
		result.put("appId", configuration.hzfAppid);
		logger.debug("JS API 签名信息" + result.toString());
		return result;
	}

	/**
	 * 获取微信接口访问授权access_token
	 * 
	 * @return
	 */
	public String reqWxAccessToken() throws Exception {
		// 先从内存数据库里面查询
		String token = (String) redisClient.get(WX_API_ACCESSTOKEN_PREFIX);
		// 如果未获取，则需要从微信平台获取
		if (StringUtil.isRedisBlank(token)) {
			// 先获取锁
			synchronized (accecctokenSysnc) {
				// 检查是否已经获得新的token，如果有则直接返回
				String curToken = (String) redisClient.get(WX_API_ACCESSTOKEN_PREFIX);
				if (!StringUtil.isRedisBlank(curToken)) {
					return curToken;
				}
				String url = String.format(AUTH_GET_API_ACCESSTOKEN, configuration.hzfAppid, configuration.hzfSecret);
				try {
					String response = HttpUtil.get(url);
					Map<String, Object> retMap = GsonUtil.buildGson().fromJson(response,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					token = (String) retMap.get("access_token");
					if (StringUtil.isBlank(token)) {
						throw new Exception("获取Access Token失败!");
					}
					// 缓存到内存数据库,1分钟
					// int expiresIn =
					// ((Double)retMap.get("expires_in")).intValue();
					// redisClient.set(WX_API_ACCESSTOKEN_PREFIX, token,
					// expiresIn / 4 * 1000);
					//redisClient.set(WX_API_ACCESSTOKEN_PREFIX, token, 60 * 1000);
					redisClient.set(WX_API_ACCESSTOKEN_PREFIX, token, 60 * 60 * 1000);
				} catch (Exception e) {
					logger.error("获取Access Token失败:" + e.toString());
					throw new Exception("获取Access Token失败!");
				}
				try {
					reqWxApiTicket(token);
				} catch (Exception e) {
					logger.error("刷新ticket失败:" + e.toString());
				}
			}
		}
		return token;
	}

	/**
	 * 请求微信JS API的ticket
	 * 
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public String reqWxApiTicket(String accessToken) throws Exception {
		// 先从内存数据库里面查询
		String ticket = (String) redisClient.get(WX_JS_TICKET_PREFIX);
		// 如果未获取，则需要从微信平台获取
		if (StringUtil.isRedisBlank(ticket)) {
			// 先获取锁
			synchronized (ticketSync) {
				// 查看内存是否有最新的ticket，如果有则直接返回
				String curTicket = (String) redisClient.get(WX_JS_TICKET_PREFIX);
				if (!StringUtil.isRedisBlank(curTicket)) {
					return curTicket;
				}
				String url = String.format(AUTH_GET_JSAPI_TICKET, accessToken);
				try {
					String response = HttpUtil.get(url);

					WxTicket wxTicket = GsonUtil.buildGson().fromJson(response, WxTicket.class);
					if (0 != wxTicket.errcode) {
						throw new Exception("获取ticket失败!" + wxTicket.errmsg);
					}
					ticket = wxTicket.ticket;
					if (StringUtil.isBlank(ticket)) {
						throw new Exception("获取ticket失败!");
					}
					// 缓存到内存数据库,1分钟
					// redisClient.set(WX_JS_TICKET_PREFIX, ticket,
					// wxTicket.expiresIn / 4 * 1000);
					redisClient.set(WX_JS_TICKET_PREFIX, ticket, 60 * 1000);
				} catch (Exception e) {
					logger.error("获取ticket失败:" + e.toString());
					throw new Exception("获取ticket失败!");
				}
			}
		}
		return ticket;
	}

	/**
	 * 清空微信的访问授权和微信JS API的ticket
	 */
	public void delWxTokenAndTicket() {
		logger.info("Delete The access token and ticket from cache.");
		redisClient.delete(WX_API_ACCESSTOKEN_PREFIX);
		redisClient.delete(WX_JS_TICKET_PREFIX);
	}

	/**
	 * 随机获取一个16位的字符串，有数字和字母（大小写）组成
	 * 
	 * @return
	 */
	public String generateNoncestr() {
		Random random = new Random(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			int seed1 = random.nextInt(3);
			switch (seed1) {
			// 大写字符
			case 0:
				sb.append((char) (65 + random.nextInt(26)));
				break;
			// 小写字母
			case 1:
				sb.append((char) (97 + random.nextInt(26)));
				break;
			// 数字
			default:
				sb.append(random.nextInt(10));
				break;
			}
		}
		return sb.toString();
	}

	public String getWxDownloadApi(String mediaId) throws Exception {
		String token = reqWxAccessToken();
		return String.format(WX_FILE_DOWNLOAD, token, mediaId);
	}

	/**
	 * 验证签名
	 * 
	 * @author zangxufeng
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public boolean checkSignature(String signature, String timestamp, String nonce, String token) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String tmpStr = DigestUtils.sha1Hex(content.toString());
		return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;
	}

	private Integer getIntValue(String str) {

		Integer intValue = null;
		try {
			intValue = Integer.valueOf(str);
		} catch (NumberFormatException e) {
		}
		return intValue;
	}

	class WxTicket {
		public int errcode;
		public String errmsg;
		public String ticket;
		public int expiresIn;
	}
}
