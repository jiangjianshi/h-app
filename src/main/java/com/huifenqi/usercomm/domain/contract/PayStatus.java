/** 
 * Project Name: usercomm_project 
 * File Name: PayStatus.java 
 * Package Name: com.huifenqi.usercomm.domain 
 * Date: 2015年12月9日下午2:45:23 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.usercomm.domain.contract;

import org.springframework.data.repository.CrudRepository;

import javax.persistence.*;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/** 
 * ClassName: PayStatus
 * date: 2015年12月9日 下午2:45:23
 * Description: 订单状态
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Entity
@Table(name = "user_apply_status")
public class PayStatus implements Serializable {

	private static final long serialVersionUID = -8791147956835297472L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	//用户ID
	@Column
	private long uid;
	
	//合同号
	@Column
	private String contractNo;
	
	//合同审核状态
	@Column
	private int contractStatus;
	
	//消息标题,40中/英字符（长度）
	@Column
	private String msgTitle;
	
	//消息内容：600中/英字符
	@Column
	private String msgText;
	
	// 消息时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date msgTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public int getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(int contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public Date getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}

	@Override
	public String toString() {
		return "PayStatus [id=" + id + ", uid=" + uid + ", contractNo=" + contractNo + ", contractStatus="
				+ contractStatus + ", msgTitle=" + msgTitle + ", msgText=" + msgText + ", msgTime=" + msgTime + "]";
	}
}
