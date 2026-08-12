package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class BinaryContentIdRequestDto extends IdRequestDto {
    private BinaryContentIdRequestDto(UUID id) {
        super(id);
    }

    public static BinaryContentIdRequestDto from(UUID id) {
        return new BinaryContentIdRequestDto(id);
    }
}
