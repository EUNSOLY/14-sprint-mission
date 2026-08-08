package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;

public interface UserStatusService {
    void save(UserStatusCreateRequestDto request);

    UserStatus findById(UserStatusIdRequestDto requestDto);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateRequestDto request);

    UserStatus updateByUserId(UserIdRequestDto requestDto);

    void delete(UserStatusIdRequestDto requestDto);
}
