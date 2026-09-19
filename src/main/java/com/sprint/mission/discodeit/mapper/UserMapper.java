package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {BinaryContentMapper.class})
public interface UserMapper {

    @Mapping(target = "online", source = "userStatus.online")
    UserDto toDto(User user);
}
