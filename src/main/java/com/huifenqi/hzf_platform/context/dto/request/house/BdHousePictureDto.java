/** 
* Project Name: hzf_platform 
* File Name: BdHousePictureDto.java
* Package Name: com.huifenqi.hzf_platform.context.dto.request.house
* Date:  2017年4月14日 下午18:06:22
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.request.house;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: BdHousePictureDto date: 2017年4月14日 下午18:06:22 Description: 房源图片Dto
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */

public class BdHousePictureDto {
	private String detailNum;
	private String picDesc;
	private String picUrl;
	public BdHousePictureDto() {
	}

	public String getDetailNum() {
		return detailNum;
	}

	public void setDetailNum(String detailNum) {
		this.detailNum = detailNum;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public BdHousePictureDto(String detailNum, String picDesc, String picUrl) {
		this.detailNum = detailNum;
		this.picDesc = picDesc;
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		return "BdHousePictureDto{" +
				"detailNum='" + detailNum + '\'' +
				", picDesc='" + picDesc + '\'' +
				", picUrl='" + picUrl + '\'' +
				'}';
	}
}
