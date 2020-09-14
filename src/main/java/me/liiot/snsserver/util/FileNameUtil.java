package me.liiot.snsserver.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileNameUtil {

    public static String changeFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        StringBuilder newFileName = new StringBuilder();
        newFileName.append(uuid).append(".").append(extension);

        return String.valueOf(newFileName);
    }
}
