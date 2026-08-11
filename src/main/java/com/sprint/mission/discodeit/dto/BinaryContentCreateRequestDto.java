package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequestDto {
    private String fileName;
    private byte[] bytes;

    public BinaryContent toEntity(String path) {
        return new BinaryContent(this.fileName, this.bytes, path);
    }
}
