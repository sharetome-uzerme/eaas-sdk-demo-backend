package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SessionJoinEventDTO {
    private String clientId;
    private String clientSecret;
    private String sessionId;
    private String userId;

    public SessionJoinEventDTO() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SessionJoinEventDTO{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", correlatedSessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
