package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.service.channel.ChannelService;
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
public class ChannelController implements ChannelControllerDocs {
    private final ChannelService channelService;

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<Channel> createPublicChannel(
            @RequestBody PublicChannelCreateRequestDto request
    ) {
        Channel response = channelService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(response);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<Channel> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequestDto request
    ) {
        Channel response = channelService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<Channel> updatePublicChannel(
            @PathVariable(value = "id") UUID channelId,
            @RequestBody ChannelUpdateRequestDto request
    ) {
        Channel channel = channelService.update(ChannelIdRequestDto.from(channelId), request);
        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteChannel(
            @PathVariable(value = "id") UUID channelId
    ) {
        channelService.delete(ChannelIdRequestDto.from(channelId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAccessibleChannelsByUserId(
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
