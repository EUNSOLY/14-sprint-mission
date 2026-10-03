package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        boolean hasDuplicateName = userRepository.existsByUsername(username);
        
        if (!hasDuplicateName) {
            throw new UserNotFoundException(Map.of("로그인 요청 이름", username));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(Map.of("로그인 요청 이름", username)));


        return userMapper.toDto(user);
    }
}
