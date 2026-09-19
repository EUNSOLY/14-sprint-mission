package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.entity.message.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, BinaryContentMapper.class})
public abstract class MessageMapper {
    @Mapping(target = "channelId", source = "channel.id")
    public abstract MessageDto toDto(Message message);
}

