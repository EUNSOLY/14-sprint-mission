package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
// 이렇게 한개의 DTO로 하게되면 사용자에게 빈값의 필드를 보내게 되는상황인데 따로둬야할까...
public class ChannelResponseDto {
    private final UUID id;
    private final String name;
    private final String description;
    private final ZonedDateTime lastMessageAt;
    private final List<UUID> userIds;

    // 비공개 채널
    public static ChannelResponseDto privateFrom(Channel channel, Instant messageLastTime, List<UUID> userIds) {
        ZonedDateTime messageTime = getMessageTime(messageLastTime);
        return new ChannelResponseDto(channel.getId(), "", "", messageTime, userIds);
    }

    // 공개 채널
    public static ChannelResponseDto publicFrom(Channel channel, Instant messageLastTime) {
        ZonedDateTime messageTime = getMessageTime(messageLastTime);
        return new ChannelResponseDto(channel.getId(), channel.getName(), channel.getDescription(), messageTime, null);
    }

    // 날짜 변환
    private static ZonedDateTime getMessageTime(Instant messageLastTime) {
        if (Objects.isNull(messageLastTime)) {
            return null;
        }

        return messageLastTime.atZone(ZoneId.of("Asia/Seoul")); // 서버위치?값을 쓸수있지않을까 상수말공..?
    }

}
