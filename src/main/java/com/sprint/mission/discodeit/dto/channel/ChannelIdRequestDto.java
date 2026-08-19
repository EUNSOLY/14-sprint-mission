package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.common.IdRequestDto;

import java.util.UUID;

public class ChannelIdRequestDto extends IdRequestDto {
    private ChannelIdRequestDto(UUID id) {
        super(id);
    }

    public static ChannelIdRequestDto from(UUID id) {
        return new ChannelIdRequestDto(id);
    }
}
