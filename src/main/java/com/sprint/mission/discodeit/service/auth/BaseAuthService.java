package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseAuthService implements AuthService {
    private final UserRepository userRepository;

    public User login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        boolean hasDuplicateName = userRepository.existsByName(username);
        if (!hasDuplicateName) {
            throw new GlobalCustomException(CustomStatusCode.USER_NOT_FOUND);
        }

        return userRepository.findByUsername(username).orElseThrow(() -> new GlobalCustomException(CustomStatusCode.USER_NOT_FOUND));
    }
}
