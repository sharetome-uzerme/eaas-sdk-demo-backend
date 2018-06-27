package com.xietong.demo.eaas.facade.rest;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.facade.dto.*;
import com.xietong.demo.eaas.service.SessionService;
import com.xietong.demo.eaas.service.SessionStoreService;
import com.xietong.demo.eaas.service.SessionUserService;
import com.xietong.demo.util.ResponseUtil;
import com.xietong.phoenix.common.util.ParamValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Administrator on 2016/6/18.
 * 
 * <li>Create Session
 * <li>Join Session
 * <li>Close session
 * 
 */
@RestController
@Api(value = "云应用基本操作")
public class SessionUserResource {
	private final static Logger logger = Logger.getLogger(SessionUserResource.class);

	@Value("${xt.clientId}")
	String clientId;
	@Value("${xt.clientSecret}")
	String clientSecret;

	@Value("${xt.sessionmgr.service.url}")
	String sessionMgrUrl;

	@Autowired
	SessionService sessionService;

	@Autowired
	SessionUserService sessionUserService;

	@Autowired
	SessionStoreService sessionStoreService;

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
		String eaasSessionId = sessionUserStatusUpdateDTO.getCorrelatedSessionId();
		if (eaasSessionId == null) {
			ResponseUtil.buildResponseDTO(false);
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

		return ResponseUtil.buildResponseDTO(true);
	}

	/**
	 * 验证是否允许用户加入 Eaas session
	 * 对应于mongodb中client_configuration配置的SessionUserVerify回调地址。
	 * 
	 * @param sessionId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/verify", method = GET)
	@ApiOperation(value = "验证会话用户状态", response = ResponseDTO.class)
	public ResponseDTO sessionVerifySessionUser(
			@ApiParam(value = "会话ID") @RequestParam(value = "sessionId", required = false) String sessionId,
			@ApiParam(value = "用户ID", required = true) @RequestParam(value = "userId", required = false) String userId) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("sessionVerifySessionUser:");
		}
		
		boolean result = sessionUserService.verifyParticipant(Long.valueOf(sessionId), userId);

		return ResponseUtil.buildResponseDTO(result);
	}


	/**
	 * Eaas session邀请用户加入session.
	 * InviteSessionUser回调地址。
	 *
	 * @param inviteRequest
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/user/invite", method = {RequestMethod.POST,RequestMethod.PUT}, consumes = "application/json")
	public ResponseDTO inviteSessionUsersCallBack(@RequestBody SessionInviteRequestDTO inviteRequest) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("inviteSessionUsersCallBack:");
		}
		ParamValidateUtils.checkNotNull(inviteRequest.getInviterList(), "no invite list found");
		ParamValidateUtils.checkArgument(inviteRequest.getInviterList().size() != 0, "no invite list found");

		String sessionId = getSessionId(inviteRequest);
		if (sessionId == null) {
			return new ResponseDTO();
		}

		List<SessionUser> sessionUsers = buildSessionUserList(inviteRequest);
		sessionUserService.inviteParticipants(Long.valueOf(sessionId), inviteRequest.getUserId(), sessionUsers, inviteRequest.getMessage());
		return new ResponseDTO();
	}

	/**
	 * 获取Eaas session里参与人列表
	 * SessionUserList回调地址。
	 *
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/api/session/{sessionId}/user/list", method = GET)
	public ResponseDTO getSessionUserList(@PathVariable(value = "sessionId") String sessionId) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("getSessionUserList:");
		}
		List<SessionUser> SessionUserList = sessionUserService.getParticipantList(Long.valueOf(sessionId));
		List<SessionUserDTO> SessionUserDTOs = buildSessionUserList(SessionUserList);
		return new ResponseDTO(SessionUserDTOs);
	}

	@ResponseBody
	@RequestMapping(value = "/api/session/user/transform", method = {RequestMethod.POST,RequestMethod.PUT}, consumes = "application/json")
	@ApiOperation(value = "控制权转换", response = ResponseDTO.class)
	public ResponseDTO SessionUserTransform(@ApiParam(value = "控制权转换信息", required = true) @RequestBody SessionUserTransformDTO sessionUserTransformDTO) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("SessionUserTransform:");
		}
		if (sessionUserTransformDTO == null || sessionUserTransformDTO.getSessionId() == null
				|| sessionUserTransformDTO.getFromUserId() == null || sessionUserTransformDTO.getToUserId() == null) {
			return null;
		}

		if (sessionUserTransformDTO.getSessionId() != null) {
			sessionUserService.participantTransform(Long.valueOf(sessionUserTransformDTO.getSessionId()),
					sessionUserTransformDTO.getFromUserId(),
					sessionUserTransformDTO.getToUserId()
			);
		}
		return new ResponseDTO();
	}


	private List<SessionUser> buildSessionUserList(SessionInviteRequestDTO inviteRequest) {
		List<SessionUser> SessionUsers = new ArrayList<>();
		for (SessionUserDTO sessionUserDTO : inviteRequest.getInviterList()) {
			SessionUsers.add(fromDTO(sessionUserDTO));
		}
		return SessionUsers;
	}

	private String getSessionId(SessionInviteRequestDTO inviteRequest) {
		String sessionId = null;
		if (inviteRequest.getSessionId() != null && !inviteRequest.getSessionId().isEmpty()) {
			sessionId = inviteRequest.getSessionId();
		} else {

		}
		return sessionId;
	}
	
	private List<SessionUserDTO> buildSessionUserList(List<SessionUser> sessionUserList) {
		List<SessionUserDTO> sessionUserDTOs = new ArrayList<>();
		if(sessionUserList==null||sessionUserList.size()==0){
			return sessionUserDTOs;
		}
		for (SessionUser SessionUser : sessionUserList) {
			sessionUserDTOs.add(toDTO(SessionUser));
		}
		return sessionUserDTOs;
	}

	private SessionUser fromDTO(SessionUserDTO participantDTO) {
		SessionUser participant = new SessionUser(
				participantDTO.getId(),
				participantDTO.getNickName()
		);
		participant.setAvatarUrl(participantDTO.getAvatarUrl());
		return participant;
	}
	private SessionUserDTO toDTO(SessionUser participant){
		SessionUserDTO participantDTO = new SessionUserDTO();
		participantDTO.setId(participant.getUserId());
		participantDTO.setNickName(participant.getUserDisplayName());
		participantDTO.setStatus(participant.getStatus());
		participantDTO.setAvatarUrl(participant.getAvatarUrl());
		participantDTO.setIsAnonymous(false);
		return participantDTO;
	}

	private void inputCheck(SessionUserStatusUpdateDTO sessionUserStatusUpdateDTO) {
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO, "sessionUserStatusUpdateDTO is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getCorrelatedSessionId(), "correlatedSessionId is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getStatus(), "status is required");
		ParamValidateUtils.checkNotNull(sessionUserStatusUpdateDTO.getUserId() , "userId is required");
	}
}
