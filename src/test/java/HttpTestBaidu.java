import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.amazonaws.util.json.JSONObject;
import com.huifenqi.hzf_platform.utils.HttpUtil;

public class HttpTestBaidu {
	public static void main(String args[]){
		 //String url = "http://api.hzfapi.com/house/feedHouse";
		 String url = "http://api.hzfapi.com/task/houseSubmit";
		 String appId = "110001";
		 String secretKey = "ba0b3d86458a69b67df51a18d5e3c9bb";
		 String ts = "20170418";
		 try {
		 //数据请求参数
		 //Map<String, String> params = new  HashMap<String, String>();
		 JSONObject params = new JSONObject();
		 Map<String, String> requetParams = new  HashMap<String, String>(); 
		 params.put("positionX", "89.897797");
		 params.put("positionY", "11.897797");
		 params.put("outHouseId", "11.897797");
		 	
		 //参数按字典排序
		 //List<String> sortParams = sortParamValues(params);
		 		 
		 //公共参数
		 params.put("appId", appId);
		 params.put("ts", ts);
		 //获取签名
		 //String sign = getSign(appId, secretKey,sortParams, ts);
		 //params.put("sign", sign);
		 
		 System.out.println("返回结果："+params.toString());
		 requetParams.put("data", params.toString());
		
			String result = HttpUtil.post(url, requetParams);
			System.out.println("---------------------------------------------------------");
			System.out.println("返回结果："+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按照参数名称的字典排序
	 * 
	 * @param params
	 * @return
	 */
	public static List<String> sortParamValues(Map<String, String> params) {		
		List<String> sortResult = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			return sortResult;
		}
		List<String> pKeys = new ArrayList<>();
		pKeys.addAll(params.keySet());
		Collections.sort(pKeys, new Comparator<String>() {

			@Override
			public int compare(String key1, String key2) {
				return key1.compareTo(key2);
			}
		});

		for (String pKey : pKeys) {
			sortResult.add(params.get(pKey));
		}
		return sortResult;
	}
	
	
	/**
	 * 进行签名校验
	 * 
	 * @param appId
	 * @param secretKey
	 * @param values
	 * @param sign
	 * @return
	 */
	public static String getSign(String appId, String secretKey,List<String> values, String ts) {

		// 拼接加密的字符串
		StringBuilder signSeed = new StringBuilder();
		signSeed.append(appId);
		for (String value : values) {
			signSeed.append(value);
		}
		signSeed.append(secretKey);
		//signSeed.append(ts);

		// 使用sha256进行加密
		String seed = signSeed.toString();
		String cSign = DigestUtils.sha256Hex(seed);

		return cSign;
	}
}
