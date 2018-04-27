/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月4日下午2:55:16 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: AlibabaSysHouseConfiguration
 * date: 2016年5月4日 下午2:55:16
 * Description: 
 * 
 * @author changmingwei 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class AlibabaSysHouseConfiguration {
    
    @Value("${hfq.alibaba.idle.url}")
    private String url;
	
	@Value("${hfq.alibaba.idle.house.user.add}")
	private String userAdd;
	
	@Value("${hfq.alibaba.idle.house.user.del}")
	private String userDel;
	
	@Value("${hfq.alibaba.idle.house.item.add}")
	private String itemAdd;

	@Value("${hfq.alibaba.idle.house.media.add}")
	private String mediaAdd;
	
	@Value("${hfq.alibaba.idle.house.item.update}")
	private String itemUpdate;
	
	@Value("${hfq.alibaba.idle.owner.user}")
    private String ownerUser;

	@Value("${hfq.alibaba.appkey}")
    private String aliAppkey;

	@Value("${hfq.alibaba.appsecret}")
    private String aliAppSecret;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(String userAdd) {
        this.userAdd = userAdd;
    }

    public String getUserDel() {
        return userDel;
    }

    public void setUserDel(String userDel) {
        this.userDel = userDel;
    }

    public String getItemAdd() {
        return itemAdd;
    }

    public void setItemAdd(String itemAdd) {
        this.itemAdd = itemAdd;
    }

    public String getMediaAdd() {
        return mediaAdd;
    }

    public void setMediaAdd(String mediaAdd) {
        this.mediaAdd = mediaAdd;
    }

    public String getItemUpdate() {
        return itemUpdate;
    }

    public void setItemUpdate(String itemUpdate) {
        this.itemUpdate = itemUpdate;
    }

    public String getAliAppkey() {
        return aliAppkey;
    }

    public void setAliAppkey(String aliAppkey) {
        this.aliAppkey = aliAppkey;
    }

    public String getAliAppSecret() {
        return aliAppSecret;
    }

    public void setAliAppSecret(String aliAppSecret) {
        this.aliAppSecret = aliAppSecret;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }
    
}
