package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel find(UUID id) {
        if (!data.containsKey(id)) {
            return null;
        }
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, Channel channel) {
        if (!data.containsKey(id)) {
            throw new RuntimeException("요청한 데이터가 존재하지 않습니다.");
        }
        data.replace(id, channel);

    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new RuntimeException("요청한 데이터가 존재하지 않습니다.");
        }
        data.remove(id);
    }
}
