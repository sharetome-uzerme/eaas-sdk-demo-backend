package com.xietong.demo.eaas.facade.dto;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CreateSessionForOpenFileWithAppDTO extends CreateSessionForOpenAppDTO {
	private String fileId;
	private String fileName;

    private Boolean readOnly = true;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
    public String toString() {
        return "CreateSessionForOpenFileWithAppDTO{" +
                "fileId=" + fileId +
                ", appId='" + appId + '\'' +
                '}';
    }
}
