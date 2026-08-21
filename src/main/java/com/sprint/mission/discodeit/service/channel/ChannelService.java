package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;

import java.util.List;

public interface ChannelService {
    Channel save(PublicChannelCreateRequestDto request);

    Channel save(PrivateChannelCreateRequestDto request);

    List<ChannelResponseDto> findAll();

    ChannelResponseDto find(ChannelIdRequestDto requestDto);

    List<ChannelDto> findAllByUserId(UserIdRequestDto requestDto);

    Channel update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto);

    void delete(ChannelIdRequestDto requestDto);
}
