package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor()
public class ChannelMapper {
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel) {
        Instant messageLastTime = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);

        List<ReadStatus> readStatusList = readStatusRepository.findByChannelId(channel.getId());
        List<UserDto> userDtos = readStatusList.stream().map(readStatus -> userMapper.toDto(readStatus.getUser())).toList();

        return ChannelDto.of(channel, userDtos, messageLastTime);
    }
}
