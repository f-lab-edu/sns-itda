package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {

    FileInfo uploadFile(MultipartFile file, String userId) throws FileUploadException;

    void deleteFile(String filePath);

    void deleteDirectory(String userId);

    /*
    FileService 인터페이스를 구현하는 모든 클래스에서 중복 파일명, 한글 파일명과 같은 문제로
    changeFileName() 메소드를 사용하고 있습니다. 그러나 각 클래스마다 changeFileName 로직이
    같아 중복되는 코드가 발생했기 때문에 이를 제거하고자 인터페이스에서 default 메소드로 정의했습니다.
     */
    default String changeFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        StringBuilder newFileName = new StringBuilder();
        newFileName.append(uuid).append(".").append(extension);

        return String.valueOf(newFileName);
    }
}
