package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
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
