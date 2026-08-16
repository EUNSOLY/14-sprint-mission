package com.sprint.mission.discodeit.controller.binarycotent;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/find")

    public ResponseEntity<ApiResponse<BinaryContentResponseDto>> getFindFile(
            @RequestParam UUID binaryContentId
    ) {
        BinaryContentResponseDto binaryContentResponse = binaryContentService.find(BinaryContentIdRequestDto.from(binaryContentId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, binaryContentResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ApiResponse<BinaryContentResponseDto>> getFile(
            @PathVariable(value = "id") UUID binaryContentId
    ) {
        BinaryContentResponseDto binaryContentResponse = binaryContentService.find(BinaryContentIdRequestDto.from(binaryContentId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, binaryContentResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<ApiResponse<List<BinaryContentResponseDto>>> getFiles(
            @RequestBody List<BinaryContentIdRequestDto> requests
    ) {
        List<BinaryContentResponseDto> binaryContentResponse = binaryContentService.findAllByIdIn(requests);
        return ApiResponse.toSuccess(CustomStatusCode.OK, binaryContentResponse);
    }
}
