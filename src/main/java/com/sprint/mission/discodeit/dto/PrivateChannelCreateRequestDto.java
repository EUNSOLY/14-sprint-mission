package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PrivateChannelCreateRequestDto {
    private final List<UUID> userIds;

    public Channel toEntity() {
        return new Channel(ChannelType.PRIVATE, "", "");
    }
}
