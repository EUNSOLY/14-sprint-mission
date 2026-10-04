package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequestDto(
        String content,

        @NotNull(message = "채널ID는 필수 입니다.")
        UUID channelId,

        @NotNull(message = "유저ID는 필수 입니다.")
        UUID authorId
) {
}
