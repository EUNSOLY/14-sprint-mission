package com.sprint.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "수정할 Channel 정보")
public class ChannelUpdateRequestDto {
    private final String newName;
    private final String newDescription;
}
