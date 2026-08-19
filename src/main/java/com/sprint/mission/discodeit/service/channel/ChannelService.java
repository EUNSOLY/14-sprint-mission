package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;

import java.util.List;

public interface ChannelService {
    ChannelResponseDto save(PublicChannelCreateRequestDto request);

    ChannelResponseDto save(PrivateChannelCreateRequestDto request);

    List<ChannelResponseDto> findAll();

    ChannelResponseDto find(ChannelIdRequestDto requestDto);

    List<ChannelResponseDto> findAllByUserId(UserIdRequestDto requestDto);

    void update(ChannelUpdateRequestDto requestDto);

    void delete(ChannelIdRequestDto requestDto);
}
