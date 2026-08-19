package com.sprint.mission.discodeit.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {
    private final String name;
    private final String password;
}
