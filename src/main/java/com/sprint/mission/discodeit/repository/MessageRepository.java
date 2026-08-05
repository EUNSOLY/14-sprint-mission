package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findByUserId(UUID userId);

    List<Message> findByChannelId(UUID channelId);

    List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId);


    List<Message> findAll();

    void update(UUID id, Message message);

    void delete(UUID id);

    void deleteByChannelId(UUID channelId);
}
