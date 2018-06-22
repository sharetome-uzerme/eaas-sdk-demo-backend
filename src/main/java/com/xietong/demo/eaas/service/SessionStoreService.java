package com.xietong.demo.eaas.service;


import com.xietong.demo.eaas.domain.Session;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SessionStoreService {
    Session readSession(Long sessionId);

    Session readSession(String eaasSessionId);

    void writeSession(Long sessionId, Session session);

    void removeSession(Long sessionId);

    Long getSessionIdByEaaSSessionId(String eaasSessionId);

    String getEaaSSessionIdBySessionId(Long sessionId);

}
