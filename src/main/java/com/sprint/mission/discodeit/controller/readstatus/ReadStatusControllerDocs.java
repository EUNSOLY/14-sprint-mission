package com.sprint.mission.discodeit.controller.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ReadStatusControllerDocs {
    @Operation(summary = "Message 읽음 상태 생성")
    @ApiResponse(
            responseCode = "404",
            description = "Channel 또는 User를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("Channel | User with id {channelId | userId} not found"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Channel 또는 User를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("ReadStatus with userId {userId} and channelId {channelId} already exists"))
    )
    @ApiResponse(
            responseCode = "201",
            description = "ChanChannelMessage 읽음 상태가 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = ReadStatus.class))
    )
    ResponseEntity<ReadStatus> createMessageReadStatus(ReadStatusCreateRequestDto request);

    @Operation(summary = "Message 읽음 상태 수정")
    @ApiResponse(
            responseCode = "404",
            description = "Message 읽음 상태를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("ReadStatus with id {readStatusId} not found"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Message 읽음 상태가 성공적으로 수정됨",
            content = @Content(schema = @Schema(implementation = ReadStatus.class))
    )
    ResponseEntity<ReadStatus> updateMessageReadStatus(
            @Parameter(description = "수정할 읽음 상태 ID")
            UUID readStatusId,
            ReadStatusUpdateRequestDto requestDto
    );

    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "Message 읽음 상태 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))
    )
    ResponseEntity<List<ReadStatus>> findAllByUserId(
            @Parameter(description = "조회할 User ID")
            UUID userId
    );

}
