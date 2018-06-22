package com.xietong.demo.eaas.facade.rest;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.facade.dto.*;
import com.xietong.demo.eaas.service.SessionService;
import com.xietong.demo.util.ResponseUtil;
import com.xietong.phoenix.common.util.ParamValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Administrator on 2016/6/18.
 * 
 * <li>Create Session <li>Join Session <li>Close session
 * 
 */
@RestController
@Api(value = "云应用基本操作")
public class SessionResource {
	private final static Logger logger = Logger.getLogger(SessionResource.class);

	@Autowired
	SessionService sessionService;

	/**
	 * 创建Eaas App session.
	 * 
	 * @param createSessionForOpenAppDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/app", method = POST, consumes = "application/json")
	@ApiOperation(value = "打开云应用", response = SessionCreateACK.class)
	public ResponseDTO createSessionForOpenApp(
			@ApiParam(value = "打开应用参数", required = true) @RequestBody CreateSessionForOpenAppDTO createSessionForOpenAppDTO)
			throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("createSessionForOpenApp:");
		}

		ParamValidateUtils.checkNotNull(createSessionForOpenAppDTO.getAppId(), "appId is required");
		ParamValidateUtils.checkNotNull(createSessionForOpenAppDTO.getUserId(), "UserId is required");
		// get appName?

		SessionCreateACK sessionCreateACK = sessionService.createSessionForOpenApp(createSessionForOpenAppDTO);

		return ResponseUtil.buildResponseDTO(sessionCreateACK);
	}

	/**
	 * 创建EaaS session 打开文件。
	 * 
	 * @param createSessionForOpenFileWithAppDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/file", method = POST, consumes = "application/json")
	@ApiOperation(value = "使用云应用打开某文档", response = SessionCreateACK.class)
	public ResponseDTO createSessionForOpenFileWithApp(
			@ApiParam(value = "打开应用参数", required = true) @RequestBody CreateSessionForOpenFileWithAppDTO createSessionForOpenFileWithAppDTO)
			throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("createSessionForOpenFileWithApp:");
		}

		ParamValidateUtils.checkNotNull(createSessionForOpenFileWithAppDTO.getFileId(), "fileId is required");
		ParamValidateUtils.checkNotNull( createSessionForOpenFileWithAppDTO.getAppId(), "appId is required");
		ParamValidateUtils.checkNotNull( createSessionForOpenFileWithAppDTO.getUserId(), "userId is required");

		SessionCreateACK sessionCreateACK = sessionService.createSessionForOpenFile(createSessionForOpenFileWithAppDTO);

		return ResponseUtil.buildResponseDTO(sessionCreateACK);
	}

	/**
	 * 强制关闭会话，释放EaaS资源
	 *
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/{sessionId}", method = DELETE)
	@ApiOperation(value = "关闭会话", response = ResponseDTO.class)
	public ResponseDTO closeSession(@ApiParam(value = "会话ID", required = true) @PathVariable Long sessionId) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("session user session close!!");
		}
		sessionService.closeSession(sessionId);
		return new ResponseDTO();
	}

	/**
	 * EaaS Session发生变化时，调用该结果。
	 * 对应于mongodb中client_configuration配置的ParticipantStatusChange回调地址。
	 *
	 * @param sessionUserStatusUpdateDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/status", method = { RequestMethod.POST, RequestMethod.PUT }, consumes = "application/json")
	@ApiOperation(value = "更新会话用户列表状态（加入、离开、关闭）", response = ResponseDTO.class)
	public ResponseDTO sessionStatusChange(
			@ApiParam(value = "状态更新数据", required = true) @RequestBody SessionUserStatusUpdateDTO sessionUserStatusUpdateDTO) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("sessionStatusChange:" + sessionUserStatusUpdateDTO);
		}

		inputCheck(sessionUserStatusUpdateDTO);

		changeUserStatus(sessionUserStatusUpdateDTO);

		return ResponseUtil.buildResponseDTO(true);
	}

	private void changeUserStatus(SessionUserStatusUpdateDTO sessionUserStatusUpdateDTO) {
		inputCheck(sessionUserStatusUpdateDTO);

		String eaasSessionId = sessionUserStatusUpdateDTO.getCorrelatedSessionId();
		if (eaasSessionId == null) {
			return;
		}
		Session session = sessionService.getSession(sessionUserStatusUpdateDTO.getCorrelatedSessionId());
		if (sessionUserStatusUpdateDTO.getCorrelatedSessionId() != null && session != null) {
			if (sessionUserStatusUpdateDTO.getStatus().equalsIgnoreCase("join")) {
				sessionService.joinSession(session.getSessionId(), sessionUserStatusUpdateDTO.getUserId());
			} else if (sessionUserStatusUpdateDTO.getStatus().equalsIgnoreCase("leave")) {
				sessionService.leaveSession(session.getSessionId(), sessionUserStatusUpdateDTO.getUserId());
			} else if (sessionUserStatusUpdateDTO.getStatus().equalsIgnoreCase("close")) {
				if (logger.isInfoEnabled()) {
					logger.info("close session:" + eaasSessionId);
				}
				if (session != null) {
					sessionService.closeSession(session.getSessionId());
				}
			}
		}
	}

	private void inputCheck(SessionUserStatusUpdateDTO sessionUserStatusUpdateDTO) {
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO, "sessionUserStatusUpdateDTO is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getCorrelatedSessionId(), "correlatedSessionId is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getStatus(), "status is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getUserId() , "userId is required");
	}
}
