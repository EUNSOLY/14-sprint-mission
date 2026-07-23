package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    Message findById(UUID id);

    List<Message> findByUserId(UUID userId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findByChannelIdAndUserId(UUID channelId, UUID userId);


    List<Message> findAll();

    void update(UUID id, Message message);

    void delete(UUID id);
}
