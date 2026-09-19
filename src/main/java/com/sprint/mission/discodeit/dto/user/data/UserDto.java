package com.sprint.mission.discodeit.dto.user.data;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDto {
    UUID id;
    String username;
    String email;
    BinaryContentDto profile;
    Boolean online;

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
