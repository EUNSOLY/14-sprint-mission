package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusMapper {

    public UserStatusDto toDto(UserStatus userStatus) {
        return UserStatusDto.to(userStatus);
    }

}
