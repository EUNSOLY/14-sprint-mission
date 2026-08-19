package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentValidator {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent getOrThrow(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CONTENT_FILE_NOT_FOUND));
    }
}
