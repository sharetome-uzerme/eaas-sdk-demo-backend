package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.facade.dto.*;

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
	SessionJoinACK joinSession(Long sessionId, String userId);

	/**
	 * remove session
	 *
	 * @param sessionId
	 */
	void forceCloseSession(Long sessionId);

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
