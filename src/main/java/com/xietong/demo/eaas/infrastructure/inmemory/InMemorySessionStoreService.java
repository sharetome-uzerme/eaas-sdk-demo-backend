package com.xietong.demo.eaas.infrastructure.inmemory;

import com.xietong.demo.eaas.domain.Session;
import com.xietong.demo.eaas.service.SessionStoreService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2016/6/20.
 */
@Service
public class InMemorySessionStoreService implements SessionStoreService {

	private static ConcurrentMap<Long, Session> sessionConcurrentHashMap = new ConcurrentHashMap();
	private static ConcurrentHashMap<Long, String> sessionToEaaSSession = new ConcurrentHashMap();
	private static ConcurrentHashMap<String, Long> correlatedSessionToSession = new ConcurrentHashMap();

	@Override
	public Session readSession(Long sessionId) {
		return sessionConcurrentHashMap.get(sessionId);
	}

	@Override
	public Session readSession(String eaasSessionId) {
		Long sessionId = getSessionIdByEaaSSessionId(eaasSessionId);
		if (sessionId == null) {
			return null;
		}
		return sessionConcurrentHashMap.get(sessionId);
	}

	@Override
	public void writeSession(Long sessionId, Session session) {
		sessionConcurrentHashMap.put(sessionId, session);
		if (session.getCorrelatedSessionId() != null) {
			sessionToEaaSSession.put(sessionId, session.getCorrelatedSessionId());
			correlatedSessionToSession.put(session.getCorrelatedSessionId(), sessionId);
		}
	}

	@Override
	public void removeSession(Long sessionId) {
		sessionConcurrentHashMap.remove(sessionId);
		String correlatedSessionId = getEaaSSessionIdBySessionId(sessionId);
		correlatedSessionToSession.remove(correlatedSessionId);
		sessionToEaaSSession.remove(sessionId);
	}

	@Override
	public Long getSessionIdByEaaSSessionId(String correlatedSessionId) {
		return correlatedSessionToSession.get(correlatedSessionId);
	}

	@Override
	public String getEaaSSessionIdBySessionId(Long sessionId) {
		return sessionToEaaSSession.get(sessionId);
	}

}
