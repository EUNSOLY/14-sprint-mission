package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateRequestDto {
    private final String content;
    private final UUID channelId;
    private final UUID authorId;


    public Message toEntity() {
        return new Message(this.content, this.authorId, this.channelId);
    }
}
