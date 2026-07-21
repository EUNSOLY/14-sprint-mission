package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(
            UserService userService,
            ChannelService channelService
    ){
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public void save(Message message) {
        if(Objects.isNull(this.userService.find(message.getUserId()))){
            throw new RuntimeException("잘못된 사용자 ID 입니다. 확인 해주세요. 메세지 내용 : " + message.getMessage());
        }

        if(Objects.isNull(this.channelService.find(message.getChannelId()))){
            throw new RuntimeException("잘못된 사용자 ID 입니다. 확인 해주세요. 메세지 내용 : " + message.getMessage());
        }

        data.put(message.getId(), message);
    }

    @Override
    public Message find(UUID id) {
        if(!data.containsKey(id)){
            return null;
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
