package com.sprint.mission.discodeit.controller.binarycotent;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentController implements BinaryContentControllerDocs {
    private final BinaryContentService binaryContentService;

    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BinaryContent> getFile(
            @Parameter(description = "조회할 첨부 파일 ID")
            @PathVariable(value = "id") UUID binaryContentId
    ) {
        BinaryContent binaryContentResponse = binaryContentService.find(BinaryContentIdRequestDto.from(binaryContentId));
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentResponse);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getFiles(
            @Parameter(description = "조회할 첨부 파일 ID 목록")
            @RequestParam List<UUID> binaryContentIds
    ) {
        List<BinaryContentIdRequestDto> binaryContentIdsDto = binaryContentIds.stream().map(BinaryContentIdRequestDto::from).toList();

        List<BinaryContent> binaryContentResponse = binaryContentService.findAllByIdIn(binaryContentIdsDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContentResponse);
    }
}
