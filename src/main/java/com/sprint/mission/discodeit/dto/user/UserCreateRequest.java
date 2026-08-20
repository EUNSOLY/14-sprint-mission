package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.user.User;

public record UserCreateRequest(String username, String email, String password) {
    public User toEntity() {
        return new User(this.username, this.email, this.password);
    }
}
