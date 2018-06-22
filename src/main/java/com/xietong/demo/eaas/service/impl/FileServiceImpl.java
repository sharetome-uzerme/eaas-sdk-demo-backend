package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.service.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by haibin on 2016/9/5.
 */
@Service
public class FileServiceImpl implements FileService {
	private final static Logger logger = Logger.getLogger(FileServiceImpl.class);

	@Value("${eaas.demo.config.file-path}")
	private String demoFilePath;

	@Value("${eaas.demo.config.root-path}")
	private String rootFilePath;

	@Override
	public boolean saveFile(String filePath, MultipartFile fileInfo) {
		try {
			return createTempFile(fileInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean modifyFile(String fileId, MultipartFile fileInfo) {
		try {
			return createTempFile(fileInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean notifyFileStatus(String fileId, String resourceId, String status) {
		if(StringUtils.isEmpty(fileId)){
			return false;
		}

		if(StringUtils.isEmpty(resourceId)){
			return false;
		}

		String fileName = fileId + ".docx";
		if (logger.isInfoEnabled()) {
			logger.info("get fileStatus");
			logger.info("fileId:" + fileId + "fileName:" + fileName + ";xtResourceId:" + resourceId + ";status:" + status);
		}

		return "200".equals(status);
	}

	@Override
	public File downloadFile(String fileId) {
		File file = new File(rootFilePath + fileId);
		if (file.exists()) {
			if (logger.isInfoEnabled()) {
				logger.info("file existed:" + fileId);
			}
			return file;
		}
		if (logger.isInfoEnabled()) {
			logger.info("open default demo file");
		}
		Resource resource = new ClassPathResource(demoFilePath);
		try {
			file = resource.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private boolean createTempFile(MultipartFile fileInfo) throws Exception {

		InputStream inputStream = fileInfo.getInputStream();
		File targetTempFile = File.createTempFile("temp-upload-file", ".uploaded");
		if (logger.isInfoEnabled()) {
			logger.info("absolutePath:" + targetTempFile.getAbsolutePath());
		}
		System.out.println(targetTempFile.getAbsolutePath());
		Files.copy(inputStream,
				Paths.get(targetTempFile.getAbsolutePath()),
				StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
}
