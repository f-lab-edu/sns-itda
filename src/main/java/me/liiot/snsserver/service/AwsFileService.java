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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class AwsFileService implements FileService {

    @Value("${itda.aws.s3.base.url}")
    private String baseUrl;

    @Value("${itda.aws.s3.bucket.name}")
    private String bucket;

    private final S3Client s3Client;

    private final FileMapper fileMapper;

    @Override
    public FileInfo uploadFile(MultipartFile file, String userId) throws FileUploadException {

        String newFileName = FileUtil.changeFileName(file);

        return createFileInfo(file, userId, newFileName);
    }

    @Override
    public List<FileInfo> uploadFiles(List<MultipartFile> files, String userId) throws FileUploadException {

        HashMap<String, String> newFileNames = FileUtil.changeFileNames(files);
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
    @Transactional(readOnly = true)
    public boolean isExistImages(int postId) {

        return fileMapper.isExistImages(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImages(int postId) {

        return fileMapper.getImages(postId);
    }

    @Override
    public void deleteFile(String filePath) {

        if (filePath != null) {
            String key = filePath.substring(baseUrl.length());

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        }
    }

    @Override
    public void deleteDirectory(String userId) throws FileDeleteException {

        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket)
                .prefix(userId)
                .build();

        ListObjectsResponse response = s3Client.listObjects(listObjects);
        List<S3Object> s3Objects = response.contents();

        List<ObjectIdentifier> toDelete = s3Objects.stream()
                .map(object -> ObjectIdentifier.builder().key(object.key()).build())
                .collect(Collectors.toList());

        sendDeleteObjectsRequest(toDelete);
    }

    @Override
    public void deleteImages(int postId) throws FileDeleteException {

        List<String> imagePaths = fileMapper.getImagePaths(postId);

        List<ObjectIdentifier> toDelete = imagePaths.stream()
                .map(path -> ObjectIdentifier.builder().key(path.substring(baseUrl.length())).build())
                .collect(Collectors.toList());

        sendDeleteObjectsRequest(toDelete);

        fileMapper.deleteImages(postId);
    }

    private FileInfo createFileInfo(MultipartFile file, String userId, String newFileName) throws FileUploadException {
        StringBuilder key = new StringBuilder();
        key.append(userId).append("/").append(newFileName);

        try {
            byte[] attachment = file.getBytes();
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(String.valueOf(key))
                            .build(),
                    RequestBody.fromByteBuffer(ByteBuffer.wrap(attachment)));

            URL reportUrl = s3Client.utilities()
                    .getUrl(GetUrlRequest.builder()
                            .bucket(bucket)
                            .key(String.valueOf(key))
                            .build()
                    );

            FileInfo fileInfo = new FileInfo(newFileName, String.valueOf(reportUrl));

            return fileInfo;
        } catch (SdkServiceException | SdkClientException | IOException e) {
            throw new FileUploadException("파일 업로드에 실패했습니다.", e);
        }
    }

    private void sendDeleteObjectsRequest(List<ObjectIdentifier> toDelete) throws FileDeleteException {

        try {
            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3Client.deleteObjects(request);
        } catch (S3Exception e) {
            throw new FileDeleteException("파일을 삭제하는데 실패하였습니다.", e);
        }
    }
}
