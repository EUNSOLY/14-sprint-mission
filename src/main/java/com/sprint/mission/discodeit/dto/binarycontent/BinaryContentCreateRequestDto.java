package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;

public record BinaryContentCreateRequestDto(
        String fileName,
        String contentType,
        byte[] bytes
) {
    public BinaryContent toEntity() {
        return BinaryContent.create(this.fileName, this.bytes, this.contentType, (long) this.bytes.length);
    }
}
