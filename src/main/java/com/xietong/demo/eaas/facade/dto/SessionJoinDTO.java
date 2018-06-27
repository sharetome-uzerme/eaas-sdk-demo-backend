package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/13.
 */
public class SessionJoinDTO {
    Long sessionId;
    String userId;
    String nickName;


    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
