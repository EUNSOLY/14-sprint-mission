package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    

    @Override
    public void save(Channel user) {
        channelRepository.save(user);
    }

    @Override
    public Channel find(UUID id) {
        Channel findChannel = channelRepository.findById(id);
        if (Objects.isNull(findChannel)) {
            throw new RuntimeException("찾으시는 채널이 존재하지 않습니다.");
        }

        return findChannel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, Channel user) {
        Channel findChannel = channelRepository.findById(id);
        if (Objects.isNull(findChannel)) {
            throw new RuntimeException("수정 할 채널이 존재하지 않습니다.");
        }
        channelRepository.update(id, user);
    }

    @Override
    public void delete(UUID id) {
        Channel findChannel = channelRepository.findById(id);
        if (Objects.isNull(findChannel)) {
            throw new RuntimeException("삭제 할 채널이 존재하지 않습니다.");
        }
        channelRepository.delete(id);
    }
}
