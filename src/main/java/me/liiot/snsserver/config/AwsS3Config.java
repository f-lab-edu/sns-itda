package me.liiot.snsserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("prod")
@PropertySource("classpath:/s3-secret.properties")
public class AwsS3Config {
    @Value("${aws.s3.region}")
    String region;

    @Value("${aws.iam.accessKeyId}")
    String accessKeyId;

    @Value("${aws.iam.secretAccessKey}")
    String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(this.region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();

        return s3Client;
    }
}
