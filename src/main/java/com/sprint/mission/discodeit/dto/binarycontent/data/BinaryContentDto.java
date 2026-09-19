package com.sprint.mission.discodeit.dto.binarycontent.data;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BinaryContentDto {
    private final UUID id;
    private final String fileName;
    private final String contentType;
    private final Long size;
    private final byte[] bytes;


    public static BinaryContentDto of(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize(),
                binaryContent.getBytes()
        );
    }
}
