package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    void save(UserCreateRequestDto requestDto);

    UserResponseDto find(UserIdRequestDto requestDto);

    List<UserResponseDto> findAll();

    void update(UserUpdateRequestDto updateRequestDto);

    void delete(UserIdRequestDto requestDto);
}
