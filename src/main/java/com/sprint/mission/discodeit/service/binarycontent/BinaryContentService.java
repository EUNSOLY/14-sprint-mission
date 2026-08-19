package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;

import java.util.List;

public interface BinaryContentService {
    BinaryContentResponseDto save(BinaryContentCreateRequestDto requestDto);

    BinaryContentResponseDto find(BinaryContentIdRequestDto requestDto);

    List<BinaryContentResponseDto> findAllByIdIn(List<BinaryContentIdRequestDto> ids);

    void delete(BinaryContentIdRequestDto requestDto);

}
