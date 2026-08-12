package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusResponseDto {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadMessageAt;

    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return new ReadStatusResponseDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadMessageAt());
    }

}
