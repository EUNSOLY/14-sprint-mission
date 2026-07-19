package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public interface MessageService {
    void save(Message message);
    Message find(UUID id);
    List<Message> findAll();
    void update(UUID id, Message message);
    void delete(UUID id);
}
