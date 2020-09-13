package me.liiot.snsserver.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FileNameUtil {

    public static String changeFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        StringBuilder newFileName = new StringBuilder();
        newFileName.append(uuid).append(".").append(extension);

        return String.valueOf(newFileName);
    }

    public static HashMap<String, String> changeFileNames(List<MultipartFile> files) {

        HashMap<String, String> newFileNames = new HashMap<>();

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

            StringBuilder newFileName = new StringBuilder();
            newFileName.append(uuid).append(".").append(extension);

            newFileNames.put(file.getOriginalFilename(), String.valueOf(newFileName));
        }
        return newFileNames;
    }
}
