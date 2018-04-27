/** 
 * Project Name: hzf_platform_project 
 * File Name: FileConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月3日下午5:09:14 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: FileConfiguration
 * date: 2016年5月3日 下午5:09:14
 * Description: 文件相关的配置类
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class FileConfiguration {
	
	
	@Value("${hfq.file.house.image.standard.width}")
	private int imageStandardWidth;
	
	@Value("${hfq.file.house.image.standard.height}")
	private int imageStandardheight;
	
	@Value("${hfq.file.house.base.save.dir.size}")
	private int saveDirSize;

	public int getImageStandardWidth() {
		return imageStandardWidth;
	}

	public void setImageStandardWidth(int imageStandardWidth) {
		this.imageStandardWidth = imageStandardWidth;
	}

	public int getImageStandardheight() {
		return imageStandardheight;
	}

	public void setImageStandardheight(int imageStandardheight) {
		this.imageStandardheight = imageStandardheight;
	}

	public int getSaveDirSize() {
		return saveDirSize;
	}

	public void setSaveDirSize(int saveDirSize) {
		this.saveDirSize = saveDirSize;
	}
}
