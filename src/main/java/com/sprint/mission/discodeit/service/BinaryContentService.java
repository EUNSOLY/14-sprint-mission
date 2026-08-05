package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent save(BinaryContentCreateRequestDto requestDto);

    BinaryContent find(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    void delete(UUID id);

}
