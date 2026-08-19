package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentResponseDto {
    private final UUID id;
    private final String fileName;
    private final byte[] bytes;
    private final String contentType;
    private final Long size;

    private final Instant createdAt;
    private final Instant updatedAt;

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getBytes(),
                binaryContent.getContentType(),
                binaryContent.getSize(),
                binaryContent.getCreatedAt(),
                binaryContent.getUpdatedAt()
        );
    }
}
