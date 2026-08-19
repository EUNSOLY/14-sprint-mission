package com.sprint.mission.discodeit.dto.userstatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatusUpdateRequestDto {
    private final UUID id;
    private final UUID userId;
}
