package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void save(UserCreateRequestDto requestDto);

    UserResponseDto find(UUID id);

    List<UserResponseDto> findAll();

    void update(UserUpdateRequestDto updateRequestDto);

    void delete(UUID id);
}
