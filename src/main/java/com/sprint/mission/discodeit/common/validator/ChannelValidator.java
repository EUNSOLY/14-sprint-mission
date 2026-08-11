package com.sprint.mission.discodeit.common.validator;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelValidator {
    private final ChannelRepository channelRepository;

    public Channel getOrThrow(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다. id=" + id));
    }
}
