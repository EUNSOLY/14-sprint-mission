package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void save(Channel channel);

    void savePublicChannel(PublicChannelCreateRequestDto request);

    void savePrivateChannel(PrivateChannelCreateRequestDto request);

    ChannelResponseDto find(UUID id);

    List<ChannelResponseDto> findAllByUserId(UUID userId);

    void update(ChannelUpdateRequestDto requestDto);

    void delete(UUID id);
}
