package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageResponseDto {
    private final UUID id;
    private final String message;
    private final UUID userId;
    private final UUID channelId;
    private final List<UUID> imageIds;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(message.getId(), message.getMessage(), message.getUserId(), message.getChannelId(), message.getAttachmentIds());
    }
}
