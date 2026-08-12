package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface UserService {
    void save(
            UserCreateRequestDto requestDto,
            BinaryContentCreateRequestDto optionalProfileCreateRequest
    );

    UserResponseDto find(UserIdRequestDto requestDto);

    List<UserResponseDto> findAll();

    void update(
            UserUpdateRequestDto updateRequestDto,
            BinaryContentCreateRequestDto optionalProfileCreateRequest
    );

    void delete(UserIdRequestDto requestDto);

    void updateUserOnlineStatus(UserIdRequestDto requestDto);
}
