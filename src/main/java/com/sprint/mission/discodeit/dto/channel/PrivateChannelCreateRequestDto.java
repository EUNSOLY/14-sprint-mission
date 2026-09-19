package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PrivateChannelCreateRequestDto {
    private final List<UUID> participantIds;

}
