package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private static JCFMessageRepository INSTANCE;
    private final Map<UUID, Message> data = new HashMap<>();

    private JCFMessageRepository() {
    }

    // 싱글턴
    public static JCFMessageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JCFMessageRepository();
        }
        return INSTANCE;
    }


    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
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
}
