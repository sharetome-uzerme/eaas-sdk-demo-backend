package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.facade.dto.*;
import com.xietong.demo.eaas.service.EAASService;
import com.xietong.demo.eaas.service.SessionService;
import com.xietong.demo.eaas.service.SessionStoreService;
import com.xietong.demo.eaas.service.SessionUserService;
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

	@Autowired
	SessionStoreService sessionStoreService;

	@Autowired
	SessionUserService sessionUserService;

	@Autowired
	EAASService eaasService;

	@Override
	public SessionCreateACK createSessionForOpenApp(CreateSessionForOpenAppDTO request) {
		if (logger.isInfoEnabled()) {
			logger.info("createSessionForOpenApp:" + request);
		}
		// create EAAS session
		SessionInfoDTO sessionInfoDTO = eaasService.createEaaSSession(request);
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
		SessionInfoDTO sessionInfoDTO = eaasService.createEaaSSession(request);

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
	public SessionJoinACK joinSession(Long sessionId, String userId) {
		Session session = getSession(sessionId);
		if (session != null) {
			// call EaaS service
			SessionJoinEventDTO sessionJoinEventDTO = buildJoinSessionDTO(userId, session.getCorrelatedSessionId());
			sessionUserService.participantJoin(sessionId, userId);
			SessionInfoDTO sessionInfoDTO = eaasService.joinEaaSSession(sessionJoinEventDTO);
			if (sessionInfoDTO != null) {
				SessionJoinACK sessionJoinACK = populateJoinSessionACK(sessionInfoDTO, session.getSessionId());
				return sessionJoinACK;
			}
		}
		return null;
	}

	@Override
	public void forceCloseSession(Long sessionId) {
		Session session = sessionStoreService.readSession(sessionId);
		if (session == null) {
			logger.warn("Session is not exists :: (" + sessionId + ")");
			return;
		}
		ForceCloseSessionEventDTO forceCloseSessionEvent = new ForceCloseSessionEventDTO();
		forceCloseSessionEvent.setSessionId(session.getCorrelatedSessionId());
		eaasService.forceCloseEAASSession(forceCloseSessionEvent);
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
		sessionJoinEventDTO.setUserId(userId);
		sessionJoinEventDTO.setSessionId(sessionId);
		return sessionJoinEventDTO;
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

	private SessionJoinACK populateJoinSessionACK(SessionInfoDTO sessionInfoDTO, Long sessionId) {
		SessionJoinACK sessionJoinACK = new SessionJoinACK();
		sessionJoinACK.setSessionId(sessionId);
		sessionJoinACK.setCorrelatedSessionId(sessionInfoDTO.getSessionId());
		sessionJoinACK.setGwHost(sessionInfoDTO.getGwHost());
		sessionJoinACK.setGwPort(sessionInfoDTO.getGwPort());
		sessionJoinACK.setMcuUrl(sessionInfoDTO.getMcuUrl());
		sessionJoinACK.setToken(sessionInfoDTO.getToken());
		sessionJoinACK.setClientId(clientId);
		sessionJoinACK.setSessionMgrUrl(sessionInfoDTO.getSessionMgrUrl());
		return sessionJoinACK;
	}
}
