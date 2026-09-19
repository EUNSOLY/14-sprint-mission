package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserStatusMapper {


    @Mapping(target = "userId", source = "user.id")
    UserStatusDto toDto(UserStatus userStatus);

}
