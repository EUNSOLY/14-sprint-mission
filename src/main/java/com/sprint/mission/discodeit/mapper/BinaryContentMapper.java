package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

    public BinaryContentDto toDto(BinaryContent binaryContent) {
        if (binaryContent == null) {
            return null;
        }
        return BinaryContentDto.of(binaryContent);
    }
}
