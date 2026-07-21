package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void save(Message message);

    Message find(UUID id);

    List<Message> findByUserId(UUID userId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findByChannelIdAndUserId(UUID channelId, UUID userId);


    List<Message> findAll();

    void update(UUID id, Message message);

    void delete(UUID id);
}
