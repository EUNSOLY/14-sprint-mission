package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User validateCredentials(LoginRequestDto requestDto) {
        return userRepository.findAll().stream()
                .filter(user -> user.getName().equals(requestDto.getName()))
                .filter(user -> user.getPassword().equals(requestDto.getPassword()))
                .findFirst()
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.INVALID_CREDENTIALS));
    }
}
