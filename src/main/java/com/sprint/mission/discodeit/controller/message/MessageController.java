package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.common.dto.ApiCustomResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentMapper;
import com.sprint.mission.discodeit.service.message.MessageService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Message 생성")
    @ApiResponse(
            responseCode = "404",
            description = "Channel 또는 User를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("Channel | Author with id {channelId | authorId} not found"))
    )
    @ApiResponse(
            responseCode = "201",
            description = "Message가 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Message.class))
    )
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestPart(value = "messageCreateRequest") MessageCreateRequestDto request,

            @Parameter(description = "Message 첨부 파일들")
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        Message savedMessage = messageService.save(request, binaryRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<ApiCustomResponse<Void>> updateMessage(
            @PathVariable(value = "id") UUID messageId,
            @RequestPart(value = "messageInfo") MessageUpdateRequestDto request,
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        messageService.update(request, binaryRequests);
        return ApiCustomResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<ApiCustomResponse<Void>> deleteMessage(
            @PathVariable(value = "id") UUID messageId
    ) {
        messageService.delete(MessageIdRequestDto.from(messageId));
        return ApiCustomResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "Message 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
    )
    @RequestMapping(method = RequestMethod.GET, params = "channelId")
    public ResponseEntity<List<Message>> getMessagesByChannelId(
            @Parameter(description = "조회할 Channel ID")
            @RequestParam("channelId") UUID channelId
    ) {
        List<Message> responses = messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId));

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
