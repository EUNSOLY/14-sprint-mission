package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ChannelResponseDto createPublicChannel(
            @RequestBody PublicChannelCreateRequestDto request
    ) {
        return channelService.savePublicChannel(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ChannelResponseDto createPrivateChannel(
            @RequestBody PrivateChannelCreateRequestDto request
    ) {
        return channelService.savePrivateChannel(request);
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public void updatePublicChannel(
            @PathVariable(value = "id") UUID channelId,
            @RequestBody ChannelUpdateRequestDto request
    ) {
        channelService.update(request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteChannel(
            @PathVariable(value = "id") UUID channelId
    ) {
        channelService.delete(ChannelIdRequestDto.from(channelId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public List<ChannelResponseDto> findAccessibleChannelsByUserId(
            @PathVariable(value = "id") UUID userId
    ) {
        return channelService.findAllByUserId(UserIdRequestDto.from(userId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ChannelResponseDto> getChannels() {
        return channelService.findAll();
    }
}
