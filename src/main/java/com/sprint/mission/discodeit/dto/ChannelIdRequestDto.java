package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class ChannelIdRequestDto extends IdRequestDto {
    private ChannelIdRequestDto(UUID id) {
        super(id);
    }

    public static ChannelIdRequestDto from(UUID id) {
        return new ChannelIdRequestDto(id);
    }
}
