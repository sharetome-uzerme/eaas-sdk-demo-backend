package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.facade.dto.SessionCreateACK;
import com.xietong.demo.eaas.facade.dto.CreateSessionForOpenAppDTO;
import com.xietong.demo.eaas.facade.dto.CreateSessionForOpenFileWithAppDTO;
import com.xietong.demo.eaas.facade.dto.SessionInfoDTO;

public interface SessionService {
	String SESSION_TYPE_EDITING = "EDITING";
	/**
	 * @param request
	 * @return
	 */
	SessionCreateACK createSessionForOpenApp(CreateSessionForOpenAppDTO request);

	/**
	 * @param request
	 * @return
	 */
	SessionCreateACK createSessionForOpenFile(CreateSessionForOpenFileWithAppDTO request);

	/**
	 * @param eaasSessionId
	 * @param sessionOwner
	 * @param appId
	 * @param fileId
	 * @param filePath
	 * @param isMultiFileEditingMode
	 * @return
	 */
	Session createSession(String eaasSessionId, SessionUser sessionOwner, String appId, String fileId, String filePath, boolean isMultiFileEditingMode);

	/**
	 * @param sessionId
	 * @param userId
	 * @return
	 */
	SessionInfoDTO joinSession(Long sessionId, String userId);

	/**
	 * @param sessionId
	 * @param userId
	 */
	void leaveSession(Long sessionId, String userId);

	/**
	 * remove session
	 *
	 * @param sessionId
	 */
	void closeSession(Long sessionId);

	/**
	 * Get session from cache
	 *
	 * @param sessionId
	 * @return
	 */
	Session getSession(Long sessionId);

	/**
	 * Get session from cache
	 *
	 * @param eaasSessionId
	 * @return
	 */
	Session getSession(String eaasSessionId);
}
