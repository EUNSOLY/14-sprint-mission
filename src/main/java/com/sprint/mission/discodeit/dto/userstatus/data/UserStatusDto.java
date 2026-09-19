package com.sprint.mission.discodeit.dto.userstatus.data;

import com.sprint.mission.discodeit.entity.userstatus.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id, UUID userId, Instant lastActiveAt) {
    public static UserStatusDto to(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUser().getId(), userStatus.getLastActiveAt());
    }
}
