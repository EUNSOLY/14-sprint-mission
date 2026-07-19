package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message find(UUID id) {
        if(!data.containsKey(id)){
            throw new RuntimeException("요청한 데이터가 존재하지 않습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, Message message) {
        if(!data.containsKey(id)){
            throw new RuntimeException("요청한 데이터가 존재하지 않습니다.");
        }
        data.replace(id, message);

    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)){
            throw new RuntimeException("요청한 데이터가 존재하지 않습니다.");
        }
        data.remove(id);
    }
}
