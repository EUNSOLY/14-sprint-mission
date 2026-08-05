package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateRequestDto {
    private final String message;
    private final UUID userId;
    private final UUID channelId;
    private final List<Byte[]> files;


    public Message toEntity() {
        return new Message(this.message, this.userId, this.channelId);
    }
}
