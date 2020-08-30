package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileInfo uploadFile(MultipartFile file) throws FileUploadException;

    void deleteFile(String filePath);
}
