package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {BinaryContentMapper.class})
public interface UserMapper {

    UserDto toDto(User user, boolean online);
}
