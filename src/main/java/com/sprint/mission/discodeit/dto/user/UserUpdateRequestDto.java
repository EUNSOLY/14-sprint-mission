package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserUpdateRequestDto {
    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
}
