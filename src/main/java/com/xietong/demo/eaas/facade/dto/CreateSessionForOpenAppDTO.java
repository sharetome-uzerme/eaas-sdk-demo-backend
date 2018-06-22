package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CreateSessionForOpenAppDTO {
    String appId;
    String userId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "CreateSessionForOpenAppDTO [appId=" + appId + ", userId="
				+ userId + "]";
	}

}
