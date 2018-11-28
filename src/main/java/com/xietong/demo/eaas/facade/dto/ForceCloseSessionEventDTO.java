package com.xietong.demo.eaas.facade.dto;

public class ForceCloseSessionEventDTO {
	private String sessionId;
	private String userId;

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
		return "ForceCloseSessionEvent [sessionId=" + sessionId + ", userId="
				+ userId + "]";
	}
}
