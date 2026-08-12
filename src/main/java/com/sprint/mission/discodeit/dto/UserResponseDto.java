package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatusType;
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
    private final UserStatusType userStatus;
    private final UUID profileId;

    public static UserResponseDto from(User userEntity, UserStatusType status) {
        return new UserResponseDto(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), status, userEntity.getProfileId());
    }
}
