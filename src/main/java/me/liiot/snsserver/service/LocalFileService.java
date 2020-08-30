package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class LocalFileService implements FileService {

    private static final String BASE_DIR = "C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images";

    @Override
    public FileInfo uploadFile(MultipartFile targetFile) throws FileUploadException {

        String newFileName = changeFileName();
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append(BASE_DIR).append("\\").append(newFileName);

        try {
            targetFile.transferTo(new File(String.valueOf(filePathBuilder)));
            FileInfo fileInfo = new FileInfo(newFileName, String.valueOf(filePathBuilder));

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

    private String changeFileName() {
        return UUID.randomUUID().toString();
    }
}
