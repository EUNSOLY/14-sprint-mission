package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDto(
        List<@NotNull(message = "잘못된 ID로 요청되었습니다.") UUID> participantIds
) {
}
