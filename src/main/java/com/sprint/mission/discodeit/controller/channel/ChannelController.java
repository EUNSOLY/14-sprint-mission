package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.service.channel.ChannelService;
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
@RequestMapping(value = "/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "Public Channel 생성")
    @ApiResponse(
            responseCode = "201",
            description = "Public Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<Channel> createPublicChannel(
            @RequestBody PublicChannelCreateRequestDto request
    ) {
        Channel response = channelService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Private Channel 생성")
    @ApiResponse(
            responseCode = "201",
            description = "Private Channel이 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<Channel> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequestDto request
    ) {
        Channel response = channelService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Channel 정보 수정")
    @ApiResponse(
            responseCode = "404",
            description = "Channel을 찾을 수 없음",
            content = @Content(examples = @ExampleObject("Channel with id {channelId} not found"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Private Channel은 수정할 수 없음",
            content = @Content(examples = @ExampleObject("Private channel cannot be updated"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Channel 정보가 성공적으로 수정됨",
            content = @Content(schema = @Schema(implementation = Channel.class))
    )
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<Channel> updatePublicChannel(
            @Parameter(description = "수정할 Channel ID")
            @PathVariable(value = "id") UUID channelId,
            @RequestBody ChannelUpdateRequestDto request
    ) {
        Channel channel = channelService.update(ChannelIdRequestDto.from(channelId), request);
        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    @Operation(summary = "Channel 삭제")
    @ApiResponse(
            responseCode = "404",
            description = "Channel을 찾을 수 없음",
            content = @Content(examples = @ExampleObject("Channel with id {channelId} not found"))
    )
    @ApiResponse(
            responseCode = "204",
            description = "Channel이 성공적으로 삭제됨"
    )
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/channels/{id}")
    public ResponseEntity<Void> deleteChannel(
            @Parameter(description = "삭제할 Channel ID")
            @PathVariable(value = "id") UUID channelId
    ) {
        channelService.delete(ChannelIdRequestDto.from(channelId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "Channel 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
    )
    @RequestMapping(method = RequestMethod.GET, params = "userId")
    public ResponseEntity<List<ChannelDto>> findAccessibleChannelsByUserId(
            @Parameter(description = "조회할 User ID")
            @RequestParam("userId") UUID userId
    ) {
        List<ChannelDto> response = channelService.findAllByUserId(UserIdRequestDto.from(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<ApiCustomResponse<List<ChannelResponseDto>>> getChannels() {
//        List<ChannelResponseDto> channelResponse = channelService.findAll();
//
//        return ApiCustomResponse.toSuccess(CustomStatusCode.OK, channelResponse);
//    }
}
