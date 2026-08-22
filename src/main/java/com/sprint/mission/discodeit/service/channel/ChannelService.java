package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;

import java.util.List;

public interface ChannelService {
    Channel save(PublicChannelCreateRequestDto request);

    Channel save(PrivateChannelCreateRequestDto request);

    List<ChannelDto> findAll();

    ChannelDto find(ChannelIdRequestDto requestDto);

    List<ChannelDto> findAllByUserId(UserIdRequestDto requestDto);

    Channel update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto);

    void delete(ChannelIdRequestDto requestDto);
}
