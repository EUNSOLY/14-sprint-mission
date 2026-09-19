package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;

import java.util.List;

public interface ChannelService {
    ChannelDto save(PublicChannelCreateRequestDto request);

    ChannelDto save(PrivateChannelCreateRequestDto request);

    List<ChannelDto> findAll();

    ChannelDto find(ChannelIdRequestDto requestDto);

    List<ChannelDto> findAllByUserId(UserIdRequestDto requestDto);

    ChannelDto update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto);

    void delete(ChannelIdRequestDto requestDto);
}
