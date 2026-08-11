package com.sprint.mission.discodeit.common.validator;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentValidator {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent getOrThrow(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다. id=" + id));
    }
}
