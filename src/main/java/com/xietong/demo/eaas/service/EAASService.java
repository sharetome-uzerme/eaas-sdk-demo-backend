package com.xietong.demo.eaas.service;

import com.xietong.demo.eaas.facade.dto.CreateSessionForOpenAppDTO;
import com.xietong.demo.eaas.facade.dto.ForceCloseSessionEventDTO;
import com.xietong.demo.eaas.facade.dto.SessionInfoDTO;
import com.xietong.demo.eaas.facade.dto.SessionJoinEventDTO;

/**
 * Created by Administrator on 2018/8/14.
 */
public interface EAASService {
    SessionInfoDTO createEaaSSession(CreateSessionForOpenAppDTO request);
    SessionInfoDTO joinEaaSSession(SessionJoinEventDTO event);
    boolean forceCloseEAASSession(ForceCloseSessionEventDTO event);
}
