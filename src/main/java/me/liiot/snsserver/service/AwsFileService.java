package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.mapper.FileMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Profile("prod")
public class AwsFileService implements FileService {

    @Value("${itda.aws.s3.base.url}")
    private String baseUrl;

    @Value("${itda.aws.s3.bucket.name}")
    private String bucket;

    private S3Client s3Client;

    private FileMapper fileMapper;

    public AwsFileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Autowired
    public AwsFileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public FileInfo uploadFile(MultipartFile file, String userId) throws FileUploadException {

        String newFileName = FileNameUtil.changeFileName(file);
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

    @Override
    public List<FileInfo> uploadFiles(List<MultipartFile> files, String userId) throws FileUploadException {

        List<FileInfo> fileInfos = new ArrayList<>();
        HashMap<String, String> newFileNames = FileNameUtil.changeFileNames(files);

        int filesLen = files.size();
        for (int i=0; i<filesLen; i++) {

            String newFileName = newFileNames.get(files.get(i).getOriginalFilename());
            StringBuilder key = new StringBuilder();
            key.append(userId).append("/").append(newFileName);

            try {
                byte[] attachment = files.get(i).getBytes();
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
                fileInfos.add(fileInfo);
            } catch (SdkServiceException | SdkClientException | IOException e) {
                throw new FileUploadException("파일 업로드에 실패했습니다.", e);
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
            String key = filePath.substring(baseUrl.length());

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        }
    }

    @Override
    public void deleteDirectory(String userId) {
        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket)
                .prefix(userId)
                .build();

        ListObjectsResponse response = s3Client.listObjects(listObjects);
        List<S3Object> s3Objects = response.contents();

        ArrayList<ObjectIdentifier> toDelete = new ArrayList<ObjectIdentifier>();

        for (S3Object object: s3Objects) {
            toDelete.add(ObjectIdentifier.builder().key(object.key()).build());
        }

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
