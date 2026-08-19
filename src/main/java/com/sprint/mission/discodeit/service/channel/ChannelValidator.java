package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelValidator {
    private final ChannelRepository channelRepository;

    public Channel getOrThrow(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

    }
}
