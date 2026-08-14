package com.sprint.mission.discodeit.common.validator;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.entity.User;
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
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.USER_NOT_FOUND, String.format("id = %s", id)));
    }
}
