package com.sprint.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Channel 정보")
public record ChannelUpdateRequestDto(
        @Size(max = 100, message = "채널명은 100자리 이하만 가능합니다.")
        String newName,
        @Size(max = 500, message = "채널설명은 500자리 이하만 가능합니다.")
        String newDescription
) {
}
