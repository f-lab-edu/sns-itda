package me.liiot.snsserver.util;

import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.ImageUploadInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FileUtil {

    public static String changeFileName(MultipartFile file) {

        return String.valueOf(createNewFileName(file));
    }

    public static HashMap<String, String> changeFileNames(List<MultipartFile> files) {

        HashMap<String, String> newFileNames = new HashMap<>();

        for (MultipartFile file : files) {
            newFileNames.put(file.getOriginalFilename(), String.valueOf(createNewFileName(file)));
        }
        return newFileNames;
    }

    public static ImageUploadInfo toImageUploadInfo(int postId, FileInfo fileInfo, int seq) {
        return new ImageUploadInfo(postId, fileInfo.getFileName(), fileInfo.getFilePath(), seq);
    }

    private static StringBuilder createNewFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        StringBuilder newFileName = new StringBuilder()
                .append(uuid)
                .append(".")
                .append(extension);

        return newFileName;
    }
}
