package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public User getOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.USER_NOT_FOUND));
    }
}
