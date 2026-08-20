package com.sprint.mission.discodeit.controller.binarycotent;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @Operation(summary = "첨부 파일 조회")
    @ApiResponse(
            responseCode = "404",
            description = "첨부 파일을 찾을 수 없음",
            content = @Content(examples = @ExampleObject("BinaryContent with id {binaryContentId} not found"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "첨부 파일 조회 성공",
            content = @Content(schema = @Schema(implementation = BinaryContent.class))
    )
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BinaryContent> getFile(
            @Parameter(description = "조회할 첨부 파일 ID")
            @PathVariable(value = "id") UUID binaryContentId
    ) {
        BinaryContent binaryContentResponse = binaryContentService.find(BinaryContentIdRequestDto.from(binaryContentId));
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentResponse);
    }

    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponse(
            responseCode = "200",
            description = "첨부 파일 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class))
            )
    )
    @RequestMapping(method = RequestMethod.GET, value = "")
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
