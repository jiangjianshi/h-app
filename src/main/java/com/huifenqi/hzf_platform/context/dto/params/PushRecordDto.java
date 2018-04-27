package com.huifenqi.hzf_platform.context.dto.params;

public class PushRecordDto {
	
	private String msgTitle;
	private String msgContent;
	private String createTime;
	private String createTimeDesc;
	
	
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	public String getCreateTimeDesc() {
		return createTimeDesc;
	}
	public void setCreateTimeDesc(String createTimeDesc) {
		this.createTimeDesc = createTimeDesc;
	}
	
	@Override
	public String toString() {
		return "PushRecordDto [msgTitle=" + msgTitle + ", msgContent=" + msgContent + ", createTime=" + createTime
				+ ", createTimeDesc=" + createTimeDesc + "]";
	}
		
}
