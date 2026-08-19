package com.sprint.mission.discodeit.dto.readstatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusUpdateRequestDto {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
}
