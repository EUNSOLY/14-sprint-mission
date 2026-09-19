package com.sprint.mission.discodeit.dto.message.data;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.message.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDto {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String content;
    private final UserDto author;
    private final UUID channelId;
    private final List<BinaryContentDto> attachments;

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
