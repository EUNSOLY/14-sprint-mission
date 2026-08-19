package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;

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
