package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.mapper.FileMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.model.post.ImageUploadInfo;
import me.liiot.snsserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class LocalFileService implements FileService {

    @Value("${itda.local.file.base.directory}")
    private String baseDir;

    private final FileMapper fileMapper;

    @Override
    public FileInfo uploadFile(MultipartFile file,
                               String userId) throws FileUploadException {

        String newFileName = FileUtil.changeFileName(file);

        checkDirectory(userId);

        return createFileInfo(file, userId, newFileName);
    }

    @Override
    public List<FileInfo> uploadFiles(List<MultipartFile> files,
                                      String userId) throws FileUploadException {

        HashMap<String, String> newFileNames = FileUtil.changeFileNames(files);

        checkDirectory(userId);

        List<FileInfo> fileInfos = files.stream()
                .map(file ->
                    createFileInfo(file, userId, newFileNames.get(file.getOriginalFilename())))
                .collect(Collectors.toList());

        return fileInfos;
    }

    @Override
    public void uploadImage(int postId, FileInfo fileInfo) {

        ImageUploadInfo imageUploadInfo =
                FileUtil.toImageUploadInfo(postId, fileInfo, 1);

        fileMapper.insertImage(imageUploadInfo);
    }

    @Override
    public void uploadImages(int postId, List<FileInfo> fileInfos) {

        List<ImageUploadInfo> imageUploadInfos = fileInfos.stream()
                .map(info -> FileUtil.toImageUploadInfo(postId, info, fileInfos.indexOf(info) + 1))
                .collect(Collectors.toList());

        fileMapper.insertImages(imageUploadInfos);
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

    @Override
    public void deleteImages(int postId) {
        List<String> imagePaths = fileMapper.getImagePaths(postId);

        imagePaths.stream().forEach(this::deleteFile);

        fileMapper.deleteImages(postId);
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

    private FileInfo createFileInfo(MultipartFile file, String userId, String newFileName) {
        StringBuilder filePath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId)
                .append(File.separator)
                .append(newFileName);

        try {
            file.transferTo(new File(String.valueOf(filePath)));
            FileInfo fileInfo = new FileInfo(newFileName, String.valueOf(filePath));

            return fileInfo;
        } catch (IOException e) {
            throw new FileUploadException("파일을 업로드하는데 실패하였습니다.", e);
        }
    }
}
