package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2016/7/23.
 */
public class SessionUserStatusUpdateDTO {
    String correlatedSessionId;  // eaas session id
    String userId;
    String status;

    public String getCorrelatedSessionId() {
        return correlatedSessionId;
    }

    public void setCorrelatedSessionId(String correlatedSessionId) {
        this.correlatedSessionId = correlatedSessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "SessionUserStatusUpdateDTO [correlatedSessionId="
				+ correlatedSessionId + ", userId=" + userId + ", status="
				+ status + "]";
	}
}
