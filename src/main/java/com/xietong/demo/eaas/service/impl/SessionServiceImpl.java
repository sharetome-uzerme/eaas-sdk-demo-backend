package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.domain.exception.CreateSessionFailException;
import com.xietong.demo.eaas.facade.dto.*;
import com.xietong.demo.eaas.service.SessionService;
import com.xietong.demo.eaas.service.SessionStoreService;
import com.xietong.phoenix.eaas.service.api.IEAASServiceAPI;
import com.xietong.phoenix.eaas.service.api.IEAASServiceAPIResult;
import com.xietong.phoenix.eaas.service.api.builder.session.CreateSessionAPIBuilder;
import com.xietong.phoenix.eaas.service.api.builder.session.JoinSessionAPIBuilder;
import com.xietong.phoenix.eaas.service.core.caller.EAASServiceAPIResult;
import com.xietong.phoenix.eaas.service.core.caller.param.EAASServiceAPICallParam;
import com.xietong.phoenix.eaas.service.core.common.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/25.
 */
@Service
public class SessionServiceImpl implements SessionService {
	private final static Logger logger = Logger.getLogger(SessionServiceImpl.class);

	@Value("${eaas.config.clientId}")
	String clientId;
	@Value("${eaas.config.clientSecret}")
	String clientSecret;
	@Value("${eaas.config.server.dns}")
	private String eaasServerDomain;

	final boolean  isReadOnlyMode = false;      // user open file with read auth only
	final boolean canSaveFile = true;          // user open app can save file
	final boolean canExportFile = false;      // user open app can save as (to new file)
	final boolean canCopyPasteOut = false;    // user open app can copy (paste out content)
	final boolean isDFSMode = false;           // user open app DFS depend on (tenant and app config)

	@Autowired
	IEAASServiceAPI eaasServiceAPIRestProxy;

	@Autowired
	SessionStoreService sessionStoreService;

	@Override
	public SessionCreateACK createSessionForOpenApp(CreateSessionForOpenAppDTO request) {
		if (logger.isInfoEnabled()) {
			logger.info("createSessionForOpenApp:" + request);
		}
		// create EAAS session
		SessionInfoDTO sessionInfoDTO = createEaaSSession(request);
		if (sessionInfoDTO == null){
			return null;
		}

		String nickName = "User" + request.getUserId();
		SessionUser owner = new SessionUser(request.getUserId(), nickName);
		Session session = createSession(sessionInfoDTO.getSessionId(), owner, request.getAppId(), null, null, false);
		// populate ack
		SessionCreateACK sessionCreateACK = populateCreateSessionACK(sessionInfoDTO, session.getSessionId());
		return sessionCreateACK;

	}

	@Override
	public SessionCreateACK createSessionForOpenFile(CreateSessionForOpenFileWithAppDTO request) {
		if (logger.isInfoEnabled()) {
			logger.info("createSessionForOpenFile");
		}
		// create EAAS session
		SessionInfoDTO sessionInfoDTO = createEaaSSession(request);

		// create session user info
		String userName = "test" + request.getUserId();
		SessionUser owner = new SessionUser(request.getUserId(), userName);
		Session session = createSession(sessionInfoDTO.getSessionId(), owner, request.getAppId(), request.getFileId(), null, false);
		if(logger.isInfoEnabled()){
			logger.info("session:"+session);
		}

		// populate ack
		SessionCreateACK sessionCreateACK = populateCreateSessionACK(sessionInfoDTO, session.getSessionId());
		sessionCreateACK.setReadOnly(request.getReadOnly());
		return sessionCreateACK;
	}

	@Override
	public Session createSession(String eaasSessionId, SessionUser sessionOwner, String appId, String fileId, String filePath, boolean isMultiFileEditingMode) {
		Map<String, Object> param = new HashMap<>();
		if (appId != null) {
			param.put(Session.PARAM_APP_ID, appId);
		}
		if (fileId != null) {
			param.put(Session.PARAM_FILE_ID, fileId);
		}
		if (filePath != null) {
			param.put(Session.PARAM_FILE_PATH, filePath);
		}
		
		sessionOwner.setStatus(SessionUser.Status.Online);
		Session session = new Session(sessionOwner, param);
		session.setCorrelatedSessionId(eaasSessionId);
		session.setIsMultiFileEditingMode(isMultiFileEditingMode);
		sessionStoreService.writeSession(session.getSessionId(), session);
		return session;
	}

	@Override
	public SessionInfoDTO joinSession(Long sessionId, String userId) {
		Session session = getSession(sessionId);
		if (session != null) {
			// call EaaS service
			SessionJoinEventDTO sessionJoinEventDTO = buildJoinSessionDTO(userId, session.getCorrelatedSessionId());
			SessionInfoDTO sessionInfoDTO = joinEaaSSession(sessionJoinEventDTO);
			if (sessionInfoDTO != null) {
				SessionUser sessionUser = session.getSessionUser(userId);
				sessionUser.setStatus(SessionUser.Status.Online);
				sessionStoreService.writeSession(sessionId, session);

				return sessionInfoDTO;
			} else{
				sessionStoreService.removeSession(sessionId);
				return null;
			}
		}

		return null;
	}

	@Override
	public void leaveSession(Long sessionId, String userId) {
		Session session = sessionStoreService.readSession(sessionId);
		if (session == null) {
			logger.warn("Session is not exists :: (" + sessionId + ")");
			return;
		}
		session.removeSessionUser(userId);
		sessionStoreService.writeSession(sessionId, session);
	}

	@Override
	public void closeSession(Long sessionId) {
		Session session = sessionStoreService.readSession(sessionId);
		if (session == null) {
			logger.warn("Session is not exists :: (" + sessionId + ")");
			return;
		}

		sessionStoreService.removeSession(sessionId);
	}

	@Override
	public Session getSession(Long sessionId) {
		return sessionStoreService.readSession(sessionId);
	}

	@Override
	public Session getSession(String eaasSessionId) {
		return sessionStoreService.readSession(eaasSessionId);
	}

	private SessionJoinEventDTO buildJoinSessionDTO(String userId, String sessionId) {
		SessionJoinEventDTO sessionJoinEventDTO = new SessionJoinEventDTO();
		sessionJoinEventDTO.setClientId(clientId);
		sessionJoinEventDTO.setClientSecret(clientSecret);
		sessionJoinEventDTO.setUserId(userId);
		sessionJoinEventDTO.setSessionId(sessionId);
		return sessionJoinEventDTO;
	}

	private SessionStartEventDTO buildCreateSessionDTO(CreateSessionForOpenAppDTO request) {
		// create EAAS session
		SessionStartEventDTO sessionStartEventDTO = new SessionStartEventDTO();

		sessionStartEventDTO.setType(SESSION_TYPE_EDITING);
		sessionStartEventDTO.setAppId(request.getAppId());
		sessionStartEventDTO.setUserId(request.getUserId());

		sessionStartEventDTO.setIsReadOnlyMode(isReadOnlyMode); // user open file with read auth only
		sessionStartEventDTO.setCanSaveFile(canSaveFile);  // user open app can save file
		sessionStartEventDTO.setCanExportFile(canExportFile);// user open app can save as (to new file)
		sessionStartEventDTO.setCanCopyPasteOut(canCopyPasteOut);// user open app can copy (paste out content)
		sessionStartEventDTO.setIsDFSMode(isDFSMode);// user open app DFS depend on (tenant and app config)

		if (request instanceof CreateSessionForOpenFileWithAppDTO) {
			sessionStartEventDTO.setFileId(((CreateSessionForOpenFileWithAppDTO) request).getFileId());
			String fileName = ((CreateSessionForOpenFileWithAppDTO) request).getFileName();
			sessionStartEventDTO.setFileName(fileName);
			sessionStartEventDTO.setIsReadOnlyMode(((CreateSessionForOpenFileWithAppDTO) request).getReadOnly());
			sessionStartEventDTO.setCanSaveFile(canSaveFile);
			sessionStartEventDTO.setCanExportFile(canExportFile);
			sessionStartEventDTO.setCanCopyPasteOut(canCopyPasteOut);
		}

		return sessionStartEventDTO;
	}

	private SessionCreateACK populateCreateSessionACK(SessionInfoDTO sessionInfoDTO, Long sessionId) {
		SessionCreateACK sessionCreateACK = new SessionCreateACK();
		sessionCreateACK.setSessionId(sessionId);
		sessionCreateACK.setCorrelatedSessionId(sessionInfoDTO.getSessionId());
		sessionCreateACK.setGwHost(sessionInfoDTO.getGwHost());
		sessionCreateACK.setGwPort(sessionInfoDTO.getGwPort());
		sessionCreateACK.setMcuUrl(sessionInfoDTO.getMcuUrl());
		sessionCreateACK.setReadOnly(sessionInfoDTO.getReadOnly());
		sessionCreateACK.setToken(sessionInfoDTO.getToken());
		sessionCreateACK.setClientId(clientId);
		sessionCreateACK.setSessionMgrUrl(sessionInfoDTO.getSessionMgrUrl());
		return sessionCreateACK;
	}

	public SessionInfoDTO createEaaSSession(CreateSessionForOpenAppDTO request) {
		SessionStartEventDTO sessionStartEventDTO = buildCreateSessionDTO(request);

		CreateSessionAPIBuilder builder = new CreateSessionAPIBuilder()
				.resourceIp(eaasServerDomain)
				.appId(sessionStartEventDTO.getAppId())
				.userId(sessionStartEventDTO.getUserId())
				.groupId(sessionStartEventDTO.getGroupId())
				.fileId(sessionStartEventDTO.getFileId())
				.fileVersion(sessionStartEventDTO.getFileVersion())
				.fileName(sessionStartEventDTO.getFileName())
				.filePath(sessionStartEventDTO.getFilePath())
				.type(sessionStartEventDTO.getType())
				.appUrl(sessionStartEventDTO.getAppUrl())
				.isReadOnlyMode(sessionStartEventDTO.getIsReadOnlyMode())
				.isDFSMode(sessionStartEventDTO.getIsDFSMode())
				.canSaveFile(sessionStartEventDTO.getCanSaveFile())
				.canExportFile(sessionStartEventDTO.getCanExportFile())
				.canCopyPasteOut(sessionStartEventDTO.getCanCopyPasteOut());

		IEAASServiceAPIResult<String> result = eaasServiceAPIRestProxy.callApi(builder.build());
		return parseResponse(eaasServerDomain + Constants.EAAS_SESSIONMGR_URL, (EAASServiceAPIResult) result);
	}

	private SessionInfoDTO joinEaaSSession(SessionJoinEventDTO event) {
		logger.info("joinEaaSSession.event=" + event);

		EAASServiceAPICallParam param = new JoinSessionAPIBuilder()
				.resourceIp(eaasServerDomain)
				.sessionId(event.getSessionId())
				.userId(event.getUserId())
				.build();

		IEAASServiceAPIResult<String> result = eaasServiceAPIRestProxy.callApi(param);

		return parseResponse(eaasServerDomain + Constants.EAAS_SESSIONMGR_URL, (EAASServiceAPIResult) result);
	}


	private SessionInfoDTO parseResponse(String sessionUrl, EAASServiceAPIResult result) {
		SessionInfoDTO sessionInfoDTO = new SessionInfoDTO();
		if (result.isSuccess()) {
			Map<String, Object> map = (Map<String, Object>) result.getData();
			sessionInfoDTO.setSessionId((String) map.get("sessionId"));
			sessionInfoDTO.setGwHost((String) map.get("gwHost"));
			sessionInfoDTO.setGwPort((Integer) map.get("gwPort"));
			sessionInfoDTO.setMcuUrl((String) map.get("mcuUrl"));
			sessionInfoDTO.setReadOnly((Boolean) map.get("readOnly"));
			sessionInfoDTO.setToken(result.getToken());
			sessionInfoDTO.setSessionMgrUrl(sessionUrl);
			return sessionInfoDTO;
		}
		throw new CreateSessionFailException(Integer.valueOf(result.getErrorCode()), result.getMessage());
	}
}
