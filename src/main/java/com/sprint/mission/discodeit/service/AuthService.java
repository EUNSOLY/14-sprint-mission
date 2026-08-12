package com.sprint.mission.discodeit.service;

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
                .orElseThrow(() -> new RuntimeException("이름 또는 패스워드가 일치하지않습니다."));
    }
}
