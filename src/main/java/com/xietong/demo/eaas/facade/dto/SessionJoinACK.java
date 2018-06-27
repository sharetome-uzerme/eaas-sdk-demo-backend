package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/10.
 */
public class SessionJoinACK {
    private Long sessionId;
    private String gwHost;
    private Integer gwPort;
    private String mcuUrl;
    private String token;
    private String clientId;
    private String sessionMgrUrl;
    private String correlatedSessionId;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
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

	public String getCorrelatedSessionId() {
		return correlatedSessionId;
	}

	public void setCorrelatedSessionId(String correlatedSessionId) {
		this.correlatedSessionId = correlatedSessionId;
	}
}
