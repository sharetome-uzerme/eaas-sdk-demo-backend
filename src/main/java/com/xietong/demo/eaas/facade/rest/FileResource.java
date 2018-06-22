package com.xietong.demo.eaas.facade.rest;

import com.xietong.demo.eaas.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class FileResource {
	private final static Logger logger = Logger.getLogger(FileResource.class);

	@Autowired
	FileService fileService;

	/**
	 * 用于EaaS更新完文件后，回调本接口。
	 * 对应于mongodb中client_configuration配置的FileUploadTransport回调地址
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param file
	 * @param filePath
	 * @param fileId
	 * @param fileName
	 * @param resourceId
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/file", method = RequestMethod.POST, headers = {"content-type=multipart/form-data;Charset=UTF-8" })
	@ResponseBody
	@ApiOperation(value = "文件上传推送（新文件 或者 更新已有文件）", notes = "返回值为 code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败") })
	public void uploadFile(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@ApiParam(name = "file", value = "上传文件流") @RequestPart(value = "file") MultipartFile file,
			@ApiParam(name = "fileId", value = "文件ID") @RequestParam(required = false, value = "fileId") String fileId,
		    @ApiParam(name = "fileName", value = "文件名称") @RequestParam(required = false, value = "fileName") String fileName,
		    @ApiParam(name = "filePath", value = "文件路径") @RequestParam(value = "filePath",required = false) String filePath,
			@ApiParam(name = "resourceId", value = "EaaS系统内部文件ID") @RequestParam(required = false, value = "resourceId") String resourceId)
			throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("uploadFile filePath:" + filePath + ";fileId:" + fileId + ";fileName:" + fileName + ";resourceId:" + resourceId);
		}
		// third part self code start
		// TODO
		// third part self code end
		if (StringUtils.isEmpty(fileId) && StringUtils.isEmpty(resourceId)) {
			httpResponse.setStatus(400);
			return;
		}

		if (file != null && file.getSize() > 0) {
			boolean result = fileService.saveFile(filePath, file);
			if (logger.isInfoEnabled()) {
				logger.info("upload file result:" + result);
			}
			if (result) {
				buildResult(httpResponse, file);
				return;
			}
		}
		httpResponse.setStatus(200);
	}

	/**
	 * 用于EaaS获取文件。
	 * 对应于mongodb中client_configuration配置的FileDownloadTransport回调地址
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param fileId
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/file", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "文件内容获取", notes = "返回值为 code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败") })
	public void downloadFile(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
				@ApiParam(name = "fileId", value = "EaaS系统内部文件ID") @RequestParam(value = "fileId") String fileId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("prepareDownloadResponse fileId:" + fileId );
		}
		// third part self code start
		// TODO
		// third part self code end
		if (fileId != null) {
			File targetFile = fileService.downloadFile(fileId);
			prepareDownloadResponse(httpResponse, targetFile);
			httpResponse.setStatus(200);
		} else {
			httpResponse.setStatus(400);
		}
		return;
	}

	/**
	 * 当文件被更新时，EAAS会调用本接口.
	 * 对应于mongodb中client_configuration配置的FileUploadFinishedNotification回调地址
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param fileId
	 * @param resourceId
	 * @param status
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/file/status", method = {RequestMethod.POST,RequestMethod.PUT})
	@ResponseBody
	@ApiOperation(value = "文件更新完成通知", notes = "返回值为 code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败") })
	public void notifyFileStatusChange(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@ApiParam(name = "fileId", value = "文件唯一编号") @RequestParam(required = false, value = "fileId") String fileId,
			@ApiParam(name = "resourceId", value = "EaaS文件唯一编号") @RequestParam(required = false, value = "resourceId") String resourceId,
			@ApiParam(name = "status", value = "更新状态") @RequestParam(value = "status") String status) throws Exception {

		notifyFileStatusChange(httpResponse, fileId, resourceId, status);
	}

	private void prepareDownloadResponse(HttpServletResponse httpResponse, File targetFile) {
		try {
			InputStream inputStream = new FileInputStream(targetFile);
			OutputStream outputStream = httpResponse.getOutputStream();

			httpResponse.addHeader("Content-Disposition", "attachment; filename=" + targetFile.getName());
			httpResponse.setContentType("application/octet-stream; charset=utf-8");
			httpResponse.addHeader("Content-Length", "" + inputStream.available());
			httpResponse.addHeader("name", targetFile.getName());
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyFileStatusChange(HttpServletResponse httpResponse, String fileId, String resourceId, String status) {
		if (logger.isInfoEnabled()) {
			logger.info("noticeFileStatus fileId:" + fileId );
		}
		boolean result = fileService.notifyFileStatus(fileId, resourceId, status);
		setHttpResponseCode(httpResponse, result);
	}

	private void setHttpResponseCode(HttpServletResponse httpResponse, boolean result) {
		if (result) {
			httpResponse.setStatus(200);
		} else {
			httpResponse.setStatus(400);
		}
	}

	private void buildResult(HttpServletResponse httpResponse, MultipartFile file)
			throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(file.getBytes());
		BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
		httpResponse.setHeader("MD5", bigInteger.toString(16));
		httpResponse.setHeader("fileSize", String.valueOf(file.getSize()));

		httpResponse.setStatus(200);
		return;
	}
}
