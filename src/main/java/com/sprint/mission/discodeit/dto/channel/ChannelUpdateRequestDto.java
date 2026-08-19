package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelUpdateRequestDto {
    private final UUID id;
    private final String name;
    private final String description;
}
