import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

public class TestSign {
	public static void main(String args[]){	
		Map<String, String> params = new HashMap<String, String>();
		// 移除掉不需要排序的值
		String appId ="找房提供";
		String secretKey ="找房提供";
		params.put("outHouseId", "123456");
		params.put("rentType", "1");
		List<String> sParamValues = sortParamValues(params);
		getSign(appId,secretKey,sParamValues);
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
	public static String getSign(String appId, String secretKey,List<String> values) {

		// 拼接加密的字符串
		StringBuilder signSeed = new StringBuilder();
		signSeed.append(appId);
		for (String value : values) {
			signSeed.append(value);
		}
		signSeed.append(secretKey);

		// 使用sha256进行加密
		String seed = signSeed.toString();

		String cSign = DigestUtils.sha256Hex(seed);
		return cSign;
	}
}
