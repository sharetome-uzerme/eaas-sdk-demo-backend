package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.domain.SessionUser;

import java.util.List;

public interface SessionUserService {
	/**
	 * @param sessionId
	 * @param inviterUserId
	 * @param participants
	 * @param message
	 */
	void inviteParticipants(Long sessionId, String inviterUserId, List<SessionUser> participants, String message);

	/**
	 * @param sessionId
	 * @param participantId
	 */
	void participantJoin(Long sessionId, String participantId);

	/**
	 * @param sessionId
	 * @param participantId
	 */
	void participantJoinConfirm(Long sessionId, String participantId);

	/**
	 * @param sessionId
	 * @param participantId
	 */
	void participantLeave(Long sessionId, String participantId);

	/**
	 * @param sessionId
	 * @param participantId
	 */
	void participantSessionClose(Long sessionId, String participantId);

	/**
	 * @param sessionId
	 * @param fromUserId
	 * @param toUserId
	 */
	void participantTransform(Long sessionId, String fromUserId, String toUserId);

	/**
	 * @param sessionId
	 * @return
	 */
	List<SessionUser> getParticipantList(Long sessionId);

	/**
	 * @param sessionId
	 * @param userId
	 * @return
	 */
	boolean verifyParticipant(Long sessionId, String userId);
}
