package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/channels/public")
    public ResponseEntity<ApiResponse<ChannelResponseDto>> createPublicChannel(
            @RequestBody PublicChannelCreateRequestDto request
    ) {
        ChannelResponseDto responseDto = channelService.save(request);
        return ApiResponse.toSuccess(CustomStatusCode.OK, responseDto);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/channels/private")
    public ResponseEntity<ApiResponse<ChannelResponseDto>> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequestDto request
    ) {
        ChannelResponseDto channelResponse = channelService.save(request);
        return ApiResponse.toSuccess(CustomStatusCode.OK, channelResponse);

    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/api/channels/public/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePublicChannel(
            @PathVariable(value = "id") UUID channelId,
            @RequestBody ChannelUpdateRequestDto request
    ) {
        channelService.update(request);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/channels/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChannel(
            @PathVariable(value = "id") UUID channelId
    ) {
        channelService.delete(ChannelIdRequestDto.from(channelId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/channels", params = "userId")
    public ResponseEntity<ApiResponse<List<ChannelResponseDto>>> findAccessibleChannelsByUserId(
            @RequestParam("userId") UUID userId
    ) {
        List<ChannelResponseDto> channelResponse = channelService.findAllByUserId(UserIdRequestDto.from(userId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, channelResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/channels")
    public ResponseEntity<ApiResponse<List<ChannelResponseDto>>> getChannels() {
        List<ChannelResponseDto> channelResponse = channelService.findAll();

        return ApiResponse.toSuccess(CustomStatusCode.OK, channelResponse);
    }
}
