package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageUpdateRequestDto {
    private final UUID id;
    private final String message;
    private final UUID userId;
    private final UUID channelId;
    private final List<UUID> deleteFileIds;
    private final List<Byte[]> files;
}
