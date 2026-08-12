package com.sprint.mission.discodeit.controller.binarycotent;

import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/files")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public BinaryContentResponseDto getFile(
            @PathVariable(value = "id") UUID binaryContentId
    ) {
        return binaryContentService.find(BinaryContentIdRequestDto.from(binaryContentId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<BinaryContentResponseDto> getFiles(
            @RequestBody List<BinaryContentIdRequestDto> requests
    ) {
        return binaryContentService.findAllByIdIn(requests);
    }
}
