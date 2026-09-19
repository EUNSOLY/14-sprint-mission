package com.sprint.mission.discodeit.dto.readstatus.data;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadStatusDto {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;


    public static ReadStatusDto to(ReadStatus readStatus, UUID user, UUID channel) {
        return new ReadStatusDto(
                readStatus.getId(),
                user,
                channel,
                readStatus.getLastReadAt()
        );
    }
}
