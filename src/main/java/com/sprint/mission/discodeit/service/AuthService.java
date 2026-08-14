package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
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
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.INVALID_CREDENTIALS));
    }
}
