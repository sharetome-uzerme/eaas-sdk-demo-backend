package com.xietong.demo.eaas.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by haibin on 2016/9/5.
 */
public interface FileService {
    boolean saveFile(String filePath, MultipartFile fileInfo);
    boolean notifyFileChangeStatus(String fileId, String resourceId, String status);
    File downloadFile(String fileId);
}
