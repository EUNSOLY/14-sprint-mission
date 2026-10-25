package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "discodeit.storage.s3") // @ConfigurationPropertiesScan으로 빈 등록
public class AwsS3Properties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private long presignedUrlExpiration = 600; // 초 단위 (기본값: 10분)
}
