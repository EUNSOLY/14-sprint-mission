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
    private UUID id;
    private String fileName;
    private String contentType;
    private Long size;


    public static BinaryContentDto of(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize()
        );
    }
}
