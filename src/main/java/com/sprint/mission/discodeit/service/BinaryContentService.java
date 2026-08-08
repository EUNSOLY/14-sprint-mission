package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;

import java.util.List;

public interface BinaryContentService {
    BinaryContentResponseDto save(BinaryContentCreateRequestDto requestDto);

    BinaryContentResponseDto find(BinaryContentIdRequestDto requestDto);

    List<BinaryContentResponseDto> findAllByIdIn(List<BinaryContentIdRequestDto> ids);

    void delete(BinaryContentIdRequestDto requestDto);

}
