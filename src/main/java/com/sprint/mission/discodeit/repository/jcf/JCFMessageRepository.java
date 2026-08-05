package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }


    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, Message message) {
        data.replace(id, message);

    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        this.data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> this.data.remove(message.getId()));
    }
}
