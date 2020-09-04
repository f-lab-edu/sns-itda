package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Profile("dev")
public class LocalFileService implements FileService {

    @Value("${local.file.base.directory}")
    private String baseDir;

    @Override
    public FileInfo uploadFile(MultipartFile targetFile) throws FileUploadException {

        String newFileName = changeFileName(targetFile);

        StringBuilder filePath = new StringBuilder();
        filePath.append(baseDir).append("\\").append(newFileName);

        try {
            targetFile.transferTo(new File(String.valueOf(filePath)));
            FileInfo fileInfo = new FileInfo(newFileName, String.valueOf(filePath));

            return fileInfo;
        } catch (IOException e) {
            throw new FileUploadException("파일 업로드에 실패하였습니다.");
        }
    }

    @Override
    public void deleteFile(String filePath) {

        if (filePath != null) {
            new File(filePath).delete();
        }
    }
}
