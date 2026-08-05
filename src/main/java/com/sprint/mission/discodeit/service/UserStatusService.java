package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    void save(UserStatusCreateRequestDto request);

    UserStatus findById(UUID id);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateRequestDto request);

    UserStatus updateByUserId(UUID userId);
    
    void delete(UUID id);
}
