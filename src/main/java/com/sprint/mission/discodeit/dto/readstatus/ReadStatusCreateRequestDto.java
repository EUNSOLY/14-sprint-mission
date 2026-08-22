package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusCreateRequestDto {
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;

    public ReadStatus toEntity() {
        return new ReadStatus(this.userId, this.channelId);
    }
}
