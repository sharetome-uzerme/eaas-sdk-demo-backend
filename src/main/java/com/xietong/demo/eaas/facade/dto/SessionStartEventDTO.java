package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SessionStartEventDTO {
    private String appId;
    private String fileId;
    private String fileVersion;
    private String fileName;
    private String filePath;
    private String userId;
    private String groupId;
	private String appUrl;
    private Boolean isReadOnlyMode = true;
    private Boolean canSaveFile = false;
    private Boolean canExportFile = false;
    private Boolean canCopyPasteOut = false;
    private Boolean isDFSMode = false;


    public SessionStartEventDTO() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsReadOnlyMode() {
        return isReadOnlyMode;
    }

    public void setIsReadOnlyMode(Boolean isReadOnlyMode) {
        this.isReadOnlyMode = isReadOnlyMode;
    }

    public Boolean getIsDFSMode() {
        return isDFSMode;
    }

    public void setIsDFSMode(Boolean isDFSMode) {
        this.isDFSMode = isDFSMode;
    }

    public Boolean getCanSaveFile() {
        return canSaveFile;
    }

    public void setCanSaveFile(Boolean canSaveFile) {
        this.canSaveFile = canSaveFile;
    }

    public Boolean getCanExportFile() {
        return canExportFile;
    }

    public void setCanExportFile(Boolean canExportFile) {
        this.canExportFile = canExportFile;
    }

    public Boolean getCanCopyPasteOut() {
        return canCopyPasteOut;
    }

    public void setCanCopyPasteOut(Boolean canCopyPasteOut) {
        this.canCopyPasteOut = canCopyPasteOut;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public Boolean getIsMultiFileEditingMode() {
        return getIsDFSMode() && !getIsReadOnlyMode();
    }

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	@Override
    public String toString() {
        return "SessionStartEventDTO{" +
                "appId='" + appId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileVersion='" + fileVersion + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", isReadOnlyMode=" + isReadOnlyMode +
                ", isDFSMode=" + isDFSMode +
                ", canSaveFile=" + canSaveFile +
                ", canExportFile=" + canExportFile +
                ", canCopyPasteOut=" + canCopyPasteOut +
                '}';
    }
}
