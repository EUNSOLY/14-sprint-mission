package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PublicChannelCreateRequestDto {
    private final String name;
    private final String description;


    public Channel toEntity() {
        return new Channel(ChannelType.PUBLIC, this.name, this.description);
    }
}
