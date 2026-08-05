package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequestDto {
    private final Byte[] bytes;

    public BinaryContent toEntity() {
        return new BinaryContent(this.bytes);
    }
}
