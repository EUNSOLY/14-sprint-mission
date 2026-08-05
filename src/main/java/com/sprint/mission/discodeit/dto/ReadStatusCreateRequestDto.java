package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusCreateRequestDto {
    private final UUID userId;
    private final UUID channelId;

    public ReadStatus toEntity() {
        return new ReadStatus(this.userId, this.channelId);
    }
}
