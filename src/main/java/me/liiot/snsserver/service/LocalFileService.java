package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.mapper.FileMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Profile("dev")
public class LocalFileService implements FileService {

    @Value("${itda.local.file.base.directory}")
    private String baseDir;

    private FileMapper fileMapper;

    public LocalFileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public FileInfo uploadFile(MultipartFile targetFile,
                               String userId) throws FileUploadException {

        String newFileName = FileNameUtil.changeFileName(targetFile);

        checkDirectory(userId);

        StringBuilder filePath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId)
                .append(File.separator)
                .append(newFileName);

        try {
            targetFile.transferTo(new File(String.valueOf(filePath)));
            FileInfo fileInfo = new FileInfo(newFileName, String.valueOf(filePath));

            return fileInfo;
        } catch (IOException e) {
            throw new FileUploadException("파일을 업로드하는데 실패하였습니다.", e);
        }
    }

    @Override
    public List<FileInfo> uploadFiles(List<MultipartFile> targetFiles,
                                      String userId) throws FileUploadException {

        List<FileInfo> fileInfos = new ArrayList<>();
        HashMap<String, String> newFileNames = FileNameUtil.changeFileNames(targetFiles);

        checkDirectory(userId);

        for (MultipartFile targetFile : targetFiles) {
            StringBuilder filePath = new StringBuilder()
                    .append(baseDir)
                    .append(File.separator)
                    .append(userId)
                    .append(File.separator)
                    .append(newFileNames.get(targetFile.getOriginalFilename()));

            try {
                targetFile.transferTo(new File(String.valueOf(filePath)));
                FileInfo fileInfo = new FileInfo(newFileNames.get(targetFile.getOriginalFilename()),
                        String.valueOf(filePath));

                fileInfos.add(fileInfo);
            } catch (IOException e) {
                throw new FileUploadException("파일을 업로드하는데 실패하였습니다.", e);
            }
        }
        return fileInfos;
    }

    @Override
    public boolean isExistImages(int postId) {

        return fileMapper.isExistImages(postId);
    }

    @Override
    public List<Image> getImages(int postId) {

        return fileMapper.getImages(postId);
    }

    @Override
    public void deleteFile(String filePath) {

        if (filePath != null) {
            new File(filePath).delete();
        }
    }

    @Override
    public void deleteDirectory(String userId) {

        StringBuilder dirPath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId);

        boolean isSuccess = FileSystemUtils.deleteRecursively(new File(String.valueOf(dirPath)));

        if (!isSuccess) {
            throw new FileDeleteException("파일을 삭제하는데 실패하였습니다.");
        }
    }

    private void checkDirectory(String userId) {

        StringBuilder dirPath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId);

        File directory = new File(String.valueOf(dirPath));

        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
