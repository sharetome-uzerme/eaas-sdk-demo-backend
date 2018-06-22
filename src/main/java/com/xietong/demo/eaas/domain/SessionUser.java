package com.xietong.demo.eaas.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/18.
 */
public class SessionUser implements Serializable{

    String userId;
    String userDisplayName;
    String avatarUrl;
    Status status;

    public SessionUser(String userId) {
        this.userId = userId;
    }

    public SessionUser(String userId, String userDisplayName) {
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.status = Status.Offline;
    }

    public SessionUser(String userId, String userDisplayName, String avatarUrl) {
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.avatarUrl = avatarUrl;
        this.status = Status.Offline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionUser)) return false;

        SessionUser that = (SessionUser) o;

        return userId.equals(that.userId);

    }

 
    /**
     * Created by Administrator on 2016/6/18.
     */
    public static enum Status {
        Online,
        Offline,
    }
}
