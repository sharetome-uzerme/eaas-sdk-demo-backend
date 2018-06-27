package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2016/7/23.
 */
public class SessionUserTransformDTO {
    String sessionId;
    String fromUserId;
    String toUserId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
