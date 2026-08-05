package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, Channel channel) {
        data.replace(id, channel);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
