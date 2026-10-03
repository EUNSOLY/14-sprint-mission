package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentValidator {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent getOrThrow(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new BinaryContentNotFoundException(Map.of("파일 ID", id)));
    }
}
