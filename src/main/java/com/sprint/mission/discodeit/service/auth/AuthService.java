package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.user.User;

public interface AuthService {
    User login(LoginRequest requestDto);
}
