package com.xietong.demo.eaas.facade.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/7/23.
 */
public class SessionInviteRequestDTO {
    String sessionId;
    String userId;
    List<SessionUserDTO> inviterList;
    String message;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SessionUserDTO> getInviterList() {
        return inviterList;
    }

    public void setInviterList(List<SessionUserDTO> inviterList) {
        this.inviterList = inviterList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
