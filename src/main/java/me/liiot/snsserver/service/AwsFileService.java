package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.FileInfo;
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

@Service
@Profile("prod")
public class AwsFileService implements FileService {

    @Value("${aws.s3.bucket.name}")
    String bucket;

    @Autowired
    S3Client s3Client;

    @Override
    public FileInfo uploadFile(MultipartFile file) throws FileUploadException {

        String key = changeFileName(file);

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

            FileInfo fileInfo = new FileInfo(key, String.valueOf(reportUrl));

            return fileInfo;
        } catch (SdkServiceException | SdkClientException | IOException e) {
            throw new FileUploadException("파일 업로드에 실패했습니다.");
        }
    }

    @Override
    public void deleteFile(String filePath) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .build());
    }
}
