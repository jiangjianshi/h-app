package com.huifenqi.hzf_platform.context.dto.params;

import java.util.List;

import com.google.gson.JsonObject;

public class PushMessageDto {

	private Integer totalPages;
	private Integer totalElements;
	private Integer pageNum;
	private Integer pageSize;
	private List<PushRecordDto> records;

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public List<PushRecordDto> getRecords() {
		return records;
	}

	public void setRecords(List<PushRecordDto> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "PushMessageDto [totalPages=" + totalPages + ", totalElements=" + totalElements + ", pageNum=" + pageNum
				+ ", pageSize=" + pageSize + ", records=" + records + "]";
	}

}
