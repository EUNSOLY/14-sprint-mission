package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PrivateChannelCreateRequestDto {
    private final List<UUID> participantIds;

    public Channel toEntity() {
        return new Channel(ChannelType.PRIVATE, "", "");
    }
}
