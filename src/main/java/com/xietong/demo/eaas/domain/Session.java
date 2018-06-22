package com.xietong.demo.eaas.domain;

import com.xietong.phoenix.common.util.IdWorker;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Administrator on 2016/6/18.
 */
public class Session implements Serializable {
    public final static String PARAM_FILE_ID = "param_file_id";
    public final static String PARAM_FILE_PATH = "param_file_path";
    public final static String PARAM_APP_ID = "param_app_id";

    Long sessionId;
    String correlatedSessionId;
    Date startTime;
    String sessionOwnerId;
    Boolean isMultiFileEditingMode=false;
    List<SessionUser> sessionUsers = new ArrayList<>();
    Map<String, Object> parameters = new HashMap<>();

    public Session(SessionUser sessionOwner, Map<String, Object> parameters) {
        this.sessionId = IdWorker.getInstance(2).nextId();
        this.sessionOwnerId = sessionOwner.getUserId();
        this.startTime = new Date();
        this.parameters = parameters;
        this.sessionUsers.add(sessionOwner);
    }

    public Object getParameterValue(String parameterName){
        if (this.parameters==null)
            return null;
        return this.parameters.get(parameterName);
    }

    public List<SessionUser> getSessionUsers() {
        return this.sessionUsers;
    }

    public SessionUser getSessionUser(String userId) {
        if (this.sessionUsers ==null)
            return null;
        for (SessionUser sessionUser : sessionUsers){
            if (sessionUser.getUserId().equals(userId))
                return sessionUser;
        }
        return null;
    }

    public void addSessionUser(SessionUser sessionUser){
        if (!this.sessionUsers.contains(sessionUser))
            this.sessionUsers.add(sessionUser);
    }

    public void removeSessionUser(String userId){
        this.sessionUsers.remove(getSessionUser(userId));
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Boolean getIsMultiFileEditingMode() {
        return isMultiFileEditingMode;
    }

    public void setIsMultiFileEditingMode(Boolean isMultiFileEditingMode) {
        this.isMultiFileEditingMode = isMultiFileEditingMode;
    }

    public SessionUser getSessionOwner() {
        SessionUser sessionOwner = null;
        if (this.sessionOwnerId==null)
            return null;
        sessionOwner = getSessionUser(sessionOwnerId);
        if (sessionOwner==null)
            return null;
        return sessionOwner;
    }

    public String getCorrelatedSessionId() {
        return correlatedSessionId;
    }

    public void setCorrelatedSessionId(String correlatedSessionId) {
        this.correlatedSessionId = correlatedSessionId;
    }

    
    @Override
	public String toString() {
		return "Session [sessionId=" + sessionId + ", correlatedSessionId=" + correlatedSessionId + ", startTime="
				+ startTime + ", sessionOwnerId=" + sessionOwnerId + ", isMultiFileEditingMode="
				+ isMultiFileEditingMode + ", sessionUsers=" + sessionUsers + ", parameters=" + parameters + "]";
	}

	/**
     * Created by Administrator on 2016/6/18.
     */
    public static enum Status {
        Initial,
        Started,
        Ended,
    }
}
