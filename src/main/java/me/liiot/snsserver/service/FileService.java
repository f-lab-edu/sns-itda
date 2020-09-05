package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.EXTENSION_SEPARATOR_STR;

public interface FileService {

    FileInfo uploadFile(MultipartFile file, String userId) throws FileUploadException;

    void deleteFile(String filePath);

    void deleteDirectory(String userId);

    default String changeFileName(MultipartFile file) {
        String uuid =  UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        StringBuilder newFileName = new StringBuilder();
        newFileName.append(uuid).append(EXTENSION_SEPARATOR_STR).append(extension);

        return String.valueOf(newFileName);
    }
}
