package com.sprint.mission.discodeit.dto.userstatus.data;

import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserStatusDto {
    private final UUID id;
    private final UUID userId;
    private final Instant lastActiveAt;

    public static UserStatusDto to(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUser().getId(), userStatus.getLastActiveAt());
    }
}
