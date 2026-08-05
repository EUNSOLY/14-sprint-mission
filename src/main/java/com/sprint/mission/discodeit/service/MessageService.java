package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void save(MessageCreateRequestDto requestDto);

    Message find(UUID id);

    List<Message> findByUserId(UUID userId);

    List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId);

    List<Message> findallByChannelId(UUID channelId);

    void update(MessageUpdateRequestDto request);

    void delete(UUID id);
}
