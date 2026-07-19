package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.*;

public interface ChannelService {
    void save(Channel channel);
    Channel find(UUID id);
    List<Channel> findAll();
    void update(UUID id, Channel channel);
    void delete(UUID id);
}
