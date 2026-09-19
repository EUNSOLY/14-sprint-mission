package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user) {
        boolean online = Optional.ofNullable(user.getUserStatus())
                .map(UserStatus::isOnline)
                .orElse(false);

        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfile());


        return UserDto.of(user, binaryContentDto, online);
    }
}
