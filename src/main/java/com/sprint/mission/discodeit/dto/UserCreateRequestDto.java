package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDto {
    private final String name;
    private final String email;
    private final String password;
    private final BinaryContentCreateRequestDto profile;

    public User toEntity() {
        return new User(this.name, this.email, this.password);
    }
}
