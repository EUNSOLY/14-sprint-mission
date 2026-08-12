package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class MessageIdRequestDto extends IdRequestDto {
    private MessageIdRequestDto(UUID id) {
        super(id);
    }

    public static MessageIdRequestDto from(UUID id) {
        return new MessageIdRequestDto(id);
    }
}
