package com.sprint.mission.discodeit.dto.message.data;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.message.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id, Instant createdAt, Instant updatedAt,
        String content, UserDto author, UUID channelId,
        List<BinaryContentDto> attachments
) {
    public static MessageDto to(Message message, UserDto user, UUID channelId, List<BinaryContentDto> binaryContents) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                user,
                channelId,
                binaryContents
        );
    }
}
