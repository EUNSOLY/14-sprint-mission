package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent save(BinaryContentCreateRequestDto requestDto);

    BinaryContent find(BinaryContentIdRequestDto requestDto);

    List<BinaryContent> findAllByIdIn(List<BinaryContentIdRequestDto> ids);

    void delete(BinaryContentIdRequestDto requestDto);

    ResponseEntity<?> findFile(UUID fileId);
}
