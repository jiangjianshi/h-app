package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OssConfiguration {
	
	@Value("${oss.accessKeyId}")
	private String accessKeyId;
	
	@Value("${oss.accessKeySecret}")
	private String accessKeySecret;
	
	@Value("${oss.upload-endpoint}")
	private String uploadEndpoint;
	
	@Value("${oss.bucketName}")
	private String bucketName;
	
	@Value("${oss.download-endpoint}")
	private String downloadEndpoint;
	

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getUploadEndpoint() {
		return uploadEndpoint;
	}

	public void setUploadEndpoint(String uploadEndpoint) {
		this.uploadEndpoint = uploadEndpoint;
	}


	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getDownloadEndpoint() {
		return downloadEndpoint;
	}

	public void setDownloadEndpoint(String downloadEndpoint) {
		this.downloadEndpoint = downloadEndpoint;
	}
	
	
}
