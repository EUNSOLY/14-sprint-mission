package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.message.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageControllerDocs {
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
    @RequestBody(content = @Content(encoding = @Encoding(name = "messageCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
    ResponseEntity<Message> create(
            MessageCreateRequestDto request,
            @Parameter(description = "Message 첨부 파일들")
            List<MultipartFile> contentFiles
    ) throws IOException;

    @Operation(summary = "Message 내용 수정")
    @ApiResponse(
            responseCode = "404",
            description = "Message를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("Message with id {messageId} not found"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Message가 성공적으로 수정됨",
            content = @Content(schema = @Schema(implementation = Message.class))
    )
    ResponseEntity<Message> updateMessage(
            @Parameter(description = "수정할 Message ID")
            UUID messageId,
            MessageUpdateRequestDto request
    ) throws IOException;

    @Operation(summary = "Message 삭제")
    @ApiResponse(
            responseCode = "404",
            description = "Message를 찾을 수 없음"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User를 찾을 수 없음",
            content = @Content(schema = @Schema(examples = "User with id {userId} not found"))
    )
    ResponseEntity<Void> deleteMessage(
            @Parameter(description = "삭제할 Message ID")
            UUID messageId
    );

    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "Message 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
    )
    ResponseEntity<List<Message>> getMessagesByChannelId(
            @Parameter(description = "조회할 Channel ID")
            UUID channelId
    );
}
