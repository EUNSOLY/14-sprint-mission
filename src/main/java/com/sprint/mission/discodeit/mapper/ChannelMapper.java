package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class ChannelMapper {
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected ReadStatusRepository readStatusRepository;
    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "participants", expression = "java(getParticipants(channel))")
    @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel))")
    public abstract ChannelDto toDto(Channel channel);

    protected Instant getLastMessageAt(Channel channel) {
        return messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);
    }

    protected List<UserDto> getParticipants(Channel channel) {
        List<ReadStatus> readStatusList = readStatusRepository.findByChannelId(channel.getId());
        return readStatusList.stream().map(readStatus -> userMapper.toDto(readStatus.getUser())).toList();
    }
}

