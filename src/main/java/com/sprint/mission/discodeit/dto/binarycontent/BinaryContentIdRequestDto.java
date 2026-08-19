package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.dto.common.IdRequestDto;

import java.util.UUID;

public class BinaryContentIdRequestDto extends IdRequestDto {
    private BinaryContentIdRequestDto(UUID id) {
        super(id);
    }

    public static BinaryContentIdRequestDto from(UUID id) {
        return new BinaryContentIdRequestDto(id);
    }
}
