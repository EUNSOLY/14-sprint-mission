package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequestDto {
    private final String fileName;
    private final String contentType;
    private final byte[] bytes;

    public BinaryContent toEntity() {
        return new BinaryContent(this.fileName, this.bytes, this.contentType, (long) this.bytes.length);
    }
}
