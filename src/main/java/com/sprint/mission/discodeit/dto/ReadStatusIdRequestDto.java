package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class ReadStatusIdRequestDto extends IdRequestDto {
    private ReadStatusIdRequestDto(UUID id) {
        super(id);
    }

    public static ReadStatusIdRequestDto from(UUID id) {
        return new ReadStatusIdRequestDto(id);
    }
}
