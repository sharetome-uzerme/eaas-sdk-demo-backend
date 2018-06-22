package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/10.
 */
public class SessionCreateACK {
    private Long sessionId;
    private String correlatedSessionId;
    private String gwHost;
    private Integer gwPort;
    private String mcuUrl;
    private Boolean readOnly;
    private String token;
    private String clientId;
    private String sessionMgrUrl;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getCorrelatedSessionId() {
        return correlatedSessionId;
    }

    public void setCorrelatedSessionId(String correlatedSessionId) {
        this.correlatedSessionId = correlatedSessionId;
    }

    public String getGwHost() {
        return gwHost;
    }

    public void setGwHost(String gwHost) {
        this.gwHost = gwHost;
    }

    public Integer getGwPort() {
        return gwPort;
    }

    public void setGwPort(Integer gwPort) {
        this.gwPort = gwPort;
    }

    public String getMcuUrl() {
        return mcuUrl;
    }

    public void setMcuUrl(String mcuUrl) {
        this.mcuUrl = mcuUrl;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSessionMgrUrl() {
        return sessionMgrUrl;
    }

    public void setSessionMgrUrl(String sessionMgrUrl) {
        this.sessionMgrUrl = sessionMgrUrl;
    }
}
