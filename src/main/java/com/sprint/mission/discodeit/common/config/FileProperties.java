package com.sprint.mission.discodeit.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor // JavaApplication 테스트를 위해
@ConfigurationProperties(prefix = "discodeit.repository") // 환경변수 기본 경로
public class FileProperties {
    private String fileDirectory; // key-value : key 값을 기준으로 value 매칭

}
