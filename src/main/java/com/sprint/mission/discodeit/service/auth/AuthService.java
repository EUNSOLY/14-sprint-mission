package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;

public interface AuthService {
    UserDto login(LoginRequest requestDto);
}
