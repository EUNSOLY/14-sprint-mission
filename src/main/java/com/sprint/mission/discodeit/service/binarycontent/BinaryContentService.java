package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;

import java.util.List;

public interface BinaryContentService {
    BinaryContent save(BinaryContentCreateRequestDto requestDto);

    BinaryContent find(BinaryContentIdRequestDto requestDto);

    List<BinaryContent> findAllByIdIn(List<BinaryContentIdRequestDto> ids);

    void delete(BinaryContentIdRequestDto requestDto);

}
