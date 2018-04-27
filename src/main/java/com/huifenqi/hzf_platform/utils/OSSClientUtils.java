package com.huifenqi.hzf_platform.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.huifenqi.hzf_platform.configuration.OssConfiguration;

import ch.qos.logback.core.util.CloseUtil;

/**
 * 阿里云对象存储工具类
 * 
 * @author jjs
 *
 */
@Component
public class OSSClientUtils {

	private static Logger log = LoggerFactory.getLogger(OSSClientUtils.class);

	@Resource
	private OssConfiguration ossConfig;

	/**
	 * 上传文件到阿里云OSS
	 *
	 * @param file要上传的文件
	 * @param storagePath文件存储路径，这个路径会被当做阿里云OSS存储数据的key使用
	 * @return OSS的entity tag
	 * @throws Exception
	 */
	public String upload(MultipartFile file, String storagePath) throws Exception {
		checkNotNull(file);
		checkArgument(StringUtils.isNotEmpty(storagePath));
		return upload(file.getInputStream(), file.getSize(), storagePath);
	}

	public String upload(File file, String storePath) throws Exception {
		checkNotNull(file);
		checkArgument(StringUtils.isNotEmpty(storePath));
		return upload(new FileInputStream(file), file.length(), storePath);
	}

	public String upload(InputStream inputStream, long fileSize, String storePath) throws Exception {
		checkNotNull(inputStream);
		checkArgument(fileSize >= 0);
		checkArgument(StringUtils.isNotEmpty(storePath));

		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(fileSize);
		String fixedPath = fixStorePath(storePath);
		PutObjectResult putResult = ossClientPutObject(ossConfig.getBucketName(), fixedPath, inputStream, meta);
		return putResult.getETag();
	}

	/**
	 * 删除OSS上存储的文件
	 * 
	 * @param storagePaths存储路径列表，路径会被当做阿里云OSS存储数据的key使用
	 * @return 成功删除的文件的key列表
	 * @throws Exception
	 */
	public List<String> delete(List<String> storagePaths) throws Exception {
		checkArgument(CollectionUtils.isNotEmpty(storagePaths));

		List<String> fixedPaths = fixStoragePaths(storagePaths);
		DeleteObjectsResult deleteResult = ossClientDeleteObjects(ossConfig.getBucketName(), fixedPaths);
		return deleteResult.getDeletedObjects();
	}

	public void download(String storagePath, String targetDir, String targetFilename) throws Exception {
		checkArgument(StringUtils.isNotEmpty(storagePath));
		checkArgument(StringUtils.isNotEmpty(targetDir));
		checkArgument(StringUtils.isNotEmpty(targetFilename));
		// String ext = FilenameUtils.getExtension(storagePath);
		String targetPath = targetDir + File.separatorChar + targetFilename;
		download(fixStorePath(storagePath), targetPath);
	}

	/**
	 * 下载文件
	 * 
	 * @param storagePath
	 * @param targetPath
	 * @throws Exception
	 */
	public void download(String storagePath, String targetPath) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream outputStream = null;

		try {
			OSSObject ossObject = ossClientGetObject(ossConfig.getBucketName(), storagePath);
			bis = new BufferedInputStream(ossObject.getObjectContent());
			outputStream = new FileOutputStream(FileUtils.createFile(new File(targetPath)));
			bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			log.error("下载文件失败：{}", e);
		} finally {
			CloseUtil.closeQuietly(bis);
			CloseUtil.closeQuietly(outputStream);
			CloseUtil.closeQuietly(bos);
		}
	}

	private PutObjectResult ossClientPutObject(String bucketName, String key, InputStream input,
			ObjectMetadata metadata) throws Exception {
		checkArgument(StringUtils.isNotEmpty(bucketName));
		checkArgument(StringUtils.isNotEmpty(key));
		checkNotNull(input);
		checkNotNull(metadata);

		try {
			long start = System.currentTimeMillis();
			OSSClient client = new OSSClient(ossConfig.getUploadEndpoint(), ossConfig.getAccessKeyId(),
					ossConfig.getAccessKeySecret());
			PutObjectResult result = client.putObject(bucketName, key, input, metadata);
			log.info("上传{}耗时：{}ms", key, System.currentTimeMillis() - start);
			return result;
		} catch (OSSException | ClientException e) {
			log.error("upload", "OSS error", e);
			throw new Exception("上传失败", e);
		}
	}

	private DeleteObjectsResult ossClientDeleteObjects(String bucketName, List<String> keys) throws Exception {
		checkArgument(StringUtils.isNotEmpty(bucketName));
		checkArgument(CollectionUtils.isNotEmpty(keys));

		try {
			long start = System.currentTimeMillis();
			OSSClient client = new OSSClient(ossConfig.getUploadEndpoint(), ossConfig.getAccessKeyId(),
					ossConfig.getAccessKeySecret());
			DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
			request.setKeys(keys);
			DeleteObjectsResult result = client.deleteObjects(request);
			log.info("删除{}耗时：{}ms", keys, System.currentTimeMillis() - start);
			return result;
		} catch (OSSException | ClientException e) {
			log.error("delete", "OSS error", e);
			throw new Exception("删除失败", e);
		}
	}

	private OSSObject ossClientGetObject(String bucketName, String key) throws Exception {
		checkArgument(StringUtils.isNotEmpty(bucketName));
		checkArgument(StringUtils.isNotEmpty(key));

		try {
			long start = System.currentTimeMillis();
			OSSClient client = new OSSClient(ossConfig.getUploadEndpoint(), ossConfig.getAccessKeyId(),
					ossConfig.getAccessKeySecret());
			OSSObject result = client.getObject(bucketName, key);
			log.info("查询{}耗时：{}ms", key, System.currentTimeMillis() - start);
			return result;
		} catch (OSSException | ClientException e) {
			log.error("get", "OSS error", e);
			throw new Exception("查询失败", e);
		}
	}

	private String fixStorePath(String storagePath) {
		if (StringUtils.isEmpty(storagePath)) {
			return "";
		}

		String result = storagePath;
		while (result.startsWith("/")) {// 阿里云的存储路径不以/开头，正确的路径类似于:"dir1/dir2/dir3/file.png"
			result = result.substring(1);
		}
		return result;
	}

	private List<String> fixStoragePaths(List<String> paths) {
		if (CollectionUtils.isEmpty(paths)) {
			return Lists.newArrayList();
		}

		List<String> result = Lists.newArrayList();
		for (String path : paths) {
			result.add(fixStorePath(path));
		}
		return result;
	}

}
