package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.AwsS3Properties;
import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {
    private static final String BUCKET = "test-bucket";

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private S3BinaryContentStorage storage;

    @BeforeEach
    void setUp() {
        AwsS3Properties properties = new AwsS3Properties();
        properties.setAccessKey("access");
        properties.setSecretKey("secret");
        properties.setBucket(BUCKET);
        properties.setRegion("ap-northeast-2");
        storage = new S3BinaryContentStorage(properties, s3Client, s3Presigner);
    }

    @Test
    @DisplayName("파일 업로드 성공")
    void put_success() {
        UUID key = UUID.randomUUID();
        byte[] content = "Hello AWS S3!".getBytes();

        UUID result = storage.put(key, content);

        assertThat(result).isEqualTo(key);
        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture(), any(RequestBody.class));
        assertThat(captor.getValue().bucket()).isEqualTo(BUCKET);
        assertThat(captor.getValue().key()).isEqualTo(key.toString());
    }

    @Test
    @DisplayName("파일 조회 성공")
    void get_success() {
        UUID key = UUID.randomUUID();
        @SuppressWarnings("unchecked")
        ResponseInputStream<GetObjectResponse> stream = mock(ResponseInputStream.class);
        doReturn(stream).when(s3Client).getObject(any(GetObjectRequest.class));

        InputStream result = storage.get(key);

        assertThat(result).isSameAs(stream);
        ArgumentCaptor<GetObjectRequest> captor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client).getObject(captor.capture());
        assertThat(captor.getValue().bucket()).isEqualTo(BUCKET);
        assertThat(captor.getValue().key()).isEqualTo(key.toString());
    }

    @Test
    @DisplayName("파일 삭제 성공")
    void delete_success() {
        UUID key = UUID.randomUUID();

        storage.delete(key);

        ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(captor.capture());
        assertThat(captor.getValue().bucket()).isEqualTo(BUCKET);
        assertThat(captor.getValue().key()).isEqualTo(key.toString());
    }

    @Test
    @DisplayName("파일 다운로드 시 Presigned URL로 리다이렉트한다")
    void download_success() throws MalformedURLException {
        UUID key = UUID.randomUUID();
        String presignedUrl = "https://test-bucket.s3.ap-northeast-2.amazonaws.com/" + key + "?X-Amz-Signature=abc";
        PresignedGetObjectRequest presigned = mock(PresignedGetObjectRequest.class);
        given(presigned.url()).willReturn(new URL(presignedUrl));
        given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).willReturn(presigned);
        BinaryContentDto dto = new BinaryContentDto(key, "test.txt", "text/plain", 13L);

        ResponseEntity<?> response = storage.download(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo(presignedUrl);

        ArgumentCaptor<GetObjectPresignRequest> captor = ArgumentCaptor.forClass(GetObjectPresignRequest.class);
        verify(s3Presigner).presignGetObject(captor.capture());
        assertThat(captor.getValue().getObjectRequest().bucket()).isEqualTo(BUCKET);
        assertThat(captor.getValue().getObjectRequest().key()).isEqualTo(key.toString());
    }
}
