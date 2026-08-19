package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.common.IdRequestDto;

import java.util.UUID;

public class UserIdRequestDto extends IdRequestDto {
    private UserIdRequestDto(UUID id) {
        super(id);
    }

    public static UserIdRequestDto from(UUID id) {
        return new UserIdRequestDto(id);
    }

}
