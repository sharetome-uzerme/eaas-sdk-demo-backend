package com.xietong.demo.eaas.service.impl;

import com.xietong.demo.eaas.domain.exception.CreateSessionFailException;
import com.xietong.demo.eaas.facade.dto.*;
import com.xietong.demo.eaas.service.EAASService;
import com.xietong.phoenix.eaas.service.api.IEAASServiceAPI;
import com.xietong.phoenix.eaas.service.api.IEAASServiceAPIResult;
import com.xietong.phoenix.eaas.service.api.builder.session.CloseSessionAPIBuilder;
import com.xietong.phoenix.eaas.service.api.builder.session.CreateSessionAPIBuilder;
import com.xietong.phoenix.eaas.service.api.builder.session.JoinSessionAPIBuilder;
import com.xietong.phoenix.eaas.service.core.caller.EAASServiceAPIResult;
import com.xietong.phoenix.eaas.service.core.caller.param.EAASServiceAPICallParam;
import com.xietong.phoenix.eaas.service.core.common.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/14.
 */
@Service
public class EAASServiceImpl implements EAASService{
    private final static Logger logger = Logger.getLogger(EAASServiceImpl.class);

    @Value("${eaas.config.server.dns}")
    private String eaasServerDomain;

    final boolean  isReadOnlyMode = false;      // user open file with read auth only
    final boolean canSaveFile = true;          // user open app can save file
    final boolean canExportFile = false;      // user open app can save as (to new file)
    final boolean canCopyPasteOut = false;    // user open app can copy (paste out content)
    final boolean isDFSMode = false;           // user open app DFS depend on (tenant and app config)

    @Autowired
    IEAASServiceAPI eaasServiceAPIRestProxy;

    public SessionInfoDTO createEaaSSession(CreateSessionForOpenAppDTO request) {
        SessionStartEventDTO sessionStartEventDTO = buildCreateSessionDTO(request);

        CreateSessionAPIBuilder builder = new CreateSessionAPIBuilder()
                .resourceIp(eaasServerDomain)
                .appId(sessionStartEventDTO.getAppId())
                .userId(sessionStartEventDTO.getUserId())
                .groupId(sessionStartEventDTO.getGroupId())
                .fileId(sessionStartEventDTO.getFileId())
                .fileVersion(sessionStartEventDTO.getFileVersion())
                .fileName(sessionStartEventDTO.getFileName())
                .filePath(sessionStartEventDTO.getFilePath())
                .appUrl(sessionStartEventDTO.getAppUrl())
                .isReadOnlyMode(sessionStartEventDTO.getIsReadOnlyMode())
                .isDFSMode(sessionStartEventDTO.getIsDFSMode())
                .canSaveFile(sessionStartEventDTO.getCanSaveFile())
                .canExportFile(sessionStartEventDTO.getCanExportFile())
                .canCopyPasteOut(sessionStartEventDTO.getCanCopyPasteOut());

        IEAASServiceAPIResult<String> result = eaasServiceAPIRestProxy.callApi(builder.build());
        return parseResponse(eaasServerDomain + Constants.EAAS_SESSIONMGR_URL, (EAASServiceAPIResult) result);
    }

    public SessionInfoDTO joinEaaSSession(SessionJoinEventDTO event) {
        logger.info("joinEaaSSession.event=" + event);

        EAASServiceAPICallParam param = new JoinSessionAPIBuilder()
                .resourceIp(eaasServerDomain)
                .sessionId(event.getSessionId())
                .userId(event.getUserId())
                .build();

        IEAASServiceAPIResult<String> result = eaasServiceAPIRestProxy.callApi(param);

        return parseResponse(eaasServerDomain + Constants.EAAS_SESSIONMGR_URL, (EAASServiceAPIResult) result);
    }

    private SessionInfoDTO parseResponse(String sessionUrl, EAASServiceAPIResult result) {
        SessionInfoDTO sessionInfoDTO = new SessionInfoDTO();
        if (result.isSuccess()) {
            Map<String, Object> map = (Map<String, Object>) result.getData();
            sessionInfoDTO.setSessionId((String) map.get("sessionId"));
            sessionInfoDTO.setGwHost((String) map.get("gwHost"));
            sessionInfoDTO.setGwPort((Integer) map.get("gwPort"));
            sessionInfoDTO.setMcuUrl((String) map.get("mcuUrl"));
            sessionInfoDTO.setReadOnly((Boolean) map.get("readOnly"));
            sessionInfoDTO.setToken(result.getToken());
            sessionInfoDTO.setSessionMgrUrl(sessionUrl);
            return sessionInfoDTO;
        }
        throw new CreateSessionFailException(Integer.valueOf(result.getErrorCode()), result.getMessage());
    }

    private SessionStartEventDTO buildCreateSessionDTO(CreateSessionForOpenAppDTO request) {
        // create EAAS session
        SessionStartEventDTO sessionStartEventDTO = new SessionStartEventDTO();

        sessionStartEventDTO.setAppId(request.getAppId());
        sessionStartEventDTO.setUserId(request.getUserId());

        sessionStartEventDTO.setIsReadOnlyMode(isReadOnlyMode); // user open file with read auth only
        sessionStartEventDTO.setCanSaveFile(canSaveFile);  // user open app can save file
        sessionStartEventDTO.setCanExportFile(canExportFile);// user open app can save as (to new file)
        sessionStartEventDTO.setCanCopyPasteOut(canCopyPasteOut);// user open app can copy (paste out content)
        sessionStartEventDTO.setIsDFSMode(isDFSMode);// user open app DFS depend on (tenant and app config)

        if (request instanceof CreateSessionForOpenFileWithAppDTO) {
            sessionStartEventDTO.setFileId(((CreateSessionForOpenFileWithAppDTO) request).getFileId());
            String fileName = ((CreateSessionForOpenFileWithAppDTO) request).getFileName();
            sessionStartEventDTO.setFileName(fileName);
            sessionStartEventDTO.setIsReadOnlyMode(((CreateSessionForOpenFileWithAppDTO) request).getReadOnly());
            sessionStartEventDTO.setCanSaveFile(canSaveFile);
            sessionStartEventDTO.setCanExportFile(canExportFile);
            sessionStartEventDTO.setCanCopyPasteOut(canCopyPasteOut);
        }

        return sessionStartEventDTO;
    }

    @Override
    public boolean forceCloseEAASSession(ForceCloseSessionEventDTO event) {
        logger.info("forceCloseEAASSession.event=" + event);
        IEAASServiceAPIResult result = eaasServiceAPIRestProxy.callApi(
                new CloseSessionAPIBuilder()
                        .resourceIp(eaasServerDomain)
                        .sessionId(event.getSessionId())
                        .build());
        return result.isSuccess();
    }
}
