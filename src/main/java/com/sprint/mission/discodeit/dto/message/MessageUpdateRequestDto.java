package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageUpdateRequestDto {
    private final String newContent;
}
