package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    FileInfo uploadFile(MultipartFile file, String userId) throws FileUploadException;

    List<FileInfo> uploadFiles(List<MultipartFile> files, String userId) throws FileUploadException;

    void uploadImage(int postId, FileInfo fileInfo);

    void uploadImages(int postId, List<FileInfo> fileInfos);

    void deleteFile(String filePath);

    void deleteDirectory(String userId);

}
