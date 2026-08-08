package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentService {
    BinaryContent save(BinaryContentCreateRequestDto requestDto);

    BinaryContent find(BinaryContentIdRequestDto requestDto);

    List<BinaryContent> findAllByIdIn(List<BinaryContentIdRequestDto> ids);

    void delete(BinaryContentIdRequestDto requestDto);

}
