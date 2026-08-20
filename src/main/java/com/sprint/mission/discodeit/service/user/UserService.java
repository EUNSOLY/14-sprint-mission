package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;

import java.util.List;

public interface UserService {
    User save(
            UserCreateRequest requestDto,
            BinaryContentCreateRequestDto profileCreateRequest
    );

    UserDto find(UserIdRequestDto requestDto);

    List<UserDto> findAll();

    User update(
            UserIdRequestDto userId, UserUpdateRequest userUpdateRequest,
            BinaryContentCreateRequestDto profileCreateRequest
    );

    void delete(UserIdRequestDto requestDto);

    UserStatus updateUserOnlineStatus(UserIdRequestDto requestDto);
}
