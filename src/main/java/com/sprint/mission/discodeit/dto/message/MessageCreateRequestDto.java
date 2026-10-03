package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessageCreateRequestDto(
        String content,

        @NotBlank(message = "채널ID는 필수 입니다.")
        UUID channelId,

        @NotBlank(message = "유저ID는 필수 입니다.")
        UUID authorId
) {
}
