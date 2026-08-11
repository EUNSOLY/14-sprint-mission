package com.sprint.mission.discodeit.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor // JavaApplication 테스트를 위해
@ConfigurationProperties(prefix = "discodeit.repository")
public class FileProperties {
    private String fileDirectory;

}
