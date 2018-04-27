/** 
 * Project Name: hzf-platform
 * File Name: SignAndEncryptUtil.java 
 * Package Name: com.huifenqi.saas.activity.utils 
 * Date: 2017年11月7日下午2:25:53 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.service;

import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.jedi.security.sign.RsaEncryptor;
import com.huifenqi.jedi.security.sign.exception.RsaEncryptorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/** 
 * ClassName: SignAndEncryptUtil
 * date: 2017年11月7日 下午2:25:53
 * Description: 
 * 
 * @author arison
 * @version  
 * @since JDK 1.8 
 */
@Component
public class PaymentManager {
	
	private static final Log logger = LogFactory.getLog(PaymentManager.class);


    @Autowired
    private Configuration paymentConfiguration;

	/**
    *
    * RsaEncryptor.PRIVATE_KEY_FLAG：表示使用私钥初始化RsaEncryptor
    * RsaEncryptor.PUBLIC_KEY_FLAG： 表示使用公钥初始化RsaEncryptor
    *
    */


   /**
    * 签名： 签名需要使用自己的private_key初始化RsaEncryptor
 * @throws RsaEncryptorException 
    */
   public String sign(Map<String, String> params) throws RsaEncryptorException {
	  
       String priKeyPath = paymentConfiguration.privKeyFile;// 自己私钥的存放路径

       RsaEncryptor rsaEncryptor = null;

       try {
           rsaEncryptor = new RsaEncryptor(priKeyPath, RsaEncryptor.PRIVATE_KEY_FLAG);
       } catch (RsaEncryptorException e) {
           System.out.println("初始化公钥私钥文件失败：" + e.getMessage());
       }

       String sign = rsaEncryptor.sign(params);

       logger.info("签名成功, sign:" + sign);

       return sign;
   }

   /**
    * 验签： 验签需要使用对方的public_key初始化RsaEncryptor
 * @throws RsaEncryptorException 
    */
   public boolean checkSign(Map<String, String> paramMap, String sign) throws RsaEncryptorException {
       String pubKeyPath = paymentConfiguration.pubKeyFile;// 对方公钥的存放路径

       RsaEncryptor rsaEncryptor = null;

       try {
           rsaEncryptor = new RsaEncryptor(pubKeyPath, RsaEncryptor.PUBLIC_KEY_FLAG);
       } catch (RsaEncryptorException e) {
           System.out.println("初始化公钥私钥文件失败：" + e.getMessage());
       }

       boolean result = rsaEncryptor.checkSign(paramMap, sign);
       System.out.println("验签结果" + result);

       return result;
   }


   /**
    * 加密： 加密需要使用对方的public_key初始化RsaEncryptor
 * @throws RsaEncryptorException 
    */
   public String encrypt(String originalData) throws RsaEncryptorException {
       String pubKeyPath = paymentConfiguration.pubKeyFile;// 对方公钥的存放路径

       RsaEncryptor rsaEncryptor = null;

       try {
           rsaEncryptor = new RsaEncryptor(pubKeyPath, RsaEncryptor.PUBLIC_KEY_FLAG);
       } catch (RsaEncryptorException e) {
           System.out.println("初始化公钥私钥文件失败：" + e.getMessage());
       }

       System.out.println("原始待加密数据:\n" + originalData);
       System.out.println("加密成功, 加密后:" + rsaEncryptor.encrypt(originalData));

       return rsaEncryptor.encrypt(originalData);
   }

   /**
    * 解密： 解密需要使用自己的private_key初始化RsaEncryptor
 * @throws RsaEncryptorException 
    */
   public String decrypt(String encryptData) throws RsaEncryptorException{
       String priKeyPath = paymentConfiguration.privKeyFile;// 自己私钥的存放路径

       RsaEncryptor rsaEncryptor = null;

       try {
           rsaEncryptor = new RsaEncryptor(priKeyPath, RsaEncryptor.PRIVATE_KEY_FLAG);
       } catch (RsaEncryptorException e) {
    	   logger.info("初始化公钥私钥文件失败：" + e.getMessage());
       }

       String result = rsaEncryptor.decrypt(encryptData);
       logger.info("解密成功, 解密后:" + result);

       return result;

   }

}
