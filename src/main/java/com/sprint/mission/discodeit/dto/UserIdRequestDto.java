package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class UserIdRequestDto extends IdRequestDto {
    private UserIdRequestDto(UUID id) {
        super(id);
    }
    
    public static UserIdRequestDto from(UUID id) {
        return new UserIdRequestDto(id);
    }

}
