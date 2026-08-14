package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {
    private final UUID id;
    private final String name;
    private final String email;
    private final boolean userStatus;
    private final UUID profileId;

    public static UserResponseDto from(User userEntity,
                                       boolean userStatus
    ) {
        return new UserResponseDto(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userStatus, userEntity.getProfileId());
    }
}
