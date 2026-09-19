package com.sprint.mission.discodeit.dto.readstatus.data;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id, UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public static ReadStatusDto to(ReadStatus readStatus, UUID user, UUID channel) {
        return new ReadStatusDto(
                readStatus.getId(),
                user,
                channel,
                readStatus.getLastReadAt()
        );
    }
}
