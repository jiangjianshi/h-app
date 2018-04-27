package com.house.asz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.lang3.ArrayUtils;

public class RSAUtil {
	public static String ALGORITHM = "RSA";
	public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算饭
	public static String CHAR_SET = "UTF-8";
	
	public static void main(String[] args) {
		try {
			// 公钥
			String pb_str = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+qom66TXq4vKGeWqRrSdl76+vGjB92OmzRotvlPjBV2N0Omc6GL0fvQsr5aVZQXMeL7ErMXCM9QVeco2piqYXxKp8kazr1XSO1Pic7SNp8lThf5yt1mOX3GFOdTYDb5s0KrGzSz4a4cRPVeGftnJiOYt26a0/6tnD0FW4bintBQIDAQAB";
			// 私钥
			String pr_str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL6qibrpNeri8oZ5apGtJ2Xvr68aMH3Y6bNGi2+U+MFXY3Q6ZzoYvR+9CyvlpVlBcx4vsSsxcIz1BV5yjamKphfEqnyRrOvVdI7U+JztI2nyVOF/nK3WY5fcYU51NgNvmzQqsbNLPhrhxE9V4Z+2cmI5i3bprT/q2cPQVbhuKe0FAgMBAAECgYABGAliQSRGDLdHfjrWSyAGvbFMV+IfVrdAiA8UvM4QjefMKumcs7eiDvuZbN/d+zol2jAyBz6WEHHPcOjPKDR7u+Edf8pNgB7N11I2W2YKa13ZHGFDgEryk42ipcyZIKMHt8Jlz1PwqORKGc1Y5KkLiOJ8cVetf0Geg6Ojc7rAoQJBAPNLKxUQ1WENliHCRrq9LdnckV+A/eA+oI8kmfWWJ3ziKzRJUkVvE/RlS1qq3HCVF/kdOK/a55y6+rVDi3tKzLcCQQDIn73OcludC8OCJ5O9Ba2FOVmAiLRZPj8hhL0NwUVR70L6agiy6kT/yeoL7h6Adar6+DuegW7RcgFwwReXf5AjAkAT8jy0/G1SCKgfWmssEih5LREqEExAH0JQmgKZVNcl8PDz13MMSEANkGRuKYXrIP4XKWMlX8APZHD7fW8pC4ffAkEAgMhx2dT01BHaFXF8V6kOYueWeXjHdEYN1mFTzkGTUu4oa4CnRto1IpElaTUYZVOjRukTtELXtSDepdd9YmWjSwJAbfhK9taqhPOtVvswb6Ruh5tGUjBaCv8S08rmmaRc9lwJekCd5KftVLSxrN62T0g0OODbc/T8Qm7PtOVaFB0kPg==";
			
			String content = "{\"client_id\":\"1234567890\",\"version\":\"1.0.0\",\"page_no\":1,\"page_size\":200,\"start_time\":\"2016-08-24 12:01:01\"}";

			String estr = RSAUtil.encrypt(content, pr_str);
			System.out.println("私钥加密：" + estr);
			System.out.println("公钥解密：" + RSAUtil.decrypt(estr, pb_str));
		} catch (Exception e) {
		}
	}

	/**
	 * 解密数据，接收端接收到数据直接解密
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String publicKey) {
		// System.out.println(log + "：decrypt方法中key=" + publicKey);
		if (null == publicKey || "".equals(publicKey)) {
			// System.out.println(log + "：decrypt方法中key=" + publicKey);
			return null;
		}
		PublicKey pk = getPublicKey(publicKey);
		byte[] data = decryptByPublicKey(content, pk);
		String res = null;
		try {
			res = new String(data, CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 对内容进行加密
	 * 
	 * @param content
	 * @param key私钥
	 * @return
	 */
	public static String encrypt(String content, String key) {
		PrivateKey pk = getPrivateKey(key);
		byte[] data = encryptByPrivateKey(content, pk);
		String res = null;
		try {
			res = Base64Util.encode(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 得到私钥对象
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码的秘钥字节）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKey) {
		try {
			byte[] keyBytes;
			keyBytes = Base64Util.decode(privateKey);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey privatekey = keyFactory.generatePrivate(keySpec);
			return privatekey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取公钥对象
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码秘钥字节）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKey) {
		try {
			byte[] keyBytes;
			keyBytes = Base64Util.decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PublicKey publickey = keyFactory.generatePublic(keySpec);
			return publickey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过公钥解密
	 * 
	 * @param content待解密数据
	 * @param pk公钥
	 * @return 返回 解密后的数据
	 */
	protected static byte[] decryptByPublicKey(String content, PublicKey pk) {
		InputStream ins = null;
		ByteArrayOutputStream writer = null;
		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.DECRYPT_MODE, pk);
			ins = new ByteArrayInputStream(Base64Util.decode(content));
			writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(ch.doFinal(block));
			}
			return writer.toByteArray();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ins.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 通过私钥加密
	 * 
	 * @param content
	 * @param pk
	 * @return,加密数据，未进行base64进行加密
	 */
	protected static byte[] encryptByPrivateKey(String content, PrivateKey pk) {
		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.ENCRYPT_MODE, pk);
			byte[] contentBytes = content.getBytes(CHAR_SET);
			// 加密时超过117字节就报错。为此采用分段加密的办法来加密
			byte[] enBytes = null;
			for (int i = 0; i < contentBytes.length; i += 64) {
				// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
				byte[] doFinal = ch.doFinal(ArrayUtils.subarray(contentBytes, i, i + 64));
				enBytes = ArrayUtils.addAll(enBytes, doFinal);
			}
			return enBytes;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("通过私钥加密出错");
		}
		return null;
	}
}
