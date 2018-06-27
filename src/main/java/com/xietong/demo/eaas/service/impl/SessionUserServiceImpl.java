package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.domain.SessionUser;
import com.xietong.demo.eaas.service.SessionStoreService;
import com.xietong.demo.eaas.service.SessionUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/18.
 */
@Service
public class SessionUserServiceImpl implements SessionUserService {
    private final static Logger logger = Logger.getLogger(SessionUserServiceImpl.class);

    @Autowired
    SessionStoreService sessionStoreService;

    @Override
    public void inviteParticipants(Long sessionId, String inviterUserId, List<SessionUser> participants, String message) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        addParticipantsToInviteList(sessionId, participants);
    }

    @Override
    public void participantJoinWithoutLimit(Long sessionId, SessionUser participant) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        participant.setStatus(SessionUser.Status.Offline);
        session.addSessionUser(participant);
        sessionStoreService.writeSession(Long.valueOf(sessionId), session);
        removeParticipantFromInviteList(sessionId, participant.getUserId());
    }

    @Override
    public void participantJoin(Long sessionId, String participantId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        SessionUser participant = getInvitedParticipant(sessionId, participantId);
        if (participant == null) {
            logger.warn("User not in invite list :: (" + participantId + ")");
            return;
        }
        participant.setStatus(SessionUser.Status.Offline);
        session.addSessionUser(participant);
        sessionStoreService.writeSession(sessionId, session);
        removeParticipantFromInviteList(sessionId, participantId);
    }

    @Override
    public void participantJoinConfirm(Long sessionId, String participantId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        SessionUser participant = session.getSessionUser(participantId);
        participant.setStatus(SessionUser.Status.Online);
        sessionStoreService.writeSession(sessionId, session);
    }

    @Override
    public void participantLeave(Long sessionId, String participantId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        session.removeSessionUser(participantId);
        sessionStoreService.writeSession(sessionId, session);
    }

    @Override
    public void participantSessionClose(Long sessionId, String participantId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        sessionStoreService.removeSession(sessionId);
    }

    @Override
    public void participantTransform(Long sessionId, String fromUserId, String toUserId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return;
        }
        SessionUser participant = getParticipantInfo(toUserId);
        participant.setStatus(SessionUser.Status.Online);
        session.removeSessionUser(fromUserId);
        session.addSessionUser(participant);
        sessionStoreService.writeSession(sessionId, session);
    }

    @Override
    public List<SessionUser> getParticipantList(Long sessionId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return null;
        }
        return session.getSessionUsers();
    }

    @Override
    public boolean verifyParticipant(Long sessionId, String userId) {
        Session session = sessionStoreService.readSession(sessionId);
        if (session == null) {
            logger.warn("Session is not exists :: (" + sessionId + ")");
            return false;
        }
        if (session.getSessionUsers().isEmpty()) {
            return false;
        }
        return session.getSessionUsers().contains(new SessionUser(userId));
    }

    static final String PREFIX = "participant_";
    static final String INVITE_LIST = PREFIX + "inviteList:";
    private static ConcurrentHashMap<String, SessionUser> participantInfoConcurrentHashMap = new ConcurrentHashMap();
    private SessionUser getParticipantInfo(String participantId) {
        SessionUser userInfo = participantInfoConcurrentHashMap.get(participantId);
        if (userInfo == null) {
            logger.error("user info not found, participantId is :: " + participantId);
            return null;
        }
        SessionUser participant = new SessionUser(participantId, userInfo.getUserDisplayName());
        participant.setAvatarUrl(userInfo.getAvatarUrl());
        return participant;
    }

    private void addParticipantsToInviteList(Long sessionId, List<SessionUser> participantList) {
        if (sessionId == null)
            return;
        if (CollectionUtils.isEmpty(participantList))
            return;
        for (SessionUser participant : participantList) {
            participantInfoConcurrentHashMap.put(getInviteListKey(sessionId, participant.getUserId()), participant);
        }
    }

    private SessionUser getInvitedParticipant(Long sessionId, String participantId) {
        if (sessionId == null || participantId == null)
            return null;
        Object obj = participantInfoConcurrentHashMap.get(getInviteListKey(sessionId, participantId));
        if (obj == null)
            return null;
        return (SessionUser) obj;
    }

    private void removeParticipantFromInviteList(Long sessionId, String participantId) {
        if (sessionId == null || participantId == null)
            return;
        participantInfoConcurrentHashMap.remove(getInviteListKey(sessionId, participantId));
    }

    String getInviteListKey(Long sessionId, String userId) {
        return INVITE_LIST + String.valueOf(sessionId) + "@" + userId;
    }
}
