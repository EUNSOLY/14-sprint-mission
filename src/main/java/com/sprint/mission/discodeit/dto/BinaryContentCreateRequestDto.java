package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequestDto {
    private final String fileName;
    private final byte[] bytes;

    public BinaryContent toEntity(String path) {
        return new BinaryContent(this.fileName, this.bytes, path);
    }
}
