package com.sprint.mission.discodeit.entity.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Role {
    ADMIN,
    CHANNEL_MANAGER,
    USER
}
