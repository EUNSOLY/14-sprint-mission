package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface ChannelService {
    ChannelResponseDto savePublicChannel(PublicChannelCreateRequestDto request);

    ChannelResponseDto savePrivateChannel(PrivateChannelCreateRequestDto request);

    ChannelResponseDto find(ChannelIdRequestDto requestDto);

    List<ChannelResponseDto> findAllByUserId(UserIdRequestDto requestDto);

    void update(ChannelUpdateRequestDto requestDto);

    void delete(ChannelIdRequestDto requestDto);
}
