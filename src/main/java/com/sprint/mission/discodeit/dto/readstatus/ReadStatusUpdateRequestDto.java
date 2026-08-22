package com.sprint.mission.discodeit.dto.readstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
@Schema(description = "수정할 읽음 상태 정보")
public class ReadStatusUpdateRequestDto {
    private final Instant newLastReadAt;
}
