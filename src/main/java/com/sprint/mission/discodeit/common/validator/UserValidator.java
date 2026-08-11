package com.sprint.mission.discodeit.common.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public User getOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다. id=" + id));
    }
}
