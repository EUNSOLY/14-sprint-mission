package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDto {
    private final String name;
    private final String email;
    private final String password;

    public User toEntity() {
        return new User(this.name, this.email, this.password);
    }
}
