package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelValidator {
    private final ChannelRepository channelRepository;

    public Channel getOrThrow(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("채널이 존재하지 않습니다. id : {}", id);
                    return new ChannelNotFoundException(Map.of("요청 채널 ID", id));
                });

    }
}
