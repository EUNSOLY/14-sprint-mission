package com.sprint.mission.discodeit.dto.channel.data;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChannelDto {
    UUID id;
    ChannelType type;
    String name;
    String description;
    List<UUID> participantIds;
    Instant lastMessageAt;

    public static ChannelDto of(Channel channel, List<UUID> participantIds, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }
}
