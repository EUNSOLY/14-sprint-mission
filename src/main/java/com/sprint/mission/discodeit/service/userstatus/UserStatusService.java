package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;

import java.util.List;

public interface UserStatusService {
    void save(UserStatusCreateRequestDto request);

    UserStatus findById(UserStatusIdRequestDto requestDto);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateRequestDto request);

    UserStatus updateByUserId(UserIdRequestDto requestDto);

    void delete(UserStatusIdRequestDto requestDto);
}
