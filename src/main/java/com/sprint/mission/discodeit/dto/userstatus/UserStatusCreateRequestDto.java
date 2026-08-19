package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatusCreateRequestDto {
    private final UUID userId;

    public UserStatus toEntity() {
        return new UserStatus(this.userId);
    }
}
