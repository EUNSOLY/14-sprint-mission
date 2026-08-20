package com.sprint.mission.discodeit.dto.user.data;

import com.sprint.mission.discodeit.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;
    String username;
    String email;
    UUID profileId;
    Boolean online;

    public static UserDto of(User user, boolean online) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
