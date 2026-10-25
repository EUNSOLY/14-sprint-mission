package com.sprint.mission.discodeit.storage.s3;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Tag("s3테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AWSS3Test {
    private static final String TEST_KEY = "test/s3-upload-test.txt";
    private static final String TEST_CONTENT = "Hello AWS S3!";

    private static String accessKey;
    private static String secretKey;
    private static String region;
    private static String bucket;
    private static S3Client s3Client;


    // 테스트 시작 전에 한 번만: .env 읽고 S3Client 생성
    @BeforeAll
    static void setUp() throws IOException {
        Properties properties = new Properties();

        try (FileInputStream envFile = new FileInputStream(".env")) {
            properties.load(envFile);
        }
        accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
        secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
        region = properties.getProperty("AWS_S3_REGION");
        bucket = properties.getProperty("AWS_S3_BUCKET");

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    // 모든 테스트가 끝나면: 테스트 파일 삭제 + 클라이언트 닫기
    @AfterAll
    static void tearDown() {
        if (s3Client == null) {
            return;
        }
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(TEST_KEY)
                .build());
        s3Client.close();
    }


    @Test
    @Order(1) // 기본적으로 테스트 순서 보장하지않아 order 명시
    @DisplayName(".env에서 AWS 정보를 불러온다.")
    void loadEnv_success() {
        assertThat(accessKey).isNotBlank();
        assertThat(secretKey).isNotBlank();
        assertThat(region).isNotBlank();
        assertThat(bucket).isNotBlank();

        log.info("ENV 파일 로드 성공");
    }

    @Test
    @Order(2)
    @DisplayName("파일 업로드 성공")
    void upload_success() {
        // given
        byte[] bytes = TEST_CONTENT.getBytes(StandardCharsets.UTF_8);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(TEST_KEY)
                .contentType("text/plain")
                .build();

        // when
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

        // then
        String downloaded = s3Client.getObjectAsBytes(getObjectRequest()).asUtf8String();
        assertThat(downloaded).isEqualTo(TEST_CONTENT);
    }

    @Test
    @Order(3)
    @DisplayName("파일 다운로드 성공")
    void download_success() throws IOException {
        try (InputStream inputStream = s3Client.getObject(getObjectRequest())) {
            String downloadedContent = new String(inputStream.readAllBytes(),
                    StandardCharsets.UTF_8);
            assertThat(downloadedContent).isEqualTo(TEST_CONTENT);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Presigned URL 생성 성공")
    void generatePresignedUrl_success() {
        try (S3Presigner resigned = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // 10분 유효
                    .getObjectRequest(getObjectRequest())
                    .build();

            PresignedGetObjectRequest presignedRequest = resigned.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            log.info("생성된 Presigned URL: {}", url);
            assertThat(url).contains(bucket).contains(TEST_KEY);
        }
    }


    private static GetObjectRequest getObjectRequest() {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(TEST_KEY)
                .build();
    }
}