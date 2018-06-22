package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/10.
 */
public class SessionInfoDTO {
    private String sessionId;
    private String gwHost;
    private Integer gwPort;
    private String mcuUrl;
    private Boolean readOnly;
    private String token;
    private String sessionMgrUrl;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getSessionMgrUrl() {
        return sessionMgrUrl;
    }

    public void setSessionMgrUrl(String sessionMgrUrl) {
        this.sessionMgrUrl = sessionMgrUrl;
    }
}
