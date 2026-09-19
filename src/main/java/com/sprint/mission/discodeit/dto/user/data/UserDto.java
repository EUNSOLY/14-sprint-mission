package com.sprint.mission.discodeit.dto.user.data;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.user.User;

import java.util.UUID;

public record UserDto(UUID id, String username, String email, BinaryContentDto profile, Boolean online) {
    public static UserDto of(User user, BinaryContentDto binaryContentDto, boolean online) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentDto,
                online
        );
    }
}
