package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.AwsS3Properties;
import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

/**
 * BinaryContent(프로필 이미지, 첨부파일 등) 파일을 AWS S3에 저장하는 구현체.
 * [S3 기본 개념]
 * - S3(Simple Storage Service): AWS가 제공하는 파일 저장소. 서버 디스크 대신 인터넷상의 저장소에 파일을 보관한다.
 * discodeit.storage.type=s3 일 때만 빈으로 등록되며, 그 외(local)에는 LocalBinaryContentStorage가 사용
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {
    private final AwsS3Properties awsS3Properties;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;


    @Override
    public UUID put(UUID key, byte[] value) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsS3Properties.getBucket())
                .key(key.toString())
                .build();
        try {
            s3Client.putObject(request, RequestBody.fromBytes(value));
        } catch (S3Exception e) {
            log.debug("S3 파일 저장 중 발생 예외 FileId : {}", key);
            throw new RuntimeException("S3 파일 저장 중 오류 발생", e);
        }
        log.info("S3 파일 업로드 완료 FileId : {}, size : {}", key, value.length);
        return key;
    }

    @Override
    public void delete(UUID key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(awsS3Properties.getBucket())
                .key(key.toString())
                .build();
        try {
            s3Client.deleteObject(request);
        } catch (S3Exception e) {
            log.debug("S3 파일 삭제 중 발생 예외 FileId : {}", key);
            throw new RuntimeException("S3 파일 삭제 중 오류 발생", e);
        }
    }


    @Override
    public InputStream get(UUID key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(awsS3Properties.getBucket())
                .key(key.toString())
                .build();
        try {
            return s3Client.getObject(request);
        } catch (S3Exception e) {
            log.debug("S3 파일을 불러오는 도중 발생 예외 FileId : {}", key);
            throw new RuntimeException("S3 파일을 불러오는 도중 예외가 발생했습니다.", e);
        }
    }

    // 파일 다운로드: 서버가 파일을 직접 내려주지 않고, Presigned URL로 리다이렉트시킨다.
    @Override
    public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
        String url = generatePresignedUrl(binaryContentDto.id().toString(), binaryContentDto.contentType());
        log.info("S3 파일 다운로드 리다이렉트 FileId : {}", binaryContentDto.id());
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    /**
     * 다운로드용 Presigned URL을 생성한다.
     * - 서버의 키로 서명한 임시 URL이라, 이 URL만 있으면 비공개 버킷의 파일도 받을 수 있다.
     * - 만료 시간(기본 600초)이 지나면 사용할 수 없다.
     * - 서명은 서버 안에서 계산하므로 S3에 요청을 보내지 않는다.
     *
     * @param key         다운로드할 객체 이름 (BinaryContent의 ID)
     * @param contentType 응답에 지정할 Content-Type (예: image/png)
     */
    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsS3Properties.getBucket())
                .key(key)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(awsS3Properties.getPresignedUrlExpiration()))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
